package communarchy.vb.global.branches;

import java.lang.reflect.Type;
import com.google.template.soy.data.SoyMapData;

import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IGlobalTemplateWrapper;
import communarchy.vb.global.nodes.Script;
import communarchy.vb.global.nodes.Style;

public class HeaderWrapper extends AbstractTemplateWrapper implements IGlobalTemplateWrapper<SoyMapData> {

	private HeaderWrapper() {}
	private static HeaderWrapper INSTANCE;

	public static HeaderWrapper get() {
		if(INSTANCE == null) {
			INSTANCE = new HeaderWrapper();
			INSTANCE.possiblePaths.add(Script.get());
			INSTANCE.possiblePaths.add(Style.get());
		}	
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/global/branches/Head.soy";
	}

	private static final String P_STYLE_SHEETS = "styleSheets";
	private static final String P_SCRIPTS = "scripts";
	
	@Override
	public SoyMapData getParams(Type t) {
		SoyMapData map = new SoyMapData();
		map.put(P_STYLE_SHEETS, Style.get().getParams(t));
		map.put(P_SCRIPTS, Script.get().getParams(t));
		
		return map;
	}
}