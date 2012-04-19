package com.yebob.apidemo;

import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.anddev.andengine.ui.activity.BaseSplashActivity;
import android.app.Activity;

public class YebobDemoActivity extends BaseSplashActivity {
	
	private static final int SPLASH_DURATION = 1;
	private static final float SPLASH_SCALE_FROM = 0.9f;
	
	@Override
	protected ScreenOrientation getScreenOrientation() {
		return ScreenOrientation.PORTRAIT;
	}

	@Override
	protected float getSplashDuration() {
		return SPLASH_DURATION;
	}

	@Override
    protected float getSplashScaleFrom() {
		return SPLASH_SCALE_FROM;
    }
	
	@Override
	protected Class<? extends Activity> getFollowUpActivity() {
		return MenuActivity.class;
	}

	@Override
	protected IBitmapTextureAtlasSource onGetSplashTextureAtlasSource() {
		return new AssetBitmapTextureAtlasSource(this, "gfx/sky.png");
	}
}