package com.github.tamnguyenbbt.dom;

import java.util.ArrayList;
import java.util.List;

public class DomUtilConfig
{
    public int webDriverTimeoutInMilliseconds;
    public XpathBuildMethod xpathBuildMethod;
    public List<XpathBuildOption> xpathBuildOptions;

    public DomUtilConfig()
    {
        xpathBuildMethod = XpathBuildMethod.ContainText;
        xpathBuildOptions = new ArrayList<>();
        xpathBuildOptions.add(XpathBuildOption.AttachId);
        xpathBuildOptions.add(XpathBuildOption.AttachName);
        webDriverTimeoutInMilliseconds = 2000;
    }
}
