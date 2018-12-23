package com.github.tamnguyenbbt.dom;

public class TestMethodInfo
{
    public boolean hasReturn;
    public boolean hasParam;
    public String bodyWithInjectableXpathAndParam;

    protected TestMethodInfo(boolean hasReturn, boolean hasParam, String bodyWithInjectableXpathAndParam)
    {
        this.hasReturn = hasReturn;
        this.hasParam = hasParam;
        this.bodyWithInjectableXpathAndParam = bodyWithInjectableXpathAndParam;
    }
}
