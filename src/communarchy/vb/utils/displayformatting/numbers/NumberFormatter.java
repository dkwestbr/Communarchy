package communarchy.vb.utils.displayformatting.numbers;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class NumberFormatter {
	private NumberFormatter() {}
	
	public static String CommaSeparatedNumber(Integer number, Locale loc) {
		
		NumberFormat nf = NumberFormat.getNumberInstance(loc);
		DecimalFormat US_CS_FORMAT = (DecimalFormat)nf;
		US_CS_FORMAT.applyPattern("###,###.###");
		DecimalFormat df = US_CS_FORMAT;
		
		return df.format(number);
	}
	
	public static String VoteNumber(Integer votes) {
		
		String displayNumber = "";
		if(votes < 1000) {
			displayNumber = String.valueOf(votes);
		} else {
			displayNumber = String.format("%.1fk", votes / 1000); 
		}
		
		return displayNumber;
	}
	
	private static final long SECOND_IN_MILLIS = 1000;
	private static final long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60;
	private static final long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60;
	private static final long DAY_IN_MILLIS = HOUR_IN_MILLIS * 24;
	public static String DisplayTime(Date origin, TimeZone displayZone) {
		
		String date = null;
		
		long l1 = origin.getTime();
		long l2 = (new Date()).getTime();
		long diff = l2 - l1;

		long elapsedDays = diff / DAY_IN_MILLIS;
		diff = diff % DAY_IN_MILLIS;
		long elapsedHours = diff / HOUR_IN_MILLIS;
		diff = diff % HOUR_IN_MILLIS;
		long elapsedMinutes = diff / MINUTE_IN_MILLIS;
		diff = diff % MINUTE_IN_MILLIS;
		long elapsedSeconds = diff / SECOND_IN_MILLIS;
		
		if(elapsedDays < 8) {
			if(elapsedDays < 1) {
				if(elapsedHours < 1) {
					if(elapsedMinutes < 1) {
						date = String.format("%ds ago", elapsedSeconds);
					} else {
						date = String.format("%dm ago", elapsedMinutes);
					}
				} else {
					date = String.format("%dh %dm ago", elapsedHours, elapsedMinutes);
				}
			} else {
				date = String.format("%dd %dh", elapsedDays, elapsedMinutes);
			}
		} else {
			  String formatPattern = "MMM d, ''yy 'at' hh:mm aaa";
			  SimpleDateFormat sdf = new SimpleDateFormat(formatPattern);
			  sdf.setTimeZone(displayZone);
			  date = sdf.format(origin);
		}
		
		return date;
	}
}