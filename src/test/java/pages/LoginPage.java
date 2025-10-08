package pages;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class LoginPage extends BasePage {
    private final String USER_FIELD = "[name=email]",
            ERROR_MESSAGE = "//small",
            PASSWORD_FIELD = "[name='password']";

    @Step("Открытие страницы авторизации")
    public LoginPage openPage() {
        open("/login");
        return this;
    }

    @Step("Авторизация под валидными данными")
    public ProductsPage login(String user, String password) {
        $(USER_FIELD).shouldBe(visible).setValue(user);
        $(PASSWORD_FIELD).setValue(password).submit();
        return new ProductsPage();
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
