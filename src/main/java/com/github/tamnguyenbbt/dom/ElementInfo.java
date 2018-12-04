package com.github.tamnguyenbbt.dom;

public class ElementInfo
{
    public String tagName;
    public String ownText;
    public int indexIfMultipleFound;
    public boolean whereIgnoreCaseForOwnText;
    public boolean whereOwnTextContainingPattern;
    public boolean whereIncludingTabsAndSpacesForOwnText;

    public ElementInfo()
    {
        indexIfMultipleFound = -1;
    }

    public ElementInfo(String tagName, String ownText, int indexIfMultipleFound)
    {
        this();
        this.tagName = tagName;
        this.ownText = ownText;
        this.indexIfMultipleFound = indexIfMultipleFound;
    }

    public ElementInfo(String tagName, String ownText)
    {
        this();
        this.tagName = tagName;
        this.ownText = ownText;
    }
}
