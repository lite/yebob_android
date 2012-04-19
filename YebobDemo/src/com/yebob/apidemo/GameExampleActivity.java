package com.yebob.apidemo;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import java.io.IOException;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.physics.PhysicsHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ParallaxBackground;
import org.anddev.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.buildable.builder.BlackPawnTextureBuilder;
import org.anddev.andengine.opengl.texture.buildable.builder.ITextureBuilder.TextureAtlasSourcePackingException;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.opengl.view.RenderSurfaceView;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class GameExampleActivity extends BaseGameActivity implements
		IOnSceneTouchListener {
	private static final float DEMO_VELOCITY = 100.0f;

	public static final GameScore score = new GameScore();
	
	private Camera camera;
	private TextureRegion sky;
	private TextureRegion grass;
	private TextureRegion star;
	private TiledTextureRegion shipRegion;
	private ParallaxBackground background;
	private TextView scoreView;
	private BitmapTextureAtlas shipTexture;
	private static Music musicSound;

	@Override
	public Engine onLoadEngine() {

		this.camera = GameUtils.CameraFactory.getDefaultCamera(this.getApplicationContext());
		RatioResolutionPolicy resolutionPolicy = new RatioResolutionPolicy(
				camera.getWidth(), camera.getHeight());
		final EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.PORTRAIT, resolutionPolicy, this.camera);
		
		engineOptions.setNeedsSound(true);
		engineOptions.setNeedsMusic(true);
		return new Engine(engineOptions);
	}

	@Override
	protected void onSetContentView() {
		final RelativeLayout relativeLayout = new RelativeLayout(this);
		final FrameLayout.LayoutParams relativeLayoutLayoutParams = new FrameLayout.LayoutParams(
				FILL_PARENT, FILL_PARENT);

		scoreView = new TextView(getApplicationContext());
		Typeface font = Typeface.createFromAsset(getAssets(),
				"font/pf_tempesta_five.ttf");
		scoreView.setTypeface(font);

		scoreView.setPadding(30, 30, 0, 0);
		scoreView.setTextColor(Color.BLACK);
		scoreView.setGravity(Gravity.CENTER);

		this.mRenderSurfaceView = new RenderSurfaceView(this);
		this.mRenderSurfaceView.setRenderer(this.mEngine);

		final LayoutParams surfaceViewLayoutParams = new RelativeLayout.LayoutParams(
				super.createSurfaceViewLayoutParams());
		surfaceViewLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		relativeLayout
				.addView(this.mRenderSurfaceView, surfaceViewLayoutParams);
		relativeLayout.addView(scoreView, this.createAdViewLayoutParams());

		this.setContentView(relativeLayout, relativeLayoutLayoutParams);
	}

	private LayoutParams createAdViewLayoutParams() {
		final LayoutParams adViewLayoutParams = new LayoutParams(WRAP_CONTENT,
				WRAP_CONTENT);
		adViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		adViewLayoutParams.addRule(RelativeLayout.ALIGN_LEFT);
		return adViewLayoutParams;
	}

	@Override
	public void onLoadResources() {
		try {
			BuildableBitmapTextureAtlas texture = new BuildableBitmapTextureAtlas(
					1024, 1024);

			sky = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
					texture, this, "gfx/sky.png");
			grass = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
					texture, this, "gfx/grass.png");
			star = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
					texture, this, "gfx/star.png");

			texture.build(new BlackPawnTextureBuilder(0));
			mEngine.getTextureManager().loadTextures(texture);

			shipTexture = new BitmapTextureAtlas(32, 32, TextureOptions.BILINEAR);
			shipRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(shipTexture, this, "gfx/ship.png", 0, 0, 1, 1);
			mEngine.getTextureManager().loadTextures(shipTexture);

			musicSound = MusicFactory.createMusicFromAsset(
					this.mEngine.getMusicManager(), this, "snd/music.ogg");
			musicSound.setLooping(true);

		} catch (TextureAtlasSourcePackingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.mEngine.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void reset() {}

            @Override
            public void onUpdate(float pSecondsElapsed) {
            	runOnUpdateThread(new Runnable() {
                    public void run() {
                    	refreshScore();
                    }
            	});
            }
	    });

		Scene scene = new Scene();
		Sprite skySprite = new Sprite(0, 0, camera.getWidth(), camera.getHeight(), sky);
		background = new ParallaxBackground(0, 0, 0);
		background.attachParallaxEntity(new ParallaxEntity(0, skySprite));
		scene.setBackground(background);

		Sprite grassSprite = new Sprite(50, 50, 32, 32, grass);
		scene.attachChild(grassSprite);

		Sprite starSprite = new Sprite(100, 100, 32, 32, star);
		scene.attachChild(starSprite);

		final int centerX = this.shipRegion.getWidth() / 2;
		final int centerY = this.shipRegion.getHeight() / 2;

		final Ball ball = new Ball(centerX, centerY, this.shipRegion);

		final PhysicsHandler physicsHandler = new PhysicsHandler(ball);
		ball.registerUpdateHandler(physicsHandler);
		physicsHandler.setVelocity(DEMO_VELOCITY, DEMO_VELOCITY);

		scene.attachChild(ball);
		scene.setOnSceneTouchListener(this);

		return scene;
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pTouchEvent) {
		this.camera.setCenter(pTouchEvent.getMotionEvent().getX(),pTouchEvent.getMotionEvent().getX()); 
		return true;
	}

	public void refreshScore() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				scoreView.setText("SCORE: " + score.getScore());
			}
		});
	}

	@Override
	public void onLoadComplete() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				score.reset();
				scoreView.setText("SCORE: 0");
			}
		});

		musicSound.play();
		mEngine.start();
	}

	private static class Ball extends AnimatedSprite {
		private final PhysicsHandler mPhysicsHandler;

		public Ball(final float pX, final float pY,
				final TiledTextureRegion ballTextureRegion) {
			super(pX, pY, ballTextureRegion);
			this.mPhysicsHandler = new PhysicsHandler(this);
			this.registerUpdateHandler(this.mPhysicsHandler);
		}

		@Override
		protected void onManagedUpdate(final float pSecondsElapsed) {
			if (this.mX < 0) {
				this.mPhysicsHandler.setVelocityX(DEMO_VELOCITY);
				score.addPoints();
			} else if (this.mX + this.getWidth() > GameUtils.getWidth()) {
				this.mPhysicsHandler.setVelocityX(-DEMO_VELOCITY * 2);
				score.addPoints();
			}

			if (this.mY < 0) {
				this.mPhysicsHandler.setVelocityY(DEMO_VELOCITY);
				score.addPoints();
			} else if (this.mY + this.getHeight() > GameUtils.getHeight()) {
				this.mPhysicsHandler.setVelocityY(-DEMO_VELOCITY * 2);
				score.addPoints();
			}

			super.onManagedUpdate(pSecondsElapsed);
		}
	}

	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if (pKeyCode == KeyEvent.KEYCODE_BACK
				&& pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			showExitDialog();
			return true;
		}

		return super.onKeyDown(pKeyCode, pEvent);
	}

	public void showExitDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				GameExampleActivity.this);
		builder.setMessage("Are you sure you want to exit?")
				.setCancelable(false)
				.setTitle("EXIT")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						finish();
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alert = builder.create();
		alert.setIcon(R.drawable.icon);
		alert.show();
	}
}
