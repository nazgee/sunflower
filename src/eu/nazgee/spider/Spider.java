package eu.nazgee.spider;

import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import eu.nazgee.sunflower.misc.PhysicsEditorShapeLibrary;
import eu.nazgee.sunflower.textures.Spiders;
import eu.nazgee.sunflower.textures.TexturesLibrarySpiders;

public class Spider {
	final SpiderDesc mDesc;
	public final Leg mLegs[];

	public Spider(SpiderDesc pDesc, 
			float pX, float pY, PhysicsWorld pWorld, PhysicsEditorShapeLibrary shapes,
			TexturesLibrarySpiders pTextures, VertexBufferObjectManager pVBO) {
		mDesc = pDesc;
		mLegs = new Leg[pDesc.LEGS_NUMBER];
		for (int i = 0; i < mLegs.length; i++) {
			mLegs[i] = new Leg(i, pX, pY + 100, pDesc, pWorld, shapes, pTextures, pVBO);
		}
	}

	public enum SpiderDesc {
		SPIDER_01(4, 3, Spiders.S01_SPIDER01_R11_ID);

		public final int LEGS_NUMBER;
		public final int JOINTS_NUMBER;
		public final int LEG_ID_FIRST;

		private SpiderDesc(int pLegs, int pJoints, int pFirstLegID) {
			LEGS_NUMBER = pLegs;
			JOINTS_NUMBER = pJoints;
			LEG_ID_FIRST = pFirstLegID;
		}

		public int getLeg(int pLeg, int pSegment) {
			return LEG_ID_FIRST + pLeg * JOINTS_NUMBER + pSegment;
		}
	}
}
