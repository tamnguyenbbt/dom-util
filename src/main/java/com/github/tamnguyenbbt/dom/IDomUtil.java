package com.github.tamnguyenbbt.dom;

import com.github.tamnguyenbbt.exception.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public interface IDomUtil
{
    /** 
     * region get web element from driver by two anchors
     */
    WebElement getWebElementWithTwoAnchorsBestEffort(WebDriver driver, String parentAnchorElementOwnText, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundWebElementsException;

    WebElement getWebElementWithTwoAnchorsBestEffort(WebDriver driver, String parentAnchorElementOwnText,
                                           String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundWebElementsException;

    WebElement getWebElementWithTwoAnchorsBestEffort(WebDriver driver, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                           String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundWebElementsException;

    WebElement getWebElementWithTwoAnchors(WebDriver driver, String parentAnchorElementOwnText, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException;

    WebElement getWebElementWithTwoAnchors(WebDriver driver, String parentAnchorElementOwnText,
                                           String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException;

    WebElement getWebElementWithTwoAnchors(WebDriver driver, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                           String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException;

    WebElement getWebElementWithTwoAnchorsExactMatchBestEffort(WebDriver driver, String parentAnchorElementOwnText, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundWebElementsException;

    WebElement getWebElementWithTwoAnchorsExactMatchBestEffort(WebDriver driver, String parentAnchorElementOwnText,
                                                     String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundWebElementsException;

    WebElement getWebElementWithTwoAnchorsExactMatchBestEffort(WebDriver driver, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                     String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundWebElementsException;

    WebElement getWebElementWithTwoAnchorsExactMatch(WebDriver driver, String parentAnchorElementOwnText, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException;

    WebElement getWebElementWithTwoAnchorsExactMatch(WebDriver driver, String parentAnchorElementOwnText,
                                                     String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException;

    WebElement getWebElementWithTwoAnchorsExactMatch(WebDriver driver, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                     String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException;

    WebElement getWebElementWithTwoAnchorsBestEffort(WebDriver driver, ElementInfo parentAnchorElementInfo, ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException;

    WebElement getWebElementWithTwoAnchors(WebDriver driver, ElementInfo parentAnchorElementInfo, ElementInfo anchorElementInfo, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException;

    /**
     * region get web element from driver by anchor
     */
    WebElement getWebElementBestEffort(WebDriver driver, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundWebElementsException;

    WebElement getWebElementBestEffort(WebDriver driver, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundWebElementsException;

    WebElement getWebElement(WebDriver driver, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException;

    WebElement getWebElement(WebDriver driver, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException;

    WebElement getWebElementExactMatchBestEffort(WebDriver driver, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundWebElementsException;

    WebElement getWebElementExactMatchBestEffort(WebDriver driver, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundWebElementsException;

    WebElement getWebElementExactMatch(WebDriver driver, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException;

    WebElement getWebElementExactMatch(WebDriver driver, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException;

    WebElement getWebElementBestEffort(WebDriver driver, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException;

    WebElement getWebElement(WebDriver driver, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
            throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException, AnchorIndexIfMultipleFoundOutOfBoundException;

    /**
     * region get web elements from driver by two anchors
     */
    List<WebElement> getWebElementsWithTwoAnchorsBestEffort(WebDriver driver, String parentAnchorElementOwnText, String anchorElementOwnText, String searchCssQuery);

    List<WebElement> getWebElementsWithTwoAnchorsBestEffort(WebDriver driver, String parentAnchorElementOwnText, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery);

    List<WebElement> getWebElementsWithTwoAnchorsBestEffort(WebDriver driver, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                            String anchorElementTagName, String anchorElementOwnText, String searchCssQuery);

    List<WebElement> getWebElementsWithTwoAnchors(WebDriver driver, String parentAnchorElementOwnText, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException;

    List<WebElement> getWebElementsWithTwoAnchors(WebDriver driver, String parentAnchorElementOwnText, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException;

    List<WebElement> getWebElementsWithTwoAnchors(WebDriver driver, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                  String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException;

    List<WebElement> getWebElementsWithTwoAnchorsExactMatchBestEffort(WebDriver driver, String parentAnchorElementOwnText, String anchorElementOwnText, String searchCssQuery);

    List<WebElement> getWebElementsWithTwoAnchorsExactMatchBestEffort(WebDriver driver, String parentAnchorElementOwnText,
                                                            String anchorElementTagName, String anchorElementOwnText, String searchCssQuery);

    List<WebElement> getWebElementsWithTwoAnchorsExactMatchBestEffort(WebDriver driver, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                            String anchorElementTagName, String anchorElementOwnText, String searchCssQuery);

    List<WebElement> getWebElementsWithTwoAnchorsExactMatch(WebDriver driver, String parentAnchorElementOwnText, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException;

    List<WebElement> getWebElementsWithTwoAnchorsExactMatch(WebDriver driver, String parentAnchorElementOwnText,
                                                            String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException;

    List<WebElement> getWebElementsWithTwoAnchorsExactMatch(WebDriver driver, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                            String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException;

    List<WebElement> getWebElementsWithTwoAnchorsBestEffort(WebDriver driver, ElementInfo parentAnchorElementInfo, ElementInfo anchorElementInfo, String searchCssQuery)
        throws AnchorIndexIfMultipleFoundOutOfBoundException;

    List<WebElement> getWebElementsWithTwoAnchors(WebDriver driver, ElementInfo parentAnchorElementInfo, ElementInfo anchorElementInfo, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException;

    /**
     * region get web elements from driver by anchor
     */
    List<WebElement> getWebElementsBestEffort(WebDriver driver, String anchorElementOwnText, String searchCssQuery);

    List<WebElement> getWebElementsBestEffort(WebDriver driver, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery);

    List<WebElement> getWebElements(WebDriver driver, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException;

    List<WebElement> getWebElements(WebDriver driver, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException;

    List<WebElement> getWebElementsExactMatchBestEffort(WebDriver driver, String anchorElementOwnText, String searchCssQuery);

    List<WebElement> getWebElementsExactMatchBestEffort(WebDriver driver, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery);

    List<WebElement> getWebElementsExactMatch(WebDriver driver, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException;

    List<WebElement> getWebElementsExactMatch(WebDriver driver, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException;

    List<WebElement> getWebElementsBestEffort(WebDriver driver, ElementInfo anchorElementInfo, String searchCssQuery)
            throws AnchorIndexIfMultipleFoundOutOfBoundException;

    List<WebElement> getWebElements(WebDriver driver, ElementInfo anchorElementInfo, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException;

    List<WebElement> getWebElementsBestEffort(WebDriver driver, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
            throws AnchorIndexIfMultipleFoundOutOfBoundException;

    List<WebElement> getWebElements(WebDriver driver, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException;

    /**
     * region get xpath from document by two anchors
     */
    String getXpathWithTwoAnchorsBestEffort(Document document, String parentAnchorElementOwnText, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundXpathsException;

    String getXpathWithTwoAnchorsBestEffort(Document document, String parentAnchorElementOwnText,
                                  String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundXpathsException;

    String getXpathWithTwoAnchorsBestEffort(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                  String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundXpathsException;

    String getXpathWithTwoAnchors(Document document, String parentAnchorElementOwnText, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException;

    String getXpathWithTwoAnchors(Document document, String parentAnchorElementOwnText,
                                  String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException;

    String getXpathWithTwoAnchors(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                  String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException;

    String getXpathWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementOwnText, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundXpathsException;

    String getXpathWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementOwnText,
                                            String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundXpathsException;

    String getXpathWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                            String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundXpathsException;

    String getXpathWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException;

    String getXpathWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText,
                                            String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException;

    String getXpathWithTwoAnchorsExactMatch(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                            String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException;

    String getXpathWithTwoAnchorsBestEffort(Document document, ElementInfo parentAnchorElementInfo, ElementInfo anchorElementInfo, String searchCssQuery)
            throws AnchorIndexIfMultipleFoundOutOfBoundException, AmbiguousFoundXpathsException;

    String getXpathWithTwoAnchors(Document document, ElementInfo parentAnchorElementInfo, ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException, AmbiguousFoundXpathsException;

    /**
     * region get xpath from document by anchor
     */
    String getXpathBestEffort(Document document, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundXpathsException;

    String getXpathBestEffort(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundXpathsException;

    String getXpath(Document document, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException;

    String getXpath(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException;

    String getXpathExactMatchBestEffort(Document document, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundXpathsException;

    String getXpathExactMatchBestEffort(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundXpathsException;

    String getXpathExactMatch(Document document, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException;

    String getXpathExactMatch(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException;

    String getXpathBestEffort(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
            throws AmbiguousFoundXpathsException, AnchorIndexIfMultipleFoundOutOfBoundException;

    String getXpath(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException, AnchorIndexIfMultipleFoundOutOfBoundException;

    String getXpathBestEffort(Document document, Elements anchorElements, ElementInfo searchElementInfo)
            throws AmbiguousFoundXpathsException;

    String getXpath(Document document, Elements anchorElements, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException;

    String getXpathBestEffort(Elements anchorElements, Elements searchElements)
            throws AmbiguousFoundXpathsException;

    String getXpath(Elements anchorElements, Elements searchElements)
        throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException;

    String getXpath(Element anchorElement, Elements searchElements)
            throws AmbiguousFoundXpathsException;

    /**
     * region get xpaths from document by two anchors
     */
    List<String> getXpathsWithTwoAnchorsBestEffort(Document document, String parentAnchorElementOwnText, String anchorElementOwnText, String searchCssQuery);

    List<String> getXpathsWithTwoAnchorsBestEffort(Document document, String parentAnchorElementOwnText, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery);

    List<String> getXpathsWithTwoAnchorsBestEffort(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                   String anchorElementTagName, String anchorElementOwnText, String searchCssQuery);

    List<String> getXpathsWithTwoAnchors(Document document, String parentAnchorElementOwnText, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException;

    List<String> getXpathsWithTwoAnchors(Document document, String parentAnchorElementOwnText, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException;

    List<String> getXpathsWithTwoAnchors(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                         String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException;

    List<String> getXpathsWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementOwnText,
                                                   String anchorElementOwnText, String searchCssQuery);

    List<String> getXpathsWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementOwnText,
                                                   String anchorElementTagName, String anchorElementOwnText, String searchCssQuery);

    List<String> getXpathsWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                   String anchorElementTagName, String anchorElementOwnText, String searchCssQuery);

    List<String> getXpathsWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException;

    List<String> getXpathsWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText,
                                                   String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException;

    List<String> getXpathsWithTwoAnchorsExactMatch(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                   String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException;

    List<String> getXpathsWithTwoAnchorsBestEffort(Document document, ElementInfo parentAnchorElementInfo, ElementInfo anchorElementInfo, String searchCssQuery)
        throws AnchorIndexIfMultipleFoundOutOfBoundException;

    List<String> getXpathsWithTwoAnchors(Document document, ElementInfo parentAnchorElementInfo, ElementInfo anchorElementInfo, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException;

    /**
     * region get xpaths from document by anchor
     */
    List<List<String>> getAllPossibleXpaths(Document document, String anchorElementOwnText, String searchCssQuery);

    List<List<String>> getAllPossibleXpaths(Document document, String anchorElementTagName, String anchorElementOwnText,
                                            String searchCssQuery);

    List<List<String>> getAllPossibleXpathsExactMatch(Document document, String anchorElementOwnText, String searchCssQuery);

    List<List<String>> getAllPossibleXpathsExactMatch(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery);

    List<List<String>> getAllPossibleXpaths(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
            throws AnchorIndexIfMultipleFoundOutOfBoundException;

    List<List<String>> getAllPossibleXpaths(Elements anchorElements, Elements searchElements);

    List<String> getXpathsBestEffort(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery);

    List<String> getXpaths(Document document, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException;

    List<String> getXpaths(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException;

    List<String> getXpathsExactMatchBestEffort(Document document, String anchorElementOwnText, String searchCssQuery);

    List<String> getXpathsExactMatchBestEffort(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery);

    List<String> getXpathsExactMatch(Document document, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException;

    List<String> getXpathsExactMatch(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException;

    List<String> getXpathsBestEffort(Document document, ElementInfo anchorElementInfo, String searchCssQuery)
            throws AnchorIndexIfMultipleFoundOutOfBoundException;

    List<String> getXpaths(Document document, ElementInfo anchorElementInfo, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException;

    List<String> getXpathsBestEffort(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
            throws AnchorIndexIfMultipleFoundOutOfBoundException;

    List<String> getXpaths(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException;

    List<String> getXpathsBestEffort(Document document, Elements anchorElements, ElementInfo searchElementInfo);

    List<String> getXpaths(Document document, Elements anchorElements, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException;

    List<String> getXpathsBestEffort(Elements anchorElements, Elements searchElements);

    List<String> getXpaths(Elements anchorElements, Elements searchElements)
        throws AmbiguousAnchorElementsException;

    List<String> getXpaths(Element anchorElement, Elements searchElements);

    /**
     * region get element from document by two anchors
     */
    Element getElementWithTwoAnchorsBestEffort(Document document, String parentAnchorElementOwnText,
                                     String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundElementsException;

    Element getElementWithTwoAnchorsBestEffort(Document document, String parentAnchorElementOwnText,
                                     String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundElementsException;

    Element getElementWithTwoAnchorsBestEffort(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                     String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundElementsException;

    Element getElementWithTwoAnchors(Document document, String parentAnchorElementOwnText,
                                     String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException;

    Element getElementWithTwoAnchors(Document document, String parentAnchorElementOwnText,
                                     String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException;

    Element getElementWithTwoAnchors(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                     String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException;

    Element getElementWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementOwnText,
                                                        String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundElementsException;

    Element getElementWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementOwnText,
                                                        String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundElementsException;

    Element getElementWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                        String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundElementsException;

    Element getElementWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText,
                                               String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException;

    Element getElementWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText,
                                               String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException;

    Element getElementWithTwoAnchorsExactMatch(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                               String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException;

    Element getElementWithTwoAnchorsBestEffort(Document document, ElementInfo parentAnchorElementInfo, ElementInfo anchorElementInfo, String searchCssQuery)
            throws AnchorIndexIfMultipleFoundOutOfBoundException, AmbiguousFoundElementsException;

    Element getElementWithTwoAnchors(Document document, ElementInfo parentAnchorElementInfo, ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException, AmbiguousFoundElementsException;

    /**
     * region get elements from document by two anchors
     */
    Elements getElementsWithTwoAnchorsBestEffort(Document document, String parentAnchorElementOwnText, String anchorElementOwnText, String searchCssQuery);

    Elements getElementsWithTwoAnchorsBestEffort(Document document, String parentAnchorElementOwnText,
                                                 String anchorElementTagName, String anchorElementOwnText, String searchCssQuery);

    Elements getElementsWithTwoAnchorsBestEffort(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                 String anchorElementTagName, String anchorElementOwnText, String searchCssQuery);

    Elements getElementsWithTwoAnchors(Document document, String parentAnchorElementOwnText, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException;

    Elements getElementsWithTwoAnchors(Document document, String parentAnchorElementOwnText,
                                       String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException;

    Elements getElementsWithTwoAnchors(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                       String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException;

    Elements getElementsWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementOwnText, String anchorElementOwnText, String searchCssQuery);

    Elements getElementsWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementOwnText,
                                                           String anchorElementTagName, String anchorElementOwnText, String searchCssQuery);

    Elements getElementsWithTwoAnchorsExactMatchBestEffort(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                           String anchorElementTagName, String anchorElementOwnText, String searchCssQuery);

    Elements getElementsWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText,
                                                 String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException;

    Elements getElementsWithTwoAnchorsExactMatch(Document document, String parentAnchorElementOwnText,
                                                 String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException;

    Elements getElementsWithTwoAnchorsExactMatch(Document document, String parentAnchorElementTagName, String parentAnchorElementOwnText,
                                                 String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException;

    Elements getElementsWithTwoAnchorsBestEffort(Document document, ElementInfo parentAnchorElementInfo, ElementInfo anchorElementInfo, String searchCssQuery)
            throws AnchorIndexIfMultipleFoundOutOfBoundException;

    Elements getElementsWithTwoAnchors(Document document, ElementInfo parentAnchorElementInfo, ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException;

    /**
     * region get element from document by anchor
     */
    Element getElementBestEffort(Document document, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException;

    Element getElementBestEffort(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException;

    Element getElement(Document document, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException;

    Element getElement(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException;

    Element getElementExactMatchBestEffort(Document document, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundElementsException;

    Element getElementExactMatchBestEffort(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousFoundElementsException;

    Element getElementExactMatch(Document document, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException;

    Element getElementExactMatch(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException;

    Element getElementBestEffort(Document document, ElementInfo anchorElementInfo, String searchCssQuery)
            throws AmbiguousFoundElementsException, AnchorIndexIfMultipleFoundOutOfBoundException;

    Element getElement(Document document, ElementInfo anchorElementInfo, String searchCssQuery)
            throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException, AnchorIndexIfMultipleFoundOutOfBoundException;

    Element getElementBestEffort(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
            throws AmbiguousFoundElementsException, AnchorIndexIfMultipleFoundOutOfBoundException;

    Element getElement(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException, AnchorIndexIfMultipleFoundOutOfBoundException;

    Element getElementBestEffort(Document document, Elements anchorElements, ElementInfo searchElementInfo)
            throws AmbiguousFoundElementsException;

    Element getElement(Document document, Elements anchorElements, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException;

    Element getElementBestEffort(Elements anchorElements, Elements searchElements)
            throws AmbiguousFoundElementsException;

    Element getElement(Elements anchorElements, Elements searchElements)
        throws AmbiguousAnchorElementsException, AmbiguousFoundElementsException;

    Element getElement(Element anchorElement, Elements searchElements)
            throws AmbiguousFoundElementsException;

    /**
     * region get elements from document by anchor
     */
    Elements getElementsBestEffort(Document document, String anchorElementOwnText, String searchCssQuery);

    Elements getElementsBestEffort(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery);

    Elements getElements(Document document, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException;

    Elements getElements(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException;

    Elements getElementsExactMatchBestEffort(Document document, String anchorElementOwnText, String searchCssQuery);

    Elements getElementsExactMatchBestEffort(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery);

    Elements getElementsExactMatch(Document document, String anchorElementOwnText, String searchCssQuery)
            throws AmbiguousAnchorElementsException;

    Elements getElementsExactMatch(Document document, String anchorElementTagName, String anchorElementOwnText, String searchCssQuery)
        throws AmbiguousAnchorElementsException;

    Elements getElementsBestEffort(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
            throws AnchorIndexIfMultipleFoundOutOfBoundException;

    Elements getElementsBestEffort(Document document, ElementInfo anchorElementInfo, String searchCssQuery)
            throws AnchorIndexIfMultipleFoundOutOfBoundException;

    Elements getElements(Document document, ElementInfo anchorElementInfo, String searchCssQuery)
        throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException;

    Elements getElements(Document document, ElementInfo anchorElementInfo, ElementInfo searchElementInfo)
            throws AmbiguousAnchorElementsException, AnchorIndexIfMultipleFoundOutOfBoundException;

    Elements getElementsBestEffort(Document document, Elements anchorElements, ElementInfo searchElementInfo)
            throws AmbiguousAnchorElementsException;

    Elements getElements(Document document, Elements anchorElements, ElementInfo searchElementInfo)
        throws AmbiguousAnchorElementsException;

    Elements getElementsBestEffort(Elements anchorElements, Elements searchElements);

    Elements getElements(Elements anchorElements, Elements searchElements)
        throws AmbiguousAnchorElementsException;

    Elements getElements(Element anchorElement, Elements searchElements);
}