package pages;

import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Log4j2
public class RepositoryCasePreviewPage extends BasePage {

    @Step("Открыть страницу превью репозитория: project={projectCode}, caseId={caseId}, suiteId={suiteId}")
    public RepositoryCasePreviewPage open(String projectCode, int caseId, int suiteId) {
       String.format("/project/%s?case=%d&suite=%d",
                projectCode, caseId, suiteId
        );
        return this;
    }

    public RepositoryCasePreviewPage isPageOpened() {
        // проверки, что открыт preview (например, видна панель кейса)
        return this;
    }

    @Step("Проверка успешного создания тест-кейса")
    public RepositoryCasePreviewPage shouldSeeSuccessToast() {
        $(byText("Test case was created successfully!"))
                .shouldBe(visible, Duration.ofSeconds(20));
        log.info("Отображён toast об успешном создании тест-кейса");
        return this;
    }
}