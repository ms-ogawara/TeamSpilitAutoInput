package jp.ohanamaru.teamsplit.entity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author ogawara
 * 
 */
public class TeamSplitInfo {

	public static final String DEFAULT_CHROME_VERSION = "80";

	/**
	 * 休憩時間
	 */
	private static final int BREAK_TIME = -1;

	/**
	 * ユーザ名
	 */
	private String userName;

	/**
	 * パスワード
	 */
	private String password;

	/**
	 * 出社時刻
	 */
	private String startTime;

	/**
	 * 退社時刻
	 */
	private String endTime;

	/**
	 * 工数の番号
	 */
	private int worknumber;

	/**
	 * 工数の時間
	 */
	private String workTime;

	// アクセサ
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getWorknumber() {
		return worknumber;
	}

	public void setWorknumber(int worknumber) {
		this.worknumber = worknumber;
	}

	public String getWorkTime() {
		return workTime;
	}

	/**
	 * <pre>
	 * 出社時刻と退社時刻から工数を算出。 
	 * ただ、工数入力はスライダーをクリックする方式に変えたので、いまは使ってない。
	 * </pre>
	 * 
	 * @param startTime 出社時刻
	 * @param endTime   退社時刻
	 */
	@Deprecated
	public void setWorkTime(String startTime, String endTime) {
		// 出社時刻と退社時刻の差を工数の時間として設定する
		Calendar startCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		Calendar workCalendar = Calendar.getInstance();

		String[] startTimeArray = startTime.split(":");
		String[] endTimeArray = endTime.split(":");

		startCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTimeArray[0]));
		startCalendar.set(Calendar.MINUTE, Integer.parseInt(startTimeArray[1]));
		startCalendar.set(Calendar.SECOND, 00);

		endCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endTimeArray[0]));
		endCalendar.set(Calendar.MINUTE, Integer.parseInt(endTimeArray[1]));
		endCalendar.set(Calendar.SECOND, 00);

		// 差分を求める
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		workCalendar.setTimeInMillis(endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis()
				- workCalendar.getTimeZone().getRawOffset());

		// 休憩時間を引く
		workCalendar.add(Calendar.HOUR_OF_DAY, BREAK_TIME);

		this.workTime = sdf.format(workCalendar.getTime());
	}

}
