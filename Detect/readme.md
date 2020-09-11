


# Detect  

## :heavy_check_mark: Contents
### 1. [Use YOLO In Tensorflow](#pushpin-use-yolo-in-tensorflow)
### 2. [Keras Detect](#pushpin-keras-detect)
### 3.  [YOLOv4 tensorflow lite](#pushpin-yolov4-tensorflow-tflite)

<br>
<br>

### :hourglass_flowing_sand: To do
- [x] *Transform YOLO for using Tensorflow (.h5 file)*
- [x] *Set the Tesorflow version of transformed YOLO*
- [x] *Transform YOLO for using android (pb file)*

<br>
  
## :pushpin: Use YOLO in Tensorflow
  
#### :pencil2: ckpt 파일 : 모델의 변수(가중치)인 체크포인트 파일
- 텐서학습된 모델의 구조를 제외한 변수들을 담고 있는 파일.
- 즉, 모델의 가중치만 담고 있는 파일. 모델에 대한 메타 정보를 담고 있어 **재학습**이 가능

#### :pencil2: pb 파일 : 모델의 변수 + 구조로 이루어진 바이너리 파일
 - 모델 구조와 가중치 값이 합쳐진 파일. 재학습이 불가능하다.
 TensorRT를 위해서도 사용될 수 있다.
 
#### :grey_question: TensorRT란
-  GPU 상에서 딥러닝 모델을 추론하는 과정에서 대기시간을 최소화하고 처리량을 극대화 할 수 있도록 도와주는 최적화 라이브러리
	 
#### :pencil2:  pbtxt 파일 : pb 파일을 읽을 수 있는 텍스트 파일, 모델 구조 파악 가능
  - pbtxt는 사람이 읽을 수 있는 형식이므로 디버깅 및 편집에 적합하다. <br>
  - 하지만 텐서플로우 그래프의 구조를 담은 pbtxt는 엄청난 정보를 담고 있기 때문에 파일 용량이 크다.

<br>  

  ## :pushpin: keras detect

### :pencil2: yolo weight를 keras 모델로 변형 ⇒ ``.h5 파일`` 생성

<details>
<summary>  :point_right: Click </summary>
<br>
<image src="https://user-images.githubusercontent.com/34594339/92692365-03e5a800-f37f-11ea-9679-2cf1ebdd4d8b.png" width="70%">

<br>

#### Android에서 사용할 수 있는 버전인 Tensorflow 1.6.0을 사용하여 변환을 시도. <br>
#### 파이썬에서 영상을 테스트할 수 있었다.

</div>
</details>
<br>

### :rocket: Detect.py
	
#### .h5 파일을 통해 파이썬으로 이미지, 비디오를 detection

#### :x: 안드로이드에서 구동하기 위해서는 tensorflow의 버전에 주의 :x:


<details>
<summary>  :point_right: Click </summary>
<br>

Link : https://youtu.be/wBbyAQJkaNI

</div>
</details>
<br>

:round_pushpin: Source : https://github.com/Ma-Dan/keras-yolo4 <br>
:round_pushpin: Source : https://github.com/qqwweee/keras-yolo3 <br>


<br>

## :pushpin: YOLOv4 Tensorflow TFLite

#### :zap: Convert Darknet weight to tensorflow (.pb file)

	python save_model.py --weights ./data/yolov4.weights --output ./checkpoints/yolov4-512 --input_size 512 --model yolov4

<br>

#### :zap: Convert Darknet weight to Tensorflow Lite (.tflite file)

	python convert_tflite.py --weights ./checkpoints/yolov4-512 --output ./checkpoints/yolov4-512.tflite

<br>

:round_pushpin: Source : https://github.com/hunglc007/tensorflow-yolov4-tflite

<br>

<details>
<summary>  :collision: ERROR:collision: </summary>
<br>

<image src="https://user-images.githubusercontent.com/34594339/92444282-99513280-f1ed-11ea-99c9-1e4daf8e4191.png" width="100%">

#### core/config.py line 14,  __C.YOLO.CLASSES = "./data/classes/coco.names"
#### ⇒ 알맞는 custom names로 설정

anchors는 yolov4에서 제공하는 anchors 그대로 사용하여서 별 이상이 없었는데, 
class는 4개 밖에 없어서 coco.names의 80개 클래스로 인식되어 pb 파일로 변환하는데 오류가 생겼었다.

</div>
</details>
<br>

<details>
<summary> :x: Version Error :x: </summary>
<br>

#### Android tesorflow version : https://mvnrepository.com/artifact/org.tensorflow/tensorflow-android <br>
#### Prerequisites of Tensorflow convert file : Tensorflow 2.3.0rc0
<br>

### :point_right: [Tensorflow](https://github.com/tensorflow/tensorflow/tree/master/tensorflow/examples/android)에서 제공되는 안드로이드 구현 소스는  tensorflow-lite를 compile 하여 사용<br>
### 이 버전과 내가 만든 케라스 모델의 텐서플로우 버전(*2.3.0rc0*)에서 충돌이 나는 듯하다. <br>

</div>
</details>
<br>

<details>
<summary> :mag: Detection result </summary>
<br>

<image src="https://user-images.githubusercontent.com/34594339/92913805-0838c080-f466-11ea-8fb0-6cc823b48cca.png" width="60%">

<br>

<image src="https://user-images.githubusercontent.com/34594339/92914015-3918f580-f466-11ea-9b07-28637788c2b3.png" width="70%">


</div>
</details>
<br>