

# 시선(視先)

###  視先(볼 시, 먼저 선) 
> ### :loudspeaker: "먼저 바라보고, 시각 기능을 제공하다."
<br>

### 시각 장애인의 안전한 보행을 위한 횡단보도/신호등 탐지 프로그램

## :heavy_check_mark: Contents
###  [1. Summary](#pushpin-project-summary)
### [2. Background](#pushpin-background)
### [3. Custom YOLO Model](#pushpin-custom-yolo-model)
1. [Data processing](#pencil2-data-processing)
2. [Modify cfg for custom data](#pencil2-modify-cfg-for-custom-data)
3. [Train YOLOv3 tiny on Google Colab](#pencil2-train-yolov3-tiny-on-google-colab)
4. [How to Increase Accuracy](#pencil2-how-to-increase-accuracy)
### [4. YOLO Object Detection on Android](#pushpin-yolo-object-detection-on-android)
### [5. TTS(Text To Speech)](#pushpin-text-to-speech)
### [6. Outputs](#pushpin-outputs)

## :pushpin: Project Summary

### :books: 개발 배경
- #### 대중교통 이용의 불편함 [[출처 : 유튜브 MBCNEWS]](https://youtu.be/saoX-lR-iJ0)
	대중교통, 특히 버스의 경우 타고자 하는 버스가 오는지 안 오는지 확실하게 판단하기 힘들다.
	(ex.버스 탑승을 위해 평균 20~30분을 기다리는 시각장애인들, 잘못된 버스를 타는 경우도 흔함)

- #### 시각 장애인의 눈이 되어 그들의 보행을 돕는 안내견 [[출처 : 웰페어 뉴스]](https://www.welfarenews.net/news/articleView.html?idxno=71314)
	안내견 탑승 및 출입 거부는 위법행위임에도 불구하고 여전히 안내견에 대한 불편한 인식이 산재한다.
	(ex. 대중교통 탑승 거부, 식품접객업소 출입 거부 등등)
<br>

### :pencil: 개발 목적
안내견이나 기존의 시각 장애인 보조 서비스가 있음에도 불구하고, 실제 시각장애인들의 보행에 어려움이 많았다. 이를 개선하기 위해 음성 안내 서비스 '시선(視先, 먼저 보다)'을 고안하였다.
#### **'시선'은 영상 처리를 활용하여 객체를 탐지하고, 음성으로 안내하는 서비스이다.**
#### 이를 통해 시각장애인들의 안전한 보행을 돕는 것을 목표로 한다.
<br>

### :bulb: Insight 
- 시각 장애인과 비시각 장애인의 "안전" 차이
	- 시각 장애인의 안전  : 직접 부딪히고 닿아서 물체의 위치 확인. 벽에 붙어서 보행
	- 비시각 장애인의 안전 : 아무 물체가 없는 중간이 안전하다고 생각하고 중간으로 안내
	### ⇒ **시각장애인의 needs에 초점을 맞춰 기능을 구현**
<br>

## :pushpin: Background

### :pencil2: Darknet

C언어로 작성된 물체 인식 오픈 소스 신경망<br>
YOLO 개발자가 특별히 제작한 프레임워크
빠르고  GPU/CPU와 함께 사용이 가능하나, 리눅스에서만 호환된다.

### :pencil2: Darkflow

Darknet을 Tensorflow에 적용<br>
빠르고 GPU/CPU와 함께 사용이 가능하고 Linux/Mac/Window에서 호환이 가능하나,
설치가 복잡하다.

### :pencil2: YOLO란?
<details>
<summary> :zap: CONCEPT</summary>
<br>

### **"darknet을 통해 학습된 신경망"**  
이전 탐지 시스템은 classifier나 localizer를 사용해 탐지를 수행한다.<br>
하지만 YOLO는 하나의 신경망을 전체 이미지에 적용한다.<br>
이 신경망은 이미지를 영역으로 분할하고 각 영역의 Bounding Box와 확률을 예측한다.<br>
이런 Bounding Box는 예측된 확률에 의해 가중치가 적용된다.<br>

</div>
</details>
<br>

<details>
<summary> :grey_question: How to Run YOLOV4 </summary>
<br> 

## :fire: Run YOLOv4

1) dark net의 weight ⇒ yolov4.weights 으로 변환하는 과정.    

	```
	# Convert darknet weights to tensorflow
	## yolov4  버전
	python save_model.py --weights ./data/yolov4.weights --output ./checkpoints/yolov4-416 --input_size 416 --model yolov4 

	## yolov4-tiny 버전
	python save_model.py --weights ./data/yolov4-tiny.weights --output ./checkpoints/yolov4-tiny-416 --input_size 416 --model yolov4 --tiny
	```
2) object detection이 잘 되는 지 확인하기	  
	```
	# Run demo tensorflow
	python detect.py --weights ./checkpoints/yolov4-416 --size 416 --model yolov4 --image ./data/kite.jpg

	python detect.py --weights ./checkpoints/yolov4-tiny-416 --size 416 --model yolov4 --image ./data/kite.jpg --tiny
	```
3) 결과  
   <image src="https://github.com/kairess/tensorflow-yolov4-tflite/raw/master/result.png" width="90%">
  
   <image src="https://user-images.githubusercontent.com/34594339/89185473-3f998f00-d5d5-11ea-99f7-45c37f85e8f0.png" width="90%">  
  
	 #### ⇒ yolov4 weight (위) / yolo4-tiny (아래)  
	 #### 속도는 tiny가 훨씬 빠르다.  

#### :x:  YOLOV4를 이용해 커스텀 데이터 셋을 만들려고 하였으나, YOLOV3를 이용한 정확도가 훨씬 높아 YOLOV3-tiny를 사용하기로 함.

 <br>
</div>
</details>
<br>


### Google Colab

<details>
<summary> :point_right: Click </summary>
<br>

#### yolo를 노트북에서도 사용하기 위해서는 **GPU를 사용해야 한다.**   
#### 이를 위해서 Google에서 지원하는 Colab을 이용해 yolo를 구동시킬 수 있다.  

<br>

#### Colab을 세션을 12시간만 유지시켜주기 때문에 저장이 불가하다. <br>
#### ⇒  구글 드라이브에 데이터를 저장해 놓고 마운트 해서 쓸 수 있다

<br>

#### :bulb: Colab 런타임 장시간 세션 유지하기
    function ClickConnect() { // 백엔드를 할당하지 못했습니다. // GPU이(가) 있는 백엔드를 사용할 수 없습니다. 가속기가 없는 런타임을 사용하시겠습니까? // 취소 버튼을 찾아서 클릭 var buttons = document.querySelectorAll("colab-dialog.yes-no-dialog paper-button#cancel"); buttons.forEach(function(btn) { btn.click(); }); console.log("1분마다 자동 재연결"); document.querySelector("#top-toolbar > colab-connect-button").click(); } setInterval(ClickConnect,1000*60);  

F12를 눌러 자바 스크립트 창에 입력해주면 된다.<br>
  

#### :bulb: 주피터 노트북이 명령 프롬트에서 입력한  것처럼  처리하는 명령어  
   - `` !`` :  쉘이 끝나면 유지 되지 않음
   - ``%`` : 쉘이 끝난 후에도 계속 유지
	   
#### <실행 화면>

<image src="https://user-images.githubusercontent.com/34594339/89725910-db9d1d80-da4f-11ea-88bf-8ab79c47a555.png" width="80%">  

</div>
</details>
<br>

## :pushpin: Custom YOLO Model

### :pencil2: Data Processing
<details>
<summary>  :point_right: Click </summary>

### :mag_right: 신호등 데이터셋  : [[AI Hub]](http://www.aihub.or.kr/aidata/136)
  신호등이 있는 사진과 Bounding Box가 되어있는 xml파일을 받았으나, 
:warning: **보행등과 차량등이 분류가 되어있지 않다**:warning:  는 문제점이 발생하였다. 

### :bulb: 해결방안  
  1) 우선적으로 데이터 셋에 신호등이 다 있는 것도 아니기 때문에 1차적으로 신호등을 찾아준다.  
  ⇒ 신호등 label을 갖는 사진을 분류한다는 의미  
  
  2) label을 확인함과 동시에 신호등 사진을 띄운다. 그 사진속에 있는 신호등이 보행등이라면 저장, 차량등이라면 저장하지 않고 넘어간다.  
  
  3) 사진을 저장하는 경우에 label 데이터는 가공이 필요하다.  
  현재 AI hub에서 제공되는 Bounding Box  좌표 ⇒ (좌상단 x, 좌상단 y, 우하단 x, 우하단 y좌표)  

### :point_right: [Explore Labeling folder](https://github.com/bosl95/Seesun/tree/master/Labeling)

</div>
</details>
<br>

### :pencil2: Data File for YOLO
<details>
<summary>  :point_right: Click </summary>

#### :page_with_curl: ```obj.data``` : 학습을 위한 내용이 담긴 파일  
   - classes 개수  
   - train.txt와 valid.txt의 경로  
   - obj.names의 경로  
   - weight을 저장할 폴더의 경로  
####  :page_with_curl: ``obj.cfg`` : 모델 구조 및 train과 관련된 설정이 들어있는 파일  
   - batch 및  subdivisions 사이즈(Cuda memory 관련), width 및 height 사이즈  
   - learning late, burn_in, max_batches,  policy, steps, scales 설정  
   - filter : (4+1+class수) * 3  
   - classes  
   - anchors 및 mask 설정  
#### :page_with_curl: ``weight``  : 미리 트레이닝된 모델 또는 darknet53.conv.74 등의 가중치 파일
#### :page_with_curl: ``obj.names`` : annotation에 포함되어있는 라벨링 이름 목록. 검출하고자 하는 목록  
#### :page_with_curl: ``train.txt`` : 학습시킬 이미지들의 경로들이 담긴 리스트  
#### :page_with_curl: ``valid.txt`` : 학습 시 validation 할 이미지들의 경로들이 담긴 리스트  
 
</div>
</details>
<br>

### :pencil2: Modify cfg For Custom Data
<details>
<summary>  :point_right: Click </summary>
<br>

#### :zap: cfg 설정  
   
   <image src="https://user-images.githubusercontent.com/34594339/89791590-7b48d180-db5e-11ea-9c98-5e67e557fc33.png" width="100%"><br>

<br>
:small_blue_diamond: max_batches = number of classes * 2000 <br>
:small_blue_diamond: steps = max_batches의 80%, max_batches의 90%<br>
<br>
  
#### :zap: anchor 계산  
   
   <image src="https://user-images.githubusercontent.com/34594339/89791801-b9de8c00-db5e-11ea-9e7a-b9e63bdbe049.png" width="100%">
   

### :information_source: Source
<details>
<summary>  :point_right: How To set cfg File </summary>
<br>

:fire: [https://murra.tistory.com/115](https://murra.tistory.com/115)<br>
:round_pushpin: [https://keyog.tistory.com/22](https://keyog.tistory.com/22)  <br>
:round_pushpin: [https://eehoeskrap.tistory.com/370](https://eehoeskrap.tistory.com/370)<br>
:round_pushpin: [https://codingzzangmimi.tistory.com/76](https://codingzzangmimi.tistory.com/76) <br>
:round_pushpin: [https://go-programming.tistory.com/160](https://go-programming.tistory.com/160)<br>
 :round_pushpin: [https://github.com/AlexeyAB/darknet#how-to-train-to-detect-your-custom-objects](https://github.com/AlexeyAB/darknet#how-to-train-to-detect-your-custom-objects)  

</div>
</details>
<br>

</div>
</details>
<br>

### :pencil2: Train YOLOv3 tiny on Google Colab

### :pencil2: How to Increase Accuracy
<details>
<summary> :books: Data augmentation </summary>
<br>
</div>
</details>
<br>

## :pushpin: YOLO Object Detection on Android

## :pushpin: Text To Speech

## :pushpin: Outputs
