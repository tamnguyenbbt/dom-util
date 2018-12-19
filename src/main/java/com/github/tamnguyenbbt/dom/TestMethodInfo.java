package com.github.tamnguyenbbt.dom;

class TestMethodInfo
{
    protected String returnType;
    protected TestMethodType methodType;
    protected boolean hasParam;
    protected String seleniumFuncName;

    protected TestMethodInfo(TestMethodType methodType, boolean hasParam, String returnType, String seleniumFuncName)
    {
        this.methodType = methodType;
        this.hasParam = hasParam;
        this.returnType = returnType;
        this.seleniumFuncName = seleniumFuncName;
    }
}
