package dnn.tam.util.dom;

import dnn.tam.util.exception.AmbiguousAnchorElementsException;
import dnn.tam.util.exception.AmbiguousFoundWebElementsException;
import dnn.tam.util.exception.AmbiguousFoundXPathsException;
import dnn.tam.util.exception.NoAnchorElementFoundException;
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

    @Before
    public void init()
    {
        url = "https://accounts.google.com/signup/v2/webcreateaccount?hl=en&flowName=GlifWebSignIn&flowEntry=SignUp";
    }

    @Test
    public void getElementsByTagNameContainingOwnText() throws IOException
    {
        //Arrange
        String resourcePath = getClass().getClassLoader().getResource("google-signup.html").getFile();
        Document document = DomUtil.htmlFileToDocument(resourcePath);

        //Act
        List<Element> elements = DomUtil.getElementsByTagNameContainingOwnText(document, "div", "Username");

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
        Document document = DomUtil.htmlFileToDocument(resourcePath);

        //Act
        List<Element> elements = DomUtil.getElementsByTagNameMatchingOwnText(document, "div", "Username");

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
        Document document = DomUtil.htmlFileToDocument(resourcePath);

        //Act
        List<Element> elements = DomUtil.getElementsMatchingOwnText(document, "Username");

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
        Document document = DomUtil.htmlFileToDocument(resourcePath);

        //Act
        String xpath1 = DomUtil.getXPaths(document, "div", "Username", "input").get(0);
        String xpath2 = DomUtil.getXPaths(document, "Username", "input").get(0);

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
        Document document = DomUtil.htmlFileToDocument(resourcePath);

        //Act
        Element userNameTextBox1 = DomUtil.getClosestElements(document, "div", "Username", "input").get(0);
        Element userNameTextBox2 = DomUtil.getClosestElements(document, "Username", "input").get(0);

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
        Document document = DomUtil.getActiveDocument(driver);
        driver.quit();

        //Act
        String xpath = DomUtil.getXPath(document, "div", "Username", "input");

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
        Document document = DomUtil.getActiveDocument(driver);
        String xpath = DomUtil.getXPath(document, "div", "Username", "input");
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

        //Arrange
        DomUtil.findElement(driver, "First name", "input").sendKeys(uuid);
        DomUtil.findElement(driver, "Last name", "input").sendKeys(uuid);
        DomUtil.findElement(driver, "Username", "input").sendKeys(uuid);
        DomUtil.findElement(driver, "Password", "input").sendKeys(uuid);
        DomUtil.findElement(driver, "Confirm", "input").sendKeys(uuid);
        DomUtil.findElement(driver, "Next", "button").click();

        try
        {
            DomUtil.findElement(driver, "invalid anchor", "input").sendKeys(String.format("%s@google.com", uuid));
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
        WebElement firstName = DomUtil.findElement(driver, "First name", "label");
        Thread.sleep(2000); //open your eyes to see
        driver.quit();

        //Assert
        Assert.assertNull(firstName);
    }
}
