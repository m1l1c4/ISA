package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AdminPage {
	
	private WebDriver driver;

	@FindBy(id = "zahteviE2E")
	private WebElement requestsE2E;		// navbar click
	
	@FindBy(id = "zakaziE2E")
	private WebElement bookCheckupE2E;		// redirect to checkup page, li element

	@FindBy(id = "findRoomE2E")
	private WebElement findRoomE2E;		// find room for checkup
	
	@FindBy(id = "chooseRoomE2E")
	private WebElement chooseRoomE2E;		// table id, click on row inside table
	
	@FindBy(id = "bookRoomE2E")
	private WebElement bookRoomE2E;
	
	@FindBy(xpath = "//*[@id=\"chooseRoomE2E\"]//tr")
	private List<WebElement> rows;
	
	@FindBy(xpath = "//*[@id=\"zakaziE2E\"]//li")
	private List<WebElement> lis;

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public WebElement getRequestsE2E() {
		return requestsE2E;
	}

	public void setRequestsE2E(WebElement requestsE2E) {
		this.requestsE2E = requestsE2E;
	}

	public WebElement getBookCheckupE2E() {
		return bookCheckupE2E;
	}

	public void setBookCheckupE2E(WebElement bookCheckupE2E) {
		this.bookCheckupE2E = bookCheckupE2E;
	}

	public WebElement getFindRoomE2E() {
		return findRoomE2E;
	}

	public void setFindRoomE2E(WebElement findRoomE2E) {
		this.findRoomE2E = findRoomE2E;
	}

	public WebElement getChooseRoomE2E() {
		return chooseRoomE2E;
	}

	public void setChooseRoomE2E(WebElement chooseRoomE2E) {
		this.chooseRoomE2E = chooseRoomE2E;
	}

	public WebElement getBookRoomE2E() {
		return bookRoomE2E;
	}

	public void setBookRoomE2E(WebElement bookRoomE2E) {
		this.bookRoomE2E = bookRoomE2E;
	}
	
	
	public void ensureIsDisplayedRequestsE2E() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOf(requestsE2E));
	}

	public void ensureIsDisplayedbookCheckupE2E() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(bookCheckupE2E));
	}

	public void ensureIsDisplayedbookRoomE2E() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(bookRoomE2E));
	}

	public void ensureIsDisplayedchooseRoomE2E() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(chooseRoomE2E));
	}

	public AdminPage(WebDriver driver) {
		super();
		this.driver = driver;
	}
	
	public AdminPage() {
		super();
	}

	public void ensureIsDisplayedfindRoomE2E() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(findRoomE2E));
	}

	public WebElement findFirstBookingButton() {
		List<WebElement> unorderedList = bookCheckupE2E.findElements(By.tagName("li"));
		WebElement li = unorderedList.get(0);
		return li.findElement(By.tagName("button"));
	}
	
	public WebElement findFirstRoomButton() {
		return rows.get(1).findElement(By.tagName("button"));
	}
	
	public void scroll(int x, int y) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("scroll(0, 700);");
	}

	public List<WebElement> getRows() {
		return rows;
	}

	public void setRows(List<WebElement> rows) {
		this.rows = rows;
	}
	
	public void ensureRows() {
		(new WebDriverWait(driver, 20)).until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver input) {
				// TODO Auto-generated method stub
				return rows.size() > 1;
			}
		});
	}
	
	public void ensureList() {
		(new WebDriverWait(driver, 20)).until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver input) {
				// TODO Auto-generated method stub
				return lis.size() > 1;
			}
		});
	}
}
