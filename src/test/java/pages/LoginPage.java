package pages;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

@Log4j2
public class LoginPage extends BasePage {
    private final String USER_FIELD = "[name=email]",
            ERROR_MESSAGE = "//small",
            PASSWORD_FIELD = "[name='password']";

    private final String SIGN_IN_BUTTON = "button[type='submit']";

    @Step("Открытие страницы авторизации")
    public LoginPage openPage() {
        open("/login");
        return new LoginPage();
    }

      @Step("Проверка,что страница авторизации открыта")
    public LoginPage isPageOpened() {
        $(PASSWORD_FIELD).shouldBe(visible);
        return this;
    }

    @Step("Авторизация под валидными данными")
    public ProductsPage login(String user, String password) {
        $(USER_FIELD).shouldBe(visible).setValue(user);
        $(PASSWORD_FIELD).setValue(password);
        $(SIGN_IN_BUTTON)
                .shouldBe(Condition.enabled)
                .click();
         return page(ProductsPage.class);
    }

    @Step("Проверка, что отображается сообщение об ошибке: '{error}'")
    public void checkErrorMessage(String error) {
        $x(ERROR_MESSAGE).shouldHave(Condition.text(error));
    }

    @Step("Получение текста сообщения об ошибке со страницы")
    public String getErrorMessage() {//страница открылась и виден текст , затем в тете ассерт
        return $x(ERROR_MESSAGE).text();
    }
}
