package pages;

import io.qameta.allure.Feature;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.selected;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static data.Elements.CREATE_NEW_PROJECT_BTN;
import static data.Elements.CREATE_PROJECT_BTN;


public class ModalCreateProjectPage {

    private String PUBLIC_RADIO_BTN = "input[value='public']";
    private String PROJECT_NAME_CSS = "#project-name";

    public ModalCreateProjectPage open() {
        CREATE_PROJECT_BTN.shouldBe(visible);//с помощью селенида цепляемся за текста а не локатор
        return this;
    }

    @Step("Создание и заполнение формы проекта")
    public ProductsPage createProject(String project) {
        CREATE_NEW_PROJECT_BTN .click();
        $( PROJECT_NAME_CSS ).setValue(project);
        $(PUBLIC_RADIO_BTN).click();
        $(PUBLIC_RADIO_BTN).shouldBe(selected);
        CREATE_PROJECT_BTN.click();
        return new ProductsPage();
    }
}
