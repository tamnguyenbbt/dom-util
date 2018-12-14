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
    protected final String uniqueInsertedAttribute = "wusiwug";
    DomUtilConfig config;
    protected Logger logger =  Logger.getLogger(this.getClass().getName());
    private int searchDepth;
    private boolean getAnchorsForAnchor = false;
    private int shortestDistanceDepth = 5;
    private IFunction<GetElementRecordParam, ElementRecord> getElementRecordFunc = this::getElementRecord;

    protected DomCore(DomUtilConfig config)
    {
        if(config == null)
        {
            this.config = new DomUtilConfig();
        }

        if(hasNoItem(config.xpathBuildOptions))
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
        searchDepth = 5;
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

    protected Attribute getAttributeByName(Element element, String name)
    {
        if(element == null)
        {
            return null;
        }

        List<Attribute> anchorAttributes = element.attributes().asList();

        for(Attribute item : anchorAttributes)
        {
            String attributeKey = item.getKey();

            if(attributeKey.equalsIgnoreCase(name))
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

    protected <K,V> boolean hasItem(Map<K,V> map)
    {
        return (map != null && !map.isEmpty());
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
        String displayedAnchor = anchorElement == null ? "" : cutText(anchorElement.toString(), 100, true);
        logger.info(String.format("get element records for ANCHOR: '%s'", displayedAnchor));
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

        anchorElement.attr(uniqueInsertedAttribute, UUID.randomUUID().toString());
        searchElement.attr(uniqueInsertedAttribute, UUID.randomUUID().toString());
        MapEntry<Position, ElementTree> matchedElementPositionAndTreePair = getContainingTree(anchorElement, searchElement);
        ElementRecord currentFoundElementRecord = null;

        if(matchedElementPositionAndTreePair != null)
        {
            ElementTree tree = matchedElementPositionAndTreePair.getValue();
            Position matchedElementPosition = matchedElementPositionAndTreePair.getKey();
            TreeElement anchor = getFirstMatchedElementInTreeByUniqueInsertedAttribute(tree, anchorElement);

            if(hasItem(matchedElementPosition) && anchor != null && hasItem(anchor.position))
            {
                currentFoundElementRecord = new ElementRecord();
                currentFoundElementRecord.element = searchElement;
                currentFoundElementRecord.rootElement = getRootElement(tree).element;
                currentFoundElementRecord.index = getElementRecordParam.searchElementIndex;
                currentFoundElementRecord.distanceToAnchorElement = matchedElementPosition.size() + anchor.position.size() - 2;
            }
        }

        return currentFoundElementRecord;
    }

    private MapEntry<Position, ElementTree> getContainingTree(Element anchorElement, Element searchElement)
    {
        ElementTree elementTree = new ElementTree();
        Element rootElement = anchorElement;
        TreeElement firstFound = null;

        while(firstFound == null && rootElement != null)
        {
            elementTree = getElementTree(rootElement);
            firstFound = getFirstMatchedElementInTreeByUniqueInsertedAttribute(elementTree, searchElement);

            if(firstFound == null)
            {
                rootElement = rootElement.parent();

                if(rootElement != null)
                {
                    rootElement.attr(uniqueInsertedAttribute, UUID.randomUUID().toString());
                }
            }
        }

        return firstFound == null ? null : new MapEntry<>(firstFound.position, elementTree);
    }

    //TODO: poor performance - will be removed when getFirstMatchedElementInTreeByUniqueInsertedAttribute is proved to work better
    private TreeElement getFirstMatchedElementInTree(ElementTree elementTree, Element searchElement)
    {
        if(hasItem(elementTree))
        {
            for (TreeElement item : elementTree)
            {
                if (elementEquals(item.element, searchElement))
                {
                    return item;
                }
            }
        }

        return null;
    }

    private TreeElement getFirstMatchedElementInTreeByUniqueInsertedAttribute(ElementTree elementTree, Element searchElement)
    {
        if(hasItem(elementTree))
        {
            for (TreeElement item : elementTree)
            {
                if (elementEqualsByUniqueInsertedAttribute(item.element, searchElement))
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
            if(elementEqualsByUniqueInsertedAttribute(currentParent, root))
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

    //TODO: poor performance - will be removed when elementEqualsByUniqueInsertedAttribute is proved to work better
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

    private boolean elementEqualsByUniqueInsertedAttribute(Element element1, Element element2)
    {
        return element1 != null
               && element1.hasAttr(uniqueInsertedAttribute)
               && element2 != null
               && element2.hasAttr(uniqueInsertedAttribute)
               && element1.attr(uniqueInsertedAttribute).equals(element2.attr(uniqueInsertedAttribute));
    }

    private String getTag(Element element)
    {
        String outerHtml = element.outerHtml();
        return outerHtml == null ? null : outerHtml.substring(0, outerHtml.indexOf('>') + 1);
    }

    public void gatherDataForDocumentTree(ElementTree documentTree)
    {
        List<TreeElement> anchors = updateAsAnchorCandidatePropertyForTree(documentTree);
        updateDistancesAndLinksToAnchorsForElementTree(anchors, documentTree);
        updateXpathsForElementTree(documentTree);
    }

    private void updateXpathsForElementTree(ElementTree documentTree)
    {
        if(hasItem(documentTree))
        {
            documentTree.forEach(x -> updateTreeElementXpaths(x, documentTree));
        }
    }

    private void updateTreeElementXpaths(TreeElement treeElement, ElementTree documentTree)
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
                if(hasItem(treeElement.linkedAnchors))
                {
                    Map.Entry<TreeElement, Attribute> linkedAnchorAndElementAttribute = treeElement.linkedAnchors.entrySet().iterator().next();
                    TreeElement linkedAnchor = linkedAnchorAndElementAttribute.getKey();
                    Position rootPositionForLinkedAnchor = getRootPositionForLinkedAnchor(linkedAnchor, treeElement);
                    TreeElement rootElement = getRootElementByRootPosition(documentTree, rootPositionForLinkedAnchor);
                    rootElement.element.attr(uniqueInsertedAttribute, UUID.randomUUID().toString());
                    MapEntry<String,String> xpaths = buildXpath(rootElement.element, linkedAnchor.element, treeElement.element);
                    treeElement.uniqueXpaths.add(xpaths.getKey());
                    treeElement.leastRefactoredXpaths.add(xpaths.getValue());
                }
                else
                {
                    List<TreeElement> anchors = getAnchorsByShortestDistanceDepth(treeElement);

                    if(hasItem(anchors))
                    {
                        anchors.forEach(x -> {
                            Position rootPosition = getRootElementPosition(x, treeElement);
                            TreeElement rootElement = getRootElementByRootPosition(documentTree, rootPosition);
                            rootElement.element.attr(uniqueInsertedAttribute, UUID.randomUUID().toString());
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

    private List<TreeElement> getAnchorsByShortestDistanceDepth(TreeElement treeElement)
    {
        List<Integer> shortestDistances  = getNonDuplicatedShortestDistancesToAnchors(treeElement);
        List<TreeElement> result = new ArrayList<>();

        if(treeElement != null && hasItem(treeElement.distancesToAnchors) && hasItem(shortestDistances))
        {
            Set<Map.Entry<TreeElement, Integer>> distancesToAnchors = treeElement.distancesToAnchors.entrySet();

            for (Map.Entry<TreeElement, Integer> item : distancesToAnchors)
            {
                int distance = item.getValue();

                if(matchDistance(distance, shortestDistances))
                {
                    result.add(item.getKey());
                }

            }
        }

        return result;
    }

    private boolean matchDistance(int distance, List<Integer> patterns)
    {
        if(hasItem(patterns))
        {
            for (Integer item : patterns)
            {
                if(distance == item)
                {
                    return true;
                }
            }
        }

        return false;
    }

    private List<Integer> getNonDuplicatedShortestDistancesToAnchors(TreeElement treeElement)
    {
        List<Integer> distances = getDistancesToAnchors(treeElement);
        Set<Integer> set = new HashSet<>();
        set.addAll(distances);
        distances.clear();
        distances.addAll(set);
        Collections.sort(distances);
        return new ArrayList<>(distances.subList(0, shortestDistanceDepth - 1));
    }

    private List<Integer> getDistancesToAnchors(TreeElement treeElement)
    {
        List<Integer> distances = new ArrayList<>();

        if(treeElement != null && hasItem(treeElement.distancesToAnchors))
        {
            Set<Map.Entry<TreeElement, Integer>> distancesToAnchors = treeElement.distancesToAnchors.entrySet();

            for (Map.Entry<TreeElement, Integer> item : distancesToAnchors)
            {
                distances.add(item.getValue());
            }
        }

        return distances;
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

        if(element != null && hasItem(xpaths))
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
            Attribute idAttribute = getAttributeByName(element, "id");
            Attribute nameAttribute = getAttributeByName(element, "name");

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

    private Position getRootPositionForLinkedAnchor(TreeElement linkedAnchor, TreeElement treeElement)
    {
        if(treeElement != null && linkedAnchor != null && hasItem(treeElement.rootPositionsForAnchors))
        {
            Set<Map.Entry<TreeElement, Position>> rootPositionsForAnchors = treeElement.rootPositionsForAnchors.entrySet();

            for (Map.Entry<TreeElement, Position> item : rootPositionsForAnchors)
            {
                if(item.getKey().id.equals(linkedAnchor.id))
                {
                    return item.getValue();
                }
            }
        }

        return null;
    }

    private void updateAnchorsLinksAndDistancesForDocumentTree(ElementTree documentTree)
    {
        List<TreeElement> anchors = updateAsAnchorCandidatePropertyForTree(documentTree);
        updateDistancesAndLinksToAnchorsForElementTree(anchors, documentTree);
    }

    private void updateDistancesAndLinksToAnchorsForElementTree(List<TreeElement> anchors, ElementTree elementTree)
    {
        if(hasItem(anchors) && hasItem(elementTree))
        {
            elementTree.forEach(x->updateTreeElementDistancesAndLinksToAnchors(anchors, x));
        }
    }

    private void updateTreeElementDistancesAndLinksToAnchors(List<TreeElement> anchors, TreeElement treeElement)
    {
        if(hasItem(anchors) && treeElement != null)
        {
            anchors.forEach(x -> {

                if(!treeElement.asAnchorCandidate ||(treeElement.asAnchorCandidate && getAnchorsForAnchor))
                {
                    treeElement.distancesToAnchors.put(x, getDistanceFromElementToAnchor(x, treeElement));
                    treeElement.rootPositionsForAnchors.put(x, getRootElementPosition(x, treeElement));
                    Attribute linkedAttribute = getElementAttributeLinkedToAnchor(x, treeElement);

                    if(linkedAttribute != null)
                    {
                        treeElement.linkedAnchors.put(x, linkedAttribute);
                    }
                }
            });
        }
    }

    private Attribute getElementAttributeLinkedToAnchor(TreeElement anchor, TreeElement treeElement)
    {
        if(anchor != null && treeElement != null)
        {
            Attribute anchorForAttribute = getAttributeByNameContainingPattern(anchor.element, "for");

            if(anchorForAttribute != null)
            {
                String anchorForAttributeValue = anchorForAttribute.getValue();
                Attribute elementIdAttribute = getAttributeByName(treeElement.element, "id");

                if (elementIdAttribute != null && elementIdAttribute.getValue() != null && elementIdAttribute.getValue().trim().equalsIgnoreCase(anchorForAttributeValue))
                {
                    return elementIdAttribute;
                }
                else
                {
                    Attribute elementNameAttribute = getAttributeByName(treeElement.element, "name");

                    if(elementNameAttribute != null && elementNameAttribute.getValue() != null && elementNameAttribute.getValue().trim().equals(anchorForAttributeValue))
                    {
                        return elementNameAttribute;
                    }
                }
            }
        }

       return null;
    }

    private int getDistanceFromElementToAnchor(TreeElement anchor, TreeElement treeElement)
    {
        Position rootPosition = getRootElementPosition(anchor, treeElement);
        return anchor != null && hasItem(anchor.position) && treeElement != null &&
               hasItem(treeElement.position) && hasItem(rootPosition)
            ? anchor.position.size() + treeElement.position.size() - 2 * rootPosition.size()
            : 0;
    }

    private TreeElement getRootElementByRootPosition(ElementTree documentTree, Position rootPosition)
    {
        if(hasItem(documentTree) && hasItem(rootPosition))
        {
            for (TreeElement item : documentTree)
            {
                if(hasItem(item.position) && item.position.equals(rootPosition))
                {
                    return item;
                }
            }
        }

        return null;
    }

    private Position getRootElementPosition(TreeElement anchor, TreeElement treeElement)
    {
        Position rootPosition = new Position();

        if(anchor != null && hasItem(anchor.position) && treeElement != null && hasItem(treeElement.position))
        {
            Position anchorPosition = anchor.position;
            Position elementPosition = treeElement.position;

            for(int i = 0; i < anchorPosition.size(); i++)
            {
                if(i < elementPosition.size() && anchorPosition.get(i).equals(elementPosition.get(i)))
                {
                    rootPosition.add(anchorPosition.get(i));
                }
                else if(i < elementPosition.size() && !anchorPosition.get(i).equals(elementPosition.get(i)))
                {
                    break;
                }
            }
        }

        return rootPosition;
    }

    private  List<TreeElement> updateAsAnchorCandidatePropertyForTree(ElementTree documentTree)
    {
        List<TreeElement> anchors = new ArrayList<>();
        documentTree.forEach(x -> {
            updateAsAnchorCandidatePropertyForTreeElement(documentTree, x);

            if(x.asAnchorCandidate)
            {
                anchors.add(x);
            }
        });

        return anchors;
    }

    private void updateAsAnchorCandidatePropertyForTreeElement(ElementTree documentTree, TreeElement treeElement)
    {
        if(treeElement != null)
        {
            List<TreeElement> elementsWithSameOwnText = getTreeElementsHavingTheSameOwnText(documentTree, treeElement);

            if(elementsWithSameOwnText.size() > 1)
            {
                treeElement.elementsWithSameOwnText = elementsWithSameOwnText;
            }

            if(elementsWithSameOwnText.size() == 1 && treeElement.element != null &&
               treeElement.element.ownText() != null && !treeElement.element.ownText().trim().equals(""))
            {
                treeElement.asAnchorCandidate = true;
            }
        }
    }

    private List<TreeElement> getTreeElementsHavingTheSameOwnText(ElementTree documentTree, TreeElement treeElement)
    {
        List<TreeElement> result = new ArrayList<>();

        if(hasItem(documentTree) && treeElement != null)
        {
            documentTree.forEach(x -> {

                if(x != null && x.element != null && x.element.ownText() != null &&
                   treeElement.element != null && treeElement.element.ownText() != null &&
                   !treeElement.element.ownText().trim().equals("") && x.element.ownText().equals(treeElement.element.ownText()))
                {
                    result.add(x);
                }
            });
        }

        return result;
    }

    public ElementTree getDocumentTree(Document document)
    {
        ElementTree tree = new ElementTree();

        if(document == null)
        {
            return tree;
        }

        Elements documentElements = document.select("html");

        if(hasItem(documentElements))
        {
            tree = getElementTree(documentElements.get(0));
        }

        return tree;
    }

    private ElementTree getElementTree(Element element)
    {
        ElementTree tree = new ElementTree();
        TreeElement rootElement = new TreeElement();
        rootElement.position.add(0);
        rootElement.element = element;
        tree.add(rootElement);
        ElementTree allChildren = getAllChildren(element, rootElement.position);
        tree.addAll(allChildren);

        return tree;
    }

    private Element getHtmlElement(Document document)
    {
        Elements elements = document == null ? null : document.select("html");
        return hasItem(elements) ? elements.first() : null;
    }

    private TreeElement getRootElement(ElementTree tree)
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

    private ElementTree getAllChildren(Element element, Position startingPosition)
    {
        ElementTree result = new ElementTree();

        if(element != null)
        {
            Elements children = element.children();

            if(hasItem(children))
            {
                for(int i = 0; i < children.size(); i++)
                {
                    TreeElement treeElement = new TreeElement();
                    treeElement.position = new Position(startingPosition);
                    treeElement.position.add(i);
                    treeElement.element = children.get(i);
                    result.add(treeElement);
                    ElementTree nextResult = getAllChildren(treeElement.element, treeElement.position);
                    result.addAll(nextResult);
                }
            }
        }

        return result;
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
