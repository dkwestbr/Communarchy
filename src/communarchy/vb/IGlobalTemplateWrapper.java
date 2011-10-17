package communarchy.vb;

import java.lang.reflect.Type;

import com.google.template.soy.data.SoyData;

public interface IGlobalTemplateWrapper<T extends SoyData> extends ITemplateWrapper {
	public T getParams(Type t);
}
