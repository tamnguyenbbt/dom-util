package com.github.tamnguyenbbt.dom;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

final class FindFuncParam
{
    protected WebDriver driver;
    protected Element anchorElement;
    protected Elements searchElements;

    protected FindFuncParam(WebDriver driver, Element anchorElement, Elements searchElements)
    {
        this.driver = driver;
        this.anchorElement = anchorElement;
        this.searchElements = searchElements;
    }
}
