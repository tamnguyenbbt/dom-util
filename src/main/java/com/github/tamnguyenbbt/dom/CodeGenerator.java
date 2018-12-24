package com.github.tamnguyenbbt.dom;

import org.apache.commons.lang.WordUtils;
import org.jsoup.nodes.Document;
import java.util.ArrayList;
import java.util.List;

public class CodeGenerator
{
    private Tree tree;
    private AssociationRules associationRules;

    public CodeGenerator(Document document, AssociationRules associationRules)
    {
        if(document != null)
        {
            tree = new Tree(document);
        }

        this.associationRules = associationRules;
    }

    public List<String> generateDocumentMethods()
    {
        List<String> methods = new ArrayList<>();

        if(Util.hasItem(tree))
        {
            for (TreeElement item : tree)
            {
                if(item.element.tagName().equals("input"))
                {
                    methods.add(generateMethod(item, associationRules.get(0)));
                    break;
                }
            }
        }

        return methods;
    }

    private String generateMethod(TreeElement element, AssociationRule associationRule)
    {
        if(element != null && element.isValid() && associationRule != null && associationRule.isValid())
        {
            String xpath = element.uniqueXpaths.get(0);
            TreeElement anchor = element.anchorElementsFormingXpaths.get(0);
            String anchorText = Util.removeLineSeparators(anchor.element.ownText()).trim();
            StringBuilder methodBuilder = new StringBuilder();
            String methodName = WordUtils.capitalizeFully(anchorText).replace(" ", "");
            String paramName  = associationRule.testMethodInfo.hasParam ? Character.toLowerCase(methodName.charAt(0)) + methodName.substring(1) : "";
            methodName = String.format("public %s %s%s(%s)",
                    associationRule.testMethodInfo.returnType,
                    associationRule.methodType,
                    methodName,
                    paramName == "" ? "" : String.format("String %s", paramName));
            String body = String.format(associationRule.testMethodInfo.bodyWithInjectableXpathAndParam, xpath, paramName);
            methodBuilder.append(methodName);
            methodBuilder.append("\n{\n");

            if(paramName != "")
            {
                methodBuilder.append(String.format("\tif(%s != null)\n", paramName));
                methodBuilder.append("\t{\n");
                methodBuilder.append(String.format("\t\t%s\n", body));
                methodBuilder.append("\t}");
            }
            else
            {
                methodBuilder.append(String.format("\t\t%s", body));
            }

            methodBuilder.append("\n}");
            return methodBuilder.toString();
        }

        return null;
    }
}