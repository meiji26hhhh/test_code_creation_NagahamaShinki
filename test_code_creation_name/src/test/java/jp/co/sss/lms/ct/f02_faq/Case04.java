package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト よくある質問機能
 * ケース04
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース04 よくある質問画面への遷移")
public class Case04 {

	private int port = 8080;

	private WebDriver driver;

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	// 各メソッド実行前に都度、webDriver 準備する
	@BeforeEach
	void setUp() {
		driver = WebDriverUtils.webDriver;
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		//		driver = WebDriverUtils.webDriver;
		goTo("http://localhost:" + port + "/lms/");
		String pageTitle = driver.getTitle();
		assertEquals("ログイン | LMS", pageTitle);

		// screenshot
		getEvidence(new Object() {
		}, "SUCCESS");

	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		String loginText = "StudentAA01";
		String passwordText = "StudentAA011";

		pageLoadTimeout(60);
		// id password login
		WebElement loginId = driver.findElement(By.name("loginId"));
		loginId.clear();
		loginId.sendKeys(loginText);

		WebElement password = driver.findElement(By.name("password"));
		password.clear();
		password.sendKeys(passwordText);

		visibilityTimeout(By.cssSelector("input[type='submit']"), 6);
		WebElement loginBtn = driver.findElement(By.cssSelector("input[type='submit']"));
		loginBtn.click();

		visibilityTimeout(By.tagName("h2"), 6);
		// assert
		String pageTitle = driver.getTitle();
		assertEquals("コース詳細 | LMS", pageTitle);
		// screenshot
		getEvidence(new Object() {
		}, "SUCCESS");

	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {

		// .dropdown click
		WebElement dropdown = driver.findElement(
				By.cssSelector("li.dropdown > a.dropdown-toggle"));

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", dropdown);

		assertTrue(Boolean.parseBoolean(
				dropdown.getAttribute("aria-expanded")));

		// helpLink click
		WebElement helpLink = driver.findElement(By.linkText("ヘルプ"));

		js.executeScript("arguments[0].click();", helpLink);
		// wait
		new WebDriverWait(driver, Duration.ofSeconds(6))
				.until(ExpectedConditions.urlContains("/lms/help"));

		// assert
		String pageTitle = driver.getTitle();
		assertEquals("ヘルプ | LMS", pageTitle);

		// screenshot
		getEvidence(new Object() {
		}, "SUCCESS");

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		// println 確認用
		//		System.out.println("現在URL：" + driver.getCurrentUrl());
		//		System.out.println("現在タイトル：" + driver.getTitle());

		// 現在のタブ
		String currentWindow = driver.getWindowHandle();

		// 「よくある質問」click
		visibilityTimeout(By.linkText("よくある質問"), 6);
		WebElement faqLink = driver.findElement(By.linkText("よくある質問"));
		// Javascript click 実行
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", faqLink);
		// println 確認用
		//		System.out.println("クリック後タブ数："
		//				+ driver.getWindowHandles().size());

		// newTab
		WebDriverWait newTabWait = new WebDriverWait(driver, Duration.ofSeconds(6));
		newTabWait.until(driver -> driver.getWindowHandles().size() == 2);

		// 新しいタブへ切り替え
		for (String windowHandle : driver.getWindowHandles()) {
			if (!windowHandle.equals(currentWindow)) {
				driver.switchTo().window(windowHandle);
				break;
			}
		}

		// 新しいタブへ遷移するまで待機
		newTabWait.until(
				ExpectedConditions.urlContains("/lms/faq"));
		// println 確認用
		//		System.out.println("現在URL：" + driver.getCurrentUrl());
		//		System.out.println("現在タイトル：" + driver.getTitle());

		// assert
		String pageTitle = driver.getTitle();
		assertEquals("よくある質問 | LMS", pageTitle);

		// screenshot
		getEvidence(new Object() {
		}, "SUCCESS");

	}

}
