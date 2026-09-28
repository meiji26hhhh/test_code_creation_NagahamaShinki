package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

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

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト レポート機能
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {

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
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		// 一覧の行が表示されるまで待機
		visibilityTimeout(
				By.cssSelector("tbody tr"), 6);

		// 一覧の行をすべて取得
		List<WebElement> rows = driver.findElements(
				By.cssSelector("tbody tr"));
		WebElement report = null;

		// 各行の内容を表示
		for (WebElement row : rows) {
			if (row.getText().contains("提出済み")) {
				report = row;
				break;
			}
		}

		// セクション情報を取得
		String overCharsectionDate = report.findElement(
				By.cssSelector("td.w20per")).getText();
		// 日付の曜日削除
		String sectionDate = overCharsectionDate.substring(0, overCharsectionDate.length() - 3);
		// セクション名を取得
		String sectionName = report.findElement(
				By.cssSelector("td.wh")).getText();

		// 「詳細」ボタン
		WebElement detailButton = report.findElement(
				By.cssSelector("input[type='submit'][value='詳細']"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", detailButton);

		// h2 表示されるまで待機
		visibilityTimeout(By.cssSelector("#sectionDetail h2"), 6);
		// h2 情報を取得
		WebElement h2Element = driver.findElement(By.cssSelector("#sectionDetail h2"));
		// セクション名を取得
		String sectionDetailName = h2Element.getText()
				.replace(h2Element.findElement(By.tagName("small")).getText(), "")
				.trim();
		// 日付を取得
		String sectionDetailDate = h2Element.findElement(
				By.tagName("small")).getText();

		// assert
		assertEquals(sectionName, sectionDetailName);
		assertEquals(sectionDate, sectionDetailDate);
		// screenshot
		getEvidence(new Object() {
		}, "SUCCESS");

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		// セクション詳細が表示されるまで待機
		visibilityTimeout(
				By.cssSelector("#sectionDetail h2"), 6);

		// 遷移前に h2 日付情報を取得
		WebElement detailH2SmallElement = driver.findElement(By.cssSelector("#sectionDetail h2 small"));
		String detailH2Date = detailH2SmallElement.getText();

		// 日報【デモ】を提出する click
		WebElement reportBtn = driver.findElement(
				By.cssSelector("input[type='submit'][value='提出済み日報【デモ】を確認する']"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", reportBtn);

		// 日報【デモ】が表示されるまで待機
		visibilityTimeout(
				By.cssSelector("#main h2"), 6);
		// h2 情報を取得
		WebElement h2Element = driver.findElement(By.cssSelector("#main h2"));
		// セクション名を取得
		String reportRegistName = h2Element.getText()
				.replace(h2Element.findElement(By.tagName("small")).getText(), "")
				.trim();
		// 日付を取得
		String reportRegistDate = h2Element.findElement(
				By.tagName("small")).getText();

		// assert
		assertEquals("日報【デモ】", reportRegistName);
		assertEquals(detailH2Date, reportRegistDate);
		// screenshot
		getEvidence(new Object() {
		}, "SUCCESS");

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() {
		// TODO ここに追加
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() {
		// TODO ここに追加
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() {
		// TODO ここに追加
	}

}
