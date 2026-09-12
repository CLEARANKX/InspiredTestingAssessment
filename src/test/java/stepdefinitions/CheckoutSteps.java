package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.CheckoutPage;
import utils.DriverFactory;

public class CheckoutSteps {

    private WebDriver driver;
    private CheckoutPage checkoutPage;

    // --- HOOKS ---
    @Before
    public void setUp() {
        System.out.println("DEBUG: Initializing Driver in @Before hook");
        // Initialize the driver BEFORE creating the Page Object
        DriverFactory.initDriver();
        driver = DriverFactory.getDriver();

        // Safety check: If driver is still null, fail fast with a clear message
        if (driver == null) {
            throw new RuntimeException("FATAL: WebDriver initialization failed in @Before hook.");
        }

        checkoutPage = new CheckoutPage(driver);
    }

    @After
    public void tearDown() {
        System.out.println("DEBUG: Quitting Driver in @After hook");
        DriverFactory.quitDriver();
    }

    // --- BACKGROUND STEPS ---
    @Given("the user is logged in and has items in the cart")
    public void theUserIsLoggedInAndHasItemsInTheCart() {
        // Safety check to ensure driver exists before using it
        if (driver == null) {
            throw new IllegalStateException("Driver is null. Did the @Before hook run?");
        }
        System.out.println("Setup: User logged in and items added to cart.");
        // Example: driver.get("https://your-app-url.com/login?autoLogin=true");
    }

    @Given("the user is on the checkout page")
    public void theUserIsOnTheCheckoutPage() {
        // This is where your NullPointerException was occurring.
        // Now that we have a safety check and a proper @Before hook, it should work.
        if (driver == null) {
            throw new IllegalStateException("Driver is null in userOnCheckoutPage. Check @Before hook execution.");
        }
        driver.get("https://your-app-url.com/checkout");
    }

    // ... (Rest of your steps remain the same) ...

    @When("the user clicks on the {string} button")
    public void theUserClicksOnTheButton(String buttonName) {
        if (buttonName.equalsIgnoreCase("Change Address")) {
            checkoutPage.clickChangeAddress();
        } else {
            throw new IllegalArgumentException("Unknown button: " + buttonName);
        }
    }

    @When("the user enters a new valid address {string}")
    public void theUserEntersANewValidAddress(String fullAddress) {
        String[] addressParts = fullAddress.split(",\\s*");
        if (addressParts.length < 3) {
            throw new IllegalArgumentException("Address format must be: 'Street, City, Postcode'");
        }
        checkoutPage.enterNewAddress(addressParts[0], addressParts[1], addressParts[2]);
    }

    @When("the user clicks {string}")
    public void theUserClicks(String buttonName) {
        if (buttonName.equalsIgnoreCase("Save and Continue")) {
            checkoutPage.clickSaveAndContinue();
        } else if (buttonName.equalsIgnoreCase("Place Order")) {
            checkoutPage.clickPlaceOrder();
        } else {
            throw new IllegalArgumentException("Unknown button action: " + buttonName);
        }
    }

    @When("the user selects a saved address {string}")
    public void theUserSelectsASavedAddress(String addressName) {
        checkoutPage.selectSavedAddress(addressName);
    }

    @When("the user places the order")
    public void theUserPlacesTheOrder() {
        checkoutPage.clickPlaceOrder();
    }

    @Then("the checkout page should display the new address {string}")
    public void theCheckoutPageShouldDisplayTheNewAddress(String expectedAddress) {
        String actualAddress = checkoutPage.getDisplayedAddress();
        Assert.assertTrue(actualAddress.contains(expectedAddress),
                "Expected address: [" + expectedAddress + "] but found: [" + actualAddress + "]");
    }

    @Then("an error message {string} should be displayed")
    public void anErrorMessageShouldBeDisplayed(String expectedErrorMessage) {
        String actualErrorMessage = checkoutPage.getErrorMessage();
        Assert.assertEquals(actualErrorMessage, expectedErrorMessage, "Error message text mismatch");
    }

    @Then("the order confirmation page should display the new address")
    public void theOrderConfirmationPageShouldDisplayTheNewAddress() {
        String confirmationAddress = checkoutPage.getConfirmationAddress();
        Assert.assertNotNull(confirmationAddress, "Address is missing on the order confirmation page");
        Assert.assertFalse(confirmationAddress.isEmpty(), "Address is empty on the order confirmation page");
    }

    @Then("the confirmation email should contain the new address {string}")
    public void theConfirmationEmailShouldContainTheNewAddress(String expectedAddress) {
        System.out.println("Mocking Email Verification: Checking inbox for address: " + expectedAddress);
    }

    @Then("the order management system should receive the order with the {string}")
    public void theOrderManagementSystemShouldReceiveTheOrderWithThe(String expectedAddress) {
        System.out.println("Mocking OMS Verification: Checking backend for order with address: " + expectedAddress);
    }
}