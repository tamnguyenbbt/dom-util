package com.github.tamnguyenbbt.dom;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.*;

public class Tree extends ArrayList<TreeElement>
{
    protected final static String uniqueInsertedAttribute = "wusiwug";

    protected TreeElement root;
    protected TreeElement leaf;
    protected TreeElement anchor;
    protected boolean getAnchorForAnchors = false;
    protected boolean includeTagIndex = true;

    protected Tree()
    {
        super();
    }

    protected Tree(Element rootElement)
    {
        this();
        this.root = new TreeElement(rootElement);
        buildTree(this.root);
    }

    protected Tree(Document document)
    {
        this();

        if(document != null)
        {
            Elements documentElements = document.select("html");

            if(Util.hasItem(documentElements))
            {
                this.root = new TreeElement(documentElements.get(0));
                buildTree(this.root);

                List<TreeElement> anchors = setAsAnchorCandidatesForAllTreeElements();
                setDistancesAndLinksToAnchorsForAllTreeElements(anchors, getAnchorForAnchors);
                updateXpaths(includeTagIndex);
            }
        }
    }

    protected Tree(Element element, Element anchorElement)
    {
        this();
        TreeElement anchor = new TreeElement(anchorElement);
        TreeElement root = anchor;
        TreeElement treeElement = new TreeElement(element);
        TreeElement firstFound = null;

        while(firstFound == null && root.element != null && element != null)
        {
            buildTree(root);
            firstFound = getFirstMatchedTreeElement(treeElement);

            if(firstFound == null)
            {
                root = new TreeElement(root.element.parent());
            }
        }

        this.root = root;
        leaf = firstFound;
        this.anchor = getFirstMatchedTreeElement(anchor);
    }

    protected TreeElement getRoot()
    {
        return root;
    }

    protected TreeElement getLeaf()
    {
        return leaf;
    }

    protected TreeElement getAnchor()
    {
        return anchor;
    }

    protected void updateXpaths(boolean includeTagIndex)
    {
        this.forEach(x -> updateTreeElementXpaths(x, includeTagIndex));
    }

    protected void updateTreeElementXpaths(TreeElement treeElement, boolean includeTagIndex)
    {
        if(treeElement != null && treeElement.element != null)
        {
            Element element = treeElement.element;

            if(treeElement.asAnchorCandidate)
            {
                treeElement.uniqueXpaths.add(String.format("//%s[text()='%s']", element.tagName(), element.ownText()));
                treeElement.leastRefactoredXpaths.add(String.format("//%s[contains(text(),'%s')]", element.tagName(), Util.removeLineSeparators(element.ownText()).trim()));
            }
            else
            {
                if(Util.hasItem(treeElement.linkedAnchors))
                {
                    Map.Entry<TreeElement, Attribute> linkedAnchorAndElementAttribute = treeElement.linkedAnchors.entrySet().iterator().next();
                    TreeElement linkedAnchor = linkedAnchorAndElementAttribute.getKey();
                    Position rootPositionForLinkedAnchor = treeElement.getRootPositionForLinkedAnchor(linkedAnchor);
                    TreeElement rootElement = getTreeElementByPosition( rootPositionForLinkedAnchor);
                    rootElement.element.attr(Tree.uniqueInsertedAttribute, UUID.randomUUID().toString());
                    MapEntry<String,String> xpaths = buildXpath(rootElement, linkedAnchor, treeElement, includeTagIndex);
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
                            TreeElement rootElement = getTreeElementByPosition(rootPosition);
                            rootElement.element.attr(Tree.uniqueInsertedAttribute, UUID.randomUUID().toString());
                            MapEntry<String,String> xpaths = buildXpath(rootElement, x, treeElement, includeTagIndex);
                            treeElement.uniqueXpaths.add(xpaths.getKey());
                            treeElement.leastRefactoredXpaths.add(xpaths.getValue());
                        });
                    }
                }
            }

            List<String> patterns = new ArrayList<>();
            patterns.add("id");
            patterns.add("name");
            treeElement.uniqueXpathsWithAttributes =
                    treeElement.attachAttributesByNamePatternsToXpaths(treeElement.uniqueXpaths, patterns, GetAttributeMethod.ByNameOrByNameContainingPattern);
            treeElement.leastRefactoredXpathsWithAttributes =
                    treeElement.attachAttributesByNamePatternsToXpaths(treeElement.leastRefactoredXpaths, patterns, GetAttributeMethod.ByNameOrByNameContainingPattern);
        }
    }

    protected MapEntry<String,String> buildXpath(TreeElement subRootElement, TreeElement anchorElement, TreeElement element, boolean includeTagIndex)
    {
        if(subRootElement == null || anchorElement == null || element == null)
        {
            return null;
        }

        if(subRootElement.element == null || anchorElement.element == null || element.element == null)
        {
            return null;
        }

        String uniqueXpath = null;
        String leastRefactoredXpath = null;
        String xpathPartFromRootElementToFoundElement = buildXpathPartBetweenSubTreeRootAndLeafExcludingRoot(subRootElement, element, includeTagIndex);
        String xpathPartFromRootElementToAnchorElement = buildXpathPartBetweenSubTreeRootAndLeafExcludingRoot(subRootElement, anchorElement, includeTagIndex);
        String rootElementTagName = subRootElement.element.tagName();
        String anchorElementOwnText = anchorElement.element.ownText();

        if (xpathPartFromRootElementToFoundElement != null && xpathPartFromRootElementToAnchorElement != null)
        {
            if (xpathPartFromRootElementToAnchorElement == "" && xpathPartFromRootElementToFoundElement == "")
            {
                uniqueXpath = String.format("//%s[text()='%s']", rootElementTagName, anchorElementOwnText);
                leastRefactoredXpath = String.format("//%s[contains(text(),'%s')]", rootElementTagName, Util.removeLineSeparators(anchorElementOwnText).trim());
            }
            else if (xpathPartFromRootElementToAnchorElement == "")
            {
                uniqueXpath = String.format("//%s[text()='%s']/%s", rootElementTagName, anchorElementOwnText,
                                            xpathPartFromRootElementToFoundElement);
                leastRefactoredXpath = String.format("//%s[contains(text(),'%s')]/%s", rootElementTagName, Util.removeLineSeparators(anchorElementOwnText).trim(),
                                                     xpathPartFromRootElementToFoundElement);
            }
            else if (xpathPartFromRootElementToFoundElement == "")
            {
                uniqueXpath = String.format("//%s[%s[text()='%s']]",
                                            rootElementTagName, xpathPartFromRootElementToAnchorElement, anchorElementOwnText);
                leastRefactoredXpath = String.format("//%s[%s[contains(text(),'%s')]]",
                                                     rootElementTagName, xpathPartFromRootElementToAnchorElement, Util.removeLineSeparators(anchorElementOwnText).trim());
            }
            else
            {
                uniqueXpath =  String.format("//%s[%s[text()='%s']]/%s",
                                             rootElementTagName, xpathPartFromRootElementToAnchorElement, anchorElementOwnText,
                                             xpathPartFromRootElementToFoundElement);
                leastRefactoredXpath = String.format("//%s[%s[contains(text(),'%s')]]/%s",
                                                     rootElementTagName, xpathPartFromRootElementToAnchorElement, Util.removeLineSeparators(anchorElementOwnText).trim(),
                                                     xpathPartFromRootElementToFoundElement);
            }
        }

        return new MapEntry<>(uniqueXpath, leastRefactoredXpath);
    }

    protected String buildXpathPartBetweenSubTreeRootAndLeafExcludingRoot(TreeElement root, TreeElement leaf, boolean includeTagIndex)
    {
        List<TreeElement> allElements = getElementsBetweenSubTreeRootAndLeafInclusive(root, leaf);

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
            TreeElement treeElement = allElements.get(i);
            String tagName = treeElement.element.tagName();
            xpathBuilder.append(tagName);

            if(includeTagIndex && Util.hasItem(treeElement.position))
            {
                MapEntry<List<TreeElement>, List<TreeElement>> siblings = getSiblings(treeElement, true);
                List<TreeElement> youngerSiblings = siblings.getKey();
                List<TreeElement> olderSiblings = siblings.getValue();

                if(Util.hasItem(olderSiblings))
                {
                    xpathBuilder.append(String.format("[%s]", olderSiblings.size() + 1));
                }
                else if(Util.hasItem(youngerSiblings))
                {
                    xpathBuilder.append(String.format("[%s]", 1));
                }
            }

            if (i > 0)
            {
                xpathBuilder.append("/");
            }
        }

        return xpathBuilder.toString();
    }

    // not a good return pattern but for better performance (reducing 1 loop) and private then OK
    private MapEntry<List<TreeElement>, List<TreeElement>> getSiblings(TreeElement treeElement, boolean sameTagName)
    {
        List<TreeElement> youngerSiblings = new ArrayList<>();
        List<TreeElement> olderSiblings = new ArrayList<>();

        if(treeElement != null && Util.hasItem(treeElement.position))
        {
            Position treeElementPosition = treeElement.position;
            Position parentPosition = treeElementPosition.getParentPosition();

            if(Util.hasItem(parentPosition))
            {
                this.forEach(x -> {
                    boolean condition = Util.hasItem(x.position) &&
                                        x.position.size() == treeElementPosition.size() &&
                                        Collections.indexOfSubList(x.position , parentPosition) == 0 &&
                                        !x.position.equals(treeElement.position);

                    if(sameTagName)
                    {
                        condition = condition && x.element != null && treeElement.element != null && x.element.tagName().equalsIgnoreCase(treeElement.element.tagName());
                    }

                    boolean toGetOlderSiblings = condition && x.position.get(x.position.size()-1) < treeElement.position.get(treeElement.position.size()-1);
                    boolean toGetYoungerSiblings = condition && x.position.get(x.position.size()-1) > treeElement.position.get(treeElement.position.size()-1);

                    if(toGetOlderSiblings)
                    {
                        olderSiblings.add(x);
                    }

                    if(toGetYoungerSiblings)
                    {
                        youngerSiblings.add(x);
                    }
                });
            }
        }

        return new MapEntry<>(youngerSiblings, olderSiblings);
    }

    protected List<TreeElement> getElementsBetweenSubTreeRootAndLeafInclusive(TreeElement subTreeRoot, TreeElement leaf)
    {
        List<TreeElement> result = new ArrayList<>();

        if (leaf != null && subTreeRoot != null && Util.hasItem(leaf.position) && Util.hasItem(subTreeRoot.position))
        {
            result.add(leaf);
            Position leafPosition = leaf.position;
            Position subTreeRootPosition = subTreeRoot.position;
            Position currentPosition  = leafPosition;

            do
            {
                if(Util.hasNoItem(currentPosition) || currentPosition.equals(subTreeRootPosition))
                {
                    break;
                }
                else
                {
                    currentPosition = currentPosition.getParentPosition();
                    TreeElement parentTreeElement = getTreeElementByPosition(currentPosition);

                    if(parentTreeElement != null)
                    {
                        result.add(parentTreeElement);
                    }
                }
            }
            while(true);
        }

        return result;
    }

    protected TreeElement getFirstMatchedTreeElement(TreeElement element)
    {
        for (TreeElement item : this)
        {
            if (item.equals(element))
            {
                return item;
            }
        }

        return null;
    }

    protected void setDistancesAndLinksToAnchorsForAllTreeElements(List<TreeElement> anchors, boolean getAnchorsOfAnchor)
    {
        if(Util.hasItem(anchors))
        {
            this.forEach(x->x.setDistancesAndLinksToAnchors(anchors, getAnchorsOfAnchor));
        }
    }

    protected TreeElement getTreeElementByPosition(Position position)
    {
        if(Util.hasItem(position))
        {
            for (TreeElement item : this)
            {
                if(Util.hasItem(item.position) && item.position.equals(position))
                {
                    return item;
                }
            }
        }

        return null;
    }

    protected List<TreeElement> setAsAnchorCandidatesForAllTreeElements()
    {
        List<TreeElement> anchors = new ArrayList<>();

        this.forEach(x -> {
            List<TreeElement> elementsHavingSameOwnText = getTreeElementsHavingSameOwnText(x);
            x.setElementsWithSameOwnText(elementsHavingSameOwnText);
            x.setAsAnchorCandidate(elementsHavingSameOwnText);

            if(x.asAnchorCandidate)
            {
                anchors.add(x);
            }
        });

        return anchors;
    }

    protected List<TreeElement> getTreeElementsHavingSameOwnText(TreeElement treeElement)
    {
        List<TreeElement> result = new ArrayList<>();

        if(treeElement != null)
        {
            this.forEach(x -> {

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

    protected UUID buildTree(TreeElement rootElement)
    {
        if(rootElement != null && rootElement.element != null)
        {
            this.clear();
            rootElement.position.add(0);
            this.add(rootElement);
            Tree allChildren = getAllChildren(rootElement.element, rootElement.position);
            this.addAll(allChildren);
            return rootElement.id;
        }

        return null;
    }

    protected Tree getAllChildren(Element element, Position startingPosition)
    {
        Tree result = new Tree();

        if(element != null)
        {
            Elements children = element.children();

            if(Util.hasItem(children))
            {
                for(int i = 0; i < children.size(); i++)
                {
                    TreeElement treeElement = new TreeElement(children.get(i));
                    treeElement.position = new Position(startingPosition);
                    treeElement.position.add(i);
                    result.add(treeElement);
                    Tree nextResult = getAllChildren(treeElement.element, treeElement.position);
                    result.addAll(nextResult);
                }
            }
        }

        return result;
    }
}
