package communarchy.vb.arg.nodes;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.template.soy.data.SoyMapData;

import communarchy.constants.ISessionConstants;
import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IArgument;
import communarchy.facts.interfaces.IUser;
import communarchy.inputValidation.UserInputValidator;
import communarchy.inputValidation.messanger.NewArgMessenger;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

public class NewPointWithErrors extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IArgument> {

	private static NewPointWithErrors INSTANCE;
	private NewPointWithErrors() {}
	
	private static String PARAM_NEW_POINT_ACTION = "newPointAction";
	
	public static NewPointWithErrors get() {
		if(INSTANCE == null) {
			INSTANCE = new NewPointWithErrors();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/point/nodes/NewPointWithErrors.soy";
	}

	@Override
	@SuppressWarnings("unchecked")
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IArgument scopedResource) {
	
		SoyMapData paramMap = new SoyMapData();
		paramMap.put(PARAM_NEW_POINT_ACTION, String.format("/arg/%s", scopedResource.getArgId().toString()));
		
		HttpSession session = request.getSession();
		
		Map<String, String> requiredParamMap = (Map<String, String>) session.getAttribute(ISessionConstants.REQ_MAP);
		for(String key : requiredParamMap.keySet()) {
			paramMap.put(key, requiredParamMap.get(key));
		}
		
		Map<String, Map<String, Object>> errors = (Map<String, Map<String, Object>>) session.getAttribute(ISessionConstants.ERRS_FOUND);
		for(String key : errors.keySet()) {
			Map<String, Object> errSet = errors.get(key);
			
			String messageKey = String.format("%s%s", errSet.get(UserInputValidator.RESOURCE_KEY), "Message");
			String errorsKey = String.format("%s%s", errSet.get(UserInputValidator.RESOURCE_KEY), "Errors");
			paramMap.put(messageKey, errSet
					.get(UserInputValidator.MESSAGE_KEY));
			paramMap.put(errorsKey,
					NewArgMessenger
							.toSoyListData((List<String>) errSet
									.get(UserInputValidator.ERROR_KEY)));
		}
		
		return paramMap;
	}
}