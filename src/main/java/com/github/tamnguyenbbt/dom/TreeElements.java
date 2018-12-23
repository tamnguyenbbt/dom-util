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

    protected  TreeElements(Elements elements)
    {
        this();

        if(Util.hasItem(elements))
        {
            elements.forEach(x->this.add(new TreeElement(x)));
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

    protected TreeElements getTreeElementsNotSharingTreeAnchor()
    {
        TreeElements result = new TreeElements();
        int treeElementCount = this.size();

        if(treeElementCount == 1)
        {
            result.add(this.get(0));
        }
        else
        {
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
