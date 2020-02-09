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
public class CheckupToAdmin {

	private WebDriver browser;

	private LogginPage logginPage;

	private PatientPage patientPage;

	@Before
	public void setup() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
		browser = new ChromeDriver();
		browser.manage().window().maximize();
		browser.navigate().to("http://localhost:3000/register-page");
		logginPage = PageFactory.initElements(browser, LogginPage.class);
		patientPage = PageFactory.initElements(browser, PatientPage.class);
	}

	@Rollback
	@Test
	public void seleniumBookCheckupOk() throws InterruptedException {
		logginPage.login("pacijent@gmail.com", "sifra1");
		patientPage.ensureIsDisplayedAllClinicsE2E();
		assertEquals("http://localhost:3000/patient-page/5", browser.getCurrentUrl());
		patientPage.getAllClinicsE2E().click();

		patientPage.ensureIsDisplayedTypeOfCheckupE2E();
		patientPage.setTypeOfCheckup("KARDIOLOSKI");
		patientPage.setDateOfCheckup("23", "Jan", "2020");

		patientPage.getSearchE2E().click();
		patientPage.ensureRows();
		patientPage.ensureIsDisplayedFilter();
		List<WebElement> tableAfterSearch = patientPage.getRows();
		assertEquals(1, tableAfterSearch.size() - 1);

		patientPage.setOcjena("5");

		patientPage.ensureIsDisplayedLabel();
		List<WebElement> tableAfterFilter = patientPage.getRows();
		assertEquals(1, tableAfterFilter.size() - 1);
		
		WebElement choosenClinic = patientPage.findFirstClinicButton();
		choosenClinic.click();
		patientPage.ensureRowsDoctors();
		assertEquals(1, patientPage.getRowsDoctors().size()-1);
		//patientPage.ensureIsDisplayDoctorSearch();
		WebElement choosenDoctor = patientPage.findFirstButtonDoctor();
		choosenDoctor.click();
		patientPage.ensureIsDisplayedModal1();
		patientPage.setTimeOfCheckup("8");
		patientPage.getButtonFirstClick().click();
		patientPage.ensureIsDisplayedModal2();
		patientPage.getButtonSecondClick().click();
		patientPage.ensureIsNotVisible();
		//assertEquals(1, 1);
		
		
		
		
		

	}

	@Test
	public void seleniumSearchAndFilterClinicsNonEnteredValue() throws InterruptedException {

		logginPage.login("pacijent@gmail.com", "sifra1");
		patientPage.ensureIsDisplayedAllClinicsE2E();
		assertEquals("http://localhost:3000/patient-page/5", browser.getCurrentUrl());
		patientPage.getAllClinicsE2E().click();

		patientPage.ensureIsDisplayedDateOfCheckupE2E();
		patientPage.setDateOfCheckup("23", "Jan", "2020");

		patientPage.getSearchE2E().click();
		patientPage.ensureIsDisplayedFilter();
		List<WebElement> tableAfterSearch = patientPage.getRows();
		assertEquals(1, tableAfterSearch.size() - 1);

		patientPage.setOcjena("20");
		patientPage.ensureIsDisplayedLabel();
		List<WebElement> tableAfterFilter = patientPage.getRows();
		assertEquals(0, tableAfterFilter.size() - 1);

	}

	@After
	public void tearDown() throws InterruptedException {
		browser.close();
	}

}