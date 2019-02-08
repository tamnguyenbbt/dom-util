package com.github.tamnguyenbbt.dom;

import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

final class TreeElements extends ArrayList<TreeElement>
{
    protected  TreeElements()
    {
        super();
    }

    protected  TreeElements(TreeElements treeElements)
    {
        this();

        if(Util.hasItem(treeElements))
        {
            treeElements.forEach(x->this.add(x));
        }
    }

    protected Elements getElements()
    {
        Elements elements = new Elements();
        this.forEach(x->elements.add(x.element));
        return elements;
    }

    protected List<String> getActiveXpaths()
    {
        List<String> xpaths = new ArrayList<>();
        this.forEach(x->xpaths.add(x.activeXpath));
        return xpaths;
    }

    protected List<TreeElements> groupTreeElementsByAnchor()
    {
        List<TreeElements> result = new ArrayList<>();
        TreeElements copiedTreeElements = new TreeElements(this);

        while(Util.hasItem(copiedTreeElements))
        {
            TreeElement currentTreeElement = copiedTreeElements.get(0);
            TreeElement anchorOfTheCurrentTreeElement = currentTreeElement.activeContainingTree.getAnchor();
            TreeElements treeElements = new TreeElements();
            treeElements.add(currentTreeElement);
            copiedTreeElements.remove(currentTreeElement);

            if(Util.hasItem(copiedTreeElements))
            {
                copiedTreeElements.forEach(x->{
                    TreeElement anchorOfTreeElement =  x.activeContainingTree.getAnchor();

                    if(anchorOfTheCurrentTreeElement.equals(anchorOfTreeElement))
                    {
                        treeElements.add(x);
                        copiedTreeElements.remove(x);
                    }
                });
            }

            if(Util.hasItem(treeElements))
            {
                result.add(treeElements);
            }
        }

        return result;
    }

    protected TreeElements getTreeElementsNotSharingTreeAnchor()
    {
        TreeElements result = new TreeElements();
        int treeElementCount = this.size();

        for(int i = 0; i < treeElementCount; i++)
        {
            TreeElement currentTreeElement = this.get(i);
            TreeElement iAnchor = currentTreeElement.activeContainingTree.getAnchor();
            boolean notSharingAnchor = true;

            for(int j = 0; j < treeElementCount; j++)
            {
                TreeElement jAnchor =  this.get(j).activeContainingTree.getAnchor();

                if(i != j && iAnchor.equals(jAnchor))
                {
                    notSharingAnchor = false;
                    break;
                }
            }

            if(notSharingAnchor)
            {
                result.add(currentTreeElement);
            }
        }

        return result;
    }

    protected TreeElements getTreeElementsHavingShortestDistanceToOwnAnchors()
    {
        int shortestDistance = -1;
        TreeElements result = new TreeElements();

        for (TreeElement item : this)
        {
            int distance = item.activeDistanceToAnchorElement;

            if (shortestDistance == -1 || distance < shortestDistance)
            {
                shortestDistance = distance;
                result = new TreeElements();
                result.add(item);
            }
            else if (distance == shortestDistance)
            {
                result.add(item);
            }
        }

        return result;
    }
}