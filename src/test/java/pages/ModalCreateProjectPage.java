package pages;

import io.qameta.allure.Step;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import static com.codeborne.selenide.Condition.selected;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static data.Elements.CREATE_NEW_PROJECT_BTN;
import static data.Elements.CREATE_PROJECT_BTN;

@Data
@Log4j2
public class ModalCreateProjectPage extends BasePage {

    public static final String PROJECT_NAME_FIELD_CSS = "#project-name";//!!убрать статис
    private String PUBLIC_RADIO_BTN = "input[value='public']";

    @Step("Открытие модального окна создания проекта")
    public ModalCreateProjectPage  isPageOpened() {
        CREATE_PROJECT_BTN.shouldBe(visible);//с помощью селенида цепляемся за текста а не локатор
        return this;
    }
    @Step("Создание нового проекта с именем: '{project}'")
    public ProductsPage createProject(String project) {
        log.info("Начало создания проекта с именем: {}", project);

        CREATE_NEW_PROJECT_BTN.click();
        $(PROJECT_NAME_FIELD_CSS).setValue(project);
        $(PUBLIC_RADIO_BTN).click();
        $(PUBLIC_RADIO_BTN).shouldBe(selected);
        CREATE_PROJECT_BTN.click();

        log.info("Проект '{}' успешно создан", project);
        return new ProductsPage();
    }

    @Step("Проверка создания проекта без названия (негативный сценарий)")
    public ModalCreateProjectPage createFailProject() {
        log.info("Проверка валидации: попытка создать проект без названия");

        CREATE_NEW_PROJECT_BTN.click();
        $(PUBLIC_RADIO_BTN).click();
        $(PUBLIC_RADIO_BTN).shouldBe(selected);
        CREATE_PROJECT_BTN.click();

        log.info("Проект без названия не был создан — ожидается сообщение об ошибке");
        return this;
    }
}
