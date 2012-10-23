package eu.nazgee.sunflower;


import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleAsyncGameActivity;
import org.andengine.util.progress.IProgressListener;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import eu.nazgee.sunflower.textures.Library;

public class GameActivity extends SimpleAsyncGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================



	// ===========================================================
	// Fields
	// ===========================================================

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mFaceTextureRegion;
	private Library mLibrary;
	private PhysicsWorld mPhysicsWorld;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT);
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResourcesAsync(final IProgressListener pProgressListener) throws Exception {
		/* Comfortably load the resources asynchronously, adding artificial pauses between each step. */
		pProgressListener.onProgressChanged(0);
		Thread.sleep(100);
		pProgressListener.onProgressChanged(20);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		Thread.sleep(100);
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 32, 32, TextureOptions.BILINEAR);
		pProgressListener.onProgressChanged(40);
		Thread.sleep(100);
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(GameActivity.this.mBitmapTextureAtlas, GameActivity.this, "face_box.png", 0, 0);
		pProgressListener.onProgressChanged(60);
		Thread.sleep(100);
		this.mBitmapTextureAtlas.load();
		this.mLibrary = new Library(getEngine(), GameActivity.this);
		this.mLibrary.load(getEngine(), GameActivity.this);
		pProgressListener.onProgressChanged(80);
		Thread.sleep(100);
		pProgressListener.onProgressChanged(100);
	}

	@Override
	public Scene onCreateSceneAsync(final IProgressListener pProgressListener) throws Exception {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		return scene;
	}

	@Override
	public void onPopulateSceneAsync(final Scene pScene, final IProgressListener pProgressListener) throws Exception {
		/* Calculate the coordinates for the face, so its centered on the camera. */
		final float centerX = (Consts.CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
		final float centerY = (Consts.CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;

		/* Create the face and add it to the scene. */
		final Sprite grass = new Sprite(centerX, centerY, this.mLibrary.getTiles().getGrass(), this.getVertexBufferObjectManager());
		pScene.attachChild(grass);
		final Sprite face = new Sprite(centerX, centerY, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
		pScene.attachChild(face);

		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);

		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		final Rectangle ground = new Rectangle(Consts.CAMERA_WIDTH / 2, 1, Consts.CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle roof = new Rectangle(Consts.CAMERA_WIDTH / 2, Consts.CAMERA_HEIGHT - 1, Consts.CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle left = new Rectangle(1, Consts.CAMERA_HEIGHT / 2, 1, Consts.CAMERA_HEIGHT, vertexBufferObjectManager);
		final Rectangle right = new Rectangle(Consts.CAMERA_WIDTH - 1, Consts.CAMERA_HEIGHT / 2, 2, Consts.CAMERA_HEIGHT, vertexBufferObjectManager);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);

		pScene.attachChild(ground);
		pScene.attachChild(roof);
		pScene.attachChild(left);
		pScene.attachChild(right);

		pScene.registerUpdateHandler(this.mPhysicsWorld);

		final Sprite animatedSprite;
		final Body body;

		final float pX = 100;
		final float pY = 100;
		FixtureDef fixture = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
		animatedSprite = new Sprite(pX, pY, this.mLibrary.getProps().getSeed(1), this.getVertexBufferObjectManager());
		body = PhysicsFactory.createCircleBody(this.mPhysicsWorld, animatedSprite, BodyType.DynamicBody, fixture);

		pScene.attachChild(animatedSprite);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(animatedSprite, body, true, true));
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}