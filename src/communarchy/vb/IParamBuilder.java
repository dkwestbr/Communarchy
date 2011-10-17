package communarchy.vb;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;
import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;

public interface IParamBuilder extends ITemplateWrapper {
	public SoyMapData getParams(PMSession pmSession, IUser user, HttpServletRequest request);
}