import os
import numpy as np
from keras.layers import Input
from keras_yolo4.yolo4.model import yolo4_body
import cv2

from keras_yolo4.decode_np import Decode


def get_class(classes_path):
    classes_path = os.path.expanduser(classes_path)
    with open(classes_path) as f:
        class_names = f.readlines()
    class_names = [c.strip() for c in class_names]
    return class_names

def get_anchors(anchors_path):
    anchors_path = os.path.expanduser(anchors_path)
    with open(anchors_path) as f:
        anchors = f.readline()
    anchors = [float(x) for x in anchors.split(',')]
    return np.array(anchors).reshape(-1, 2)

if __name__ == '__main__':

    model_path = 'keras_yolo4/model_data/yolo4_weight.h5'
    anchors_path = 'keras_yolo4/model_data/yolo4_anchors.txt'
    classes_path = 'keras_yolo4/model_data/voc_classes.txt'

    class_names = get_class(classes_path)
    anchors = get_anchors(anchors_path)

    num_anchors = len(anchors)
    num_classes = len(class_names)

    model_image_size = (610, 610)


    conf_thresh = 0.2
    nms_thresh = 0.45

    yolo4_model = yolo4_body(Input(shape=model_image_size+(3,)), num_anchors//3, num_classes)

    model_path = os.path.expanduser(model_path)
    assert model_path.endswith('.h5'), 'Keras model or weights must be a .h5 file.'

    yolo4_model.load_weights(model_path)

    _decode = Decode(conf_thresh, nms_thresh, model_image_size, yolo4_model, class_names)

    video_name = "crosswalk3.MOV"

    cap = cv2.VideoCapture('video/'+video_name)


    out = cv2.VideoWriter('result/'+video_name.split('.')[0]+'.avi', cv2.VideoWriter_fourcc('M', 'J', 'P', 'G'), 20, (960, 540))
    time = 1

    while True:

        ret, frame = cap.read()
        if ret:

            frame = cv2.resize(frame, dsize=(0,0), fx=0.5, fy=0.5)
            height, width, channel = frame.shape
            matrix = cv2.getRotationMatrix2D((width / 2, height / 2), 270, 1)
            dst = cv2.warpAffine(frame, matrix, (width, height))
            image, boxes, scores, classes = _decode.detect_image(dst, True)

            out.write(image)
            print(time)
            cv2.imshow('image', image)
            time += 1
            if cv2.waitKey(1) & 0xFF == ord('q'):
                break
        else:
            break

    cap.release()
    out.release()
    cv2.destroyAllWindows()
