package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnhandledAlertException;
import wrappers.DropDawn;
import wrappers.Input;

public abstract class BasePage {
    protected DropDawn dropDawn;
    protected Input input;
    public abstract BasePage isPageOpened();

    /**
     * Закрывает браузерный alert (confirm/prompt),
     * если он внезапно появился (актуально для CI и headless режима).
     */

    protected void dismissAlertIfPresent() {
        try {
            WebDriverRunner.getWebDriver()
                    .switchTo()
                    .alert()
                    .dismiss();
                  } catch (NoAlertPresentException ignored) {
           }
    }

    /**
     * Выполняет JavaScript с защитой от неожиданного alert.
     * Если alert всплывает — закрывает его и повторяет выполнение.
     */

    protected void safeExecuteJs(String script) {
        try {
            Selenide.executeJavaScript(script);
        } catch (UnhandledAlertException e) {
                       dismissAlertIfPresent();
            Selenide.executeJavaScript(script);
        }
    }

    /**
     * Отключает обработчик beforeunload,
     * чтобы браузер не показывал confirm
     * «Are you sure you want to leave?»
     */

    protected void disableBeforeUnloadSafe() {
        safeExecuteJs("window.onbeforeunload = null;");
        safeExecuteJs(
                "window.addEventListener('beforeunload', function(e){ e.stopImmediatePropagation(); }, true);"
        );
    }
    @Step("Принять cookies, если баннер появился")
    public void acceptCookiesIfPresent() {
        safeExecuteJs(
                "const b=document.querySelector('#usercentrics-cmp-ui');" +
                        "if(!b) return;" +
                        "const btn=b.shadowRoot?.querySelector('#accept');" +
                        "btn?.click();"
        );
    }

}
