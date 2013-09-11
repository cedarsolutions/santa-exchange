Feature: Edit Exchange Functionality

Scenario: Check that that table is empty as expected
    When the user views the canonical site url
     And the user logs in with normal user credentials
    Then the user should be taken to the internal landing page
     And the exchange list table should be empty
     And the exchange list pager should say "0 of 0"

Scenario: Create an exchange and then delete it
    When the user clicks the "Create New" button
    Then the exchange list table should have 1 data row with exchange name "My New Exchange"
     And the exchange list pager should say "1-1 of 1"
    When the user clicks the "Delete" button
    Then the "Confirm Delete" pop-up should be hidden
     And the exchange list table should have 1 data row with exchange name "My New Exchange"
     And the exchange list pager should say "1-1 of 1"
    When the user clicks the checkbox in exchange list row 1, column 1
     And the user clicks the "Delete" button
    Then the "Confirm Delete" pop-up should be visible
    When the user clicks the "Confirm Delete" pop-up "No" button
    Then the "Confirm Delete" pop-up should be hidden
    And the exchange list table should have 1 data row with exchange name "My New Exchange"
     And the exchange list pager should say "1-1 of 1"
    When the user clicks the "Delete" button
    Then the "Confirm Delete" pop-up should be visible
    When the user clicks the "Confirm Delete" pop-up "Yes" button
    Then the "Confirm Delete" pop-up should be hidden
     And the exchange list table should be empty
     And the exchange list pager should say "0 of 0"

Scenario: Create two exchanges and delete them with the select all checkbox
    When the user clicks the "Create New" button
    Then the exchange list table should have 1 data row with exchange name "My New Exchange"
     And the exchange list pager should say "1-1 of 1"
    When the user clicks the "Create New" button
    Then the exchange list table should have 2 data rows with exchange name "My New Exchange"
     And the exchange list pager should say "1-2 of 2"
    When the user clicks the "Delete" button
    Then the "Confirm Delete" pop-up should be hidden
     And the exchange list table should have 2 data rows with exchange name "My New Exchange"
     And the exchange list pager should say "1-2 of 2"
    When the user clicks the exchange list select all checkbox
     And the user clicks the "Delete" button
    Then the "Confirm Delete" pop-up should be visible
    When the user clicks the "Confirm Delete" pop-up "No" button
    Then the "Confirm Delete" pop-up should be hidden
     And the exchange list table should have 2 data rows with exchange name "My New Exchange"
     And the exchange list pager should say "1-2 of 2"
    When the user clicks the "Delete" button
    Then the "Confirm Delete" pop-up should be visible
    When the user clicks the "Confirm Delete" pop-up "Yes" button
    Then the "Confirm Delete" pop-up should be hidden
     And the exchange list table should be empty
     And the exchange list pager should say "0 of 0"

Scenario: Create an exchange and change its name
    When the user clicks the "Create New" button
    Then the exchange list table should have 1 data row with exchange name "My New Exchange"
     And the exchange list pager should say "1-1 of 1"
    When the user clicks the first data row in the exchange list table
    Then the user should be taken to the edit exchange page
     And the exchange name should be "My New Exchange"
     And the participant list table should be empty
     And the participant list pager should say "0 of 0"
    When the user enters exchange name "First Exchange"
     And the user clicks the "<< Return to List" button
    Then the user should be taken to the internal landing page
     And the exchange list table should have 1 data row with exchange name "First Exchange"
     And the exchange list pager should say "1-1 of 1"

Scenario: Check the revert changes behavior
    When the user clicks the first data row in the exchange list table
    Then the user should be taken to the edit exchange page
     And the exchange status should be "Started"
     And the exchange name should be "First Exchange"
     And the participant list table should be empty
     And the participant list pager should say "0 of 0"
     And the theme should be ""
    When the user enters theme "My Theme"
     And the user clicks the "Revert Changes" button
    Then the "Are you sure?" pop-up should be visible
    When the user clicks the "Are you sure?" pop-up "No" button
    Then the "Are you sure?" pop-up should be hidden
     And the exchange name should be "First Exchange"
     And the participant list table should be empty
     And the participant list pager should say "0 of 0"
     And the theme should be "My Theme"
    When the user clicks the "Revert Changes" button
    Then the "Are you sure?" pop-up should be visible
    When the user clicks the "Are you sure?" pop-up "Yes" button
    Then the "Are you sure?" pop-up should be hidden
     And the exchange name should be "First Exchange"
     And the participant list table should be empty
     And the participant list pager should say "0 of 0"
     And the theme should be ""

Scenario: Fill in valid exchange data and check save
    When the user enters theme "My Theme"
     And the user enters date and time "My Date and Time"
     And the user enters suggested cost "My Suggested Cost"
     And the user enters organizer name "My Organizer Name"
     And the user enters organizer email "My Organizer Email"
     And the user enters organizer phone "My Organizer Phone"
     And the user enters extra information "My Extra Information"
     And the user clicks the "<< Return to List" button
    Then the user should be taken to the internal landing page
    When the user clicks the first data row in the exchange list table
     Then the user should be taken to the edit exchange page
     And the exchange name should be "First Exchange"
     And the date and time should be "My Date and Time"
     And the suggested cost should be "My Suggested Cost"
     And the organizer name should be "My Organizer Name"
     And the organizer email should be "My Organizer Email"
     And the organizer phone should be "My Organizer Phone"
     And the extra information should be "My Extra Information"

Scenario: Create a participant and cancel it
    When the user clicks the "Add" button
    Then the user should be taken to the edit participant page
     And the participant name should be "New Participant"
     And the participant nickname should be ""
     And the participant email address should be ""
    When the user clicks the "Cancel" button
    Then the user should be taken to the edit exchange page
     And the participant list table should be empty
     And the participant list pager should say "0 of 0"

 Scenario: Create a participant and delete it
    When the user clicks the "Add" button
    Then the user should be taken to the edit participant page
    When the user enters participant name "Participant One"
     And the user enters participant nickname "p1"
     And the user enters participant email address "p1@test.com"
     And the user clicks the "Save" button
    Then the user should be taken to the edit exchange page
     And the participant list table should have 1 data row with participant name "Participant One"
     And the participant list pager should say "1-1 of 1"
    When the user clicks the first data row in the participant list table
    Then the user should be taken to the edit participant page
     And the participant name should be "Participant One"
     And the participant nickname should be "p1"
     And the participant email address should be "p1@test.com"
    When the user clicks the "Cancel" button
    Then the user should be taken to the edit exchange page
     And the participant list table should have 1 data row with participant name "Participant One"
     And the participant list pager should say "1-1 of 1"
    When the user clicks the "Remove" button
    Then the participant list table should have 1 data row with participant name "Participant One"
     And the participant list pager should say "1-1 of 1"
    When the user clicks the checkbox in participant list table row 1, column 1
     And the user clicks the "Remove" button
    Then participant list table should be empty
     And the participant list pager should say "0 of 0"

Scenario: Create two participants and delete them with the select all checkbox
    When the user clicks the "Add" button
    Then the user should be taken to the edit participant page
    When the user enters participant name "Participant One"
     And the user enters participant nickname "p1"
     And the user enters participant email address "p1@test.com"
     And the user clicks the "Save" button
    Then the user should be taken to the edit exchange page
     And the participant list table should have 1 data row with participant name "Participant One"
     And the participant list pager should say "1-1 of 1"
    When the user clicks the "Add" button
    Then the user should be taken to the edit participant page
    When the user enters participant name "Participant Two"
     And the user enters participant nickname "p2"
     And the user enters participant email address "p2@test.com"
     And the user clicks the "Save" button
    Then the user should be taken to the edit exchange page
     And the participant list table should have 1 data row with participant name "Participant One"
     And the participant list table should have 1 data row with participant name "Participant Two
     And the participant list pager should say "1-2 of 2"
    When the user clicks the exchange list select all checkbox
     And the user clicks the "Remove" button
    Then participant list table should be empty
     And the participant list pager should say "0 of 0"

Scenario: Create two participants that will stay around
    When the user clicks the "Add" button
    Then the user should be taken to the edit participant page
    When the user enters participant name "Participant One"
     And the user enters participant nickname "p1"
     And the user enters participant email address "p1@test.com"
     And the user clicks the "Save" button
    Then the user should be taken to the edit exchange page
     And the participant list table should have 1 data row with participant name "Participant One"
     And the participant list pager should say "1-1 of 1"
    When the user clicks the "Add" button
    Then the user should be taken to the edit participant page
    When the user enters participant name "Participant Two"
     And the user enters participant nickname "p2"
     And the user enters participant email address "p2@test.com"
     And the user clicks the "Save" button
    Then the user should be taken to the edit exchange page
     And the participant list table should have 1 data row with participant name "Participant One"
     And the participant list table should have 1 data row with participant name "Participant Two"
     And the participant list pager should say "1-2 of 2"

Scenario: Preview the invitations
    When the user clicks the "Preview" button
    Then the "Email Notification Preview" pop-up should be visible
    When the user clicks the "Email Notification Preview" pop-up "Close" button
    Then the "Email Notification Preview" pop-up should be hidden

Scenario: Check part of the send notifications flow (but don't actually send)
    When the user clicks the "Send Invitations" button
    Then the "Are you sure?" pop-up should be visible
    When the user clicks the "Are you sure?" pop-up "No" button
    Then the "Are you sure?" pop-up should be hidden
     And the exchange status should be "Started"
    When the user clicks the "Send Invitations" button
    Then the "Are you sure?" pop-up should be visible
