package com.github.tamnguyenbbt.dom;

import org.jsoup.nodes.Element;

final class GetElementRecordParam
{
    protected Element anchorElement;
    protected Element searchElement;
    protected int searchElementIndex;

    public GetElementRecordParam(Element anchorElement, Element searchElement, int searchElementIndex)
    {
        this.anchorElement = anchorElement;
        this.searchElement = searchElement;
        this.searchElementIndex = searchElementIndex;
    }
}
