package com.github.tamnguyenbbt.dom;

import com.github.tamnguyenbbt.task.IFunction;
import com.github.tamnguyenbbt.task.ITask;
import com.github.tamnguyenbbt.task.TaskFactory;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

final class ElementRecords extends ArrayList<ElementRecord>
{
    protected Logger logger =  Logger.getLogger(this.getClass().getName());
    protected IFunction<GetElementRecordParam, ElementRecord> getElementRecordFunc = this::getElementRecord;

    protected ElementRecords()
    {
        super();
    }

    protected ElementRecords(Element anchorElement, Elements searchElements)
    {
        this();
        String displayedAnchor = anchorElement == null ? "" : Util.cutText(anchorElement.toString(), 100, true);
        logger.info(String.format("creating element records for ANCHOR: '%s'", displayedAnchor));
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        if(anchorElement != null && Util.hasItem(searchElements))
        {
            List<ITask> getElementRecordTasks = new ArrayList<>();

            for (int i = 0 ; i < searchElements.size(); i++)
            {
                Element currentSearchElement = searchElements.get(i);
                GetElementRecordTask task = new GetElementRecordTask(getElementRecordFunc, new GetElementRecordParam(anchorElement, currentSearchElement, i));
                getElementRecordTasks.add(task);
            }

            this.addAll(new TaskFactory(getElementRecordTasks).run());
        }

        stopWatch.stop();
        logger.info(String.format("creating element records for ANCHOR: '%s' - time in ms: %s", displayedAnchor, stopWatch.getTime()));
    }

    protected Elements getElements()
    {
        Elements elements = new Elements();
        this.forEach(x -> elements.add(x.containingTree.getLeaf().element));
        return elements;
    }

    protected ElementRecords getClosestElementRecords()
    {
        int shortestDistance = -1;
        ElementRecords result = new ElementRecords();

        for (ElementRecord item : this)
        {
            int distance = item.distanceToAnchorElement;

            if (shortestDistance == -1 || distance < shortestDistance)
            {
                shortestDistance = distance;
                result = new ElementRecords();
                result.add(item);
            }
            else if (distance == shortestDistance)
            {
                result.add(item);
            }
        }

        return result;
    }

    private ElementRecord getElementRecord(GetElementRecordParam getElementRecordParam)
    {
        Element anchorElement = getElementRecordParam == null ? null : getElementRecordParam.anchorElement;
        Element searchElement = getElementRecordParam == null ? null : getElementRecordParam.searchElement;
        String displayedAnchor = anchorElement == null ? "" : Util.cutText(anchorElement.toString(), 100, true);
        String displayedSearchElement = searchElement == null ? "" : Util.cutText(searchElement.toString(), 100, true);
        logger.info(String.format("  creating element record for ANCHOR: '%s' and SEARCH ELEMENT '%s'",displayedAnchor, displayedSearchElement));

        if(anchorElement == null || searchElement == null)
        {
            return null;
        }

        ElementRecord elementRecord = new ElementRecord(anchorElement, searchElement);
        elementRecord.index = getElementRecordParam.searchElementIndex;
        return elementRecord;
    }
}
