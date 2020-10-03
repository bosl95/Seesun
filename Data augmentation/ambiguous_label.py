from os import listdir

path = 'C:\\Users\sor\Desktop\label\\ambiguous\\txt_File'

for file in listdir(path):
    if 'txt' in file:   # 텍스트 파일의 라벨을 수정 (1->4, 2->5, 3->6)
        lines = ''
        with open(path+"\\"+file, 'r') as f:
            old = f.readline()
            old = list(old)

            print('old : ', ''.join(old))
            if old[0]=='1':
                old[0]='4'
            elif old[0]=='2':
                old[0]='5'
            elif old[0]=='3':
                old[0]='6'
            lines += (''.join(old)+'\n')
            print('new : ', ''.join(old))
            print()

        with open(path+'\\'+file, 'w') as f:
            f.writelines(lines)
