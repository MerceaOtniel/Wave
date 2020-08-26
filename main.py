import os
import re
import time
import cv2
import argparse
import functools
import subprocess
from concurrent.futures import ThreadPoolExecutor
from collections import deque
from functools import partial

from flask import Flask
from flask import request
from flask import jsonify
import numpy as np
from PIL import Image
import moviepy.editor as mpy
import torchvision
import torch.nn.parallel
import torch.optim
from models import TSN
from ops import transforms
from torch.nn import functional as F
import torch
from threading import Lock, Thread
from threading import Thread
tzeny_queue_lock = Lock()

app = Flask(__name__)
# URL = 'http://192.168.6.13:8080/video'
URL = 'http://localhost:8080/video'

# Enable cudnn benchmark
torch.backends.cudnn.benchmark = True

# Parse arguments
parser = argparse.ArgumentParser()
parser.add_argument('--modality', type=str, default='RGB')
parser.add_argument('--rendered_output', type=str, default=None)
parser.add_argument('--arch', type=str, default="BNInception")
parser.add_argument('--input_size', type=int, default=224)
parser.add_argument('--test_segments', type=int, default=8)
parser.add_argument('--img_feature_dim', type=int, default=256)
parser.add_argument('--consensus_type', type=str, default='TRNmultiscale')
parser.add_argument('--weights', type=str, default="data/jester_inception.pth")
args = parser.parse_args()

categories_file = 'data/jester_categories.txt'
categories = [line.rstrip() for line in open(categories_file, 'r').readlines()]
num_class = len(categories)

a_t = time.time()

model_count = 6

# Load model
nets = []
for i in range(model_count):
    net = TSN(num_class, args.test_segments, args.modality, args.arch,
            consensus_type=args.consensus_type, img_feature_dim=args.img_feature_dim)
    checkpoint = torch.load(args.weights)
    base_dict = {'.'.join(k.split('.')[1:]): v for k, v in list(checkpoint['state_dict'].items())}
    net.load_state_dict(base_dict)
    net.eval()
    net.half().cuda()

    nets.append(net)

    print(f'Loaded model {i} in {time.time() - a_t}')

# Initialize frame transforms.
transform = torchvision.transforms.Compose([
    transforms.GroupOverSample(net.input_size, net.scale_size),
    transforms.Stack(roll=(args.arch in ['BNInception'])),
    transforms.ToTorchFormatTensor(div=(args.arch not in ['BNInception'])),
    transforms.GroupNormalize(net.input_mean, net.input_std),
])
black_background = np.zeros((200, 768, 3), np.uint8)
font = cv2.FONT_HERSHEY_SIMPLEX
bottomLeftCornerOfText = (50, 100)
fontScale = 1
fontColor = (255, 255, 255)
lineType = 2
batch_size = 1

def get_prediction(net, frames):
    data = transform(frames)
    input = data.view(-1, 240, data.size(1), data.size(2)).unsqueeze(0).half().cuda()

    with torch.no_grad():
        logits = net(input)
        h_x = torch.mean(F.softmax(logits, 1), dim=0).data
        probs, idx = h_x.sort(0, True)

    prediction = categories[idx[0]]
    print(prediction, probs[0].item())

    if probs[0] > 0.6:
        global tzeny_queue
        with tzeny_queue_lock:
            tzeny_queue.append(prediction)

current_prediction = 0
tzeny_queue = []
@app.route('/', methods=['GET', 'POST'])
def hello_world():
    global tzeny_queue

    if request.method == 'GET':
        with tzeny_queue_lock:
            returnable = tzeny_queue
            tzeny_queue = []
        
        return jsonify({'queue': returnable})

def loop_run_inference(net):
    print("Initializing webcam.")
    cap = cv2.VideoCapture(URL)

    current_batch = deque([],maxlen=batch_size * 8)
    fps_interval = 24
    current_frame = 0
    dropped_frame_count = 0

    inference_jobs = [None] * model_count
    with ThreadPoolExecutor(model_count) as executor:
        while True:
            if current_frame == 0:
                start_time = time.time() # start time of the loop
            
            ret, frame = cap.read()

            if not ret:
                continue
                
            frame = cv2.resize(frame, (256, 256))
            #### ROTATE #### 
            (h, w) = frame.shape[:2]
            center = (w / 2, h / 2)
            M = cv2.getRotationMatrix2D(center, 90, 1.0)
            rotated90 = cv2.warpAffine(frame, M, (h, w))
            frame = rotated90

            # cv2.imshow('frame', frame)
            current_batch.append(Image.fromarray(frame))
            if len(current_batch) == batch_size * 8:
                job_submitted = False
                for index, inference_job in enumerate(inference_jobs):
                    if inference_job is None or inference_job.done():
                        # print(f'Submitting job to nets[{index}]')
                        inference_jobs[index] = executor.submit(get_prediction, nets[index], current_batch)
                        job_submitted = True
                        break
                if not job_submitted:
                    dropped_frame_count += 1

            current_frame += 1
            if current_frame == fps_interval:
                current_frame = 0
                print("FPS: ", fps_interval / (time.time() - start_time), " dropped frames ", dropped_frame_count) # FPS = 1 / time to process loop
                dropped_frame_count = 0

            if cv2.waitKey(1) & 0xFF == ord('q'):
                break

    cap.release()
    cv2.destroyAllWindows()

if __name__ == '__main__':
    print('Starting flask server')
    flask_run = partial(app.run, host='0.0.0.0')
    thread = Thread(target = flask_run)
    thread.start()

    print('Starting inference loop')
    loop_run_inference(net)
    # thread = Thread(target = loop_run_inference, args=(net))
    # thread.start()

    
