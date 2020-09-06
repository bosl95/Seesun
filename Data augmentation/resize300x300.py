from make_labeling import convert

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

from os import listdir
import cv2
import numpy as np

def read_train_dataset(dir):
    images = []
    annotations = []

    # dir 폴더의 모든 이미지 파일 읽어들임
    for file in listdir(dir):
        if 'jpg' in file.lower() or 'png' in file.lower():
            img = cv2.imread(dir + file, 1)
            images.append(img) # 컬러로 읽어들여 images 리스트에 추가
            imgName.append(file.split('.')[0])
            annotation_file = file.replace(file.split('.')[-1], 'txt')
            bounding_box_list, file_name = read_anntation(dir + annotation_file, img.shape)
            annotations.append((bounding_box_list, annotation_file, file_name)) # 라벨담은 리스트, 텍스트파일명, 텍스트파일명(?)

    images = np.array(images) # Image 타입으로 변환

    return images, annotations # Image배열과 라벨배열 반환


def insert(save_path, img, img_name):
    img2 = cv2.imread('876.jpg')  # 876x876 이미지 (검은 이미지)

    w, h, c = img.shape  # 삽입할 이미지의 row, col, channel정보

    img2[:w, :h] = img
    cv2.imwrite(save_path + img_name+".jpg", img2)


import imgaug as ia
from imgaug import augmenters as iaa

ia.seed(1)

dir = 'clear/'    # 정사각형 형태의 신호등 데이터셋 폴더
save = 'resize_clear/'
imgName = []
images, annotations = read_train_dataset(dir)

for idx in range(len(images)):
    image = images[idx]
    boxes = annotations[idx][0] # 라벨 : [info[0], info[1], info[2], info[3], info[4]]


    ia_bounding_boxes = []
    for box in boxes:
        ia_bounding_boxes.append(ia.BoundingBox(label=box[0], x1=box[1], y1=box[2], x2=box[3], y2=box[4]))
    bbs = ia.BoundingBoxesOnImage(ia_bounding_boxes, shape=image.shape)


    seq = iaa.Resize((300, 300))

    seq_det = seq.to_deterministic()

    image_aug = seq_det.augment_images([image])[0]
    bbs_aug = seq_det.augment_bounding_boxes([bbs])[0]

    with open(save + imgName[idx] + ".txt", 'w') as f:
        for i in range(len(bbs.bounding_boxes)):  # label을 txt로 저장
            after = bbs_aug.bounding_boxes[i]
            label = str(after.label)
            box = [after.x1, after.y1, after.x2, after.y2]

            box = convert([876, 876], box)

            f.write(label + " " + " ".join(box))
        f.close()
    insert(save, image_aug, imgName[idx])
