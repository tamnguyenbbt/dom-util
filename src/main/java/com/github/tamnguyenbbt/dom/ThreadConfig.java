package com.github.tamnguyenbbt.task;

public class ThreadConfig
{
    public final static Double ioBlockingCoefficient = 0.8;

    public static int getOptimisedNumberOfThreadsForIOOperations()
    {
        final int numberOfCores = Runtime.getRuntime().availableProcessors();
        return (int)(numberOfCores/(1-ioBlockingCoefficient));
    }
}
