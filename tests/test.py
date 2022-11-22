import requests
import time
import random
scores = []


url = 'http://119.209.77.170:5000/upload'
files = {'file': open('./tests/coke.jpg', 'rb')}

for i in range(100):
    r = requests.post(url, files=files)
    try:
        if '[SUCCESS]model' in r.text:
            scores.append(1)
            print(f"{i}th request")
        else:
            scores.append(-1)
    except Exception as e:
        print("ERROR!", r.text, e)
        scores.append(-1)
    time.sleep(random.random()/5)

c = 0
e = 0
for s in scores:
    if s == -1:
        e += 1 
    elif s != 1:
        c += 1
print("Wrong count", c)
print("error count", e)
