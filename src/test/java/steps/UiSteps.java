package steps;

import dto.QaseTestCaseDto;
import dto.QaseTestSuiteDto;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import pages.*;

@Log4j2
public class UiSteps {

    private final ModalCreateProjectPage modalCreateProjectPage;
    private final RepositoryPage repositoryPage;
    private final ProjectsPage projectsPage;
    private final CaseCreatePage caseCreatePage;
    private final SuitePage suitePage;
    private final CaseEditPage caseEditPage;

    public UiSteps(ModalCreateProjectPage modalCreateProjectPage,
                   RepositoryPage repositoryPage,
                   ProjectsPage projectsPage,
                   CaseCreatePage caseCreatePage,
                   SuitePage suitePage,
                   CaseEditPage caseEditPage) {
        this.modalCreateProjectPage = modalCreateProjectPage;
        this.repositoryPage = repositoryPage;
        this.projectsPage = projectsPage;
        this.caseCreatePage = caseCreatePage;
        this.suitePage = suitePage;
        this.caseEditPage = caseEditPage;
    }
    @Step("UI: Создать проект '{projectName}'")
    public UiSteps createProject(String projectName) {
           log.info("UI: создаём проект '{}'", projectName);

            projectsPage.openCreateProjectModal()
                        .createProject(projectName);

            repositoryPage.checkCreatingProject(projectName);
            return this;
        }
    @Step("UI: Попытаться создать проект без названия (негативный сценарий)")
    public UiSteps createProjectWithoutTitle() {
        log.info("UI: негативный сценарий создания проекта без названия");
        modalCreateProjectPage.createFailProject();
        return this;
    }
    @Step("UI: Удалить проект '{projectName}'")
    public UiSteps deleteProject(String projectName) {
        log.info("UI: удаляем проект '{}'", projectName);
        projectsPage.openPage()
                .isPageOpened()
                .deleteProject(projectName);
        return this;
    }
    @Step("UI: Создать тест-кейс '{title}'")
    public UiSteps createCase(QaseTestCaseDto testCase) {
        log.info("UI: создаём тест-кейс '{}'", testCase.getTitle());

        repositoryPage.openCasePage();
        caseCreatePage.isPageOpened()
                .fillCreateCaseForm(testCase)
                .clickSave();
        repositoryPage.shouldSeeSuccessToast();
        return this;
    }
    @Step("UI: Проверить, что количество тест-кейсов = {expectedCount}")
    public UiSteps assertCaseCount(int expectedCount) {
        repositoryPage.checkThatTestCaseIsCreated(expectedCount);
        return this;
    }
    @Step("UI: Открыть редактирование кейса id={caseId} в проекте '{projectCode}' и проверить соответствие введенных данных")
    public UiSteps openEditAndAssertCaseFormMatches(String projectCode, int caseId, QaseTestCaseDto expected) {
        log.info("UI: проверяем, что форма кейса id={} соответствует введённым данным", caseId, projectCode);

        caseEditPage.openEditCasePage(projectCode, caseId)
                .isPageOpened()
                .assertEditFormMatchesTestCase(expected);
        return this;
    }
    @Step("UI: Создать Suite '{suiteName}'")
    public UiSteps createSuite(QaseTestSuiteDto suite) {
        log.info("UI: создаём suite '{}'", suite.getSuit_name());

        repositoryPage.openSuitPage();

        suitePage.isPageOpened()
                .fillFormSuitePge(suite);
        return this;
    }
    @Step("UI: Проверить, что Suite '{suiteName}' отображается в репозитории")
    public UiSteps assertSuiteVisible(String suiteName) {
        repositoryPage.shouldHaveSuite(suiteName);
        return this;
    }
}
