


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

<details>
<summary>  :point_right: Click </summary>
<br>

###  :fire: train custom data

	!./darknet detector train custom/custom.data custom/custom_yolov4-tiny.cfg custom/yolov4-tiny.conv.29 -dont_show 

- 원래 map과 loss에 대한 그래프가 나오는데 코랩의 리눅스 상에서는 볼 수 없는 듯하다.<br>
  에러가 나기 때문에 dont_show를 추가해 보지 않는 것으로 처리해준다. <br>
   
#### :page_with_curl: yolov3-tiny.conv.15 : pre-train된 weight<br>
 [Fine-Tuning](https://eehoeskrap.tistory.com/186)을 하고 싶을 떄 마지막 레이어를 삭제하고 모델 파일과 가중치 파일을 이용하여 darknet53.conv.74 처럼 가중치 파일을 생성 할 수도 있다.
  
  
	./darknet partial cfg / yolov3-tiny.cfg yolov3-tiny.weights yolov3-tiny.conv.15 15

###  :fire: detect custom model

	!./darknet detector test custom/custom.data custom/custom_yolov4-tiny.cfg custom_yolov4-tiny_last.weights -thresh 0.25 -dont_show -ext_output < custom/train.txt > result.txt  

:x: tarin.txt에 있는 이미지의 경로를 읽어오지 못한다는 에러 발생<br>

#### ⇒ 경로가 제대로 설정 되어 있음에도 에러가 발생한다면,<br>
#### 1. 절대 경로로 재설정
#### 2. 리눅스상에서 읽을 수 있는 포맷으로 변환 ( module : dos2unix )

	!apt-get install dos2unix   
	!dos2unix custom/train.txt  # to linux format

</div>
</details>
<br>  

### :pencil2: How to Increase Accuracy

<br>
<details>
<summary> :zap: Re-modify cfg file </summary>

#### :black_nib: width랑 height가 클수록 정확도는 올라간다.
<br>

#### :black_nib: batch_nomalize는 1로 설정되어있는데, 이 말은 안 쓰겠다는 소리
#### 	 값을 높여서 정확도를 높이려고 했으나 정확도가 올라가진 않음
<br>

#### :black_nib:  subdivisions은 8인 경우 실행되지 않았다. 16으로 설정한 경우에만 실행
<br>

#### :black_nib:  cfg 값을 변경해줄때마다 anchor 값 또한 변경되었다. ⇒  재설정 필요.
<br>

#### :black_nib: YOLOv4 사용

</div>
</details>
<br>

<details>
<summary> :zap: Data augmentation </summary>
<br>

<details>
<summary> :zap:  step 1</summary>
<br>

#### :black_nib:  Hub에서 받은 데이터 셋 중 신호등이 정면에서 보이는 경우 (시각 장애인이 횡단보도 정면에 서있는 경우 신호등을 인식해야한다고 생각) 라벨링을 하였다. (약 900장)
#### 이때 횡단보도 길이가 먼 경우를 고려하여 멀리 있는 신호등도 라벨링을 해주었다.
#### ⇒ YOLOv4를 사용하여 정확도가 30% 정도로 현저히 떨어지는 인식률이 나타났다.

</div>
</details>
<br>

<details>
<summary> :zap:  step 2</summary>
<br>

#### :black_nib:  낮은 인식률이 1차 시도에서 했던 데이터셋의 라벨링이 잘못 되었다고 판단<br>
#### 좀 더 잘 정제된 횡단보도 데이터 셋을 학습시키면서 원인을 찾고자하였다. 
하지만 횡단보도 데이터셋을 이용해 yoloV4를 이용한 학습이 여전히 낮은 인식률을 보여줬다.
또한 cfg 설정을 바꿔보는 방법으로 학습을 시켜보았지만 별 소용이 없어, YOLOv3-tiny를 사용해보았다.
⇒ 이때 yolov4가 원인임을 발견.
#### :black_nib: yolov4 대신 **yolov3-tiny**를 이용하여 학습 시키니 정확도가 훨씬 높게 나타났다.
#### (accuracy 30%  ==> 60% 이상으로 올라갔다.)

<image src="https://user-images.githubusercontent.com/34594339/90633401-f138f100-e260-11ea-8d70-d78506eb1e76.png" width="90%">
	
</div>
</details>
<br>

<details>
<summary> :zap:  step 3</summary>
<br>

#### 횡단보도가 yolov3-tiny를 이용하여 60% 이상의 인식률을 보였고, 
#### :black_nib: 신호등 데이터셋 또한 다시 라벨링하여 가까운 위치에 있는 신호등 데이터셋만 라벨링을 다시 하였다.
	
#### :black_nib: 새로 정제한 신호등 데이터 셋과 YoloV3-tiny를 이용하여 학습 시도
	
<image src="https://user-images.githubusercontent.com/34594339/90770202-61f90f80-e32c-11ea-9086-43e0d3269b24.png" width="90%">

#### :black_nib:  500 여장 정도의  이미지로 50%의 인식률을 보여줬다.

#### :black_nib: 훈련된 위의 weight를 1차 시도의 데이터 셋까지 추가하여 학습 시도
####  ⇒ 48%로  정확도가 떨어졌다. 멀리 있는 신호등 사진의 데이터 셋은 오히려 인식의 정확도를 낮추는 것 같다.

</div>
</details>
<br>

<details>
<summary> :zap:  step 4</summary>
<br>

#### :black_nib: 정확도를 더 올리기 위해  width, height를 608로 설정.
#### :black_nib: anchor도 재정의하여 실행하였으나 
	
<image src="https://user-images.githubusercontent.com/34594339/91044260-f676b100-e64f-11ea-81f7-50fc95d95e30.png" width="80%">

#### :black_nib: 메모리 초과가 발생했다. <br>
#### ⇒ batch의 크기를 조금 줄여주고, subdivision의 크기를 키워주면 된다고 함. <br>
#### (batch : 64, 32, 16 ...  / subdivision : 8, 16, 32, .. )

</div>
</details>
<br>

<details>
<summary> :zap:  step 5</summary>
<br>

#### :black_nib: **batch=32 / subdivision=16으로 설정하여 재시도!**
	 
<image src="https://user-images.githubusercontent.com/34594339/91061321-fe8e1b00-e666-11ea-8cfe-24373780e5ea.png" width="80%">
	
#### :black_nib: 416 크기였을 때보다 낮은 정확도

</div>
</details>
<br>

<details>
<summary> :zap:  step 6</summary>
<br>

#### :black_nib: flip : 좌우 구별 감지를 이용. 정확도를 높이는 방법.
#### :black_nib: max_batches = 5200 </br>
#### :black_nib: width, height = 416, 416 </br>
#### :black_nib: steps=4000,4500 </br>

<image src="https://user-images.githubusercontent.com/34594339/91108707-aaf5ee80-e6b3-11ea-9bf6-8eeac227eb68.png" width="80%">

#### :black_nib: max batches를 올려서 학습한 결과 정확도가 70% 까지 올라갔다.

</div>
</details>
<br>

</div>
</details>
<br>

<details>
<summary> :zap: Merge Traffic Light Model And Crosswalk Model</summary>
<br>

 - [x] 만들어놓은 신호등 데이터셋으로 학습 다시 시켜보기 
 - [x] 예지가 만들어놓은 신호등 데이터셋으로 학습 다시 시켜보기
	- 미리 학습시켜놓았던 weight 파일(정확도 53%)에  남은 데이터 파일을 학습시켜보았다.
		⇒ 정확도가 45%로 약 10%가 내려갔다.
- [x] 모든 데이터셋을 합쳐서 처음부터 학습시켜보기

	<image src="https://user-images.githubusercontent.com/34594339/90980085-26f91500-e594-11ea-8208-56fa07f77410.png" width="76%">

	:black_nib: **45% ⇒ 54%로 상승. <br>
	:black_nib:	똑같은 데이터임에도 불구하고 처음부터 다시 학습시키니 정확도가 제대로 상승하는 것을 확인할 수 있었다.**
<br>
</div>
</details>

<br>
<details>
<summary> :zap: Add Traffic Light Dataset</summary>
<br>

#### :black_nib: 첫번째 시도
 횡단보도 데이터셋 + AI Hub 데이터셋 전부 : :x: 초반에 터짐 / 아예 안됨 :x: <br>
 cfg 설정 등을 바꿔보면서 or  데이터셋을 로컬에 다운,  구글 드라이브에 재업로드 (구글 드라이브 상의 문제로 인한)


#### :black_nib: 두번째 시도
신호등 원본 데이터 셋 + Bbox4는 원래 잘 됐었기 때문에 새로 추가한 Bbox들을 하나씩 빼보면서 학습을 실행<br>
⇒ 25, 30을 빼고 나니 학습이 되긴함.

#### :black_nib: 세번째 시도
BBox25/Bbox30 제외한 모든 데이터셋 학습 ⇒ 30분 남겨놓고 터졌다.

 ### ⇒ :warning: Colab 상의 문제로 밝혀졌다. 무료 버전을 사용하고 있는데, 12시간 이상의 과도한 GPU 사용하면 구글에서 자체적으로 세션을 중지시킨다고 한다. (~~무료 버전의 한계~~) 

#### :black_nib: 네번째 시도
신호등까지 추가 라벨링을 해준 횡단보도 원본 데이터셋만 학습<br>
 ⇒ 횡단보도 정확도 : 58.40 %
 ⇒ 신호등 정확도 : 47.38 %

#### :black_nib: 다섯번째 시도
커스텀 데이터셋만 (Bbox 전부, 25/30 여전히 안됨) : 둘다 20%대

<br>
</div>
</details>

<br>
<details>
<summary> :zap: Dataset classification according to accuracy</summary>
<br>

1. 횡단보도 데이터 셋 : 이미 라벨링 된 데이터 사용.
	 #### :black_nib: 이 데이터셋의 신호등은 라벨링이 되어있지 않아 일단 사용하지 않기로 함
2. #### :black_nib: 신호등 데이터셋 : **신호등만 보이도록 이미지를 자름**
	- Bbox1(AI hub) 
	- 구글링한 신호등 데이터 
	- 직접 찍은 동영상 라벨링

3. 라벨 :  [cross walk, traffic light]<br>
	 #### :black_nib:  [cross walk, red light, green light, black]으로 바꿈
4. 폴더 분류 
	-  Clear(확실)
	-  neutral(중간) : 빛 번짐 없음. 형체가 확실한데 거리가 가깝고 빛번짐 살짝 허용함 (빛번짐이 심하면은 3번으로)    
	- ambiguous(애매) : 거리가 일정이상 멀어졌다고 생각이 들면 형체와 상관없이 3번 빛은 번졌는데 거리가 가깝고 박스 형체가 보이는 경우는 OK
5. 신호등 라벨링 범위
	- #### :black_nib: 어떤 신호등이든 빨간불/파란불  2칸만 라벨링
	- #### :black_nib: 화살표는 라벨링 하지 않음
	- #### :black_nib: 숫자도 라벨링 하지 않음.

	
		<image src="https://user-images.githubusercontent.com/34594339/91948589-dbd0c600-ed3a-11ea-97f5-a894caba618e.png" width="80%">


#### :black_nib: 결과 : 횡단보도 인식은 매우 잘됨. 그러나 신호등을 거의 잡지 못함 
#### :black_nib: 신호등이 매우 크게 잡힌 상태로 라벨링 되었기 때문인듯함.

<br>
</div>
</details>


### 이 이후부터는 [Data Augmentation](https://github.com/bosl95/MachineLearning_Note/tree/master/contest_preparation/data%20augmentation)에 정리


- 남은 과제들
- [x] 횡단보도 정확도 올리기 
- [x] 횡단보도 + 신호등 데이터셋을 모두 합친 학습 모델 만들기.
- [ ]  횡단보도 / 신호등 을 탐지하는 학습 모델 정확도 올리기

## :pushpin: YOLO Object Detection on Android

## :pushpin: Text To Speech

## :pushpin: Outputs
