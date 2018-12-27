package com.github.tamnguyenbbt.dom;

import java.util.ArrayList;
import java.util.List;

public class SeleniumCodeGenAssociation implements ICodeGenAssociation
{
    @Override
    public AssociationRules generateRules()
    {
        AssociationRules associationRules = new AssociationRules();
        associationRules.add(inputTagTypeTextSetRule());
        associationRules.add(inputTagTypeTextGetTextRule());
        associationRules.add(clickableTagsClickRule());
        associationRules.add(selectTagSelectByVisibleTextRule());
        associationRules.add(selectTagSelectByValueRule());
        associationRules.add(selectTagSelectByIndexRule());
        associationRules.add(inputTagTypeCheckBoxCheck());
        associationRules.add(inputTagTypeCheckBoxUncheck());
        associationRules.add(inputTagTypeRadioButtonCheck());
        return associationRules;
    }

    @Override
    public String generatePackageStatement()
    {
        return "package com.github.tamnguyenbbt.dom;\n\n";
    }

    @Override
    public String generateImportStatements()
    {
        return "import org.openqa.selenium.By;\n" +
                "import org.openqa.selenium.WebDriver;\n" +
                "import org.openqa.selenium.WebElement;\n" +
                "import org.openqa.selenium.support.ui.*;";
    }

    @Override
    public String generateClassConstructorWithInjectableClassName()
    {
        return "public %s(WebDriver driver)\n{\n\tthis.driver = driver;\n}";
    }

    @Override
    public String generateClassVariables()
    {
        StringBuilder classVariablesBuilder = new StringBuilder();
        classVariablesBuilder.append("private WebDriver driver;");

        return classVariablesBuilder.toString();
    }

    public AssociationRule inputTagTypeTextSetRule()
    {
        String body = "driver.findElement(By.xpath(\"%s\")).sendKeys(%s);";
        TestMethodInfo testMethodInfo = new TestMethodInfo("void", true, body);
        return new AssociationRule(HtmlTag.input, "text", TestMethodType.set, testMethodInfo);
    }

    public AssociationRule inputTagTypeTextGetTextRule()
    {
        String body = "return driver.findElement(By.xpath(\"%s\")).getText(%s);";
        TestMethodInfo testMethodInfo = new TestMethodInfo("String", false, body);
        return new AssociationRule(HtmlTag.input, "text", TestMethodType.get, testMethodInfo);
    }

    public AssociationRule clickableTagsClickRule()
    {
        String body = "return driver.findElement(By.xpath(\"%s\")).click(%s);";
        TestMethodInfo testMethodInfo = new TestMethodInfo("void", false, body);
        List<HtmlTag> clickableTags = new ArrayList<>();
        clickableTags.add(HtmlTag.a);
        clickableTags.add(HtmlTag.button);
        clickableTags.add(HtmlTag.img);
        clickableTags.add(HtmlTag.label);
        clickableTags.add(HtmlTag.link);
        clickableTags.add(HtmlTag.span);
        clickableTags.add(HtmlTag.div);
        return new AssociationRule(clickableTags, TestMethodType.click, testMethodInfo);
    }

    public AssociationRule selectTagSelectByVisibleTextRule()
    {
        String body = "new Select(driver.findElement(By.xpath(\"%s\"))).selectByVisibleText(%s);";
        TestMethodInfo testMethodInfo = new TestMethodInfo("void", true, body);
        return new AssociationRule(HtmlTag.select, TestMethodType.selectByVisibleText, testMethodInfo);
    }

    public AssociationRule selectTagSelectByValueRule()
    {
        String body = "new Select(driver.findElement(By.xpath(\"%s\"))).selectByValue(%s);";
        TestMethodInfo testMethodInfo = new TestMethodInfo("void", true, body);
        return new AssociationRule(HtmlTag.select, TestMethodType.selectByValue, testMethodInfo);
    }

    public AssociationRule selectTagSelectByIndexRule()
    {
        String body = "new Select(driver.findElement(By.xpath(\"%s\"))).selectByIndex(%s);";
        TestMethodInfo testMethodInfo = new TestMethodInfo("void", true, body);
        return new AssociationRule(HtmlTag.select, TestMethodType.selectByIndex, testMethodInfo);
    }

    public AssociationRule inputTagTypeCheckBoxCheck()
    {
        String body = "WebElement checkBox = driver.findElement(By.xpath(\"%s\"));\n\n" +
                "if (!checkBox.isSelected())\n" +
                "{\n" +
                "\tcheckBox.click(%s);\n" +
                "}";
        TestMethodInfo testMethodInfo = new TestMethodInfo("void", false, body);
        return new AssociationRule(HtmlTag.input, "checkbox", TestMethodType.check, testMethodInfo);
    }

    public AssociationRule inputTagTypeCheckBoxUncheck()
    {
        String body = "WebElement checkBox = driver.findElement(By.xpath(\"%s\"));\n\n" +
                "if (checkBox.isSelected())\n" +
                "{\n" +
                "\tcheckBox.click(%s);\n" +
                "}";
        TestMethodInfo testMethodInfo = new TestMethodInfo("void", false, body);
        return new AssociationRule(HtmlTag.input, "checkbox", TestMethodType.uncheck, testMethodInfo);
    }

    public AssociationRule inputTagTypeRadioButtonCheck()
    {
        String body = "WebElement radioButton = driver.findElement(By.xpath(\"%s\"));\n\n" +
                "if (!radioButton.isSelected())\n" +
                "{\n" +
                "\tradioButton.click(%s);\n" +
                "}";
        TestMethodInfo testMethodInfo = new TestMethodInfo("void", false, body);
        return new AssociationRule(HtmlTag.input, "radio", TestMethodType.check, testMethodInfo);
    }
}