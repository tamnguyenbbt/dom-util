package com.github.tamnguyenbbt.dom;

import java.util.List;
import java.util.Map;

public class Util
{
    protected static <K,V> boolean hasItem(Map<K,V> map)
    {
        return (map != null && !map.isEmpty());
    }

    protected static <T> boolean hasItem(List<T> list)
    {
        return (list != null && !list.isEmpty());
    }

    protected static <T> boolean hasNoItem(List<T> list)
    {
        return (list == null || list.isEmpty());
    }
}
