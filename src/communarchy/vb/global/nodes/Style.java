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
import communarchy.vb.newarg.NewArgRoot;

public class Style extends AbstractTemplateWrapper implements IGlobalTemplateWrapper<SoyListData> {

	private static Style INSTANCE;
	private Style() {
		rootMap = new HashMap<Type, SoyListData>();
		rootMap.put(ArgRoot.class, ArgRoot.get().getStlyes());
		rootMap.put(MainRoot.class, MainRoot.get().getStlyes());
		rootMap.put(LoginRoot.class, MainRoot.get().getStlyes());
		rootMap.put(NewArgRoot.class, NewArgRoot.get().getStlyes());
	}
	
	private Map<Type, SoyListData> rootMap;
	
	public static Style get() {
		if(INSTANCE == null) {
			INSTANCE = new Style();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/global/nodes/Style.soy";
	}

	@Override
	public SoyListData getParams(Type t) {
		return rootMap.get(t);
	}
}