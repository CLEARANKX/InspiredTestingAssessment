package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CheckoutPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators (Replace with actual IDs/Classes from your app)
    private By changeAddressBtn = By.id("change-address-btn");
    private By addressLine1Input = By.id("address-line1");
    private By cityInput = By.id("city");
    private By postcodeInput = By.id("postcode");
    private By saveAddressBtn = By.id("save-address-btn");
    private By displayedAddressText = By.id("displayed-delivery-address");
    private By errorMessage = By.className("error-message");
    private By placeOrderBtn = By.id("place-order-btn");
    private By savedAddressDropdown = By.id("saved-addresses");
    private By confirmationAddress = By.id("confirmation-address");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickChangeAddress() {
        wait.until(ExpectedConditions.elementToBeClickable(changeAddressBtn)).click();
    }

    public void enterNewAddress(String addressLine1, String city, String postcode) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(addressLine1Input)).clear();
        driver.findElement(addressLine1Input).sendKeys(addressLine1);
        driver.findElement(cityInput).clear();
        driver.findElement(cityInput).sendKeys(city);
        driver.findElement(postcodeInput).clear();
        driver.findElement(postcodeInput).sendKeys(postcode);
    }

    public void clickSaveAndContinue() {
        driver.findElement(saveAddressBtn).click();
    }

    public String getDisplayedAddress() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(displayedAddressText)).getText();
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
    }

    public void selectSavedAddress(String addressName) {
        // Assuming a dropdown, this might differ based on UI implementation
        WebElement dropdown = driver.findElement(savedAddressDropdown);
        // Logic to select the specific option based on visible text
        // This is a simplified example
        dropdown.sendKeys(addressName);
    }

    public void clickPlaceOrder() {
        driver.findElement(placeOrderBtn).click();
    }

    public String getConfirmationAddress() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(confirmationAddress)).getText();
    }
}