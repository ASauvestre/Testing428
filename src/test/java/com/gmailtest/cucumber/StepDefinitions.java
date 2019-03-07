package com.gmailtest.cucumber;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
    private final String COMPOSE_URL= "https://mail.google.com/mail/u/0/#inbox?compose=new";
    private final String COMPOSE_XPATH="//*[@aria-label=\"Navigate to\"]/../../../div[1]/div/div/div/div/div/div";
    private final String SUBJECT_XPATH="//*[@aria-label=\"Subject\"]";
    private final String ATTACH_XPATH="//*[@aria-label=\"Attach files\"]/div/div/div";
    private final String SEND_KEY="//*[contains(@aria-label,'Send')]";

    @Given("I am logged in")
    public void iAmLoggedIn() {
        setupSeleniumWebDrivers();
        goTo(SIGNIN_PAGE_URL);
        WebElement user_field = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id(SIGNIN_PAGE_USERNAME_ID)));
        user_field.sendKeys(USER_MAIL);
        WebElement next_button = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id(SIGNIN_PAGE_USERNAME_NEXT_ID)));
        next_button.click();

        WebElement password_field = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.name(SIGNIN_PAGE_PASSWORD_NAME)));
        password_field.sendKeys(USER_PASSWORD);

        WebElement second_next_button = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id(SIGNIN_PAGE_PASSWORD_NEXT_ID)));
        second_next_button.click();

        (new WebDriverWait(driver, 10)).until(ExpectedConditions.urlToBe(MAIN_PAGE_URL));
    }

    @And("I am on the Gmail main page")
    public void iAmOnTheGmailMainPage() {
        setupSeleniumWebDrivers();
        goTo(MAIN_PAGE_URL);
    }

    @When("I press {string}")
    public void iPress(String arg0) {
        setupSeleniumWebDrivers();
        WebElement compose_button = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(COMPOSE_XPATH)));
        compose_button.click();
    }

    //doesn't work
    @And("I compose an email to {string}")
    public void iComposeAnEmailTo(String arg0) {
        setupSeleniumWebDrivers();
        System.out.println("before subject");
        WebElement random = (new WebDriverWait(driver, 20)).until(ExpectedConditions.elementToBeClickable(By.xpath(SUBJECT_XPATH)));
        random.sendKeys(arg0);
       // System.out.println("before to");







    }

    @And("I attach a picture")
    public void iAttachAPicture() {
        driver.findElement(By.name("Filedata")).sendKeys("C:\\Users\\dibbo\\OneDrive\\Desktop\\GGMU.JPEG");


    }

    //trying to make it work
    @Then("the email should be sent")
    public void theEmailShouldBeSent() throws InterruptedException {
        System.out.println("before sleep");
        TimeUnit.SECONDS.sleep(20);
        System.out.println("after sleep");
        WebElement send = (new WebDriverWait(driver, 20)).until(ExpectedConditions.elementToBeClickable(By.xpath(SEND_KEY)));
        send.click();
    }

    // Helper functions
    private void setupSeleniumWebDrivers() {
        if (driver == null) {
            System.out.println("Setting up ChromeDriver... ");
            System.setProperty("webdriver.chrome.driver", PATH_TO_WEBDRIVER);
            driver = new ChromeDriver();
            System.out.print("Done!\n");
        }
    }

    private void goTo(String url) {
        if (driver != null) {
            System.out.println("Going to " + url);
            driver.get(url);
        }
    }


}
