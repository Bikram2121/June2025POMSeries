package com.qa.opencart.tests;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Epic 100:design open cart log login page")
@Feature("Feature 101: login feature")
@Story("US 120: All the features related to open cart login page")
@Owner("Vikram")
public class LoginPageTest extends BaseTest {

    @Severity(SeverityLevel.MINOR)
    @Description("login page title test...")
    @Feature("Feature 400: title test feature")
    @Test
    public void loginPageTitleTest() {
        String actTitle = loginPage.getLoginPageTitle();
        Assert.assertEquals(actTitle, AppConstants.LOGIN_PAGE_TITLE);
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("login page url test...")
    @Feature("Feature 401: url test feature")
    @Test()
    public void loginPageURLTest() {
        String actURL = loginPage.getLoginPageURL();
        Assert.assertTrue(actURL.contains(AppConstants.LOGIN_PAGE_FRACTION_URL));
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("login page forgot password link exist test...")
    @Test
    public void forgotPwdLinkExistTest() {
        Assert.assertTrue(loginPage.isForgotPwdLinkExist());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("login page logo test...")
    @Test
    public void logoExistTest() {
        Assert.assertTrue(loginPage.isLogoExist());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("user is able to login...")
    @Test(priority = Integer.MAX_VALUE)
    public void loginTest() {
        accPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
        Assert.assertEquals(accPage.getAccountsPageTitle(), AppConstants.ACCOUNTS_PAGE_TITLE);
    }


}