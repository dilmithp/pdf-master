package com.pdfmaster.pdfcore.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(
    packages = "com.pdfmaster.pdfcore",
    importOptions = {ImportOption.DoNotIncludeTests.class})
class HexagonalArchitectureTest {

  @ArchTest
  static final ArchRule domainIsPure =
      noClasses()
          .that()
          .resideInAPackage("..domain..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(
              "org.springframework..",
              "jakarta.persistence..",
              "software.amazon..",
              "com.rabbitmq..",
              "org.apache.pdfbox..",
              "net.sourceforge.tess4j..",
              "com.pdfmaster.pdfcore.adapter..");

  @ArchTest
  static final ArchRule applicationOnlyDependsOnDomainAndPorts =
      noClasses()
          .that()
          .resideInAPackage("..application..")
          .and()
          .resideOutsideOfPackage("..application.port..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("..adapter..");

  @ArchTest
  static final ArchRule controllersInAdapterIn =
      classes()
          .that()
          .areAnnotatedWith(org.springframework.web.bind.annotation.RestController.class)
          .should()
          .resideInAPackage("..adapter.in..");
}
