package eu.nazgee.sunflower.textures;

import eu.nazgee.spider.Spider.SpiderDesc;


public class Spiders implements ISpiders {
	public int getLeg(SpiderDesc pDesc, int pLeg, int pSegment) {
		return pDesc.getLeg(pLeg, pSegment);
	}
}
