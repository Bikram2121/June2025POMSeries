package com.qa.opencart.tests;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.utils.CSVUtil;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginPageNegativeTest extends BaseTest {

    @DataProvider
    public Object[][] invalidLoginData() {
        return CSVUtil.csvData("login");
    }

    @Test(dataProvider = "invalidLoginData")
    public void invalidLoginTest(String username, String password) {
        Assert.assertTrue(loginPage.doInvalidLogin(username, password));
    }

}
