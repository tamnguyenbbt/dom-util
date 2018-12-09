package com.github.tamnguyenbbt.dom;

public class Condition
{
    public boolean whereIgnoreCaseForOwnText;
    public boolean whereOwnTextContainingPattern;
    public boolean whereIncludingTabsAndSpacesForOwnText;

    public Condition()
    {
    }

    public Condition(boolean whereIgnoreCaseForOwnText, boolean whereOwnTextContainingPattern, boolean whereIncludingTabsAndSpacesForOwnText)
    {
        this.whereIgnoreCaseForOwnText = whereIgnoreCaseForOwnText;
        this.whereOwnTextContainingPattern = whereOwnTextContainingPattern;
        this.whereIncludingTabsAndSpacesForOwnText = whereIncludingTabsAndSpacesForOwnText;
    }
}
