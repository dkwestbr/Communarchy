package communarchy.constants;

import java.util.regex.Pattern;

public interface IServletConstants {
	public static String MAIN_HTML_TMPL = "src/com/subjective/html/templates/main.html";
	public static String LOGIN_HTML_TMPL = "src/com/subjective/html/templates/login.html";
	
	public static String JSON_MIME_TYPE = "application/json";
	
	public static String LOGIN_REQUIRED = "You must login or register now...now!";
	
	public static final Pattern RESOURCE_ID_PATTERN = Pattern.compile(".*?/([^/]+)");
	public static int RESOURCE_ID_IDX = 0;
}