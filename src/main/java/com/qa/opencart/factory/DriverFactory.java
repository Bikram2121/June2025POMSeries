package com.qa.opencart.factory;

import com.qa.opencart.errors.AppError;
import com.qa.opencart.exceptions.BrowserException;
import com.qa.opencart.exceptions.FrameworkException;
import io.qameta.allure.Step;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Vikram Mohanty
 */
public class DriverFactory {

    WebDriver driver;
    Properties prop;
    public static String isHighlight;
    public static ThreadLocal<WebDriver> tlDrver = new ThreadLocal<WebDriver>();


    /**
     * Thie methos is used to initialize the driver on the basis of given browsername.
     *
     * @param browserName
     * @return driver
     */
    @Step("initializing the driver with properties:{0}")
    public WebDriver initDriver(Properties prop) {
        String browserName = prop.getProperty("browser");
        System.out.println("browser name is : " + browserName);
        isHighlight = prop.getProperty("highlight");
        OptionsManager optionsManager = new OptionsManager(prop);

        switch (browserName.toLowerCase().trim()) {
            case "edge":
                // driver = new EdgeDriver(optionsManager.getEdgeOptions());
                tlDrver.set(new EdgeDriver(optionsManager.getEdgeOptions()));
                break;

            case "chrome":
                //driver = new ChromeDriver(optionsManager.getChromeOptions());
                tlDrver.set(new ChromeDriver(optionsManager.getChromeOptions()));
                break;

            case "firefox":
                //   driver = new FirefoxDriver(optionsManager.getFirefoxOptions());
                tlDrver.set(new FirefoxDriver(optionsManager.getFirefoxOptions()));
                break;

            default:
                System.out.println(AppError.INVALID_BROWSER_MSG + browserName + " is invalid");
                throw new BrowserException(AppError.INVALID_BROWSER_MSG);
        }

        getDriver().manage().window().maximize();
        getDriver().manage().deleteAllCookies();
        getDriver().get(prop.getProperty("url"));

        return getDriver();

    }

    /**
     * This method is returning the driver with thread local.
     *
     * @return WebDriver
     */
    public static WebDriver getDriver() {
        return tlDrver.get();
    }


    /**
     * This method is used initialize the properties from the config file
     *
     * @return
     */
    //mvn clean install -Denv="qa"
    public Properties initProp() {
        prop = new Properties();
        FileInputStream fis;

        String envName = System.getProperty("env");
        System.out.println("Running test on : " + envName);
        try {
            if (envName == null) {
                System.out.println("env is null...running tests on QA env");
                fis = new FileInputStream("./src/test/resources/config/qa.config.properties");
            } else {
                switch (envName.toLowerCase().trim()) {
                    case "qa":
                        fis = new FileInputStream("./src/test/resources/config/qa.config.properties");
                        break;
                    case "dev":
                        fis = new FileInputStream("./src/test/resources/config/dev.config.properties");
                        break;
                    case "stage":
                        fis = new FileInputStream("./src/test/resources/config/stage.config.properties");
                        break;
                    case "uat":
                        fis = new FileInputStream("./src/test/resources/config/uat.config.properties");
                        break;
                    case "prod":
                        fis = new FileInputStream("./src/test/resources/config/config.properties");
                        break;

                    default:
                        System.out.println("please pass the correct env name..." + envName);
                        throw new FrameworkException("INVALID ENV NAME");
                }
            }

            prop.load(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prop;
    }

    /**
     * take screenshot
     */
    public static String getScreenshot(String methodName) {
        File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        String path = System.getProperty("user.dir") + "/screenshot/" + methodName + "-" + java.lang.System.currentTimeMillis() + ".png";
        File destination = new File(path);
        try {
            FileHandler.copy(srcFile, destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }


}
