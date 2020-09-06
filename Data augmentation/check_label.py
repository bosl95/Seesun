import os, cv2

def read_anntation(file_name,img_shape):
    bounding_box_list = []

    f = open(file_name,'r')
    labels = f.readlines()
    print("labels: ",labels)

    # shape
    dh = img_shape[0]
    dw = img_shape[1]

    for l in labels:
        info = l.split(' ')
        x = float(info[1]) * dw
        y = float(info[2]) * dh
        w = float(info[3]) * dw
        h = float(info[4]) * dh

        x1 = x-(w/2)
        y1 = y-(h/2)
        x2 = x+(w/2)
        y2 = y+(h/2)


        bounding_box = [int(info[0]), x1, y1, x2, y2]
        bounding_box_list.append(bounding_box)

    return bounding_box_list, file_name

dir = './resize_clear/'


for file in os.listdir(dir):
    print(file)
    if 'jpg' in file.lower() or 'png' in file.lower():

        img =cv2.imread(dir+file)

        boxes,  _ = read_anntation(dir+file.split('.')[0]+".txt", img.shape)
        label, x, y, x2, y2 =  map(round, boxes[0])

        img = cv2.rectangle(img, (x, y), (x2, y2), (0, 0, 255), 5)
        cv2.imshow('img', img)
        cv2.waitKey(0)