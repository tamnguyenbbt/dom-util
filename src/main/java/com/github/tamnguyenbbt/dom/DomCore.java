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
import java.util.ArrayList;
import java.util.List;

class DomCore
{
    protected final String ambiguousAnchorMessage = "%s anchor elements found";
    protected final String anchorIndexIfMultipleFoundOutOfBoundMessage = "indexIfMultipleFound property of the AnchorElementInfo provided is out of bound";
    protected final String ambiguousFoundElementsMessage = "%s elements found";
    protected final String ambiguousFoundXpathMessage = "%s xpaths found";
    protected final String ambiguousFoundWebElementMessage = "More than one web element found";
    protected int searchDepth;
    protected int timeoutInMs;
    protected Logger logger =  Logger.getLogger(this.getClass().getName());

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
        String xpath = null;
        Element rootElement = record.rootElement;
        Element foundElement = record.element;
        String xpathPartFromRootElementToFoundElement = buildXpathPartBetweenRootAndLeafExcludingRoot(rootElement,
                                                                                                      foundElement);
        String xpathPartFromRootElementToAnchorElement = buildXpathPartBetweenRootAndLeafExcludingRoot(rootElement,
                                                                                                       anchorElement);
        String rootElementTagName = rootElement.tagName();
        String anchorElementOwnText = anchorElement.ownText();

        if (xpathPartFromRootElementToFoundElement != null && xpathPartFromRootElementToAnchorElement != null)
        {
            if (xpathPartFromRootElementToAnchorElement == "" && xpathPartFromRootElementToFoundElement == "")
            {
                xpath = String.format("//%s[contains(text(),'%s')]", rootElementTagName, anchorElementOwnText);
            }
            else if (xpathPartFromRootElementToAnchorElement == "")
            {
                xpath = String.format("//%s[contains(text(),'%s')]/%s", rootElementTagName, anchorElementOwnText,
                                      xpathPartFromRootElementToFoundElement);
            }
            else if (xpathPartFromRootElementToFoundElement == "")
            {
                xpath = String.format("//%s[%s[contains(text(),'%s')]]", xpathPartFromRootElementToAnchorElement,
                                      rootElementTagName, anchorElementOwnText);
            }
            else
            {
                xpath = String.format("//%s[%s[contains(text(),'%s')]]/%s",
                                      rootElementTagName, xpathPartFromRootElementToAnchorElement, anchorElementOwnText,
                                      xpathPartFromRootElementToFoundElement);
            }
        }

        return xpath;
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
    
    protected Attribute getAttributeByNameContainingPattern(Element element, String pattern)
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

    protected <T> boolean hasItem(List<T> list)
    {
        return (list != null && !list.isEmpty());
    }

    protected <T> boolean hasNoItem(List<T> list)
    {
        return (list == null || list.isEmpty());
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

        if (hasItem(elements))
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

        if (hasNoItem(allElements))
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

        if (hasItem(linkedElements))
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

            if (anchorElementForAttribute != null)
            {
                String anchorElementForAttributeValue = anchorElementForAttribute.getValue();

                for (Element item : searchElements)
                {
                    String searchElementId = item.attr("id");
                    String searchElementName = item.attr("name");

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
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<ElementRecord> result;

        if(hasNoItem(searchElements) || anchorElement == null)
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
        logger.info(String.format("get element records for ANCHOR '%s' - time in ms: %s", anchorElement, stopWatch.getTime()));
        return result;
    }

    private IFunction<GetElementRecordParam, ElementRecord> getElementRecordFunc = this::getElementRecord;

    private ElementRecord getElementRecord(GetElementRecordParam getElementRecordParam)
    {
        logger.info(String.format("get element record for ANCHOR '%s' and SEARCH ELEMENT '%s'", getElementRecordParam.anchorElement, getElementRecordParam.searchElement));
        MapEntry<List<Integer>, List<TreeElement>> matchedElementPositionAndTreePair =
            getContainingTree(getElementRecordParam.anchorElement, getElementRecordParam.searchElement);
        ElementRecord currentFoundElementRecord = null;

        if(matchedElementPositionAndTreePair != null)
        {
            List<TreeElement> tree = matchedElementPositionAndTreePair.getValue();
            List<Integer> matchedElementPosition = matchedElementPositionAndTreePair.getKey();
            TreeElement anchor = getFirstMatchedElementInTree(tree, new TreeElement(getElementRecordParam.anchorElement));

            if(hasItem(matchedElementPosition) && anchor != null && hasItem(anchor.position))
            {
                currentFoundElementRecord = new ElementRecord();
                currentFoundElementRecord.element = getElementRecordParam.searchElement;
                currentFoundElementRecord.rootElement = getRootElement(tree).element;
                currentFoundElementRecord.index = getElementRecordParam.searchElementIndex;
                currentFoundElementRecord.distanceToAnchorElement = matchedElementPosition.size() + anchor.position.size() - 2;
            }
        }

        return currentFoundElementRecord;
    }

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

    private Element getHtmlElement(Document document)
    {
        Elements elements = document == null ? null : document.select("html");
        return hasItem(elements) ? elements.first() : null;
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
}
