package dnn.tam.util.dom;

import dnn.tam.util.exception.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.io.File;
import java.io.IOException;
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
public class DomUtil
{
    /**
     * get jsoup document from a browser managed by Selenium WebDriver
     *
     * @param driver Selenium WebDriver
     * @return jsoup document. Null if driver is null
     */
    public static Document getActiveDocument(WebDriver driver)
    {
        if(driver == null)
        {
            return null;
        }

        WebElement htmlElement = driver.findElement(By.xpath("//html"));
        String htmlContent = htmlElement.getAttribute("innerHTML");
        return DomUtil.getDocument(htmlContent);
    }

    /**
     * get jsoup document from a file. jsoup parsing file with UTF-8 charset
     *
     * @param path path to html file
     * @return jsoup document. Null if the file not exist
     * @exception IOException
     */
    public static Document htmlFileToDocument(String path) throws IOException
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
    public static Document htmlFileToDocument(String path, String charsetName) throws IOException
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
    public static Document getDocument(String htmlContent)
    {
        return Jsoup.parse(htmlContent);
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
    public static WebElement findElement(
            WebDriver driver,
            String anchorElementOwnText,
            String searchCssQuery)
            throws NoAnchorElementFoundException, AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        return findElement(driver, null, anchorElementOwnText, searchCssQuery);
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
    public static WebElement findElement(
            WebDriver driver,
            String anchorElementTagName,
            String anchorElementOwnText,
            String searchCssQuery)
            throws NoAnchorElementFoundException, AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException
    {
        Document document = DomUtil.getActiveDocument(driver);

        if(document == null)
        {
            return null;
        }

        String xpath;

        try
        {
            xpath = getXPath(document, anchorElementTagName, anchorElementOwnText, searchCssQuery);
        }
        catch(AmbiguousFoundXPathsException e)
        {
            throw new AmbiguousFoundWebElementsException("More than one web element found");
        }

        return xpath == null ? null : driver.findElement(By.xpath(xpath));
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
    public static String getXPath(
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
    public static String getXPath(
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
     * @param anchorElement unique jsoup element in DOM document.
     *                      The anchor element should be easy to be located on a web page with unique text such as a label
     * @param searchElements possible jsoup element candidates to search from
     * @return xpath string based on the minimal DOM distance from possible found element to anchor element.
     *         Null if no xpath found
     * @exception AmbiguousFoundXPathsException when more than one search xpaths are found.
     *                                          This occurs when the found elements having the same DOM distances to the anchor element
     */
    public static String getXPath(Element anchorElement, Elements searchElements)
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
    public static List<String> getXPaths(
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
    public static List<String> getXPaths(
            Document document,
            String anchorElementTagName,
            String anchorElementOwnText,
            String searchCssQuery)
            throws NoAnchorElementFoundException, AmbiguousAnchorElementsException
    {
        List<String> xpathList = new ArrayList<>();

        if(document == null)
        {
            return xpathList;
        }

        List<Element> anchorElements = anchorElementTagName == null
                ? getElementsMatchingOwnText(document, anchorElementOwnText)
                : getElementsByTagNameMatchingOwnText(document, anchorElementTagName, anchorElementOwnText);

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
    public static List<String> getXPaths(Element anchorElement, Elements searchElements)
    {
        List<String> xpathList = new ArrayList<>();
        List<SearchElementRecord> foundElementRecords = getClosestSearchElementsFromAnchorElement(anchorElement, searchElements);

        if(hasNoItem(foundElementRecords))
        {
            return xpathList;
        }

        for(SearchElementRecord record : foundElementRecords)
        {
            Element rootElement = record.rootElement;
            Element foundElement = record.element;
            String xpathPartFromRootElementToFoundElement = buildXpathPartBetweenRootAndLeafExcludingRoot(rootElement, foundElement);
            String xpathPartFromRootElementToAnchorElement = buildXpathPartBetweenRootAndLeafExcludingRoot(rootElement, anchorElement);
            String rootElementTagName = rootElement.tagName();
            String anchorElementOwnText = anchorElement.ownText();

            if(xpathPartFromRootElementToFoundElement != null && xpathPartFromRootElementToAnchorElement != null)
            {
                String xpath = String.format("//%s[%s[contains(text(),'%s')]]/%s",
                        rootElementTagName, xpathPartFromRootElementToAnchorElement, anchorElementOwnText, xpathPartFromRootElementToFoundElement);
                xpathList.add(xpath);
            }
        }

        return xpathList;
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
    public static Element getClosestElement(
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
    public static Element getClosestElement(
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
     * get jsoup element closest to anchor element
     *
     * @param anchorElement unique jsoup element in DOM document.
     *                      The anchor element should be easy to be located on a web page with unique text such as a label
     * @param searchElements possible jsoup element candidates to search from
     * @return jsoup element based on the minimal DOM distance from possible found element to anchor element.
     *         Null if no element found
     */
    public static Element getClosestElement(Element anchorElement, Elements searchElements)
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
    public static List<Element> getClosestElements(
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
    public static List<Element> getClosestElements(
            Document document,
            String anchorElementTagName,
            String anchorElementOwnText,
            String searchCssQuery)
            throws NoAnchorElementFoundException, AmbiguousAnchorElementsException
    {
        List<Element> foundElements = new ArrayList<>();

        if(document == null)
        {
            return foundElements;
        }

        List<Element> anchorElements = anchorElementTagName == null
                ? getElementsMatchingOwnText(document, anchorElementOwnText)
                : getElementsByTagNameMatchingOwnText(document, anchorElementTagName, anchorElementOwnText);

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
        List<SearchElementRecord> foundElementRecords = getClosestSearchElementsFromAnchorElement(anchorElement, searchElements);

        if(hasItem(searchElements) && hasItem(foundElementRecords))
        {
            for(int i = 0; i <searchElements.size(); i++)
            {
                for(SearchElementRecord record : foundElementRecords)
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
    public static List<Element> getClosestElements(Element anchorElement, Elements searchElements)
    {
        List<Element> elements = new ArrayList<>();
        List<SearchElementRecord> foundElementRecords = getClosestSearchElementsFromAnchorElement(anchorElement, searchElements);

        if(hasItem(foundElementRecords))
        {
            for(SearchElementRecord record : foundElementRecords)
            {
                elements.add(record.element);
            }
        }

        return elements;
    }

    /**
     * get jsoup element list from jsoup document by tag name where the tag own text contains a pattern with case insensitive
     *
     * @param document DOM document
     * @param tagName tag name, case insensitive
     * @param pattern a string which the tag own text should contain, case insensitive. Spaces and tabs are ignored
     * @return jsoup element list
     */
    public static List<Element> getElementsByTagNameContainingOwnTextIgnoreCase(Document document, String tagName, String pattern)
    {
        return getElementsByTagNameMatchingOwnText(document, tagName, pattern, false, false);
    }

    /**
     * get jsoup element list from jsoup document by tag name where the tag own text contains a pattern with case sensitive
     *
     * @param document DOM document
     * @param tagName tag name, case insensitive
     * @param pattern a string which the tag own text should contain, case sensitive. Spaces and tabs are ignored
     * @return jsoup element list
     */
    public static List<Element> getElementsByTagNameContainingOwnText(Document document, String tagName, String pattern)
    {
        return getElementsByTagNameMatchingOwnText(document, tagName, pattern, true, false);
    }

    /**
     * get jsoup element list from jsoup document by tag name where the tag own text matches a pattern with case insensitive
     *
     * @param document DOM document
     * @param tagName tag name, case insensitive
     * @param pattern a string which the tag own text should match, case insensitive. Spaces and tabs are ignored
     * @return jsoup element list
     */
    public static List<Element> getElementsByTagNameMatchingOwnTextIgnoreCase(Document document, String tagName, String pattern)
    {
        return getElementsByTagNameMatchingOwnText(document, tagName, pattern, false, true);
    }

    /**
     * get jsoup element list from jsoup document by tag name where the tag own text matches a pattern with case sensitive
     *
     * @param document DOM document
     * @param tagName tag name, case insensitive
     * @param pattern a string which the tag own text should match, case sensitive. Spaces and tabs are ignored
     * @return jsoup element list
     */
    public static List<Element> getElementsByTagNameMatchingOwnText(Document document, String tagName, String pattern)
    {
        return getElementsByTagNameMatchingOwnText(document, tagName, pattern, true, true);
    }

    /**
     * get jsoup element list from jsoup document where the tag own text contains a pattern with case insensitive
     *
     * @param document DOM document
     * @param pattern a string which the tag own text should contain, case insensitive. Spaces and tabs are ignored
     * @return jsoup element list
     */
    public static List<Element> getElementsContainingOwnTextIgnoreCase(Document document, String pattern)
    {
        return getElementsMatchingOwnText(document, pattern, false, false);
    }

    /**
     * get jsoup element list from jsoup document where the tag own text contains a pattern with case sensitive
     *
     * @param document DOM document
     * @param pattern a string which the tag own text should contain, case sensitive. Spaces and tabs are ignored
     * @return jsoup element list
     */
    public static List<Element> getElementsContainingOwnText(Document document, String pattern)
    {
        return getElementsMatchingOwnText(document, pattern, true, false);
    }

    /**
     * get jsoup element list from jsoup document where the tag own text matches a pattern with case insensitive
     *
     * @param document DOM document
     * @param pattern a string which the tag own text should match, case insensitive. Spaces and tabs are ignored
     * @return jsoup element list
     */
    public static List<Element> getElementsMatchingOwnTextIgnoreCase(Document document, String pattern)
    {
        return getElementsMatchingOwnText(document, pattern, false, true);
    }

    /**
     * get jsoup element list from jsoup document where the tag own text matches a pattern with case sensitive
     *
     * @param document DOM document
     * @param pattern a string which the tag own text should match, case sensitive. Spaces and tabs are ignored
     * @return jsoup element list
     */
    public static List<Element> getElementsMatchingOwnText(Document document, String pattern)
    {
        return getElementsMatchingOwnText(document, pattern, true, true);
    }

    private static List<SearchElementRecord> getClosestSearchElementsFromAnchorElement(Element anchorElement, Elements searchElements)
    {
        List<SearchElementRecord> foundElementRecords = new ArrayList<>();

        if(hasNoItem(searchElements) || anchorElement == null)
        {
            return foundElementRecords;
        }

        for (int i = 0 ; i < searchElements.size(); i++)
        {
            Element currentSearchElement = searchElements.get(i);
            MapEntry<List<Integer>, List<TreeElement>> matchedElementPositionAndTreePair = buildTreeContainingBothSearchAndAnchorElements(anchorElement, currentSearchElement);

            if(matchedElementPositionAndTreePair != null)
            {
                List<TreeElement> tree = matchedElementPositionAndTreePair.getValue();
                List<Integer> currentSearchElementPosition = matchedElementPositionAndTreePair.getKey();
                TreeElement anchor = getFirstMatchedElementInTree(tree, anchorElement);

                if(hasItem(currentSearchElementPosition) && anchor != null && hasItem(anchor.position))
                {
                    SearchElementRecord currentFoundElementRecord = new SearchElementRecord();
                    currentFoundElementRecord.element = currentSearchElement;
                    currentFoundElementRecord.rootElement = getTreeRootElement(tree).element;
                    currentFoundElementRecord.index = i;
                    currentFoundElementRecord.distanceToAnchorElement = currentSearchElementPosition.size() + anchor.position.size() - 2;
                    foundElementRecords.add(currentFoundElementRecord);
                }
            }
        }

        return getFoundElementRecordsWithShortestDistanceToAnchorElement(foundElementRecords);
    }

    /**
     * @return map entry of the position of the search element and the tree
     */
    private static MapEntry<List<Integer>, List<TreeElement>> buildTreeContainingBothSearchAndAnchorElements(Element anchorElement, Element searchElement)
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
                rootElement = rootElement.parent();
            }
        }

        return firstFound == null ? null : new MapEntry<>(firstFound.position, elementTree);
    }

    private static TreeElement getFirstMatchedElementInTree(List<TreeElement> elementTree, Element searchElement)
    {
        if(hasItem(elementTree))
        {
            for (TreeElement item : elementTree)
            {
                if (item.element != null && item.element.equals(searchElement))
                {
                    return item;
                }
            }
        }

        return null;
    }

    private static List<SearchElementRecord> getFoundElementRecordsWithShortestDistanceToAnchorElement(List<SearchElementRecord> foundElementRecords)
    {
        int shortestDistance = -1;
        List<SearchElementRecord> searchElementRecordsWithShortestDistance = new ArrayList<>();

        if (hasItem(foundElementRecords))
        {
            for (SearchElementRecord item : foundElementRecords)
            {
                int distance = item.distanceToAnchorElement;

                if (shortestDistance == -1 || distance < shortestDistance)
                {
                    shortestDistance = distance;
                    searchElementRecordsWithShortestDistance = new ArrayList<>();
                    searchElementRecordsWithShortestDistance.add(item);
                }
                else if (distance == shortestDistance)
                {
                    searchElementRecordsWithShortestDistance.add(item);
                }
            }
        }

        return  searchElementRecordsWithShortestDistance;
    }

    private static List<Element> getElementsByTagNameMatchingOwnText(Document document, String tagName, String pattern, boolean caseSensitive, boolean exactMatch)
    {
        List<Element> elements = getElementsByTagName(document, tagName);
        return getElementsMatchingOwnText(elements, pattern, caseSensitive, exactMatch);
    }

    private static List<Element> getElementsMatchingOwnText(Document document, String pattern, boolean caseSensitive, boolean exactMatch)
    {
        List<Element> elements = document == null ? new ArrayList<>() : document.getAllElements();
        return getElementsMatchingOwnText(elements, pattern, caseSensitive, exactMatch);
    }

    private static List<Element> getElementsMatchingOwnText(List<Element> elements, String pattern, boolean caseSensitive, boolean exactMatch)
    {
        List<Element> result = new ArrayList<>();

        if(hasItem(elements))
        {
            for (Element item : elements)
            {
                if(matchElementOwnText(item, pattern, caseSensitive, exactMatch))
                {
                    result.add(item);
                }
            }
        }

        return result;
    }

    private static boolean matchElementOwnText(Element element, String pattern, boolean caseSensitive, boolean exactMatch)
    {
        if(element == null || element.ownText() == null || pattern == null)
        {
            return false;
        }

        String elementOwnText = element.ownText().replace("\\s+", "");
        String patternWithoutSpaces = pattern.replace("\\s+", "");

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

    private static List<Element> getElementsByTagName(Document document, String tagName)
    {
        List<Element> result = new ArrayList<>();

        if(document != null)
        {
            List<TreeElement> elementTree = getHtmlDocumentElementTree(document);

            for (TreeElement item : elementTree)
            {
                String elementTagName = item.element.tagName();

                if(elementTagName.trim().equalsIgnoreCase(tagName.trim()))
                {
                    result.add(item.element);
                }
            }
        }

        return result;
    }

    private static String buildXpathPartBetweenRootAndLeafExcludingRoot(Element root, Element leaf)
    {
        List<Element> allElements = getElementsBetweenLeafAndRootInclusive(leaf, root);

        if(hasNoItem(allElements))
        {
            return null;
        }

        StringBuilder xpathBuilder = new StringBuilder();
        int elementCount = allElements.size();

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

    private static List<Element> getElementsBetweenLeafAndRootInclusive(Element leaf, Element root)
    {
        List<Element> result = new ArrayList<>();
        result.add(leaf);
        Element currentParent = leaf;

        do
        {
            currentParent = currentParent.parent();
            result.add(currentParent);

            if(currentParent.equals(root))
            {
                break;
            }
        }
        while(true);

        return result;
    }

    /**
     * same outcome as jsoup.getAllElements()
     */
    private static List<TreeElement> getHtmlDocumentElementTree(Document document)
    {
        Element htmlElement = getHtmlElement(document);
        return htmlElement == null ? new ArrayList<>() : getElementTree(htmlElement);
    }

    private static TreeElement getTreeRootElement(List<TreeElement> tree)
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

    private static List<TreeElement> getElementTree(Element element)
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

    private static List<TreeElement> getAllChildren(Element element, List<Integer> startingPosition)
    {
        List<TreeElement> result = new ArrayList<>();
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

        return result;
    }

    private static Element getHtmlElement(Document document)
    {
        Elements elements = document == null ? null : document.select("html");
        return hasItem(elements) ? elements.first() : null;
    }

    private static <T> boolean hasItem(List<T> list)
    {
        return (list != null && !list.isEmpty());
    }

    private static <T> boolean hasNoItem(List<T> list)
    {
        return (list == null || list.isEmpty());
    }
}