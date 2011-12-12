package communarchy.vb.utils.displayformatting.numbers;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatter {
	private NumberFormatter() {}
	
	public static String CommaSeparatedNumber(Integer number, Locale loc) {
		
		NumberFormat nf = NumberFormat.getNumberInstance(loc);
		DecimalFormat US_CS_FORMAT = (DecimalFormat)nf;
		US_CS_FORMAT.applyPattern("###,###.###");
		DecimalFormat df = US_CS_FORMAT;
		
		return df.format(number);
	}
}
