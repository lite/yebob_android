package com.yebob.apidemo;

import org.anddev.andengine.engine.camera.Camera;
import org.json.JSONException;
import org.json.JSONObject;

import com.yebob.api.Error;

import android.content.Context;

public class GameUtils {
	private static final int CAMERA_WIDTH = 240;
	private static final int CAMERA_HEIGHT = 360;

	public static class CameraFactory {
		public static Camera getDefaultCamera(Context context) {
	        return new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
	    }
	}
	public static float getWidth() {
		return CAMERA_WIDTH;
	}
	
	public static float getHeight() {
		return CAMERA_HEIGHT;
	}
}
