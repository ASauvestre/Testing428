Feature: Gmail

#  Scenario Outline: Normal Flow: Sending an email with a picture
#    Given I am logged in
#    And I am on the Gmail main page
#    When I press Compose
#    And I compose an email to <email>
#    And I attach <file>
#    And I press Send
#    Then the email should be sent
#
#    Examples:
#      | email                           | file  |
#      | dibbo.ritwik@mail.mcgill.ca     | 1.jpg |
#      | adrien.sauvestre@mail.mcgill.ca | 2.jpg |
#      | dibbo.ritwik@gmail.com          | 3.jpg |
#      | chester.the.dog904@gmail.com    | 4.jpg |
#      | djadeed@gmail.com               | 5.jpg |


  Scenario: Error Flow: Sending an email without a recipient
    Given I am logged in
    And I am on the Gmail main page
    When I press Compose
    And I attach 1.jpg
    And I press Send
    Then the email should not be sent


  Scenario: Alternate Flow: Sending an email with a picture
    Given I am logged in
    And I am on the Gmail main page
    When I press Compose
    And I compose an email to "dibbo.ritwik@mail.mcgill.ca" with no subject or body
    And I attach 5.jpg
    And I press Send
    And I click the no subject popup
    Then the email should be sent


