import cv2, os, json
import darknet.darknet as dn


def load_model():
    origin_path = os.getcwd()
    path = os.environ['DARKNET_PATH'] + '/'
    os.chdir(path)  # cd darknet
    net, cls, colors = dn.load_network("cfg/yolov4.cfg", "custom/class.data",
                                       "backup/v4/yolov4_best.weights")
    print(net, cls)
    print("done")
    os.chdir(origin_path)   # cd ..
    return [net, cls, colors]


def bbox2points(bbox):
    x, y, w, h = bbox
    xmin = int(round(x - (w / 2)))
    xmax = int(round(x + (w / 2)))
    ymin = int(round(y - (h / 2)))
    ymax = int(round(y + (h / 2)))
    return xmin, ymin, xmax, ymax


def detect(net, cls, color, cwd):
    img = dn.load_image(cwd.encode() + b'/uploads/test.jpg', 0, 0)
    dts = dn.detect_image(net, cls, img, 0.2)
    print(dts)
    res = {}
    for i, d in enumerate(dts):
        tmp = {}
        tmp['object'] = str(d[0])
        tmp['accuracy'] = str(d[1])
        modify_dts = (bbox2points(d[2]))
        print("result :: ", modify_dts)
        tmp['location'] = str(modify_dts)
        res['obj' + str(i)] = tmp

    return json.dumps(res)