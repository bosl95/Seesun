# 시선(視先)
### 시각 장애인의 안전한 보행을 위한 횡단보도/신호등 탐지 프로그램

## :heavy_check_mark: 목차
###  [1. Summary](#pushpin-project-summary)
### [2. Background](#pushpin-background)
### [3. Custom YOLO Model](#pushpin-custom-yolo-model)
1. [Data augmentation](#pencil2-data-augmentation)
2. [Modify cfg for custom data](#pencil2-modify-cfg-for-custom-data)
3. [Train YOLOv3 tiny on Google Colab](#pencil2-train-yolov3-tiny-on-google-colab)
4. [How to Increase Accuracy](#pencil2-how-to-increase-accuracy)
### [4. YOLO Object Detection on Android](#pushpin-yolo-object-detection-on-android)
### [5. Outputs](#pushpin-outputs)

## :pushpin: Project Summary

### :books: 개발 배경
- #### 대중교통 이용의 불편함 [[출처 : 유튜브 MBCNEWS]](https://youtu.be/saoX-lR-iJ0)
	대중교통, 특히 버스의 경우 타고자 하는 버스가 오는지 안 오는지 확실하게 판단하기 힘들다.
	(ex.버스 탑승을 위해 평균 20~30분을 기다리는 시각장애인들, 잘못된 버스를 타는 경우도 흔함)
<br>

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

c언어로 작성된 물체 인식 오픈 소스 신경망

### :pencil2: YOLO란?
<details>
<summary> :zap: CONCEPT</summary>
<br>

### **"darknet을 통해 학습된 신경망"**  
이전 탐지 시스템은 classifier나 localizer를 사용해 탐지를 수행합니다.
하지만 YOLO는 하나의 신경망을 전체 이미지에 적용합니다.
이 신경망은 이미지를 영역으로 분할하고 각 영역의 Bounding Box와 확률을 예측합니다.
이런 Bounding Box는 예측된 확률에 의해 가중치가 적용됩니다.

</div>
</details>
<br>
<details>
<summary> :grey_question: How to Run YOLOV4</summary>
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

## :pushpin: Custom YOLO Model

### :pencil2: Data Augmentation
<br>
<details>
<summary> :books: Data processing </summary>
<br>

### :mag_right: 신호등 데이터셋  : [[AI Hub]](http://www.aihub.or.kr/aidata/136)
  신호등이 있는 사진과 Bounding Box가 되어있는 xml파일을 받았으나, 
:warning: **보행등과 차량등이 분류가 되어있지 않다**:warning:  는 문제점이 발생하였다. 

### :bulb: 해결방안  
  1) 우선적으로 데이터 셋에 신호등이 다 있는 것도 아니기 때문에 1차적으로 신호등을 찾아준다.  
  ⇒ 신호등 label을 갖는 사진을 분류한다는 의미  
  
  2) label을 확인함과 동시에 신호등 사진을 띄운다. 그 사진속에 있는 신호등이 보행등이라면 저장, 차량등이라면 저장하지 않고 넘어간다.  
  
  3) 사진을 저장하는 경우에 label 데이터는 가공이 필요하다.  
  현재 AI hub에서 제공되는 Bounding Box  좌표 ⇒ (좌상단 x, 좌상단 y, 우하단 x, 우하단 y좌표)  

### :point_right: [Explore Labeling folder]()

</div>
</details>
<br>

### :pencil2: Modify cfg For Custom Data

### :pencil2: Train YOLOv3 tiny on Google Colab

### :pencil2: How to Increase Accuracy

## :pushpin: YOLO Object Detection on Android


## :pushpin: Outputs
