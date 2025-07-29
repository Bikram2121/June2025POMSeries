package com.qa.opencart.factory;

import com.qa.opencart.errors.AppError;
import com.qa.opencart.exceptions.BrowserException;
import com.qa.opencart.exceptions.FrameworkException;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

/**
 * @author Vikram Mohanty
 */
public class DriverFactory {

    private static final Logger log = LogManager.getLogger(DriverFactory.class);
    WebDriver driver;
    Properties prop;
    public static String isHighlight;
    OptionsManager optionsManager;
    public static ThreadLocal<WebDriver> tlDrver = new ThreadLocal<WebDriver>();


    /**
     * Thie methos is used to initialize the driver on the basis of given browsername.
     *
     * @param browserName
     * @return driver
     */
    @Step("initializing the driver with properties:{0}")
    public WebDriver initDriver(Properties prop) {
        log.info("properties : " + prop);
        String browserName = prop.getProperty("browser");
        log.info("browser name : " + browserName);
        isHighlight = prop.getProperty("highlight");
        optionsManager = new OptionsManager(prop);

        switch (browserName.toLowerCase().trim()) {
            case "edge":
                if (Boolean.parseBoolean(prop.getProperty("remote"))) {
                    //run test cases on remote / container:
                    init_remoteDriver("edge");
                } else {
                    //run test cases in local
                    tlDrver.set(new EdgeDriver(optionsManager.getEdgeOptions()));
                }
                break;

            case "chrome":
                if (Boolean.parseBoolean(prop.getProperty("remote"))) {
                    //run test cases on remote / container:
                    init_remoteDriver("chrome");
                } else {
                    //run test cases in local
                    tlDrver.set(new ChromeDriver(optionsManager.getChromeOptions()));
                }
                break;

            case "firefox":
                if (Boolean.parseBoolean(prop.getProperty("remote"))) {
                    //run test cases on remote / container:
                    init_remoteDriver("firefox");
                } else {
                    //run test cases in local
                    tlDrver.set(new FirefoxDriver(optionsManager.getFirefoxOptions()));
                }
                break;

            default:
                log.error(AppError.INVALID_BROWSER_MSG + browserName + " is invalid");
                throw new BrowserException(AppError.INVALID_BROWSER_MSG);
        }

        getDriver().manage().window().maximize();
        getDriver().manage().deleteAllCookies();
        getDriver().get(prop.getProperty("url"));

        return getDriver();

    }

    private void init_remoteDriver(String browserName) {
        log.info("running tests on grid with browser : " + browserName);
        try {
            switch (browserName.toLowerCase().trim()) {
                case "chrome":
                    tlDrver.set(new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getChromeOptions()));
                    break;

                case "firefox":
                    tlDrver.set(new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getFirefoxOptions()));

                case "edge":
                    tlDrver.set(new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getEdgeOptions()));

                default:
                    log.error("please pass the right remote browser name....");
                    throw new BrowserException(AppError.INVALID_BROWSER_MSG);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
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
        log.info("Running test on : " + envName);
        try {
            if (envName == null) {
                log.warn("env is null...running tests on QA env");
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
                        log.error("please pass the correct env name..." + envName);
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
     * takes screenshot
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

    public static File getScreenshotFile() {
        File srcfile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        return srcfile;
    }

    public static byte[] getScreenshotByte() {
        return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
    }

    public static String getScreenshotBse64() {
        return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BASE64);
    }


}
