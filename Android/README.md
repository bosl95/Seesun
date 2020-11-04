# Seesun_android

## 01. Camera API

### Camera 1
- Android 5.0(API 레벨 21) 이전에 사용되었던 카메라 클래스
- 가장 많은 레퍼런스
- API 레벨 21 이상에서 지원 중단

<br>

- #### 문제점
	a. 셔터음 음소거 불가
	b. 서버로 전송되는 이미지 사이즈 문제
	c. 사이즈가 커지는 경우 발생하는 통신 문제
	d. 애뮬레이터에서 실행되지 않음

### ⇒  `CameraX`를 통해 문제 해결

<br>

### Camera 2
- Android 5.0(API 레벨 21) 이상에서 작동하는 새로운 카메라 클래스
- 전문가용 카메라를 지향하여 다양한 기능한다.
- 사용방법 복잡하다.

<br>

### Camera X
- Android 5.0(API 레벨 21) 이상에서 작동하는 새로운 카메라 클래스
- Camera2의 기능을 활용하면서 수명 주기를 인식하는 더 단순한 방식 사용
- 기기의 호환성 문제 해결할 수 있다.

<br>

## 02. Android Http Client Library

### 통신 라이브러리 조건

1. 스레드를 생성하여 1초 간격으로 사진을 촬영
2. 서버로 이미지를 전송하고, 서버로부터 문자열을 받아옴  ⇒  `비동기처리`
3. 서버로부터 받아온 데이터를 즉시 음성으로 변환하여 안내 ⇒   메인스레드와 데이터 공유

<br>

<p align='center'>
<image src='https://user-images.githubusercontent.com/34594339/98076599-e67e1780-1eb1-11eb-8c21-7cebe9a62eb0.png' width='70%'>
</p>

<br>

### Http 프로토콜 방식

1. HttpURLConnection
	- 가장  원시적인 형태
	- 자신이 원하는 방식으로 커스텀 가능
	- 자유도 높은 대신 직접 구현해야 하는 것 많음

2. HttpClient
	- Apache에서 제공
	- 1에 비해 다양한 API 지원
	- 1보다 구현 쉽지만, 여전히 불편하다.

3. OkHttp
	- Square의 오픈소스 프로젝트
	- 1,2에 비해 직관적이고 사용 편리
	- 동기와 비동기처리 쉽게 가능
	- 스레드간 공유 불가(데이터 공유를 위해 Handler 사용)

<br>

### :point_right:  OkHttp 라이브러리 선택
1. 레퍼런스가 많다.
2. 비동기 처리가 간편하다.
3. 스레드간 공유 필요없이 데이터를 받아온 즉시 처리한다. 
	- ImageView - main thread에서 접근할 필요가 없다.

<br>

# HTTP 뿌시기

<br>

참고 원문
- https://qiita.com/Reyurnible/items/33049c293c70bd9924ee
- https://pluu.github.io/blog/android/2016/12/25/android-network/ (가장 많이 참고한 자료)
- http://static.springsource.org/spring-android/docs/1.0.x/reference/html/rest-template.html
- https://code.google.com/p/google-http-java-client/wiki/Android
- https://github.com/square/retrofit
- http://loopj.com/android-async-http/
- https://github.com/kevinsawicki/http-request
- http://droidparts.org/
- https://d2.naver.com/helloworld/377316
- https://sjh836.tistory.com/141
- https://woowabros.github.io/experience/2019/12/20/feign2.html

<br>

## HTTP(Hyper transfer protocol)
클라이언트와 서버 양쪽에서 통신할 수 있도록 구현해야 하는 기본 통신 프로토콜
- 요청과 응답, 세션, 캐싱, 인증 등을 다룬다.

<br>

## HTTPS를 사용하는 이유

1. 기밀성 : 두 참여자간의 통신을 보호
2. 무결성 : 변조되지 않은 정보로 목적지 도달
3. 인증 : 웹사이트의 진위 여부 확인

<br>

## 통신 라이브러리 맛보기

- Android에서의 통신은 HttpClient이다.
- DefaultHttpClient와 Apache HTTP Client, AndroidHttpClient 등 HttpClient를 변형하여 사용되고 있었다. 
- 사용법이 복잡한 HttpUrlConnection의 대안으로 Google의 Volley 등장하였다.
  - HttpUrlConnection을 사용했었으나 내부적으로는 Okhttp를 많이 사용한다.
  - 그러나 HttpClient의 몇가지 버그로 인해 Google의 HttpUrlConnection이 권장된다.
- Square의 Okhttp, Retrofit
  - Retrofit은 OkHttp를 더 쉽게 사용할 수 있게 하는 라이브러리
- Android 5.1에서 Httpclient가 Deprecated된 후, Httpclient를 많이 의존하던 Volley도 함께 Deprecated 되었다.
- Square에서 만들어진 OkHttp와 그 래퍼인 Retrofit만 남았다 ⇒ Retrofit에서는 통신 클라이언트 부분과 콜백 형식 등을 플러그로 변경하는 것이 가능해 인기가 많았다.

<br>

## 통신 라이브러리 릴리즈 순서

1. 2007/11/05 : Android가 발표
2. 2011/09/29 : HttpURLConnection을 권장하는 블로그 등장
3. 2013/05/06 : OkHttp 1.0.0이 릴리즈 
4. 2013/05/14 : Retrofit 1.0.0이 릴리즈 
5. 2013/05/21 : Volley가 릴리즈
6. 2016 : Android6.0에서 HttpClient가 삭제
7. 2016/03/12 : Retrofit2가 릴리즈

<br>

## DefaultHttpClient

- 지금은 Android 최신 버전에서 지워졌기 때문에 사용하려면 build.gradle에 uselibrary를 추가해야한다.

      // app/build.gradle
      android {
          useLibrary 'org.apache.http.legacy'
      }

- HttpClient는 HttpClient를 인스턴스를 만들고 Get을 요청하면 HttpGet, Post를 요청하면 HttpPost를 사용.  Response는 HttpResponse 형식으로 되돌아온다.
- 비동기 처리 ⇒ AsyncTask를 감싸 처리

		// WeatherRepositoryImplHttpClient.java
		public class WeatherRepositoryImplHttpClient implements WeatherRepository {
		    public static final String TAG = WeatherRepositoryImplHttpClient.class.getSimpleName();

		    @Override
		    public void getWeather(final RequestCallback callback) {
		        new AsyncTask<Void, Void, Weather>() {
		            // 처리 전에 호출되는 메소드
		            @Override
		            protected void onPreExecute() {
		                super.onPreExecute();
		            }

		            // 처리를 하는 메소드
		            @Override
		            protected Weather doInBackground(Void... params) {
		                final HttpClient httpClient = new DefaultHttpClient();
		                final HttpGet httpGet = new HttpGet(uri.toString());
		                final HttpResponse httpResponse;
		                try {
		                    httpResponse = httpClient.execute(httpGet);
		                    final String response = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		                    return new Gson().fromJson(response, Weather.class);
		                } catch (IOException e) {
		                    return null;
		                }
		            }

		            // 처리가 모두 끝나면 불리는 메소드
		            @Override
		            protected void onPostExecute(Weather response) {
		                super.onPostExecute(response);
		                // 통신 실패로 처리
		                if (response == null) {
		                    callback.error(new IOException("HttpClient request error"));
		                } else {
		                    Log.d(TAG, "result: " + response.toString());
		                    // 통신 결과를 표시
		                    callback.success(response);
		                }
		            }
		        }.execute();
		    }

<br>

## HttpURLConnection

-   HttpURLConnection은 URL을 생성하고 연결. 응답은 버퍼 형식으로 받기 위해 스스로 String 변환한다.
-   HTTPClient의 단점

	<image src='https://lh6.googleusercontent.com/ef6lc0xY_JTCOY_Cd8s3n5Emp-11tEEPiis-QlKQVFYM6syaehdhYes8t-GIZ3jTYMyH7mluX0bpZfGbJqGuApoeclw3nqB_omnqH6mP-VqSErBDKNmErf5waChlwqrmhJXNdn13' width='70%'>

-   Apache HttpClient의 최신 버전을 따라가지 않고 있다.
-   Gingerbread 미만에서는 HttpUrlConnection의 버그를 피하기 위해 Apache Httpclient를 사용할 것을 권고한다.
	
		// WeatherRepositoryImplHttpURLConnection.java
		public class WeatherRepositoryImplHttpURLConnection implements WeatherRepository {
		    public static final String TAG = WeatherRepositoryImplHttpURLConnection.class.getSimpleName();

		    @Override
		    public void getWeather(final RequestCallback callback) {
		        new AsyncTask<Void, Void, Weather>() {
		            // 처리 전에 호출되는 메소드
		            @Override
		            protected void onPreExecute() {
		                super.onPreExecute();
		            }

		            // 처리를 하는 메소드
		            @Override
		            protected Weather doInBackground(Void... params) {
		                final HttpURLConnection urlConnection;
		                try {
		                    URL url = new URL(uri.toString());
		                    urlConnection = (HttpURLConnection) url.openConnection();
		                    urlConnection.setRequestMethod("GET");
		                    urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		                } catch (MalformedURLException e) {
		                    return null;
		                } catch (IOException e) {
		                    return null;
		                }
		                final String buffer;
		                try {
		                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
		                    buffer = reader.readLine();
		                } catch (IOException e) {
		                    return null;
		                } finally {
		                    urlConnection.disconnect();
		                }
		                if (TextUtils.isEmpty(buffer)) {
		                    return null;
		                }
		                return new Gson().fromJson(buffer, Weather.class);
		            }

		            // 처리가 모두 끝나면 불리는 메소드
		            @Override
		            protected void onPostExecute(Weather response) {
		                super.onPostExecute(response);
		                // 통신 실패로 처리
		                if (response == null) {
		                    callback.error(new IOException("HttpURLConnection request error"));
		                } else {
		                    Log.d(TAG, "result: " + response.toString());
		                    // 통신 결과를 표시
		                    callback.success(response);
		                }
		            }
		        }.execute();
		    }
		}

-   HttpRequest
	-   HttpURLConnection의 사용성을 개선.
	-   30kb의 작은 크기와 클래스 하나 밖에 없는 단순한 구조, 간결한 사용법
	-   단점 : HttpURLConnection만 사용하기 때문에 Gingerbread 이하 버전에서는 HttpURLConnection의 버그 때문에 사용하기 어렵다.

<br>

##  Volley
- 비동기 처리를 감싸주고 있기 때문에 AsyncTask 등을 사용하지 않아도 된다.

		// WeatherRepositoryImplVolley.java
		public class WeatherRepositoryImplVolley implements WeatherRepository {
		    public static final String TAG = WeatherRepositoryImplVolley.class.getSimpleName();

		    RequestQueue queue;

		    public WeatherRepositoryImplVolley(Context context) {
		        queue = Volley.newRequestQueue(context);
		    }

		    @Override
		    public void getWeather(final RequestCallback callback) {
		        final JsonObjectRequest request =
		                new JsonObjectRequest(uri.toString(), null, new Response.Listener<JSONObject>() {
		                    @Override
		                    public void onResponse(JSONObject response) {
		                        Log.d(TAG, "result: " + response.toString());
		                        final Weather weather = new Gson().fromJson(response.toString(), Weather.class);
		                        callback.success(weather);
		                    }
		                }, new Response.ErrorListener() {
		                    @Override
		                    public void onErrorResponse(VolleyError error) {
		                        callback.error(error);
		                    }
		                });
		        queue.add(request);

		    }
		}

<br>

## OkHttp
- 통신을 동기화로할지 비동기를 할 지 선택할 수 있다. 
- 그러나 thread를 넘나들 수 없어 `Handler`를 사용한다. ⇒ 더 조사해봐야겠다.


		// WeatherRepositoryImplOkHttp3.java
		public class WeatherRepositoryImplOkHttp3 implements WeatherRepository {
		    public static final String TAG = WeatherRepositoryImplOkHttp3.class.getSimpleName();

		    private Handler handler = new Handler();

		    @Override
		    public void getWeather(final RequestCallback callback) {
		        final Request request = new Request.Builder()
		                // URL 생성
		                .url(uri.toString())
		                .get()
		                .build();
		        // 클라이언트 개체를 만듬
		        final OkHttpClient client = new OkHttpClient();
		        // 새로운 요청을 한다
		        client.newCall(request).enqueue(new Callback() {
		            // 통신이 성공했을 때
		            @Override
		            public void onResponse(Call call, Response response) throws IOException {
		                // 통신 결과를 로그에 출력한다
		                final String responseBody = response.body().string();
		                Log.d(TAG, "result: " + responseBody);
		                final Weather weather = new Gson().fromJson(responseBody, Weather.class);
		                handler.post(new Runnable() {
		                    @Override
		                    public void run() {
		                        callback.success(weather);
		                    }
		                });
		            }

		            // 통신이 실패했을 때
		            @Override
		            public void onFailure(Call call, final IOException e) {
		                handler.post(new Runnable() {
		                    @Override
		                    public void run() {
		                        callback.error(e);
		                    }
		                });
		            }
		        });
		    }
		}

- OkHttpClient 장단점

	<image src='https://lh4.googleusercontent.com/cZtdjMdqwpWCWqXcLtLIJu9bwPO1eD2pEqXDB_bbyqwO08ariSl_VVlkAj3BLwreTI88ntJ3CdQEqTQdQAsRuDmmh_2Y_Sqb5ZI69mwQ7auQGmVuf8nPP2oX0VxxK4xlQ9x0iVH5' width='80%'>

<br>

## Retrofit
- Retrofit은 annotation을 사용하여 코드를 생성하기 때문에 이를 위한 인터페이스를 만든다.
- annotation이란? ([https://galid1.tistory.com/534](https://galid1.tistory.com/534))

		// WeatherRepositoryImplRetrofit2.java
		public class WeatherRepositoryImplRetrofit2 implements WeatherRepository {
		    public static final String TAG = WeatherRepositoryImplRetrofit2.class.getSimpleName();

		    private final WeatherService service;

		    public WeatherRepositoryImplRetrofit2() {
		        final Retrofit retrofit = new Retrofit.Builder()
		                .baseUrl(new Uri.Builder().scheme(SCHEME).authority(AUTHORITY).build().toString())
		                .client(new OkHttpClient())
		                .addConverterFactory(GsonConverterFactory.create())
		                .build();
		        service = retrofit.create(WeatherService.class);
		    }

		    @Override
		    public void getWeather(final RequestCallback callback) {
		        service.getWeather(130010).enqueue(new Callback<Weather>() {
		            @Override
		            public void onResponse(Call<Weather> call, Response<Weather> response) {
		                Log.d(TAG, "result: " + response.body().toString());
		                callback.success(response.body());
		            }

		            @Override
		            public void onFailure(Call<Weather> call, Throwable error) {
		                callback.error(error);
		            }
		        });
		    }

		    private interface WeatherService {
		        @GET(PATH)
		        Call<Weather> getWeather(@Query("city") int city);
		    }

		}

<br>

- Retrofit의 annotation 활용
	
	<image src='https://lh3.googleusercontent.com/HOfjkf1vPPuMXmFHDvDDTyIJV7c5Vmu06WkfrbCKsfD3QF6bHiqGE5wV8pra9LoVzVbHMqfn06dLWseOq_LrpcW_amtLnkPffcbuUSu7eRPeR9iDaGJwSPMI84PDP1XZZ6_GOObT' width='70%'>

<br>

-   단점
	-   런타임에 수행하는 작업이 많아서 성능 저하가 우려된다.
		- 내부적으로 Dynamic Proxy를 사용하고 런타임에 annotation을 파싱한다. 두 번째 호출부터는 annotation에 있는 정보가 캐시되지만 첫 번째 호출은 길어질 수 있다.


<br>

## Apache HttpClient
-   버전이 불명확하고 더 이상 버전이 업데이트되지 않는다.
-   다양하고 세부적인 사용법까지 관여하는 고수준의 라이브러리
-   Apache HttpClient 4.1 미만에서는 ThreadSafeClientConnManager라는 클래스를 생성할 때 HTTP와 HTTPS에 대한 디폴트 포트와 SocketFactory 클래스를 따로 등록해야 했는데, 4.1부터는 매개변수가 없는 디폴트 생성자를 추가로 지원해서 이 클래스를 쓰기가 훨씬 편해졌다.
-   여러 환경에서 사용되기 때문에 특별히 Android의 SDK를 의식하지는 않는 듯하다.
-   ApacheHttpClient을 사용하기 어려운 이유

	<image src='https://lh5.googleusercontent.com/MZVXIFz3_5Vi7iCMnpL6rBFBFThrCmDrf-1ubGjJP-Ik_iy9RPcvys7mjQot0IDsprbGV-rv_DKm_0zVmEHZHuAhwpndg3MiaQSxoBcfmqBRPhctgbAocyuFpA1UEMwT5yO53tY_' width='70%'>

<br>

## Spring-Android RestTemplate

-   서버 프레임워크로 유명한 Spring의 RestClient 모듈을 Android 용으로 변경
-   Android의 각 버전별로 잘 대응한다.
-   비동기 처리를 지원하지는 않는다 ⇒ Android의 기본 멀티 스레드 지원 클래스인 AysncTask를 함께 사용하여 문제를 해결할 수 있다.
-   @Rest, @Get과 같은 annotation을 사용
-   비동기 처리를 도와주는 Robospice 라이브러리도 RestTemplate을 지원한다.    
-   단점
	1.  비교적 용량을 많이 차지, 기능이 많다.
	2.  spring-android-rest-template 모듈이 의존하고 있는 spring-android-core 모듈은 순전히 기존 Spring과 동일한 Exception이나 인터페이스 구조를 유지하기 위한 것인데, 기존 sprint rest template을 그대로 쓰고 싶지 않은 사람에게 의존성때문에 생기는 용량이 아까울 수 있다.
	3.  순수 JSON만을 파싱하려는 용도라면 다양한 converter 확장 기능도 필요가 없다.
	4.  타임아웃을 설정하기 번거롭다
    	-   Android 버전에 따라서 SimpleClientHttpRequestFactory 혹은 HttpComponentsClientHttpRequestFactory를 연결에 사용하는데, 두 클래스 모두 setConnectTimeout(int) 메서드와 setReadTimeout(int) 메서드를 가지고 있지만 인터페이스인 ClientHttpRequestFactory 인터페이스에는 정의되어 있지 않기 때문에 캐스팅을 해야 한다. ( 잘 모르겠음 )

- RestTemplate는 Java와 Android 용을 따로 제공한다. ⇒ 차이점에 대해 주의하자!

	<image src='https://lh5.googleusercontent.com/z7FeEjv3I9tzuv715dSE_imI-5pZ0OPYpDWcu4j6GI5hWu90L3_AxWt6vBWsVG58cERWlF0hTKUbysCDm2GKS-p2jbs0ppH1cgmZtu2w9Ost8jiHZs6AZNPWtdEcNQHcUDnVrux8' width='70%'>

<br>

## Google Htpp Java Client Android

-   Google Http Java Client에 Android용 클래스(androidutils, androihttp)만을 추가한 라이브러리
-   JSON, XML, ATOM 등 다양한 파서를 지원
-   JSON Converter는 라이브러리 의존 없이 Android Json Util을 이용하여 자체 구현
-   GZIP Compression 지원, 비동기 메서드 실행, retry 정책 등이 기본 제공
-   단점
	-   일반 Java에서도 실행되는 모듈에 Android 클래스를 추가한 라이브러리인 만큼 용량이 크다
	-   ProGuard 사용을 권장한다. ProGuard를 사용하면 애플리케이션 크기를 최대 88% 줄일 수 있다.

<br>

## Loopj Asynchronous Http Client
-   크기가 25kb로 비교적 작으며, 비동기를 기본으로 지원하는 라이브러리
-   Instagram, Pinterest 등 유명한 애플리케이션에 사용
-   용량을 적게 차지하면서도 Request Cancellation. back-off Policy 지원
-   Binary File Converter, SharedPreferences를 이용한 Persistent Cookie Store 등 유용한 기능을 많이 제공한다.

<br>

## Android 구현체가 갖추어야 할 요건
1.  버전별 에러에 미리 대처한다
	-   인증 방식이나 서버의 HTTP 옵션 변경으로 에러가 발생할 여지가 있는 부분은 파악
2.  버전에 따라 추가된 기능을 의식하고 대처  한다.
	-   캐싱이나 Gzip 압축 등
3.  파서에 독립적으로 통신 모듈을 구성한다.
	-   Spring RestTemplate의 Converter 모듈 등
	-   향후 애플리케이션의 성능 개선이나 경량화 과정을 거치면서 라이브러리를 변경하거나 추가할 수 있다.
	-   모든 서비스 모듈마다 파서가 침범적으로 쓰였다면 그런 변경이 쉽지 않다
4.  권고 사안에 따라 Apache HttpClient에 대한 직접 의존을 줄인다.
	-   하위 호환성을 위해 당장 해당 패키지를 없애지는 않겠지만, 갈수록 이 모듈의 장점이 퇴색할 것으로 예상
