package com.github.tamnguyenbbt.dom;

import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.List;

public final class TreeElement
{
    protected Element element;
    protected Position position;
    protected String ownText;
    protected boolean isAnchorCandidate;
    protected List<TreeElement> elementsWithSameOwnText;

    protected TreeElement()
    {
        position = new Position();
        elementsWithSameOwnText = new ArrayList<>();
    }

    protected TreeElement(Element element)
    {
        this();
        this.element = element;
    }
}
