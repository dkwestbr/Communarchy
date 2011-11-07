package communarchy.vb.global.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IParamBuilder;

public class ThickBorder extends AbstractTemplateWrapper implements
		IParamBuilder {

	private static ThickBorder INSTANCE;
	private ThickBorder() {}
	public static ThickBorder get() {
		if(INSTANCE == null) {
			INSTANCE = new ThickBorder();
		}
		
		return INSTANCE;
	}
	
	private static final SoyMapData blankMap = new SoyMapData();
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request) {
		return blankMap;
	}

	@Override
	public String getTemplate() {
		return "./templates/html/global/nodes/ThickBorder.soy";
	}
}