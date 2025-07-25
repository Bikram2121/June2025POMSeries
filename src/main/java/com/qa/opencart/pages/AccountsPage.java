package com.qa.opencart.pages;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.utils.ElementUtil;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class AccountsPage {
    private WebDriver driver;
    private ElementUtil eleUtil;

    private By logoutLink = By.linkText("Logout");
    private By headers = By.cssSelector("div#content h2");
    private By search = By.name("search");
    private By searchIcon = By.cssSelector("div#search button");

    public AccountsPage(WebDriver driver) {
        this.driver = driver;
        eleUtil = new ElementUtil(driver);
    }

    public String getAccountsPageTitle() {
        String title = eleUtil.waitForTitleContainsAndReturn(AppConstants.ACCOUNTS_PAGE_TITLE, AppConstants.DEFAULT_SHORT_TIME_OUT);
        System.out.println("Accounts page title: " + title);
        return title;
    }

    public boolean isLogoutLinkExist() {
        return eleUtil.isElementDisplayed(logoutLink);
    }

    public int getTotalAccountsPageHeader() {
        List<WebElement> headerList = eleUtil.waitForElementsVisible(headers, AppConstants.DEFAULT_LONG_TIME_OUT);
        return headerList.size();
    }


    public List<String> getAccPageHeaders() {
        List<WebElement> headersList = eleUtil.waitForElementsVisible(headers, AppConstants.DEFAULT_MEDIUM_TIME_OUT);
        List<String> headersValueList = new ArrayList<String>();
        for (WebElement e : headersList) {
            String header = e.getText();
            headersValueList.add(header);
        }
        return headersValueList;
    }

    public ResultsPage doSearch(String searchKey) {
        System.out.println("Search Key ==>" + searchKey);
        WebElement searchEle = eleUtil.waitForElementVisible(search, AppConstants.DEFAULT_SHORT_TIME_OUT);
        eleUtil.doSendKeys(searchEle, searchKey);
        eleUtil.doClick(searchIcon);
        return new ResultsPage(driver);
    }


}