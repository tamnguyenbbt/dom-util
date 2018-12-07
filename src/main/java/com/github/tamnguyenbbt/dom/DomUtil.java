package com.github.tamnguyenbbt.dom;

import com.github.tamnguyenbbt.exception.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

/**
 * <h1> Html DOM Utility </h1>
 *
 * Generate xpath and find jsoup element based on the closest element (anchor element) information.
 * Useful for back-end web service resource testing and web front end Selenium-based testing
 *
 * @author: Tam Nguyen - tamnguyennb@gmail.com - https://www.linkedin.com/in/tam-nguyen-a0792930
 * @version: 1.0
 * @since: 2018-09-30
 */
public class DomUtil
{
    private boolean returnErrorOnMultipleAnchorsFound;
    private boolean returnErrorOnMultipleWebElementsFound;

    public void returnErrorOnMultipleWebElementsFound(boolean returnErrorOnMultipleWebElementsFound)
    {
       this.returnErrorOnMultipleWebElementsFound = returnErrorOnMultipleWebElementsFound;
    }

    public void returnErrorOnMultipleAnchorsFound(boolean returnErrorOnMultipleAnchorsFound)
    {
        this.returnErrorOnMultipleAnchorsFound = returnErrorOnMultipleAnchorsFound;
    }

    /**
     * get jsoup document from a browser managed by Selenium WebDriver
     *
     * @param driver Selenium WebDriver
     * @return jsoup document. Null if driver is null
     */
    public Document getActiveDocument(WebDriver driver)
    {
        if(driver == null)
        {
            return null;
        }

        WebElement htmlElement = driver.findElement(By.xpath("//html"));
        String htmlContent = htmlElement.getAttribute("innerHTML");
        return getDocument(htmlContent);
    }

    /**
     * get jsoup document from a file. jsoup parsing file with UTF-8 charset
     *
     * @param path path to html file
     * @return jsoup document. Null if the file not exist
     * @exception IOException
     */
    public Document htmlFileToDocument(String path) throws IOException
    {
        return htmlFileToDocument(path, "UTF-8");
    }

    /**
     * get jsoup document from a file
     *
     * @param path path to html file
     * @param charsetName charset name used by jsoup parsing
     * @return jsoup document. Null if the file not exist
     * @exception IOException
     */
    public Document htmlFileToDocument(String path, String charsetName) throws IOException
    {
        File file = new File(path);
        Document document = null;

        if(file.exists() && file.isFile())
        {
            document = Jsoup.parse(file, charsetName);
        }

        return document;
    }

    /**
     * get jsoup document from html content
     *
     * @param htmlContent html content string
     * @return jsoup document
     */
    public Document getDocument(String htmlContent)
    {
        return Jsoup.parse(htmlContent);
    }

    public String findXpathWithTwoAnchors(WebDriver driver,
                                                 String parentAnchorElementOwnText,
                                                 String anchorElementOwnText,
                                                 String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXPathsException
    {
        return findXpathWithTwoAnchors(driver, parentAnchorElementOwnText, null, anchorElementOwnText, searchCssQuery);
    }

    public String findXpathWithTwoAnchors(WebDriver driver,
                                                 String parentAnchorElementOwnText,
                                                 String anchorElementTagName,
                                                 String anchorElementOwnText,
                                                 String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXPathsException
    {
        return findXpathWithTwoAnchors(driver, null, parentAnchorElementOwnText, anchorElementTagName, anchorElementOwnText, searchCssQuery);
    }

    public String findXpathWithTwoAnchors(WebDriver driver,
                                                 String parentAnchorElementTagName,
                                                 String parentAnchorElementOwnText,
                                                 String anchorElementTagName,
                                                 String anchorElementOwnText,
                                                 String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXPathsException
    {
        String xpath = findXpathWithTwoAnchorsExactMatch(driver,
                                                         parentAnchorElementTagName,
                                                         parentAnchorElementOwnText,
                                                         anchorElementTagName,
                                                         anchorElementOwnText,
                                                         searchCssQuery);

        if (xpath == null)
        {
            ElementInfo parentAnchorElementInfo = new ElementInfo(parentAnchorElementTagName,
                                                                  parentAnchorElementOwnText, true);
            ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText, true);
            xpath = findXpathWithTwoAnchorsExactMatch(driver, parentAnchorElementInfo, anchorElementInfo,
                                                      searchCssQuery);
        }

        return xpath;
    }

    public String findXpathWithTwoAnchorsExactMatch(WebDriver driver,
                                                           String parentAnchorElementOwnText,
                                                           String anchorElementOwnText,
                                                           String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXPathsException
    {
        return findXpathWithTwoAnchorsExactMatch(driver,
                                                 parentAnchorElementOwnText,
                                                 null,
                                                 anchorElementOwnText,
                                                 searchCssQuery);
    }

    public String findXpathWithTwoAnchorsExactMatch(WebDriver driver,
                                                           String parentAnchorElementOwnText,
                                                           String anchorElementTagName,
                                                           String anchorElementOwnText,
                                                           String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXPathsException
    {
        return findXpathWithTwoAnchorsExactMatch(driver,
                                                 null,
                                                 parentAnchorElementOwnText,
                                                 anchorElementTagName,
                                                 anchorElementOwnText,
                                                 searchCssQuery);
    }

    public String findXpathWithTwoAnchorsExactMatch(WebDriver driver,
                                                           String parentAnchorElementTagName,
                                                           String parentAnchorElementOwnText,
                                                           String anchorElementTagName,
                                                           String anchorElementOwnText,
                                                           String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXPathsException
    {
        return findXpathWithTwoAnchorsExactMatch(driver,
                                                 new ElementInfo(parentAnchorElementTagName, parentAnchorElementOwnText),
                                                 new ElementInfo(anchorElementTagName, anchorElementOwnText),
                                                 searchCssQuery);
    }

    public String findXpathWithTwoAnchorsExactMatch(WebDriver driver,
                                                    ElementInfo parentAnchorElementInfo,
                                                    ElementInfo anchorElementInfo,
                                                    String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXPathsException
    {
        Document document = getActiveDocument(driver);
        Elements anchorElementsByLink = getElements(document, parentAnchorElementInfo, anchorElementInfo, SearchMethod.ByLink);

        if(hasItem(anchorElementsByLink))
        {
            return findXpath(driver, anchorElementsByLink, searchCssQuery);
        }
        else
        {
            Elements elementsByLink = getElements(document, parentAnchorElementInfo, new ElementInfo(searchCssQuery), SearchMethod.ByLink);

            if(hasItem(elementsByLink))
            {
                Elements anchorElements = toElements(getElementsByTagNameMatchingOwnText(
                    document,
                    anchorElementInfo.tagName,
                    anchorElementInfo.ownText,
                    !anchorElementInfo.whereIgnoreCaseForOwnText,
                    !anchorElementInfo.whereOwnTextContainingPattern,
                    !anchorElementInfo.whereIncludingTabsAndSpacesForOwnText));

                List<Element> filteredAnchors = getClosestElements(elementsByLink, anchorElements);
                return findXpath(driver, toElements(filteredAnchors), searchCssQuery);
            }
            else
            {
                Elements anchorElementsByShortestDistance = getElements(document, parentAnchorElementInfo, anchorElementInfo, SearchMethod.ByDistance);
                return findXpath(driver, anchorElementsByShortestDistance, searchCssQuery);
            }
        }
    }

    public WebElement findWebElementWithTwoAnchors(WebDriver driver,
                                                          String parentAnchorElementOwnText,
                                                          String anchorElementOwnText,
                                                          String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return findWebElementWithTwoAnchors(driver,
                                            parentAnchorElementOwnText,
                                            null,
                                            anchorElementOwnText,
                                            searchCssQuery);
    }

    public WebElement findWebElementWithTwoAnchors(WebDriver driver,
                                                          String parentAnchorElementOwnText,
                                                          String anchorElementTagName,
                                                          String anchorElementOwnText,
                                                          String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return findWebElementWithTwoAnchors(driver,
                                            null,
                                            parentAnchorElementOwnText,
                                            anchorElementTagName,
                                            anchorElementOwnText,
                                            searchCssQuery);
    }

    public WebElement findWebElementWithTwoAnchors(WebDriver driver,
                                                          String parentAnchorElementTagName,
                                                          String parentAnchorElementOwnText,
                                                          String anchorElementTagName,
                                                          String anchorElementOwnText,
                                                          String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        WebElement webElement = findWebElementWithTwoAnchorsExactMatch(driver,
                                                                       parentAnchorElementTagName,
                                                                       parentAnchorElementOwnText,
                                                                       anchorElementTagName,
                                                                       anchorElementOwnText,
                                                                       searchCssQuery);

        if(webElement == null)
        {
            ElementInfo parentAnchorElementInfo = new ElementInfo(parentAnchorElementTagName, parentAnchorElementOwnText, true);
            ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText, true);
            webElement = findWebElementWithTwoAnchorsExactMatch(driver, parentAnchorElementInfo, anchorElementInfo, searchCssQuery);
        }

        return webElement;
    }

    public WebElement findWebElementWithTwoAnchorsExactMatch(WebDriver driver,
                                                                    String parentAnchorElementOwnText,
                                                                    String anchorElementOwnText,
                                                                    String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return findWebElementWithTwoAnchorsExactMatch(driver,
                                                      parentAnchorElementOwnText,
                                                      null,
                                                      anchorElementOwnText,
                                                      searchCssQuery);
    }

    public WebElement findWebElementWithTwoAnchorsExactMatch(WebDriver driver,
                                                                    String parentAnchorElementOwnText,
                                                                    String anchorElementTagName,
                                                                    String anchorElementOwnText,
                                                                    String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return findWebElementWithTwoAnchorsExactMatch(driver,
                                                      null,
                                                      parentAnchorElementOwnText,
                                                      anchorElementTagName,
                                                      anchorElementOwnText,
                                                      searchCssQuery);
    }

    public WebElement findWebElementWithTwoAnchorsExactMatch(WebDriver driver,
                                                                    String parentAnchorElementTagName,
                                                                    String parentAnchorElementOwnText,
                                                                    String anchorElementTagName,
                                                                    String anchorElementOwnText,
                                                                    String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return findWebElementWithTwoAnchorsExactMatch(driver,
                                                      new ElementInfo(parentAnchorElementTagName, parentAnchorElementOwnText),
                                                      new ElementInfo(anchorElementTagName, anchorElementOwnText),
                                                      searchCssQuery);
    }

    public WebElement findWebElementWithTwoAnchorsExactMatch(WebDriver driver,
                                                             ElementInfo parentAnchorElementInfo,
                                                             ElementInfo anchorElementInfo,
                                                             String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        Document document = getActiveDocument(driver);
        Elements anchorElementsByLink = getElements(document, parentAnchorElementInfo, anchorElementInfo, SearchMethod.ByLink);

        if(hasItem(anchorElementsByLink))
        {
            return findWebElement(driver, document, anchorElementsByLink, searchCssQuery);
        }
        else
        {
            Elements elementsByLink = getElements(document, parentAnchorElementInfo, new ElementInfo(searchCssQuery), SearchMethod.ByLink);

            if(hasItem(elementsByLink))
            {
                Elements anchorElements = toElements(getElementsByTagNameMatchingOwnText(
                    document,
                    anchorElementInfo.tagName,
                    anchorElementInfo.ownText,
                    !anchorElementInfo.whereIgnoreCaseForOwnText,
                    !anchorElementInfo.whereOwnTextContainingPattern,
                    !anchorElementInfo.whereIncludingTabsAndSpacesForOwnText));

                List<Element> filteredAnchors = getClosestElements(elementsByLink, anchorElements);
                return findWebElement(driver, document, toElements(filteredAnchors), searchCssQuery);
            }
            else
            {
                Elements anchorElementsByShortestDistance = getElements(document, parentAnchorElementInfo, anchorElementInfo, SearchMethod.ByDistance);
                return findWebElement(driver, document, anchorElementsByShortestDistance, searchCssQuery);
            }
        }
    }

    public WebElement findWebElement(WebDriver driver,
                                            String anchorElementOwnText,
                                            String searchCssQuery)
        throws NoAnchorElementFoundException, AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return findWebElement(driver, null, anchorElementOwnText, searchCssQuery);
    }

    public WebElement findWebElementHandlingPossibleMultipleAnchorsFound(WebDriver driver,
                                                                         String anchorElementOwnText,
                                                                         String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return findWebElementHandlingPossibleMultipleAnchorsFound(driver, null, anchorElementOwnText, searchCssQuery);
    }

    public WebElement findWebElementHandlingPossibleMultipleAnchorsFound(WebDriver driver,
                                                                         String anchorElementTagName,
                                                                         String anchorElementOwnText,
                                                                         String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText);
        Document document = getActiveDocument(driver);
        WebElement webElement = findWebElement(driver, document, anchorElementInfo, searchCssQuery);

        if(webElement == null)
        {
            anchorElementInfo.whereOwnTextContainingPattern = true;
            webElement = findWebElement(driver, document, anchorElementInfo, searchCssQuery);
        }

        return webElement;
    }

    public WebElement findWebElement(WebDriver driver,
                                            String anchorElementTagName,
                                            String anchorElementOwnText,
                                            String searchCssQuery)
        throws NoAnchorElementFoundException, AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        WebElement webElement = findWebElementExactMatch(driver, anchorElementTagName, anchorElementOwnText, searchCssQuery);

        if(webElement == null)
        {
            try
            {
                webElement = findWebElementExactMatch(driver, new ElementInfo(anchorElementTagName, anchorElementOwnText, true), searchCssQuery);
            }
            catch(AnchorIndexIfMultipleFoundOutOfBoundException e){}
        }

        return webElement;
    }

    /**
     * get Selenium WebElement from a browser managed by Selenium WebDriver
     *
     * @param driver Selenium WebDriver
     * @param anchorElementOwnText own text of an html anchor tag, excluding spaces and tabs.
     *                             The anchor element is an unique html element closest to the web element to search.
     *                             The anchor element should be easy to be located on a web page with unique text such as a label
     * @param searchCssQuery css query of the element to search
     * @return Selenium WebElement based on the minimal DOM distance from possible found elements to anchor element.
     *         Null if no element found by the provided searchCssQuery
     * @exception NoAnchorElementFoundException when no anchor element is found by @anchorElementOwnText
     * @exception AmbiguousAnchorElementsException when more than one anchor elements are found by @anchorElementOwnText
     * @exception AmbiguousFoundWebElementsException when more than one search elements are found by @searchCssQuery.
     *                                               This occurs when the found elements having the same DOM distances to the anchor element
     */
    public WebElement findWebElementExactMatch(
            WebDriver driver,
            String anchorElementOwnText,
            String searchCssQuery)
            throws NoAnchorElementFoundException, AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return findWebElementExactMatch(driver, null, anchorElementOwnText, searchCssQuery);
    }

    /**
     * get Selenium WebElement from a browser managed by Selenium WebDriver
     *
     * @param driver Selenium WebDriver
     * @param anchorElementTagName html anchor tag
     * @param anchorElementOwnText own text of the html anchor tag, excluding spaces and tabs.
     *                             The anchor element is an unique html element closest to the web element to search.
     *                             The anchor element should be easy to be located on a web page with unique text such as a label
     * @param searchCssQuery css query of the element to search
     * @return Selenium WebElement based on the minimal DOM distance from possible found element to anchor element.
     *         Null if no element found by the provided searchCssQuery
     * @exception NoAnchorElementFoundException when no anchor element is found by @anchorElementOwnText
     * @exception AmbiguousAnchorElementsException when more than one anchor elements are found by @anchorElementOwnText
     * @exception AmbiguousFoundWebElementsException when more than one search elements are found by @searchCssQuery.
     *                                               This occurs when the found elements having the same DOM distances to the anchor element
     */
    public WebElement findWebElementExactMatch(
            WebDriver driver,
            String anchorElementTagName,
            String anchorElementOwnText,
            String searchCssQuery)
            throws NoAnchorElementFoundException, AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        try
        {
            return findWebElementExactMatch(driver, new ElementInfo(anchorElementTagName, anchorElementOwnText),searchCssQuery);
        }
        catch(AnchorIndexIfMultipleFoundOutOfBoundException e){}

        return null;
    }

    /**
     * get Selenium WebElement from a browser managed by Selenium WebDriver
     *
     * @param driver Selenium WebDriver
     * @param anchorElementInfo information about anchor element.
     *                          The anchor element is an unique html element closest to the web element to search.
     *                          The anchor element should be easy to be located on a web page with unique text such as a label
     * @param searchCssQuery css query of the element to search
     * @return Selenium WebElement based on the minimal DOM distance from possible found element to anchor element.
     *         Null if no element found by the provided searchCssQuery
     * @exception AnchorIndexIfMultipleFoundOutOfBoundException when the indexIfMultipleFound property of @anchorElementInfo is out of bound
     * @exception NoAnchorElementFoundException when no anchor element is found by @anchorElementOwnText
     * @exception AmbiguousAnchorElementsException when more than one anchor elements are found by @anchorElementOwnText
     * @exception AmbiguousFoundWebElementsException when more than one search elements are found by @searchCssQuery.
     *                                               This occurs when the found elements having the same DOM distances to the anchor element
     */
    public WebElement findWebElementExactMatch(
            WebDriver driver,
            ElementInfo anchorElementInfo,
            String searchCssQuery)
            throws AnchorIndexIfMultipleFoundOutOfBoundException, NoAnchorElementFoundException, AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        String xpath;

        try
        {
            xpath = findXpathExactMatch(driver, anchorElementInfo, searchCssQuery);
        }
        catch(AmbiguousFoundXPathsException e)
        {
            throw new AmbiguousFoundWebElementsException("More than one web element found");
        }

        return xpath == null ? null : findWebElement(driver, xpath);
    }

    public List<String> getIndexedXpaths(WebDriver driver, String xpath)
    {
        List<String> result = new ArrayList<>();

        if(xpath == null)
        {
            return result;
        }

        List<WebElement> foundWebElements = driver.findElements(By.xpath(xpath));

        if(hasItem(foundWebElements))
        {
            int numberOfIndexedXpaths = foundWebElements.size();

            if(numberOfIndexedXpaths == 1)
            {
                result.add(xpath);
            }
            else
            {
                for (int i = 0; i < numberOfIndexedXpaths; i++)
                {
                    String indexedXpath = String.format("%s[%s]", xpath, i);
                    result.add(indexedXpath);
                }
            }
        }

        return result;
    }

    public String findXpath(WebDriver driver,
                                   String anchorElementOwnText,
                                   String searchCssQuery)
        throws NoAnchorElementFoundException, AmbiguousAnchorElementsException, AmbiguousFoundXPathsException
    {
        return findXpath(driver, null, anchorElementOwnText, searchCssQuery);
    }

    public String findXpath(WebDriver driver,
                                   String anchorElementTagName,
                                   String anchorElementOwnText,
                                   String searchCssQuery)
        throws NoAnchorElementFoundException, AmbiguousAnchorElementsException, AmbiguousFoundXPathsException
    {
        String xpath = findXpathExactMatch(driver, anchorElementTagName, anchorElementOwnText, searchCssQuery);

        if(xpath == null)
        {
            try
            {
                xpath = findXpathExactMatch(driver, new ElementInfo(anchorElementTagName, anchorElementOwnText, true), searchCssQuery);
            }
            catch(AnchorIndexIfMultipleFoundOutOfBoundException e){}
        }

        return xpath;
    }

    public String findXpathExactMatch(
        WebDriver driver,
        String anchorElementOwnText,
        String searchCssQuery)
        throws NoAnchorElementFoundException, AmbiguousAnchorElementsException, AmbiguousFoundXPathsException
    {
        return findXpathExactMatch(driver, null, anchorElementOwnText, searchCssQuery);
    }

    public String findXpathExactMatch(
        WebDriver driver,
        String anchorElementTagName,
        String anchorElementOwnText,
        String searchCssQuery)
        throws NoAnchorElementFoundException, AmbiguousAnchorElementsException, AmbiguousFoundXPathsException
    {
        try
        {
            return findXpathExactMatch(driver, new ElementInfo(anchorElementTagName, anchorElementOwnText),searchCssQuery);
        }
        catch(AnchorIndexIfMultipleFoundOutOfBoundException e){}

        return null;
    }

    public String findXpathExactMatch(
        WebDriver driver,
        ElementInfo anchorElementInfo,
        String searchCssQuery)
        throws AnchorIndexIfMultipleFoundOutOfBoundException, NoAnchorElementFoundException, AmbiguousAnchorElementsException, AmbiguousFoundXPathsException
    {
        Document document = getActiveDocument(driver);

        if(document == null)
        {
            return null;
        }

        return getXPath(document, anchorElementInfo, searchCssQuery);
    }

    /**
     * get xpath of the element in jsoup document closest to anchor element
     *
     * @param document jsoup Document
     * @param anchorElementOwnText own text of a html anchor tag, excluding spaces and tabs.
     *                             The anchor element is an unique html element closest to the web element to search.
     *                             The anchor element should be easy to be located on a web page with unique text such as a label
     * @param searchCssQuery css query of the element to search
     * @return xpath string based on the minimal DOM distance from possible found element to anchor element.
     *         Null if no xpath found by the provided searchCssQuery
     * @exception NoAnchorElementFoundException when no anchor element is found by @anchorElementOwnText
     * @exception AmbiguousAnchorElementsException when more than one anchor elements are found by @anchorElementOwnText
     * @exception AmbiguousFoundXPathsException when more than one search xpaths are found by @searchCssQuery.
     *                                               This occurs when the found elements having the same DOM distances to the anchor element
     */
    public String getXPath(
            Document document,
            String anchorElementOwnText,
            String searchCssQuery)
            throws NoAnchorElementFoundException, AmbiguousAnchorElementsException, AmbiguousFoundXPathsException
    {
        return getXPath(document, null, anchorElementOwnText, searchCssQuery);
    }

    /**
     * get xpath of the element in jsoup document closest to anchor element
     *
     * @param document jsoup Document
     * @param anchorElementTagName html anchor tag
     * @param anchorElementOwnText own text of the html anchor tag, excluding spaces and tabs.
     *                             The anchor element is an unique html element closest to the web element to search.
     *                             The anchor element should be easy to be located on a web page with unique text such as a label
     * @param searchCssQuery css query of the element to search
     * @return xpath string based on the minimal DOM distance from possible found element to anchor element.
     *         Null if no xpath found by the provided searchCssQuery
     * @exception NoAnchorElementFoundException when no anchor element is found by @anchorElementOwnText
     * @exception AmbiguousAnchorElementsException when more than one anchor elements are found by @anchorElementOwnText
     * @exception AmbiguousFoundXPathsException when more than one search xpaths are found by @searchCssQuery.
     *                                               This occurs when the found elements having the same DOM distances to the anchor element
     */
    public String getXPath(
            Document document,
            String anchorElementTagName,
            String anchorElementOwnText,
            String searchCssQuery)
            throws NoAnchorElementFoundException, AmbiguousAnchorElementsException, AmbiguousFoundXPathsException
    {
        List<String> xpaths = getXPaths(document, anchorElementTagName, anchorElementOwnText, searchCssQuery);

        if(xpaths.size() > 1)
        {
            throw new AmbiguousFoundXPathsException("More than one xpaths found");
        }

        return hasItem(xpaths) ? xpaths.get(0) : null;
    }

    /**
     * get xpath of the element in jsoup document closest to anchor element
     *
     * @param document jsoup Document
     * @param anchorElementInfo information about anchor element.
     *                          The anchor element is an unique html element closest to the web element to search.
     *                          The anchor element should be easy to be located on a web page with unique text such as a label
     * @param searchCssQuery css query of the element to search
     * @return xpath string based on the minimal DOM distance from possible found element to anchor element.
     *         Null if no xpath found by the provided searchCssQuery
     * @exception AnchorIndexIfMultipleFoundOutOfBoundException when the indexIfMultipleFound property of @anchorElementInfo is out of bound
     * @exception NoAnchorElementFoundException when no anchor element is found by @anchorElementOwnText
     * @exception AmbiguousAnchorElementsException when more than one anchor elements are found by @anchorElementOwnText
     * @exception AmbiguousFoundXPathsException when more than one search xpaths are found by @searchCssQuery.
     *                                               This occurs when the found elements having the same DOM distances to the anchor element
     */
    public String getXPath(
            Document document,
            ElementInfo anchorElementInfo,
            String searchCssQuery)
            throws AnchorIndexIfMultipleFoundOutOfBoundException, NoAnchorElementFoundException, AmbiguousAnchorElementsException, AmbiguousFoundXPathsException
    {
        List<String> xpaths = getXPaths(document, anchorElementInfo, searchCssQuery);

        if(xpaths.size() > 1)
        {
            throw new AmbiguousFoundXPathsException("More than one xpaths found");
        }

        return hasItem(xpaths) ? xpaths.get(0) : null;
    }

    /**
     * get xpath of the element in jsoup document closest to anchor element
     *
     * @param anchorElement unique jsoup element in DOM document.
     *                      The anchor element should be easy to be located on a web page with unique text such as a label
     * @param searchElements possible jsoup element candidates to search from
     * @return xpath string based on the minimal DOM distance from possible found element to anchor element.
     *         Null if no xpath found
     * @exception AmbiguousFoundXPathsException when more than one search xpaths are found.
     *                                          This occurs when the found elements having the same DOM distances to the anchor element
     */
    public String getXPath(Element anchorElement, Elements searchElements)
            throws AmbiguousFoundXPathsException
    {
        List<String> xpaths = getXPaths(anchorElement, searchElements);

        if(xpaths.size() > 1)
        {
            throw new AmbiguousFoundXPathsException("More than one xpaths found");
        }

        return hasItem(xpaths) ? xpaths.get(0) : null;
    }

    /**
     * get xpath list of the elements in jsoup document closest to anchor element
     *
     * @param document jsoup Document
     * @param anchorElementOwnText own text of a html anchor tag, excluding spaces and tabs.
     *                             The anchor element is an unique html element closest to the web element to search.
     *                             The anchor element should be easy to be located on a web page with unique text such as a label
     * @param searchCssQuery css query of the element to search
     * @return xpath list based on the minimal DOM distance from possible found elements to anchor element.
     *         When there are more than one elements having the same minimal DOM distance to the anchor element, their xpaths are returned.
     *         Empty list if no xpath found by the provided searchCssQuery
     * @exception NoAnchorElementFoundException when no anchor element is found by @anchorElementOwnText
     * @exception AmbiguousAnchorElementsException when more than one anchor elements are found by @anchorElementOwnText
     */
    public List<String> getXPaths(
            Document document,
            String anchorElementOwnText,
            String searchCssQuery)
            throws NoAnchorElementFoundException, AmbiguousAnchorElementsException
    {
        return getXPaths(document, null, anchorElementOwnText, searchCssQuery);
    }

    /**
     * get xpath list of the elements in jsoup document closest to anchor element
     *
     * @param document jsoup Document
     * @param anchorElementInfo information about the anchor element.
     *                          The anchor element is an unique html element closest to the web element to search.
     *                          The anchor element should be easy to be located on a web page with unique text such as a label
     * @param searchCssQuery css query of the element to search
     * @return xpath list based on the minimal DOM distance from possible found elements to anchor element.
     *         When there are more than one elements having the same minimal DOM distance to the anchor element, their xpaths are returned.
     *         Empty list if no xpath found by the provided searchCssQuery
     * @exception AnchorIndexIfMultipleFoundOutOfBoundException when the indexIfMultipleFound property of @anchorElementInfo is out of bound
     * @exception NoAnchorElementFoundException when no anchor element is found by @anchorElementOwnText
     * @exception AmbiguousAnchorElementsException when more than one anchor elements are found by @anchorElementOwnText
     */
    public List<String> getXPaths(
            Document document,
            ElementInfo anchorElementInfo,
            String searchCssQuery)
            throws AnchorIndexIfMultipleFoundOutOfBoundException, NoAnchorElementFoundException, AmbiguousAnchorElementsException
    {
        List<Element> anchorElements = getElements(document, anchorElementInfo);
        List<Element> activeAnchorElements = getActiveAnchorElements(anchorElementInfo, anchorElements);
        return getXPaths(document, activeAnchorElements, searchCssQuery);
    }

    /**
     * get xpath list of the elements in jsoup document closest to anchor element
     *
     * @param document jsoup Document
     * @param anchorElementTagName html anchor tag
     * @param anchorElementOwnText own text of the html anchor tag, excluding spaces and tabs.
     *                             The anchor element is an unique html element closest to the web element to search.
     *                             The anchor element should be easy to be located on a web page with unique text such as a label
     * @param searchCssQuery css query of the element to search
     * @return xpath list based on the minimal DOM distance from possible found elements to anchor element.
     *         When there are more than one elements having the same minimal DOM distance to the anchor element, their xpaths are returned.
     *         Empty list if no xpath found by the provided searchCssQuery
     * @exception NoAnchorElementFoundException when no anchor element is found by @anchorElementOwnText
     * @exception AmbiguousAnchorElementsException when more than one anchor elements are found by @anchorElementOwnText
     */
    public List<String> getXPaths(
            Document document,
            String anchorElementTagName,
            String anchorElementOwnText,
            String searchCssQuery)
            throws NoAnchorElementFoundException, AmbiguousAnchorElementsException
    {
        List<Element> anchorElements = getElements(document, anchorElementTagName, anchorElementOwnText);
        return getXPaths(document, anchorElements, searchCssQuery);
    }

    /**
     * get xpath list of the elements closest to anchor element
     *
     * @param anchorElement unique jsoup element in DOM document.
     *                      The anchor element should be easy to be located on a web page with unique text such as a label
     * @param searchElements possible jsoup element candidates to search from
     * @return xpath list based on the minimal DOM distance from possible found elements to anchor element.
     *         When there are more than one elements having the same minimal DOM distance to the anchor element, their xpaths are returned.
     *         Empty list if no xpath found
     */
    public List<String> getXPaths(Element anchorElement, Elements searchElements)
    {
        List<String> xpathList = new ArrayList<>();
        List<ElementRecord> finalFoundRecords =
            getElementRecordsByLinkAndShortestDistance(anchorElement, searchElements);

        if(hasNoItem(finalFoundRecords))
        {
            return xpathList;
        }

        for(ElementRecord record : finalFoundRecords)
        {
            String xpath = buildXpath(record, anchorElement);

            if(xpath != null)
            {
                xpathList.add(xpath);
            }
        }

        return new ArrayList<>(new HashSet<>(xpathList));
    }

    /**
     * get jsoup element from jsoup document closest to anchor element
     *
     * @param document jsoup Document
     * @param anchorElementOwnText own text of a html anchor tag, excluding spaces and tabs.
     *                             The anchor element is an unique html element closest to the web element to search.
     *                             The anchor element should be easy to be located on a web page with unique text such as a label
     * @param searchCssQuery css query of the element to search
     * @return jsoup based on the minimal DOM distance from possible found element to anchor element.
     *         Null if no element found by the provided searchCssQuery
     * @exception NoAnchorElementFoundException when no anchor element is found by @anchorElementOwnText
     * @exception AmbiguousAnchorElementsException when more than one anchor elements are found by @anchorElementOwnText
     * @exception AmbiguousFoundElementsException when more than one search elements are found by @searchCssQuery.
     *                                               This occurs when the found elements having the same DOM distances to the anchor element
     */
    public Element getClosestElement(
            Document document,
            String anchorElementOwnText,
            String searchCssQuery)
            throws NoAnchorElementFoundException, AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getClosestElement(document, null, anchorElementOwnText, searchCssQuery);
    }

    /**
     * get jsoup element from jsoup document closest to anchor element
     *
     * @param document jsoup Document
     * @param anchorElementTagName html anchor tag
     * @param anchorElementOwnText own text of the html anchor tag, excluding spaces and tabs.
     *                             The anchor element is an unique html element closest to the web element to search.
     *                             The anchor element should be easy to be located on a web page with unique text such as a label
     * @param searchCssQuery css query of the element to search
     * @return jsoup based on the minimal DOM distance from possible found element to anchor element.
     *         Null if no element found by the provided searchCssQuery
     * @exception NoAnchorElementFoundException when no anchor element is found by @anchorElementOwnText
     * @exception AmbiguousAnchorElementsException when more than one anchor elements are found by @anchorElementOwnText
     * @exception AmbiguousFoundElementsException when more than one search elements are found by @searchCssQuery.
     *                                               This occurs when the found elements having the same DOM distances to the anchor element
     */
    public Element getClosestElement(
            Document document,
            String anchorElementTagName,
            String anchorElementOwnText,
            String searchCssQuery)
            throws NoAnchorElementFoundException, AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        List<Element> foundElement = getClosestElements(document, anchorElementTagName, anchorElementOwnText, searchCssQuery);

        if(foundElement.size() > 1)
        {
            throw new AmbiguousFoundElementsException("More than one elements found");
        }

        return hasItem(foundElement) ? foundElement.get(0) : null;
    }

    /**
     * get jsoup element from jsoup document closest to anchor element
     *
     * @param document jsoup Document
     * @param anchorElementInfo information about the anchor element.
     *                          The anchor element is an unique html element closest to the web element to search.
     *                          The anchor element should be easy to be located on a web page with unique text such as a label
     * @param searchCssQuery css query of the element to search
     * @return jsoup based on the minimal DOM distance from possible found element to anchor element.
     *         Null if no element found by the provided searchCssQuery
     * @exception AnchorIndexIfMultipleFoundOutOfBoundException when the indexIfMultipleFound property of @anchorElementInfo is out of bound
     * @exception NoAnchorElementFoundException when no anchor element is found by @anchorElementOwnText
     * @exception AmbiguousAnchorElementsException when more than one anchor elements are found by @anchorElementOwnText
     * @exception AmbiguousFoundElementsException when more than one search elements are found by @searchCssQuery.
     *                                               This occurs when the found elements having the same DOM distances to the anchor element
     */
    public Element getClosestElement(
            Document document,
            ElementInfo anchorElementInfo,
            String searchCssQuery)
            throws AnchorIndexIfMultipleFoundOutOfBoundException, NoAnchorElementFoundException, AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        List<Element> foundElement = getClosestElements(document, anchorElementInfo, searchCssQuery);

        if(foundElement.size() > 1)
        {
            throw new AmbiguousFoundElementsException("More than one elements found");
        }

        return hasItem(foundElement) ? foundElement.get(0) : null;
    }

    /**
     * get jsoup element closest to anchor element
     *
     * @param anchorElement unique jsoup element in DOM document.
     *                      The anchor element should be easy to be located on a web page with unique text such as a label
     * @param searchElements possible jsoup element candidates to search from
     * @return jsoup element based on the minimal DOM distance from possible found element to anchor element.
     *         Null if no element found
     */
    public Element getClosestElement(Element anchorElement, Elements searchElements)
            throws AmbiguousFoundElementsException
    {
        List<Element> foundElement = getClosestElements(anchorElement, searchElements);

        if(foundElement.size() > 1)
        {
            throw new AmbiguousFoundElementsException("More than one elements found");
        }

        return hasItem(foundElement) ? foundElement.get(0) : null;
    }

    /**
     * get jsoup element list from jsoup document closest to anchor element
     *
     * @param document jsoup Document
     * @param anchorElementOwnText own text of a html anchor tag, excluding spaces and tabs.
     *                             The anchor element is an unique html element closest to the web element to search.
     *                             The anchor element should be easy to be located on a web page with unique text such as a label
     * @param searchCssQuery css query of the element to search
     * @return jsoup element list based on the minimal DOM distance from possible found elements to anchor element.
     *         When there are more than one elements having the same minimal DOM distance to the anchor element, they are returned.
     *         Empty list if no element found by the provided searchCssQuery
     * @exception NoAnchorElementFoundException when no anchor element is found by @anchorElementOwnText
     * @exception AmbiguousAnchorElementsException when more than one anchor elements are found by @anchorElementOwnText
     */
    public List<Element> getClosestElements(
            Document document,
            String anchorElementOwnText,
            String searchCssQuery)
            throws NoAnchorElementFoundException, AmbiguousAnchorElementsException
    {
        return getClosestElements(document, null, anchorElementOwnText, searchCssQuery);
    }

    /**
     * get jsoup element list from jsoup document closest to anchor element
     *
     * @param document jsoup Document
     * @param anchorElementTagName html anchor tag
     * @param anchorElementOwnText own text of the html anchor tag, excluding spaces and tabs.
     *                             The anchor element is an unique html element closest to the web element to search.
     *                             The anchor element should be easy to be located on a web page with unique text such as a label
     * @param searchCssQuery css query of the element to search
     * @return jsoup element list based on the minimal DOM distance from possible found elements to anchor element.
     *         When there are more than one elements having the same minimal DOM distance to the anchor element, they are returned.
     *         Empty list if no element found by the provided searchCssQuery
     * @exception NoAnchorElementFoundException when no anchor element is found by @anchorElementOwnText
     * @exception AmbiguousAnchorElementsException when more than one anchor elements are found by @anchorElementOwnText
     */
    public List<Element> getClosestElements(
            Document document,
            String anchorElementTagName,
            String anchorElementOwnText,
            String searchCssQuery)
            throws NoAnchorElementFoundException, AmbiguousAnchorElementsException
    {
        List<Element> anchorElements = getElements(document, anchorElementTagName, anchorElementOwnText);
        return getClosestElements(document, anchorElements, searchCssQuery);
    }

    /**
     * get jsoup element list from jsoup document closest to anchor element
     *
     * @param document jsoup Document
     * @param anchorElementInfo information about the anchor element.
     *                          The anchor element is an unique html element closest to the web element to search.
     *                          The anchor element should be easy to be located on a web page with unique text such as a label
     * @param searchCssQuery css query of the element to search
     * @return jsoup element list based on the minimal DOM distance from possible found elements to anchor element.
     *         When there are more than one elements having the same minimal DOM distance to the anchor element, they are returned.
     *         Empty list if no element found by the provided searchCssQuery
     * @exception AnchorIndexIfMultipleFoundOutOfBoundException when the indexIfMultipleFound property of @anchorElementInfo is out of bound
     * @exception NoAnchorElementFoundException when no anchor element is found by @anchorElementOwnText
     * @exception AmbiguousAnchorElementsException when more than one anchor elements are found by @anchorElementOwnText
     */
    public List<Element> getClosestElements(
            Document document,
            ElementInfo anchorElementInfo,
            String searchCssQuery)
            throws AnchorIndexIfMultipleFoundOutOfBoundException, NoAnchorElementFoundException, AmbiguousAnchorElementsException
    {
        List<Element> anchorElements = getElements(document, anchorElementInfo);
        List<Element> activeAnchorElements = getActiveAnchorElements(anchorElementInfo, anchorElements);
        return getClosestElements(document, activeAnchorElements, searchCssQuery);
    }

    /**
     * get jsoup element list closest to anchor element
     *
     * @param anchorElement unique jsoup element in DOM document.
     *                      The anchor element should be easy to be located on a web page with unique text such as a label
     * @param searchElements possible jsoup element candidates to search from
     * @return jsoup element list based on the minimal DOM distance from possible found elements to anchor element.
     *         When there are more than one elements having the same minimal DOM distance to the anchor element, they are returned.
     *         Empty list if no element found
     */
    public List<Element> getClosestElements(Element anchorElement, Elements searchElements)
    {
        return getElements(anchorElement, searchElements, SearchMethod.ByLinkAndDistance);
    }

    /**
     * get jsoup element list from jsoup document by tag name where the tag own text contains a pattern with case insensitive
     *
     * @param document DOM document
     * @param tagName tag name, case insensitive
     * @param pattern a string which the tag own text should contain, case insensitive. Spaces and tabs are ignored
     * @return jsoup element list
     */
    public List<Element> getElementsByTagNameContainingOwnTextIgnoreCase(Document document, String tagName, String pattern)
    {
        return getElementsByTagNameMatchingOwnText(document, tagName, pattern, false, false, true);
    }

    /**
     * get jsoup element list from jsoup document by tag name where the tag own text contains a pattern with case sensitive
     *
     * @param document DOM document
     * @param tagName tag name, case insensitive
     * @param pattern a string which the tag own text should contain, case sensitive. Spaces and tabs are ignored
     * @return jsoup element list
     */
    public List<Element> getElementsByTagNameContainingOwnText(Document document, String tagName, String pattern)
    {
        return getElementsByTagNameMatchingOwnText(document, tagName, pattern, true, false, true);
    }

    /**
     * get jsoup element list from jsoup document by tag name where the tag own text matches a pattern with case insensitive
     *
     * @param document DOM document
     * @param tagName tag name, case insensitive
     * @param pattern a string which the tag own text should match, case insensitive. Spaces and tabs are ignored
     * @return jsoup element list
     */
    public List<Element> getElementsByTagNameMatchingOwnTextIgnoreCase(Document document, String tagName, String pattern)
    {
        return getElementsByTagNameMatchingOwnText(document, tagName, pattern, false, true, true);
    }

    /**
     * get jsoup element list from jsoup document by tag name where the tag own text matches a pattern with case sensitive
     *
     * @param document DOM document
     * @param tagName tag name, case insensitive
     * @param pattern a string which the tag own text should match, case sensitive. Spaces and tabs are ignored
     * @return jsoup element list
     */
    public List<Element> getElementsByTagNameMatchingOwnText(Document document, String tagName, String pattern)
    {
        return getElementsByTagNameMatchingOwnText(document, tagName, pattern, true, true, true);
    }

    /**
     * get jsoup element list from jsoup document where the tag own text contains a pattern with case insensitive
     *
     * @param document DOM document
     * @param pattern a string which the tag own text should contain, case insensitive. Spaces and tabs are ignored
     * @return jsoup element list
     */
    public List<Element> getElementsContainingOwnTextIgnoreCase(Document document, String pattern)
    {
        return getElementsMatchingOwnText(document, pattern, false, false, true);
    }

    /**
     * get jsoup element list from jsoup document where the tag own text contains a pattern with case sensitive
     *
     * @param document DOM document
     * @param pattern a string which the tag own text should contain, case sensitive. Spaces and tabs are ignored
     * @return jsoup element list
     */
    public List<Element> getElementsContainingOwnText(Document document, String pattern)
    {
        return getElementsMatchingOwnText(document, pattern, true, false, true);
    }

    /**
     * get jsoup element list from jsoup document where the tag own text matches a pattern with case insensitive
     *
     * @param document DOM document
     * @param pattern a string which the tag own text should match, case insensitive. Spaces and tabs are ignored
     * @return jsoup element list
     */
    public List<Element> getElementsMatchingOwnTextIgnoreCase(Document document, String pattern)
    {
        return getElementsMatchingOwnText(document, pattern, false, true, true);
    }

    /**
     * get jsoup element list from jsoup document where the tag own text matches a pattern with case sensitive
     *
     * @param document DOM document
     * @param pattern a string which the tag own text should match, case sensitive. Spaces and tabs are ignored
     * @return jsoup element list
     */
    public List<Element> getElementsMatchingOwnText(Document document, String pattern)
    {
        return getElementsMatchingOwnText(document, pattern, true, true, true);
    }

    private List<Element> filterByPattern(List<Element> elements, String pattern, Condition condition)
    {
        List<Element> filtered = new ArrayList<>();

        if(hasItem(elements))
        {
            for (Element item : elements)
            {
                if(matchElementOwnText(
                        item,
                        pattern,
                        !condition.whereIgnoreCaseForOwnText,
                        !condition.whereOwnTextContainingPattern,
                        !condition.whereIncludingTabsAndSpacesForOwnText))
                {
                    filtered.add(item);
                }
            }
        }

        return filtered;
    }

    private WebElement findWebElement(WebDriver driver, Document document, ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        Elements anchorElements = toElements(getElements(document, anchorElementInfo));
        return findWebElement(driver, document, anchorElements, searchCssQuery);
    }

    private WebElement findWebElement(WebDriver driver, Document document, Elements anchorElements, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return findWebElementObject(driver, document, anchorElements, searchCssQuery);
    }

    private String findXpath(WebDriver driver, Elements anchorElements, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXPathsException
    {
        Document document = getActiveDocument(driver);
        return findXpath(document, anchorElements, searchCssQuery);
    }

    private List<String> getXPaths(
            Document document,
            List<Element> anchorElements,
            String searchCssQuery)
            throws NoAnchorElementFoundException, AmbiguousAnchorElementsException
    {
        List<String> xpathList = new ArrayList<>();

        if(document == null)
        {
            return xpathList;
        }

        if(hasNoItem(anchorElements))
        {
            throw new NoAnchorElementFoundException("No anchor element found");
        }

        if(anchorElements.size() > 1)
        {
            throw new AmbiguousAnchorElementsException("More than one anchor elements found");
        }

        Element anchorElement = anchorElements.get(0);
        Elements searchElements = document.select(searchCssQuery);

        if(hasItem(searchElements) && anchorElement != null)
        {
            xpathList = getXPaths(anchorElement, searchElements);
        }

        return xpathList;
    }

    private String buildXpathPartBetweenRootAndLeafExcludingRoot(Element root, Element leaf)
    {
        List<Element> allElements = getElementsBetweenLeafAndRootInclusive(leaf, root);

        if(hasNoItem(allElements))
        {
            return null;
        }

        int elementCount = allElements.size();

        if(elementCount == 1)
        {
            return "";
        }

        StringBuilder xpathBuilder = new StringBuilder();

        for(int i = elementCount - 2; i >= 0; i--)
        {
            String tagName = allElements.get(i).tagName();
            xpathBuilder.append(tagName);

            if(i > 0)
            {
                xpathBuilder.append("/");
            }
        }

        return xpathBuilder.toString();
    }

    private String buildXpath(ElementRecord record, Element anchorElement)
    {
        String xpath = null;
        Element rootElement = record.rootElement;
        Element foundElement = record.element;
        String xpathPartFromRootElementToFoundElement = buildXpathPartBetweenRootAndLeafExcludingRoot(rootElement, foundElement);
        String xpathPartFromRootElementToAnchorElement = buildXpathPartBetweenRootAndLeafExcludingRoot(rootElement, anchorElement);
        String rootElementTagName = rootElement.tagName();
        String anchorElementOwnText = anchorElement.ownText();

        if(xpathPartFromRootElementToFoundElement != null && xpathPartFromRootElementToAnchorElement != null)
        {
            if(xpathPartFromRootElementToAnchorElement == "" && xpathPartFromRootElementToFoundElement == "")
            {
                xpath =  String.format("//%s[contains(text(),'%s')]", rootElementTagName, anchorElementOwnText);
            }
            else if(xpathPartFromRootElementToAnchorElement == "")
            {
                xpath =  String.format("//%s[contains(text(),'%s')]/%s", rootElementTagName, anchorElementOwnText, xpathPartFromRootElementToFoundElement);
            }
            else if(xpathPartFromRootElementToFoundElement == "")
            {
                xpath =  String.format("//%s[%s[contains(text(),'%s')]]", xpathPartFromRootElementToAnchorElement, rootElementTagName, anchorElementOwnText);
            }
            else
            {
                xpath = String.format("//%s[%s[contains(text(),'%s')]]/%s",
                                      rootElementTagName, xpathPartFromRootElementToAnchorElement, anchorElementOwnText, xpathPartFromRootElementToFoundElement);
            }
        }

        return xpath;
    }

    private IFindObjectFunction<FindFuncParam, String> findXpathFunc = this::findXpath;
    private IFindObjectFunction<FindFuncParam, WebElement> findWebElementFunc =  this::findWebElement;

    private String findXpath(Document document, Elements anchorElements, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXPathsException
    {
        try
        {
            return findWebObject(null, document, anchorElements, searchCssQuery, findXpathFunc);
        }
        catch(AmbiguousFoundWebElementsException e)
        {}

        return null;
    }

    private WebElement findWebElementObject(WebDriver driver, Document document, Elements anchorElements, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
       try
       {
           return findWebObject(driver, document, anchorElements, searchCssQuery, findWebElementFunc);
       }
       catch(AmbiguousFoundXPathsException e)
       {
           throw new AmbiguousFoundWebElementsException("More than one web element found");
       }
    }

    private WebElement findWebElement(FindFuncParam params) throws AmbiguousFoundXPathsException, AmbiguousFoundWebElementsException
    {
        String xpath = getXPath(params.anchorElement, params.searchElements);
        return findWebElement(params.driver, xpath);
    }

    private String findXpath(FindFuncParam params) throws AmbiguousFoundXPathsException
    {
        return getXPath(params.anchorElement, params.searchElements);
    }

    private <T> T findWebObject(WebDriver driver, Document document, Elements anchorElements, String searchCssQuery, IFindObjectFunction function)
    throws AmbiguousAnchorElementsException, AmbiguousFoundXPathsException, AmbiguousFoundWebElementsException
    {
        if(document == null)
        {
            return null;
        }

        Elements searchElements = document.select(searchCssQuery);

        if(hasItem(anchorElements) && hasItem(searchElements))
        {
            AmbiguousFoundXPathsException lastAmbiguousFoundXPathsException = null;
            AmbiguousFoundWebElementsException lastAmbiguousFoundWebElementsException = null;

            if(returnErrorOnMultipleAnchorsFound && anchorElements.size() > 1)
            {
                throw new AmbiguousAnchorElementsException("More than one anchor elements found");
            }

            for (Element item : anchorElements)
            {
                FindFuncParam functionParams = new FindFuncParam(driver, item, searchElements);

                try
                {
                    return (T)function.apply(functionParams);
                }
                catch(AmbiguousFoundXPathsException e1)
                {
                    lastAmbiguousFoundXPathsException = e1;
                }
                catch(AmbiguousFoundWebElementsException e2)
                {
                    lastAmbiguousFoundWebElementsException = e2;
                }
            }

            if(lastAmbiguousFoundXPathsException != null)
            {
                throw lastAmbiguousFoundXPathsException;
            }

            if(lastAmbiguousFoundWebElementsException != null)
            {
                throw lastAmbiguousFoundWebElementsException;
            }
        }

        return null;
    }

    private WebElement findWebElement(WebDriver driver, String xpath) throws AmbiguousFoundWebElementsException
    {
        int timeoutInMs = 2000;
        int pollingEveryInMs = timeoutInMs/10;
        List<WebElement> foundWebElements = findWebElements(driver, By.xpath(xpath), timeoutInMs, pollingEveryInMs);

        if(hasItem(foundWebElements))
        {
            if(foundWebElements.size() > 1 && returnErrorOnMultipleWebElementsFound)
            {
                throw new AmbiguousFoundWebElementsException("More than one web elements found");
            }

            return findWebElement(driver, By.xpath(xpath), timeoutInMs, pollingEveryInMs);
        }

        return null;
    }

    private List<WebElement> findWebElements(WebDriver driver, final By locator, int timeoutInMs, int pollingEveryInMs)
    {
        Wait<WebDriver> wait = new FluentWait<>(driver)
            .withTimeout(timeoutInMs, TimeUnit.MILLISECONDS)
            .pollingEvery(pollingEveryInMs, TimeUnit.MILLISECONDS)
            .ignoring(NoSuchElementException.class);

        return wait.until(d -> d.findElements(locator));
    }

    private WebElement findWebElement(WebDriver driver, final By locator, int timeoutInMs, int pollingEveryInMs)
    {
        Wait<WebDriver> wait = new FluentWait<>(driver)
            .withTimeout(timeoutInMs, TimeUnit.MILLISECONDS)
            .pollingEvery(pollingEveryInMs, TimeUnit.MILLISECONDS)
            .ignoring(NoSuchElementException.class);

        return wait.until(d -> d.findElement(locator));
    }

    private List<Element> getActiveAnchorElements(ElementInfo anchorElementInfo, List<Element> anchorElements)
        throws AnchorIndexIfMultipleFoundOutOfBoundException
    {
        List<Element> activeAnchorElements = new ArrayList<>();

        if(anchorElementInfo != null)
        {
            if(anchorElementInfo.indexIfMultipleFound < 0 || anchorElements.isEmpty())
            {
                activeAnchorElements = anchorElements;
            }
            else if(anchorElementInfo.indexIfMultipleFound < anchorElements.size())
            {
                activeAnchorElements.add(anchorElements.get(anchorElementInfo.indexIfMultipleFound));
            }
            else
            {
                throw new AnchorIndexIfMultipleFoundOutOfBoundException("indexIfMultipleFound property of the AnchorElementInfo provided is out of bound");
            }
        }

        return activeAnchorElements;
    }

    private Elements getElements(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo, SearchMethod searchMethod)
        throws AmbiguousAnchorElementsException
    {
        List<Element> result = new ArrayList<>();
        List<Element> anchorElements = getElements(document, anchorElementInfo);
        Elements searchElements = toElements(getElements(document, searchElementInfo));

        if(hasItem(anchorElements) && hasItem(searchElements))
        {
            if(returnErrorOnMultipleAnchorsFound && anchorElements.size() > 1)
            {
                throw new AmbiguousAnchorElementsException("More than one anchor elements found");
            }

            for (Element item : anchorElements)
            {
                result = getElements(item, searchElements, searchMethod);

                if(hasItem(result))
                {
                    break;
                }
            }
        }

        return toElements(result);
    }

    private List<Element> getElements(Document document, String elementTagName, String elementOwnText)
    {
        return elementTagName == null
            ? getElementsMatchingOwnText(document, elementOwnText)
            : getElementsByTagNameMatchingOwnText(document, elementTagName, elementOwnText);
    }

    private List<Element> getElements(Document document, ElementInfo elementInfo)
    {
        List<Element> elements = new ArrayList<>();

        if(elementInfo == null)
        {
            return elements;
        }

        if(elementInfo.tagName == null && elementInfo.ownText != null)
        {
            return getElementsMatchingOwnText(
                document,
                elementInfo.ownText,
                !elementInfo.whereIgnoreCaseForOwnText,
                !elementInfo.whereOwnTextContainingPattern,
                !elementInfo.whereIncludingTabsAndSpacesForOwnText);
        }
        else if (elementInfo.tagName != null && elementInfo.ownText != null)
        {
            return getElementsByTagNameMatchingOwnText(
                document,
                elementInfo.tagName,
                elementInfo.ownText,
                !elementInfo.whereIgnoreCaseForOwnText,
                !elementInfo.whereOwnTextContainingPattern,
                !elementInfo.whereIncludingTabsAndSpacesForOwnText);
        }
        else
        {
            return getElementsByTagName(document, elementInfo.tagName);
        }
    }

    private List<Element> getElementsByTagNameMatchingOwnText(Document document,
                                                              String tagName,
                                                              String pattern,
                                                              boolean caseSensitive,
                                                              boolean exactMatch,
                                                              boolean ignoreTabsAndSpaces)
    {
        List<Element> elements = getElementsByTagName(document, tagName);
        return getElementsMatchingOwnText(elements, pattern, caseSensitive, exactMatch, ignoreTabsAndSpaces);
    }

    private List<Element> getElementsMatchingOwnText(Document document,
                                                     String pattern,
                                                     boolean caseSensitive,
                                                     boolean exactMatch,
                                                     boolean ignoreTabsAndSpaces)
    {
        List<Element> elements = document == null ? new ArrayList<>() : document.getAllElements();
        return getElementsMatchingOwnText(elements, pattern, caseSensitive, exactMatch, ignoreTabsAndSpaces);
    }

    private List<Element> getElementsMatchingOwnText(List<Element> elements,
                                                     String pattern,
                                                     boolean caseSensitive,
                                                     boolean exactMatch,
                                                     boolean ignoreTabsAndSpaces)
    {
        List<Element> result = new ArrayList<>();

        if(hasItem(elements))
        {
            for (Element item : elements)
            {
                if(matchElementOwnText(item, pattern, caseSensitive, exactMatch, ignoreTabsAndSpaces))
                {
                    result.add(item);
                }
            }
        }

        return result;
    }

    private boolean matchElementOwnText(Element element,
                                        String pattern,
                                        boolean caseSensitive,
                                        boolean exactMatch,
                                        boolean ignoreTabsAndSpaces)
    {
        if(element == null || element.ownText() == null || pattern == null)
        {
            return false;
        }

        String elementOwnText = ignoreTabsAndSpaces ? element.ownText().replace("\\s+", "") : element.ownText();
        String patternWithoutSpaces = ignoreTabsAndSpaces ? pattern.replace("\\s+", "") : pattern;

        if(caseSensitive && exactMatch)
        {
            if (elementOwnText.equals(patternWithoutSpaces))
            {
                return true;
            }
        }
        else if (caseSensitive)
        {
            if(elementOwnText.contains(patternWithoutSpaces))
            {
                return true;
            }
        }
        else if(exactMatch)
        {
            if(elementOwnText.equalsIgnoreCase(patternWithoutSpaces))
            {
                return true;
            }
        }
        else
        {
            if(elementOwnText.toLowerCase().contains(patternWithoutSpaces.toLowerCase()))
            {
                return true;
            }
        }

        return false;
    }

    private List<Element> getElementsByTagName(Document document, String tagName)
    {
        List<Element> result = new ArrayList<>();

        if(document != null)
        {
            List<TreeElement> elementTree = getHtmlDocumentElementTree(document);

            for (TreeElement item : elementTree)
            {
                String elementTagName = item.element.tagName();

                if(elementTagName != null && tagName != null && elementTagName.trim().equalsIgnoreCase(tagName.trim()))
                {
                    result.add(item.element);
                }
            }
        }

        return result;
    }

    private List<Element> getClosestElements(
        Document document,
        List<Element> anchorElements,
        String searchCssQuery)
        throws NoAnchorElementFoundException, AmbiguousAnchorElementsException
    {
        List<Element> foundElements = new ArrayList<>();

        if(document == null)
        {
            return foundElements;
        }

        if(hasNoItem(anchorElements))
        {
            throw new NoAnchorElementFoundException("No anchor element found");
        }

        if(anchorElements.size() > 1)
        {
            throw new AmbiguousAnchorElementsException("More than one anchor elements found");
        }

        Element anchorElement = anchorElements.get(0);
        Elements searchElements = document.select(searchCssQuery);
        List<ElementRecord> foundElementRecords =
            getElementRecordsByLinkAndShortestDistance(anchorElement, searchElements);

        if(hasItem(searchElements) && hasItem(foundElementRecords))
        {
            for(int i = 0; i <searchElements.size(); i++)
            {
                for(ElementRecord record : foundElementRecords)
                {
                    if(i == record.index)
                    {
                        foundElements.add(searchElements.get(i));
                        break;
                    }
                }
            }
        }

        return foundElements;
    }

    private List<Element> getClosestElements(Elements anchorElements, Elements searchElements)
    {
        List<ElementRecord> result = new ArrayList<>();

        if(hasItem(anchorElements))
        {
            for (Element anchorElement : anchorElements)
            {
                List<ElementRecord> currentFound = getElementRecords(anchorElement, searchElements, SearchMethod.ByDistance);
                result.addAll(currentFound);
            }
        }

        List<ElementRecord> finalFoundRecords = getClosestElementRecords(result);
        return extractElementsFromElementRecords(finalFoundRecords);
    }

    private List<Element> getElements(Element anchorElement, Elements searchElements, SearchMethod searchMethod)
    {
        List<ElementRecord> result = getElementRecords(anchorElement, searchElements, searchMethod);
        return extractElementsFromElementRecords(result);
    }

    private List<Element> extractElementsFromElementRecords(List<ElementRecord> elementRecords)
    {
        List<Element> elements = new ArrayList<>();

        if(hasItem(elementRecords))
        {
            for(ElementRecord record : elementRecords)
            {
                elements.add(record.element);
            }
        }

        return elements;
    }

    private List<ElementRecord> getElementRecords(Element anchorElement, Elements searchElements, SearchMethod searchMethod)
    {
        List<ElementRecord> result;

        switch (searchMethod)
        {
            case ByDistance:
                result = getClosestElementRecords(anchorElement, searchElements);
                break;
            case ByLink:
                result = getLinkedElementRecords(anchorElement, searchElements);
                break;
            case ByLinkAndDistance:
                result = getElementRecordsByLinkAndShortestDistance(anchorElement, searchElements);
                break;
            default:
                throw new NotImplementedException();
        }

        return result;
    }

    private List<ElementRecord> getElementRecordsByLinkAndShortestDistance(Element anchorElement, Elements searchElements)
    {
        List<ElementRecord> linkedRecords = getLinkedElementRecords(anchorElement, searchElements);
        List<ElementRecord> result;

        if(hasItem(linkedRecords))
        {
            result = linkedRecords.size() == 1
                ? linkedRecords
                : getClosestElementRecords(linkedRecords);
        }
        else
        {
            result = getClosestElementRecords(anchorElement, searchElements);
        }

        return result;
    }

    private List<ElementRecord> getLinkedElementRecords(Element anchorElement, Elements searchElements)
    {
        List<ElementRecord> searchElementRecords = getElementRecords(anchorElement, searchElements);
        return getLinkedElementRecords(searchElementRecords, anchorElement);
    }

    private List<ElementRecord> getLinkedElementRecords(List<ElementRecord> searchElementRecords, Element anchorElement)
    {
        List<ElementRecord> result = new ArrayList<>();

        if (hasItem(searchElementRecords) && anchorElement != null)
        {
            Attribute anchorElementForAttribute = getAttributeByNameContainingPattern(anchorElement, "for");

            if(anchorElementForAttribute != null)
            {
                String anchorElementForAttributeValue = anchorElementForAttribute.getValue();

                for (ElementRecord item : searchElementRecords)
                {
                    String searchElementId = item.element.attr("id");
                    String searchElementName = item.element.attr("name");

                    if((searchElementId != null && searchElementId.trim().equalsIgnoreCase(anchorElementForAttributeValue)) ||
                       (searchElementName != null && searchElementName.trim().equals(anchorElementForAttributeValue)))
                    {
                        result.add(item);
                    }
                }
            }
        }

        return result;
    }

    private Attribute getAttributeByNameContainingPattern(Element element, String pattern)
    {
        if(element == null)
        {
            return null;
        }

        List<Attribute> anchorAttributes = element.attributes().asList();

        for(Attribute item : anchorAttributes)
        {
            String attributeKey = item.getKey();

            if(attributeKey.toLowerCase().contains(pattern.toLowerCase()))
            {
                return item;
            }
        }

        return null;
    }

    private List<ElementRecord> getClosestElementRecords(Element anchorElement, Elements searchElements)
    {
        List<ElementRecord> searchElementRecords = getElementRecords(anchorElement, searchElements);
        return getClosestElementRecords(searchElementRecords);
    }

    private List<ElementRecord> getClosestElementRecords(List<ElementRecord> searchElementRecords)
    {
        int shortestDistance = -1;
        List<ElementRecord> result = new ArrayList<>();

        if (hasItem(searchElementRecords))
        {
            for (ElementRecord item : searchElementRecords)
            {
                int distance = item.distanceToAnchorElement;

                if (shortestDistance == -1 || distance < shortestDistance)
                {
                    shortestDistance = distance;
                    result = new ArrayList<>();
                    result.add(item);
                }
                else if (distance == shortestDistance)
                {
                    result.add(item);
                }
            }
        }

        return result;
    }

    private List<ElementRecord> getElementRecords(Element anchorElement, Elements searchElements)
    {
        List<ElementRecord> result = new ArrayList<>();

        if(hasNoItem(searchElements) || anchorElement == null)
        {
            return result;
        }

        for (int i = 0 ; i < searchElements.size(); i++)
        {
            Element currentSearchElement = searchElements.get(i);
            ElementRecord searchElementRecord = getElementRecord(anchorElement, currentSearchElement, i);

            if(searchElementRecord != null)
            {
                result.add(searchElementRecord);
            }
        }

        return result;
    }

    private ElementRecord getElementRecord(Element anchorElement, Element searchElement, int searchElementIndex)
    {
        MapEntry<List<Integer>, List<TreeElement>> matchedElementPositionAndTreePair =
            getContainingTree(anchorElement, searchElement);
        ElementRecord currentFoundElementRecord = null;

        if(matchedElementPositionAndTreePair != null)
        {
            List<TreeElement> tree = matchedElementPositionAndTreePair.getValue();
            List<Integer> matchedElementPosition = matchedElementPositionAndTreePair.getKey();
            TreeElement anchor = getFirstMatchedElementInTree(tree, anchorElement);

            if(hasItem(matchedElementPosition) && anchor != null && hasItem(anchor.position))
            {
                currentFoundElementRecord = new ElementRecord();
                currentFoundElementRecord.element = searchElement;
                currentFoundElementRecord.rootElement = getRootElement(tree).element;
                currentFoundElementRecord.index = searchElementIndex;
                currentFoundElementRecord.distanceToAnchorElement = matchedElementPosition.size() + anchor.position.size() - 2;
            }
        }

        return currentFoundElementRecord;
    }

    /**
     * @return map entry of the position of the search element and the tree
     */
    private MapEntry<List<Integer>, List<TreeElement>> getContainingTree(Element anchorElement, Element searchElement)
    {
        List<TreeElement> elementTree = new ArrayList<>();
        Element rootElement = anchorElement;
        TreeElement firstFound = null;

        while(firstFound == null)
        {
            elementTree = getElementTree(rootElement);
            firstFound = getFirstMatchedElementInTree(elementTree, searchElement);

            if(firstFound == null)
            {
                if(rootElement == null)
                {
                    break;
                }

                rootElement = rootElement.parent();
            }
        }

        return firstFound == null ? null : new MapEntry<>(firstFound.position, elementTree);
    }

    private TreeElement getFirstMatchedElementInTree(List<TreeElement> elementTree, Element searchElement)
    {
        if(hasItem(elementTree))
        {
            TreeElement searchTreeElement = new TreeElement(searchElement);

            for (TreeElement item : elementTree)
            {
                if (elementEquals(item, searchTreeElement))
                {
                    return item;
                }
            }
        }

        return null;
    }

    private List<Element> getElementsBetweenLeafAndRootInclusive(Element leaf, Element root)
    {
        List<Element> result = new ArrayList<>();
        result.add(leaf);
        Element currentParent = leaf;

        do
        {
            if(elementEquals(new TreeElement(currentParent), new TreeElement(root)))
            {
                break;
            }
            else
            {
                currentParent = currentParent.parent();
                result.add(currentParent);
            }
        }
        while(true);

        return result;
    }

    private boolean elementEquals(TreeElement element1, TreeElement element2)
    {
        if(element1 == null)
        {
            return false;
        }

        boolean result = element1.equals(element2);
        return result ? true : areEquals(element1.element, element2.element);
    }

    private boolean areEquals(Element element1, Element element2)
    {
        if(element1 == null || element2 == null)
        {
            return false;
        }

        if(element1.toString().equals(element2.toString()))
        {
            Element parent1 = element1.parent();
            Element parent2 = element2.parent();

            if(parent1.tagName().equalsIgnoreCase("html") && parent2.tagName().equalsIgnoreCase("html"))
            {
                 return true;
            }
            else
            {
                return areEquals(element1.parent(), element2.parent());
            }
        }
        else
        {
            return false;
        }
    }

    private TreeElement getRootElement(List<TreeElement> tree)
    {
        List<Integer> rootPosition = new ArrayList<>();
        rootPosition.add(0);

        for (TreeElement item : tree)
        {
            if(item.position.equals(rootPosition))
            {
                return item;
            }
        }

        return null;
    }

    /**
     * same outcome as jsoup.getAllElements()
     */
    private List<TreeElement> getHtmlDocumentElementTree(Document document)
    {
        Element htmlElement = getHtmlElement(document);
        return htmlElement == null ? new ArrayList<>() : getElementTree(htmlElement);
    }

    private List<TreeElement> getElementTree(Element element)
    {
        List<TreeElement> tree = new ArrayList<>();
        TreeElement rootElement = new TreeElement();
        rootElement.position.add(0);
        rootElement.element = element;
        tree.add(rootElement);
        List<TreeElement> allChildren = getAllChildren(element, rootElement.position);
        tree.addAll(allChildren);

        return tree;
    }

    private List<TreeElement> getAllChildren(Element element, List<Integer> startingPosition)
    {
        List<TreeElement> result = new ArrayList<>();

        if(element != null)
        {
            Elements children = element.children();

            if(hasItem(children))
            {
                for(int i = 0; i < children.size(); i++)
                {
                    TreeElement treeElement = new TreeElement();
                    treeElement.position = new ArrayList<>(startingPosition);
                    treeElement.position.add(i);
                    treeElement.element = children.get(i);
                    result.add(treeElement);
                    List<TreeElement> nextResult = getAllChildren(treeElement.element, treeElement.position);
                    result.addAll(nextResult);
                }
            }
        }

        return result;
    }

    private Element getHtmlElement(Document document)
    {
        Elements elements = document == null ? null : document.select("html");
        return hasItem(elements) ? elements.first() : null;
    }

    private Elements toElements(List<Element> elements)
    {
        Elements result = new Elements();

        if(hasItem(elements))
        {
            for(Element item : elements)
            {
                result.add(item);
            }
        }

        return result;
    }

    private <T> boolean hasItem(List<T> list)
    {
        return (list != null && !list.isEmpty());
    }

    private <T> boolean hasNoItem(List<T> list)
    {
        return (list == null || list.isEmpty());
    }
}
