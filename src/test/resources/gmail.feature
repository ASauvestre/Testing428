Feature: Gmail

  Scenario Outline: Normal Flow: Sending an email with a picture
    Given A user is logged in
    When they compose an email to <email>
    And they attach <file>
    Then the email should be found in the sent folder

    Examples:
      | email                           | file  |
      | dibbo.ritwik@mail.mcgill.ca     | 1.jpg |
      | adrien.sauvestre@mail.mcgill.ca | 2.jpg |
      | dibbo.ritwik@gmail.com          | 3.jpg |
      | chester.the.dog904@gmail.com    | 4.jpg |
      | djadeed@gmail.com               | 5.jpg |

  Scenario: Error Flow: Sending an email without a recipient
    Given A user is logged in
    When they don't specify an email address
    And they attach 1.jpg
    Then the email should not be sent


  Scenario: Alternate Flow: Sending an email with a picture
    Given A user is logged in
    When they compose an email to "dibbo.ritwik@mail.mcgill.ca" without a body or subject
    And they attach 5.jpg
    And they handle the pop up to send the email
    Then the email should be found in the sent folder


