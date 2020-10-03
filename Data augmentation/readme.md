
# Increase Accuracy

### 신호등 + 횡단보도 YOLO  모델 정확도 올리기    

:hourglass_flowing_sand: Goal
- [x] *Increase crosswalk accuracy*
- [x] *Make Model that combines crosswalk and traffic light Dataset*
- [x] *Increase Combined Model Accuracy*
<br>

:hourglass_flowing_sand: Task

- [x] *version 1 : Square traffic light Dataset and Train Model*
- [x]  *version 2: Resize traffic light Dataset similar to the size of actual traffic light size*    
- [x] *Retry Training YOLOv4 focusing on Accuracy*

<details>    
<summary> Source </summary>    
<br>

 :round_pushpin: https://github.com/Paperspace/DataAugmentationForObjectDetection      
:round_pushpin: https://github.com/albumentations-team/albumentations      
:fire: https://github.com/aleju/imgaug     
 
  <br>
 </div>    
</details> 
<br>

## :rocket: Check Label
#### :pencil: dir에 확인할 라벨링 데이터 폴더 이름을 넣어주면 라벨링된 이미지를 띄워준다.    
   <br> 
    
## :rocket: Make Square
#### :pencil: 직사각형의 이미지를 정사각형 형태로 만들어주기    
⇒ yolov3에서 416x416 형태로 학습을 진행하기 때문에 정사각형 변형을 통해 정확도 향상을 확인    
    
'images' 폴더 대신에 들어갈 인풋 이미지 폴더 이름을 넣어주고, 'output' 폴더에 정사각형 형태의 이미지가 저장  
  <br>  
    
    
## :rocket: Resize certain ratio
#### :pencil: 이미지 사이즈를 일정 비율로 줄이기 ⇒ 0.5, 0.5로 비율로 줄임

 <image src="https://user-images.githubusercontent.com/34594339/91954309-96ad9380-ed3c-11ea-82f1-a83fa20af28d.png" width="80%">    
 
    
![image](https://user-images.githubusercontent.com/34594339/91967657-78e92a00-ed4e-11ea-986c-71bebdead81b.png)    
    
#### :heavy_exclamation_mark: convert 함수(꼭지점 ⇒ yolo  포맷 변환)에 shape를 전달해줄때 w, h 가 뒤바뀐다. :heavy_exclamation_mark:
<br>
    

## :rocket: resize 300x300.py   

#### :pencil: 300x300 크기의 사진으로 resize 해준다.
<br>

## :rocket: ambiguous_label.py
### Ambiguous 폴더의 라벨을 변경해준다.<br>

	class label
	0 : cross walk
	1 : green light
	2 : red light
	3 : black
	4 : green light2
	5 : red light2
	6 : black2

### ⇒ ambiguous 파일 1~3의 라벨을 4~6으로 변경해준다. <br>

<br>

## :pushpin: Data augmentation

### :heavy_check_mark: STEP1. YOLOv3-tiny

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

### :heavy_check_mark: STEP2. YOLOv4

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

<br>
</div>
</details>
<br>

<details>    
<summary> :zap: 1차 Data augmentation</summary>    
<br>
    
#### :black_nib: 추가할 Augmentation Dataset    
#### :black_nib: 1. 정사각형 사이즈의 횡단보도  데이터 (패딩)    
#### :black_nib:  2. 정사각형 사이즈의 신호등 데이터 (패딩)    
#### :black_nib:  3. 비율을 0.5로 Resize 한 신호등 데이터     
   
   
<image src="https://user-images.githubusercontent.com/34594339/92190089-2d906200-ee9b-11ea-81ae-4c6126a731a5.png" width="70%">    

<br>

### :point_right: [result](https://youtu.be/7nY9py7DEQw)

 <br>
</div>    
</details>    

<details>    
<summary> :zap: 2차 Data augmentation</summary>    
<br>
	
정사각형 형태의 신호등을 그냥 학습시켜도 신호등이 가깝지 않으면 인식이 잘 되지 않았다.    
그래서 우리가 만든 신호등 데이터 셋을 횡단보도 데이터셋안의  신호등 데이터와 유사한 크기로 만들어주었다.<br>
[(Darknet에서 사이즈를 조정해주기는 하나, 정사각형으로 resize하는 것이 성능에 영향을 주기는 하는 것 같다.)](https://github.com/pjreddie/darknet/issues/728#issuecomment-383539370) <br>

1. 정사각형 형태로 리사이즈된 신호등 데이터셋 A    
    
   <image src="https://user-images.githubusercontent.com/34594339/92205369-81617200-eec0-11ea-9702-035496b8ccca.png" width="50%">    
    
   <예시> 이미지 크기 : 822x822    
2. A' = A를 300x300 크기로 바꿔준다. (횡단보도 데이터셋의 신호등 데이터의 평균 크기로 잡았다.)    
    
    <image src="https://user-images.githubusercontent.com/34594339/92205483-bcfc3c00-eec0-11ea-9e88-7162df41d5c8.png" width="50%">    
    
   <예시> 876x876 크기 안에 300x300 으로 리사이즈된 신호등 데이터     
   
<br>

### :mag_right: 결과물

:black_nib: 신호등에 대한 인식률이 올랐다. <br>
:black_nib: 작은 객체(신호등)은 완벽하게 인식이 되지 않았다.

### :point_right: [result](https://youtu.be/mKuDjEIbfsg)


<br>
</div>    
</details>

<br>
<details>    
<summary> :mag_right: Result Image </summary>    
<br>

<image src="https://user-images.githubusercontent.com/34594339/92569047-0b437d80-f2bb-11ea-8835-7330ae998777.png" width="50%">

<br>

#### YOLO3-tiny 마지막 학습에서 썼던 정사각형+300x300 사이즈의 데이터셋을 YOLOv4를 이용해 학습 시켰다.<br>
#### YOLOv3-tiny에 비해서 정확도가 높은 만큼이나 작은 물체도 상대적으로 잘 탐지됐다.

<br>
</div>    
</details>
<br>
