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
 * ケース07
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース07 受講生 レポート新規登録(日報) 正常系")
public class Case07 {

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
	@DisplayName("テスト03 未提出の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {

		pageLoadTimeout(60);

		// 一覧の行が表示されるまで待機
		visibilityTimeout(
				By.cssSelector("tbody tr"), 6);

		// 一覧の行をすべて取得
		List<WebElement> rows = driver.findElements(
				By.cssSelector("tbody tr"));
		WebElement report = null;

		// 各行の内容を表示
		for (WebElement row : rows) {
			if (row.getText().contains("未提出")) {
				report = row;
				break;
			}
		}
		// println 確認用
		//		System.out.println("行：" + report.getText());

		// セクション情報を取得
		String overCharsectionDate = report.findElement(
				By.cssSelector("td.w20per")).getText();
		// 曜日削除
		String sectionDate = overCharsectionDate.substring(0, overCharsectionDate.length() - 3);

		String sectionName = report.findElement(
				By.cssSelector("td.wh")).getText();

		// println 確認用
		//		System.out.println("日付：" + sectionDate);
		//		System.out.println("セクション名：" + sectionName);

		// 「詳細」ボタンを取得
		WebElement detailButton = report.findElement(
				By.cssSelector("input[type='submit'][value='詳細']"));
		// 「詳細」ボタンを押下
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", detailButton);

		// println 確認用
		//		System.out.println("クリック後URL：" + driver.getCurrentUrl());
		//		System.out.println("クリック後タイトル：" + driver.getTitle());

		// h2 表示されるまで待機
		visibilityTimeout(By.cssSelector("#sectionDetail h2"), 6);
		WebElement h2text = driver.findElement(By.cssSelector("#sectionDetail h2"));
		// println 確認用
		//		System.out.println(h2text.getText());

		// セクション名を取得
		String sectionDetailName = h2text.getText()
				.replace(h2text.findElement(By.tagName("small")).getText(), "")
				.trim();

		// 日付を取得
		String sectionDetailDate = h2text.findElement(
				By.tagName("small")).getText();

		// println 確認用
		//		System.out.println("詳細画面セクション名：" + sectionDetailName);
		//		System.out.println("詳細画面日付：" + sectionDetailDate);

		// assert
		assertEquals(sectionName, sectionDetailName);
		assertEquals(sectionDate, sectionDetailDate);
		// screenshot
		getEvidence(new Object() {
		}, "SUCCESS");

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「提出する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		// TODO ここに追加
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を入力して「提出する」ボタンを押下し確認ボタン名が更新される")
	void test05() {
		// TODO ここに追加
	}

}
