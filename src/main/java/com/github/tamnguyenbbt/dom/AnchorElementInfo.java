package com.github.tamnguyenbbt.dom;

public class AnchorElementInfo
{
    public String tagName;
    public String ownText;
    public int indexIfMultipleFound;
    public boolean whereIgnoreCaseForOwnText;
    public boolean whereOwnTextContainingPattern;
    public boolean whereIncludingTabsAndSpacesForOwnText;

    public AnchorElementInfo()
    {
        indexIfMultipleFound = -1;
        whereIgnoreCaseForOwnText = false;
        whereOwnTextContainingPattern = false;
        whereIncludingTabsAndSpacesForOwnText = false;
    }
}
