package com.paf.exercise;

import static com.tngtech.archunit.lang.conditions.ArchConditions.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.Architectures;

/**
 * This class is used to enforce architectural rules for the package structure within the
 * "com.paf.exercise" package. It utilizes ArchUnit to define and enforce a domain-driven design
 * architecture, ensuring that different layers (Application, Domain, Infrastructure) adhere to
 * specified dependencies and access constraints. This helps maintain a clean separation of concerns
 * and prevents unintended coupling between layers.
 */
@AnalyzeClasses(
    packages = "com.paf.exercise",
    importOptions = {DoNotIncludeTests.class})
public class PackageStructureTest {

  private static final String ROOT_PACKAGE = "com.paf.exercise.";
  public static final String APPLICATION = "Application";
  public static final String DOMAIN = "Domain";
  public static final String INFRASTRUCTURE = "Infrastructure";
  public static final String TEST = "Test";

  @ArchTest
  public void enforceDomainDrivenArchitecture(JavaClasses classes) {
    Architectures.LayeredArchitecture layeredArchitecture =
        Architectures.layeredArchitecture()
            .consideringAllDependencies()
            .layer(APPLICATION)
            .definedBy(ROOT_PACKAGE + ".application..")
            .optionalLayer(DOMAIN)
            .definedBy(ROOT_PACKAGE + ".domain..")
            .optionalLayer(INFRASTRUCTURE)
            .definedBy(ROOT_PACKAGE + ".infrastructure..");

    layeredArchitecture =
        layeredArchitecture
            .whereLayer(APPLICATION)
            .mayNotBeAccessedByAnyLayer()
            .whereLayer(DOMAIN)
            .mayOnlyBeAccessedByLayers(APPLICATION, INFRASTRUCTURE)
            .whereLayer(INFRASTRUCTURE)
            .mayOnlyBeAccessedByLayers(APPLICATION);

    layeredArchitecture.check(classes);
  }
}
