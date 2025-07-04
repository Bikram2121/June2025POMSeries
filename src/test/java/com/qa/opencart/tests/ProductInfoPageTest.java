package com.qa.opencart.tests;

import com.qa.opencart.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Map;

public class ProductInfoPageTest extends BaseTest {

    @BeforeClass
    public void productInfoSetup() {
        accPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
    }


    @Test
    public void productHeaderTest() {
        resultsPage = accPage.doSearch("macbook");
        productInfoPage = resultsPage.selectProduct("MacBook Pro");
        Assert.assertEquals(productInfoPage.getProductHeader(), "MacBook Pro");
    }

    @DataProvider
    public Object[][] getProductInfoData() {
        return new Object[][]{
                {
                        "macbook", "MacBook Pro", "Brand", "Apple",
                        "Product Code", "Product 18", "Reward Points", "800",
                        "Availability", "Out Of Stock", "productprice", "$2,000.00",
                        "extaxprice", "$2,000.00"
                }
        };
    }


    @Test(dataProvider = "getProductInfoData")
    public void ProductInfoTest(String searchKey, String productName, String brandName, String brand, String productCode,
                                String code, String rewardPoints, String points, String availInfo, String availability,
                                String productPrice, String price, String exTaxPrice, String taxPrice) {

        SoftAssert softAssert = new SoftAssert();
        resultsPage = accPage.doSearch(searchKey);
        productInfoPage = resultsPage.selectProduct(productName);
        Map<String, String> actProductDataMap = productInfoPage.getProductData();

        softAssert.assertEquals(actProductDataMap.get(brandName), brand);
        softAssert.assertEquals(actProductDataMap.get(productCode), code);
        softAssert.assertEquals(actProductDataMap.get(rewardPoints), points);
        softAssert.assertEquals(actProductDataMap.get(availInfo), availability);
        softAssert.assertEquals(actProductDataMap.get(productPrice), price);
        softAssert.assertEquals(actProductDataMap.get(exTaxPrice), taxPrice);
        softAssert.assertAll();
    }


    @DataProvider
    public Object[][] getProductImagesCountData() {
        return new Object[][]{
                {"macbook", "MacBook Pro", 4},
                {"imac", "iMac", 3},
                {"samsung", "Samsung SyncMaster 941BW", 1},
                {"samsung", "Samsung Galaxy Tab 10.1", 7},
                {"canon", "Canon EOS 5D", 3}
        };
    }


    @Test(dataProvider = "getProductImagesCountData")
    public void productImagesCountTest(String searchKey, String productName, int imagesCount) {
        resultsPage = accPage.doSearch(searchKey);
        productInfoPage = resultsPage.selectProduct(productName);
        Assert.assertEquals(productInfoPage.getProductImagesCount(), imagesCount);
    }


}