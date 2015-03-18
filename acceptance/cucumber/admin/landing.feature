Feature: Admin Landing Page and Menus

Scenario: Log in for the first time
    When the user views the canonical site url
     And the user logs in with admin credentials
    Then the user should be taken to the "Admin Home" admin tab
     And the admin title bar should say "Santa Exchange Administrator Interface"
     And the admin menu should display the admin email address
     And the "Internal Landing Page" admin menu option should be visible
     And the "Application Dashboard" admin menu option should be visible
     And the "About Santa Exchange" admin menu option should be visible
     And the "Report a Problem" admin menu option should be visible
     And the "Source Code" admin menu option should be visible
     And the "Sign Out" admin menu option should be visible
    When the user chooses the "Sign Out" admin menu option
    Then the user should be taken to the external landing page

Scenario: Log in subsequent times
    When the user views the canonical site url
     And the user logs in with admin credentials
    Then the user should be taken to the "Admin Home" admin tab
    When the user chooses the "Sign Out" admin menu option
    Then the user should be taken to the external landing page

Scenario: Choose the "Internal Landing Page" admin menu option
    When the user views the canonical site url
     And the user logs in with admin credentials
    Then the user should be taken to the "Admin Home" admin tab
    When the user chooses the "Internal Landing Page" admin menu option
    Then the user should be taken to the internal landing page
     And the internal title bar should say "Santa Exchange"
     And the internal menu should display the admin email address
     And the "Admin Landing Page" internal menu option should be visible
     And the "About Santa Exchange" internal menu option should be visible
     And the "Report a Problem" internal menu option should be visible
     And the "Source Code" internal menu option should be visible
    And the "Sign Out" internal menu option should be visible
    When the user chooses the "Admin Landing Page" internal menu option
    Then the user should be taken to the "Admin Home" admin tab
    When the user chooses the "Sign Out" admin menu option
    Then the user should be taken to the external landing page

Scenario: Choose the "Application Dashboard" admin menu option
    Given that external link checking is enabled
    When the user views the canonical site url
     And the user logs in with admin credentials
    Then the user should be taken to the "Admin Home" admin tab
    When the user chooses the "Application Dashboard" admin menu option
    Then the user should be taken to the external application dashboard page
    When the user views an "Admin Landing Page" bookmark
    Then the user should be taken to the "Admin Home" admin tab
    When the user chooses the "Sign Out" admin menu option
    Then the user should be taken to the external landing page

Scenario: Choose the "About Santa Exchange" admin menu option
    When the user views the canonical site url
     And the user logs in with admin credentials
    Then the user should be taken to the "Admin Home" admin tab
    When the user chooses the "About Santa Exchange" admin menu option
    Then the "About Santa Exchange" pop-up should be visible
    When the user clicks the "About Santa Exchange" pop-up "Close" button
    Then the "About Santa Exchange" pop-up should be hidden
    When the user chooses the "About Santa Exchange" admin menu option
    Then the "About Santa Exchange" pop-up should be visible
    When the user presses the Enter key on the "About Santa Exchange" pop-up
    Then the "About Santa Exchange" pop-up should be hidden
    When the user chooses the "About Santa Exchange" admin menu option
    Then the "About Santa Exchange" pop-up should be visible
    When the user presses the Escape key on the "About Santa Exchange" pop-up
    Then the "About Santa Exchange" pop-up should be hidden
    When the user chooses the "Sign Out" admin menu option
    Then the user should be taken to the external landing page

Scenario: Choose the "Report a Problem" admin menu option
    When the user views the canonical site url
     And the user logs in with admin credentials
    Then the user should be taken to the "Admin Home" admin tab
    When the user chooses the "Report a Problem" admin menu option
    Then the "Report a Problem" pop-up should be visible
    When the user presses the Escape key on the "Report a Problem" pop-up
    Then the "Report a Problem" pop-up should be visible
    When the user presses the Enter key on the "Report a Problem" pop-up
    Then the "Report a Problem" pop-up should be visible
    When the user clicks the "Report a Problem" pop-up "Cancel" button
    Then the "Report a Problem" pop-up should be hidden
    When the user chooses the "Sign Out" admin menu option
    Then the user should be taken to the external landing page

Scenario: Choose the "Source Code" admin menu option
    Given that external link checking is enabled
    When the user views the canonical site url
     And the user logs in with admin credentials
    Then the user should be taken to the "Admin Home" admin tab
    When the user chooses the "Source Code" admin menu option
    Then the user should be taken to the external BitBucket page
    When the user views an "Admin Landing Page" bookmark
    Then the user should be taken to the "Admin Home" admin tab
    When the user chooses the "Sign Out" admin menu option
    Then the user should be taken to the external landing page

Scenario: Go to the base landing page bookmark when logged in
    When the user views the canonical site url
     And the user logs in with admin credentials
    Then the user should be taken to the "Admin Home" admin tab
    When the user views an "Base Landing Page" bookmark
    Then the user should be taken to the "Admin Home" admin tab
    When the user chooses the "Sign Out" admin menu option
    Then the user should be taken to the external landing page

Scenario: Go to the external landing page bookmark when logged in
    When the user views the canonical site url
     And the user logs in with admin credentials
    Then the user should be taken to the "Admin Home" admin tab
    When the user views an "External Landing Page" bookmark
    Then the user should be taken to the external landing page
    When the user clicks the "Sign In With Google" button
    Then the user should be taken to the "Admin Home" admin tab
    When the user chooses the "Sign Out" admin menu option
    Then the user should be taken to the external landing page

Scenario: Go to the admin landing page bookmark when logged in
    When the user views the canonical site url
     And the user logs in with admin credentials
    Then the user should be taken to the "Admin Home" admin tab
    When the user views an "Admin Landing Page" bookmark
    Then the user should be taken to the "Admin Home" admin tab
    When the user chooses the "Sign Out" admin menu option
    Then the user should be taken to the external landing page

Scenario: Go to the admin home tab bookmark when logged in
    When the user views the canonical site url
     And the user logs in with admin credentials
    Then the user should be taken to the "Admin Home" admin tab
    When the user views an "Admin Home" admin tab bookmark
    Then the user should be taken to the "Admin Home" admin tab
    When the user chooses the "Sign Out" admin menu option
    Then the user should be taken to the external landing page

Scenario: Go to the audit tab bookmark when logged in
    When the user views the canonical site url
     And the user logs in with admin credentials
    Then the user should be taken to the "Admin Home" admin tab
    When the user views an "Auditing" admin tab bookmark
    Then the user should be taken to the "Auditing" admin tab
    When the user chooses the "Sign Out" admin menu option
    Then the user should be taken to the external landing page

Scenario: Go to the user maintenance tab bookmark when logged in
    When the user views the canonical site url
     And the user logs in with admin credentials
    Then the user should be taken to the "Admin Home" admin tab
    When the user views a "User Maintenance" admin tab bookmark
    Then the user should be taken to the "User Maintenance" admin tab
    When the user chooses the "Sign Out" admin menu option
    Then the user should be taken to the external landing page

Scenario: Go to the admin landing page bookmark when not logged in
    When the user views an "Admin Landing Page" bookmark
    Then the user should be taken to the login required page
    When the user logs in with admin credentials
    Then the user should be taken to the "Admin Home" admin tab
    When the user chooses the "Sign Out" admin menu option
    Then the user should be taken to the external landing page

Scenario: Go to the admin home tab bookmark when not logged in
    When the user views an "Admin Home" admin tab bookmark
    Then the user should be taken to the login required page
    When the user logs in with admin credentials
    Then the user should be taken to the "Admin Home" admin tab
    When the user chooses the "Sign Out" admin menu option
    Then the user should be taken to the external landing page

Scenario: Go to the audit tab bookmark when not logged in
    When the user views an "Auditing" admin tab bookmark
    Then the user should be taken to the login required page
    When the user logs in with admin credentials
    Then the user should be taken to the "Auditing" admin tab
    When the user chooses the "Sign Out" admin menu option
    Then the user should be taken to the external landing page

Scenario: Go to the user maintenance tab bookmark when not logged in
    When the user views a "User Maintenance" admin tab bookmark
    Then the user should be taken to the login required page
    When the user logs in with admin credentials
    Then the user should be taken to the "User Maintenance" admin tab
    When the user chooses the "Sign Out" admin menu option
    Then the user should be taken to the external landing page

Scenario: Enter a bug report
    When the user views the canonical site url
     And the user logs in with admin credentials
    Then the user should be taken to the "Admin Home" admin tab
    When the user chooses the "Report a Problem" admin menu option
    Then the "Report a Problem" pop-up should be visible
    When the user clicks the "Report a Problem" pop-up "Submit" button
    Then validation errors should be displayed on the "Report a Problem" pop-up
    When the user fills in valid bug report data in the "Report a Problem" pop-up
     And the user clicks the "Report a Problem" pop-up "Submit" button
    Then the "Report a Problem" pop-up should be hidden
    When the user chooses the "Sign Out" admin menu option
    Then the user should be taken to the external landing page

Scenario: Go to an invalid top-level bookmark when logged in as an admin user
    When the user views the canonical site url
     And the user logs in with admin credentials
    Then the user should be taken to the "Admin Home" admin tab
    When the user views an invalid top-level bookmark
    Then the "Error Occurred" pop-up should be visible
    When the user clicks the "Error Occurred" pop-up "Close" button
    Then the "Error Occurred" pop-up should be hidden
     And the user should be taken to the "Admin Home" admin tab

Scenario: Go to an invalid admin bookmark when logged in as an admin user
    When the user views the canonical site url
     And the user logs in with admin credentials
    Then the user should be taken to the "Admin Home" admin tab
    When the user views an invalid admin bookmark
    Then the "Error Occurred" pop-up should be visible
    When the user clicks the "Error Occurred" pop-up "Close" button
    Then the "Error Occurred" pop-up should be hidden
     And the user should be taken to the "Admin Home" admin tab

Scenario: Go to an invalid internal bookmark when logged in as an admin user
    When the user views the canonical site url
     And the user logs in with admin credentials
    Then the user should be taken to the "Admin Home" admin tab
    When the user views an invalid internal bookmark
    Then the "Error Occurred" pop-up should be visible
    When the user clicks the "Error Occurred" pop-up "Close" button
    Then the "Error Occurred" pop-up should be hidden
     And the user should be taken to the "Admin Home" admin tab
