package dnn.tam.util.dom;

import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.List;

public class TreeElement
{
    protected Element element;
    protected List<Integer> position;

    public TreeElement()
    {
        position = new ArrayList<>();
    }
}
