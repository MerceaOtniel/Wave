from flask import Flask
from flask import request
from flask import jsonify

app = Flask(__name__)
current_prediction = 0
tzeny_queue = []
@app.route('/', methods=['GET', 'POST'])
def hello_world():
    global tzeny_queue
    if request.method == 'GET':
        returnable = tzeny_queue
        tzeny_queue = []
        return jsonify({'queue': returnable})
    elif request.method == 'POST':
        tzeny_queue.append(request.args.get('prediction'))
        return "OK"

if __name__ == '__main__':
    app.run()