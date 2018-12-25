package com.github.tamnguyenbbt.dom;

public class SeleniumCodeGenAssociation implements ICodeGenAssociation
{
    @Override
    public AssociationRules generateRules()
    {
        AssociationRules associationRules = new AssociationRules();
        associationRules.add(inputTagSetRule());
        associationRules.add(inputTagGetTextRule());
        return associationRules;
    }

    @Override
    public String generateImportStatements()
    {
        StringBuilder classImportStatementBuilder = new StringBuilder();
        classImportStatementBuilder.append("import org.openqa.selenium.By;\n" +
                "import org.openqa.selenium.WebDriver;\n" +
                "import org.openqa.selenium.WebElement;");

        return classImportStatementBuilder.toString();
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

    private AssociationRule inputTagSetRule()
    {
        String body = "driver.findElement(By.xpath(\"%s\")).sendKeys(%s);";
        TestMethodInfo testMethodInfo = new TestMethodInfo("void", true, body);
        return new AssociationRule(HtmlTag.input, TestMethodType.set, testMethodInfo);
    }

    private AssociationRule inputTagGetTextRule()
    {
        String body = "return driver.findElement(By.xpath(\"%s\")).getText(%s);";
        TestMethodInfo testMethodInfo = new TestMethodInfo("String", false, body);
        return new AssociationRule(HtmlTag.input, TestMethodType.get, testMethodInfo);
    }
}
