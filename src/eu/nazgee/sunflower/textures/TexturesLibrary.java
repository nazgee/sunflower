package eu.nazgee.sunflower.textures;

import org.andengine.engine.Engine;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.texturepack.TexturePack;
import org.andengine.util.texturepack.TexturePackLoader;
import org.andengine.util.texturepack.exception.TexturePackParseException;

import android.content.Context;
import eu.nazgee.sunflower.Consts;

public class TexturesLibrary {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private BitmapTextureAtlas mFontAtlas;
	private Font mFont;

	private TexturePack mSpritesheetProps;
	private TexturePack mSpritesheetTiles;

	// ===========================================================
	// Constructors
	// ===========================================================
	public TexturesLibrary() {
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Font getFont() {
		return mFont;
	}

	public ITextureRegion getTileGrass() {
		return mSpritesheetTiles.getTexturePackTextureRegionLibrary().get(Tiles.GRASS_ID);
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public void load(final Engine e, final Context c) {
		// Prepare a reloaders that will store and reload big texture atlasses
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		FontFactory.setAssetBasePath("fonts/");

		// Load spritesheet
		try {
			mSpritesheetProps = new TexturePackLoader(c.getAssets(), e.getTextureManager()).loadFromAsset(Consts.PATH_SPRITESHEETS + "props.xml", Consts.PATH_SPRITESHEETS);
			mSpritesheetProps.loadTexture();
		} catch (final TexturePackParseException ex) {
			Debug.e(ex);
		}

		// Load spritesheet
		try {
			mSpritesheetTiles = new TexturePackLoader(c.getAssets(), e.getTextureManager()).loadFromAsset(Consts.PATH_SPRITESHEETS + "tiles.xml", Consts.PATH_SPRITESHEETS);
			mSpritesheetTiles.loadTexture();
		} catch (final TexturePackParseException ex) {
			Debug.e(ex);
		}

		// Load fonts
		final TextureManager textureManager = e.getTextureManager();
		final FontManager fontManager = e.getFontManager();

		mFontAtlas = new BitmapTextureAtlas(textureManager, 256, 256, TextureOptions.BILINEAR);
		mFont = FontFactory.createFromAsset(fontManager, mFontAtlas, c.getAssets(), Consts.FONT_NAME, Consts.FONT_SIZE, true, Color.WHITE.getARGBPackedInt());
		mFont.load();
	}

	public void unload() {
		mSpritesheetProps.unloadTexture();
		mSpritesheetTiles.unloadTexture();
		mFont.unload();
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}