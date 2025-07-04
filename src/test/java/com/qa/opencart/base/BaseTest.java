package com.qa.opencart.base;

import com.qa.opencart.factory.DriverFactory;
import com.qa.opencart.pages.*;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import java.util.Properties;

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
    @Parameters({"browser"})
    @BeforeTest
    public void setup(@Optional("chrome") String browserName) {
        df = new DriverFactory();
        prop = df.initProp();
        //check if browser parameter is coming from testng.xml
        if (browserName != null) {
            prop.setProperty("browser", browserName);
        }

        driver = df.initDriver(prop);
        loginPage = new LoginPage(driver);
    }

    @Step("Close the browser")
    @AfterTest
    public void tearDown() {
        driver.quit();
    }


}
