package tim31.pswisa.selenium;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import pages.AdminPage;
import pages.LogginPage;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)public class UpdateCheckupE2E {
	private WebDriver browser;

	private LogginPage logginPage;

	private AdminPage adminPage;

	@Before
	public void setUp() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
		browser = new ChromeDriver();
		browser.manage().window().maximize();
		browser.navigate().to("http://localhost:3000/register-page");
		logginPage = PageFactory.initElements(browser, LogginPage.class);
		adminPage = PageFactory.initElements(browser, AdminPage.class);

	}

	@Rollback
	@Test
	public void testUpdateCheckup() throws InterruptedException {
		logginPage.login("admin@gmail.com", "sifra1");

		adminPage.ensureIsDisplayedRequestsE2E();
		assertEquals("http://localhost:3000/administrator-page", browser.getCurrentUrl());

		adminPage.getRequestsE2E().click();
		adminPage.ensureList();
		assertEquals("http://localhost:3000/registration-request", browser.getCurrentUrl());

		WebElement checkupForBookingButton = adminPage.findFirstBookingButton();
		checkupForBookingButton.click();

		adminPage.ensureIsDisplayedfindRoomE2E();
		assertEquals("http://localhost:3000/checkup/1", browser.getCurrentUrl());
		adminPage.getFindRoomE2E().click();

		adminPage.scroll(0, 700);


		adminPage.ensureRows();
		List<WebElement> freeRooms = adminPage.getRows();
		
		WebElement choosenRoom = adminPage.findFirstRoomButton();
		choosenRoom.click();
		
		assertEquals(4, freeRooms.size());

		adminPage.ensureIsDisplayedbookRoomE2E();
		WebElement book = adminPage.getBookRoomE2E();
		book.click();
		adminPage.ensureList();
		assertEquals("http://localhost:3000/registration-request", browser.getCurrentUrl());
	}

	@After
	public void tearDown() {
		browser.close();
	}
}
