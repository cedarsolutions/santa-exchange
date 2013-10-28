Feature: Locked Users

Scenario: Confirm that lock and unlock behavior works as expected
    When the user views the canonical site url
     And the user logs in with locked credentials
     Then the user should be taken to the internal landing page
    When the user chooses the "Sign Out" internal menu option
    Then the user should be taken to the external landing page
    When the user logs in with admin credentials
    Then the user should be taken to the "Admin Home" admin tab
    When the user clicks on the "User Maintenance" admin tab
    Then the user should be taken to the "User Maintenance" admin tab
     And the "Users" table should exist on the "User Maintenance" admin tab
     And the "Users" table on the "User Maintenance" admin tab should have a row for user "admin" that is unchecked
     And the "Users" table on the "User Maintenance" admin tab should have a row for user "locked" that is unchecked
     And the "Users" table on the "User Maintenance" admin tab should show the "admin" user as not locked
     And the "Users" table on the "User Maintenance" admin tab should show the "locked" user as not locked
    When the user clicks the checkbox for the "locked" user on the "Users" table on the "User Maintenance" admin tab
     Then the "Users" table on the "User Maintenance" admin tab should have a row for user "locked" that is checked
    When the user clicks the "Lock" button on the "User Maintenance" admin tab
    Then the "Users" table should exist on the "User Maintenance" admin tab
     And the "Users" table on the "User Maintenance" admin tab should show the "admin" user as not locked
     And the "Users" table on the "User Maintenance" admin tab should show the "locked" user as locked
    When the user chooses the "Sign Out" admin menu option
    Then the user should be taken to the external landing page
    When the user logs in with locked credentials
    Then the user should be taken to the account locked page
    When the user clicks the "Continue" button
    Then the user should be taken to the external landing page
    When the user logs in with admin credentials
    Then the user should be taken to the "Admin Home" admin tab
    When the user clicks on the "User Maintenance" admin tab
    Then the user should be taken to the "User Maintenance" admin tab
     And the "Users" table should exist on the "User Maintenance" admin tab
     And the "Users" table on the "User Maintenance" admin tab should have a row for user "admin" that is unchecked
     And the "Users" table on the "User Maintenance" admin tab should have a row for user "locked" that is unchecked
     And the "Users" table on the "User Maintenance" admin tab should show the "admin" user as not locked
     And the "Users" table on the "User Maintenance" admin tab should show the "locked" user as locked
    When the user clicks the checkbox for the "locked" user on the "Users" table on the "User Maintenance" admin tab
     Then the "Users" table on the "User Maintenance" admin tab should have a row for user "locked" that is checked
    When the user clicks the "Unlock" button on the "User Maintenance" admin tab
    Then the "Users" table should exist on the "User Maintenance" admin tab
     And the "Users" table on the "User Maintenance" admin tab should show the "admin" user as not locked
     And the "Users" table on the "User Maintenance" admin tab should show the "locked" user as not locked
    When the user chooses the "Sign Out" admin menu option
    Then the user should be taken to the external landing page
    When the user logs in with locked credentials
    Then the user should be taken to the internal landing page
    When the user chooses the "Sign Out" internal menu option
    Then the user should be taken to the external landing page
