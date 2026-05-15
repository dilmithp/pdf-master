package com.pdfmaster.esign.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(
    packages = "com.pdfmaster.esign",
    importOptions = {})
class HexagonalArchitectureTest {

  @ArchTest
  static final ArchRule domainIsPure =
      noClasses()
          .that()
          .resideInAPackage("..domain..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage("org.springframework..", "jakarta.persistence..", "..adapter..");

  @ArchTest
  static final ArchRule applicationDoesNotDependOnAdapters =
      noClasses()
          .that()
          .resideInAPackage("..application..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("..adapter..");

  @ArchTest
  static final ArchRule inboundAdaptersNamedCorrectly =
      classes()
          .that()
          .resideInAPackage("..adapter.in..")
          .and()
          .areTopLevelClasses()
          .should()
          .haveSimpleNameEndingWith("Controller")
          .orShould()
          .haveSimpleNameEndingWith("Listener")
          .orShould()
          .haveSimpleNameEndingWith("WebhookController");

  @ArchTest
  static final ArchRule outboundAdaptersNamedCorrectly =
      classes()
          .that()
          .resideInAPackage("..adapter.out..")
          .and()
          .areTopLevelClasses()
          .and()
          .areNotEnums()
          .should()
          .haveSimpleNameEndingWith("Adapter")
          .orShould()
          .haveSimpleNameEndingWith("Repository")
          .orShould()
          .haveSimpleNameEndingWith("Client")
          .orShould()
          .haveSimpleNameEndingWith("Entity");

  @ArchTest
  static final ArchRule configClassesNamedCorrectly =
      classes()
          .that()
          .resideInAPackage("..config..")
          .and()
          .areTopLevelClasses()
          .should()
          .haveSimpleNameEndingWith("Config")
          .orShould()
          .haveSimpleNameEndingWith("Properties");
}
