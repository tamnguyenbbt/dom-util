package com.github.tamnguyenbbt.dom;

import com.github.tamnguyenbbt.exception.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.ArrayList;
import java.util.List;

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
public class DomUtil extends DomInternal
{
    public DomUtil()
    {
        searchDepth = 1;
        timeoutInMs = 2000;
    }

    public void setSearchDepth(int searchDepth)
    {
        this.searchDepth = searchDepth;
    }

    public void setTimeoutInMs(int timeoutInMs)
    {
        this.timeoutInMs = timeoutInMs;
    }

    public WebElement getWebElementWithTwoAnchorsBestEffort(WebDriver driver, String parentAnchorElementOwnText,
                                                            String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundWebElementsException
    {
        return getWebElementWithTwoAnchorsBestEffort(driver, parentAnchorElementOwnText,
                                                     null, anchorElementOwnText, searchCssQuery);
    }

    public WebElement getWebElementWithTwoAnchors(WebDriver driver, String parentAnchorElementOwnText,
                                                  String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return getWebElementWithTwoAnchors(driver, parentAnchorElementOwnText,
                                           null, anchorElementOwnText, searchCssQuery);
    }

    public WebElement getWebElementWithTwoAnchorsBestEffort(WebDriver driver, String parentAnchorElementOwnText,
                                                            String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundWebElementsException
    {
        return getWebElementWithTwoAnchorsBestEffort(driver, null, parentAnchorElementOwnText,
                                              anchorElementTagName, anchorElementOwnText, searchCssQuery);
    }

    public WebElement getWebElementWithTwoAnchors(WebDriver driver, String parentAnchorElementOwnText,
                                                  String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return getWebElementWithTwoAnchors(driver, null, parentAnchorElementOwnText,
                                                     anchorElementTagName, anchorElementOwnText, searchCssQuery);
    }

    public WebElement getWebElementWithTwoAnchorsBestEffort(WebDriver driver, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                            String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundWebElementsException
    {
        try
        {
            return getWebElementWithTwoAnchors(driver, parentAnchorElementTagName, parentAnchorElementOwnText,
                                                         anchorElementTagName, anchorElementOwnText, searchCssQuery, true);
        }
        catch(AmbiguousAnchorElementsException e) { return null; }
    }

    public WebElement getWebElementWithTwoAnchors(WebDriver driver, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                  String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return getWebElementWithTwoAnchors(driver, parentAnchorElementTagName, parentAnchorElementOwnText, anchorElementTagName, anchorElementOwnText, searchCssQuery, false);
    }

    public WebElement getWebElementWithTwoAnchorsBestEffort(WebDriver driver, ElementInfo parentAnchorElementInfo,
                                                     ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getWebElementWithTwoAnchors(driver, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, true);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public WebElement getWebElementWithTwoAnchors(WebDriver driver, ElementInfo parentAnchorElementInfo,
                                                  ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        return getWebElementWithTwoAnchors(driver, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, false);
    }

    public WebElement getWebElementBestEffort(WebDriver driver, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundWebElementsException
    {
        return getWebElementBestEffort(driver, null, anchorElementOwnText, searchCssQuery);
    }

    public WebElement getWebElement(WebDriver driver, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return getWebElement(driver, null, anchorElementOwnText, searchCssQuery);
    }

    public WebElement getWebElementBestEffort(WebDriver driver, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundWebElementsException
    {
        try
        {
            return getWebElement(driver, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, true);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public WebElement getWebElement(WebDriver driver, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return getWebElementExactMatch(driver, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, false);
    }

    public WebElement getWebElementBestEffort(WebDriver driver, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getWebElement(driver, anchorElementInfo, searchElementInfo, SearchMethod.ByLinkAndDistance, true);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public WebElement getWebElement(WebDriver driver, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        return getWebElement(driver, anchorElementInfo, searchElementInfo, SearchMethod.ByLinkAndDistance, false);
    }

    public List<WebElement> getWebElementsWithTwoAnchorsBestEffort(WebDriver driver, ElementInfo parentAnchorElementInfo,
                                                            ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getWebElementsWithTwoAnchors(driver, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, true);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public List<WebElement> getWebElementsWithTwoAnchors(WebDriver driver, ElementInfo parentAnchorElementInfo,
                                                            ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        return getWebElementsWithTwoAnchors(driver, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, false);
    }

    public List<WebElement> getWebElementsBestEffort(WebDriver driver, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundWebElementsException
    {
        try
        {
            return getWebElements(driver, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, true);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public List<WebElement> getWebElements(WebDriver driver, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return getWebElements(driver, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, false);
    }

    public List<WebElement> getWebElementsBestEffort(WebDriver driver, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getWebElements(driver, anchorElementInfo, searchElementInfo, SearchMethod.ByLinkAndDistance, true);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public List<WebElement> getWebElements(WebDriver driver, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        return getWebElements(driver, anchorElementInfo, searchElementInfo, SearchMethod.ByLinkAndDistance, false);
    }

    public String getXpathWithTwoAnchorsBestEffort(Document document, String parentAnchorElementOwnText,
                                            String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundXpathsException
    {
        try
        {
            return getXpathWithTwoAnchors(document, parentAnchorElementOwnText, anchorElementOwnText, searchCssQuery, true);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public String getXpathWithTwoAnchors(Document document, String parentAnchorElementOwnText,
                                            String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return getXpathWithTwoAnchors(document, parentAnchorElementOwnText, anchorElementOwnText, searchCssQuery, false);
    }

    public String getXpathWithTwoAnchorsBestEffort(Document document, String parentAnchorElementOwnText,
                                            String anchorElementTagName,
                                            String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundXpathsException
    {
        try
        {
            return getXpathWithTwoAnchors(document, parentAnchorElementOwnText, anchorElementTagName,
                                          anchorElementOwnText, searchCssQuery, true);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public String getXpathWithTwoAnchors(Document document, String parentAnchorElementOwnText,
                                            String anchorElementTagName,
                                            String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return getXpathWithTwoAnchors(document, parentAnchorElementOwnText, anchorElementTagName,
                                      anchorElementOwnText, searchCssQuery, false);
    }

    public String getXpathWithTwoAnchorsBestEffort(Document document, String parentAnchorElementTagName,
                                            String parentAnchorElementOwnText, String anchorElementTagName,
                                            String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundXpathsException
    {
        try
        {
            return getXpathWithTwoAnchors(document, parentAnchorElementTagName, parentAnchorElementOwnText,
                                          anchorElementTagName, anchorElementOwnText, searchCssQuery, true);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public String getXpathWithTwoAnchors(Document document, String parentAnchorElementTagName,
                                            String parentAnchorElementOwnText, String anchorElementTagName,
                                            String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return getXpathWithTwoAnchors(document, parentAnchorElementTagName, parentAnchorElementOwnText,
                                      anchorElementTagName, anchorElementOwnText, searchCssQuery, false);
    }

    public String getXpathWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementOwnText,
                                                      String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundXpathsException
    {
        try
        {
            return getXpathWithTwoAnchorsExactMatch(document, parentAnchorElementOwnText, anchorElementOwnText, searchCssQuery, true);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public String getXpathWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText,
                                                      String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return getXpathWithTwoAnchorsExactMatch(document, parentAnchorElementOwnText, anchorElementOwnText, searchCssQuery, false);
    }

    public String getXpathWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementOwnText,
                                                      String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundXpathsException
    {
        try
        {
            return getXpathWithTwoAnchorsExactMatch(document, parentAnchorElementOwnText, anchorElementTagName,
                                                    anchorElementOwnText, searchCssQuery, true);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public String getXpathWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText,
                                                      String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return getXpathWithTwoAnchorsExactMatch(document, parentAnchorElementOwnText, anchorElementTagName,
                                                anchorElementOwnText, searchCssQuery, false);
    }

    public String getXpathWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementTagName,
                                                      String parentAnchorElementOwnText,
                                                      String anchorElementTagName, String anchorElementOwnText,
                                                      String searchCssQuery)
        throws AmbiguousFoundXpathsException
    {
        try
        {
            return getXpathWithTwoAnchorsExactMatch(document, parentAnchorElementTagName, parentAnchorElementOwnText,
                                                    anchorElementTagName, anchorElementOwnText, searchCssQuery, true);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public String getXpathWithTwoAnchorsExactMatch(Document document, String parentAnchorElementTagName,
                                                      String parentAnchorElementOwnText,
                                                      String anchorElementTagName, String anchorElementOwnText,
                                                      String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return getXpathWithTwoAnchorsExactMatch(document, parentAnchorElementTagName, parentAnchorElementOwnText,
                                                anchorElementTagName, anchorElementOwnText, searchCssQuery, false);
    }

    public String getXpathWithTwoAnchorsBestEffort(Document document, ElementInfo parentAnchorElementInfo,
                                            ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousFoundXpathsException,
        AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getXpathWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, true);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public String getXpathWithTwoAnchors(Document document, ElementInfo parentAnchorElementInfo,
                                            ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException,
        AnchorIndexIfMultipleFoundOutOfBoundException
    {
        return getXpathWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, false);
    }

    public String getXpathWithTwoAnchorsBestEffort(WebDriver driver, ElementInfo parentAnchorElementInfo,
                                            ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getXpathWithTwoAnchors(driver, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, true);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public String getXpathWithTwoAnchors(WebDriver driver, ElementInfo parentAnchorElementInfo,
                                            ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        return getXpathWithTwoAnchors(driver, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, false);
    }

    public String getXpathBestEffort(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundXpathsException
    {
        try
        {
            return getXpath(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, true);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public String getXpath(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return getXpath(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, false);
    }

    public String getXpathBestEffort(Document document, Elements anchorElements, ElementInfo searchElementInfo)
        throws AmbiguousFoundXpathsException
    {
        try
        {
            return  getXpath(document, anchorElements, searchElementInfo, SearchMethod.ByLinkAndDistance, true);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public String getXpath(Document document, Elements anchorElements, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return  getXpath(document, anchorElements, searchElementInfo, SearchMethod.ByLinkAndDistance, false);
    }

    public String getXpathBestEffort(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AmbiguousFoundXpathsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getXpath(document, anchorElementInfo, searchElementInfo, SearchMethod.ByLinkAndDistance, true);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public String getXpath(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException,
        AnchorIndexIfMultipleFoundOutOfBoundException
    {
        return getXpath(document, anchorElementInfo, searchElementInfo, SearchMethod.ByLinkAndDistance, false);
    }

    public String getXpathBestEffort(Elements anchorElements, Elements searchElements)
        throws AmbiguousFoundXpathsException
    {
        try
        {
            return getXpath(anchorElements, searchElements, SearchMethod.ByLinkAndDistance, true);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public String getXpath(Elements anchorElements, Elements searchElements)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return getXpath(anchorElements, searchElements, SearchMethod.ByLinkAndDistance, false);
    }

    public String getXpath(Element anchorElement, Elements searchElements)
        throws AmbiguousFoundXpathsException
    {
        return getXpath(anchorElement, searchElements, SearchMethod.ByLinkAndDistance);
    }

    public List<String> getXpathsBestEffort(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
    {
        try
        {
            return getXpaths(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, true);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return new ArrayList<>();
        }
    }

    public List<String> getXpaths(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException
    {
        return getXpaths(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, false);
    }

    public List<String> getXpathsBestEffort(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getXpaths(document, anchorElementInfo, searchElementInfo, SearchMethod.ByLinkAndDistance, true);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return new ArrayList<>();
        }
    }

    public List<String> getXpaths(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        return getXpaths(document, anchorElementInfo, searchElementInfo, SearchMethod.ByLinkAndDistance, false);
    }

    public List<String> getXpathsBestEffort(Document document, Elements anchorElements, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException
    {
        try
        {
            return getXpaths(document, anchorElements, searchElementInfo, SearchMethod.ByLinkAndDistance, true);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return new ArrayList<>();
        }
    }

    public List<String> getXpaths(Document document, Elements anchorElements, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException
    {
        return getXpaths(document, anchorElements, searchElementInfo, SearchMethod.ByLinkAndDistance, false);
    }

    public List<String> getXpathsBestEffort(Elements anchorElements, Elements searchElements)
    {
        try
        {
            return getXpaths(anchorElements, searchElements, SearchMethod.ByLinkAndDistance, true);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return new ArrayList<>();
        }
    }

    public List<String> getXpaths(Elements anchorElements, Elements searchElements)
        throws AmbiguousAnchorElementsException
    {
        return getXpaths(anchorElements, searchElements, SearchMethod.ByLinkAndDistance, false);
    }

    public List<String> getXpaths(Element anchorElement, Elements searchElements)
    {
        return getXpaths(anchorElement, searchElements, SearchMethod.ByLinkAndDistance);
    }

    public List<String> getIndexedXpaths(WebDriver driver, String xpath)
    {
        List<String> result = new ArrayList<>();

        if (xpath == null)
        {
            return result;
        }

        List<WebElement> foundWebElements = driver.findElements(By.xpath(xpath));

        if (hasItem(foundWebElements))
        {
            int numberOfIndexedXpaths = foundWebElements.size();

            if (numberOfIndexedXpaths == 1)
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

    public Element getElementWithTwoAnchorsBestEffort(Document document, String parentAnchorElementOwnText,
                                                      String anchorElementOwnText,
                                                      String searchCssQuery)
        throws AmbiguousFoundElementsException
    {
        try
        {
            return getElementWithTwoAnchors(document, parentAnchorElementOwnText, anchorElementOwnText,
                                            searchCssQuery, true);
        }
        catch (AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public Element getElementWithTwoAnchors(Document document, String parentAnchorElementOwnText,
                                            String anchorElementOwnText,
                                            String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchors(document, parentAnchorElementOwnText, anchorElementOwnText,
                                        searchCssQuery, false);
    }

    public Element getElementWithTwoAnchorsBestEffort(Document document, String parentAnchorElementOwnText,
                                                      String anchorElementTagName,
                                                      String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundElementsException
    {
        try
        {
            return getElementWithTwoAnchors(document, parentAnchorElementOwnText, anchorElementTagName,
                                            anchorElementOwnText, searchCssQuery, true);
        }
        catch (AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public Element getElementWithTwoAnchors(Document document, String parentAnchorElementOwnText,
                                            String anchorElementTagName,
                                            String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchors(document, parentAnchorElementOwnText, anchorElementTagName,
                                        anchorElementOwnText, searchCssQuery, false);
    }

    public Element getElementWithTwoAnchorsBestEffort(Document document, String parentAnchorElementTagName,
                                                      String parentAnchorElementOwnText,
                                                      String anchorElementTagName, String anchorElementOwnText,
                                                      String searchCssQuery)
        throws AmbiguousFoundElementsException
    {
        try
        {
            return getElementWithTwoAnchors(document, parentAnchorElementTagName, parentAnchorElementOwnText,
                                            anchorElementTagName, anchorElementOwnText, searchCssQuery, true);
        }
        catch (AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public Element getElementWithTwoAnchors(Document document, String parentAnchorElementTagName,
                                            String parentAnchorElementOwnText,
                                            String anchorElementTagName, String anchorElementOwnText,
                                            String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchors(document, parentAnchorElementTagName, parentAnchorElementOwnText,
                                        anchorElementTagName, anchorElementOwnText, searchCssQuery, false);
    }

    public Element getElementWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementOwnText,
                                                                String anchorElementOwnText,
                                                                String searchCssQuery)
        throws AmbiguousFoundElementsException
    {
        try
        {
            return getElementWithTwoAnchorsExactMatch(document, parentAnchorElementOwnText, anchorElementOwnText,
                                                      searchCssQuery, true);
        }
        catch (AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public Element getElementWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText,
                                                      String anchorElementOwnText,
                                                      String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchorsExactMatch(document, parentAnchorElementOwnText, anchorElementOwnText,
                                                  searchCssQuery, false);
    }

    public Element getElementWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementOwnText,
                                                                String anchorElementTagName,
                                                                String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundElementsException
    {
        try
        {
            return getElementWithTwoAnchorsExactMatch(document, parentAnchorElementOwnText, anchorElementTagName,
                                                      anchorElementOwnText, searchCssQuery, true);
        }
        catch (AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public Element getElementWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText,
                                                      String anchorElementTagName,
                                                      String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchorsExactMatch(document, parentAnchorElementOwnText, anchorElementTagName,
                                                  anchorElementOwnText, searchCssQuery, false);
    }

    public Element getElementWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementTagName,
                                                                String parentAnchorElementOwnText,
                                                                String anchorElementTagName,
                                                                String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundElementsException
    {
        try
        {
            return getElementWithTwoAnchorsExactMatch(document, parentAnchorElementTagName, parentAnchorElementOwnText,
                                                      anchorElementTagName, anchorElementOwnText, searchCssQuery, true);
        }
        catch (AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public Element getElementWithTwoAnchorsExactMatch(Document document, String parentAnchorElementTagName,
                                                      String parentAnchorElementOwnText,
                                                      String anchorElementTagName, String anchorElementOwnText,
                                                      String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchorsExactMatch(document, parentAnchorElementTagName, parentAnchorElementOwnText,
                                                  anchorElementTagName, anchorElementOwnText, searchCssQuery, false);
    }

    public Element getElementWithTwoAnchorsBestEffort(Document document, ElementInfo parentAnchorElementInfo,
                                                      ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousFoundElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getElementWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo,
                                            searchCssQuery, true);
        }
        catch (AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public Element getElementWithTwoAnchors(Document document, ElementInfo parentAnchorElementInfo,
                                            ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException,
        AnchorIndexIfMultipleFoundOutOfBoundException
    {
        return getElementWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo,
                                        searchCssQuery, false);
    }

    public Element getElementBestEffort(Document document, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundElementsException
    {
        return getElementBestEffort(document, null, anchorElementOwnText, searchCssQuery);
    }

    public Element getElement(Document document, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElement(document, null, anchorElementOwnText, searchCssQuery);
    }

    public Element getElementBestEffort(Document document, String anchorElementTagName, String anchorElementOwnText,
                                        String searchCssQuery)
        throws AmbiguousFoundElementsException
    {
        try
        {
            return getElementExactMatch(document, anchorElementTagName, anchorElementOwnText, searchCssQuery,
                                        SearchMethod.ByLinkAndDistance, true);
        }
        catch (AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public Element getElement(Document document, String anchorElementTagName, String anchorElementOwnText,
                              String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementExactMatch(document, anchorElementTagName, anchorElementOwnText, searchCssQuery,
                                    SearchMethod.ByLinkAndDistance, false);
    }

    public Element getElementBestEffort(Document document, ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousFoundElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getElement(document, anchorElementInfo, new ElementInfo(searchCssQuery),
                              SearchMethod.ByLinkAndDistance, true);
        }
        catch (AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public Element getElement(Document document, ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException,
        AnchorIndexIfMultipleFoundOutOfBoundException
    {
        return getElement(document, anchorElementInfo, new ElementInfo(searchCssQuery), SearchMethod.ByLinkAndDistance,
                          false);
    }

    public Element getElementBestEffort(Document document, Elements anchorElements, ElementInfo searchElementInfo)
        throws AmbiguousFoundElementsException
    {
        try
        {
            return getElement(document, anchorElements, searchElementInfo, SearchMethod.ByLinkAndDistance, true);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public Element getElement(Document document, Elements anchorElements, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElement(document, anchorElements, searchElementInfo, SearchMethod.ByLinkAndDistance, false);
    }

    public Element getElementBestEffort(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AmbiguousFoundElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getElement(document, anchorElementInfo, searchElementInfo, SearchMethod.ByLinkAndDistance, true);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public Element getElement(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        return getElement(document, anchorElementInfo, searchElementInfo, SearchMethod.ByLinkAndDistance, false);
    }

    public Element getElementBestEffort(Elements anchorElements, Elements searchElements)
        throws AmbiguousFoundElementsException
    {
        try
        {
            return getElement(anchorElements, searchElements, SearchMethod.ByLinkAndDistance, true);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public Element getElement(Elements anchorElements, Elements searchElements)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElement(anchorElements, searchElements, SearchMethod.ByLinkAndDistance, false);
    }

    public Element getElement(Element anchorElement, Elements searchElements)
        throws AmbiguousFoundElementsException
    {
        return getElement(anchorElement, searchElements, SearchMethod.ByLinkAndDistance);
    }

    public Elements getElementsBestEffort(Document document, ElementInfo anchorElementInfo, String searchCssQuery)
        throws AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getElements(document, anchorElementInfo, new ElementInfo(searchCssQuery), SearchMethod.ByDistance,
                               true);
        }
        catch (AmbiguousAnchorElementsException e)
        {
            return new Elements();
        }
    }

    public Elements getElements(Document document, ElementInfo anchorElementInfo, String searchCssQuery)
        throws AnchorIndexIfMultipleFoundOutOfBoundException, AmbiguousAnchorElementsException
    {
        return getElements(document, anchorElementInfo, new ElementInfo(searchCssQuery), SearchMethod.ByDistance,
                           false);
    }

    public Elements getElementsBestEffort(Document document, String anchorElementOwnText, String searchCssQuery)
    {
        return getElementsBestEffort(document, null, anchorElementOwnText, searchCssQuery);
    }

    public Elements getElements(Document document, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException
    {
        return getElements(document, null, anchorElementOwnText, searchCssQuery);
    }

    public Elements getElementsBestEffort(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
    {
        try
        {
            return getElementsExactMatch(document, anchorElementTagName, anchorElementOwnText, searchCssQuery,
                                         SearchMethod.ByLinkAndDistance, true);
        }
        catch (AmbiguousAnchorElementsException e)
        {
            return new Elements();
        }
    }

    public Elements getElements(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException
    {
        return getElementsExactMatch(document, anchorElementTagName, anchorElementOwnText, searchCssQuery,
                                     SearchMethod.ByLinkAndDistance, false);
    }

    public Elements getElementsBestEffort(Document document, ElementInfo anchorElementInfo,
                                          ElementInfo searchElementInfo)
        throws AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getElements(document, anchorElementInfo, searchElementInfo, SearchMethod.ByLinkAndDistance, true);
        }
        catch (AmbiguousAnchorElementsException e)
        {
            return new Elements();
        }
    }

    public Elements getElements(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        return getElements(document, anchorElementInfo, searchElementInfo, SearchMethod.ByLinkAndDistance, false);
    }

    public Elements getElementsBestEffort(Document document, Elements anchorElements, ElementInfo searchElementInfo)
    {
        try
        {
            return getElements(document, anchorElements, searchElementInfo, SearchMethod.ByLinkAndDistance, true);
        }
        catch (AmbiguousAnchorElementsException e)
        {
            return new Elements();
        }
    }

    public Elements getElements(Document document, Elements anchorElements, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException
    {
        return getElements(document, anchorElements, searchElementInfo, SearchMethod.ByLinkAndDistance, false);
    }

    public Elements getElementsBestEffort(Elements anchorElements, Elements searchElements)
    {
        try
        {
            return getElements(anchorElements, searchElements, SearchMethod.ByLinkAndDistance, true);
        }
        catch (AmbiguousAnchorElementsException e)
        {
            return new Elements();
        }
    }

    public Elements getElements(Elements anchorElements, Elements searchElements)
        throws AmbiguousAnchorElementsException
    {
        return getElements(anchorElements, searchElements, SearchMethod.ByLinkAndDistance, false);
    }

    public Elements getElements(Element anchorElement, Elements searchElements)
    {
        return getElements(anchorElement, searchElements, SearchMethod.ByLinkAndDistance);
    }

    public Elements getElementsByTagNameContainingOwnTextIgnoreCase(Document document, String tagName, String pattern)
    {
        return getElementsByTagNameMatchingOwnText(document, tagName, pattern, new Condition(true, true, false));
    }

    public Elements getElementsByTagNameContainingOwnText(Document document, String tagName, String pattern)
    {
        return getElementsByTagNameMatchingOwnText(document, tagName, pattern,
                                                   new Condition(false, true, false));
    }

    public Elements getElementsByTagNameMatchingOwnTextIgnoreCase(Document document, String tagName, String pattern)
    {
        return getElementsByTagNameMatchingOwnText(document, tagName, pattern,
                                                   new Condition(true, false, false));
    }

    public Elements getElementsByTagNameMatchingOwnText(Document document, String tagName, String pattern)
    {
        return getElementsByTagNameMatchingOwnText(document, tagName, pattern, new Condition());
    }

    public Elements getElementsContainingOwnTextIgnoreCase(Document document, String pattern)
    {
        return getElementsMatchingOwnText(document, pattern, new Condition(true, true, false));
    }

    public Elements getElementsContainingOwnText(Document document, String pattern)
    {
        return getElementsMatchingOwnText(document, pattern,
                                          new Condition(false, true, false));
    }

    public Elements getElementsMatchingOwnTextIgnoreCase(Document document, String pattern)
    {
        return getElementsMatchingOwnText(document, pattern,
                                          new Condition(true, false, false));
    }

    public Elements getElementsMatchingOwnText(Document document, String pattern)
    {
        return getElementsMatchingOwnText(document, pattern, new Condition());
    }
}
