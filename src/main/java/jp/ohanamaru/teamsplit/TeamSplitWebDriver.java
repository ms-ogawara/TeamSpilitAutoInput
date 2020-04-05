package jp.ohanamaru.teamsplit;

import java.util.Calendar;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import jp.ohanamaru.teamsplit.entity.TeamSplitInfo;
import jp.ohanamaru.teamsplit.uti.WebDriveServiceUtil;

/**
 * @author ogawara
 * 
 */
public class TeamSplitWebDriver {
	private WebDriver driver;
	// 定数
	private static final String BASE_URL = "https://teamspirit-385.cloudforce.com";
	private static final int WINDOW_WIDTH_SIZE = 800;
	private static final int WINDOW_HEIGHT_SIZE = 900;

	private static final String APPLAY_HTML_ELEMENT_PREFIX = "ttvApply";
	private static final String TIME_SET_HTML_ELEMENT_PREFIX = "ttvTimeSt";
	private static final String WORK_SET_HTML_ELEMENT_PREFIX = "dailyWorkCell";

	private static final String USER_NAME_HTML_ELEMENT_NAME = "username";
	private static final String PASSWORD_HTML_ELEMENT_NAME = "password";
	private static final String LOGIN_BUTTON_HTML_ELEMENT_NAME = "Login";
	private static final String KINMUHYOU_LINK_HTML_ELEMENT_NAME = "勤務表";
	private static final String START_TIME_HTML_ELEMENT_NAME = "startTime";
	private static final String END_TIME_HTML_ELEMENT_NAME = "endTime";
	private static final String TIME_REGIST_BUTTON_HTML_ELEMENT_NAME = "dlgInpTimeOk";
	private static final String WORK_REGIST_BUTTON_HTML_ELEMENT_NAME = "empWorkOk";
	private static final String WORK_SLIDER_HTML_ELEMENT_PREFIX = "empWorkSlider";
	private static final String WORK_SLIDER_INCREMENT_CLASS_PREFIX = "dijitSliderIncrementIconH";

	private static final int WORK_REGIST_SKIP_NUMBER = 0;

	public TeamSplitWebDriver() {
		driver = new ChromeDriver();
	}

	public void drive(TeamSplitInfo entity) {
		driver.get(BASE_URL);
		driver.manage().window().setSize(new Dimension(WINDOW_WIDTH_SIZE, WINDOW_HEIGHT_SIZE));

		Wait<WebDriver> wait = new WebDriverWait(driver, 30);

		// ログイン
		ExpectedCondition<WebElement> presenceOfElementIdentifiedAsUserName = new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id(USER_NAME_HTML_ELEMENT_NAME));
			}
		};
		WebElement userNameElement = wait.until(presenceOfElementIdentifiedAsUserName);

		userNameElement.clear();
		driver.findElement(By.id(USER_NAME_HTML_ELEMENT_NAME)).sendKeys(entity.getUserName());
		driver.findElement(By.id(PASSWORD_HTML_ELEMENT_NAME)).clear();
		driver.findElement(By.id(PASSWORD_HTML_ELEMENT_NAME)).sendKeys(entity.getPassword());
		driver.findElement(By.id(LOGIN_BUTTON_HTML_ELEMENT_NAME)).click();

		// 勤務表メニュー表示
		WebDriveServiceUtil.waitUntilFindElement(1000, 30, new TeamSplitStrategy() {
			@Override
			public boolean isFindElement() {
				if (driver.getTitle().contains(KINMUHYOU_LINK_HTML_ELEMENT_NAME)) {
					return true;
				}
				driver.findElement(By.linkText(KINMUHYOU_LINK_HTML_ELEMENT_NAME)).click();
				return false;
			}
		});

		// 出勤時間、工数登録
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int firstDay = cal.getActualMinimum(Calendar.DATE);
		int endDay = cal.getActualMaximum(Calendar.DATE);

		final String MIN_DAY = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", firstDay);
		ExpectedCondition<WebElement> presenceOfElementIdentifiedAsTtvApply = new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id(APPLAY_HTML_ELEMENT_PREFIX + MIN_DAY));
			}
		};
		wait.until(presenceOfElementIdentifiedAsTtvApply);

		String dayStr = "";
		for (int day = firstDay; day <= endDay; day++) {
			// 1日ごとに処理
			dayStr = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);

			// 出勤時間登録
			timeRegist(entity, dayStr);

			if (entity.getWorknumber() != WORK_REGIST_SKIP_NUMBER) {
				// 工数登録
				workRegist(entity, dayStr);
			}
		}
	}

	// 出勤時間登録処理
	private void timeRegist(TeamSplitInfo entity, String dayStr) {
		String timeSetElementId = TIME_SET_HTML_ELEMENT_PREFIX + dayStr;

		if (driver.findElements(By.id(timeSetElementId)).size() > 0) {

			// セルが存在したら登録
			driver.findElement(By.id(timeSetElementId)).click();

			driver.findElement(By.id(START_TIME_HTML_ELEMENT_NAME)).clear();
			driver.findElement(By.id(START_TIME_HTML_ELEMENT_NAME)).sendKeys(entity.getStartTime());
			driver.findElement(By.id(END_TIME_HTML_ELEMENT_NAME)).clear();
			driver.findElement(By.id(END_TIME_HTML_ELEMENT_NAME)).sendKeys(entity.getEndTime());

			driver.findElement(By.id(TIME_REGIST_BUTTON_HTML_ELEMENT_NAME)).click();

			WebDriveServiceUtil.waitUntilNotFindElement(1000, 30, new TeamSplitStrategy() {
				@Override
				public boolean isFindElement() {
					if (driver.findElement(By.id(TIME_REGIST_BUTTON_HTML_ELEMENT_NAME)).isDisplayed()) {
						return true;
					}
					return false;
				}
			});
		}
	}

	// 工数登録処理
	private void workRegist(TeamSplitInfo entity, String dayStr) {
		String worksetElementId = WORK_SET_HTML_ELEMENT_PREFIX + dayStr;

		WebElement workParentelement = driver.findElement(By.id(worksetElementId));
		if (workParentelement.findElements(By.className("png-add")).size() > 0) {
			// リンクが存在したら登録
			workParentelement.findElement(By.className("png-add")).click();

			String workSliderId = WORK_SLIDER_HTML_ELEMENT_PREFIX + (entity.getWorknumber() - 1);
			WebDriveServiceUtil.waitUntilFindElement(1000, 30, new TeamSplitStrategy() {
				@Override
				public boolean isFindElement() {
					if (driver.findElements(By.id(workSliderId)).size() > 0) {
						return true;
					}
					workParentelement.findElement(By.className("png-add")).click();
					return false;
				}
			});

			driver.findElement(By.id(workSliderId)).findElement(By.className(WORK_SLIDER_INCREMENT_CLASS_PREFIX))
					.click();
			driver.findElement(By.id(WORK_REGIST_BUTTON_HTML_ELEMENT_NAME)).click();

			WebDriveServiceUtil.waitUntilNotFindElement(1000, 30, new TeamSplitStrategy() {
				@Override
				public boolean isFindElement() {
					if (driver.findElement(By.id(WORK_REGIST_BUTTON_HTML_ELEMENT_NAME)).isDisplayed()) {
						return true;
					}

					return false;
				}
			});
		}
	}
}
