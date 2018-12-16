package com.github.tamnguyenbbt.dom;

import com.github.tamnguyenbbt.task.IFunction;
import com.github.tamnguyenbbt.task.ITask;
import com.github.tamnguyenbbt.task.TaskFactory;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang.time.StopWatch; //this gets called instead of those of lang3 so use lang to avoid problem when lang and lang3 co-exist
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.io.File;
import java.io.IOException;
import java.util.*;

class DomCore
{
    protected final String ambiguousAnchorMessage = "%s anchor elements found";
    protected final String anchorIndexIfMultipleFoundOutOfBoundMessage = "indexIfMultipleFound property of the AnchorElementInfo provided is out of bound";
    protected final String ambiguousFoundElementsMessage = "%s elements found";
    protected final String ambiguousFoundXpathMessage = "%s xpaths found";
    protected final String ambiguousFoundWebElementMessage = "More than one web element found";
    DomUtilConfig config;
    protected Logger logger =  Logger.getLogger(this.getClass().getName());
    private IFunction<GetElementRecordParam, ElementRecord> getElementRecordFunc = this::getElementRecord;

    protected DomCore(DomUtilConfig config)
    {
        if(config == null)
        {
            this.config = new DomUtilConfig();
        }

        if(Util.hasNoItem(config.xpathBuildOptions))
        {
            config.xpathBuildOptions = new ArrayList<>();
            config.xpathBuildOptions.add(XpathBuildOption.AttachId);
            config.xpathBuildOptions.add(XpathBuildOption.AttachName);
        }

        if(config.webDriverTimeoutInMilliseconds <= 0)
        {
            config.webDriverTimeoutInMilliseconds = 2000;
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

    protected String buildXpath(Element anchorElement, ElementRecord record)
    {
        if(record == null || anchorElement == null)
        {
            return null;
        }

        Element rootElement = record.rootElement;
        Element element = record.element;
        MapEntry<String,String> xpaths = buildXpath(rootElement, anchorElement, element);

        String xpath = config.xpathBuildMethod == XpathBuildMethod.EqualText
            ? xpaths.getKey()
            : xpaths.getValue();

        return attachIdAndNameToXpath(element, xpath);
    }

    protected Elements getElements(Element anchorElement, Elements searchElements, SearchMethod searchMethod)
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

    protected List<ElementRecord> getElementRecords(Element anchorElement, Elements searchElements, SearchMethod searchMethod)
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

    protected boolean matchElementOwnText(Element element, String pattern, Condition condition)
    {
        Condition activeCondition = condition;

        if (condition == null)
        {
            activeCondition = new Condition();
        }

        if (element == null || element.ownText() == null || pattern == null)
        {
            return false;
        }

        String elementOwnText = activeCondition.whereIncludingTabsAndSpacesForOwnText ?
            element.ownText() :
            element.ownText().replace("\\s+", "");
        String patternWithoutSpaces = activeCondition.whereIncludingTabsAndSpacesForOwnText ?
            pattern :
            pattern.replace("\\s+", "");

        if (activeCondition.whereIgnoreCaseForOwnText && activeCondition.whereOwnTextContainingPattern)
        {
            if (elementOwnText.toLowerCase().contains(patternWithoutSpaces.toLowerCase()))
            {
                return true;
            }
        }
        else if (activeCondition.whereIgnoreCaseForOwnText)
        {
            if (elementOwnText.equalsIgnoreCase(patternWithoutSpaces))
            {
                return true;
            }
        }
        else if (activeCondition.whereOwnTextContainingPattern)
        {
            if (elementOwnText.contains(patternWithoutSpaces))
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

    protected Elements getElementsByPattern(Elements elements, String pattern, Condition condition)
    {
        Elements filtered = new Elements();

        if (Util.hasItem(elements))
        {
            for (Element item : elements)
            {
                if (matchElementOwnText(item, pattern, condition))
                {
                    filtered.add(item);
                }
            }
        }

        return filtered;
    }

    private String buildXpathPartBetweenRootAndLeafExcludingRoot(Element root, Element leaf)
    {
        List<Element> allElements = getElementsBetweenLeafAndRootInclusive(leaf, root);

        if (Util.hasNoItem(allElements))
        {
            return null;
        }

        int elementCount = allElements.size();

        if (elementCount == 1)
        {
            return "";
        }

        StringBuilder xpathBuilder = new StringBuilder();

        for (int i = elementCount - 2; i >= 0; i--)
        {
            String tagName = allElements.get(i).tagName();
            xpathBuilder.append(tagName);

            if (i > 0)
            {
                xpathBuilder.append("/");
            }
        }

        return xpathBuilder.toString();
    }

    private Elements getElementsByLinkAndShortestDistance(Element anchorElement, Elements searchElements)
    {
        Elements linkedElements = getLinkedElements(anchorElement, searchElements);
        Elements result;

        if (Util.hasItem(linkedElements))
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

        if (Util.hasItem(searchElements) && anchorElement != null)
        {
            Attribute anchorElementForAttribute = new TreeElement(anchorElement).getAttributeByNameContainingPattern("for");

            if (anchorElementForAttribute != null)
            {
                String anchorElementForAttributeValue = anchorElementForAttribute.getValue();

                for (Element item : searchElements)
                {
                    String searchElementId = getElementAttributeValue(item, "id");
                    String searchElementName = getElementAttributeValue(item, "name");

                    if ((searchElementId != null && searchElementId.trim()
                        .equalsIgnoreCase(anchorElementForAttributeValue)) ||
                        (searchElementName != null && searchElementName.trim().equals(anchorElementForAttributeValue)))
                    {
                        result.add(item);
                    }
                }
            }
        }

        return result;
    }

    private String getElementAttributeValue(Element element, String attributeKey)
    {
        if(attributeKey == null || element == null || !element.hasAttr(attributeKey))
        {
            return null;
        }

        return element.attr(attributeKey);
    }

    private List<ElementRecord> getElementRecordsByLinkAndShortestDistance(Element anchorElement, Elements searchElements)
    {
        List<ElementRecord> linkedElementRecords = getLinkedElementRecords(anchorElement, searchElements);
        List<ElementRecord> result;

        if(Util.hasItem(linkedElementRecords))
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

        if(Util.hasItem(elementRecords))
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

        if (Util.hasItem(searchElements) && anchorElement != null)
        {
            Attribute anchorElementForAttribute = new TreeElement(anchorElement).getAttributeByNameContainingPattern("for");

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

        if (Util.hasItem(searchElementRecords))
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
        String displayedAnchor = anchorElement == null ? "" : cutText(anchorElement.toString(), 100, true);
        logger.info(String.format("get element records for ANCHOR: '%s'", displayedAnchor));
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<ElementRecord> result;

        if(Util.hasNoItem(searchElements) || anchorElement == null)
        {
            result = new ArrayList<>();
        }
        else
        {
            List<ITask> getElementRecordTasks = new ArrayList<>();

            for (int i = 0 ; i < searchElements.size(); i++)
            {
                Element currentSearchElement = searchElements.get(i);
                GetElementRecordTask task = new GetElementRecordTask(getElementRecordFunc, new GetElementRecordParam(anchorElement, currentSearchElement, i));
                getElementRecordTasks.add(task);
            }

            result = new TaskFactory(getElementRecordTasks).run();
        }

        stopWatch.stop();
        logger.info(String.format("get element records for ANCHOR: '%s' - time in ms: %s", displayedAnchor, stopWatch.getTime()));
        return result;
    }

    private ElementRecord getElementRecord(GetElementRecordParam getElementRecordParam)
    {
        Element anchorElement = getElementRecordParam == null ? null : getElementRecordParam.anchorElement;
        Element searchElement = getElementRecordParam == null ? null : getElementRecordParam.searchElement;
        String displayedAnchor = anchorElement == null ? "" : cutText(anchorElement.toString(), 100, true);
        String displayedSearchElement = searchElement == null ? "" : cutText(searchElement.toString(), 100, true);
        logger.info(String.format("get element record for ANCHOR: '%s' and SEARCH ELEMENT '%s'",displayedAnchor, displayedSearchElement));

        if(anchorElement == null || searchElement == null)
        {
            return null;
        }

        anchorElement.attr(Tree.uniqueInsertedAttribute, UUID.randomUUID().toString());
        searchElement.attr(Tree.uniqueInsertedAttribute, UUID.randomUUID().toString());
        MapEntry<Position, Tree> matchedElementPositionAndTreePair = new TreeElement(searchElement).getContainingTree(anchorElement);
        ElementRecord currentFoundElementRecord = null;

        if(matchedElementPositionAndTreePair != null)
        {
            Tree tree = matchedElementPositionAndTreePair.getValue();
            Position matchedElementPosition = matchedElementPositionAndTreePair.getKey();
            TreeElement anchor = tree.getFirstMatchedTreeElement(new TreeElement(anchorElement));

            if(Util.hasItem(matchedElementPosition) && anchor != null && Util.hasItem(anchor.position))
            {
                currentFoundElementRecord = new ElementRecord();
                currentFoundElementRecord.element = searchElement;
                currentFoundElementRecord.rootElement = tree.getRootElement().element;
                currentFoundElementRecord.index = getElementRecordParam.searchElementIndex;
                currentFoundElementRecord.distanceToAnchorElement = matchedElementPosition.size() + anchor.position.size() - 2;
            }
        }

        return currentFoundElementRecord;
    }

    private List<Element> getElementsBetweenLeafAndRootInclusive(Element leaf, Element root)
    {
        List<Element> result = new ArrayList<>();
        result.add(leaf);
        Element currentParent = leaf;

        do
        {
            if(new TreeElement(currentParent).equals(new TreeElement(root)))
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

    private String getTag(Element element)
    {
        String outerHtml = element.outerHtml();
        return outerHtml == null ? null : outerHtml.substring(0, outerHtml.indexOf('>') + 1);
    }

    protected void gatherDataForDocumentTree(Tree documentTree)
    {
        List<TreeElement> anchors = documentTree.setAsAnchorCandidatesForAllTreeElements();
        documentTree.setDistancesAndLinksToAnchorsForAllTreeElements(anchors, false);
        updateXpathsForElementTree(documentTree);
    }

    private void updateXpathsForElementTree(Tree documentTree)
    {
        if(Util.hasItem(documentTree))
        {
            documentTree.forEach(x -> updateTreeElementXpaths(x, documentTree));
        }
    }

    private void updateTreeElementXpaths(TreeElement treeElement, Tree documentTree)
    {
        if(treeElement != null && treeElement.element != null)
        {
            Element element = treeElement.element;

            if(treeElement.asAnchorCandidate)
            {
                treeElement.uniqueXpaths.add(String.format("//%s[text()='%s']", element.tagName(), element.ownText()));
                treeElement.leastRefactoredXpaths.add(String.format("//%s[contains(text(),'%s')]", element.tagName(), removeLineSeparators(element.ownText()).trim()));
            }
            else
            {
                if(Util.hasItem(treeElement.linkedAnchors))
                {
                    Map.Entry<TreeElement, Attribute> linkedAnchorAndElementAttribute = treeElement.linkedAnchors.entrySet().iterator().next();
                    TreeElement linkedAnchor = linkedAnchorAndElementAttribute.getKey();
                    Position rootPositionForLinkedAnchor = treeElement.getRootPositionForLinkedAnchor(linkedAnchor);
                    TreeElement rootElement = documentTree.getTreeElementByPosition( rootPositionForLinkedAnchor);
                    rootElement.element.attr(Tree.uniqueInsertedAttribute, UUID.randomUUID().toString());
                    MapEntry<String,String> xpaths = buildXpath(rootElement.element, linkedAnchor.element, treeElement.element);
                    treeElement.uniqueXpaths.add(xpaths.getKey());
                    treeElement.leastRefactoredXpaths.add(xpaths.getValue());
                }
                else
                {
                    List<TreeElement> anchors = treeElement.getAnchorsByShortestDistanceDepth(2);

                    if(Util.hasItem(anchors))
                    {
                        anchors.forEach(x -> {
                            Position rootPosition = treeElement.getRootElementPosition(x);
                            TreeElement rootElement = documentTree.getTreeElementByPosition(rootPosition);
                            rootElement.element.attr(Tree.uniqueInsertedAttribute, UUID.randomUUID().toString());
                            MapEntry<String,String> xpaths = buildXpath(rootElement.element, x.element, treeElement.element);
                            treeElement.uniqueXpaths.add(xpaths.getKey());
                            treeElement.leastRefactoredXpaths.add(xpaths.getValue());
                        });
                    }
                }
            }

            treeElement.uniqueXpathsWithAttributes = attachIdAndNameToXpaths(treeElement.element, treeElement.uniqueXpaths);
            treeElement.leastRefactoredXpathsWithAttributes = attachIdAndNameToXpaths(treeElement.element, treeElement.leastRefactoredXpaths);
        }
    }

    private MapEntry<String,String> buildXpath(Element rootElement, Element anchorElement, Element element)
    {
        String uniqueXpath = null;
        String leastRefactoredXpath = null;
        String xpathPartFromRootElementToFoundElement = buildXpathPartBetweenRootAndLeafExcludingRoot(rootElement, element);
        String xpathPartFromRootElementToAnchorElement = buildXpathPartBetweenRootAndLeafExcludingRoot(rootElement, anchorElement);
        String rootElementTagName = rootElement.tagName();
        String anchorElementOwnText = anchorElement.ownText();

        if (xpathPartFromRootElementToFoundElement != null && xpathPartFromRootElementToAnchorElement != null)
        {
            if (xpathPartFromRootElementToAnchorElement == "" && xpathPartFromRootElementToFoundElement == "")
            {
                uniqueXpath = String.format("//%s[text()='%s']", rootElementTagName, anchorElementOwnText);
                leastRefactoredXpath = String.format("//%s[contains(text(),'%s')]", rootElementTagName, anchorElementOwnText);
            }
            else if (xpathPartFromRootElementToAnchorElement == "")
            {
                uniqueXpath = String.format("//%s[text()='%s']/%s", rootElementTagName, anchorElementOwnText,
                                            xpathPartFromRootElementToFoundElement);
                leastRefactoredXpath = String.format("//%s[contains(text(),'%s')]/%s", rootElementTagName, anchorElementOwnText,
                                                     xpathPartFromRootElementToFoundElement);
            }
            else if (xpathPartFromRootElementToFoundElement == "")
            {
                uniqueXpath = String.format("//%s[%s[text()='%s']]",
                                            rootElementTagName, xpathPartFromRootElementToAnchorElement, anchorElementOwnText);
                leastRefactoredXpath = String.format("//%s[%s[contains(text(),'%s')]]",
                                                     rootElementTagName, xpathPartFromRootElementToAnchorElement, anchorElementOwnText);
            }
            else
            {
                uniqueXpath =  String.format("//%s[%s[text()='%s']]/%s",
                                             rootElementTagName, xpathPartFromRootElementToAnchorElement, anchorElementOwnText,
                                             xpathPartFromRootElementToFoundElement);
                leastRefactoredXpath = String.format("//%s[%s[contains(text(),'%s')]]/%s",
                                                     rootElementTagName, xpathPartFromRootElementToAnchorElement, anchorElementOwnText,
                                                     xpathPartFromRootElementToFoundElement);
            }
        }

        return new MapEntry<>(uniqueXpath, leastRefactoredXpath);
    }

    private List<String> attachIdAndNameToXpaths(Element element, List<String> xpaths)
    {
        List<String> result = new ArrayList<>();

        if(element != null && Util.hasItem(xpaths))
        {
            xpaths.forEach(x -> result.add(attachIdAndNameToXpath(element, x)));
        }

        return result;
    }

    private String attachIdAndNameToXpath(Element element, String xpath)
    {
        String xpathWithAttributes = xpath;

        if(element != null && element != null)
        {
            Attribute idAttribute = new TreeElement(element).getAttributeByName("id");
            Attribute nameAttribute = new TreeElement(element).getAttributeByName("name");

            if(xpath != null)
            {
                if(idAttribute != null)
                {
                    xpathWithAttributes = String.format("%s[@id='%s']", xpathWithAttributes, idAttribute.getValue());
                }

                if(nameAttribute != null)
                {
                    xpathWithAttributes = String.format("%s[@name='%s']", xpathWithAttributes, nameAttribute.getValue());
                }
            }
        }

        return xpathWithAttributes;
    }

    private String cutText(String input, int toLength, boolean removeLineSeparators)
    {
        String output =  input == null ? null : input.substring(0, Math.min(input.length(), toLength));
        output = input.length() > toLength ? output + "..." : output;
        return removeLineSeparators ? removeLineSeparators(output) : output;
    }

    private String removeLineSeparators(String input)
    {
        String separator = File.separator;
        return input == null ? null : input.replace(separator, "").replace("\n", "").replace("\r", "");
    }
}