package com.github.tamnguyenbbt.dom;

import com.github.tamnguyenbbt.exception.*;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.time.StopWatch;
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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

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
    private final String ambiguousAnchorMessage = "%s anchor elements found";
    private final String anchorIndexIfMultipleFoundOutOfBoundMessage = "indexIfMultipleFound property of the AnchorElementInfo provided is out of bound";
    private final String ambiguousFoundElementsMessage = "%s elements found";
    private final String ambiguousFoundXpathMessage = "%s xpaths found";
    private final String ambiguousFoundWebElementMessage = "More than one web element found";
    private int searchDepth;
    private int timeoutInMs;
    private Logger logger =  Logger.getLogger(this.getClass().getName());

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

    public Document htmlFileToDocument(String path) throws IOException
    {
        return htmlFileToDocument(path, "UTF-8");
    }

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

    public Document getDocument(String htmlContent)
    {
        return Jsoup.parse(htmlContent);
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

    public Element getElementWithTwoAnchorsBestEffort(Document document, String parentAnchorElementOwnText, String anchorElementOwnText,
                                                      String searchCssQuery)
            throws AmbiguousFoundElementsException
    {
        try
        {
            return getElementWithTwoAnchors(document, parentAnchorElementOwnText, anchorElementOwnText,
                    searchCssQuery, true);
        }
        catch(AmbiguousAnchorElementsException e) { return null; }
    }

    public Element getElementWithTwoAnchors(Document document, String parentAnchorElementOwnText, String anchorElementOwnText,
                                            String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchors(document, parentAnchorElementOwnText, anchorElementOwnText,
                searchCssQuery, false);
    }

    public Element getElementWithTwoAnchorsBestEffort(Document document, String parentAnchorElementOwnText, String anchorElementTagName,
                                                      String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundElementsException
    {
        try
        {
            return getElementWithTwoAnchors(document, parentAnchorElementOwnText, anchorElementTagName,
                    anchorElementOwnText, searchCssQuery, true);
        }
        catch(AmbiguousAnchorElementsException e) { return null; }
    }

    public Element getElementWithTwoAnchors(Document document, String parentAnchorElementOwnText, String anchorElementTagName,
                                            String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchors(document, parentAnchorElementOwnText, anchorElementTagName,
                anchorElementOwnText, searchCssQuery, false);
    }

    public Element getElementWithTwoAnchorsBestEffort(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                      String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundElementsException
    {
        try
        {
            return getElementWithTwoAnchors(document, parentAnchorElementTagName, parentAnchorElementOwnText,
                    anchorElementTagName, anchorElementOwnText, searchCssQuery, true);
        }
        catch(AmbiguousAnchorElementsException e) { return null; }
    }

    public Element getElementWithTwoAnchors(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                            String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchors(document, parentAnchorElementTagName, parentAnchorElementOwnText,
                anchorElementTagName, anchorElementOwnText, searchCssQuery, false);
    }

    public Element getElementWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementOwnText, String anchorElementOwnText,
                                                                String searchCssQuery)
            throws AmbiguousFoundElementsException
    {
        try
        {
            return getElementWithTwoAnchorsExactMatch(document, parentAnchorElementOwnText, anchorElementOwnText,
                    searchCssQuery, true);
        }
        catch(AmbiguousAnchorElementsException e) { return null; }
    }

    public Element getElementWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText, String anchorElementOwnText,
                                                      String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchorsExactMatch(document, parentAnchorElementOwnText, anchorElementOwnText,
                searchCssQuery, false);
    }

    public Element getElementWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementOwnText, String anchorElementTagName,
                                                                String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundElementsException
    {
        try
        {
            return getElementWithTwoAnchorsExactMatch(document, parentAnchorElementOwnText, anchorElementTagName,
                    anchorElementOwnText, searchCssQuery, true);
        }
        catch(AmbiguousAnchorElementsException e) { return null; }
    }

    public Element getElementWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText, String anchorElementTagName,
                                                      String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchorsExactMatch(document, parentAnchorElementOwnText, anchorElementTagName,
                anchorElementOwnText, searchCssQuery, false);
    }

    public Element getElementWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                                String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundElementsException
    {
        try
        {
            return getElementWithTwoAnchorsExactMatch(document, parentAnchorElementTagName, parentAnchorElementOwnText,
                    anchorElementTagName, anchorElementOwnText, searchCssQuery, true);
        }
        catch(AmbiguousAnchorElementsException e) { return null; }
    }

    public Element getElementWithTwoAnchorsExactMatch(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                      String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
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
        catch(AmbiguousAnchorElementsException e) { return null; }
    }

    public Element getElementWithTwoAnchors(Document document, ElementInfo parentAnchorElementInfo,
                                            ElementInfo anchorElementInfo, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
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

    public Element getElementBestEffort(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundElementsException
    {
        try
        {
            return getElement(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, true);
        }
        catch(AmbiguousAnchorElementsException e) { return null;}
    }

    public Element getElement(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElement(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, false);
    }

    public Element getElementBestEffort(Document document, ElementInfo anchorElementInfo, String searchCssQuery)
            throws AmbiguousFoundElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getElement(document, anchorElementInfo, new ElementInfo(searchCssQuery), SearchMethod.ByLinkAndDistance, true);
        }
        catch(AmbiguousAnchorElementsException e) { return null;}
    }

    public Element getElement(Document document, ElementInfo anchorElementInfo, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        return getElement(document, anchorElementInfo, new ElementInfo(searchCssQuery), SearchMethod.ByLinkAndDistance, false);
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
            return getElements(document, anchorElementInfo, new ElementInfo(searchCssQuery), SearchMethod.ByDistance, true);
        }
        catch(AmbiguousAnchorElementsException e){ return new Elements();}
    }

    public Elements getElements(Document document, ElementInfo anchorElementInfo, String searchCssQuery)
            throws AnchorIndexIfMultipleFoundOutOfBoundException, AmbiguousAnchorElementsException
    {
        return getElements(document, anchorElementInfo, new ElementInfo(searchCssQuery), SearchMethod.ByDistance, false);
    }

    public Elements getElementsBestEffort(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
            throws AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getElements(document, anchorElementInfo, searchElementInfo, SearchMethod.ByLinkAndDistance, true);
        }
        catch(AmbiguousAnchorElementsException e){ return new Elements(); }
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
        catch(AmbiguousAnchorElementsException e){}

        return new Elements();
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
        catch(AmbiguousAnchorElementsException e){}

        return new Elements();
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

    public Elements getElementsBestEffort(Document document, String anchorElementOwnText, String searchCssQuery)
    {
        return getElementsBestEffort(document, null, anchorElementOwnText, searchCssQuery);
    }

    public Elements getElements(Document document, String anchorElementOwnText, String searchCssQuery)
            throws  AmbiguousAnchorElementsException
    {
        return getElements(document, null, anchorElementOwnText, searchCssQuery);
    }

    public Elements getElementsBestEffort(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
    {
        try
        {
            return getElements(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, true);
        }
        catch(AmbiguousAnchorElementsException e){}

        return new Elements();
    }

    public Elements getElements(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException
    {
        return getElements(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, false);
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
        return getElementsMatchingOwnText(document, pattern, new Condition(true, false, false));
    }

    public Elements getElementsMatchingOwnText(Document document, String pattern)
    {
        return getElementsMatchingOwnText(document, pattern, new Condition());
    }

    private Elements filterByPattern(Elements elements, String pattern, Condition condition)
    {
        Elements filtered = new Elements();

        if(hasItem(elements))
        {
            for (Element item : elements)
            {
                if(matchElementOwnText(item, pattern, condition))
                {
                    filtered.add(item);
                }
            }
        }

        return filtered;
    }

    private WebElement getWebElementWithTwoAnchors(WebDriver driver, ElementInfo parentAnchorElementInfo, ElementInfo anchorElementInfo, String searchCssQuery, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        String xpath;
        Document document = getActiveDocument(driver);

        if(document == null)
        {
            return null;
        }

        try
        {
            xpath = getXpathWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, bestEffort);
        }
        catch(AmbiguousFoundXpathsException e)
        {
            throw new AmbiguousFoundWebElementsException(ambiguousFoundWebElementMessage);
        }

        return xpath == null ? null : findWebElement(driver, xpath);
    }

    private WebElement getWebElement(WebDriver driver, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchMethod searchMethod, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        try
        {
            return getWebElement(driver, new ElementInfo(anchorElementTagName, anchorElementOwnText),  new ElementInfo(searchCssQuery), searchMethod, bestEffort);
        }
        catch(AnchorIndexIfMultipleFoundOutOfBoundException e) { return null; }
    }

    private WebElement getWebElement(WebDriver driver, ElementInfo anchorElementInfo, ElementInfo searchElementInfo, SearchMethod searchMethod, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        String xpath;
        Document document = getActiveDocument(driver);

        if(document == null)
        {
            return null;
        }

        try
        {
            xpath = getXpath(document, anchorElementInfo, searchElementInfo, searchMethod, bestEffort);
        }
        catch(AmbiguousFoundXpathsException e)
        {
            throw new AmbiguousFoundWebElementsException(ambiguousFoundWebElementMessage);
        }

        return xpath == null ? null : findWebElement(driver, xpath);
    }

    private WebElement findWebElement(WebDriver driver, String xpath) throws AmbiguousFoundWebElementsException
    {
        int pollingEveryInMs = timeoutInMs/10;
        List<WebElement> foundWebElements = findWebElements(driver, By.xpath(xpath), timeoutInMs, pollingEveryInMs);

        if(hasItem(foundWebElements))
        {
            if(foundWebElements.size() > 1)
            {
                throw new AmbiguousFoundWebElementsException(ambiguousFoundWebElementMessage);
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

    private String getXpathWithTwoAnchors(Document document, String parentAnchorElementOwnText, String anchorElementOwnText,
                                             String searchCssQuery, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return getXpathWithTwoAnchors(document, parentAnchorElementOwnText, null,
                anchorElementOwnText, searchCssQuery, bestEffort);
    }

    private String getXpathWithTwoAnchors(Document document, String parentAnchorElementOwnText, String anchorElementTagName,
                                             String anchorElementOwnText, String searchCssQuery, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return getXpathWithTwoAnchors(document,null, parentAnchorElementOwnText,
                anchorElementTagName, anchorElementOwnText, searchCssQuery, bestEffort);
    }

    private String getXpathWithTwoAnchors(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText, String anchorElementTagName,
                                             String anchorElementOwnText, String searchCssQuery, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        String xpath = getXpathWithTwoAnchorsExactMatch(document, parentAnchorElementTagName, parentAnchorElementOwnText,
                anchorElementTagName, anchorElementOwnText, searchCssQuery, bestEffort);

        if (xpath == null)
        {
            ElementInfo parentAnchorElementInfo = new ElementInfo(parentAnchorElementTagName, parentAnchorElementOwnText, true);
            ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText, true);

            try
            {
                xpath = getXpathWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, bestEffort);
            }
            catch(AnchorIndexIfMultipleFoundOutOfBoundException e){ return null; }
        }

        return xpath;
    }

    private String getXpathWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText, String anchorElementOwnText,
                                                       String searchCssQuery, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return getXpathWithTwoAnchorsExactMatch(document, parentAnchorElementOwnText, null,
                anchorElementOwnText, searchCssQuery, bestEffort);
    }

    private String getXpathWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText, String anchorElementTagName,
                                                       String anchorElementOwnText, String searchCssQuery, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return getXpathWithTwoAnchorsExactMatch(document, null, parentAnchorElementOwnText,
                anchorElementTagName, anchorElementOwnText, searchCssQuery, bestEffort);
    }

    private String getXpathWithTwoAnchorsExactMatch(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                    String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        try
        {
            return getXpathWithTwoAnchors(document,
                    new ElementInfo(parentAnchorElementTagName, parentAnchorElementOwnText),
                    new ElementInfo(anchorElementTagName, anchorElementOwnText),
                    searchCssQuery, bestEffort);
        }
        catch(AnchorIndexIfMultipleFoundOutOfBoundException e) { return null; }
    }

    private String getXpathWithTwoAnchors(Document document, ElementInfo parentAnchorElementInfo, ElementInfo anchorElementInfo, String searchCssQuery, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        Elements searchElements = document.select(searchCssQuery);

        if(hasNoItem(searchElements))
        {
            return null;
        }

        Elements anchorElementsByLink = getElements(document, parentAnchorElementInfo, anchorElementInfo, SearchMethod.ByLink, bestEffort);

        if(hasItem(anchorElementsByLink))
        {
            Elements activeAnchorElementsByLink = getActiveAnchorElements(anchorElementsByLink, anchorElementInfo);
            int activeAnchorElementsByLinkCount = activeAnchorElementsByLink.size();

            if(activeAnchorElementsByLinkCount > 1 && !bestEffort)
            {
                throw new AmbiguousAnchorElementsException(String.format(ambiguousAnchorMessage, activeAnchorElementsByLinkCount));
            }

            return getXpath(activeAnchorElementsByLink, searchElements, SearchMethod.ByDistance, bestEffort);
        }
        else
        {
            Elements elementsByLink = getElements(document, parentAnchorElementInfo, new ElementInfo(searchCssQuery), SearchMethod.ByLink, bestEffort);

            if(hasItem(elementsByLink))
            {
                Elements anchorElements = getElementsByTagNameMatchingOwnText(
                        document,
                        anchorElementInfo.tagName,
                        anchorElementInfo.ownText,
                        anchorElementInfo.condition);

                int currentSearchDepth = searchDepth;
                searchDepth = searchDepth * 5;
                Elements filteredAnchors = getElements(elementsByLink, anchorElements, SearchMethod.ByDistance, bestEffort);
                searchDepth = currentSearchDepth;
                return getXpath(document, filteredAnchors, new ElementInfo(searchCssQuery), SearchMethod.ByDistance, bestEffort);
            }
            else
            {
                Elements closestAnchorElements = getElements(document, parentAnchorElementInfo, anchorElementInfo, SearchMethod.ByDistance, bestEffort);
                return getXpath(document, closestAnchorElements, new ElementInfo(searchCssQuery), SearchMethod.ByDistance, bestEffort);
            }
        }
    }

    private String getXpath(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchMethod searchMethod, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        List<String> xpaths = getXpaths(document,anchorElementTagName,anchorElementOwnText, searchCssQuery, searchMethod, bestEffort);
        int xpathCount = xpaths.size();

        if(xpathCount > 1)
        {
            throw new AmbiguousFoundXpathsException(String.format(ambiguousFoundXpathMessage, xpathCount));
        }

        return hasNoItem(xpaths) ? null : xpaths.get(0);
    }

    private String getXpath(Element anchorElement, Elements searchElements, SearchMethod searchMethod)
            throws AmbiguousFoundXpathsException
    {
        List<String> xpaths = getXpaths(anchorElement, searchElements, searchMethod);
        int xpathCount = xpaths.size();

        if(xpathCount > 1)
        {
            throw new AmbiguousFoundXpathsException(String.format(ambiguousFoundXpathMessage, xpathCount));
        }

        return hasNoItem(xpaths) ? null : xpaths.get(0);
    }

    private String getXpath(Elements anchorElements, Elements searchElements, SearchMethod searchMethod, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        List<String> xpaths = getXpaths(anchorElements, searchElements, searchMethod, bestEffort);
        int xpathCount = xpaths.size();

        if(xpathCount > 1)
        {
            throw new AmbiguousFoundXpathsException(String.format(ambiguousFoundXpathMessage, xpathCount));
        }

        return hasNoItem(xpaths) ? null : xpaths.get(0);
    }

    private String getXpath(Document document, Elements anchorElements, ElementInfo searchElementInfo, SearchMethod searchMethod, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        List<String> xpaths = getXpaths(document, anchorElements, searchElementInfo, searchMethod, bestEffort);
        int xpathCount = xpaths.size();

        if(xpathCount > 1)
        {
            throw new AmbiguousFoundXpathsException(String.format(ambiguousFoundXpathMessage, xpathCount));
        }

        return hasNoItem(xpaths) ? null : xpaths.get(0);
    }

    private String getXpath(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo, SearchMethod searchMethod, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        List<String> xpaths = getXpaths(document, anchorElementInfo, searchElementInfo, searchMethod, bestEffort);
        int xpathCount = xpaths.size();

        if(xpathCount > 1)
        {
            throw new AmbiguousFoundXpathsException(String.format(ambiguousFoundXpathMessage, xpathCount));
        }

        return hasNoItem(xpaths) ? null : xpaths.get(0);
    }

    public List<String> getXpaths(Document document, String anchorElementTagName, String anchorElementOwnText,
                                  String searchCssQuery)
            throws AmbiguousAnchorElementsException
    {
        return getXpaths(document, anchorElementTagName, anchorElementOwnText,
                searchCssQuery, SearchMethod.ByLinkAndDistance, false);
    }

    private List<String> getXpaths(Document document, String anchorElementTagName, String anchorElementOwnText,
                                   String searchCssQuery, SearchMethod searchMethod, boolean bestEffort)
            throws AmbiguousAnchorElementsException
    {
        try
        {
            return getXpaths(document, new ElementInfo(anchorElementTagName, anchorElementOwnText), new ElementInfo(searchCssQuery),searchMethod, bestEffort);
        }
        catch(AnchorIndexIfMultipleFoundOutOfBoundException e){ return new ArrayList<>(); }
    }

    private List<String> getXpaths(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo, SearchMethod searchMethod, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        Elements anchorElements = getElements(document, anchorElementInfo);
        Elements activeAnchorElements = getActiveAnchorElements(anchorElements, anchorElementInfo);
        Elements searchElements = getElements(document, searchElementInfo);
        return getXpaths(activeAnchorElements, searchElements, searchMethod, bestEffort);
    }

    private List<String> getXpaths(Document document, Elements anchorElements, ElementInfo searchElementInfo, SearchMethod searchMethod, boolean bestEffort)
            throws AmbiguousAnchorElementsException
    {
        Elements searchElements = getElements(document, searchElementInfo);
        return getXpaths(anchorElements, searchElements, searchMethod, bestEffort);
    }

    private List<String> getXpaths(Elements anchorElements, Elements searchElements, SearchMethod searchMethod, boolean bestEffort)
            throws AmbiguousAnchorElementsException
    {
        List<String> result = new ArrayList<>();

        if(hasItem(anchorElements) && hasItem(searchElements))
        {
            int anchorElementCount = anchorElements.size();

            if(anchorElementCount > 1 && !bestEffort)
            {
                throw new AmbiguousAnchorElementsException(String.format(ambiguousAnchorMessage, anchorElementCount));
            }

            for (Element item : anchorElements)
            {
                result = getXpaths(item, searchElements, searchMethod);

                if(hasItem(result))
                {
                    break;
                }
            }
        }

        return result;
    }

    private List<String> getXpaths(Element anchorElement, Elements searchElements, SearchMethod searchMethod)
    {
        List<String> xpathList = new ArrayList<>();
        List<ElementRecord> finalFoundRecords = getElementRecords(anchorElement, searchElements, searchMethod);

        if(hasNoItem(finalFoundRecords))
        {
            return xpathList;
        }

        for(ElementRecord record : finalFoundRecords)
        {
            String xpath = buildXpath(anchorElement, record);

            if(xpath != null)
            {
                xpathList.add(xpath);
            }
        }

        return new ArrayList<>(new HashSet<>(xpathList));
    }

    private String buildXpath(Element anchorElement, ElementRecord record)
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

    private Element getElementWithTwoAnchors(Document document, String parentAnchorElementOwnText, String anchorElementOwnText,
                                             String searchCssQuery, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchors(document, parentAnchorElementOwnText, null,
                anchorElementOwnText, searchCssQuery, bestEffort);
    }

    private Element getElementWithTwoAnchors(Document document, String parentAnchorElementOwnText, String anchorElementTagName,
                                             String anchorElementOwnText, String searchCssQuery, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchors(document,null, parentAnchorElementOwnText,
                anchorElementTagName, anchorElementOwnText, searchCssQuery, bestEffort);
    }

    private Element getElementWithTwoAnchors(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                             String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        Element element = getElementWithTwoAnchorsExactMatch(document, parentAnchorElementTagName, parentAnchorElementOwnText,
                anchorElementTagName, anchorElementOwnText, searchCssQuery, bestEffort);

        if (element == null)
        {
            ElementInfo parentAnchorElementInfo = new ElementInfo(parentAnchorElementTagName, parentAnchorElementOwnText, true);
            ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText, true);

            try
            {
                element = getElementWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, bestEffort);
            }
            catch(AnchorIndexIfMultipleFoundOutOfBoundException e){ return null;}
        }

        return element;
    }

    private Element getElementWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText, String anchorElementOwnText,
                                                       String searchCssQuery, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchorsExactMatch(document, parentAnchorElementOwnText, null,
                anchorElementOwnText, searchCssQuery, bestEffort);
    }

    private Element getElementWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText, String anchorElementTagName,
                                                       String anchorElementOwnText, String searchCssQuery, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchorsExactMatch(document, null, parentAnchorElementOwnText,
                anchorElementTagName, anchorElementOwnText, searchCssQuery, bestEffort);
    }

    private Element getElementWithTwoAnchorsExactMatch(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                       String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
       try
       {
           return getElementWithTwoAnchors(document,
                   new ElementInfo(parentAnchorElementTagName, parentAnchorElementOwnText),
                   new ElementInfo(anchorElementTagName, anchorElementOwnText),
                   searchCssQuery, bestEffort);
       }
       catch(AnchorIndexIfMultipleFoundOutOfBoundException e){}

       return null;
    }

    private Element getElementWithTwoAnchors(Document document, ElementInfo parentAnchorElementInfo, ElementInfo anchorElementInfo,
                                             String searchCssQuery, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        Elements searchElements = document.select(searchCssQuery);

        if(hasNoItem(searchElements))
        {
            return null;
        }

        Elements anchorElementsByLink = getElements(document, parentAnchorElementInfo, anchorElementInfo, SearchMethod.ByLink, bestEffort);

        if(hasItem(anchorElementsByLink))
        {
            Elements activeAnchorElementsByLink = getActiveAnchorElements(anchorElementsByLink, anchorElementInfo);
            int activeAnchorElementsByLinkCount = activeAnchorElementsByLink.size();

            if(activeAnchorElementsByLinkCount > 1 && !bestEffort)
            {
                throw new AmbiguousAnchorElementsException(String.format(ambiguousAnchorMessage, activeAnchorElementsByLinkCount));
            }

            return getElement(anchorElementsByLink, searchElements, SearchMethod.ByDistance, bestEffort);
        }
        else
        {
            Elements elementsByLink = getElements(document, parentAnchorElementInfo, new ElementInfo(searchCssQuery), SearchMethod.ByLink, bestEffort);

            if(hasItem(elementsByLink))
            {
                Elements anchorElements = getElementsByTagNameMatchingOwnText(
                        document,
                        anchorElementInfo.tagName,
                        anchorElementInfo.ownText,
                        anchorElementInfo.condition);

                int currentSearchDepth = searchDepth;
                searchDepth = searchDepth * 5;
                Elements filteredAnchors = getElements(elementsByLink, anchorElements, SearchMethod.ByDistance, bestEffort);
                searchDepth = currentSearchDepth;
                return getElement(document, filteredAnchors, new ElementInfo(searchCssQuery), SearchMethod.ByDistance, bestEffort);
            }
            else
            {
                Elements closestAnchorElements = getElements(document, parentAnchorElementInfo, anchorElementInfo, SearchMethod.ByDistance, bestEffort);
                return getElement(document, closestAnchorElements, new ElementInfo(searchCssQuery), SearchMethod.ByDistance, bestEffort);
            }
        }
    }

    private Element getElement(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchMethod searchMethod, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        Elements elements = getElements(document,anchorElementTagName,anchorElementOwnText, searchCssQuery, searchMethod, bestEffort);
        int elementCount = elements.size();

        if(elementCount > 1)
        {
            throw new AmbiguousFoundElementsException(String.format(ambiguousFoundElementsMessage, elementCount));
        }

        return hasNoItem(elements) ? null : elements.get(0);
    }

    private Element getElement(Element anchorElement, Elements searchElements, SearchMethod searchMethod)
            throws AmbiguousFoundElementsException
    {
        Elements elements = getElements(anchorElement, searchElements, searchMethod);
        int elementCount = elements.size();

        if(elementCount > 1)
        {
            throw new AmbiguousFoundElementsException(String.format(ambiguousFoundElementsMessage, elementCount));
        }

        return hasNoItem(elements) ? null : elements.get(0);
    }

    private Element getElement(Elements anchorElements, Elements searchElements, SearchMethod searchMethod, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        Elements elements = getElements(anchorElements, searchElements, searchMethod, bestEffort);
        int elementCount = elements.size();

        if(elementCount > 1)
        {
            throw new AmbiguousFoundElementsException(String.format(ambiguousFoundElementsMessage, elementCount));
        }

        return hasNoItem(elements) ? null : elements.get(0);
    }

    private Element getElement(Document document, Elements anchorElements, ElementInfo searchElementInfo, SearchMethod searchMethod, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        Elements elements = getElements(document, anchorElements, searchElementInfo, searchMethod, bestEffort);
        int elementCount = elements.size();

        if(elementCount > 1)
        {
            throw new AmbiguousFoundElementsException(String.format(ambiguousFoundElementsMessage, elementCount));
        }

        return hasNoItem(elements) ? null : elements.get(0);
    }

    private Element getElement(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo, SearchMethod searchMethod, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        Elements elements = getElements(document, anchorElementInfo, searchElementInfo, searchMethod, bestEffort);
        int elementCount = elements.size();

        if(elementCount > 1)
        {
            throw new AmbiguousFoundElementsException(String.format(ambiguousFoundElementsMessage, elementCount));
        }

        return hasNoItem(elements) ? null : elements.get(0);
    }

    private Elements getElements(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchMethod searchMethod, boolean bestEffort)
            throws AmbiguousAnchorElementsException
    {
        try
        {
            return getElements(document, new ElementInfo(anchorElementTagName, anchorElementOwnText), new ElementInfo(searchCssQuery),searchMethod, bestEffort);
        }
        catch(AnchorIndexIfMultipleFoundOutOfBoundException e){}

        return new Elements();
    }

    private Elements getElements(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo, SearchMethod searchMethod, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        Elements anchorElements = getElements(document, anchorElementInfo);
        Elements activeAnchorElements = getActiveAnchorElements(anchorElements, anchorElementInfo);
        Elements searchElements = getElements(document, searchElementInfo);
        return getElements(activeAnchorElements, searchElements, searchMethod, bestEffort);
    }

    private Elements getActiveAnchorElements(Elements anchorElements, ElementInfo anchorElementInfo)
            throws AnchorIndexIfMultipleFoundOutOfBoundException
    {
        Elements activeAnchorElements = new Elements();

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
                throw new AnchorIndexIfMultipleFoundOutOfBoundException(anchorIndexIfMultipleFoundOutOfBoundMessage);
            }
        }

        return activeAnchorElements;
    }

    private Elements getElements(Document document, Elements anchorElements, ElementInfo searchElementInfo, SearchMethod searchMethod, boolean bestEffort)
            throws AmbiguousAnchorElementsException
    {
        Elements searchElements = getElements(document, searchElementInfo);
        return getElements(anchorElements, searchElements, searchMethod, bestEffort);
    }

    private Elements getElements(Elements anchorElements, Elements searchElements, SearchMethod searchMethod, boolean bestEffort)
            throws AmbiguousAnchorElementsException
    {
        Elements result = new Elements();

        if(hasItem(anchorElements) && hasItem(searchElements))
        {
            int anchorElementCount = anchorElements.size();

            if(anchorElementCount > 1 && !bestEffort)
            {
                throw new AmbiguousAnchorElementsException(String.format(ambiguousAnchorMessage, anchorElementCount));
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

        return result;
    }

    private Elements getOwnElements(Document document, String elementTagName, String elementOwnText)
    {
        return elementTagName == null
            ? getElementsMatchingOwnText(document, elementOwnText, new Condition())
            : getElementsByTagNameMatchingOwnText(document, elementTagName, elementOwnText, new Condition());
    }

    private Elements getElements(Document document, ElementInfo elementInfo)
    {
        Elements elements = new Elements();

        if(elementInfo == null)
        {
            return elements;
        }

        if(elementInfo.tagName == null && elementInfo.ownText != null)
        {
            return getElementsMatchingOwnText(document, elementInfo.ownText, elementInfo.condition);
        }
        else if (elementInfo.tagName != null && elementInfo.ownText != null)
        {
            return getElementsByTagNameMatchingOwnText(document, elementInfo.tagName, elementInfo.ownText, elementInfo.condition);
        }
        else
        {
            return getElementsByTagName(document, elementInfo.tagName);
        }
    }

    private Elements getElementsByTagNameMatchingOwnText(Document document, String tagName, String pattern, Condition condition)
    {
        Elements elements = getElementsByTagName(document, tagName);
        return getElementsMatchingOwnText(elements, pattern, condition);
    }

    private Elements getElementsMatchingOwnText(Document document, String pattern, Condition condition)
    {
        Elements elements = document == null ? new Elements() : document.getAllElements();
        return getElementsMatchingOwnText(elements, pattern, condition);
    }

    private Elements getElementsMatchingOwnText(Elements elements, String pattern, Condition condition)
    {
        Elements result = new Elements();

        if(hasItem(elements))
        {
            for (Element item : elements)
            {
                if(matchElementOwnText(item, pattern, condition))
                {
                    result.add(item);
                }
            }
        }

        return result;
    }

    private boolean matchElementOwnText(Element element, String pattern, Condition condition)
    {
        Condition activeCondition = condition;

        if (condition == null)
        {
            activeCondition = new Condition();
        }

        if(element == null || element.ownText() == null || pattern == null)
        {
            return false;
        }

        String elementOwnText = activeCondition.whereIncludingTabsAndSpacesForOwnText ? element.ownText() : element.ownText().replace("\\s+", "");
        String patternWithoutSpaces = activeCondition.whereIncludingTabsAndSpacesForOwnText ? pattern : pattern.replace("\\s+", "");

        if(activeCondition.whereIgnoreCaseForOwnText && activeCondition.whereOwnTextContainingPattern)
        {
            if(elementOwnText.toLowerCase().contains(patternWithoutSpaces.toLowerCase()))
            {
                return true;
            }
        }
        else if (activeCondition.whereIgnoreCaseForOwnText)
        {
            if(elementOwnText.equalsIgnoreCase(patternWithoutSpaces))
            {
                return true;
            }
        }
        else if(activeCondition.whereOwnTextContainingPattern)
        {
            if(elementOwnText.contains(patternWithoutSpaces))
            {
                return true;
            }
        }
        else
        {
            if (elementOwnText.equals(patternWithoutSpaces))
            {
                return true;
            }
        }

        return false;
    }

    private Elements getElementsByTagName(Document document, String tagName)
    {
        Elements result = new Elements();

        if(document != null)
        {
            result = document.select(tagName);
        }

        return result;
    }

    private Elements getElements(Element anchorElement, Elements searchElements, SearchMethod searchMethod)
    {
        Elements result;

        switch (searchMethod)
        {
            case ByDistance:
                result = getClosestElements(anchorElement, searchElements);
                break;
            case ByLink:
                result = getLinkedElements(anchorElement, searchElements);
                break;
            case ByLinkAndDistance:
                result = getElementsByLinkAndShortestDistance(anchorElement, searchElements);
                break;
            default:
                throw new NotImplementedException("Search type not implemented");
        }

        return result;
    }

    private Elements getElementsByLinkAndShortestDistance(Element anchorElement, Elements searchElements)
    {
        Elements linkedElements = getLinkedElements(anchorElement, searchElements);
        Elements result;

        if(hasItem(linkedElements))
        {
            result = linkedElements.size() == 1
                    ? linkedElements
                    : getClosestElements(anchorElement, linkedElements);
        }
        else
        {
            result = getClosestElements(anchorElement, searchElements);
        }

        return result;
    }

    private Elements getClosestElements(Element anchorElement, Elements searchElements)
    {
        List<ElementRecord> closestElementRecords = getClosestElementRecords(anchorElement, searchElements);
        return extractElementsFromElementRecords(closestElementRecords);
    }

    private Elements getLinkedElements(Element anchorElement, Elements searchElements)
    {
        Elements result = new Elements();

        if (hasItem(searchElements) && anchorElement != null)
        {
            Attribute anchorElementForAttribute = getAttributeByNameContainingPattern(anchorElement, "for");

            if(anchorElementForAttribute != null)
            {
                String anchorElementForAttributeValue = anchorElementForAttribute.getValue();

                for (Element item : searchElements)
                {
                    String searchElementId = item.attr("id");
                    String searchElementName = item.attr("name");

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
                throw new NotImplementedException("Search type not implemented");
        }

        return result;
    }

    private List<ElementRecord> getElementRecordsByLinkAndShortestDistance(Element anchorElement, Elements searchElements)
    {
        List<ElementRecord> linkedElementRecords = getLinkedElementRecords(anchorElement, searchElements);
        List<ElementRecord> result;

        if(hasItem(linkedElementRecords))
        {
            result = linkedElementRecords.size() == 1
                    ? linkedElementRecords
                    : getClosestElementRecords(anchorElement, extractElementsFromElementRecords(linkedElementRecords));
        }
        else
        {
            result = getClosestElementRecords(anchorElement, searchElements);
        }

        return result;
    }

    private Elements extractElementsFromElementRecords(List<ElementRecord> elementRecords)
    {
        Elements elements = new Elements();

        if(hasItem(elementRecords))
        {
            for(ElementRecord record : elementRecords)
            {
                elements.add(record.element);
            }
        }

        return elements;
    }

    private List<ElementRecord> getLinkedElementRecords(Element anchorElement, Elements searchElements)
    {
        List<ElementRecord> result = new ArrayList<>();
        List<ElementRecord> searchElementRecords = getElementRecords(anchorElement, searchElements);

        if (hasItem(searchElements) && anchorElement != null)
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
        logger.info(String.format("get element records for ANCHOR '%s'", anchorElement));
        StopWatch stopWatch = StopWatch.createStarted();
        List<ElementRecord> result = new ArrayList<>();

        if(hasNoItem(searchElements) || anchorElement == null)
        {
            result = new ArrayList<>();
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

        stopWatch.stop();
        logger.info(String.format("get element records for ANCHOR '%s' - time in ms: %s", anchorElement, stopWatch.getTime(TimeUnit.MILLISECONDS)));
        return result;
    }

    private ElementRecord getElementRecord(Element anchorElement, Element searchElement, int searchElementIndex)
    {
        logger.info(String.format("get element record for ANCHOR '%s' and SEARCH ELEMENT '%s'", anchorElement, searchElement));
        MapEntry<List<Integer>, List<TreeElement>> matchedElementPositionAndTreePair =
            getContainingTree(anchorElement, searchElement);
        ElementRecord currentFoundElementRecord = null;

        if(matchedElementPositionAndTreePair != null)
        {
            List<TreeElement> tree = matchedElementPositionAndTreePair.getValue();
            List<Integer> matchedElementPosition = matchedElementPositionAndTreePair.getKey();
            TreeElement anchor = getFirstMatchedElementInTree(tree, new TreeElement(anchorElement));

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
            firstFound = getFirstMatchedElementInTree(elementTree, new TreeElement(searchElement));

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

    private TreeElement getFirstMatchedElementInTree(List<TreeElement> elementTree, TreeElement searchElement)
    {
        if(hasItem(elementTree))
        {
            for (TreeElement item : elementTree)
            {
                if (elementEquals(item.element, searchElement.element))
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
            if(elementEquals(currentParent, root))
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

    private boolean elementEquals(Element element1, Element element2)
    {
        Element parent1 = element1;
        Element parent2 = element2;
        int depthCount = 0;

        while (parent1 != null && parent2 != null && depthCount < searchDepth)
        {
            String parent1Tag = getTag(parent1);
            String parent2Tag = getTag(parent2);

            if(!parent1Tag.equals(parent2Tag))
            {
                return false;
            }

            parent1 = parent1.parent();
            parent2 = parent2.parent();
            depthCount++;
        }

        return true;


    }

    private String getTag(Element element)
    {
        String outerHtml = element.outerHtml();
        return outerHtml == null ? null : outerHtml.substring(0, outerHtml.indexOf('>') + 1);
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

    private <T> boolean hasItem(List<T> list)
    {
        return (list != null && !list.isEmpty());
    }

    private <T> boolean hasNoItem(List<T> list)
    {
        return (list == null || list.isEmpty());
    }
}