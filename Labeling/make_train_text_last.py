import os

if __name__ == '__main__':

    folder = '../data augmentation/neutral'
    yolo_path = 'data/square/neutral/' # + img Name

    with open('all_train.txt', 'a') as f:
        for file in os.listdir(folder):
            if 'jpg' in file.lower() or 'png' in file.lower():
                f.write(yolo_path+file+"\n")
    f.close()
