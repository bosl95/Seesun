
# Custom Data Processing


## :heavy_check_mark: Contents
### [1. Data Format](#pushpin-data-format)
### [2. Custom Data Processing](#pushpin-custom-data-processing)
### [3. Make Train Text For YOLO](#pushpin-make-train-text-for-yolo)
### [4. Division Dataset](#pushpin-division-dataset)

<br>

## :pushpin: Data Format

### :books: COCO Data Format
#### :point_right: Bounding Box = (x, y, w, h)

### :books: My Custom Data Format
#### :point_right: Bounding Box = (x1, y1, x2, y2)

### :books: YOLO Data Format
#### :point_right: Bounding Box = (center x, center y, ratio w, ratio h)

<br>

### :eyes: Attention
#### :pencil2: PIL : Python Image Library. 
 파이썬 인터프리터에 다양한 이미지 파일 형식을 지원하고 강력한 이미지 처리와 그래픽 기능을 제공하는 오픈 소스 소프트웨어 라이브러리
#### ``Image.size = (width, height)``

####  :pencil2: OPENCV(Open Source Computer Vision) : RealTime Computer Vision Library.
**실시간** 이미지 프로세싱에 중점을 둔 라이브러리
#### ```Image.shape = (height, width, channel)```

#### :pencil2: OpenCV coordinate system
<image src="https://lh4.googleusercontent.com/ndFH6A225tFLWb7JwjyMmn539c4e1c1CmU7w4hQD6j-uO9K4diKfZ-FDr8LFuKa9oad9IaunhXRz0kD0JoRbeRV4gzUpS0ELyPKMIlpXs9FgvbJZiNGreGvWQAlMnYnRkqzo8Vlh" width="60%">
	
<br>

#### :round_pushpin: Source 1 (Blog) : [How to convert COCO Format to YOLO Format?](https://eehoeskrap.tistory.com/367)
#### :round_pushpin: Source 2 (Blog) : [Sign Data Training and Recognition ](https://sites.google.com/site/bimprinciple/in-the-news/yolodibleoninggibandolopyojipaninsig)
#### :round_pushpin: Source 3 (GitHub) : [Convert annotation to darknet format ](https://github.com/Guanghan/darknet/blob/master/scripts/convert.py)
#### :round_pushpin: Source 4 (Blog) : [Differences between PIL and OPENCV](https://note.nkmk.me/en/python-opencv-pillow-image-size/)
<br>

## :pushpin: Custom Data Processing

### :rocket: make_labeling.py 
<br>
<details>    
<summary>:pencil2: Document</summary> 
<br>

:small_blue_diamond: classes : 클래스 리스트<br>
:small_blue_diamond: cls : 데이터를 분류할 클래스 이름        
:small_blue_diamond: main folder : 학습할 데이터 셋이 들어가있는 폴더        
        
#### :open_file_folder: Folder tree        
---
  - :open_file_folder:  Bbox_1 ( 데이터 셋 루트 폴더)        
     - :open_file_folder: Bbox_0001        
        - :page_with_curl: 0617_01.xml        
	    - :framed_picture: img1.jpg        
        - :framed_picture: img2.jpg        
            ...        
     - :open_file_folder: Bbox_0002        
		    ...        

---
        
  - :open_file_folder: dataset ( 분류된 데이터 셋 ) ⇒ 학습에 이용할 이미지 셋        
     - :open_file_folder: Bbox_1        
        - :open_file_folder: Bbox_0001        
            - :framed_picture: img.jpg        
            - :page_facing_up: img.txt (img.jpg의 label 값)        
            - :framed_picture: img2.jpg        
            - :page_facing_up: img2.txt        
            ...        
         - :open_file_folder: Bbox_0002        
         ...        
   <br>
   </div>
   </details>
   <br> 
   
<br>
<details>    
<summary>:mag_right: Procedure </summary> 
<br>

     
 1. 원본 데이터 root 폴더의 이름 [Bbox_1]을 dataset 폴더에다가 생성.        
     (이미 존재한다면 생성하지 않음)        
 2. 원본 데이터 root 폴더의 하위폴더 [Bbox_0001]을 dataset/하위폴더명으로 생성.        
     (마찬가지로 이미 존재한다면 생성하지 않음)        
 3. 하위 폴더 탐색을 시작        
    1. xml 파일을 찾음. file[0] ⇒ 가장 위에 있음.        
    2. xml을 파싱, traffic light를 가지는 사진을 화면에 출력        
    3. 바운딩 박스가 보행등일 경우 ``'z' 버튼`` ⇒ label.txt / img.jpg에 순서에 맞게 저장.        
    ⇒ :fire: YOLO format으로 변환하는  ``convert()`` 실행
4. 만약 라벨링이 잘못된 경우 ``'q' 버튼``을 누르면 **현재 탐색하고 있는 하위 폴더를 처음부터 재탐색 (return False)**
5. 또는 라벨링을 중단하고 싶다면 ``'p' 버튼``를 누르면 종료        
     ⇒ 재시작시         
     
        filelist = os.listdir("dataset/"+img_path)        
        last_image_name = None        
        if filelist:        
            last_image_name = filelist[-2]        
            flag = False # 이전에 실행했던 폴더면 마지막 위치로 가기 위한 플래그      
	dataset 폴더에서 마지막으로 저장된 파일의 위치를 찾아 그 위치부터 이어서 시작할 수 있도록 함.      
      
6. 그 외에 현재 사진을 라벨링 하지 않고 계속 진행할 경우 아무 버튼이나 눌러주면 다음 사진으로 넘어감.        
 
 ---
 
#### :mag_right: <실행 화면>
<image src="https://user-images.githubusercontent.com/34594339/90097605-4b254c80-dd71-11ea-9fe5-24d78e6eb917.png" width="80%">

 <br>
 </div>
 </details>
 <br> 

## :pushpin: Make Train Text For YOLO

### :rocket: make train text.py 

분류된 이미지 데이터 셋들의 경로를 저장할 train.txt 파일을 생성.        
이때 절대 경로로 생성을 해주었는데, 이 경로는 Google Colab에서 사용할 수 있는 절대 경로로 지정하였다.        

<br>
<details>    
<summary>:mag_right: <실행 화면> </summary> 
<br>
   
- 절대 경로로 train.txt 파일 생성시
     
	<image src="https://user-images.githubusercontent.com/34594339/89789461-982fd580-db5b-11ea-85a1-68c92daa20c7.png" width="70%">
    
- 상대 경로로 train.txt 파일 생성 시
darknet train을 실행하는 현재 경로에서 상대 경로를 찾아서 학습함으로 그에 맞게 설정해주면 된다.   

   <image src= "https://user-images.githubusercontent.com/34594339/90631267-61457800-e25d-11ea-8497-53762839a6f9.png" width=50% >
 
  <br>
 </div>
 </details>
 <br>  

## :pushpin: Division Dataset
### :rocket: Division dataset.py 
        
#### All_train.txt를 ``random.shuffle() 함수``를 통해 데이터를 섞어준다.
#### :question:  ``연속적인 데이터셋``이 섞여있는 경우엔 ``데이터의 과적합이 발생``할 수 있기 때문 <br>

- shuffled.txt로 생성된 모든 이미지의 절대 경로를 8:2의 비율로 나눈다.
- train data와 validation data로 분리한다. 
- 이때 custom 폴더 안에 저장하므로 실행 전 custom 폴더가 존재해야한다.
 

#### :mag_right: <실행 결과>

        
   <image src="https://user-images.githubusercontent.com/34594339/89789807-0ffe0000-db5c-11ea-9266-b7a23b01e7c9.png" width="40%">
 
 <br>
<details>    
<summary> :rainbow: Last version</summary> 
<br>

### :rocket: make train text last.py 
all_train.txt를 만드는 프로그램이 할때마다 지속적으로 수정이 되어,<br>
폴더이름과 상위 경로만 지정해주면 all_train.txt 를 자동 생성해주는 코드로 수정. (최종 버전)

1. 우선 all_train.txt 파일이 존재한다면 지우고 시작한다.
2. folder : 내가 사용할 데이터 셋의 폴더 경로
3. yolo_path : 상위 경로를 지정해준다. ( yolo_path+"img.jpg" 로 all_train.txt에 텍스트가 추가된다. 그에 맞게 지정)
4. 만약 추가할 데이터 셋 폴더가 여러개라면 folder와 yolo_path만 수정해서 계속 all_train.txt에 추가해준다.

- 예시  
      
	    data/obj/ambiguous/MP_SEL_002944.jpg  
	    data/obj/ambiguous/MP_SEL_002946.jpg  
	    data/obj/ambiguous/MP_SEL_002959.jpg  
	    data/obj/ambiguous/MP_SEL_003012.jpg  
	    data/obj/ambiguous/MP_SEL_003060.jpg  
	    data/obj/ambiguous/MP_SEL_003127.jpg  
	    data/obj/ambiguous/MP_SEL_003171.jpg  
	    data/obj/ambiguous/MP_SEL_003184.jpg

  <br>
 </div>
 </details>
 <br>  
