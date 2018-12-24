package com.github.tamnguyenbbt;

import com.github.tamnguyenbbt.dom.*;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.List;

public class CodeGeneratorTest
{
    private DomUtil domUtil;

    @Before
    public void init()
    {
        domUtil = new DomUtil();
    }

    @Test
    public void simpleTestMethodCodeGen() throws IOException
    {
        //Arrange
        String resourcePath = getClass().getClassLoader().getResource("google-signup.html").getFile();
        Document document = domUtil.htmlFileToDocument(resourcePath);
        String body = "driver.findElement(By.xpath(\"%s\")).sendKeys(%s);";
        TestMethodInfo testMethodInfo = new TestMethodInfo("void", true, body);
        AssociationRule associationRule = new AssociationRule(HtmlTag.input, TestMethodType.set, testMethodInfo);
        AssociationRules associationRules = new AssociationRules();
        associationRules.add(associationRule);
        CodeGenerator codeGenerator = new CodeGenerator(document, associationRules);

        //Act
        List<String> methods = codeGenerator.generateDocumentMethods();
    }
}
