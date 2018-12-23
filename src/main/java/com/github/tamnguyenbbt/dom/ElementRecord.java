package com.github.tamnguyenbbt.dom;

import org.jsoup.nodes.Element;

final class ElementRecord
{
    protected int index;
    protected Tree containingTree;
    protected int distanceToAnchorElement;

    protected ElementRecord(Element anchorElement, Element searchElement)
    {
        if(anchorElement != null || searchElement != null)
        {
            Tree containingTree = new Tree(searchElement, anchorElement);
            TreeElement leaf = containingTree.getLeaf();

            if(Util.hasItem(containingTree) && leaf != null)
            {
                TreeElement anchor = containingTree.getAnchor();

                if(Util.hasItem(leaf.position) && anchor != null && Util.hasItem(anchor.position))
                {
                    this.containingTree = containingTree;
                    distanceToAnchorElement = leaf.position.size() + anchor.position.size() - 2;
                }
            }
        }
    }

    protected TreeElement getLeaf()
    {
        return containingTree.getLeaf();
    }

    protected TreeElement getRoot()
    {
        return containingTree.getRoot();
    }

    protected TreeElement getAnchor()
    {
        return containingTree.getAnchor();
    }

    protected String buildXpath(DomUtilConfig config)
    {
        if(config == null)
        {
            config = new DomUtilConfig();
        }

        TreeElement leaf = getLeaf();
        TreeElement root = getRoot();
        TreeElement anchor = getAnchor();

        if(leaf == null || root == null || anchor == null)
        {
            return null;
        }

        boolean includeTagIndex = Util.hasItem(config.xpathBuildOptions) && config.xpathBuildOptions.contains(XpathBuildOption.IncludeTagIndex);
        MapEntry<String,String> xpaths = containingTree.buildXpath(root, anchor, leaf, includeTagIndex);
        String xpath = config.xpathBuildMethod == XpathBuildMethod.EqualText ? xpaths.getKey() : xpaths.getValue();
        TreeElement treeElement = containingTree.getLeaf();

        if(Util.hasItem(config.xpathBuildOptions) && config.xpathBuildOptions.contains(XpathBuildOption.AttachId))
        {
            xpath = treeElement.attachAttributesByNamePatternToXpath(xpath, "id", GetAttributeMethod.ByNameOrByNameContainingPattern);
        }

        if(Util.hasItem(config.xpathBuildOptions) && config.xpathBuildOptions.contains(XpathBuildOption.AttachName))
        {
            xpath = treeElement.attachAttributesByNamePatternToXpath(xpath, "name", GetAttributeMethod.ByNameOrByNameContainingPattern);
        }

        return xpath;
    }
}
