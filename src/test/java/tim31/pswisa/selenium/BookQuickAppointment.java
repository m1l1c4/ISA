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

import pages.LogginPage;
import pages.PatientPage;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BookQuickAppointment {

	private WebDriver browser;

	private LogginPage logginPage;

	private PatientPage patientPage;

	@Before
	public void setUp() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
		browser = new ChromeDriver();
		browser.manage().window().maximize();
		browser.navigate().to("http://localhost:3000/register-page");
		logginPage = PageFactory.initElements(browser, LogginPage.class);
		patientPage = PageFactory.initElements(browser, PatientPage.class);
	}
	@Rollback
	@Test
	public void testBookQuickAppointment() throws InterruptedException {
		logginPage.login("pacijent@gmail.com", "sifra1");

		patientPage.ensureIsDisplayedAllClinicsE2E();
		assertEquals("http://localhost:3000/patient-page/5", browser.getCurrentUrl());

		patientPage.getAllClinicsE2E().click();

		patientPage.ensureRows();

		WebElement clinicProfileButton = patientPage.findFirstButtonClinic();
		clinicProfileButton.click();

		patientPage.ensureIsDisplayedQuickAppointments();
		assertEquals("http://localhost:3000/clinic-homepage/1", browser.getCurrentUrl());
		patientPage.getQuickAppointments().click();

		patientPage.ensureCards();
		List<WebElement> cards = patientPage.getCards();
		assertEquals(1, cards.size());

		WebElement buttonBook = patientPage.findFirstButtonBook();
		buttonBook.click();

		patientPage.ensureIsDisplayedAlert();
		List<WebElement> cards2 = patientPage.getCards();
		assertEquals(0, cards2.size());
	}

	@After
	public void tearDown() {
		browser.close();
	}
}
