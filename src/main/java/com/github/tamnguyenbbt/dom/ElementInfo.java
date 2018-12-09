package com.github.tamnguyenbbt.dom;

public class ElementInfo
{
    public String tagName;
    public String ownText;
    public int indexIfMultipleFound;
    public Condition condition;

    public ElementInfo()
    {
        indexIfMultipleFound = -1;
        condition = new Condition();
    }

    public ElementInfo(String tagName, String ownText, int indexIfMultipleFound)
    {
        this();
        this.tagName = tagName;
        this.ownText = ownText;
        this.indexIfMultipleFound = indexIfMultipleFound;
    }

    public ElementInfo(String tagName)
    {
        this();
        this.tagName = tagName;
    }

    public ElementInfo(String tagName, String ownText)
    {
        this();
        this.tagName = tagName;
        this.ownText = ownText;
    }
    
    public ElementInfo(String tagName, String ownText, boolean whereOwnTextContainingPattern)
    {
        this();
        this.tagName = tagName;
        this.ownText = ownText;
        this.condition.whereOwnTextContainingPattern = whereOwnTextContainingPattern;
    }
}
