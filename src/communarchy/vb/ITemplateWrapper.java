package communarchy.vb;

import java.util.HashMap;

import com.google.template.soy.SoyFileSet.Builder;

public interface ITemplateWrapper {
	public String getTemplate();
	public void registerTemplates(Builder builder, HashMap<String, String> currentTemplates);
}
