package eu.nazgee.sunflower.textures;

import org.andengine.engine.Engine;
import org.andengine.opengl.texture.region.ITextureRegion;

import eu.nazgee.spider.Spider.SpiderDesc;

import android.content.Context;

public class TexturesLibrarySpiders extends TexturesLibraryBase {

	public TexturesLibrarySpiders(Engine e, Context c) {
		super("spiders.xml", e, c);
	}

	public ITextureRegion getLeg(SpiderDesc pDesc, int pLeg, int pSegment) {
		return mSpritesheet.getTexturePackTextureRegionLibrary().get(pDesc.getLeg(pLeg, pSegment));
	}
}
