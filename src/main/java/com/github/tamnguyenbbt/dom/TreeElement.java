package com.github.tamnguyenbbt.dom;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import java.util.*;

final class TreeElement
{
    protected UUID id;
    protected Element element;
    protected Position position;
    protected boolean asAnchorCandidate;
    protected List<TreeElement> elementsWithSameOwnText;
    protected Map<TreeElement,Integer> distancesToAnchors;
    protected Map<TreeElement,Position> rootPositionsForAnchors;
    protected Map<TreeElement,Attribute> linkedAnchors;
    protected Tree activeContainingTree;
    protected int activeDistanceToAnchorElement;
    protected String activeXpath;
    protected List<TreeElement> anchorElementsFormingXpaths;
    protected List<String> uniqueXpaths;
    protected List<String> uniqueXpathsWithAttributes;
    protected List<String> leastRefactoredXpaths;
    protected List<String> leastRefactoredXpathsWithAttributes;

    protected TreeElement(Element element)
    {
        id = UUID.randomUUID();
        position = new Position();
        elementsWithSameOwnText = new ArrayList<>();
        distancesToAnchors = new HashMap<>();
        linkedAnchors = new HashMap<>();
        rootPositionsForAnchors = new HashMap<>();
        anchorElementsFormingXpaths = new ArrayList<>();
        uniqueXpaths = new ArrayList<>();
        uniqueXpathsWithAttributes = new ArrayList<>();
        leastRefactoredXpaths = new ArrayList<>();
        leastRefactoredXpathsWithAttributes = new ArrayList<>();
        this.element = element;

        if(element != null)
        {
            this.element.attr(Tree.uniqueInsertedAttribute, id.toString());
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null || (obj != null && !(obj instanceof TreeElement)))
        {
            return false;
        }

        if(this.element == null || ((TreeElement) obj).element == null)
        {
            return false;
        }

        return this.element.hasAttr(Tree.uniqueInsertedAttribute)
               && ((TreeElement) obj).element.hasAttr(Tree.uniqueInsertedAttribute)
               && this.element.attr(Tree.uniqueInsertedAttribute).equals(((TreeElement) obj).element.attr(Tree.uniqueInsertedAttribute));
    }

    protected void setElementsWithSameOwnText(List<TreeElement> elementsWithSameOwnText)
    {
        if(Util.hasItem(elementsWithSameOwnText) && elementsWithSameOwnText.size() > 1)
        {
            this.elementsWithSameOwnText = elementsWithSameOwnText;
        }
    }

    protected void setAsAnchorCandidate(List<TreeElement> elementsWithSameOwnText)
    {
        if(Util.hasItem(elementsWithSameOwnText) &&
           elementsWithSameOwnText.size() == 1 &&
           element != null &&
           element.ownText() != null &&
           !element.ownText().trim().equals(""))
        {
            asAnchorCandidate = true;
        }
    }

    protected void setDistancesAndLinksToAnchors(List<TreeElement> anchors, boolean getAnchorsOfAnchor)
    {
        if(Util.hasItem(anchors))
        {
            anchors.forEach(x -> {

                if(!asAnchorCandidate ||(asAnchorCandidate && getAnchorsOfAnchor))
                {
                    distancesToAnchors.put(x, getDistanceToAnchor(x));
                    rootPositionsForAnchors.put(x, getRootElementPosition(x));
                    Attribute linkedAttribute = getAttributeLinkedToAnchor(x);

                    if(linkedAttribute != null)
                    {
                        linkedAnchors.put(x, linkedAttribute);
                    }
                }
            });
        }
    }

    protected List<String> attachAttributesByNamePatternsToXpaths(List<String> xpaths, List<String> attributeNamePatterns, GetAttributeMethod getAttributeMethod)
    {
        List<String> result = new ArrayList<>();

        if(element != null && Util.hasItem(xpaths) && Util.hasItem(attributeNamePatterns))
        {
            for (String xpath : xpaths)
            {
                if(xpath != null)
                {
                    String newXpath = attachAttributesByNamePatternsToXpath(xpath, attributeNamePatterns, getAttributeMethod);
                    result.add(newXpath);
                }
            }
        }

        return result;
    }

    protected String attachAttributesByNamePatternsToXpath(String xpath, List<String> attributeNamePatterns, GetAttributeMethod getAttributeMethod)
    {
        if(element != null && xpath != null && Util.hasItem(attributeNamePatterns))
        {
            String newXpath= xpath;

            for (String pattern : attributeNamePatterns)
            {
                newXpath = attachAttributesByNamePatternToXpath(newXpath, pattern, getAttributeMethod);
            }

            return  newXpath;
        }

        return null;
    }

    protected String attachAttributesByNamePatternToXpath(String xpath, String attributeNamePattern, GetAttributeMethod getAttributeMethod)
    {
        String xpathWithAttributes = xpath;

        if(xpath != null && element != null)
        {
            List<Attribute> attributes = getAttributes(attributeNamePattern, getAttributeMethod);

            if(Util.hasItem(attributes))
            {
                for (Attribute item : attributes)
                {
                    xpathWithAttributes = String.format("%s[@%s='%s']", xpathWithAttributes, item.getKey(), item.getValue());
                }
            }
        }

        return xpathWithAttributes;
    }

    protected Attribute getAttributeLinkedToAnchor(TreeElement anchor)
    {
        if(anchor == null)
        {
            return null;
        }

        List<Attribute> anchorForLikeAttributes = anchor.getAttributes("for", GetAttributeMethod.ByNameOrByNameContainingPattern);

        if(Util.hasItem(anchorForLikeAttributes))
        {
            for (Attribute item : anchorForLikeAttributes)
            {
                Attribute linkedIdAttribute = getAttributeLinkedToAnchorAttribute(item, "id");

                if(linkedIdAttribute != null)
                {
                    return linkedIdAttribute;
                }

                Attribute linkedNameAttribute = getAttributeLinkedToAnchorAttribute(item, "name");

                if(linkedNameAttribute != null)
                {
                    return linkedNameAttribute;
                }
            }
        }

        return null;
    }

    protected boolean matchAny(List<Attribute> attributes)
    {
        if(Util.hasNoItem(attributes))
        {
            return false;
        }

        for (Attribute item : attributes)
        {
            if(item != null)
            {
                List<Attribute> elementAttributes = getAttributes(item.getKey(), GetAttributeMethod.ByName);

                if(Util.hasItem(elementAttributes) && elementAttributes.get(0).getValue().equalsIgnoreCase(item.getValue()))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private Attribute getAttributeLinkedToAnchorAttribute(Attribute anchorAttribute, String attributeNamePattern)
    {
        if(anchorAttribute != null)
        {
            List<Attribute> attributes = getAttributes(attributeNamePattern, GetAttributeMethod.ByNameOrByNameContainingPattern);

            if(Util.hasItem(attributes))
            {
                for (Attribute item : attributes)
                {
                    if(item.getValue() != null && item.getValue().trim().equalsIgnoreCase(anchorAttribute.getValue()))
                    {
                        return item;
                    }
                }
            }
        }

        return null;
    }

    private List<Attribute> getAttributes(String pattern, GetAttributeMethod getAttributeMethod)
    {
        List<Attribute> result = new ArrayList<>();

        if(element != null)
        {
            List<Attribute> attributes = element.attributes().asList();

            if(Util.hasItem(attributes))
            {
                attributes.forEach(x -> {
                    switch(getAttributeMethod)
                    {
                        case ByName:
                            if(x.getKey().equalsIgnoreCase(pattern))
                            {
                                result.add(x);
                            }
                            break;
                        case ByNameContainingPattern:
                            if(x.getKey().toLowerCase().contains(pattern.toLowerCase()))
                            {
                                result.add(x);
                            }
                            break;
                        default:
                            if(x.getKey().equalsIgnoreCase(pattern) || x.getKey().toLowerCase().contains(pattern.toLowerCase()))
                            {
                                result.add(x);
                            }
                            break;
                    }
                });
            }
        }

        return result;
    }

    protected List<TreeElement> getAnchorsByShortestDistanceDepth(int shortestDistanceDepth)
    {
        List<Integer> shortestDistances  = getNonDuplicatedShortestDistancesToAnchors(shortestDistanceDepth);
        List<TreeElement> result = new ArrayList<>();

        if(Util.hasItem(this.distancesToAnchors) && Util.hasItem(shortestDistances))
        {
            Set<Map.Entry<TreeElement, Integer>> distancesToAnchors = this.distancesToAnchors.entrySet();

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

    protected boolean matchDistance(int distance, List<Integer> patterns)
    {
        if(Util.hasItem(patterns))
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

    protected List<Integer> getNonDuplicatedShortestDistancesToAnchors(int shortestDistanceDepth)
    {
        List<Integer> distances = getDistancesToAnchors();

        if(Util.hasItem(distances))
        {
            int size = distances.size();
            Set<Integer> set = new HashSet<>();
            set.addAll(distances);
            distances.clear();
            distances.addAll(set);
            Collections.sort(distances);
            return new ArrayList<>(distances.subList(0, shortestDistanceDepth > size ? size : shortestDistanceDepth));
        }

        return distances;
    }

    protected List<Integer> getDistancesToAnchors()
    {
        List<Integer> distances = new ArrayList<>();

        if(Util.hasItem(this.distancesToAnchors))
        {
            Set<Map.Entry<TreeElement, Integer>> distancesToAnchors = this.distancesToAnchors.entrySet();

            for (Map.Entry<TreeElement, Integer> item : distancesToAnchors)
            {
                distances.add(item.getValue());
            }
        }

        return distances;
    }

    protected Position getRootPositionForLinkedAnchor(TreeElement linkedAnchor)
    {
        if(linkedAnchor != null && Util.hasItem(this.rootPositionsForAnchors))
        {
            Set<Map.Entry<TreeElement, Position>> rootPositionsForAnchors = this.rootPositionsForAnchors.entrySet();

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

    protected int getDistanceToAnchor(TreeElement anchor)
    {
        Position rootPosition = getRootElementPosition(anchor);

        return anchor != null && Util.hasItem(anchor.position) && Util.hasItem(position) && Util.hasItem(rootPosition)
                ? anchor.position.size() + position.size() - 2 * rootPosition.size()
                : 0;
    }

    protected Position getRootElementPosition(TreeElement anchor)
    {
        Position rootPosition = new Position();

        if(anchor != null && Util.hasItem(anchor.position) && Util.hasItem(position))
        {
            Position anchorPosition = anchor.position;

            for(int i = 0; i < anchorPosition.size(); i++)
            {
                if(i < position.size() && anchorPosition.get(i).equals(position.get(i)))
                {
                    rootPosition.add(anchorPosition.get(i));
                }
                else if(i < position.size() && !anchorPosition.get(i).equals(position.get(i)))
                {
                    break;
                }
            }
        }

        return rootPosition;
    }

    protected boolean matchElementOwnText(String pattern, Condition condition)
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

    protected boolean isValid()
    {
        return element != null && Util.hasItem(position) && Util.hasItem(uniqueXpaths) && (uniqueXpaths.size() > 0);
    }
}
