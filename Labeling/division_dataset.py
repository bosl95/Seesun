import random

def shuffle():
    f = open('shuffled.txt', 'w')
    with open("all_train.txt", mode="r", encoding="utf-8") as myFile:
        lines = list(myFile)
    random.shuffle(lines)

    for i in range(0,len(lines)):
        f.write(lines[i])

shuffle()

count = 0
txt = open('shuffled.txt','r')
length = len(txt.readlines()) #total line
txt.close()

i = 0
txt = open('shuffled.txt','r')
f = open('custom/train.txt','w')
f2 = open('custom/validation.txt','w')

while True :
    if i == 0 :
        line = txt.readline()
        if not line :
            break
        count +=1
        if count < int(length/10)*2 :
            f2.write(line)
        else :
            f.write(line)

txt.close()
f.close()
f2.close()