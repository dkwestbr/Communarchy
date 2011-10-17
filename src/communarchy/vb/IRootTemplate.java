package communarchy.vb;

import javax.servlet.ServletContext;

import com.google.template.soy.data.SoyListData;

public interface IRootTemplate extends ITemplateWrapper {
	public String getRenderTarget();
	public SoyListData getScripts();
	public SoyListData getStlyes();
	public void init(ServletContext context);
}
