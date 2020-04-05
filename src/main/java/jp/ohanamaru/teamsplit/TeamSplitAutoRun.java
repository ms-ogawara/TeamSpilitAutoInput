package jp.ohanamaru.teamsplit;

import jp.ohanamaru.teamsplit.entity.TeamSplitInfo;

/**
 * <pre>
 * 実行方法：
 *  java -Dwebdriver.chrome.driver="chromedriver.exeのパス"　-jar teamsplit-auto-input-jar-with-dependencies.jar
 * </pre>
 * 
 * @author ogawara
 * 
 */
public class TeamSplitAutoRun {

	public static void main(String[] args) {
		try {
			System.out.println("処理開始\n");

			// コンソール入力
			TeamSplitConsole console = new TeamSplitConsole();
			TeamSplitInfo entity = console.input();

			// 実行
			TeamSplitWebDriver driver = new TeamSplitWebDriver(entity.getChromeVersion());
			driver.drive(entity);

			System.out.println("処理終了\n");

		} catch (Throwable t) {
			System.out.println("エラーが発生しました\n");

			t.printStackTrace();
			System.exit(1);
		}
	}
}
