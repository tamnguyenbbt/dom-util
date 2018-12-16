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
    protected List<String> uniqueXpaths;
    protected List<String> uniqueXpathsWithAttributes;
    protected List<String> leastRefactoredXpaths;
    protected List<String> leastRefactoredXpathsWithAttributes;

    protected TreeElement()
    {
        id = UUID.randomUUID();
        position = new Position();
        elementsWithSameOwnText = new ArrayList<>();
        distancesToAnchors = new HashMap<>();
        linkedAnchors = new HashMap<>();
        rootPositionsForAnchors = new HashMap<>();
        uniqueXpaths = new ArrayList<>();
        uniqueXpathsWithAttributes = new ArrayList<>();
        leastRefactoredXpaths = new ArrayList<>();
        leastRefactoredXpathsWithAttributes = new ArrayList<>();
    }

    protected TreeElement(Element element)
    {
        this();
        this.element = element;
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

    protected Attribute getAttributeLinkedToAnchor(TreeElement anchor)
    {
        if(anchor == null)
        {
            return null;
        }

        Attribute anchorForAttribute = anchor.getAttributeByNameContainingPattern("for");

        if(anchorForAttribute != null)
        {
            String anchorForAttributeValue = anchorForAttribute.getValue();
            Attribute elementIdAttribute = getAttributeByName("id");

            if (elementIdAttribute != null &&
                    elementIdAttribute.getValue() != null &&
                    elementIdAttribute.getValue().trim().equalsIgnoreCase(anchorForAttributeValue))
            {
                return elementIdAttribute;
            }

            Attribute elementNameAttribute = getAttributeByName("name");

            if(elementNameAttribute != null &&
                    elementNameAttribute.getValue() != null &&
                    elementNameAttribute.getValue().trim().equals(anchorForAttributeValue))
            {
                return elementNameAttribute;
            }
        }

        return null;
    }

    protected Attribute getAttributeByNameContainingPattern(String pattern)
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

    protected Attribute getAttributeByName(String name)
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

    protected MapEntry<Position, Tree> getContainingTree(Element anchorElement)
    {
        Tree tree = new Tree();
        Element rootElement = anchorElement;
        TreeElement firstFound = null;

        while(firstFound == null && rootElement != null && element != null)
        {
            tree = new Tree(rootElement);
            firstFound = tree.getFirstMatchedTreeElement(this);

            if(firstFound == null)
            {
                rootElement = rootElement.parent();

                if(rootElement != null)
                {
                    rootElement.attr(tree.uniqueInsertedAttribute, UUID.randomUUID().toString());
                }
            }
        }

        return firstFound == null ? null : new MapEntry<>(firstFound.position, tree);
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
        Set<Integer> set = new HashSet<>();
        set.addAll(distances);
        distances.clear();
        distances.addAll(set);
        Collections.sort(distances);
        return new ArrayList<>(distances.subList(0, shortestDistanceDepth - 1));
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
}
