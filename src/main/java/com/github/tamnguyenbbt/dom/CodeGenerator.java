package com.github.tamnguyenbbt.dom;

import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CodeGenerator
{
    private Tree tree;
    private Document document;
    private AssociationRules associationRules;
    private ICodeGenAssociation codeGenAssociation;

    public CodeGenerator(Document document, ICodeGenAssociation codeGenAssociation)
    {
        this.document = document;
        this.codeGenAssociation = codeGenAssociation;
        this.associationRules = codeGenAssociation.generateRules();

        if(document != null)
        {
            tree = new Tree(document);
        }
    }

    public String getCodeGenClassName()
    {
        String className = Util.removeSpecialCharacters(document.title());
        className = Util.removeLineSeparators(className).trim();
        return Util.toTitleCase(className).replace(" ", "");
    }

    public void generatePageObjectModelClass(String path) throws IOException
    {
        String content = generatePageObjectModelClass();
        Util.saveToFile(content, path);
    }

    public String generatePageObjectModelClass()
    {
        if(document != null)
        {
            String className = getCodeGenClassName();
            StringBuilder pageObjectModelBuilder = new StringBuilder();
            pageObjectModelBuilder.append(codeGenAssociation.generatePackageStatement());
            String importStatements = codeGenAssociation.generateImportStatements();

            if(importStatements != null)
            {
                pageObjectModelBuilder.append(importStatements);
                pageObjectModelBuilder.append("\n\n");
            }

            pageObjectModelBuilder.append(String.format("public class %s\n", className));
            pageObjectModelBuilder.append("{\n");
            String classVariables = codeGenAssociation.generateClassVariables();

            if(classVariables != null)
            {
                classVariables = "\t" + classVariables.replace("\n", "\n\t");
                pageObjectModelBuilder.append(classVariables);
                pageObjectModelBuilder.append("\n\n");
            }

            String constructor = codeGenAssociation.generateClassConstructorWithInjectableClassName();

            if(constructor != null)
            {
                constructor = String.format(constructor, className);
                constructor = "\t" + constructor.replace("\n", "\n\t");
                pageObjectModelBuilder.append(constructor);
                pageObjectModelBuilder.append("\n\n");
            }

            String methods = generateMethods();
            methods = "\t" + methods.replace("\n", "\n\t");

            if(methods != null)
            {
                pageObjectModelBuilder.append(methods);
            }

            pageObjectModelBuilder.append("\n}");
            return pageObjectModelBuilder.toString();
        }

        return null;
    }

    private String generateMethods()
    {
        List<MapEntry<String, String>> methods = generateDocumentMethods();

        if(Util.hasItem(methods))
        {
            StringBuilder methodsBuilder = new StringBuilder();
            int methodCount = methods.size();
            List<MapEntry<String, Integer>> takenMethodNames = new ArrayList<>();

            for (int i = 0; i< methodCount; i++)
            {
                MapEntry<String, String> pair = methods.get(i);
                String methodName = pair.getKey();
                String method = pair.getValue();

                int takenMethodVersion = -1;

                for (MapEntry<String, Integer> item : takenMethodNames)
                {
                    if(item.getKey().equals(methodName))
                    {
                        takenMethodVersion = Math.max(takenMethodVersion, item.getValue());
                    }
                }

                if(takenMethodVersion == -1)
                {
                    methodsBuilder.append(method);
                }
                else
                {
                    String newMethodName = String.format("%s_%s", methodName, takenMethodVersion + 1);
                    method = method.replaceFirst(methodName, newMethodName);
                    methodsBuilder.append(method);
                }

                takenMethodNames.add(new MapEntry<>(methodName, takenMethodVersion + 1));

                if(i != methodCount - 1)
                {
                    methodsBuilder.append("\n\n");
                }
            }

            return methodsBuilder.toString();
        }

        return null;
    }

    private List<MapEntry<String, String>> generateDocumentMethods()
    {
        List<MapEntry<String, String>> methods = new ArrayList<>();

        if(Util.hasItem(tree) && Util.hasItem(associationRules))
        {
            for (TreeElement item : tree)
            {
                methods.addAll(generateTreeElementMethods(item));
            }
        }

        return methods;
    }

    private List<MapEntry<String, String>> generateTreeElementMethods(TreeElement treeElement)
    {
        List<MapEntry<String, String>> methods = new ArrayList<>();

        if(treeElement != null && Util.hasItem(associationRules))
        {
            for(AssociationRule associationRule : associationRules)
            {
                methods.addAll(generateTreeElementMethodsPerRule(treeElement, associationRule));
            }
        }

        return methods;
    }

    private List<MapEntry<String, String>> generateTreeElementMethodsPerRule(TreeElement treeElement, AssociationRule rule)
    {
        List<MapEntry<String, String>> methods = new ArrayList<>();

        if(treeElement != null && rule != null && Util.hasItem(rule.tags))
        {
            for(HtmlTag tag : rule.tags)
            {
                if(Util.hasItem(rule.typeAttributeValues))
                {
                    if(treeElement.element.tagName().equalsIgnoreCase(tag.toString()) &&
                            treeElement.element.hasAttr("type") &&
                            rule.typeAttributeValues.contains(treeElement.element.attr("type")))
                    {
                        methods.add(generateMethod(treeElement, rule));
                    }
                }
                else
                {
                    if(treeElement.element.tagName().equalsIgnoreCase(tag.toString()))
                    {
                        methods.add(generateMethod(treeElement, rule));
                    }
                }
            }
        }

        return methods;
    }

    private MapEntry<String,String> generateMethod(TreeElement element, AssociationRule associationRule)
    {
        if(element != null && element.isValid() && associationRule != null && associationRule.isValid())
        {
            String xpath = element.uniqueXpaths.get(0);
            TreeElement anchor = element.anchorElementsFormingXpaths.get(0);
            String anchorText = anchor.element.ownText();
            anchorText = Util.removeSpecialCharacters(anchorText);
            anchorText = Util.removeLineSeparators(anchorText).trim();
            anchorText = Util.toTitleCase(anchorText);
            anchorText = anchorText.replace(" ", "");
            StringBuilder methodBuilder = new StringBuilder();
            String methodName = associationRule.methodType + anchorText;
            String paramName  = associationRule.testMethodInfo.hasParam ? Character.toLowerCase(anchorText.charAt(0)) + anchorText.substring(1) : "";
            String methodSignature = String.format("public %s %s(%s)",
                    associationRule.testMethodInfo.returnType,
                    methodName,
                    paramName == "" ? "" : String.format("String %s", paramName));
            String body = String.format(associationRule.testMethodInfo.bodyWithInjectableXpathAndParam, xpath, paramName);
            methodBuilder.append(methodSignature);
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
                methodBuilder.append(String.format("\t%s", body));
            }

            methodBuilder.append("\n}");
            return new MapEntry<>(methodName, methodBuilder.toString());
        }

        return null;
    }
}