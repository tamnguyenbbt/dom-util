package com.github.tamnguyenbbt.task;

@FunctionalInterface
public interface IFunction<T, R>
{
    R apply(T t);
}
