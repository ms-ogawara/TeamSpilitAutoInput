package jp.ohanamaru.teamsplit.uti;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;

import jp.ohanamaru.teamsplit.TeamSplitStrategy;

/**
 * @author ogawara
 * 
 */
public class WebDriveServiceUtil {

	// エレメントが見つかるまで待つ処理
	public static void waitUntilFindElement(long waitTime, int timeoutCnt, TeamSplitStrategy t) {
		for (int i = 0;; i++) {
			try {
				if (t.isFindElement()) {
					break;
				}
			} catch (NoSuchElementException e) {
				if (i > timeoutCnt) {
					throw e;
				}
			}
			try {
				Thread.sleep(waitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// エレメントが見つからなくなるまで待つ処理
	public static void waitUntilNotFindElement(long waitTime, int timeoutCnt, TeamSplitStrategy t) {
		try {
			// StaleElementReferenceExceptionを回避するため、ここでDOMの読み込みを待つ
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (int i = 0;; i++) {
			if (i > timeoutCnt) {
				throw new TimeoutException("残念、画面遷移に失敗したようだ");
			}

			try {
				if (!t.isFindElement()) {
					break;
				}
			} catch (NoSuchElementException e) {
				break;
			}
			try {
				Thread.sleep(waitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
