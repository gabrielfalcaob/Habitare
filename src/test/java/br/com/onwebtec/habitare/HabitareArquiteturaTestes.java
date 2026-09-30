package br.com.onwebtec.habitare;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "br.com.onwebtec.habitare")
public class HabitareArquiteturaTestes {
    @ArchTest
    public static final ArchRule classesInternasDevemSerAcessadasApenasPeloProprioModulo =
            classes()
                    .that().resideInAPackage("..imovel..")
                    .and().areNotNestedClasses()
                    .should().onlyBeAccessed()
                    .byClassesThat().resideInAnyPackage("..imovel..", "..visita..");
    @ArchTest
    public static final ArchRule controllersNaoDevemChamarRepositoriesDiretamente =
            noClasses()
                    .that().haveSimpleNameEndingWith("Controller")
                    .should().dependOnClassesThat().haveSimpleNameEndingWith("Repository")
                    .because("Controllers devem orquestrar via Services ou Interfaces de Caso de Uso, nunca tocar direto na persistência.");

    @ArchTest
    public static final ArchRule modulosNaoDevemTerDependenciasCiclicas =
            com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices()
                    .matching("br.com.onwebtec.habitare.(*)..")
                    .should().beFreeOfCycles()
                    .because("Dependência circular entre features destrói a modularidade (ex: Imovel depende de Visita que depende de Imovel).");
}
