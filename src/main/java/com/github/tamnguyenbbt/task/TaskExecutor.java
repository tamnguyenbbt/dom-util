package com.github.tamnguyenbbt.task;

import java.util.concurrent.Callable;

public class TaskExecutor<T> implements Callable<T>
{
    private final ITask task;

    public TaskExecutor(ITask task)
    {
        this.task = task;
    }

    public ITask getTask()
    {
        return task;
    }

    @Override
    public T call() throws Exception
    {
        T functionParam = task.getFunctionParam();
        return (T)task.getFunction().apply(functionParam);
    }
}
