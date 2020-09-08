
# Increase Accuracy

### 신호등 + 횡단보도 YOLO  모델 정확도 올리기    

:hourglass_flowing_sand: Goal
- [x] 횡단보도 정확도 올리기 
- [x] 횡단보도 + 신호등 데이터셋을 모두 합친 학습 모델 만들기.
- [ ]  횡단보도 / 신호등 을 탐지하는 학습 모델 정확도 올리기

:hourglass_flowing_sand: Task

- [x] 신호등 데이터셋 정사각형 형태로 학습    
- [x]  신호등 데이터셋을 실제 횡단보도 사진의 크기와 유사하게 resize해서 학습    
- [ ]  횡단보도 데이터셋의 신호등을 지우기
- [ ] 횡단보도 데이터셋의 신호등까지 라벨링해서 하기

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
⇒ yolov3에서 416*416 형태로 학습을 진행하기 때문에 정사각형 변형을 통해 정확도 향상을 확인    
    
'images' 폴더 대신에 들어갈 인풋 이미지 폴더 이름을 넣어주고, 'output' 폴더에 정사각형 형태의 이미지가 저장  
  <br>  
    
    
## :rocket: Resize certain ratio
#### :pencil: 이미지 사이즈를 일정 비율로 줄이기 ⇒ 0.5, 0.5로 비율로 줄임

 <image src="https://user-images.githubusercontent.com/34594339/91954309-96ad9380-ed3c-11ea-82f1-a83fa20af28d.png" width="80%">    
 
    
![image](https://user-images.githubusercontent.com/34594339/91967657-78e92a00-ed4e-11ea-986c-71bebdead81b.png)    
    
#### :heavy_exclamation_mark: convert 함수(꼭지점 ⇒ yolo  포맷 변환)에 shape를 전달해줄때 w, h 가 뒤바뀐다. :heavy_exclamation_mark:
<br>
    

## :rocket: resize 300x300.py   

#### :pencil: 300*300 크기의 사진으로 resize 해준다.
<br>

## :pushpin: Data augmentation

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
그래서 우리가 만든 신호등 데이터 셋을 횡단보도 데이터셋안의  신호등 데이터와 유사한 크기로 만들어주었다.   

1. 정사각형 형태로 리사이즈된 신호등 데이터셋 A    
    
   <image src="https://user-images.githubusercontent.com/34594339/92205369-81617200-eec0-11ea-9702-035496b8ccca.png" width="50%">    
    
   <예시> 이미지 크기 : 822x822    
2. A' = A를 300*300 크기로 바꿔준다. (횡단보도 데이터셋의 신호등 데이터의 평균 크기로 잡았다.)    
    
    <image src="https://user-images.githubusercontent.com/34594339/92205483-bcfc3c00-eec0-11ea-9e88-7162df41d5c8.png" width="50%">    
    
   <예시> 876x876 크기 안에 300x300 으로 리사이즈된 신호등 데이터     
   
<br>

### :mag_right: 결과물

:black_nib: 신호등에 대한 인식률이 올랐다. <br>
:black_nib: 작은 객체(신호등)은 여전히 인식이 되지 않았다.

### :point_right: [result](https://youtu.be/mKuDjEIbfsg)


 <br>
 </div>    
</details>
