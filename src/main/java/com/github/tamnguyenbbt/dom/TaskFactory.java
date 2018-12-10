package com.github.tamnguyenbbt.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.time.StopWatch;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TaskFactory<T>
{
    private static final Logger logger = LoggerFactory.getLogger(TaskFactory.class);
    private final Collection<ITask> tasks;
    private final static long timeout = 20;
    private final static TimeUnit timeUnit = TimeUnit.SECONDS;

    public TaskFactory(Collection<ITask> tasks)
    {
        this.tasks = tasks;
    }

    public List<T> run()
    {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<T> result = new ArrayList<>();
        ExecutorService executorPool = Executors.newFixedThreadPool(ThreadConfig.getOptimisedNumberOfThreadsForIOOperations());

        try
        {
            final List<TaskExecutor> taskExecutors =
                tasks.stream().map(TaskExecutor::new).collect(Collectors.toList());
            final List<FutureTaskExecutor> futureReportExecutors = new ArrayList<>();

            for (TaskExecutor item : taskExecutors)
            {
                futureReportExecutors.add(new FutureTaskExecutor(executorPool.submit(item), item.getTask()));
            }

            for (FutureTaskExecutor executor : futureReportExecutors)
            {
                try
                {
                    T singleResult = (T)executor.getFuture().get(timeout, timeUnit);
                    result.add(singleResult);
                }
                catch (Exception e)
                {
                    String errorMessage = String.format("Failed to run task for %s - %s\n%s", executor, e, getThrowableCause(e));
                    logger.warn(errorMessage);
                }
            }
        }
        catch(Exception e)
        {
            logger.error("Failed to run tasks", e);
        }
        finally
        {
            executorPool.shutdown();
        }

        logger.info("Run tasks in {} ms", stopWatch.getTime());
        stopWatch.stop();

        return result;
    }

    private String getThrowableCause(Throwable e)
    {
        return (e.getCause() != null) ? getStackTraceFirstElement(e.getCause().getStackTrace()) :getStackTraceFirstElement(e.getStackTrace()) ;
    }

    private String getStackTraceFirstElement(StackTraceElement[] elements)
    {
        return elements.length == 0 ? "" : elements[0].toString();
    }
}
