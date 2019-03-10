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

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

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
    private final String TO_XPATH= "//*[@aria-label=\"To\"]";
    private final String COMPOSE_XPATH="//*[@aria-label=\"Navigate to\"]/../../../div[1]/div/div/div/div/div/div";
    private final String SUBJECT_XPATH="//*[@aria-label=\"Subject\"]";
    private final String ATTACH_XPATH="//*[@aria-label=\"Attach files\"]/div/div/div";
    private final String SEND_KEY="//*[contains(@aria-label,'Send ')]";
    private final String IMAGE_NAME= "GGMU.JPEG";
    private final String SENT_XPATH="//*[@aria-label=\"Sent\"]/../../..";
    public String subject_text;

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
        generateString();
        setupSeleniumWebDrivers();
        WebElement to = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(TO_XPATH)));
        System.out.println("before subject");
        to.sendKeys(arg0);
        WebElement subject = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(SUBJECT_XPATH)));
        subject.sendKeys(subject_text);
        System.out.println("before to");

    }

    @And("I attach a picture")
    public void iAttachAPicture() throws InterruptedException, AWTException {
        driver.findElement(By.name("Filedata")).sendKeys("C:\\Users\\dibbo\\OneDrive\\Desktop\\GGMU.JPEG");
        System.out.println("before sleep");
        Robot robot= new Robot();
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_ENTER);




    }

    //trying to make it work
    @Then("the email should be sent")
    public void theEmailShouldBeSent() throws InterruptedException, AWTException {
        WebElement sent_button = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(SENT_XPATH)));
        sent_button.click();


        if ( driver.findElement(By.xpath("//div[text()='GGMU.JPEG']")) != null) {
            if( driver.findElement(By.xpath("//div[text()='" + subject_text + "']")) != null){
                System.out.println("Email was sent successfully!");
                WebElement mail = null;
                while(mail == null) {
                    WebDriverWait wait = new WebDriverWait(driver, 10);
                    mail = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@email=\"adrien.sauvestre@mail.mcgill.ca\"]/../..")));
                }
                mail.click();
            }

        }
        else {
            System.out.println("Email failed to send!");
        }

        WebElement inbox = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(@aria-label,'Inbox')]")));
        inbox.click();
        WebElement SignOut = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(@aria-label,'Google Account: Garbage Practice')]")));
        SignOut.click();
        WebElement SignOut_button = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("gb_71")));
        SignOut_button.click();

        driver.switchTo().alert().accept();


    }


    // Helper functions

    public void generateString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 12) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        subject_text = salt.toString();

    }
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
