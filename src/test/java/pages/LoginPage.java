package pages;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

@Log4j2
public class LoginPage extends BasePage {
    private final String USER_FIELD_CSS = "[name='email']";
    private final String PASSWORD_FIELD_CSS = "[name='password']";
    private final String SIGN_IN_BUTTON_CSS = "button[type='submit']";

    @Step("Открытие страницы авторизации")
    public LoginPage openPage() {
        open("/login");
        return this;
    }

    @Step("Проверка,что страница авторизации открыта")
    public LoginPage isPageOpened() {
        $(PASSWORD_FIELD_CSS).shouldBe(visible);
        log.info("Страница авторизации успешно открыта");
        return this;
    }

    @Step("Авторизация под валидными данными")
    public ProjectsPage login(String user, String password) {
        log.info("Выполняем авторизацию под пользователем: {}", user);
        $(USER_FIELD_CSS).shouldBe(visible).setValue(user);
        $(PASSWORD_FIELD_CSS).setValue(password);
        $(SIGN_IN_BUTTON_CSS)
                .shouldBe(Condition.enabled)
                .click();
        log.info("Авторизация выполнена, ожидаем переход на страницу продуктов");
        return page(ProjectsPage.class);
    }
}

