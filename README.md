Android SDK��

���� Android SDK

* �����Eclipse��Ŀ���Ҽ����� -> Properties -> Java Build Path -> Libraries
* ��� Add External JARs
* ���ѡ��YebobApiLib.jar�������
* ���OK

ȷ�����Ѿ�����INTERNEȨ��. ��֤AndroidManifest.xml�а���:

	<uses-permission android:name="android.permission.INTERNET" />

## ����YebobSDK

��Eclipse������ϲ����IDE����YebobSDK��Android��ʾ��Ŀ�����Ϳ��Կ�ʼʹ��YebobSDK
���ڵ�һ��ʹ��YebobSDKǰ����������´��롣���ǽ����������Main Activity�е�
onCreate���������δ��붨����Ӧ�ó������Ϣ��Keys������������ʾ��

	import com.yebob.api.Yebob;
	 
	static final String API_KEY = "37624c98-35d3-4bbe-8e83-3fb7ac92f06c";
	static final String API_SECRET = "b1d422ef-a3f4-4fc5-9739-77210e96b012";
	static final String USER_OBJECT_NAME = "user";
	static final int API_VERSION = 0;
	 
	static final String API_URL_FORMAT = "api.yebob.com";
	static final String PUSH_API_URL_FORMAT = "push.yebob.com";

������Ҫʱ�������������ҵ�Ӧ�ó����Keys��

## ����һ��������Yebob�Ʒ�����

Android SDK����

	String gameId = "123";
	YBUploadScore(gameId, "player", System.currentTimeMillis());	


## ��Yebob��ȡ���а�����

Android SDK����

	String gameId = "123";
	YBGetRanking(gameId);


## ��Yebob�罻����ҳ��

Android SDK����

	import com.yebob.api.YebobUI;

	public class FriendsActivity extends YebobUI
	{
	    @Override
	    public void onCreate(Bundle savedInstanceState)
	    {
		super.onCreate(savedInstanceState);
		//super.loadUrl("file:///android_asset/www/index.html");
		super.loadUrl("http://m.yebob.com");
	    }
	}