package com.github.tamnguyenbbt;

import com.github.tamnguyenbbt.dom.*;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

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
        CodeGenerator codeGenerator = new CodeGenerator(document, new SeleniumCodeGenAssociation());

        //Act
        String pageObjectModel = codeGenerator.generatePageObjectModelClass();
    }
}
