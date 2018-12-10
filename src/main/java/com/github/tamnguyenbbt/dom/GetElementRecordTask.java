package com.github.tamnguyenbbt.dom;

import com.github.tamnguyenbbt.task.IFunction;
import com.github.tamnguyenbbt.task.ITask;

final class GetElementRecordTask<T> implements ITask
{
    private IFunction function;
    private T param;

    protected GetElementRecordTask(IFunction function, T param)
    {
        this.function = function;
        this.param = param;
    }

    @Override
    public IFunction getFunction()
    {
        return function;
    }

    @Override
    public T getFunctionParam()
    {
        return param;
    }
}
