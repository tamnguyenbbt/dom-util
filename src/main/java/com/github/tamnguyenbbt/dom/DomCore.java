package com.github.tamnguyenbbt.dom;

import com.github.tamnguyenbbt.exception.*;
import org.apache.commons.lang3.NotImplementedException;
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
import java.util.*;
import java.util.concurrent.TimeUnit;

class DomCore
{
    protected final String ambiguousAnchorMessage = "%s anchor elements found\n%s";
    protected final String anchorIndexIfMultipleFoundOutOfBoundMessage = "indexIfMultipleFound property of the AnchorElementInfo provided is out of bound";
    protected final String ambiguousFoundElementsMessage = "%s elements found\n%s";
    protected final String ambiguousFoundXpathMessage = "%s xpaths found\n%s";
    protected final String ambiguousFoundWebElementMessage = "%s web elements found\n%s";

    protected DomUtilConfig config;

    protected DomCore(DomUtilConfig config)
    {
        this.config = config == null ? new DomUtilConfig() : config;

        if(Util.hasNoItem(this.config.xpathBuildOptions))
        {
            this.config.xpathBuildOptions = new ArrayList<>();
            this.config.xpathBuildOptions.add(XpathBuildOption.AttachId);
            this.config.xpathBuildOptions.add(XpathBuildOption.AttachName);
            this.config.xpathBuildOptions.add(XpathBuildOption.IncludeTagIndex);
        }

        if(this.config.webDriverTimeoutInMilliseconds <= 0)
        {
            this.config.webDriverTimeoutInMilliseconds = 2000;
        }
    }

    protected DomCore()
    {
        config = new DomUtilConfig();
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

    public Document removeTagsByAnyMatchedAttribute(Document document, List<Attribute> matchedAttributes)
    {
        Elements allElements = document.getAllElements();

        allElements.forEach(x -> {
           if(new TreeElement(x).matchAny(matchedAttributes))
           {
               x.remove();
           }
        });

        return document;
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

    /** region get web element from driver by two anchors
     */
    protected WebElement getWebElementWithTwoAnchors(WebDriver driver, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                     String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        WebElement webElement = getWebElementWithTwoAnchorsExactMatch(driver, parentAnchorElementTagName, parentAnchorElementOwnText,
                                                                      anchorElementTagName, anchorElementOwnText, searchCssQuery, searchOption);

        if (webElement == null)
        {
            ElementInfo parentAnchorElementInfo = new ElementInfo(parentAnchorElementTagName, parentAnchorElementOwnText, true);
            ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText, true);

            try
            {
                webElement = getWebElementWithTwoAnchors(driver, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, searchOption);
            }
            catch (AnchorIndexIfMultipleFoundOutOfBoundException e){}
        }

        return webElement;
    }

    protected WebElement getWebElementWithTwoAnchorsExactMatch(WebDriver driver, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                               String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        try
        {
            ElementInfo parentAnchorElementInfo = new ElementInfo(parentAnchorElementTagName,  parentAnchorElementOwnText);
            ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText);
            return getWebElementWithTwoAnchors(driver, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, searchOption);
        }
        catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
        {
            return null;
        }
    }

    protected WebElement getWebElementWithTwoAnchors(WebDriver driver, ElementInfo parentAnchorElementInfo, ElementInfo anchorElementInfo, String searchCssQuery, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        List<WebElement> webElements = getWebElementsWithTwoAnchors(driver, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, searchOption);
        int webElementCount = webElements.size();

        if (webElementCount > 1)
        {
            throw new AmbiguousFoundWebElementsException(String.format(ambiguousFoundWebElementMessage, webElementCount, objectsToString(webElements)));
        }

        return Util.hasNoItem(webElements) ? null : webElements.get(0);
    }

    /**
     * region get web element from driver by anchor
     */
    protected WebElement getWebElement(WebDriver driver, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        WebElement webElement = getWebElementExactMatch(driver, anchorElementTagName, anchorElementOwnText, searchCssQuery, searchMethod, searchOption);

        if (webElement == null)
        {
            ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText, true);

            try
            {
                webElement = getWebElement(driver, anchorElementInfo, new ElementInfo(searchCssQuery), searchMethod, searchOption);
            }
            catch (AnchorIndexIfMultipleFoundOutOfBoundException e){}
        }

        return webElement;
    }

    protected WebElement getWebElementExactMatch(WebDriver driver, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        try
        {
            ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText);
            return getWebElement(driver, anchorElementInfo, new ElementInfo(searchCssQuery), searchMethod, searchOption);
        }
        catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
        {
            return null;
        }
    }

    protected WebElement getWebElement(WebDriver driver, ElementInfo anchorElementInfo, ElementInfo searchElementInfo, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        List<WebElement> webElements =  getWebElements(driver, anchorElementInfo, searchElementInfo, searchMethod, searchOption);
        int webElementCount = webElements.size();

        if (webElementCount > 1)
        {
            throw new AmbiguousFoundWebElementsException(String.format(ambiguousFoundWebElementMessage, webElementCount, objectsToString(webElements)));
        }

        return Util.hasNoItem(webElements) ? null : webElements.get(0);
    }

    /**
     * region get web elements from driver by two anchors
     */
    protected List<WebElement> getWebElementsWithTwoAnchors(WebDriver driver, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                            String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchOption searchOption)
        throws AmbiguousAnchorElementsException
    {
        List<WebElement> webElements = getWebElementsWithTwoAnchorsExactMatch(driver, parentAnchorElementTagName, parentAnchorElementOwnText,
                                                                              anchorElementTagName, anchorElementOwnText, searchCssQuery, searchOption);

        if(Util.hasNoItem(webElements))
        {
            try
            {
                ElementInfo parentAnchorElementInfo = new ElementInfo(parentAnchorElementTagName, parentAnchorElementOwnText, true);
                ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText, true);
                webElements = getWebElementsWithTwoAnchors(driver, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, searchOption);
            }
            catch(AnchorIndexIfMultipleFoundOutOfBoundException e){}
        }

        return webElements;
    }

    protected List<WebElement> getWebElementsWithTwoAnchorsExactMatch(WebDriver driver, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                                      String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchOption searchOption)
        throws AmbiguousAnchorElementsException
    {
        try
        {
            ElementInfo parentAnchorElementInfo = new ElementInfo(parentAnchorElementTagName, parentAnchorElementOwnText);
            ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText);
            return getWebElementsWithTwoAnchors(driver, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, searchOption);
        }
        catch(AnchorIndexIfMultipleFoundOutOfBoundException e){ return new ArrayList<>(); }
    }

    protected List<WebElement> getWebElementsWithTwoAnchors(WebDriver driver, ElementInfo parentAnchorElementInfo, ElementInfo anchorElementInfo, String searchCssQuery, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        Document document = getActiveDocument(driver);

        if (document == null)
        {
            return null;
        }

        List<String> xpaths = getXpathsWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, searchOption);
        return findWebElements(driver, xpaths);
    }

    /**
     * region get web elements from driver by anchor
     */
    protected List<WebElement> getWebElements(WebDriver driver, String anchorElementTagName, String anchorElementOwnText,
                                              String searchCssQuery, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException
    {
        List<WebElement> webElements = getWebElementsExactMatch(driver, anchorElementTagName, anchorElementOwnText, searchCssQuery, searchMethod, searchOption);

        if(Util.hasNoItem(webElements))
        {
            try
            {
                ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText, true);
                return getWebElements(driver, anchorElementInfo, new ElementInfo(searchCssQuery), searchMethod, searchOption);
            }
            catch (AnchorIndexIfMultipleFoundOutOfBoundException e) {}
        }

        return webElements;
    }

    protected List<WebElement> getWebElementsExactMatch(WebDriver driver, String anchorElementTagName, String anchorElementOwnText,
                                                        String searchCssQuery, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException
    {
        try
        {
            ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText);
            return getWebElements(driver, anchorElementInfo, new ElementInfo(searchCssQuery), searchMethod, searchOption);
        }
        catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
        {
            return new ArrayList<>();
        }
    }

    protected List<WebElement> getWebElements(WebDriver driver, ElementInfo anchorElementInfo, ElementInfo searchElementInfo, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        Document document = getActiveDocument(driver);

        if (document == null)
        {
            return null;
        }

        List<String> xpaths = getXpaths(document, anchorElementInfo, searchElementInfo, searchMethod, searchOption);
        return findWebElements(driver, xpaths);
    }

    /**
     * region get xpath from document by two anchors
     */
    protected String getXpathWithTwoAnchors(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                            String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        String xpath = getXpathWithTwoAnchorsExactMatch(document, parentAnchorElementTagName, parentAnchorElementOwnText,
                                                        anchorElementTagName, anchorElementOwnText, searchCssQuery, searchOption);

        if (xpath == null)
        {
            ElementInfo parentAnchorElementInfo = new ElementInfo(parentAnchorElementTagName, parentAnchorElementOwnText, true);
            ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText, true);

            try
            {
                xpath = getXpathWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, searchOption);
            }
            catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
            {
            }
        }

        return xpath;
    }

    protected String getXpathWithTwoAnchorsExactMatch(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                      String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        try
        {
            ElementInfo parentAnchorElementInfo = new ElementInfo(parentAnchorElementTagName,  parentAnchorElementOwnText);
            ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText);
            return getXpathWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, searchOption);
        }
        catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
        {
            return null;
        }
    }

    protected String getXpathWithTwoAnchors(Document document, ElementInfo parentAnchorElementInfo,
                                            ElementInfo anchorElementInfo, String searchCssQuery, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException, AmbiguousFoundXpathsException
    {
        List<String> xpaths = getXpathsWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, searchOption);
        int xpathCount = xpaths.size();

        if (xpathCount > 1)
        {
            throw new AmbiguousFoundXpathsException(String.format(ambiguousFoundXpathMessage, xpathCount, objectsToString(xpaths)));
        }

        return Util.hasNoItem(xpaths) ? null : xpaths.get(0);
    }

    /**
     * region get xpath from document by anchor
     */
    protected String getXpath(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        String xpath = getXpathExactMatch(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, searchMethod, searchOption);

        if(xpath == null)
        {
            try
            {
                ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText, true);
                xpath = getXpath(document, anchorElementInfo, new ElementInfo(searchCssQuery), searchMethod, searchOption);
            }
            catch(AnchorIndexIfMultipleFoundOutOfBoundException e){}
        }

        return xpath;
    }

    protected String getXpathExactMatch(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        try
        {
            ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText);
            return getXpath(document, anchorElementInfo, new ElementInfo(searchCssQuery), searchMethod, searchOption);
        }
        catch(AnchorIndexIfMultipleFoundOutOfBoundException e)
        {
            return null;
        }
    }

    protected String getXpath(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        Elements anchorElements = getElements(document, anchorElementInfo);
        Elements activeAnchorElements = getActiveAnchorElements(anchorElements, anchorElementInfo);
        Elements searchElements = getElements(document, searchElementInfo);
        return getXpath(activeAnchorElements, searchElements, searchMethod, searchOption);
    }

    protected String getXpath(Document document, Elements anchorElements, ElementInfo searchElementInfo, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        Elements searchElements = getElements(document, searchElementInfo);
        return getXpath(anchorElements, searchElements, searchMethod, searchOption);
    }

    protected String getXpath(Element anchorElement, Elements searchElements, SearchMethod searchMethod)
        throws AmbiguousFoundXpathsException
    {
        Elements anchorElements = new Elements();
        anchorElements.add(anchorElement);

        try
        {
            return getXpath(anchorElements, searchElements, searchMethod, SearchOption.ErrorOnAmbiguousAnchors);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    protected String getXpath(Elements anchorElements, Elements searchElements, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        List<String> xpaths = getXpaths(anchorElements, searchElements, searchMethod, searchOption);
        int xpathCount = xpaths.size();

        if (xpathCount > 1)
        {
            throw new AmbiguousFoundXpathsException(String.format(ambiguousFoundElementsMessage, xpathCount, objectsToString(xpaths)));
        }

        return Util.hasNoItem(xpaths) ? null : xpaths.get(0);
    }

    /**
     * region get xpaths from document by two anchors
     */
    public List<List<String>> getAllPossibleXpathsWithTwoAnchors(Document document, String parentAnchorElementOwnText,
                                                                 String anchorElementOwnText, String searchCssQuery)
    {
        return getAllPossibleXpathsWithTwoAnchors(document, parentAnchorElementOwnText, null, anchorElementOwnText, searchCssQuery);
    }

    public List<List<String>> getAllPossibleXpathsWithTwoAnchors(Document document, String parentAnchorElementOwnText,
                                                                 String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
    {
        return getAllPossibleXpathsWithTwoAnchors(document, null, parentAnchorElementOwnText, anchorElementTagName, anchorElementOwnText, searchCssQuery);
    }

    public List<List<String>> getAllPossibleXpathsWithTwoAnchors(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                                 String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
    {
        List<List<String>> xpaths = getAllPossibleXpathsWithTwoAnchorsExactMatch(document, parentAnchorElementTagName, parentAnchorElementOwnText,
                anchorElementTagName, anchorElementOwnText, searchCssQuery);

        if (Util.hasNoItem(xpaths))
        {
            ElementInfo parentAnchorElementInfo = new ElementInfo(parentAnchorElementTagName, parentAnchorElementOwnText, true);
            ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText, true);

            try
            {
                xpaths = getAllPossibleXpathsWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery);
            }
            catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
            {
                return new ArrayList<>();
            }
        }

        return xpaths;
    }

    public List<List<String>> getAllPossibleXpathsWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText,
                                                                          String anchorElementOwnText, String searchCssQuery)
    {
        return getAllPossibleXpathsWithTwoAnchorsExactMatch(document, parentAnchorElementOwnText, null, anchorElementOwnText, searchCssQuery);
    }

    public List<List<String>> getAllPossibleXpathsWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText,
                                                                           String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
    {
        return getAllPossibleXpathsWithTwoAnchorsExactMatch(document, null, parentAnchorElementOwnText, anchorElementTagName, anchorElementOwnText, searchCssQuery);
    }

    public List<List<String>> getAllPossibleXpathsWithTwoAnchorsExactMatch(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                                           String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
    {
        try
        {
            ElementInfo parentAnchorElementInfo = new ElementInfo(parentAnchorElementTagName, parentAnchorElementOwnText);
            ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText);
            return getAllPossibleXpathsWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery);
        }
        catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
        {
            return new ArrayList<>();
        }
    }

    public List<List<String>> getAllPossibleXpathsWithTwoAnchors(Document document, ElementInfo parentAnchorElementInfo, ElementInfo anchorElementInfo, String searchCssQuery)
            throws AnchorIndexIfMultipleFoundOutOfBoundException
    {
        List<List<String>> result = new ArrayList<>();
        try
        {
            TreeElements treeElements = getTreeElementsWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, SearchOption.GetAllIgnoreAmbiguousAnchors);
            result = treeElementsToXpathsGroupingByAnchor(treeElements);
        }
        catch(AmbiguousAnchorElementsException e)
        {
        }

        return result;
    }

    protected List<String> getXpathsWithTwoAnchors(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                   String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchOption searchOption)
        throws AmbiguousAnchorElementsException
    {
        List<String> xpaths = getXpathsWithTwoAnchorsExactMatch(document, parentAnchorElementTagName, parentAnchorElementOwnText,
                                                                anchorElementTagName, anchorElementOwnText, searchCssQuery, searchOption);

        if (Util.hasNoItem(xpaths))
        {
            ElementInfo parentAnchorElementInfo = new ElementInfo(parentAnchorElementTagName, parentAnchorElementOwnText, true);
            ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText, true);

            try
            {
                xpaths = getXpathsWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, searchOption);
            }
            catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
            {
                return new ArrayList<>();
            }
        }

        return xpaths;
    }

    protected List<String> getXpathsWithTwoAnchorsExactMatch(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                             String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchOption searchOption)
        throws AmbiguousAnchorElementsException
    {
        try
        {
            ElementInfo parentAnchorElementInfo = new ElementInfo(parentAnchorElementTagName, parentAnchorElementOwnText);
            ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText);
            return getXpathsWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, searchOption);
        }
        catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
        {
            return new ArrayList<>();
        }
    }

    protected List<String> getXpathsWithTwoAnchors(Document document, ElementInfo parentAnchorElementInfo, ElementInfo anchorElementInfo, String searchCssQuery, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        TreeElements treeElements = getTreeElementsWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, searchOption);
        return treeElementsToXpaths(treeElements);
    }

    /**
     * region get xpaths from document by anchor
     */
    protected List<List<String>> getAllPossibleXpaths(Document document, String anchorElementTagName, String anchorElementOwnText,
                                                      String searchCssQuery, SearchMethod searchMethod)
    {
        List<List<String>> xpaths = getAllPossibleXpathsExactMatch(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, searchMethod);

        if(Util.hasNoItem(xpaths))
        {

            try
            {
                ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText, true);
                return getAllPossibleXpaths(document, anchorElementInfo, new ElementInfo(searchCssQuery), searchMethod);
            }
            catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
            {
                return new ArrayList<>();
            }
        }

        return xpaths;
    }

    protected List<List<String>> getAllPossibleXpathsExactMatch(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchMethod searchMethod)
    {
        try
        {
            return getAllPossibleXpaths(document, new ElementInfo(anchorElementTagName, anchorElementOwnText), new ElementInfo(searchCssQuery), searchMethod);
        }
        catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
        {
            return new ArrayList<>();
        }
    }

    protected List<List<String>> getAllPossibleXpaths(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo, SearchMethod searchMethod)
            throws AnchorIndexIfMultipleFoundOutOfBoundException
    {
        Elements anchorElements = getElements(document, anchorElementInfo);
        Elements activeAnchorElements = getActiveAnchorElements(anchorElements, anchorElementInfo);
        Elements searchElements = getElements(document, searchElementInfo);
        return getAllPossibleXpaths(activeAnchorElements, searchElements, searchMethod);
    }

    protected List<List<String>> getAllPossibleXpaths(Elements anchorElements, Elements searchElements, SearchMethod searchMethod)
    {
        List<List<String>> result = new ArrayList<>();
        try
        {
            TreeElements treeElements = getTreeElements(anchorElements, searchElements, searchMethod, SearchOption.GetAllIgnoreAmbiguousAnchors);
            result = treeElementsToXpathsGroupingByAnchor(treeElements);
        }
        catch(AmbiguousAnchorElementsException e)
        {
        }

        return result;
    }

    protected List<String> getXpaths(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException
    {
        List<String> xpaths = getXpathsExactMatch(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, searchMethod, searchOption);

        if(Util.hasNoItem(xpaths))
        {
            try
            {
                ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText, true);
                return getXpaths(document, anchorElementInfo, new ElementInfo(searchCssQuery), searchMethod, searchOption);
            }
            catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
            {
                return new ArrayList<>();
            }
        }

        return xpaths;
    }

    protected List<String> getXpathsExactMatch(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException
    {
        try
        {
            return getXpaths(document, new ElementInfo(anchorElementTagName, anchorElementOwnText), new ElementInfo(searchCssQuery), searchMethod, searchOption);
        }
        catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
        {
            return new ArrayList<>();
        }
    }

    protected List<String> getXpaths(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        Elements anchorElements = getElements(document, anchorElementInfo);
        Elements activeAnchorElements = getActiveAnchorElements(anchorElements, anchorElementInfo);
        Elements searchElements = getElements(document, searchElementInfo);
        return getXpaths(activeAnchorElements, searchElements, searchMethod, searchOption);
    }

    protected List<String> getXpaths(Document document, Elements anchorElements, ElementInfo searchElementInfo, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException
    {
        Elements searchElements = getElements(document, searchElementInfo);
        return getXpaths(anchorElements, searchElements, searchMethod, searchOption);
    }

    protected List<String> getXpaths(Element anchorElement, Elements searchElements, SearchMethod searchMethod)
    {
        Elements anchorElements = new Elements();
        anchorElements.add(anchorElement);

        try
        {
            return getXpaths(anchorElements, searchElements, searchMethod, SearchOption.ErrorOnAmbiguousAnchors);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return new ArrayList<>();
        }
    }

    protected List<String> getXpaths(Elements anchorElements, Elements searchElements, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException
    {
        TreeElements treeElements = getTreeElements(anchorElements, searchElements, searchMethod, searchOption);
        return treeElementsToXpaths(treeElements);
    }

    /**
     * region get element from document by two anchors
     */
    protected Element getElementWithTwoAnchors(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                               String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        Element element = getElementWithTwoAnchorsExactMatch(document, parentAnchorElementTagName, parentAnchorElementOwnText,
                                                             anchorElementTagName, anchorElementOwnText, searchCssQuery, searchOption);

        if (element == null)
        {
            ElementInfo parentAnchorElementInfo = new ElementInfo(parentAnchorElementTagName, parentAnchorElementOwnText, true);
            ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText, true);

            try
            {
                element = getElementWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, searchOption);
            }
            catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
            {
            }
        }

        return element;
    }

    protected Element getElementWithTwoAnchorsExactMatch(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                         String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        try
        {
            ElementInfo parentAnchorElementInfo = new ElementInfo(parentAnchorElementTagName,  parentAnchorElementOwnText);
            ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText);

            return getElementWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, searchOption);
        }
        catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
        {
            return null;
        }
    }

    protected Element getElementWithTwoAnchors(Document document, ElementInfo parentAnchorElementInfo, ElementInfo anchorElementInfo, String searchCssQuery, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException, AmbiguousFoundElementsException
    {
        Elements elements = getElementsWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, searchOption);
        int elementCount = elements.size();

        if (elementCount > 1)
        {
            throw new AmbiguousFoundElementsException(String.format(ambiguousFoundElementsMessage, elementCount, elementsToString(elements)));
        }

        return Util.hasNoItem(elements) ? null : elements.get(0);
    }

    /**
     * region get elements from document by two anchors
     */
    protected Elements getElementsWithTwoAnchors(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                 String anchorElementTagName, String anchorElementOwnText,
                                                 String searchCssQuery, SearchOption searchOption)
        throws AmbiguousAnchorElementsException
    {
        Elements elements = getElementsWithTwoAnchorsExactMatch(document, parentAnchorElementTagName, parentAnchorElementOwnText,
                                                                anchorElementTagName, anchorElementOwnText, searchCssQuery, searchOption);

        if (Util.hasNoItem(elements))
        {
            ElementInfo parentAnchorElementInfo = new ElementInfo(parentAnchorElementTagName, parentAnchorElementOwnText, true);
            ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText, true);

            try
            {
                elements = getElementsWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, searchOption);
            }
            catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
            {
            }
        }

        return elements;
    }

    protected Elements getElementsWithTwoAnchorsExactMatch(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                           String anchorElementTagName, String anchorElementOwnText,
                                                           String searchCssQuery, SearchOption searchOption)
        throws AmbiguousAnchorElementsException
    {
        try
        {
            ElementInfo parentAnchorElementInfo = new ElementInfo(parentAnchorElementTagName, parentAnchorElementOwnText);
            ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText);

            return getElementsWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, searchOption);
        }
        catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
        {
            return new Elements();
        }
    }

    protected Elements getElementsWithTwoAnchors(Document document, ElementInfo parentAnchorElementInfo, ElementInfo anchorElementInfo, String searchCssQuery, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        TreeElements treeElements = getTreeElementsWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, searchOption);
        return treeElementsToElements(treeElements);
    }

    protected TreeElements getTreeElementsWithTwoAnchors(Document document, ElementInfo parentAnchorElementInfo, ElementInfo anchorElementInfo, String searchCssQuery, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        TreeElements treeElements = new TreeElements();

        if(document == null)
        {
            return treeElements;
        }

        Elements searchElements = document.select(searchCssQuery);

        if (Util.hasNoItem(searchElements))
        {
            return treeElements;
        }

        Elements anchorElementsByLink = getElements(document, parentAnchorElementInfo, anchorElementInfo, SearchMethod.ByLink, searchOption);

        if (Util.hasItem(anchorElementsByLink))
        {
            Elements activeAnchorElementsByLink = getActiveAnchorElements(anchorElementsByLink, anchorElementInfo);
            return getTreeElements(activeAnchorElementsByLink, searchElements, SearchMethod.ByDistance, searchOption);
        }

        Elements elementsByLink = getElements(document, parentAnchorElementInfo, new ElementInfo(searchCssQuery), SearchMethod.ByLink, searchOption);

        if (Util.hasItem(elementsByLink))
        {
            Elements anchorElements = anchorElementInfo.tagName == null
                ? getElementsMatchingOwnText(document, anchorElementInfo.ownText, anchorElementInfo.condition)
                : getElementsByTagNameMatchingOwnText(document, anchorElementInfo.tagName, anchorElementInfo.ownText, anchorElementInfo.condition);
            Elements filteredAnchors = getElements(elementsByLink, anchorElements, SearchMethod.ByDistance, searchOption);

            return getTreeElements(filteredAnchors, elementsByLink, SearchMethod.ByDistance, searchOption);
        }
        else
        {
            Elements closestAnchorElements = getElements(document, parentAnchorElementInfo, anchorElementInfo, SearchMethod.ByDistance, searchOption);
            return getTreeElements(closestAnchorElements, searchElements, SearchMethod.ByDistance, searchOption);
        }
    }

    /**
     * region get element from document by anchor
     */
    protected Element getElement(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        Element element = getElementExactMatch(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, searchMethod, searchOption);

        if(element == null)
        {
            try
            {
                ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText, true);
                return getElement(document, anchorElementInfo, new ElementInfo(searchCssQuery), searchMethod, searchOption);
            }
            catch(AnchorIndexIfMultipleFoundOutOfBoundException e){}
        }

        return element;
    }

    protected Element getElementExactMatch(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        try
        {
            return getElement(document, new ElementInfo(anchorElementTagName, anchorElementOwnText), new ElementInfo(searchCssQuery), searchMethod, searchOption);
        }
        catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
        {
            return null;
        }
    }

    protected Element getElement(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        Elements anchorElements = getElements(document, anchorElementInfo);
        Elements activeAnchorElements = getActiveAnchorElements(anchorElements, anchorElementInfo);
        Elements searchElements = getElements(document, searchElementInfo);
        return getElement(activeAnchorElements, searchElements, searchMethod, searchOption);
    }

    protected Element getElement(Document document, Elements anchorElements, ElementInfo searchElementInfo, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        Elements searchElements = getElements(document, searchElementInfo);
        return getElement(anchorElements, searchElements, searchMethod, searchOption);
    }

    protected Element getElement(Element anchorElement, Elements searchElements, SearchMethod searchMethod)
        throws AmbiguousFoundElementsException
    {
        Elements anchorElements = new Elements();
        anchorElements.add(anchorElement);

        try
        {
            return getElement(anchorElements, searchElements, searchMethod, SearchOption.ErrorOnAmbiguousAnchors);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    protected Element getElement(Elements anchorElements, Elements searchElements, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        Elements elements = getElements(anchorElements, searchElements, searchMethod, searchOption);
        int elementCount = elements.size();

        if (elementCount > 1)
        {
            throw new AmbiguousFoundElementsException(String.format(ambiguousFoundElementsMessage, elementCount, elementsToString(elements)));
        }

        return Util.hasNoItem(elements) ? null : elements.get(0);
    }

    /**
     * region get elements from document by anchor
     */
    protected Elements getElements(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException
    {
        Elements elements = getElementsExactMatch(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, searchMethod, searchOption);

        if(Util.hasNoItem(elements))
        {
            ElementInfo anchorElementInfo = new ElementInfo(anchorElementTagName, anchorElementOwnText, true);

            try
            {
                return getElements(document, anchorElementInfo, new ElementInfo(searchCssQuery), searchMethod, searchOption);
            }
            catch (AnchorIndexIfMultipleFoundOutOfBoundException e){}
        }

        return elements;
    }

    protected Elements getElementsExactMatch(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException
    {
        try
        {
            return getElements(document, new ElementInfo(anchorElementTagName, anchorElementOwnText), new ElementInfo(searchCssQuery), searchMethod, searchOption);
        }
        catch (AnchorIndexIfMultipleFoundOutOfBoundException e)
        {
            return new Elements();
        }
    }

    protected Elements getElements(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        Elements anchorElements = getElements(document, anchorElementInfo);
        Elements activeAnchorElements = getActiveAnchorElements(anchorElements, anchorElementInfo);
        Elements searchElements = getElements(document, searchElementInfo);
        return getElements(activeAnchorElements, searchElements, searchMethod, searchOption);
    }

    protected Elements getElements(Document document, Elements anchorElements, ElementInfo searchElementInfo, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException
    {
        Elements searchElements = getElements(document, searchElementInfo);
        return getElements(anchorElements, searchElements, searchMethod, searchOption);
    }

    protected Elements getElements(Element anchorElement, Elements searchElements, SearchMethod searchMethod)
    {
        Elements anchorElements = new Elements();
        anchorElements.add(anchorElement);

        try
        {
            return getElements(anchorElements, searchElements, searchMethod, SearchOption.ErrorOnAmbiguousAnchors);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return new Elements();
        }
    }

    protected Elements getElements(Elements anchorElements, Elements searchElements, SearchMethod searchMethod, SearchOption searchOption)
        throws AmbiguousAnchorElementsException
    {
        TreeElements treeElements = getTreeElements(anchorElements, searchElements, searchMethod, searchOption);
        return treeElementsToElements(treeElements);
    }

    /**
     * region get elements from document
     */
    public Elements getElements(Document document, ElementInfo elementInfo)
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
            return getElementsByTagNameMatchingOwnText(document, elementInfo.tagName, elementInfo.ownText, elementInfo.condition);
        }
        else
        {
            return getElementsByTagName(document, elementInfo.tagName);
        }
    }

    public Elements getElementsByTagNameMatchingOwnText(Document document, String tagName, String pattern, Condition condition)
    {
        Elements elements = getElementsByTagName(document, tagName);
        return getElementsMatchingOwnText(elements, pattern, condition);
    }

    public Elements getElementsMatchingOwnText(Document document, String pattern, Condition condition)
    {
        Elements elements = document == null ? new Elements() : document.getAllElements();
        return getElementsMatchingOwnText(elements, pattern, condition);
    }

    public Elements getElementsByTagName(Document document, String tagName)
    {
        Elements result = new Elements();

        if (document != null)
        {
            result = document.select(tagName);
        }

        return result;
    }

    /**
     * region private
     */
    private TreeElements getTreeElements(Elements anchorElements, Elements searchElements, SearchMethod searchMethod, SearchOption searchOption)
            throws AmbiguousAnchorElementsException
    {
        TreeElements result = new TreeElements();

        if (Util.hasItem(anchorElements) && Util.hasItem(searchElements))
        {
            int anchorElementCount = anchorElements.size();

            if (anchorElementCount > 1 && searchOption.equals(SearchOption.ErrorOnAmbiguousAnchors))
            {
                throw new AmbiguousAnchorElementsException(String.format(ambiguousAnchorMessage, anchorElementCount, elementsToString(anchorElements)));
            }

            for (Element anchorElement : anchorElements)
            {
                ElementRecords elementRecords = getElementRecords(anchorElement, searchElements, searchMethod);

                if(Util.hasItem(elementRecords))
                {
                    TreeElements treeElements = elementRecords.getTreeElements(config);
                    result.addAll(treeElements);
                }
            }

            if(anchorElementCount == 1 || searchOption.equals(SearchOption.GetAllIgnoreAmbiguousAnchors))
            {
                return result;
            }

            return getBestMatchedTreeElements(result);
        }

        return result;
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

    private ElementRecords getElementRecords(Element anchorElement, Elements searchElements, SearchMethod searchMethod)
    {
        ElementRecords result;

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

    private ElementRecords getElementRecordsByLinkAndShortestDistance(Element anchorElement, Elements searchElements)
    {
        ElementRecords linkedElementRecords = getLinkedElementRecords(anchorElement, searchElements);
        ElementRecords result;

        if(Util.hasItem(linkedElementRecords))
        {
            result = linkedElementRecords.size() == 1
                ? linkedElementRecords
                : getClosestElementRecords(anchorElement, linkedElementRecords.getTreeElements(config).getElements());
        }
        else
        {
            result = getClosestElementRecords(anchorElement, searchElements);
        }

        return result;
    }

    private ElementRecords getLinkedElementRecords(Element anchorElement, Elements searchElements)
    {
        ElementRecords result = new ElementRecords();
        ElementRecords searchElementRecords = new ElementRecords(anchorElement, searchElements);

        if (Util.hasItem(searchElementRecords))
        {
            searchElementRecords.forEach(x -> {
                Attribute attributeLinkedToAnchor = new TreeElement(x.getLeaf().element).getAttributeLinkedToAnchor(new TreeElement(anchorElement));

                if (attributeLinkedToAnchor != null)
                {
                    result.add(x);
                }
            });
        }

        return result;
    }

    private ElementRecords getClosestElementRecords(Element anchorElement, Elements searchElements)
    {
        ElementRecords searchElementRecords = new ElementRecords(anchorElement, searchElements);
        return searchElementRecords.getClosestElementRecords();
    }

    private String elementsToString(Elements elements)
    {
        if(Util.hasItem(elements))
        {
            StringBuilder elementsAsString = new StringBuilder();
            Elements workingElements = new Elements(elements);

            for (int i = 0; i < workingElements.size(); i++)
            {
                Element currentElement = workingElements.get(i).hasAttr(Tree.uniqueInsertedAttribute)
                    ? (Element) workingElements.get(i).removeAttr(Tree.uniqueInsertedAttribute)
                    : workingElements.get(i);
                elementsAsString.append(String.format("%s. %s\n", i+1, currentElement));
            }

            return elementsAsString.toString();
        }

        return null;
    }

    private <T> String objectsToString(List<T> objects)
    {
        if(Util.hasItem(objects))
        {
            StringBuilder objectsAsString = new StringBuilder();

            for (int i = 0; i < objects.size(); i++)
            {
                objectsAsString.append(String.format("%s. %s\n", i+1, objects.get(i)));
            }

            return objectsAsString.toString();
        }

        return null;
    }

    private TreeElements getBestMatchedTreeElements(TreeElements treeElements)
    {
        TreeElements result = new TreeElements();

        if(Util.hasItem(treeElements))
        {
            TreeElements treeElementsNotSharingAnchor = treeElements.getTreeElementsNotSharingTreeAnchor();

            if(Util.hasItem(treeElementsNotSharingAnchor))
            {
                int treeElementsNotSharingAnchorCount = treeElementsNotSharingAnchor.size();

                if(treeElementsNotSharingAnchorCount == 1)
                {
                    result.addAll(treeElementsNotSharingAnchor);
                }
                else
                {
                    result.addAll(treeElementsNotSharingAnchor.getTreeElementsHavingShortestDistanceToOwnAnchors());
                }
            }
            else
            {
                result.addAll(treeElements.getTreeElementsHavingShortestDistanceToOwnAnchors());
            }
        }

        return result;
    }

    private Elements treeElementsToElements(TreeElements treeElements)
    {
        Elements elements = new Elements();

        if(Util.hasItem(treeElements))
        {
            elements = treeElements.getElements();
        }

        return elements;
    }

    private List<List<String>> treeElementsToXpathsGroupingByAnchor(TreeElements treeElements)
    {
        List<List<String>> result = new ArrayList<>();

        if(Util.hasItem(treeElements))
        {
            List<TreeElements> treeElementsByAnchor = treeElements.groupTreeElementsByAnchor();

            if(Util.hasItem(treeElementsByAnchor))
            {
                treeElementsByAnchor.forEach(x -> {
                    if(Util.hasItem(x))
                    {
                        List<String> xpaths = new ArrayList<>();
                        x.forEach(y -> xpaths.add(y.activeXpath));
                        result.add(xpaths);
                    }
                });
            }
        }

        return result;
    }

    private List<String> treeElementsToXpaths(TreeElements treeElements)
    {
        List<String> xpaths = new ArrayList<>();

        if(Util.hasItem(treeElements))
        {
            xpaths = treeElements.getActiveXpaths();
        }

        return xpaths;
    }

    private List<WebElement> findWebElements(WebDriver driver, List<String> xpaths)
    {
        List<WebElement> webElements = new ArrayList<>();

        if(Util.hasItem(xpaths))
        {
            xpaths.forEach(x -> {
                List<WebElement> result = findWebElements(driver, x);

                if(Util.hasItem(result))
                {
                    webElements.addAll(result);
                }
            });
        }

        return webElements;
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
}
