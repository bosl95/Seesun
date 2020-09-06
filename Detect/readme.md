
# detect  
## [목차]
  1. [YOLO를 Tensorflow에서 사용하기](1.-tensorflow에서-사용하기)
  2. [keras detect ](#2.keras-detect)
  3. 

  
  ## 1. tensorflow에서 사용하기
  
  - ### ckpt 파일 : 모델의 변수(가중치)인 체크포인트 파일
	텐서학습된 모델의 구조를 제외한 변수들을 담고 있는 파일.
	즉, 모델의 가중치만 담고 있는 파일. 모델에 대한 메타 정보를 담고 있어 **재학습**이 가능

  - ### pb 파일 : 모델의 변수 + 구조로 이루어진 바이너리 파일
	 모델 구조와 가중치 값이 합쳐진 파일. 재학습이 불가능하다.
	 TensorRT를 위해서도 사용될 수 있다.
	 
	 ** TensorRT란? 
	 GPU 상에서 딥러닝 모델을 추론하는 과정에서 대기시간을 최소화하고 처리량을 극대화 할 수 있도록 도와주는 최적화 라이브러리
	 
  - ### pbtxt 파일 : pb 파일을 읽을 수 있는 텍스트 파일, 모델 구조 파악 가능
	  pbtxt는 사람이 읽을 수 있는 형식이므로 디버깅 및 편집에 적합하다.
	  하지만 텐서플로우 그래프의 구조를 담은 pbtxt는 엄청난 정보를 담고 있기 때문에 파일용량이 크다.
  
  ## 2. keras detect
출처 : https://github.com/qqwweee/keras-yolo3


- ### yolo weight를 keras 모델로 변형 ⇒ ``.h5 파일`` 생성

	  python convert.py -w darknet53.cfg darknet53.weights model_data/darknet53_weights.h5

- ### detect.py
	
	.h5 파일을 통해 파이썬으로 이미지, 비디오를 detection

안드로이드에서 구동하기 위해서는 tensorflow의 버전에 주의해야한다.
(충돌이 날 수 있다.)