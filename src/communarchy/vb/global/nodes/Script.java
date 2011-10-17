package communarchy.vb.global.nodes;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.template.soy.data.SoyListData;

import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IGlobalTemplateWrapper;
import communarchy.vb.arg.ArgRoot;
import communarchy.vb.login.LoginRoot;
import communarchy.vb.main.MainRoot;

public class Script extends AbstractTemplateWrapper implements IGlobalTemplateWrapper<SoyListData> {

	private static Script INSTANCE;
	private Script() {
		rootMap = new HashMap<Type, SoyListData>();
		rootMap.put(ArgRoot.class, ArgRoot.get().getScripts());
		rootMap.put(MainRoot.class, MainRoot.get().getScripts());
		rootMap.put(LoginRoot.class, MainRoot.get().getScripts());
	}
	
	private Map<Type, SoyListData> rootMap;
	
	public static Script get() {
		if(INSTANCE == null) {
			INSTANCE = new Script();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/global/nodes/Script.soy";
	}

	@Override
	public SoyListData getParams(Type t) {
		return rootMap.get(t);
	}
}