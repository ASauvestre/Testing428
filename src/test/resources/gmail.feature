Feature: Gmail

  Scenario: Sending an email with a picture
    Given I am logged in
    And I am on the Gmail main page
    When I press "Compose"
    And I compose an email to "adrien.sauvestre@mail.mcgill.ca"
    And I attach a picture
    And I press "Send"
    Then the email should be sent


