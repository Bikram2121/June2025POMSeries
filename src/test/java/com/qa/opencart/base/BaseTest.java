package com.qa.opencart.base;

import com.aventstack.chaintest.plugins.ChainTestListener;
import com.qa.opencart.factory.DriverFactory;
import com.qa.opencart.pages.*;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.Properties;

//@Listeners(ChainTestListener.class)
public class BaseTest {

    WebDriver driver;
    DriverFactory df;
    protected Properties prop;

    protected LoginPage loginPage;
    protected AccountsPage accPage;
    protected ResultsPage resultsPage;
    protected ProductInfoPage productInfoPage;
    protected RegisterPage registerPage;

    @Step("Setup with browser:{0} and init the properties")
    @Parameters({"browser","browserversion","testname"})
    @BeforeTest
    public void setup(@Optional("chrome") String browserName,String browserVersion,String testName) {
        df = new DriverFactory();
        prop = df.initProp();
        //check if browser parameter is coming from testng.xml
        if (browserName != null) {
            prop.setProperty("browser", browserName);
            prop.setProperty("browserversion", browserVersion);
            prop.setProperty("testname", testName);
        }

        driver = df.initDriver(prop);
        loginPage = new LoginPage(driver);
    }


    @AfterMethod //will be running after each @test method
    public void attachScreenshot(ITestResult result) {
        if (!result.isSuccess()) { // only for failure test cases
            ChainTestListener.embed(DriverFactory.getScreenshotFile(), "image/png");
        }
    }


    @Step("Close the browser")
    @AfterTest
    public void tearDown() {
        driver.quit();
    }


}
