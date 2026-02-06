package pages;

import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;

import static com.codeborne.selenide.Condition.selected;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static data.Elements.*;


@Log4j2
public class ModalCreateProjectPage extends BasePage {

    private final String PUBLIC_RADIO_BTN_CSS = "input[value='public']";

    @Step("Открытие модального окна создания проекта")
    public ModalCreateProjectPage  isPageOpened() {
        CREATE_PROJECT_BTN.shouldBe(visible, Duration.ofSeconds(60));
        log.info("Модальное окно создания проекта успешно открыто");
        return this;
    }
    @Step("Создать проект '{project}' в модалке")
    public RepositoryPage createProject(String project) {
        log.info("Заполняем модалку и создаём проект: {}", project);

        $(PROJECT_NAME_FIELD_CSS).setValue(project);
        $(PUBLIC_RADIO_BTN_CSS).click();
        $(PUBLIC_RADIO_BTN_CSS).shouldBe(selected);
        CREATE_PROJECT_BTN.click();

        log.info("Проект '{}' отправлен на создание", project);
        return new RepositoryPage();
    }

    @Step("Проверка создания проекта без названия (негативный сценарий)")
    public ModalCreateProjectPage createFailProject() {
        log.info("Проверка валидации: попытка создать проект без названия");

        CREATE_NEW_PROJECT_BTN.click();
        $(PUBLIC_RADIO_BTN_CSS ).click();
        $(PUBLIC_RADIO_BTN_CSS ).shouldBe(selected);
        CREATE_PROJECT_BTN.click();

        log.info("Проект без названия не был создан — ожидается сообщение об ошибке");
        return this;
    }
}
