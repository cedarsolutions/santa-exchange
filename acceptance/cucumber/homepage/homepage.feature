Feature: Public Homepage

Scenario: View the homepage
    When the user views the canonical site url
    Then the user should be taken to the external landing page
     And the page should have a link "Apache v2.0"
     And the page should have a link "Google Code"
     And the page should have a button "Sign In With Google"
     And the page should have content "exchange is a party where people get together"

Scenario: Go to the base landing page bookmark when not logged in
    When the user views an "Base Landing Page" bookmark
    Then the user should be taken to the external landing page

Scenario: Go to an invalid top-level bookmark when not logged in
    When the user views an invalid top-level bookmark
    Then the "Error Occurred" pop-up should be visible
    When the user clicks the "Error Occurred" pop-up "Close" button
    Then the "Error Occurred" pop-up should be hidden
     And the user should be taken to the external landing page

Scenario: Go to an invalid admin bookmark when not logged in
    When the user views an invalid admin bookmark
    Then the "Error Occurred" pop-up should be visible
    When the user clicks the "Error Occurred" pop-up "Close" button
    Then the "Error Occurred" pop-up should be hidden
     And the user should be taken to the external landing page

Scenario: Go to an invalid internal bookmark when not logged in
    When the user views an invalid internal bookmark
    Then the "Error Occurred" pop-up should be visible
    When the user clicks the "Error Occurred" pop-up "Close" button
    Then the "Error Occurred" pop-up should be hidden
     And the user should be taken to the external landing page

Scenario: Click the "Apache v2.0" link
    Given that external link checking is enabled
    When the user views the canonical site url
    Then the user should be taken to the external landing page
    When the user clicks the "Apache v2.0" link
    Then the user should be taken to the external Apache license page

Scenario: Click the "Google Code" link
    Given that external link checking is enabled
    When the user views the canonical site url
    Then the user should be taken to the external landing page
    When the user clicks the "Google Code" link
    Then the user should be taken to the external Google Code site for Santa Exchange
