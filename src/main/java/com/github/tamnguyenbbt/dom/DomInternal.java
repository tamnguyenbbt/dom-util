package com.github.tamnguyenbbt.dom;

import com.github.tamnguyenbbt.exception.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

class DomInternal extends DomCore
{
    protected final String ambiguousAnchorMessage = "%s anchor elements found";
    protected final String anchorIndexIfMultipleFoundOutOfBoundMessage = "indexIfMultipleFound property of the AnchorElementInfo provided is out of bound";
    protected final String ambiguousFoundElementsMessage = "%s elements found";
    protected final String ambiguousFoundXpathMessage = "%s xpaths found";
    protected final String ambiguousFoundWebElementMessage = "More than one web element found";

    protected DomInternal(DomUtilConfig config)
    {
        super(config);
    }

    protected DomInternal()
    {
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

    public Document getDocument(String htmlContent)
    {
        return Jsoup.parse(htmlContent);
    }

    protected WebElement getWebElementWithTwoAnchors(WebDriver driver, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                     String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        WebElement webElement = getWebElementWithTwoAnchorsExactMatch(driver, parentAnchorElementTagName, parentAnchorElementOwnText,
                                                                      anchorElementTagName, anchorElementOwnText, searchCssQuery, bestEffort);

        if(webElement == null)
        {
            try
            {
                return getWebElementWithTwoAnchors(driver, new ElementInfo(parentAnchorElementTagName, parentAnchorElementOwnText, true),
                                                   new ElementInfo(anchorElementTagName, anchorElementOwnText), searchCssQuery, bestEffort);
            }
            catch(AnchorIndexIfMultipleFoundOutOfBoundException e){}
        }

        return webElement;
    }

    protected WebElement getWebElementWithTwoAnchorsExactMatch(WebDriver driver, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                            String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        try
        {
            return getWebElementWithTwoAnchors(driver, new ElementInfo(parentAnchorElementTagName, parentAnchorElementOwnText),
                                               new ElementInfo(anchorElementTagName, anchorElementOwnText), searchCssQuery, bestEffort);
        }
        catch(AnchorIndexIfMultipleFoundOutOfBoundException e){ return null; }
    }

    protected WebElement getWebElementWithTwoAnchors(WebDriver driver, ElementInfo parentAnchorElementInfo,
                                                   ElementInfo anchorElementInfo, String searchCssQuery,
                                                   boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        String xpath = getXpathWithTwoAnchors(driver, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, bestEffort);
        return xpath == null ? null : findWebElement(driver, xpath);
    }

    protected WebElement getWebElement(WebDriver driver, String anchorElementTagName, String anchorElementOwnText,
                                                 String searchCssQuery, SearchMethod searchMethod, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        WebElement webElement = getWebElementExactMatch(driver,anchorElementTagName, anchorElementOwnText,
                                                        searchCssQuery, searchMethod, bestEffort);

        if(webElement == null)
        {
            try
            {
                return getWebElement(driver, new ElementInfo(anchorElementTagName, anchorElementOwnText, true),
                                     new ElementInfo(searchCssQuery), searchMethod, bestEffort);
            }
            catch (AnchorIndexIfMultipleFoundOutOfBoundException e){}
        }

        return webElement;
    }

    protected WebElement getWebElementExactMatch(WebDriver driver, String anchorElementTagName, String anchorElementOwnText,
                                                 String searchCssQuery, SearchMethod searchMethod, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        try
        {
            return getWebElement(driver, new ElementInfo(anchorElementTagName, anchorElementOwnText),
                                 new ElementInfo(searchCssQuery), searchMethod, bestEffort);
        }
        catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
        {
            return null;
        }
    }

    protected WebElement getWebElement(WebDriver driver, ElementInfo anchorElementInfo, ElementInfo searchElementInfo,
                                     SearchMethod searchMethod, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        String xpath = getXpath(driver, anchorElementInfo, searchElementInfo, searchMethod, bestEffort);
        return xpath == null ? null : findWebElement(driver, xpath);
    }

    protected List<WebElement> getWebElementsWithTwoAnchors(WebDriver driver, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                     String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        List<WebElement> webElement = getWebElementsWithTwoAnchorsExactMatch(driver, parentAnchorElementTagName, parentAnchorElementOwnText,
                                                                      anchorElementTagName, anchorElementOwnText, searchCssQuery, bestEffort);

        if(Util.hasNoItem(webElement))
        {
            try
            {
                return getWebElementsWithTwoAnchors(driver, new ElementInfo(parentAnchorElementTagName, parentAnchorElementOwnText, true),
                                                   new ElementInfo(anchorElementTagName, anchorElementOwnText), searchCssQuery, bestEffort);
            }
            catch(AnchorIndexIfMultipleFoundOutOfBoundException e){}
        }

        return webElement;
    }

    protected List<WebElement> getWebElementsWithTwoAnchorsExactMatch(WebDriver driver, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                               String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        try
        {
            return getWebElementsWithTwoAnchors(driver, new ElementInfo(parentAnchorElementTagName, parentAnchorElementOwnText),
                                               new ElementInfo(anchorElementTagName, anchorElementOwnText), searchCssQuery, bestEffort);
        }
        catch(AnchorIndexIfMultipleFoundOutOfBoundException e){ return null; }
    }

    protected List<WebElement> getWebElementsWithTwoAnchors(WebDriver driver, ElementInfo parentAnchorElementInfo,
                                                     ElementInfo anchorElementInfo, String searchCssQuery,
                                                     boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        String xpath = getXpathWithTwoAnchors(driver, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, bestEffort);
        return xpath == null ? null : findWebElements(driver, xpath);
    }

    protected List<WebElement> getWebElements(WebDriver driver, String anchorElementTagName, String anchorElementOwnText,
                                                        String searchCssQuery, SearchMethod searchMethod, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        List<WebElement> webElements = getWebElementsExactMatch(driver, anchorElementTagName, anchorElementOwnText, searchCssQuery, searchMethod, bestEffort);

        if(Util.hasNoItem(webElements))
        {
            try
            {
                return getWebElements(driver, new ElementInfo(anchorElementTagName, anchorElementOwnText, true),
                                      new ElementInfo(searchCssQuery), searchMethod, bestEffort);
            }
            catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
            {
            }
        }

        return webElements;
    }

    protected List<WebElement> getWebElementsExactMatch(WebDriver driver, String anchorElementTagName, String anchorElementOwnText,
                                       String searchCssQuery, SearchMethod searchMethod, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        try
        {
            return getWebElements(driver, new ElementInfo(anchorElementTagName, anchorElementOwnText),
                                 new ElementInfo(searchCssQuery), searchMethod, bestEffort);
        }
        catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
        {
            return new ArrayList<>();
        }
    }

    protected List<WebElement> getWebElements(WebDriver driver, ElementInfo anchorElementInfo, ElementInfo searchElementInfo,
                                       SearchMethod searchMethod, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        String xpath = getXpath(driver, anchorElementInfo, searchElementInfo, searchMethod, bestEffort);
        return xpath == null ? null : findWebElements(driver, xpath);
    }

    protected String getXpathWithTwoAnchors(Document document, String parentAnchorElementOwnText,
                                          String anchorElementOwnText,
                                          String searchCssQuery, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return getXpathWithTwoAnchors(document, parentAnchorElementOwnText, null,
                                      anchorElementOwnText, searchCssQuery, bestEffort);
    }

    protected String getXpathWithTwoAnchors(Document document, String parentAnchorElementOwnText,
                                          String anchorElementTagName,
                                          String anchorElementOwnText, String searchCssQuery, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return getXpathWithTwoAnchors(document, null, parentAnchorElementOwnText,
                                      anchorElementTagName, anchorElementOwnText, searchCssQuery, bestEffort);
    }

    protected String getXpathWithTwoAnchors(Document document, String parentAnchorElementTagName,
                                          String parentAnchorElementOwnText, String anchorElementTagName,
                                          String anchorElementOwnText, String searchCssQuery, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        String xpath = getXpathWithTwoAnchorsExactMatch(document, parentAnchorElementTagName,
                                                        parentAnchorElementOwnText,
                                                        anchorElementTagName, anchorElementOwnText, searchCssQuery,
                                                        bestEffort);

        if (xpath == null)
        {
            ElementInfo parentAnchorElementInfo = new ElementInfo(parentAnchorElementTagName,
                                                                  parentAnchorElementOwnText, true);
            ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText, true);

            try
            {
                xpath = getXpathWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery,
                                               bestEffort);
            }
            catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
            {
                return null;
            }
        }

        return xpath;
    }

    protected String getXpathWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText,
                                                    String anchorElementOwnText,
                                                    String searchCssQuery, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return getXpathWithTwoAnchorsExactMatch(document, parentAnchorElementOwnText, null,
                                                anchorElementOwnText, searchCssQuery, bestEffort);
    }

    protected String getXpathWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText,
                                                    String anchorElementTagName,
                                                    String anchorElementOwnText, String searchCssQuery,
                                                    boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return getXpathWithTwoAnchorsExactMatch(document, null, parentAnchorElementOwnText,
                                                anchorElementTagName, anchorElementOwnText, searchCssQuery, bestEffort);
    }

    protected String getXpathWithTwoAnchorsExactMatch(Document document, String parentAnchorElementTagName,
                                                    String parentAnchorElementOwnText,
                                                    String anchorElementTagName, String anchorElementOwnText,
                                                    String searchCssQuery, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        try
        {
            return getXpathWithTwoAnchors(document,
                                          new ElementInfo(parentAnchorElementTagName, parentAnchorElementOwnText),
                                          new ElementInfo(anchorElementTagName, anchorElementOwnText),
                                          searchCssQuery, bestEffort);
        }
        catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
        {
            return null;
        }
    }

    protected String getXpathWithTwoAnchors(Document document, ElementInfo parentAnchorElementInfo,
                                          ElementInfo anchorElementInfo, String searchCssQuery, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        Elements searchElements = document.select(searchCssQuery);

        if (Util.hasNoItem(searchElements))
        {
            return null;
        }

        Elements anchorElementsByLink = getElements(document, parentAnchorElementInfo, anchorElementInfo, SearchMethod.ByLink, bestEffort);

        if (Util.hasItem(anchorElementsByLink))
        {
            Elements activeAnchorElementsByLink = getActiveAnchorElements(anchorElementsByLink, anchorElementInfo);
            int activeAnchorElementsByLinkCount = activeAnchorElementsByLink.size();

            if (activeAnchorElementsByLinkCount > 1 && !bestEffort)
            {
                throw new AmbiguousAnchorElementsException(String.format(ambiguousAnchorMessage, activeAnchorElementsByLinkCount));
            }

            return getXpath(activeAnchorElementsByLink, searchElements, SearchMethod.ByDistance, bestEffort);
        }

        Elements elementsByLink = getElements(document, parentAnchorElementInfo, new ElementInfo(searchCssQuery), SearchMethod.ByLink, bestEffort);

        if (Util.hasItem(elementsByLink))
        {
            Elements anchorElements = getElementsByTagNameMatchingOwnText(document, anchorElementInfo.tagName, anchorElementInfo.ownText, anchorElementInfo.condition);
            Elements filteredAnchors = getElements(elementsByLink, anchorElements, SearchMethod.ByDistance, bestEffort);
            return getXpath(filteredAnchors, elementsByLink, SearchMethod.ByDistance, bestEffort);
        }
        else
        {
            Elements closestAnchorElements = getElements(document, parentAnchorElementInfo, anchorElementInfo, SearchMethod.ByDistance, bestEffort);
            return getXpath(document, closestAnchorElements, new ElementInfo(searchCssQuery), SearchMethod.ByDistance, bestEffort);
        }
    }

    protected String getXpath(Document document, String anchorElementTagName, String anchorElementOwnText,
                            String searchCssQuery, SearchMethod searchMethod, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        String xpath = getXpathExactMatch(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, searchMethod, bestEffort);

        if(xpath == null)
        {
            try
            {
                xpath = getXpath(document, new ElementInfo(anchorElementTagName, anchorElementOwnText, true),
                        new ElementInfo(searchCssQuery), searchMethod, bestEffort);
            }
            catch(AnchorIndexIfMultipleFoundOutOfBoundException e){}
        }

        return xpath;
    }


    protected String getXpathExactMatch(Document document, String anchorElementTagName, String anchorElementOwnText,
                              String searchCssQuery, SearchMethod searchMethod, boolean bestEffort)
            throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        try
        {
            return getXpath(document, new ElementInfo(anchorElementTagName, anchorElementOwnText),
                    new ElementInfo(searchCssQuery), searchMethod, bestEffort);
        }
        catch(AnchorIndexIfMultipleFoundOutOfBoundException e){ return null; }
    }

    protected String getXpath(Document document, Elements anchorElements, ElementInfo searchElementInfo,
                            SearchMethod searchMethod, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        List<String> xpaths = getXpaths(document, anchorElements, searchElementInfo, searchMethod, bestEffort);
        int xpathCount = xpaths.size();

        if (xpathCount > 1)
        {
            throw new AmbiguousFoundXpathsException(String.format(ambiguousFoundXpathMessage, xpathCount));
        }

        return Util.hasNoItem(xpaths) ? null : xpaths.get(0);
    }

    protected String getXpath(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo,
                            SearchMethod searchMethod, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException,
        AnchorIndexIfMultipleFoundOutOfBoundException
    {
        List<String> xpaths = getXpaths(document, anchorElementInfo, searchElementInfo, searchMethod, bestEffort);
        int xpathCount = xpaths.size();

        if (xpathCount > 1)
        {
            throw new AmbiguousFoundXpathsException(String.format(ambiguousFoundXpathMessage, xpathCount));
        }

        return Util.hasNoItem(xpaths) ? null : xpaths.get(0);
    }

    protected String getXpath(Elements anchorElements, Elements searchElements, SearchMethod searchMethod,
                              boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        List<String> xpaths = getXpaths(anchorElements, searchElements, searchMethod, bestEffort);
        int xpathCount = xpaths.size();

        if (xpathCount > 1)
        {
            throw new AmbiguousFoundXpathsException(String.format(ambiguousFoundXpathMessage, xpathCount));
        }

        return Util.hasNoItem(xpaths) ? null : xpaths.get(0);
    }

    protected String getXpath(Element anchorElement, Elements searchElements, SearchMethod searchMethod)
        throws AmbiguousFoundXpathsException
    {
        List<String> xpaths = getXpaths(anchorElement, searchElements, searchMethod);
        int xpathCount = xpaths.size();

        if (xpathCount > 1)
        {
            throw new AmbiguousFoundXpathsException(String.format(ambiguousFoundXpathMessage, xpathCount));
        }

        return Util.hasNoItem(xpaths) ? null : xpaths.get(0);
    }

    protected List<String> getXpaths(Document document, String anchorElementTagName, String anchorElementOwnText,
                                               String searchCssQuery, SearchMethod searchMethod, boolean bestEffort)
        throws AmbiguousAnchorElementsException
    {
        List<String> xpaths = getXpathsExactMatch(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, searchMethod, bestEffort);

        if(Util.hasNoItem(xpaths))
        {
            try
            {
                return getXpaths(document, new ElementInfo(anchorElementTagName, anchorElementOwnText, true),
                                 new ElementInfo(searchCssQuery), searchMethod, bestEffort);
            }
            catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
            {
                return new ArrayList<>();
            }
        }

        return xpaths;
    }

    protected List<String> getXpathsExactMatch(Document document, String anchorElementTagName, String anchorElementOwnText,
                                   String searchCssQuery, SearchMethod searchMethod, boolean bestEffort)
        throws AmbiguousAnchorElementsException
    {
        try
        {
            return getXpaths(document, new ElementInfo(anchorElementTagName, anchorElementOwnText),
                             new ElementInfo(searchCssQuery), searchMethod, bestEffort);
        }
        catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
        {
            return new ArrayList<>();
        }
    }

    protected List<String> getXpaths(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo,
                                   SearchMethod searchMethod, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        Elements anchorElements = getElements(document, anchorElementInfo);
        Elements activeAnchorElements = getActiveAnchorElements(anchorElements, anchorElementInfo);
        Elements searchElements = getElements(document, searchElementInfo);
        return getXpaths(activeAnchorElements, searchElements, searchMethod, bestEffort);
    }

    protected List<String> getXpaths(Document document, Elements anchorElements, ElementInfo searchElementInfo,
                                   SearchMethod searchMethod, boolean bestEffort)
        throws AmbiguousAnchorElementsException
    {
        Elements searchElements = getElements(document, searchElementInfo);
        return getXpaths(anchorElements, searchElements, searchMethod, bestEffort);
    }

    protected List<String> getXpaths(Elements anchorElements, Elements searchElements, SearchMethod searchMethod, boolean bestEffort)
        throws AmbiguousAnchorElementsException
    {
        List<String> result = new ArrayList<>();

        if (Util.hasItem(anchorElements) && Util.hasItem(searchElements))
        {
            int anchorElementCount = anchorElements.size();

            if (anchorElementCount > 1 && !bestEffort)
            {
                throw new AmbiguousAnchorElementsException(String.format(ambiguousAnchorMessage, anchorElementCount));
            }

            for (Element item : anchorElements)
            {
                result = getXpaths(item, searchElements, searchMethod);

                if (Util.hasItem(result))
                {
                    break;
                }
            }
        }

        return result;
    }

    protected String getXpathWithTwoAnchors(WebDriver driver, ElementInfo parentAnchorElementInfo,
                                            ElementInfo anchorElementInfo, String searchCssQuery,
                                            boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        String xpath;
        Document document = getActiveDocument(driver);

        if (document == null)
        {
            return null;
        }

        try
        {
            xpath = getXpathWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, bestEffort);
        }
        catch (AmbiguousFoundXpathsException e)
        {
            throw new AmbiguousFoundWebElementsException(ambiguousFoundWebElementMessage);
        }

        return xpath;
    }

    protected List<String> getXpaths(Element anchorElement, Elements searchElements, SearchMethod searchMethod)
    {
        List<String> xpathList = new ArrayList<>();
        ElementRecords finalFoundRecords = getElementRecords(anchorElement, searchElements, searchMethod);

        if (Util.hasNoItem(finalFoundRecords))
        {
            return xpathList;
        }

        for (ElementRecord record : finalFoundRecords)
        {
            String xpath = record.buildXpath(config);

            if (xpath != null)
            {
                xpathList.add(xpath);
            }
        }

        return new ArrayList<>(new HashSet<>(xpathList));
    }

    protected Element getElementWithTwoAnchors(Document document, String parentAnchorElementOwnText,
                                             String anchorElementOwnText,
                                             String searchCssQuery, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchors(document, parentAnchorElementOwnText, null,
                                        anchorElementOwnText, searchCssQuery, bestEffort);
    }

    protected Element getElementWithTwoAnchors(Document document, String parentAnchorElementOwnText,
                                             String anchorElementTagName,
                                             String anchorElementOwnText, String searchCssQuery, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchors(document, null, parentAnchorElementOwnText,
                                        anchorElementTagName, anchorElementOwnText, searchCssQuery, bestEffort);
    }

    protected Element getElementWithTwoAnchors(Document document, String parentAnchorElementTagName,
                                             String parentAnchorElementOwnText,
                                             String anchorElementTagName, String anchorElementOwnText,
                                             String searchCssQuery, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        Element element = getElementWithTwoAnchorsExactMatch(document, parentAnchorElementTagName,
                                                             parentAnchorElementOwnText,
                                                             anchorElementTagName, anchorElementOwnText, searchCssQuery,
                                                             bestEffort);

        if (element == null)
        {
            ElementInfo parentAnchorElementInfo = new ElementInfo(parentAnchorElementTagName,
                                                                  parentAnchorElementOwnText, true);
            ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText, true);

            try
            {
                element = getElementWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, bestEffort);
            }
            catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
            {
                return null;
            }
        }

        return element;
    }

    protected Element getElementWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText,
                                                       String anchorElementOwnText,
                                                       String searchCssQuery, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchorsExactMatch(document, parentAnchorElementOwnText, null,
                                                  anchorElementOwnText, searchCssQuery, bestEffort);
    }

    protected Element getElementWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText,
                                                       String anchorElementTagName,
                                                       String anchorElementOwnText, String searchCssQuery,
                                                       boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchorsExactMatch(document, null, parentAnchorElementOwnText,
                                                  anchorElementTagName, anchorElementOwnText, searchCssQuery,
                                                  bestEffort);
    }

    protected Element getElementWithTwoAnchorsExactMatch(Document document, String parentAnchorElementTagName,
                                                       String parentAnchorElementOwnText,
                                                       String anchorElementTagName, String anchorElementOwnText,
                                                       String searchCssQuery, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        try
        {
            return getElementWithTwoAnchors(document,
                                            new ElementInfo(parentAnchorElementTagName, parentAnchorElementOwnText),
                                            new ElementInfo(anchorElementTagName, anchorElementOwnText),
                                            searchCssQuery, bestEffort);
        }
        catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
        {
            return null;
        }
    }

    protected Element getElementWithTwoAnchors(Document document, ElementInfo parentAnchorElementInfo,
                                             ElementInfo anchorElementInfo,
                                             String searchCssQuery, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        Elements searchElements = document.select(searchCssQuery);

        if (Util.hasNoItem(searchElements))
        {
            return null;
        }

        Elements anchorElementsByLink = getElements(document, parentAnchorElementInfo, anchorElementInfo, SearchMethod.ByLink, bestEffort);

        if (Util.hasItem(anchorElementsByLink))
        {
            Elements activeAnchorElementsByLink = getActiveAnchorElements(anchorElementsByLink, anchorElementInfo);
            int activeAnchorElementsByLinkCount = activeAnchorElementsByLink.size();

            if (activeAnchorElementsByLinkCount > 1 && !bestEffort)
            {
                throw new AmbiguousAnchorElementsException(
                    String.format(ambiguousAnchorMessage, activeAnchorElementsByLinkCount));
            }

            return getElement(anchorElementsByLink, searchElements, SearchMethod.ByDistance, bestEffort);
        }

        Elements elementsByLink = getElements(document, parentAnchorElementInfo, new ElementInfo(searchCssQuery),
                SearchMethod.ByLink, bestEffort);

        if (Util.hasItem(elementsByLink))
        {
            Elements anchorElements = getElementsByTagNameMatchingOwnText(
                    document, anchorElementInfo.tagName, anchorElementInfo.ownText, anchorElementInfo.condition);
            Elements filteredAnchors = getElements(elementsByLink, anchorElements, SearchMethod.ByDistance, bestEffort);
            return getElement(filteredAnchors, elementsByLink, SearchMethod.ByDistance, bestEffort);
        }
        else
        {
            Elements closestAnchorElements = getElements(document, parentAnchorElementInfo, anchorElementInfo,
                    SearchMethod.ByDistance, bestEffort);
            return getElement(document, closestAnchorElements, new ElementInfo(searchCssQuery),
                    SearchMethod.ByDistance, bestEffort);
        }
    }

    protected Element getElement(Document document, String anchorElementTagName, String anchorElementOwnText,
                                           String searchCssQuery, SearchMethod searchMethod, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        Element element = getElementExactMatch(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, searchMethod, bestEffort);

        if(element == null)
        {
            try
            {
                return getElement(document, new ElementInfo(anchorElementTagName, anchorElementOwnText, true),
                                  new ElementInfo(searchCssQuery), searchMethod, bestEffort);
            }
            catch(AnchorIndexIfMultipleFoundOutOfBoundException e){}
        }

        return element;
    }

    protected Element getElementExactMatch(Document document, String anchorElementTagName, String anchorElementOwnText,
                                           String searchCssQuery, SearchMethod searchMethod, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        Elements elements = getElementsExactMatch(document, anchorElementTagName, anchorElementOwnText, searchCssQuery,
                                                  searchMethod, bestEffort);
        int elementCount = elements.size();

        if (elementCount > 1)
        {
            throw new AmbiguousFoundElementsException(String.format(ambiguousFoundElementsMessage, elementCount));
        }

        return Util.hasNoItem(elements) ? null : elements.get(0);
    }

    protected Element getElement(Document document, Elements anchorElements, ElementInfo searchElementInfo,
                                   SearchMethod searchMethod, boolean bestEffort)
    throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        Elements elements = getElements(document, anchorElements, searchElementInfo, searchMethod, bestEffort);
        int elementCount = elements.size();

        if (elementCount > 1)
        {
            throw new AmbiguousFoundElementsException(String.format(ambiguousFoundElementsMessage, elementCount));
        }

        return Util.hasNoItem(elements) ? null : elements.get(0);
    }

    protected Element getElement(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo, SearchMethod searchMethod, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        Elements elements = getElements(document, anchorElementInfo, searchElementInfo, searchMethod, bestEffort);
        int elementCount = elements.size();

        if (elementCount > 1)
        {
            throw new AmbiguousFoundElementsException(String.format(ambiguousFoundElementsMessage, elementCount));
        }

        return Util.hasNoItem(elements) ? null : elements.get(0);
    }

    protected Element getElement(Elements anchorElements, Elements searchElements, SearchMethod searchMethod, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        Elements elements = getElements(anchorElements, searchElements, searchMethod, bestEffort);
        int elementCount = elements.size();

        if (elementCount > 1)
        {
            throw new AmbiguousFoundElementsException(String.format(ambiguousFoundElementsMessage, elementCount));
        }

        return Util.hasNoItem(elements) ? null : elements.get(0);
    }

    protected Element getElement(Element anchorElement, Elements searchElements, SearchMethod searchMethod)
        throws AmbiguousFoundElementsException
    {
        Elements elements = getElements(anchorElement, searchElements, searchMethod);
        int elementCount = elements.size();

        if (elementCount > 1)
        {
            throw new AmbiguousFoundElementsException(String.format(ambiguousFoundElementsMessage, elementCount));
        }

        return Util.hasNoItem(elements) ? null : elements.get(0);
    }

    protected Elements getElements(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchMethod searchMethod, boolean bestEffort)
        throws AmbiguousAnchorElementsException
    {
        Elements elements = getElementsExactMatch(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, searchMethod, bestEffort);

        if(Util.hasNoItem(elements))
        {
            ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText, true);

            try
            {
                return getElements(document, anchorElementInfo, new ElementInfo(searchCssQuery), searchMethod, bestEffort);
            }
            catch (AnchorIndexIfMultipleFoundOutOfBoundException e){}
        }

        return elements;
    }

    protected Elements getElementsExactMatch(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchMethod searchMethod, boolean bestEffort)
        throws AmbiguousAnchorElementsException
    {
        try
        {
            return getElements(document, new ElementInfo(anchorElementTagName, anchorElementOwnText), new ElementInfo(searchCssQuery), searchMethod, bestEffort);
        }
        catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
        {
            return new Elements();
        }
    }

    protected Elements getElements(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo, SearchMethod searchMethod, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        Elements anchorElements = getElements(document, anchorElementInfo);
        Elements activeAnchorElements = getActiveAnchorElements(anchorElements, anchorElementInfo);
        Elements searchElements = getElements(document, searchElementInfo);
        return getElements(activeAnchorElements, searchElements, searchMethod, bestEffort);
    }

    protected Elements getElements(Document document, Elements anchorElements, ElementInfo searchElementInfo, SearchMethod searchMethod, boolean bestEffort)
        throws AmbiguousAnchorElementsException
    {
        Elements searchElements = getElements(document, searchElementInfo);
        return getElements(anchorElements, searchElements, searchMethod, bestEffort);
    }

    protected Elements getElements(Elements anchorElements, Elements searchElements, SearchMethod searchMethod, boolean bestEffort)
        throws AmbiguousAnchorElementsException
    {
        Elements result = new Elements();

        if (Util.hasItem(anchorElements) && Util.hasItem(searchElements))
        {
            int anchorElementCount = anchorElements.size();

            if (anchorElementCount > 1 && !bestEffort)
            {
                throw new AmbiguousAnchorElementsException(String.format(ambiguousAnchorMessage, anchorElementCount));
            }

            for (Element item : anchorElements)
            {
                result = getElements(item, searchElements, searchMethod);

                if (Util.hasItem(result))
                {
                    break;
                }
            }
        }

        return result;
    }

    protected Elements getElements(Document document, ElementInfo elementInfo)
    {
        Elements elements = new Elements();

        if (elementInfo == null)
        {
            return elements;
        }

        if (elementInfo.tagName == null && elementInfo.ownText != null)
        {
            return getElementsMatchingOwnText(document, elementInfo.ownText, elementInfo.condition);
        }
        else if (elementInfo.tagName != null && elementInfo.ownText != null)
        {
            return getElementsByTagNameMatchingOwnText(document, elementInfo.tagName, elementInfo.ownText,
                                                       elementInfo.condition);
        }
        else
        {
            return getElementsByTagName(document, elementInfo.tagName);
        }
    }

    protected Elements getElementsByTagNameMatchingOwnText(Document document, String tagName, String pattern, Condition condition)
    {
        Elements elements = getElementsByTagName(document, tagName);
        return getElementsMatchingOwnText(elements, pattern, condition);
    }

    protected Elements getElementsMatchingOwnText(Document document, String pattern, Condition condition)
    {
        Elements elements = document == null ? new Elements() : document.getAllElements();
        return getElementsMatchingOwnText(elements, pattern, condition);
    }

    protected Elements getElementsByTagName(Document document, String tagName)
    {
        Elements result = new Elements();

        if (document != null)
        {
            result = document.select(tagName);
        }

        return result;
    }

    private String getXpath(WebDriver driver, ElementInfo anchorElementInfo, ElementInfo searchElementInfo,
                                              SearchMethod searchMethod, boolean bestEffort)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        String xpath;
        Document document = getActiveDocument(driver);

        if (document == null)
        {
            return null;
        }

        try
        {
            xpath = getXpath(document, anchorElementInfo, searchElementInfo, searchMethod, bestEffort);
        }
        catch (AmbiguousFoundXpathsException e)
        {
            throw new AmbiguousFoundWebElementsException(ambiguousFoundWebElementMessage);
        }

        return xpath;
    }

    private WebElement findWebElement(WebDriver driver, String xpath) throws AmbiguousFoundWebElementsException
    {
        List<WebElement> foundWebElements = findWebElements(driver, xpath);
        int pollingEveryInMs = config.webDriverTimeoutInMilliseconds / 10;

        if (Util.hasItem(foundWebElements))
        {
            if (foundWebElements.size() > 1)
            {
                throw new AmbiguousFoundWebElementsException(ambiguousFoundWebElementMessage);
            }

            return findWebElement(driver, By.xpath(xpath), config.webDriverTimeoutInMilliseconds, pollingEveryInMs);
        }

        return null;
    }

    private List<WebElement> findWebElements(WebDriver driver, String xpath)
    {
        int pollingEveryInMs = config.webDriverTimeoutInMilliseconds / 10;
        return findWebElements(driver, By.xpath(xpath), config.webDriverTimeoutInMilliseconds, pollingEveryInMs);
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

    private Elements getActiveAnchorElements(Elements anchorElements, ElementInfo anchorElementInfo)
        throws AnchorIndexIfMultipleFoundOutOfBoundException
    {
        Elements activeAnchorElements = new Elements();

        if (anchorElementInfo != null)
        {
            if (anchorElementInfo.indexIfMultipleFound < 0 || anchorElements.isEmpty())
            {
                activeAnchorElements = anchorElements;
            }
            else if (anchorElementInfo.indexIfMultipleFound < anchorElements.size())
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

    private Elements getElementsMatchingOwnText(Elements elements, String pattern, Condition condition)
    {
        Elements result = new Elements();

        if (Util.hasItem(elements))
        {
            for (Element item : elements)
            {
                if (new TreeElement(item).matchElementOwnText(pattern, condition))
                {
                    result.add(item);
                }
            }
        }

        return result;
    }
}
