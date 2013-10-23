Feature: RPC Security

Scenario: Check behavior when no user is logged in
   When the user views the RPC test page
   Then the user should be taken to the RPC test page
   When the user invokes the "unprotectedRpc" on the RPC test page
   Then the "unprotectedRpc" result should display "unprotectedRpc/value"
   When the user invokes the "userRpc" on the RPC test page
   Then the "Error Occurred" pop-up should be visible
    And the "Error Occurred" pop-up should contain "There was an error invoking a remote service: you are not authorized."
   When the user clicks the "Error Occurred" pop-up "Close" button
   Then the "Error Occurred" pop-up should be hidden
    And the "userRpc" result should be empty
   When the user invokes the "adminRpc" on the RPC test page
   Then the "Error Occurred" pop-up should be visible
    And the "Error Occurred" pop-up should contain "There was an error invoking a remote service: you are not authorized."
   When the user clicks the "Error Occurred" pop-up "Close" button
   Then the "Error Occurred" pop-up should be hidden
    And the "adminRpc" result should be empty
   When the user invokes the "enabledUserRpc" on the RPC test page
   Then the "Error Occurred" pop-up should be visible
    And the "Error Occurred" pop-up should contain "There was an error invoking a remote service: you are not authorized."
   When the user clicks the "Error Occurred" pop-up "Close" button
   Then the "Error Occurred" pop-up should be hidden
    And the "enabledUserRpc" result should be empty
   When the user invokes the "enabledAdminRpc" on the RPC test page
   Then the "Error Occurred" pop-up should be visible
    And the "Error Occurred" pop-up should contain "There was an error invoking a remote service: you are not authorized."
   When the user clicks the "Error Occurred" pop-up "Close" button
   Then the "Error Occurred" pop-up should be hidden
    And the "enabledAdminRpc" result should be empty

Scenario: Check behavior when logged in as normal user
   When the user views the canonical site url
    And the user logs in with normal user credentials
   Then the user should be taken to the internal landing page
   When the user views the RPC test page
   Then the user should be taken to the RPC test page
   When the user invokes the "unprotectedRpc" on the RPC test page
   Then the "unprotectedRpc" result should display "unprotectedRpc/value"
   When the user invokes the "userRpc" on the RPC test page
   Then the "userRpc" result should display "userRpc/value"
   When the user invokes the "adminRpc" on the RPC test page
   Then the "Error Occurred" pop-up should be visible
    And the "Error Occurred" pop-up should contain "There was an error invoking a remote service: you are not authorized."
   When the user clicks the "Error Occurred" pop-up "Close" button
   Then the "Error Occurred" pop-up should be hidden
    And the "adminRpc" result should be empty
   When the user invokes the "enabledUserRpc" on the RPC test page
   Then the "enabledUserRpc" result should display "enabledUserRpc/value"
   When the user invokes the "enabledAdminRpc" on the RPC test page
   Then the "Error Occurred" pop-up should be visible
    And the "Error Occurred" pop-up should contain "There was an error invoking a remote service: you are not authorized."
   When the user clicks the "Error Occurred" pop-up "Close" button
   Then the "Error Occurred" pop-up should be hidden
    And the "enabledAdminRpc" result should be empty
   When the user views an "Internal Landing Page" bookmark
   Then the user should be taken to the internal landing page
   When the user chooses the "Sign Out" internal menu option
   Then the user should be taken to the external landing page

Scenario: Check behavior when logged in as an admin user
   When the user views the canonical site url
    And the user logs in with admin credentials
   Then the user should be taken to the "Admin Home" admin tab
   When the user views the RPC test page
   Then the user should be taken to the RPC test page
   When the user invokes the "unprotectedRpc" on the RPC test page
   Then the "unprotectedRpc" result should display "unprotectedRpc/value"
   When the user invokes the "userRpc" on the RPC test page
   Then the "userRpc" result should display "userRpc/value"
   When the user invokes the "adminRpc" on the RPC test page
   Then the "adminRpc" result should display "adminRpc/value"
   When the user invokes the "enabledUserRpc" on the RPC test page
   Then the "enabledUserRpc" result should display "enabledUserRpc/value"
   When the user invokes the "enabledAdminRpc" on the RPC test page
   Then the "enabledAdminRpc" result should display "enabledAdminRpc/value"
   When the user views an "Admin Home" admin tab bookmark
   Then the user should be taken to the "Admin Home" admin tab
   When the user chooses the "Sign Out" admin menu option
   Then the user should be taken to the external landing page
