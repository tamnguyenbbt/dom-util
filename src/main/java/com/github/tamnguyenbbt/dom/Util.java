package com.github.tamnguyenbbt.dom;

import java.io.File;
import java.util.List;
import java.util.Map;

class Util
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

    protected static String removeLineSeparators(String input)
    {
        String separator = File.separator;
        return input == null ? null : input.replace(separator, "").replace("\n", "").replace("\r", "");
    }

    protected static String cutText(String input, int toLength, boolean removeLineSeparators)
    {
        String output =  input == null ? null : input.substring(0, Math.min(input.length(), toLength));
        output = input.length() > toLength ? output + "..." : output;
        return removeLineSeparators ? removeLineSeparators(output) : output;
    }
}
