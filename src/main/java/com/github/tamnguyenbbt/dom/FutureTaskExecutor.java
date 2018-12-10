package com.github.tamnguyenbbt.task;
import java.util.concurrent.Future;

public class FutureTaskExecutor<T>
{
    private final Future<T> future;
    private final ITask task;

    FutureTaskExecutor(Future<T> future, ITask task)
    {
        this.future = future;
        this.task = task;
    }

    Future<T> getFuture()
    {
        return future;
    }

    @Override
    public String toString()
    {
        return task.toString();
    }
}
