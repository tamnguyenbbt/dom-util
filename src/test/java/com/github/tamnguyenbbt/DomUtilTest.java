package com.github.tamnguyenbbt;

import com.github.tamnguyenbbt.dom.DomUtil;
import com.github.tamnguyenbbt.dom.ElementInfo;
import com.github.tamnguyenbbt.exception.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class DomUtilTest
{
    private String url;
    private DomUtil domUtil;

    @Before
    public void init()
    {
        url = "https://accounts.google.com/signup/v2/webcreateaccount?hl=en&flowName=GlifWebSignIn&flowEntry=SignUp";
        domUtil = new DomUtil();
    }

    @Test
    public void getElementsByTagNameContainingOwnText() throws IOException
    {
        //Arrange
        String resourcePath = getClass().getClassLoader().getResource("google-signup.html").getFile();
        Document document = domUtil.htmlFileToDocument(resourcePath);

        //Act
        List<Element> elements = domUtil.getElementsByTagNameContainingOwnText(document, "div", "Username");

        //Assert
        Assert.assertTrue(elements != null);
        Assert.assertTrue(elements.size()==1);
        Assert.assertTrue(elements.get(0).ownText().contains("Username"));
    }

    @Test
    public void getElementsByTagNameMatchingOwnText() throws IOException
    {
        //Arrange
        String resourcePath = getClass().getClassLoader().getResource("google-signup.html").getFile();
        Document document = domUtil.htmlFileToDocument(resourcePath);

        //Act
        List<Element> elements = domUtil.getElementsByTagNameMatchingOwnText(document, "div", "Username");

        //Assert
        Assert.assertTrue(elements != null);
        Assert.assertTrue(elements.size()==1);
        Assert.assertTrue(elements.get(0).ownText().contains("Username"));
    }

    @Test
    public void getElementsMatchingOwnText() throws IOException
    {
        //Arrange
        String resourcePath = getClass().getClassLoader().getResource("google-signup.html").getFile();
        Document document = domUtil.htmlFileToDocument(resourcePath);

        //Act
        List<Element> elements = domUtil.getElementsMatchingOwnText(document, "Username");

        //Assert
        Assert.assertTrue(elements != null);
        Assert.assertTrue(elements.size()==1);
        Assert.assertTrue(elements.get(0).ownText().contains("Username"));
    }

    @Test
    public void getXPaths() throws IOException, NoAnchorElementFoundException, AmbiguousAnchorElementsException
    {
        //Arrange
        String expectedXPath = "//div[div[contains(text(),'Username')]]/input";
        String resourcePath = getClass().getClassLoader().getResource("google-signup.html").getFile();
        Document document = domUtil.htmlFileToDocument(resourcePath);

        //Act
        String xpath1 = domUtil.getXPaths(document, "div", "Username", "input").get(0);
        String xpath2 = domUtil.getXPaths(document, "Username", "input").get(0);

        //Assert
        Assert.assertEquals(expectedXPath, xpath1);
        Assert.assertEquals(expectedXPath, xpath2);
    }

    @Test
    public void getXPaths_with_AnchorElementInfo() throws AnchorIndexIfMultipleFoundOutOfBoundException, IOException, NoAnchorElementFoundException, AmbiguousAnchorElementsException
    {
        //Arrange
        String expectedXPath = "//div[div[contains(text(),'Username')]]/input";
        String resourcePath = getClass().getClassLoader().getResource("google-signup.html").getFile();
        Document document = domUtil.htmlFileToDocument(resourcePath);

        //Act
        ElementInfo anchorElementInfo = new ElementInfo();
        anchorElementInfo.ownText = "userna";
        anchorElementInfo.tagName = "div";
        anchorElementInfo.indexIfMultipleFound = 0;
        anchorElementInfo.whereIgnoreCaseForOwnText = true;
        anchorElementInfo.whereOwnTextContainingPattern = true;
        String xpath1 = domUtil.getXPaths(document, anchorElementInfo, "input").get(0);
        String xpath2 = domUtil.getXPaths(document, "Username", "input").get(0);

        //Assert
        Assert.assertEquals(expectedXPath, xpath1);
        Assert.assertEquals(expectedXPath, xpath2);
    }

    @Test
    public void getClosestElementsFromAnchorElement() throws IOException, NoAnchorElementFoundException, AmbiguousAnchorElementsException
    {
        //Arrange
        String expectedJSNameValue = "YPqjbf";
        String resourcePath = getClass().getClassLoader().getResource("google-signup.html").getFile();
        Document document = domUtil.htmlFileToDocument(resourcePath);

        //Act
        Element userNameTextBox1 = domUtil.getClosestElements(document, "div", "Username", "input").get(0);
        Element userNameTextBox2 = domUtil.getClosestElements(document, "Username", "input").get(0);

        //Assert
        Assert.assertEquals(expectedJSNameValue, userNameTextBox1.attr("jsname"));
        Assert.assertEquals(expectedJSNameValue, userNameTextBox2.attr("jsname"));
    }

    @Test
    public void getXPath() throws NoAnchorElementFoundException, AmbiguousAnchorElementsException, AmbiguousFoundXPathsException
    {
        //Arrange
        String expectedXPath = "//div[div[contains(text(),'Username')]]/input";
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get(url);
        Document document = domUtil.getActiveDocument(driver);
        driver.quit();

        //Act
        String xpath = domUtil.getXPath(document, "div", "Username", "input");

        //Assert
        Assert.assertEquals(expectedXPath, xpath);
    }

    @Test
    public void getXPath_self() throws NoAnchorElementFoundException, AmbiguousAnchorElementsException, AmbiguousFoundXPathsException, IOException
    {
        //Arrange
        String expectedXPath = "//button[contains(text(),'Next')]";
        String resourcePath = getClass().getClassLoader().getResource("google-signup.html").getFile();
        Document document = domUtil.htmlFileToDocument(resourcePath);

        //Act
        String xpath = domUtil.getXPath(document, "button", "Next", "button");

        //Assert
        Assert.assertEquals(expectedXPath, xpath);
    }

    @Test
    public void sampleSeleniumTest_using_getXpath()
            throws NoAnchorElementFoundException, AmbiguousAnchorElementsException, AmbiguousFoundXPathsException, InterruptedException
    {
        //Arrange
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get(url);

        //Act
        Document document = domUtil.getActiveDocument(driver);
        String xpath = domUtil.getXPath(document, "div", "Username", "input");
        WebElement userName = driver.findElement(By.xpath(xpath));
        userName.sendKeys("Happy Testing!");
        Thread.sleep(2000); //open your eyes to see
        driver.quit();
    }

    @Test
    public void sampleSeleniumTest_using_findElement_NoAnchorElementFoundException()
            throws NoAnchorElementFoundException, AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException, InterruptedException
    {
        //Arrange
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get(url);
        String uuid = UUID.randomUUID().toString().substring(0, 20);

        //Act
        domUtil.findWebElement(driver, "First name", "input").sendKeys(uuid);
        domUtil.findWebElement(driver, "Last name", "input").sendKeys(uuid);
        domUtil.findWebElement(driver, "Username", "input").sendKeys(uuid);
        domUtil.findWebElement(driver, "Password", "input").sendKeys(uuid);
        domUtil.findWebElement(driver, "Confirm", "input").sendKeys(uuid);
        domUtil.findWebElement(driver, "Next", "span").click();

        try
        {
            domUtil.findWebElement(driver, "invalid anchor", "input").sendKeys(String.format("%s@google.com", uuid));
        }
        catch(NoAnchorElementFoundException e)
        {
            Thread.sleep(2000); //open your eyes to see
            driver.quit();

            //Assert
            Assert.assertEquals("No anchor element found", e.getMessage());
        }
    }

    @Test
    public void sampleSeleniumTest_using_findElement_no_element_found()
            throws NoAnchorElementFoundException, AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException, InterruptedException
    {
        //Arrange
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get(url);

        //Act
        WebElement firstName = domUtil.findWebElement(driver, "First name", "label");
        Thread.sleep(2000); //open your eyes to see
        driver.quit();

        //Assert
        Assert.assertNull(firstName);
    }
}
