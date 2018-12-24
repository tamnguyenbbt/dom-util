package com.github.tamnguyenbbt.dom;

public class TestMethodInfo
{
    public String returnType;
    public boolean hasParam;
    public String bodyWithInjectableXpathAndParam;

    public TestMethodInfo(String returnType, boolean hasParam, String bodyWithInjectableXpathAndParam)
    {
        this.returnType = returnType;
        this.hasParam = hasParam;
        this.bodyWithInjectableXpathAndParam = bodyWithInjectableXpathAndParam;
    }
}
