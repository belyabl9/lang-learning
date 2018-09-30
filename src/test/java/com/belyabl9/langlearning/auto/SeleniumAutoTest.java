package com.belyabl9.langlearning.auto;

import com.belyabl9.langlearning.TestConfiguration;
import com.belyabl9.langlearning.config.MainConfiguration;
import com.belyabl9.langlearning.domain.User;
import com.belyabl9.langlearning.service.InternalUserService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PostConstruct;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

//@RunWith(SpringJUnit4ClassRunner.class)
//@DataJpaTest
//@ContextConfiguration(classes = TestConfiguration.class)
public class SeleniumAutoTest {

    private WebDriver webDriver;
    
    @BeforeClass
    public void init() {
//        ProfilesIni profile = new ProfilesIni();
//        
//        FirefoxProfile ffProfile = profile.getProfile("QAAutomation");
//        ffProfile.setAcceptUntrustedCertificates(true);
//        ffProfile.setAssumeUntrustedCertificateIssuer(false);

        System.setProperty("webdriver.chrome.driver", "<path>");
        
//        webDriver = new FirefoxDriver(ffProfile);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");

        webDriver = new ChromeDriver(options);
    }

    public void waitForPageLoaded() {
        ExpectedCondition<Boolean> expectation = driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
        try {
            Thread.sleep(1000);
            WebDriverWait wait = new WebDriverWait(webDriver, 30);
            wait.until(expectation);
        } catch (Throwable error) {
            Assert.fail("Timeout waiting for Page Load Request to complete.");
        }
    }
    
//    @Test
    public void registerUser() throws Exception {
        webDriver.get("https://localhost:8443/registration");
        
        webDriver.findElement(By.id("name")).sendKeys("Name Surname");
        webDriver.findElement(By.id("email")).sendKeys("qa@test.com");
        webDriver.findElement(By.id("login")).sendKeys("qalogin");
        webDriver.findElement(By.id("password")).sendKeys("qapassword");

        webDriver.findElement(By.id("registerBtn")).click();
        
        Assert.assertThat(webDriver.getTitle(), is(equalTo("Login form")));
    }

//    @Test
    public void signIn() throws Exception {
        webDriver.get("https://localhost:8443");
        WebElement loginInput = webDriver.findElement(By.id("login"));
        loginInput.sendKeys("qalogin");

        WebElement passwordInput = webDriver.findElement(By.id("password"));
        passwordInput.sendKeys("qapassword");

        WebElement loginBtn = webDriver.findElement(By.id("loginBtn"));
        loginBtn.click();

        Assert.assertThat(webDriver.getTitle(), is(equalTo("Introduction")));
    }

//    @Test
    public void addCategory() throws Exception {
        signIn();
        
        webDriver.get("https://localhost:8443");

        Thread.sleep(1000);
        
        webDriver.findElement(By.id("categoriesLink")).click();

        webDriver.findElement(By.id("addCategoryBtn")).click();

        Thread.sleep(1000);
        
        webDriver.findElement(By.id("categoryNameToAdd")).sendKeys("Category");
        webDriver.findElement(By.id("addCategoryForm_addBtn")).click();

        List<WebElement> categoryCells = webDriver.findElements(By.cssSelector(".categoryCell a"));
        boolean categoryFound = categoryCells.stream().anyMatch(webElement -> webElement.getText().equals("Category"));

        Assert.assertThat(categoryFound, is(equalTo(true)));
    }
}