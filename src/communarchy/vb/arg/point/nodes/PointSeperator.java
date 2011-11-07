package communarchy.vb.arg.point.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IParamBuilder;

public class PointSeperator extends AbstractTemplateWrapper implements IParamBuilder {

	private static PointSeperator INSTANCE;
	private PointSeperator() {}
	
	public static PointSeperator get() {
		if(INSTANCE == null) {
			INSTANCE = new PointSeperator();
		}
		
		return INSTANCE;
	}
	
	private static final SoyMapData BLANK_MAP = new SoyMapData();
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request) {
		return BLANK_MAP;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/nodes/PointSeperator.soy";
	}
}
