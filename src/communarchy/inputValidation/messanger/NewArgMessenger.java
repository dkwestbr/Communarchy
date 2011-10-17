package communarchy.inputValidation.messanger;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.template.soy.data.SoyListData;
import com.google.template.soy.data.SoyMapData;

public class NewArgMessenger {
	private List<String> headerProblems;
	private List<String> contextProblems;
	
	@SuppressWarnings("unused")
	private NewArgMessenger() {}
	
	public NewArgMessenger(List<String> headerProbs, List<String> contextProbs) {
		this.headerProblems = headerProbs;
		this.contextProblems = contextProbs;
	}
	
	public String toJson() {
		Type listType = new TypeToken<List<String>>() {}.getType();
		Gson gson = new Gson();
		
		return String.format("{\"headerErrors\": %s, \"contextErrors\": %s}", gson.toJson(headerProblems, listType), gson.toJson(contextProblems, listType));
	}
	
	public static SoyListData toSoyListData(String key, List<String> vals) {
		SoyListData data = new SoyListData();
		for (String val : vals) {
			data.add(new SoyMapData(key, val));
		}
		return data;
	}
	
	public static SoyListData toSoyListData(List<String> vals) {
		if(vals.isEmpty()) {
			return null;
		}
		SoyListData data = new SoyListData();
		for (String val : vals) {
			data.add(val);
		}
		return data;
	}
}