import os, csv, shutil

def convert(shape, box):
    dw = 1. / shape[0]
    dh = 1. / shape[1]
    x = (box[0] + box[2]) / 2.0
    y = (box[1] + box[3]) / 2.0
    w = abs(box[2] - box[0])
    h = abs(box[3] - box[1])
    x = x * dw
    w = w * dw
    y *= dh
    h *= dh
    return list(map(str, [x, y, w, h]))

classes = ['cross walk']

if __name__ == '__main__':
    cls = "cross walk"

    if cls not in classes:
        exit(0)


    cls_id = classes.index(cls)
    mainfolder = 'PTL_Dataset_876x657'

    folder_list = os.listdir(mainfolder)
    csv_list = os.listdir('../../MachineLearning_Note/contest_preparation/crosswalk_labeling/annotation')

    for csv_name in csv_list:
        if "test" in csv_name:
            save = 'test.txt'
        elif "train" in csv_name:
            save = 'train.txt'
        else:
            save = 'validation.txt'

        csv_file = open('annotation/'+csv_name, 'r', encoding='utf-8')
        reader = csv.reader(csv_file)

        csv_txt = open('data/'+save, 'w')

        for row in reader:
            if row[1] == 'mode':
                continue

            imgName = row[0]
            box = [int(int(row[i])/4.6) for i in range(2, 6)]

            convert_box = convert([876, 657], box)  # x, y, w, h
            result = '0 '+ ' '.join(convert_box) + "\n" # bounding box 정제 완료

            shutil.copy(mainfolder+"/"+imgName, 'dataset/'+imgName) # 이미지 저장 완료
            img_label = open('dataset/'+imgName.split('.')[0]+'.txt', 'w')
            img_label.write(result)
            img_label.close()

            csv_txt.write('data/obj/' + imgName + "\n")

        csv_txt.close()
        csv_file.close()
