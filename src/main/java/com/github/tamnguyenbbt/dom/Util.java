package com.github.tamnguyenbbt.dom;

import org.apache.commons.lang.StringUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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

    protected static String removeSpecialCharacters(String input)
    {
        String output = input;
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9 ]");
        Matcher match= pattern.matcher(input);

        while(match.find())
        {
            String special = match.group();
            special = String.format("\\%s",special);
            output = output.replaceAll(special, "");
        }

        return output;
    }

    protected static String toTitleCase(String input)
    {
        if (StringUtils.isBlank(input))
        {
            return "";
        }

        if (StringUtils.length(input) == 1)
        {
            return input.toUpperCase();
        }

        StringBuffer resultBuffer = new StringBuffer(input.length());

        Stream.of(input.split(" ")).forEach(x ->
        {
            if (x.length() > 1)
            {
                Character firstCharacter = Character.toUpperCase(x.charAt(0));
                String remainingCharacters = x.substring(1).toLowerCase();
                resultBuffer.append(firstCharacter).append(remainingCharacters);
            }
            else
            {
                resultBuffer.append(x.toUpperCase());
            }

            resultBuffer.append(" ");

        });

        return StringUtils.trim(resultBuffer.toString());
    }

    protected static void saveToFile(String content, String path) throws IOException
    {
        File file = new File(path);
        saveToFile(content, file);
    }

    protected static void saveToFile(String content, File file) throws IOException
    {
        try(FileOutputStream fileOutputStream = new FileOutputStream(file, false))
        {
            byte[] bytes = content.getBytes();
            FileChannel fileChannel = fileOutputStream.getChannel();

            if(fileChannel != null)
            {
                FileLock fileLock = getFileLock(fileChannel, 3);
                fileOutputStream.write(bytes);

                if(fileLock != null)
                {
                    fileLock.release();
                }

                fileChannel.close();
            }
        }
    }

    private static FileLock getFileLock(FileChannel fileChannel, int retries) throws IOException
    {
        FileLock fileLock = null;
        int retryCount = 0;

        do {
            try
            {
                fileLock = fileChannel.tryLock();
                break;
            }
            catch(final OverlappingFileLockException e)
            {
                try
                {
                    TimeUnit.SECONDS.sleep(3);
                }
                catch(Exception ex){}
            }

            retryCount++;
        }
        while (retryCount<retries);

        return fileLock;
    }
}
