package com.qa.opencart.tests;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class AccountsPageTest extends BaseTest {

    @BeforeClass
    public void accSetup() {
        accPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
    }

    @Test
    public void accPageTitleTest() {
        String actTitle = accPage.getAccountsPageTitle();
        Assert.assertEquals(actTitle, AppConstants.ACCOUNTS_PAGE_TITLE);
    }

    @Test
    public void isLogoutLinkExistTest() {
        Assert.assertTrue(accPage.isLogoutLinkExist());
    }

    @Test
    public void accPageHeadersCountTest() {
        Assert.assertEquals(accPage.getTotalAccountsPageHeader(), AppConstants.ACCOUNTS_PAGE_HEADERS_COUNT);
    }

    @Test
    public void accPageHeadersTest() {
        List<String> actualHeadersList = accPage.getAccPageHeaders();
        Assert.assertEquals(actualHeadersList, AppConstants.EXPECTED_ACC_PAGE_HEADERS_LIST);
    }


    @DataProvider
    public Object[][] getSearchKey() {
        return new Object[][]{
                {"macbook", 3},
                {"imac", 1},
                {"samsung", 2},

        };
    }

    @Description("search functionality test")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "getSearchKey")
    public void searchCountTest(String searchKey, int searchCount) {
        resultsPage = accPage.doSearch(searchKey);
        Assert.assertEquals(resultsPage.getSearchResultsCount(), searchCount);
    }


    @DataProvider
    public Object[][] getSearchData() {
        return new Object[][]{
                {"macbook", "MacBook Pro"},
                {"macbook", "MacBook Air"},
                {"imac", "iMac"},
                {"samsung", "Samsung SyncMaster 941BW"},
                {"samsung", "Samsung Galaxy Tab 10.1"}
        };
    }

//    @DataProvider
//    public Object[][] getExcelSearchData() {
//        return ExcelUtil.getTestData(AppConstants.SEARCH_SHEET_NAME);
//    }


//    @Test(dataProvider = "getExcelSearchData")
    @Test(dataProvider = "getSearchData")
    public void searchTest(String searchKey, String productName) {
        resultsPage = accPage.doSearch(searchKey);
        productInfoPage = resultsPage.selectProduct(productName);
        Assert.assertEquals(productInfoPage.getProductHeader(), productName);
    }

}