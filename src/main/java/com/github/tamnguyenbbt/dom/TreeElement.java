package com.github.tamnguyenbbt.dom;

import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.List;

final class TreeElement
{
    protected Element element;
    protected List<Integer> position;

    protected TreeElement()
    {
        position = new ArrayList<>();
    }

    protected TreeElement(Element element)
    {
        this();
        this.element = element;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        if(!(obj instanceof TreeElement))
        {
            return false;
        }

        final TreeElement other = (TreeElement) obj;

        if(super.equals(other))
        {
            return true;
        }

        return false;
    }
}
