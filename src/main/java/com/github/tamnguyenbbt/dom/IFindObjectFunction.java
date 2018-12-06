package com.github.tamnguyenbbt.dom;

import com.github.tamnguyenbbt.exception.AmbiguousFoundXPathsException;

@FunctionalInterface
public interface IFindObjectFunction<T, R>
{
    R apply(T t) throws AmbiguousFoundXPathsException;
}
