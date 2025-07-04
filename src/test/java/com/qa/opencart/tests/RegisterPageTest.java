package com.qa.opencart.tests;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.utils.ExcelUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RegisterPageTest extends BaseTest {

    @BeforeClass
    public void regSetup() {
        registerPage = loginPage.navigateToRegisterPage();

    }


    public String getRandomEmail() {
        return "uiautomation" + System.currentTimeMillis() + "@open.com";
    }

//    @DataProvider
//    public Object[][] getRegisterData() {
//        return new Object[][]{
//                {"ui", "automation", getRandomEmail(),"834957893457", "uiauto28198", "Yes"}
//              {"mr.", "ui", "automation", "uisadassasd@gmasl.com", "uiauto28198", "20", "February", "2014"},
//                {"mrs.", "ui", "automation", "isdoasdadd@gmail.com", "uiauto28198", "27", "June", "2018"},
//
//                // use beforemethod for multiple registration
//        };
//    }

    @DataProvider
    public Object[][] getRegistrationData() {
        return ExcelUtil.getTestData(AppConstants.REG_SHEET_NAME);
    }


    //    @Test(dataProvider = "getRegisterData")
    @Test(dataProvider = "getRegistrationData")
    public void userRegisterTest(String firstname, String lastname, String telephone, String password, String subscribe) {
        Assert.assertTrue(registerPage.userRegisteration(firstname, lastname, getRandomEmail(), telephone, password, subscribe));

    }

}