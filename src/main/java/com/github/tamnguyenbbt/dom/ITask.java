package com.github.tamnguyenbbt.task;

public interface ITask
{
    IFunction getFunction();
    <T> T getFunctionParam();
}
