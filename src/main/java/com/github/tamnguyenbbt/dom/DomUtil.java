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
public class DomUtil extends DomCore
{
    public DomUtil(DomUtilConfig config)
    {
        super(config);
    }

    public DomUtil()
    {
    }

    /**region get web element from document by two anchors
     */
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
                                               anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e) { return null; }
    }

    public WebElement getWebElementWithTwoAnchors(WebDriver driver, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                  String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return getWebElementWithTwoAnchors(driver, parentAnchorElementTagName, parentAnchorElementOwnText, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public WebElement getWebElementWithTwoAnchorsExactMatchBestEffort(WebDriver driver, String parentAnchorElementOwnText,
                                                            String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundWebElementsException
    {
        return getWebElementWithTwoAnchorsExactMatchBestEffort(driver, parentAnchorElementOwnText,
                null, anchorElementOwnText, searchCssQuery);
    }

    public WebElement getWebElementWithTwoAnchorsExactMatch(WebDriver driver, String parentAnchorElementOwnText,
                                                  String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return getWebElementWithTwoAnchorsExactMatch(driver, parentAnchorElementOwnText,
                null, anchorElementOwnText, searchCssQuery);
    }

    public WebElement getWebElementWithTwoAnchorsExactMatchBestEffort(WebDriver driver, String parentAnchorElementOwnText,
                                                            String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundWebElementsException
    {
        return getWebElementWithTwoAnchorsExactMatchBestEffort(driver, null, parentAnchorElementOwnText,
                anchorElementTagName, anchorElementOwnText, searchCssQuery);
    }

    public WebElement getWebElementWithTwoAnchorsExactMatch(WebDriver driver, String parentAnchorElementOwnText,
                                                  String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return getWebElementWithTwoAnchorsExactMatch(driver, null, parentAnchorElementOwnText,
                anchorElementTagName, anchorElementOwnText, searchCssQuery);
    }

    public WebElement getWebElementWithTwoAnchorsExactMatchBestEffort(WebDriver driver, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                            String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundWebElementsException
    {
        try
        {
            return getWebElementWithTwoAnchorsExactMatch(driver, parentAnchorElementTagName, parentAnchorElementOwnText,
                    anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e) { return null; }
    }

    public WebElement getWebElementWithTwoAnchorsExactMatch(WebDriver driver, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                  String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return getWebElementWithTwoAnchorsExactMatch(driver, parentAnchorElementTagName, parentAnchorElementOwnText, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public WebElement getWebElementWithTwoAnchorsBestEffort(WebDriver driver, ElementInfo parentAnchorElementInfo,
                                                            ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getWebElementWithTwoAnchors(driver, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, SearchOption.BestEffort);
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
        return getWebElementWithTwoAnchors(driver, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, SearchOption.ErrorOnAmbiguousAnchors);
    }

    /**region get web element from document by anchor
     */
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
            return getWebElement(driver, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public WebElement getWebElement(WebDriver driver, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return getWebElement(driver, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public WebElement getWebElementExactMatchBestEffort(WebDriver driver, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundWebElementsException
    {
        return getWebElementExactMatchBestEffort(driver, null, anchorElementOwnText, searchCssQuery);
    }

    public WebElement getWebElementExactMatch(WebDriver driver, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return getWebElementExactMatch(driver, null, anchorElementOwnText, searchCssQuery);
    }

    public WebElement getWebElementExactMatchBestEffort(WebDriver driver, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundWebElementsException
    {
        try
        {
            return getWebElementExactMatch(driver, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public WebElement getWebElementExactMatch(WebDriver driver, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return getWebElementExactMatch(driver, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public WebElement getWebElementBestEffort(WebDriver driver, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getWebElement(driver, anchorElementInfo, searchElementInfo, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public WebElement getWebElement(WebDriver driver, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        return getWebElement(driver, anchorElementInfo, searchElementInfo, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    /**region get web elements from document by two anchors
     */
    public List<WebElement> getWebElementsWithTwoAnchorsBestEffort(WebDriver driver, String parentAnchorElementOwnText,
                                                                   String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundWebElementsException
    {
        return getWebElementsWithTwoAnchorsBestEffort(driver, parentAnchorElementOwnText, null, anchorElementOwnText, searchCssQuery);
    }

    public List<WebElement> getWebElementsWithTwoAnchors(WebDriver driver, String parentAnchorElementOwnText,
                                                         String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return getWebElementsWithTwoAnchors(driver, parentAnchorElementOwnText, null, anchorElementOwnText, searchCssQuery);
    }

    public List<WebElement> getWebElementsWithTwoAnchorsBestEffort(WebDriver driver, String parentAnchorElementOwnText,
                                                                   String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundWebElementsException
    {
        return getWebElementsWithTwoAnchorsBestEffort(driver, null, parentAnchorElementOwnText, anchorElementTagName, anchorElementOwnText, searchCssQuery);
    }

    public List<WebElement> getWebElementsWithTwoAnchors(WebDriver driver, String parentAnchorElementOwnText,
                                                         String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return getWebElementsWithTwoAnchors(driver, null, parentAnchorElementOwnText, anchorElementTagName, anchorElementOwnText, searchCssQuery);
    }

    public List<WebElement> getWebElementsWithTwoAnchorsBestEffort(WebDriver driver, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                                   String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundWebElementsException
    {
        try
        {
            return getWebElementsWithTwoAnchors(driver, parentAnchorElementTagName, parentAnchorElementOwnText,
                                                       anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return new ArrayList<>();
        }
    }

    public List<WebElement> getWebElementsWithTwoAnchors(WebDriver driver, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                         String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return getWebElementsWithTwoAnchors(driver, parentAnchorElementTagName, parentAnchorElementOwnText,
                                            anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public List<WebElement> getWebElementsWithTwoAnchorsExactMatchBestEffort(WebDriver driver, String parentAnchorElementOwnText,
                                                                   String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundWebElementsException
    {
        return getWebElementsWithTwoAnchorsExactMatchBestEffort(driver, parentAnchorElementOwnText, null, anchorElementOwnText, searchCssQuery);
    }

    public List<WebElement> getWebElementsWithTwoAnchorsExactMatch(WebDriver driver, String parentAnchorElementOwnText,
                                                         String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return getWebElementsWithTwoAnchorsExactMatch(driver, parentAnchorElementOwnText, null, anchorElementOwnText, searchCssQuery);
    }

    public List<WebElement> getWebElementsWithTwoAnchorsExactMatchBestEffort(WebDriver driver, String parentAnchorElementOwnText,
                                                                   String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundWebElementsException
    {
        return getWebElementsWithTwoAnchorsExactMatchBestEffort(driver, null, parentAnchorElementOwnText, anchorElementTagName, anchorElementOwnText, searchCssQuery);
    }

    public List<WebElement> getWebElementsWithTwoAnchorsExactMatch(WebDriver driver, String parentAnchorElementOwnText,
                                                         String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return getWebElementsWithTwoAnchorsExactMatch(driver, null, parentAnchorElementOwnText, anchorElementTagName, anchorElementOwnText, searchCssQuery);
    }

    public List<WebElement> getWebElementsWithTwoAnchorsExactMatchBestEffort(WebDriver driver, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                                   String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundWebElementsException
    {
        try
        {
            return getWebElementsWithTwoAnchorsExactMatch(driver, parentAnchorElementTagName, parentAnchorElementOwnText,
                    anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return new ArrayList<>();
        }
    }

    public List<WebElement> getWebElementsWithTwoAnchorsExactMatch(WebDriver driver, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                         String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return getWebElementsWithTwoAnchorsExactMatch(driver, parentAnchorElementTagName, parentAnchorElementOwnText,
                anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public List<WebElement> getWebElementsWithTwoAnchorsBestEffort(WebDriver driver, ElementInfo parentAnchorElementInfo,
                                                                   ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getWebElementsWithTwoAnchors(driver, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, SearchOption.BestEffort);
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
        return getWebElementsWithTwoAnchors(driver, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, SearchOption.ErrorOnAmbiguousAnchors);
    }

    /**region get web elements from document by anchor
     */
    public List<WebElement> getWebElementsBestEffort(WebDriver driver, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundWebElementsException
    {
        return getWebElementsBestEffort(driver, null, anchorElementOwnText, searchCssQuery);
    }

    public List<WebElement> getWebElements(WebDriver driver, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return getWebElements(driver, null, anchorElementOwnText, searchCssQuery);
    }

    public List<WebElement> getWebElementsBestEffort(WebDriver driver, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundWebElementsException
    {
        try
        {
            return getWebElements(driver, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public List<WebElement> getWebElements(WebDriver driver, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return getWebElements(driver, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public List<WebElement> getWebElementsExactMatchBestEffort(WebDriver driver, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundWebElementsException
    {
        return getWebElementsExactMatchBestEffort(driver, null, anchorElementOwnText, searchCssQuery);
    }

    public List<WebElement> getWebElementsExactMatch(WebDriver driver, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return getWebElementsExactMatch(driver, null, anchorElementOwnText, searchCssQuery);
    }

    public List<WebElement> getWebElementsExactMatchBestEffort(WebDriver driver, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundWebElementsException
    {
        try
        {
            return getWebElementsExactMatch(driver, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public List<WebElement> getWebElementsExactMatch(WebDriver driver, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return getWebElementsExactMatch(driver, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public List<WebElement> getWebElementsBestEffort(WebDriver driver, ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        return getWebElementsBestEffort(driver, anchorElementInfo, new ElementInfo(searchCssQuery));
    }

    public List<WebElement> getWebElements(WebDriver driver, ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        return getWebElements(driver, anchorElementInfo, new ElementInfo(searchCssQuery));
    }

    public List<WebElement> getWebElementsBestEffort(WebDriver driver, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getWebElements(driver, anchorElementInfo, searchElementInfo, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public List<WebElement> getWebElements(WebDriver driver, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        return getWebElements(driver, anchorElementInfo, searchElementInfo, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    /**region get xpath from document by two anchors
     */
    public String getXpathWithTwoAnchorsBestEffort(Document document, String parentAnchorElementOwnText,
                                                   String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundXpathsException
    {
        return getXpathWithTwoAnchorsBestEffort(document, parentAnchorElementOwnText, null, anchorElementOwnText, searchCssQuery);
    }

    public String getXpathWithTwoAnchors(Document document, String parentAnchorElementOwnText,
                                         String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return getXpathWithTwoAnchors(document, parentAnchorElementOwnText, null, anchorElementOwnText, searchCssQuery);
    }

    public String getXpathWithTwoAnchorsBestEffort(Document document, String parentAnchorElementOwnText,
                                                   String anchorElementTagName,
                                                   String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundXpathsException
    {
        return getXpathWithTwoAnchorsBestEffort(document, null, parentAnchorElementOwnText, anchorElementTagName,
                                                anchorElementOwnText, searchCssQuery);
    }

    public String getXpathWithTwoAnchors(Document document, String parentAnchorElementOwnText,
                                         String anchorElementTagName,
                                         String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return getXpathWithTwoAnchors(document, null, parentAnchorElementOwnText, anchorElementTagName,
                                      anchorElementOwnText, searchCssQuery);
    }

    public String getXpathWithTwoAnchorsBestEffort(Document document, String parentAnchorElementTagName,
                                                   String parentAnchorElementOwnText, String anchorElementTagName,
                                                   String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundXpathsException
    {
        try
        {
            return getXpathWithTwoAnchors(document, parentAnchorElementTagName, parentAnchorElementOwnText,
                                          anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchOption.BestEffort);
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
                                      anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public String getXpathWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementOwnText,
                                                             String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundXpathsException
    {
        return getXpathWithTwoAnchorsExactMatchBestEffort(document, parentAnchorElementOwnText, null, anchorElementOwnText, searchCssQuery);
    }

    public String getXpathWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText,
                                                   String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return getXpathWithTwoAnchorsExactMatch(document, parentAnchorElementOwnText, null, anchorElementOwnText, searchCssQuery);
    }

    public String getXpathWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementOwnText,
                                                             String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundXpathsException
    {
        return getXpathWithTwoAnchorsExactMatchBestEffort(document, null, parentAnchorElementOwnText, anchorElementTagName,
                                                          anchorElementOwnText, searchCssQuery);
    }

    public String getXpathWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText,
                                                   String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return getXpathWithTwoAnchorsExactMatch(document, null, parentAnchorElementOwnText, anchorElementTagName,
                                                anchorElementOwnText, searchCssQuery);
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
                                                    anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchOption.BestEffort);
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
                                                anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public String getXpathWithTwoAnchorsBestEffort(WebDriver driver, ElementInfo parentAnchorElementInfo,
                                                   ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousFoundXpathsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        Document document = getActiveDocument(driver);
        return getXpathWithTwoAnchorsBestEffort(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery);
    }

    public String getXpathWithTwoAnchors(WebDriver driver, ElementInfo parentAnchorElementInfo,
                                         ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        Document document = getActiveDocument(driver);
        return getXpathWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery);
    }

    public String getXpathWithTwoAnchorsBestEffort(Document document, ElementInfo parentAnchorElementInfo,
                                                   ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousFoundXpathsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getXpathWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, SearchOption.BestEffort);
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
        return getXpathWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, SearchOption.ErrorOnAmbiguousAnchors);
    }

    /**region get xpaths from document by two anchors
     */
    public List<String> getXpathsWithTwoAnchorsBestEffort(Document document, String parentAnchorElementOwnText,
                                                   String anchorElementOwnText, String searchCssQuery)
    {
        return getXpathsWithTwoAnchorsBestEffort(document, parentAnchorElementOwnText, null, anchorElementOwnText, searchCssQuery);
    }

    public List<String> getXpathsWithTwoAnchors(Document document, String parentAnchorElementOwnText,
                                         String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException
    {
        return getXpathsWithTwoAnchors(document, parentAnchorElementOwnText, null, anchorElementOwnText, searchCssQuery);
    }

    public List<String> getXpathsWithTwoAnchorsBestEffort(Document document, String parentAnchorElementOwnText,
                                                   String anchorElementTagName,
                                                   String anchorElementOwnText, String searchCssQuery)
    {
        return getXpathsWithTwoAnchorsBestEffort(document, null, parentAnchorElementOwnText, anchorElementTagName,
                                                anchorElementOwnText, searchCssQuery);
    }

    public List<String> getXpathsWithTwoAnchors(Document document, String parentAnchorElementOwnText,
                                         String anchorElementTagName,
                                         String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException
    {
        return getXpathsWithTwoAnchors(document, null, parentAnchorElementOwnText, anchorElementTagName,
                                      anchorElementOwnText, searchCssQuery);
    }

    public List<String> getXpathsWithTwoAnchorsBestEffort(Document document, String parentAnchorElementTagName,
                                                   String parentAnchorElementOwnText, String anchorElementTagName,
                                                   String anchorElementOwnText, String searchCssQuery)
    {
        try
        {
            return getXpathsWithTwoAnchors(document, parentAnchorElementTagName, parentAnchorElementOwnText,
                                          anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return new ArrayList<>();
        }
    }

    public List<String> getXpathsWithTwoAnchors(Document document, String parentAnchorElementTagName,
                                         String parentAnchorElementOwnText, String anchorElementTagName,
                                         String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException
    {
        return getXpathsWithTwoAnchors(document, parentAnchorElementTagName, parentAnchorElementOwnText,
                                      anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public List<String> getXpathsWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementOwnText,
                                                             String anchorElementOwnText, String searchCssQuery)
    {
        return getXpathsWithTwoAnchorsExactMatchBestEffort(document, parentAnchorElementOwnText, null, anchorElementOwnText, searchCssQuery);
    }

    public List<String> getXpathsWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText,
                                                   String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException
    {
        return getXpathsWithTwoAnchorsExactMatch(document, parentAnchorElementOwnText, null, anchorElementOwnText, searchCssQuery);
    }

    public List<String> getXpathsWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementOwnText,
                                                             String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
    {
        return getXpathsWithTwoAnchorsExactMatchBestEffort(document, null, parentAnchorElementOwnText, anchorElementTagName,
                                                          anchorElementOwnText, searchCssQuery);
    }

    public List<String> getXpathsWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText,
                                                   String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException
    {
        return getXpathsWithTwoAnchorsExactMatch(document, null, parentAnchorElementOwnText, anchorElementTagName,
                                                anchorElementOwnText, searchCssQuery);
    }

    public List<String> getXpathsWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementTagName,
                                                             String parentAnchorElementOwnText,
                                                             String anchorElementTagName, String anchorElementOwnText,
                                                             String searchCssQuery)
    {
        try
        {
            return getXpathsWithTwoAnchorsExactMatch(document, parentAnchorElementTagName, parentAnchorElementOwnText,
                                                    anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return new ArrayList<>();
        }
    }

    public List<String> getXpathsWithTwoAnchorsExactMatch(Document document, String parentAnchorElementTagName,
                                                   String parentAnchorElementOwnText,
                                                   String anchorElementTagName, String anchorElementOwnText,
                                                   String searchCssQuery)
        throws AmbiguousAnchorElementsException
    {
        return getXpathsWithTwoAnchorsExactMatch(document, parentAnchorElementTagName, parentAnchorElementOwnText,
                                                anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public List<String> getXpathsWithTwoAnchorsBestEffort(WebDriver driver, ElementInfo parentAnchorElementInfo,
                                                   ElementInfo anchorElementInfo, String searchCssQuery)
        throws AnchorIndexIfMultipleFoundOutOfBoundException
    {
        Document document = getActiveDocument(driver);
        return getXpathsWithTwoAnchorsBestEffort(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery);
    }

    public List<String> getXpathsWithTwoAnchors(WebDriver driver, ElementInfo parentAnchorElementInfo,
                                         ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        Document document = getActiveDocument(driver);
        return getXpathsWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery);
    }

    public List<String> getXpathsWithTwoAnchorsBestEffort(Document document, ElementInfo parentAnchorElementInfo,
                                                   ElementInfo anchorElementInfo, String searchCssQuery)
        throws AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getXpathsWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return new ArrayList<>();
        }
    }

    public List<String> getXpathsWithTwoAnchors(Document document, ElementInfo parentAnchorElementInfo,
                                         ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        return getXpathsWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, SearchOption.ErrorOnAmbiguousAnchors);
    }

    /**region get xpath from document by anchor
     */
    public String getXpathBestEffort(Document document, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundXpathsException
    {
        return getXpathBestEffort(document, null, anchorElementOwnText, searchCssQuery);
    }

    public String getXpath(Document document, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return getXpath(document, null, anchorElementOwnText, searchCssQuery);
    }

    public String getXpathBestEffort(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundXpathsException
    {
        try
        {
            return getXpath(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public String getXpath(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return getXpath(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public String getXpathExactMatchBestEffort(Document document, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundXpathsException
    {
        return getXpathExactMatchBestEffort(document, null, anchorElementOwnText, searchCssQuery);
    }

    public String getXpathExactMatch(Document document, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return getXpathExactMatch(document, null, anchorElementOwnText, searchCssQuery);
    }

    public String getXpathExactMatchBestEffort(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundXpathsException
    {
        try
        {
            return getXpathExactMatch(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public String getXpathExactMatch(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return getXpathExactMatch(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public String getXpathBestEffort(Document document, Elements anchorElements, ElementInfo searchElementInfo)
        throws AmbiguousFoundXpathsException
    {
        try
        {
            return  getXpath(document, anchorElements, searchElementInfo, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public String getXpath(Document document, Elements anchorElements, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return  getXpath(document, anchorElements, searchElementInfo, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public String getXpathBestEffort(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AmbiguousFoundXpathsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getXpath(document, anchorElementInfo, searchElementInfo, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
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
        return getXpath(document, anchorElementInfo, searchElementInfo, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public String getXpathBestEffort(Elements anchorElements, Elements searchElements)
        throws AmbiguousFoundXpathsException
    {
        try
        {
            return getXpath(anchorElements, searchElements, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public String getXpath(Elements anchorElements, Elements searchElements)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        return getXpath(anchorElements, searchElements, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public String getXpath(Element anchorElement, Elements searchElements)
        throws AmbiguousFoundXpathsException
    {
        return getXpath(anchorElement, searchElements, SearchMethod.ByLinkAndDistance);
    }

    /**region get xpaths from document by anchor
     */
    public List<String> getXpathsBestEffort(Document document, String anchorElementOwnText, String searchCssQuery)
    {
        return getXpathsBestEffort(document, null, anchorElementOwnText, searchCssQuery);
    }

    public List<String> getXpaths(Document document, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException
    {
        return getXpaths(document, null, anchorElementOwnText, searchCssQuery);
    }

    public List<String> getXpathsBestEffort(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
    {
        try
        {
            return getXpaths(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return new ArrayList<>();
        }
    }

    public List<String> getXpaths(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException
    {
        return getXpaths(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public List<String> getXpathsExactMatchBestEffort(Document document, String anchorElementOwnText, String searchCssQuery)
    {
        return getXpathsExactMatchBestEffort(document, null, anchorElementOwnText, searchCssQuery);
    }

    public List<String> getXpathsExactMatch(Document document, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException
    {
        return getXpathsExactMatch(document, null, anchorElementOwnText, searchCssQuery);
    }

    public List<String> getXpathsExactMatchBestEffort(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
    {
        try
        {
            return getXpathsExactMatch(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return new ArrayList<>();
        }
    }

    public List<String> getXpathsExactMatch(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException
    {
        return getXpathsExactMatch(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public List<String> getXpathsBestEffort(Document document, ElementInfo anchorElementInfo, String searchCssQuery)
        throws AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getXpaths(document, anchorElementInfo, new ElementInfo(searchCssQuery), SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return new ArrayList<>();
        }
    }

    public List<String> getXpaths(Document document, ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        return getXpaths(document, anchorElementInfo, new ElementInfo(searchCssQuery), SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public List<String> getXpathsBestEffort(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getXpaths(document, anchorElementInfo, searchElementInfo, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return new ArrayList<>();
        }
    }

    public List<String> getXpaths(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        return getXpaths(document, anchorElementInfo, searchElementInfo, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public List<String> getXpathsBestEffort(Document document, Elements anchorElements, ElementInfo searchElementInfo)
    {
        try
        {
            return getXpaths(document, anchorElements, searchElementInfo, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return new ArrayList<>();
        }
    }

    public List<String> getXpaths(Document document, Elements anchorElements, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException
    {
        return getXpaths(document, anchorElements, searchElementInfo, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public List<String> getXpathsBestEffort(Elements anchorElements, Elements searchElements)
    {
        try
        {
            return getXpaths(anchorElements, searchElements, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return new ArrayList<>();
        }
    }

    public List<String> getXpaths(Elements anchorElements, Elements searchElements)
        throws AmbiguousAnchorElementsException
    {
        return getXpaths(anchorElements, searchElements, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
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

        if (Util.hasItem(foundWebElements))
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

    /**region get element from document by two anchors
     */
    public Element getElementWithTwoAnchorsBestEffort(Document document, String parentAnchorElementOwnText,
                                                      String anchorElementOwnText,
                                                      String searchCssQuery)
        throws AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchorsBestEffort(document, parentAnchorElementOwnText, null, anchorElementOwnText, searchCssQuery);
    }

    public Element getElementWithTwoAnchors(Document document, String parentAnchorElementOwnText,
                                            String anchorElementOwnText,
                                            String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchors(document, parentAnchorElementOwnText, null, anchorElementOwnText, searchCssQuery);
    }

    public Element getElementWithTwoAnchorsBestEffort(Document document, String parentAnchorElementOwnText,
                                                      String anchorElementTagName,
                                                      String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchorsBestEffort(document, null, parentAnchorElementOwnText, anchorElementTagName,
                                                  anchorElementOwnText, searchCssQuery);
    }

    public Element getElementWithTwoAnchors(Document document, String parentAnchorElementOwnText,
                                            String anchorElementTagName,
                                            String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchors(document, null, parentAnchorElementOwnText, anchorElementTagName,
                                        anchorElementOwnText, searchCssQuery);
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
                                            anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchOption.BestEffort);
        }
        catch (AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public Element getElementWithTwoAnchors(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                            String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchors(document, parentAnchorElementTagName, parentAnchorElementOwnText,
                                        anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public Element getElementWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementOwnText,
                                                                String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchorsExactMatchBestEffort(document, parentAnchorElementOwnText, null, anchorElementOwnText, searchCssQuery);
    }

    public Element getElementWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText,
                                                      String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchorsExactMatch(document, parentAnchorElementOwnText, null, anchorElementOwnText, searchCssQuery);
    }

    public Element getElementWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementOwnText,
                                                                String anchorElementTagName,
                                                                String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchorsExactMatchBestEffort(document, null, parentAnchorElementOwnText, anchorElementTagName,
                                                            anchorElementOwnText, searchCssQuery);
    }

    public Element getElementWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText,
                                                      String anchorElementTagName,
                                                      String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementWithTwoAnchorsExactMatch(document, null, parentAnchorElementOwnText, anchorElementTagName,
                                                  anchorElementOwnText, searchCssQuery);
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
                                                      anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchOption.BestEffort);
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
                                                  anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public Element getElementWithTwoAnchorsBestEffort(Document document, ElementInfo parentAnchorElementInfo,
                                                      ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousFoundElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getElementWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, SearchOption.BestEffort);
        }
        catch (AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public Element getElementWithTwoAnchors(Document document, ElementInfo parentAnchorElementInfo, ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        return getElementWithTwoAnchors(document, parentAnchorElementInfo, anchorElementInfo, searchCssQuery, SearchOption.ErrorOnAmbiguousAnchors);
    }

    /**region get element from document by anchor
     */
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
            return getElement(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch (AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public Element getElement(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElement(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public Element getElementExactMatchBestEffort(Document document, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundElementsException
    {
        return getElementExactMatchBestEffort(document, null, anchorElementOwnText, searchCssQuery);
    }

    public Element getElementExactMatch(Document document, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementExactMatch(document, null, anchorElementOwnText, searchCssQuery);
    }

    public Element getElementExactMatchBestEffort(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousFoundElementsException
    {
        try
        {
            return getElementExactMatch(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch (AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public Element getElementExactMatch(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElementExactMatch(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public Element getElementBestEffort(Document document, ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousFoundElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getElement(document, anchorElementInfo, new ElementInfo(searchCssQuery), SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch (AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public Element getElement(Document document, ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        return getElement(document, anchorElementInfo, new ElementInfo(searchCssQuery), SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public Element getElementBestEffort(Document document, Elements anchorElements, ElementInfo searchElementInfo)
        throws AmbiguousFoundElementsException
    {
        try
        {
            return getElement(document, anchorElements, searchElementInfo, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public Element getElement(Document document, Elements anchorElements, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElement(document, anchorElements, searchElementInfo, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public Element getElementBestEffort(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AmbiguousFoundElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getElement(document, anchorElementInfo, searchElementInfo, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public Element getElement(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        return getElement(document, anchorElementInfo, searchElementInfo, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public Element getElementBestEffort(Elements anchorElements, Elements searchElements)
        throws AmbiguousFoundElementsException
    {
        try
        {
            return getElement(anchorElements, searchElements, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch(AmbiguousAnchorElementsException e)
        {
            return null;
        }
    }

    public Element getElement(Elements anchorElements, Elements searchElements)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException
    {
        return getElement(anchorElements, searchElements, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public Element getElement(Element anchorElement, Elements searchElements)
        throws AmbiguousFoundElementsException
    {
        return getElement(anchorElement, searchElements, SearchMethod.ByLinkAndDistance);
    }

    /**
     * region get elements from document by anchor
     */
    public Elements getElementsBestEffort(Document document, String anchorElementOwnText, String searchCssQuery)
    {
        return getElementsBestEffort(document, null, anchorElementOwnText, searchCssQuery);
    }

    public Elements getElementsBestEffort(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
    {
        try
        {
            return getElements(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch (AmbiguousAnchorElementsException e)
        {
            return new Elements();
        }
    }

    public Elements getElements(Document document, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException
    {
        return getElements(document, null, anchorElementOwnText, searchCssQuery);
    }

    public Elements getElements(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException
    {
        return getElements(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public Elements getElementsExactMatchBestEffort(Document document, String anchorElementOwnText, String searchCssQuery)
    {
        return getElementsExactMatchBestEffort(document, null, anchorElementOwnText, searchCssQuery);
    }

    public Elements getElementsExactMatchBestEffort(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
    {
        try
        {
            return getElementsExactMatch(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch (AmbiguousAnchorElementsException e)
        {
            return new Elements();
        }
    }

    public Elements getElementsExactMatch(Document document, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException
    {
        return getElementsExactMatch(document, null, anchorElementOwnText, searchCssQuery);
    }

    public Elements getElementsExactMatch(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException
    {
        return getElementsExactMatch(document, anchorElementTagName, anchorElementOwnText, searchCssQuery, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public Elements getElementsBestEffort(Document document, ElementInfo anchorElementInfo, String searchCssQuery)
        throws AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getElements(document, anchorElementInfo, new ElementInfo(searchCssQuery), SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch (AmbiguousAnchorElementsException e)
        {
            return new Elements();
        }
    }

    public Elements getElements(Document document, ElementInfo anchorElementInfo, String searchCssQuery)
        throws AnchorIndexIfMultipleFoundOutOfBoundException, AmbiguousAnchorElementsException
    {
        return getElements(document, anchorElementInfo, new ElementInfo(searchCssQuery), SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public Elements getElementsBestEffort(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AnchorIndexIfMultipleFoundOutOfBoundException
    {
        try
        {
            return getElements(document, anchorElementInfo, searchElementInfo, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch (AmbiguousAnchorElementsException e)
        {
            return new Elements();
        }
    }

    public Elements getElements(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException
    {
        return getElements(document, anchorElementInfo, searchElementInfo, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public Elements getElementsBestEffort(Document document, Elements anchorElements, ElementInfo searchElementInfo)
    {
        try
        {
            return getElements(document, anchorElements, searchElementInfo, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch (AmbiguousAnchorElementsException e)
        {
            return new Elements();
        }
    }

    public Elements getElements(Document document, Elements anchorElements, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException
    {
        return getElements(document, anchorElements, searchElementInfo, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public Elements getElementsBestEffort(Elements anchorElements, Elements searchElements)
    {
        try
        {
            return getElements(anchorElements, searchElements, SearchMethod.ByLinkAndDistance, SearchOption.BestEffort);
        }
        catch (AmbiguousAnchorElementsException e)
        {
            return new Elements();
        }
    }

    public Elements getElements(Elements anchorElements, Elements searchElements)
        throws AmbiguousAnchorElementsException
    {
        return getElements(anchorElements, searchElements, SearchMethod.ByLinkAndDistance, SearchOption.ErrorOnAmbiguousAnchors);
    }

    public Elements getElements(Element anchorElement, Elements searchElements)
    {
        return getElements(anchorElement, searchElements, SearchMethod.ByLinkAndDistance);
    }

    /**
     * region get elements from document by tag and text
     */
    public Elements getElementsByTagNameContainingOwnTextIgnoreCase(Document document, String tagName, String pattern)
    {
        return getElementsByTagNameMatchingOwnText(document, tagName, pattern, new Condition(true, true, false));
    }

    public Elements getElementsByTagNameContainingOwnText(Document document, String tagName, String pattern)
    {
        return getElementsByTagNameMatchingOwnText(document, tagName, pattern, new Condition(false, true, false));
    }

    public Elements getElementsByTagNameMatchingOwnTextIgnoreCase(Document document, String tagName, String pattern)
    {
        return getElementsByTagNameMatchingOwnText(document, tagName, pattern, new Condition(true, false, false));
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
        return getElementsMatchingOwnText(document, pattern, new Condition(false, true, false));
    }

    public Elements getElementsMatchingOwnTextIgnoreCase(Document document, String pattern)
    {
        return getElementsMatchingOwnText(document, pattern, new Condition(true, false, false));
    }

    public Elements getElementsMatchingOwnText(Document document, String pattern)
    {
        return getElementsMatchingOwnText(document, pattern, new Condition());
    }
}
