package com.github.tamnguyenbbt.dom;

import org.apache.commons.lang.WordUtils;
import org.jsoup.nodes.Document;

//simple for testing
public class CodeGenerator
{
    private Tree tree;

    public CodeGenerator(Document document)
    {
        if(document != null)
        {
            tree = new Tree(document);
        }
    }

    protected Tree getTree()
    {
        return tree;
    }

    public String generateSetMethodForInputTag(TreeElement element)
    {
        return generateMethod(element, new TestMethodInfo(TestMethodType.set, true, null, "sendKeys"));
    }

    public String generateMethod(TreeElement element, TestMethodInfo testMethodInfo)
    {
        if(element != null && element.isValid() && testMethodInfo != null)
        {
            String xpath = element.uniqueXpaths.get(0);
            TreeElement anchor = element.anchorElementsFormingXpaths.get(0);
            String anchorText = Util.removeLineSeparators(anchor.element.ownText()).trim();
            StringBuilder methodBuilder = new StringBuilder();
            String methodName = WordUtils.capitalizeFully(anchorText).replace(" ", "");
            String body;

            if(testMethodInfo.hasParam)
            {
                String paramName  = Character.toLowerCase(methodName.charAt(0)) + methodName.substring(1);
                methodName = String.format("public %s %s%s(String %s)", testMethodInfo.returnType == null ? "void" : testMethodInfo.returnType, testMethodInfo.methodType, methodName, paramName);
                body = String.format("driver.getElement(By.Xpath(%s)).%s(%s);", xpath, testMethodInfo.seleniumFuncName, paramName);
            }
            else
            {
                methodName = String.format("public %s %s%s()", testMethodInfo.returnType == null ? "void" : testMethodInfo.returnType, testMethodInfo.methodType, methodName);
                body = String.format("driver.getElement(By.Xpath(%s)).%s();", xpath, testMethodInfo.seleniumFuncName);
            }

            methodBuilder.append(methodName);
            methodBuilder.append("\n{\n");
            methodBuilder.append(testMethodInfo.returnType == null ? String.format("\t%s", body) : String.format("\treturn %s", body));
            methodBuilder.append("\n}");
            return methodBuilder.toString();
        }

        return null;
    }
}
