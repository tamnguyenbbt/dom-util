package com.github.tamnguyenbbt.dom;

public interface ICodeGenAssociation
{
    AssociationRules generateRules();
    String generateImportStatements();
    String generateClassConstructorWithInjectableClassName();
    String generateClassVariables();
}
