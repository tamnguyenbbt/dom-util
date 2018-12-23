package com.github.tamnguyenbbt.dom;

public class AssociationRule
{
    public HtmlTag tag;
    public TestMethodType methodType;
    public TestMethodInfo testMethodInfo;

    public AssociationRule(HtmlTag tag, TestMethodType methodType, TestMethodInfo testMethodInfo)
    {
        this.tag = tag;
        this.methodType = methodType;
        this.testMethodInfo = testMethodInfo;
    }

    public boolean isValid()
    {
       // return tag != null && methodType != null && testMethodInfo != null && testMethodInfo.body != null;
        return false;
    }
}
