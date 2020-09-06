import cv2
from PIL import Image
from yolo import YOLO
import numpy as np

yolo = YOLO(model_path='keras_yolo3/model_data/square_tiny2.h5', classes_path='classes.txt', anchors_path='keras_yolo3/model_data/anchors2.txt')


cap = cv2.VideoCapture('IMG_7544.MOV')
result = 0
while True:
    ret, frame = cap.read()

    if ret:
        x, y, _ = frame.shape
        frame = cv2.resize(frame, dsize=(416, 416), interpolation=cv2.INTER_CUBIC)

        width, height, _ = frame.shape
        matrix = cv2.getRotationMatrix2D((height / 2, width / 2), 270, 1)
        frame = cv2.warpAffine(frame, matrix, (height, width))

        cv2.imwrite("frame.jpg", frame)
        frame = Image.open('frame.jpg')

        # 회전
        # frame = frame.rotate(270)

        frame, cnt = yolo.detect_image(frame)
        result += cnt
        frame = np.array(frame)
        frame = cv2.cvtColor(frame, cv2.COLOR_RGB2BGR)
        cv2.imshow('video', frame)

        if cv2.waitKey(1) % 0xFF == 27:
            break
    else:
        break
print(result)
cap.release()
cv2.destroyAllWindows()

