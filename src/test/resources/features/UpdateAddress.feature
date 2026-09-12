Feature: Update Delivery Address During Checkout

  Background:
    Given the user is logged in and has items in the cart
    And the user is on the checkout page

  @HappyPath @Regression
  Scenario: Successfully update address to a new valid address
    When the user clicks on the "Change Address" button
    And the user enters a new valid address "123 New Street, London, 12345"
    And the user clicks "Save and Continue"
    Then the checkout page should display the new address "123 New Street, London, 12345"
    When the user places the order
    Then the order confirmation page should display the new address
    And the confirmation email should contain the new address "123 New Street, London, 12345"

  @Negative @Validation
  Scenario: Enter invalid postcode and check for error
    When the user clicks on the "Change Address" button
    And the user enters a new valid address "123 New Street, London, INVALID_POSTCODE"
    And the user clicks "Save and Continue"
    Then an error message "Please enter a valid postcode for the selected country" should be displayed

  @Regression @SavedAddress
  Scenario: Switch between multiple saved addresses
    When the user clicks on the "Change Address" button
    And the user selects a saved address "Home Address"
    And the user clicks "Save and Continue"
    Then the checkout page should display the new address "Home Address"
    When the user places the order
    Then the order management system should receive the order with the "Home Address"