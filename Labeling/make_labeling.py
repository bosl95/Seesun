import glob
import os
from xml.etree.ElementTree import parse
import cv2
import shutil

def rmDirfile(save):
    for f in glob.glob(save + '/*'):
        os.remove(f)

def convert(shape, box):
    dw = 1. / shape[0]
    dh = 1. / shape[1]
    x = (box[0] + box[2]) / 2.0
    y = (box[1] + box[3]) / 2.0
    w = box[2] - box[0]
    h = box[3] - box[1]
    x = x * dw
    w = w * dw
    y *= dh
    h *= dh
    return list(map(str, [x, y, w, h]))

def parsing(xml):
    tree = parse(xml)
    r = xml.split('/')
    img_path = '/'.join(r[:-1]) + '/'  # Bbox_1/Bbox_0001/
    # print(xml)                  # ./Bbox_1/Bbox_0001/0617_01.xml

    root = tree.getroot()

    flag = True

    filelist = os.listdir("dataset/"+img_path)
    last_image_name = None
    if filelist:
        last_image_name = filelist[-2]
        flag = False    # 이전에 실행했던 폴더면 마지막 위치로 가기 위한 플래그

    for image in root.iter('image'):
        img_name = image.attrib['name']
        if last_image_name and last_image_name == img_name:
            flag = True
            continue    # 이전에 실행했던 파일이므로 이 뒤에부터 실행.

        if flag:
            for box in image.iter('box'):
                if box.attrib['label'] == "traffic_light":  # 신호등 라벨이 존재하는 사진인 경우

                    img = cv2.imread(img_path + img_name)  # 사진을 띄우기
                    w, h, _ = img.shape  # 이미지 사이즈   (1080, 1920, 3)

                    x, y = map(round, [float(box.attrib['xtl']), float(box.attrib['ytl'])])
                    # img = cv2.line(img, (x, y), (x, y), (0, 0, 255), 5)
                    x2, y2 = map(round, [float(box.attrib['xbr']), float(box.attrib['ybr'])])
                    img = cv2.rectangle(img, (x, y), (x2, y2), (0, 0, 255), 5)
                    img2 = cv2.resize(img, (h // 2, w // 2))
                    cv2.imshow('check image', img2)
                    k = cv2.waitKey(0)
                    cv2.destroyAllWindows()

                    # 보행등이면 'z'버튼 / 차량등이면 'x'버튼
                    if k == ord('z'):  # 'z'버튼이 눌리면(보행등) 사진, 라벨 저장
                        shutil.copy(img_path + img_name, save_path + "/" + img_name)  # dataset/Bbox_1에 사진 저장.
                        print("image 저장 ", img_name)
                        # xtl , ytl  : 좌상단 x,y좌표  /  xbr, ybr : 우하단 x, y 좌표
                        b = list(map(float, [box.attrib['xtl'], box.attrib['ytl'], box.attrib['xbr'], box.attrib['ybr']]))
                        bb = convert((h,w), b)
                        # print(str(cls_id) + " " + " ".join(bb))
                        label_txt = open(save_path+"/"+img_name[:-4] + ".txt", 'a')
                        label_txt.write(str(cls_id) + " " + " ".join(bb) + "\n")  # 클래스 index ,  center x, center y, w, h 비율을 text에 저장
                        label_txt.close()
                    elif k == ord('q'):  # 잘못 라벨링한 경우 현재 폴더부터 다시 재수행
                        rmDirfile('dataset/' + '/'.join(r[:-1]))
                        return False  # 라벨링이 실패함.
                    elif k == ord('p'):
                        exit(0)

    return True  # 라벨링이 성공적으로 완료됨.


classes = ["PTL"]  # Pedestrian traffic light

if __name__ == '__main__':
    cls = "PTL"

    if cls not in classes:
        exit(0)

    cls_id = classes.index(cls)
    mainfolder = 'Bbox_1'
    if not os.path.exists('dataset/' + mainfolder):  # dataset/Bbox_1
        os.mkdir('dataset/' + mainfolder)
        idx = 0
    else:
        idx = len(os.listdir('dataset/' + mainfolder)) - 1
        # 이미 열었던 폴더라면 몇번째 폴더까지 했는지 체크, 마지막으로 방문했던 폴더부터 다시 시작할 수 있도록 인덱스 설정

    folder_list = os.listdir(mainfolder)
    # print(folder_list)  # ['Bbox_0001', 'Bbox_0002', 'Bbox_0003', 'Bbox_0004' ... ]

    for subfolder in folder_list[idx:]:
        file = os.listdir(mainfolder + "/" + subfolder)  # 폴더에 있는 사진 파일 목록을 가져옴

        # Bbox_1_new ==> 하위 폴더 Bbox_0001 폴더 생성
        save_path = 'dataset/' + mainfolder + '/' + subfolder  # dataset/Bbox_1/Bbox_0001
        if not os.path.exists(save_path):  # 새로운 폴더 생성이 안되어있다면 만들기
            os.mkdir(save_path)

        chk = False
        while not chk:
            chk = parsing(mainfolder + "/" + subfolder + "/" + file[0])  # Bbox_1/Bbox_0001/*.xml 파일에 접근.


