package com.github.tamnguyenbbt.dom;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tree extends ArrayList<TreeElement>
{
    protected final static String uniqueInsertedAttribute = "wusiwug";
    private Element rootElement;

    protected Tree()
    {
        super();
    }

    protected Tree(Element rootElement)
    {
        this();
        this.rootElement = rootElement;
        buildTree();
    }

    protected Tree(Document document)
    {
        this();

        if(document != null)
        {
            Elements documentElements = document.select("html");

            if(Util.hasItem(documentElements))
            {
                rootElement = documentElements.get(0);
                buildTree();
            }
        }
    }

    private String buildXpathPartBetweenSubTreeRootAndLeafExcludingRoot(TreeElement root, TreeElement leaf, boolean includeTagIndex)
    {
        List<TreeElement> allElements = getElementsBetweenSubTreeRootAndLeafInclusive(leaf, root);

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
                int tagIndex = treeElement.position.get(treeElement.position.size() - 1);
                xpathBuilder.append(String.format("[%s]", tagIndex));
            }

            if (i > 0)
            {
                xpathBuilder.append("/");
            }
        }

        return xpathBuilder.toString();
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

    protected TreeElement getRootElement()
    {
        Position rootPosition = new Position();
        rootPosition.add(0);

        for (TreeElement item : this)
        {
            if(item.position.equals(rootPosition))
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
            List<TreeElement> elementsHavingSameownText = getTreeElementsHavingSameOwnText(x);
            x.setElementsWithSameOwnText(elementsHavingSameownText);
            x.setAsAnchorCandidate(elementsHavingSameownText);

            if(x.asAnchorCandidate)
            {
                anchors.add(x);
            }
        });

        return anchors;
    }

    private List<TreeElement> getTreeElementsHavingSameOwnText(TreeElement treeElement)
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

    private void buildTree()
    {
        if(rootElement != null)
        {
            TreeElement rootTreeElement = new TreeElement();
            rootTreeElement.position.add(0);
            rootTreeElement.element = rootElement;
            this.add(rootTreeElement);
            Tree allChildren = getAllChildren(rootElement, rootTreeElement.position);
            this.addAll(allChildren);
        }
    }

    private Tree getAllChildren(Element element, Position startingPosition)
    {
        Tree result = new Tree();

        if(element != null)
        {
            Elements children = element.children();

            if(Util.hasItem(children))
            {
                for(int i = 0; i < children.size(); i++)
                {
                    TreeElement treeElement = new TreeElement();
                    treeElement.position = new Position(startingPosition);
                    treeElement.position.add(i);
                    treeElement.element = children.get(i);
                    result.add(treeElement);
                    Tree nextResult = getAllChildren(treeElement.element, treeElement.position);
                    result.addAll(nextResult);
                }
            }
        }

        return result;
    }
}
