import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import java.util.*;

public final class TreeElement
{
    protected UUID id;
    protected Element element;
    protected Position position;
    protected boolean asAnchorCandidate;
    protected List<TreeElement> elementsWithSameOwnText;
    protected Map<TreeElement,Integer> distancesToAnchors;
    protected Map<TreeElement,Position> rootPositionsForAnchors;
    protected Map<TreeElement,Attribute> linkedAnchors;
    protected List<String> uniqueXpaths;
    protected List<String> uniqueXpathsWithAttributes;
    protected List<String> leastRefactoredXpaths;
    protected List<String> leastRefactoredXpathsWithAttributes;

    protected TreeElement()
    {
        id = UUID.randomUUID();
        position = new Position();
        elementsWithSameOwnText = new ArrayList<>();
        distancesToAnchors = new HashMap<>();
        linkedAnchors = new HashMap<>();
        rootPositionsForAnchors = new HashMap<>();
        uniqueXpaths = new ArrayList<>();
        uniqueXpathsWithAttributes = new ArrayList<>();
        leastRefactoredXpaths = new ArrayList<>();
        leastRefactoredXpathsWithAttributes = new ArrayList<>();
    }

    protected TreeElement(Element element)
    {
        this();
        this.element = element;
    }
}
