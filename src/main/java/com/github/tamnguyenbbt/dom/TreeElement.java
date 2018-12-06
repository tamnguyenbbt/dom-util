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
}
