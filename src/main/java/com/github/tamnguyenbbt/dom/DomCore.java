package com.github.tamnguyenbbt.dom;

import org.apache.commons.lang3.NotImplementedException;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.*;

class DomCore
{
    protected DomUtilConfig config;

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
            config.xpathBuildOptions.add(XpathBuildOption.IncludeTagIndex);
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

    public Tree getDocumentTree(Document document)
    {
        if(document != null)
        {
            return new Tree(document);
        }

        return null;
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

    protected ElementRecords getElementRecords(Element anchorElement, Elements searchElements, SearchMethod searchMethod)
    {
        ElementRecords result;

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
        ElementRecords closestElementRecords = getClosestElementRecords(anchorElement, searchElements);
        return closestElementRecords.getElements();
    }

    private ElementRecords getElementRecordsByLinkAndShortestDistance(Element anchorElement, Elements searchElements)
    {
        ElementRecords linkedElementRecords = getLinkedElementRecords(anchorElement, searchElements);
        ElementRecords result;

        if(Util.hasItem(linkedElementRecords))
        {
            result = linkedElementRecords.size() == 1
                ? linkedElementRecords
                : getClosestElementRecords(anchorElement, linkedElementRecords.getElements());
        }
        else
        {
            result = getClosestElementRecords(anchorElement, searchElements);
        }

        return result;
    }

    private Elements getLinkedElements(Element anchorElement, Elements searchElements)
    {
        Elements result = new Elements();

        if (Util.hasItem(searchElements) && anchorElement != null)
        {
            searchElements.forEach(x -> {
                Attribute attributeLinkedToAnchor = new TreeElement(x).getAttributeLinkedToAnchor(new TreeElement(anchorElement));

                if (attributeLinkedToAnchor != null)
                {
                    result.add(x);
                }
            });
        }

        return result;
    }

    private ElementRecords getLinkedElementRecords(Element anchorElement, Elements searchElements)
    {
        ElementRecords result = new ElementRecords();
        ElementRecords searchElementRecords = new ElementRecords(anchorElement, searchElements);

        if (Util.hasItem(searchElementRecords))
        {
            searchElementRecords.forEach(x -> {
                Attribute attributeLinkedToAnchor = new TreeElement(x.containingTree.getLeaf().element).getAttributeLinkedToAnchor(new TreeElement(anchorElement));

                if (attributeLinkedToAnchor != null)
                {
                    result.add(x);
                }
            });
        }

        return result;
    }

    private ElementRecords getClosestElementRecords(Element anchorElement, Elements searchElements)
    {
        ElementRecords searchElementRecords = new ElementRecords(anchorElement, searchElements);
        return searchElementRecords.getClosestElementRecords();
    }
}