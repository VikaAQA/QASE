package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnhandledAlertException;
import wrappers.DropDawn;
import wrappers.Input;
@Log4j2
public abstract class BasePage {
    protected DropDawn dropDawn;
    protected Input input;
    public abstract BasePage isPageOpened();
    /**
     * Закрывает браузерный alert (confirm/prompt),
     * если он внезапно появился (актуально для CI и headless режима).
     */
    @Step("Закрытие alert, если он появился")
    protected void dismissAlertIfPresent() {
        try {
            WebDriverRunner.getWebDriver()
                    .switchTo()
                    .alert()
                    .dismiss();
            log.warn("Alert был обнаружен и закрыт");
        } catch (NoAlertPresentException ignored) {
            // alert не появился — это нормально
        }
    }

    /**
     * Выполняет JavaScript с защитой от неожиданного alert.
     * Если alert всплывает — закрывает его и повторяет выполнение.
     */
    @Step("Безопасное выполнение JavaScript")
    protected void safeExecuteJs(String script) {
        try {
            Selenide.executeJavaScript(script);
        } catch (UnhandledAlertException e) {
            log.warn("Во время выполнения JS появился alert. Закрываем и повторяем");
            dismissAlertIfPresent();
            Selenide.executeJavaScript(script);
        }
    }

    /**
     * Отключает обработчик beforeunload,
     * чтобы браузер не показывал confirm
     * «Are you sure you want to leave?»
     */
    @Step("Отключение beforeunload (защита от confirm при навигации)")
    protected void disableBeforeUnloadSafe() {
        safeExecuteJs("window.onbeforeunload = null;");
        safeExecuteJs(
                "window.addEventListener('beforeunload', function(e){ e.stopImmediatePropagation(); }, true);"
        );
    }
}
