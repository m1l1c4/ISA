package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LogginPage {

	private WebDriver driver;

	@FindBy(id = "loginEmail")
	private WebElement loginEmail;

	@FindBy(id = "loginPassword")
	private WebElement loginPassword;

	@FindBy(id = "confirmButton")
	private WebElement confirmButton;

	@FindBy(id = "showModalLogin")
	private WebElement showModalLogin;

	public LogginPage() {
	}

	public LogginPage(WebDriver driver) {
		this.driver = driver;
	}

	public void ensureIsDisplayedEmail() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(loginEmail));
	}

	public void ensureIsNotVisibleModal() {
		(new WebDriverWait(driver, 30)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("confirmButton")));
	}

	public void ensureIsDisplayedPassword() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(loginPassword));
	}

	public void ensureIsDisplayedButton() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(confirmButton));
	}

	public void ensureIsDisplayedShowModalLogin() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(showModalLogin));
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public WebElement getLoginEmail() {
		return loginEmail;
	}

	public void setLoginEmail(WebElement loginEmail) {
		this.loginEmail = loginEmail;
	}

	public WebElement getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(WebElement loginPassword) {
		this.loginPassword = loginPassword;
	}

	public WebElement getConfirmButton() {
		return confirmButton;
	}

	public void setConfirmButton(WebElement confirmButton) {
		this.confirmButton = confirmButton;
	}

	public WebElement getShowModalLogin() {
		return showModalLogin;
	}

	public void setShowModallogin(WebElement showModallogin) {
		this.showModalLogin = showModallogin;
	}

	public void login(String email, String password) {
		getShowModalLogin().click();
		ensureIsDisplayedEmail();
		loginEmail.sendKeys(email);
		loginPassword.sendKeys("sifra1");
		confirmButton.click();
		ensureIsNotVisibleModal();
	}
}