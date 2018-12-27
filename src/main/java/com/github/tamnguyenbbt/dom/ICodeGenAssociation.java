package com.github.tamnguyenbbt.dom;

public interface ICodeGenAssociation
{
    AssociationRules generateRules();
    String generatePackageStatement();
    String generateImportStatements();
    String generateClassConstructorWithInjectableClassName();
    String generateClassVariables();
}
