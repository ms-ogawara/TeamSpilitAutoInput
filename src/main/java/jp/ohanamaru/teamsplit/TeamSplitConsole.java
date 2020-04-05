package jp.ohanamaru.teamsplit;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.xml.bind.ValidationException;

import jp.ohanamaru.teamsplit.entity.TeamSplitInfo;
import jp.ohanamaru.teamsplit.uti.CheckUtil;

/**
 * @author ogawara
 * 
 */
public class TeamSplitConsole {

	public TeamSplitInfo input() throws Exception {
		TeamSplitInfo entity = new TeamSplitInfo();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String userName = "";
		String passWord = "";
		String startTime = "";
		String endTime = "";
		String workNumber = "";
		try {
			System.out.println("■■■TeamSplitのアカウントを入力して下さい■■■");
			System.out.print("ユーザ名> ");
			userName = br.readLine();
			if (!CheckUtil.isStringEmpty(userName)) {
				entity.setUserName(userName);
			} else {
				throw new ValidationException("ユーザ名が未入力です");
			}

			System.out.print("パスワード> ");
			passWord = br.readLine();
			if (!CheckUtil.isStringEmpty(passWord)) {
				entity.setPassword(passWord);
			} else {
				throw new ValidationException("パスワードが未入力です");
			}

			System.out.println("");
			System.out.println("■■■勤務時間を入力して下さい■■■");
			System.out.print("出社時刻（入力例：9:30）> ");
			startTime = br.readLine();
			if (CheckUtil.isTimeFormatValid(startTime)) {
				entity.setStartTime(startTime);
			} else {
				throw new ValidationException("出社時刻が不正です");
			}

			System.out.print("退社時刻（入力例：18:30）> ");
			endTime = br.readLine();
			if (CheckUtil.isTimeFormatValid(endTime)) {
				entity.setEndTime(endTime);
			} else {
				throw new ValidationException("退社時刻が不正です");
			}
			if (!CheckUtil.isTimePeriodValid(startTime, endTime)) {
				throw new ValidationException("出社時刻、退社時刻の入力内容が不正です");
			}

			System.out.println("");
			System.out.println("■■■工数の番号を入力して下さい■■■");
			System.out.print("工数番号（0～99。※0で工数入力はスキップします）> ");
			workNumber = br.readLine();
			if (CheckUtil.isStringEmpty(workNumber)) {
				throw new ValidationException("工数番号が未入力です");
			}
			if (CheckUtil.isWorkNumberFormatValid(workNumber)) {
				entity.setWorknumber(Integer.parseInt(workNumber));
			} else {
				throw new ValidationException("工数番号の入力内容が不正です。0から99までの番号を入力して下さい");
			}
		} finally {
			br.close();
		}
		return entity;
	}
}
