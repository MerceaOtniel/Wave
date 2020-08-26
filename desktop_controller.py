from pynput.mouse import Button, Controller
import requests
mouse = Controller()
while True:
    x = requests.get("http://192.168.6.42:5000/")
    queue = x.json()['queue']
    if len(queue) == 0:
        continue
    print(queue)
    for action in queue:
        if action == 'Swiping Left':
            print('move mouse left')
            mouse.move(-50, 0)
        if action == 'Swiping Right':
            print('move mouse right')
            mouse.move(50, 0)
        if action == 'Swiping Down':
            print('move mouse down')
            mouse.move(0, 50)
        if action == 'Swiping Up':
            print('move mouse up')
            mouse.move(0, -50)
        if action == 'Sliding Two Fingers Up':
            print('scroll mouse up')
            mouse.scroll(0, 5)
        if action == 'Sliding Two Fingers Down':
            print('scroll mouse down')
            mouse.scroll(0, -5)
        if action == 'Thumb Up':
            print('click right')
            mouse.click(Button.right, 1)
        if action == 'Thumb Down':
            print('click left')
            mouse.click(Button.left, 1)
