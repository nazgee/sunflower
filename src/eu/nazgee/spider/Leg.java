package eu.nazgee.spider;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import eu.nazgee.spider.Spider.SpiderDesc;
import eu.nazgee.sunflower.misc.PhysicsEditorShapeLibrary;
import eu.nazgee.sunflower.textures.TexturesLibrarySpiders;

public class Leg {
	public Body mSegments[];
	public Sprite mFaces[];
	public Leg(int pLeg, float pX, float pY, SpiderDesc pDesc, 
			PhysicsWorld pWorld, PhysicsEditorShapeLibrary shapes,
			TexturesLibrarySpiders pTextures, VertexBufferObjectManager pVBO) {
		mSegments = new Body[pDesc.JOINTS_NUMBER];
		mFaces = new Sprite[pDesc.JOINTS_NUMBER];

		// create segments
		for (int i = mSegments.length-1; i >= 0 ; i--) {
			ITextureRegion tex = pTextures.getLeg(pDesc, pLeg, i);
			pX = pX + tex.getWidth() * 0.5f;
			mFaces[i] = new Sprite(pX, pY, tex, pVBO);
			pX = pX + tex.getWidth() * 0.4f;
			String name = "spider01-R";
			name += pLeg + 1;
			name += i + 1;
			mSegments[i] = shapes.createBody(name, mFaces[i], pWorld);
			pWorld.registerPhysicsConnector(new PhysicsConnector(mFaces[i], mSegments[i], true, true));
		}

		// bind segments
		Vector2 v = Vector2Pool.obtain();
		for (int i = 0; i < mSegments.length - 1; i++) {
			final RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
			Body moving = mSegments[i];
			Body anchor = mSegments[i+1];
			Sprite anchorFace = mFaces[i+1];
			v.set(anchorFace.getWidth()/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT * 0.45f, 0);
			anchor.getWorldPoint(v);
			revoluteJointDef.initialize(anchor, moving, anchor.getWorldPoint(v));
			revoluteJointDef.enableMotor = false;
			revoluteJointDef.motorSpeed = 10;
			revoluteJointDef.maxMotorTorque = 20;
			revoluteJointDef.enableLimit = true;
			revoluteJointDef.upperAngle = MathUtils.degToRad(80);
			revoluteJointDef.lowerAngle = MathUtils.degToRad(-30);

			pWorld.createJoint(revoluteJointDef);
		}
		Vector2Pool.recycle(v);
	}

	public class LegDesc {
		
	}

	public class SegmentDesc {
		public final float angle;
		public final float joint_pos;

		private SegmentDesc(float angle, float joint_pos) {
			this.angle = angle;
			this.joint_pos = joint_pos;
		}
	}
}
