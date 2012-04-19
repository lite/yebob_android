Android SDK起步

设置 Android SDK

* 在你的Eclipse项目中右键单击 -> Properties -> Java Build Path -> Libraries
* 点击 Add External JARs
* 点击选择YebobApiLib.jar并点击打开
* 点击OK

确信你已经启用INTERNE权限. 保证AndroidManifest.xml中包含:

	<uses-permission android:name="android.permission.INTERNET" />

## 配置YebobSDK

用Eclipse（或你喜欢的IDE）打开YebobSDK的Android演示项目，您就可以开始使用YebobSDK
。在第一次使用YebobSDK前，请加入以下代码。我们建议放置在您Main Activity中的
onCreate方法。本段代码定义了应用程序的信息和Keys。代码如下所示。

	import com.yebob.api.Yebob;
	 
	static final String API_KEY = "37624c98-35d3-4bbe-8e83-3fb7ac92f06c";
	static final String API_SECRET = "b1d422ef-a3f4-4fc5-9739-77210e96b012";
	static final String USER_OBJECT_NAME = "user";
	static final int API_VERSION = 0;
	 
	static final String API_URL_FORMAT = "api.yebob.com";
	static final String PUSH_API_URL_FORMAT = "push.yebob.com";

将来需要时，可以在这里找到应用程序的Keys。

## 保存一个分数到Yebob云服务器

Android SDK代码

	String gameId = "123";
	YBUploadScore(gameId, "player", System.currentTimeMillis());	


## 从Yebob读取排行榜数据

Android SDK代码

	String gameId = "123";
	YBGetRanking(gameId);


## 打开Yebob社交网络页面

Android SDK代码

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