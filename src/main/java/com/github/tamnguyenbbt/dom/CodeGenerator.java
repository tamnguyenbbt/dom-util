package com.github.tamnguyenbbt.dom;

import org.apache.commons.lang.WordUtils;
import org.jsoup.nodes.Document;

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

    protected Tree getTree()
    {
        return tree;
    }

    public String generateSetMethodForInputTag(TreeElement element)
    {
        //return generateMethod(element, new TestMethodInfo(TestMethodType.set, true, null, "sendKeys"));
        return null;
    }

    public String generateMethod(TreeElement element, AssociationRule associationRule)
    {
        if(element != null && element.isValid() && associationRule != null && associationRule.isValid())
        {
            String xpath = element.uniqueXpaths.get(0);
            TreeElement anchor = element.anchorElementsFormingXpaths.get(0);
            String anchorText = Util.removeLineSeparators(anchor.element.ownText()).trim();
            StringBuilder methodBuilder = new StringBuilder();
            String methodName = WordUtils.capitalizeFully(anchorText).replace(" ", "");
            String body;

            if(associationRule.testMethodInfo.hasParam)
            {
                String paramName  = Character.toLowerCase(methodName.charAt(0)) + methodName.substring(1);
                methodName = String.format("public %s %s%s(String %s)", associationRule.testMethodInfo.hasReturn ? "String" : "void", associationRule.methodType, methodName, paramName);
                body = String.format(associationRule.testMethodInfo.bodyWithInjectableXpathAndParam, xpath, paramName);
            }
            else
            {
                methodName = String
                    .format("public %s %s%s()", associationRule.testMethodInfo.hasReturn ? "String" : "void",
                            associationRule.methodType, methodName);
                body = String.format(associationRule.testMethodInfo.bodyWithInjectableXpathAndParam, xpath, "");
            }

            methodBuilder.append(methodName);
            methodBuilder.append("\n{\n");
            //methodBuilder.append(testMethodInfo.returnType == null ? String.format("\t%s", body) : String.format("\treturn %s", body));
            methodBuilder.append("\n}");
            return methodBuilder.toString();
        }

        return null;
    }
}
