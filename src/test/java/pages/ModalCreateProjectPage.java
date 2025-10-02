package pages;

import io.qameta.allure.Step;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import static com.codeborne.selenide.Condition.selected;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static data.Elements.CREATE_NEW_PROJECT_BTN;
import static data.Elements.CREATE_PROJECT_BTN;

@Data
@Log4j2
public class ModalCreateProjectPage extends BasePage{

    private String PUBLIC_RADIO_BTN = "input[value='public']";
    public static final String PROJECT_NAME_FIELD_CSS = "#project-name";//!!убрать статис

    public ModalCreateProjectPage open() {
        CREATE_PROJECT_BTN.shouldBe(visible);//с помощью селенида цепляемся за текста а не локатор
        return this;
    }


    @Step("Создание и заполнение формы проекта")
    public ProductsPage createProject(String project) {
        log.info("Начало создания проекта с именем: {}", project);
           CREATE_NEW_PROJECT_BTN .click();
        $(PROJECT_NAME_FIELD_CSS).setValue(project);
        $(PUBLIC_RADIO_BTN).click();
        $(PUBLIC_RADIO_BTN).shouldBe(selected);
  CREATE_PROJECT_BTN.click();
              return new ProductsPage();
    }

    @Step("")
    public ModalCreateProjectPage createFailProject(){
        CREATE_NEW_PROJECT_BTN .click();
        $(PUBLIC_RADIO_BTN).click();
        $(PUBLIC_RADIO_BTN).shouldBe(selected);
        CREATE_PROJECT_BTN.click();
        return this;
    }
}
