package com.github.tamnguyenbbt.dom;

import java.util.ArrayList;
import java.util.List;

public class AssociationRule
{
    public List<HtmlTag> tags;
    List<String> typeAttributeValues;
    public TestMethodType methodType;
    public TestMethodInfo testMethodInfo;

    public AssociationRule(List<HtmlTag> tags, List<String> typeAttributeValues, TestMethodType methodType, TestMethodInfo testMethodInfo)
    {
        this.typeAttributeValues = typeAttributeValues == null ? new ArrayList<>() : typeAttributeValues;
        this.tags = tags == null ? new ArrayList<>() : tags;
        this.methodType = methodType;
        this.testMethodInfo = testMethodInfo;
    }

    public AssociationRule(HtmlTag tag, String typeAttributeValue, TestMethodType methodType, TestMethodInfo testMethodInfo)
    {
        tags = new ArrayList<>();
        typeAttributeValues = new ArrayList<>();

        if(tag != null)
        {
            tags.add(tag);
        }

        if(typeAttributeValue != null)
        {
            typeAttributeValues.add(typeAttributeValue);
        }

        this.methodType = methodType;
        this.testMethodInfo = testMethodInfo;
    }

    public AssociationRule(HtmlTag tag, TestMethodType methodType, TestMethodInfo testMethodInfo)
    {
        this(tag, null, methodType, testMethodInfo);
    }

    public AssociationRule(List<HtmlTag> tags, TestMethodType methodType, TestMethodInfo testMethodInfo)
    {
        this(tags, null, methodType, testMethodInfo);
    }

    public boolean isValid()
    {
        return Util.hasItem(tags) && methodType != null && testMethodInfo != null && testMethodInfo.bodyWithInjectableXpathAndParam != null;
    }
}