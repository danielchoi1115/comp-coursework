import os
from flask import Flask, request, url_for, jsonify 
import matplotlib.pyplot as plt
import cv2
#from keras.models import load_model
import tensorflow as tf
import keras.applications.xception as xception
from keras_preprocessing.image import img_to_array, load_img
import numpy as np
from flask_cors import CORS
from datetime import datetime
from methodType import methodDic

LABELS = ['battery', 'can', 'clothes', 'electronics', 'glass', 'light', 'paper', 'plastic', 'polystyrene', 'trash', 'vinyls']
LABELS_dic = {"battery": 0, "vinyls": 1, "polystyrene":2, "clothes": 3, "glass": 4, "trash": 5, "electronics": 6, "light":7, "paper": 8, "can": 9, "plastic":10}
CURRENT_TIME = datetime.now()    



app = Flask(__name__)
CORS(app)
app.config['UPLOAD_FOLDER'] = 'static/uploads'
MODEL = tf.keras.models.load_model("model_all.h5")

@app.route('/', methods = ['GET'])
def index():
    return 'hello'

@app.route('/upload', methods = ['POST'])
def upload():

    str_time = CURRENT_TIME.strftime("%y%m%d%H%M%S")

    result = {
        "statusCode": 200,
        "responseMessage": "[SUCCESS]model",
        "data": None
    }

    file = request.files['file']
    filename = str_time + file.filename
    file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
    #print("!!!"+filename)
    img_src = url_for('static', filename = 'uploads/' + filename)
    #print("!!!1"+img_src)

    img = cv2.imread(img_src, cv2.IMREAD_COLOR)

    img = load_img(img_src[1:], target_size=(320,320))
    img_array = img_to_array(img)
    img_array = tf.expand_dims(img_array, 0) # Create a batch
    predictions = MODEL.predict(img_array)
    score = tf.nn.softmax(predictions[0])
    final_score = 100 * np.max(score)

    if final_score > 19:
        name = LABELS[np.argmax(score)]
        code = LABELS_dic[name]
        result["data"] = {"name":name, "code": code}
        result["score"] = round(final_score, 2) 
    else:
        result["statusCode"] = 204
        result["data"] = "Unable to classify"


    return jsonify(result)

@app.route('/method', methods = ['GET'])
def methodType():
    recyclingCode = request.args.get('code')

    result = {
        "statusCode": 200,
        "responseMessage": "[SUCCESS]method",
        "data": None
    }

    if methodDic == None or methodDic[recyclingCode] == None:
        result["statusCode"]: 204
        result["responseMessage"] = "[FAIL]method"
        return result
    
    result["data"] = methodDic[recyclingCode]

    return jsonify(result)


if __name__ == "__main__":
    app.run("0.0.0.0",port=5000, debug=True)
