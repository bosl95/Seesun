

# Yolov4 Server

## :heavy_check_mark: Contents
### 1. [GPU Cloud Service](#pushpin-gpu-cloud-service)
### 2. [Use Flask Framework And Ngrok](#pushpin-use-flask-framework)
### 3. [Make YOLOv4 API](#pushpin-make-yolov4-api)
### 4. [Detect Object](#pushpin-detect-object)

<br>
<br>

### :hourglass_flowing_sand: To do
- [ ] *Dectect using AWS gpu Sever(If I can pay for it..)* :joy:

<br>

## :pushpin: GPU Cloud Service

### :pencil2: AWS (Amazon Web Services) <br>
#### AWS는 Amazon에서 개발한 클라우드 컴퓨팅 플랫폼이다. <br>
#### GPU 서버를 사용하기 위해서는 과금이 부과된다. ⇒ 가격이 부담되기 때문에 사용하기 어렵고 Amazon에서 무료로 제공해주는 AWS pretier로는 사용할 수 없다. ( cpu만 사용 가능) <br>

<br>

### :pencil2: Google Colab
#### Colab 또한 GPU를 제공해주는 클라우드 기반의 Jupyter notebook 개발 환경이다. <br>
#### Colab은 이미 모델을 구축하는 과정에서 Colab Pro로  유료 구독 서비스를 이용 중이다. (오래 사용하면 자동으로 세션이 끊기기 때문)<br>
#### Colab Pro도 오래 사용하거나 과한 GPU 사용은 램 용량 초과로 런타임 종료가 되긴 하지만, 그래도 서버로 이용이 가능하다!! <br>

<br>

## :pushpin: Use Flask Framework and Ngrok

<details>
<summary> :point_right: Django vs Flask </summary>
<br>

- ### Django : Full Stack Web Framework <br>
	> 1. 한 프로젝트 내 여러 app이 존재할 수 있다.
	> 2. MVC 기반 패턴, ORM 등과 같은 강력한 기능을 제공
	> 3. 자동으로 관리자 화면을 구성하여 데이터 생성/변경이 용이
	> 4. 무거운 프레임워크


- ### Flask : Micro Framework <br>
	> 1. 단순하고 매우 가벼운 웹 프레임워크
	> 2. 유연한 기능 확장성
	> 3. ORM 기능을 사용할 경우 SQLAlchemy 와 같은 패키지 설치 필요
	> 4. 자유도가 높다! ⇒  보안의 문제에 주의

<br>

### :point_right: 서버에서 YOLO 모델을 이용해 탐지 결과만 전송해주면 되는 단순한 기능으로 "Flask Framework"를 이용하기로 했다.
<br>
</div>
</details>
<br>

### :pencil2: Ngrok

#### 포트 포워딩과 같은 네트워크 환경 설정 변경없이 로컬에 실행중인 서버를 안전하게 외부에서 접근 가능하도록 해주는 도구 <br>
#### 무료 버전은 분당 40 커넥션이 가능하다. 60회의 커넥션이 필요하므로 무조건 Pro 버전을 이용해야했다. (거지가 되어간다... :sob:)

<details>
<summary> :point_right: How to use Ngrok </summary>
<br>

	!./ngrok authtoken your_authentication
	get_ipython().system_raw('./ngrok http -subdomain=your_domain 5000 &')

### get_ipython().system_raw()는 Google Colab에서 사용, Linux를 사용하는 경우는 명령어만 치면 된다.<br>
### subdomain은 본인이 설정한 도메인 이름을 적어주면 된다. (유료 계정에서 사용 가능하다.)<br>
<br>
</div>
</details>
<br>

### :rocket: start_server.py

- #### Flask 인스턴스를 생성하고 app 변수에 저장한다. (__name__은 "__main__"이 된다.)

		app = Flask(__name__)

- #### flask_ngrok 모듈을 이용하여 외부에서 로컬 서버에 접근 가능하도록 하는 url을 만들어준다.

		run_with_ngrok(app)	

- #### 내가 만든 yolo api를 통해 서버를 시작함과 동시 yolov4 모델을 읽어온다.

		if __name__ == "__main__":
			net, cls, colors = yolo.load_model()
			app.run()
		
- #### Android에서 사진을 전송하면 uploads 폴더에 test.jpg로 임시저장하고 yolo api를 통해 detect한 정보를 전달한다.
	
		@app.route("/file_upload", methods=['GET',  'POST'])
		def post():
		if  (request.method=='POST'):
		f = request.files['file']
		f.save(f'uploads/test.jpg')
		return yolo.detect(net, cls, colors, os.getcwd())
		
<br>

## :pushpin: Make YOLOv4 API

### YOLO 모델을 미리 불러오고 안드로이드에서 이미지를 받으면 바로 탐지할 수 있도록 하는 YOLO API를 만들었다. <br>
### yolo_api 모듈은 darknet 폴더 안에서 수행된다. 그러므로 darknet 모듈을 사용하기 위해서 os.chdir()을 이용해 현재 디렉토리를 이동해줘야한다. <br>

### libdarknet.so를 읽기 위하여 Darknet path를 설정

	os.environ['DARKNET_PATH'] = os.getcwd()+'/darknet'

<br>

## :pushpin: Detect Object

### 안드로이드 이미지에서 탐지된 객체의 좌표를 출력 및 안드로이드에게 전송한다.

<image src="https://user-images.githubusercontent.com/34594339/93883742-efa98f80-fd1c-11ea-8a08-b3cb9a747582.png" width="70%">