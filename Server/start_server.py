from flask_ngrok import run_with_ngrok
from flask import Flask, request
import darknet.yolo_api as yolo
import os

app = Flask(__name__)
run_with_ngrok(app)   #starts ngrok when the app is run

@app.route("/")
def home():
    return "<h1> <개발자가 상팔자> </h1>"


@app.route("/file_upload", methods=['GET', 'POST'])
def post():
  if (request.method=='POST'):
    f = request.files['file']
    f.save(f'uploads/test.jpg')
    return yolo.detect(net, cls, colors, os.getcwd())

if __name__ == "__main__":
  net, cls, colors = yolo.load_model()
  app.run()