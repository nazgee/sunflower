package eu.nazgee.sunflower.textures;

import org.andengine.util.math.MathUtils;

public class Props implements IProps {
	public static final int SEEDS_NUMBER = 2;
	public static final int SEEDS_FIRST = SEEDS_01_ID;
	public static int getSeedID(int pSeedIndex) {
		return MathUtils.bringToBounds(SEEDS_FIRST, SEEDS_FIRST + SEEDS_NUMBER - 1, pSeedIndex);
	}
}
