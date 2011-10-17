package communarchy.vb;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.google.template.soy.SoyFileSet.Builder;

public abstract class AbstractTemplateWrapper {
	
	public abstract String getTemplate();
	
	public void registerTemplates(Builder builder, HashMap<String, String> currentTemplates) {	
		if(!currentTemplates.containsKey(getTemplate())) {
			currentTemplates.put(getTemplate(), "");
			builder.add(new File(getTemplate()));
			for(ITemplateWrapper possiblePath : possiblePaths) {
				possiblePath.registerTemplates(builder, currentTemplates);
			}
		}
	}

	protected List<ITemplateWrapper> possiblePaths = new ArrayList<ITemplateWrapper>();
}