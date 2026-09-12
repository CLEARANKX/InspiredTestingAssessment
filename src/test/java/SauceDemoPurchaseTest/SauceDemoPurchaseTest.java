package SauceDemoPurchaseTest;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.Duration;

public class SauceDemoPurchaseTest {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By addToCartBackpack = By.id("add-to-cart-sauce-labs-backpack");
    private final By cartIcon = By.className("shopping_cart_link");
    private final By checkoutButton = By.id("checkout");
    private final By firstNameField = By.id("first-name");
    private final By lastNameField = By.id("last-name");
    private final By postalCodeField = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By finishButton = By.id("finish");
    private final By confirmationHeader = By.className("complete-header");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Initialize Explicit Wait with a 15-second timeout
        // We increased this slightly because the site is shared and can be slow
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        driver.get("https://www.saucedemo.com/");
    }

    @Test(priority = 1, description = "Positive Case: Standard user successfully completes purchase")
    public void testSuccessfulPurchase() {
        // 1. Log in - Wait for username field to be visible before interacting
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).sendKeys("standard_user");
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).sendKeys("secret_sauce");
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();

        // 2. Select product and add to cart
        // Wait for the inventory page to load by waiting for the add-to-cart button
        wait.until(ExpectedConditions.elementToBeClickable(addToCartBackpack)).click();

        // 3. Go to cart and checkout
        wait.until(ExpectedConditions.elementToBeClickable(cartIcon)).click();
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();

        // 4. Enter details - Wait for the form to be visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField)).sendKeys("John");
        wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameField)).sendKeys("Doe");
        wait.until(ExpectedConditions.visibilityOfElementLocated(postalCodeField)).sendKeys("12345");
        wait.until(ExpectedConditions.elementToBeClickable(continueButton)).click();

        // 5. Finish and confirm
        wait.until(ExpectedConditions.elementToBeClickable(finishButton)).click();

        // Assertion: Wait for the confirmation header to be visible, then extract text
        WebElement confirmHeaderElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(confirmationHeader)
        );
        String confirmationText = confirmHeaderElement.getText();

        Assert.assertEquals(confirmationText, "Thank you for your order!", "Order confirmation failed.");
    }

    @Test(priority = 2, description = "Negative Case: Locked out user cannot login")
    public void testLockedOutUserLogin() {
        // 1. Attempt login with locked_out_user
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).sendKeys("locked_out_user");
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).sendKeys("secret_sauce");
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();

        // Assertion: Wait for the error message to appear before asserting
        WebElement errorElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(errorMessage)
        );
        String actualError = errorElement.getText();

        Assert.assertTrue(actualError.contains("Sorry, this user has been locked out"),
                "Expected locked out error message not displayed.");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
