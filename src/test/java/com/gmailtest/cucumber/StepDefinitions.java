package com.gmailtest.cucumber;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class StepDefinitions {
    private WebDriver driver;

    private final String PATH_TO_WEBDRIVER = "C:\\chromedriver.exe";
    private final String SIGNIN_PAGE_URL = "https://accounts.google.com/signin/v2/identifier?continue=https%3A%2F%2Fmail.google.com%2Fmail%2F&service=mail&sacu=1&rip=1&flowName=GlifWebSignIn&flowEntry=ServiceLogin";
    private final String MAIN_PAGE_URL = "https://mail.google.com/mail/#inbox";
    private final String SIGNIN_PAGE_USERNAME_ID = "identifierId";
    private final String SIGNIN_PAGE_USERNAME_NEXT_ID = "identifierNext";
    private final String SIGNIN_PAGE_PASSWORD_NAME = "password";
    private final String SIGNIN_PAGE_PASSWORD_NEXT_ID = "passwordNext";
    private final String USER_MAIL = "practicetesting4@gmail.com";
    private final String USER_PASSWORD = "ecse428-";
    private final String TO_XPATH= "//*[@aria-label=\"To\"]";
    private final String COMPOSE_XPATH="//*[@aria-label=\"Navigate to\"]/../../../div[1]/div/div/div/div/div/div";
    private final String SUBJECT_XPATH="//*[@aria-label=\"Subject\"]";
    private final String SENT_XPATH="//*[@aria-label=\"Sent\"]/../../..";
    private final String ERROR_TEXT_= "Please specify at least one recipient.";

    private String subject_text;
    private String filename;

    @Given("the user is logged in")
    public void iAmLoggedIn() {
        setupSeleniumWebDrivers();
        goTo(SIGNIN_PAGE_URL);

        // Fill up user field and press the next button
        WebElement user_field = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id(SIGNIN_PAGE_USERNAME_ID)));
        user_field.sendKeys(USER_MAIL);
        WebElement next_button = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id(SIGNIN_PAGE_USERNAME_NEXT_ID)));
        next_button.click();

        // Fill up password field and press the next button
        WebElement password_field = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.name(SIGNIN_PAGE_PASSWORD_NAME)));
        password_field.sendKeys(USER_PASSWORD);

        WebElement second_next_button = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id(SIGNIN_PAGE_PASSWORD_NEXT_ID)));
        second_next_button.click();

        // Wait until we are redirected to the home page
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.urlToBe(MAIN_PAGE_URL));
    }

    @And("they compose an email to ([^\"]*) with attachment ([^\"]*)")
    public void iComposeAnEmailTo(String email_address, String filename) {
        generateString();

        // Click the compose button to show the message box
        WebElement compose_button = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(COMPOSE_XPATH)));
        compose_button.click();

        // Put a recipient
        WebElement to = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(TO_XPATH)));
        to.sendKeys(email_address);

        // Put a subject
        WebElement subject = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(SUBJECT_XPATH)));
        subject.sendKeys(subject_text);

        // Attach picture
        this.filename = filename;
        String basePath = new File("").getAbsolutePath();
        driver.findElement(By.name("Filedata")).sendKeys(basePath + "/src/test/resources/" + this.filename);

        // Send message
        WebElement send_button = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='Send']")));
        send_button.click();
    }

    @When("they don't specify an email address but attach ([^\"]*)")
    public void theyDonTSpecifyAnEmailAddress(String filename) {

        // Click the compose button to show the message box
        WebElement compose_button = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(COMPOSE_XPATH)));
        compose_button.click();

        // Attach picture
        this.filename = filename;
        String basePath = new File("").getAbsolutePath();
        driver.findElement(By.name("Filedata")).sendKeys(basePath + "/src/test/resources/" + this.filename);


        // Send message
        WebElement send_button = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='Send']")));
        send_button.click();
    }

    @When("they compose an email without a body or subject to ([^\"]*) with attachment ([^\"]*)")
    public void iComposeAnEmailToWithNoSubjectOrBody(String email_address, String filename) {

        // Click the compose button to show the message box
        WebElement compose_button = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(COMPOSE_XPATH)));
        compose_button.click();

        // Put a recipient
        WebElement to = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(TO_XPATH)));
        to.sendKeys(email_address);

        this.subject_text = "subject"; // Match for "(no subject)" which is the default subject.

        // Attach picture
        this.filename = filename;
        String basePath = new File("").getAbsolutePath();
        driver.findElement(By.name("Filedata")).sendKeys(basePath + "/src/test/resources/" + this.filename);

        // Send message
        WebElement send_button = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='Send']")));
        send_button.click();

        // Click no subject and body popup
        driver.switchTo().alert().accept();
    }

    @Then("the email should be found in the sent folder")
    public void theEmailShouldBeSent() {

        // Move to sent messages page
        WebElement sent_button = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(SENT_XPATH)));
        sent_button.click();

        // Look for the message we just sent by subjects and attachment name
        if (driver.findElement(By.xpath("//div[text()='" + this.filename + "']")) != null) {
            if (driver.findElement(By.xpath("//span[contains(.,'" + this.subject_text + "')]")) != null) {
                System.out.println("Email sent.");
            }
        }

        signOut();
    }

    @Then("the email should not be sent")
    public void theEmailShouldNotBeSent() {

        // Look for error in-page alert
        if (driver.findElement(By.xpath("//div[text()='" + ERROR_TEXT_ + "']")) != null) {
            System.out.println("Email not sent.");

            // Close popup
            WebElement okay_button = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.name("ok")));
            okay_button.click();
        }

        // Wait until popup disappears
        (new WebDriverWait(driver, 1)).until(ExpectedConditions.invisibilityOfElementLocated(By.className("Kj-JD-Jh")));

        // Sign out
        signOut();
    }


    // Helper functions
    private void signOut() {

        // Open Account menu
        WebElement SignOut = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@aria-label,'Google Account: Testing Practice')]")));
        SignOut.click();

        // Click "Sign out"
        WebElement SignOut_button = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.linkText("Sign out")));
        SignOut_button.click();

        // Accept sign out alert if it is there
        try {
            (new WebDriverWait(driver, 1)).until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
        } catch (Exception e) {

        }

        // Close Chrome
        driver.quit();
    }

    // Generate random subject string
    private void generateString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 12) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }

        subject_text = salt.toString();
    }

    // Setup the webdriver so we can start using it.
    private void setupSeleniumWebDrivers() {
        if (driver == null) {
            System.setProperty("webdriver.chrome.driver", PATH_TO_WEBDRIVER);
            driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        }
    }

    // Go to a specific url
    private void goTo(String url) {
        if (driver != null) {
            driver.get(url);
        }
    }
}
