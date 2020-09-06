# Data augmentation 

### 신호등 + 횡단보도 YOLO  모델 정확도 올리기    

:hourglass_flowing_sand: Goal
- [x] 횡단보도 정확도 올리기 
- [x] 횡단보도 + 신호등 데이터셋을 모두 합친 학습 모델 만들기.
- [ ]  횡단보도 / 신호등 을 탐지하는 학습 모델 정확도 올리기

:hourglass_flowing_sand: Task

- [x] 신호등 데이터셋 정사각형 형태로 학습    
- [ ]  신호등 데이터셋을 실제 횡단보도 사진의 크기와 유사하게 resize해서 학습    
- [ ]  횡단보도 데이터셋의 신호등을 지우기
- [ ] 횡단보도 데이터셋의 신호등까지 라벨링해서 하기

<details>    
<summary> :round_pushpin: Source </summary>    
<br>

1. https://github.com/Paperspace/DataAugmentationForObjectDetection      
2. albumentations Library 이용하기      
   https://github.com/albumentations-team/albumentations      
3. https://imgaug.readthedocs.io/en/latest/source/examples_bounding_boxes.html      
       
 <image src="https://user-images.githubusercontent.com/34594339/91954309-96ad9380-ed3c-11ea-82f1-a83fa20af28d.png" width="80%">    
    
 </div>    
</details>    

## check_label 
transform을 여러개 해야하기 때문에 라벨링 확인이 필수    
dir에 확인할 라벨링 데이터 폴더 이름을 넣어주면 라벨링된 이미지를 띄워준다.    
    
<details>    
<summary>1차 Data augmentation</summary>    
<br>
    
- 추가할 Augmentation Dataset    
1. 정사각형 사이즈의 횡단보도  데이터 (패딩)    
2. 정사각형 사이즈의 신호등 데이터 (패딩)    
3. 비율을 0.5로 
한 신호등 데이터     
    
- 참고한 자료    
https://github.com/aleju/imgaug    
    
## transform 
직사각형의 이미지를 정사각형 형태로 만들어주기    
⇒ yolov3에서 416*416 형태로 학습을 진행하기 때문에 정사각형 변형을 통해 정확도 향상을 확인    
    
'images' 폴더 대신에 들어갈 인풋 이미지 폴더 이름을 넣어줌    
'output' 폴더에 정사각형 형태의 이미지가 저장됨    
    
https://bhban.tistory.com/91    
    
    
    
## transform2 
이미지 사이즈를 일정 비율로 줄이기 ⇒ 0.5, 0.5로 비율로 줄임    
    
![image](https://user-images.githubusercontent.com/34594339/91967657-78e92a00-ed4e-11ea-986c-71bebdead81b.png)    
    
⇒ 이 경우는 convert 함수(꼭지점 ⇒ yolo  포맷 변환)에 shape를 전달해줄때 w, h 가 뒤바뀐다.    


<image src="https://user-images.githubusercontent.com/34594339/92190089-2d906200-ee9b-11ea-81ae-4c6126a731a5.png" width="70%"> 

 </div>    
</details>    
    
<details>    
<summary>2차 Data augmentation</summary>    
<br>
	
정사각형 형태의 신호등을 그냥 학습시켜도 신호등이 가깝지 않으면 인식이 잘 되지 않았다.    
그래서 우리가 만든 신호등 데이터 셋을 횡단보도 데이터셋안의  신호등 데이터와 유사한 크기로 만들어주었다.   

## resize300x300.py   
1. 정사각형 형태로 리사이즈된 신호등 데이터셋 A    
    
   <image src="https://user-images.githubusercontent.com/34594339/92205369-81617200-eec0-11ea-9702-035496b8ccca.png" width="50%">    
    
   <예시> 이미지 크기 : 822x822    
2. A' = A를 300*300 크기로 바꿔준다. (횡단보도 데이터셋의 신호등 데이터의 평균 크기로 잡았다.)    
    
    <image src="https://user-images.githubusercontent.com/34594339/92205483-bcfc3c00-eec0-11ea-9e88-7162df41d5c8.png" width="50%">    
    
   <예시> 876x876 크기 안에 300x300 으로 리사이즈된 신호등 데이터     
3. A'를 876x876 크기안에 붙여준다. ⇒ yolo에서는 416x416으로 학습된다.    
    
   <image src="https://user-images.githubusercontent.com/34594339/92205583-f8970600-eec0-11ea-8503-49b28613b4fc.png" width="50%">    
    
   <예시> 876x876 사이즈에 중앙에 위치시키고, yolo 학습사이즈인 416x416으로 변형했을때의 모습    
     
    
 </div>    
</details>
