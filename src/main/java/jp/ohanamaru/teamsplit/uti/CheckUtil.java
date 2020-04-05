package jp.ohanamaru.teamsplit.uti;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ogawara
 * 
 */
public class CheckUtil {

	// 文字列の空チェック
	public static boolean isStringEmpty(String str) {
		return str == null || "".equals(str);
	}

	// Chromeのバージョンチェック
	public static boolean isChromeVersionValid(String version) {
		return version.equals("80") || version.equals("81");
	}

	// 時刻の形式チェック
	public static boolean isTimeFormatValid(String time) {
		if (time == null || "".equals(time)) {
			return false;
		}
		Pattern p = Pattern.compile("^([0-9]|[0-1][0-9]|[2][0-3]):([0-9]|[0-5][0-9])$");
		Matcher m = p.matcher(time);

		return m.find();
	}

	// 開始と終了時刻の妥当性チェック
	public static boolean isTimePeriodValid(String startTime, String endTime) {
		Calendar startCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();

		String[] startTimeArray = startTime.split(":");
		String[] endTimeArray = endTime.split(":");

		startCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTimeArray[0]));
		startCalendar.set(Calendar.MINUTE, Integer.parseInt(startTimeArray[1]));
		startCalendar.set(Calendar.SECOND, 00);

		endCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endTimeArray[0]));
		endCalendar.set(Calendar.MINUTE, Integer.parseInt(endTimeArray[1]));
		endCalendar.set(Calendar.SECOND, 00);

		int diff = startCalendar.compareTo(endCalendar);

		return diff < 0;
	}

	// 工数番号の形式チェック
	public static boolean isWorkNumberFormatValid(String workNumber) {
		int workNumberNum = 0;
		try {
			workNumberNum = Integer.parseInt(workNumber);
		} catch (NumberFormatException e) {
			return false;
		}
		return workNumberNum >= 0 && workNumberNum <= 99;
	}

}
