package tim31.pswisa;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import tim31.pswisa.selenium.BookQuickAppointment;
import tim31.pswisa.selenium.SearchAndFilterClinics;
import tim31.pswisa.selenium.UpdateCheckupE2E;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	BookQuickAppointment.class, 
	SearchAndFilterClinics.class, 
	UpdateCheckupE2E.class })
public class E2ETests {

}
