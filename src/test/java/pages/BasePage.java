package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnhandledAlertException;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

@Log4j2
public abstract class BasePage {
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
    public void disableBeforeUnloadHard() {
        Selenide.executeJavaScript(
                "try {" +
                        "  if (window.__bu_blocked) return;" +
                        "  window.__bu_blocked = true;" +
                        "  const origAdd = window.addEventListener;" +
                        "  window.addEventListener = function(type, listener, options) {" +
                        "    if (type === 'beforeunload') return;" +
                        "    return origAdd.call(this, type, listener, options);" +
                        "  };" +
                        "  try {" +
                        "    Object.defineProperty(window, 'onbeforeunload', {" +
                        "      configurable: true," +
                        "      get: function(){ return null; }," +
                        "      set: function(v){ /* blocked */ }" +
                        "    });" +
                        "  } catch(e) { window.onbeforeunload = null; }" +
                        "} catch(e) {}"
        );
    }
    @Step("Принять cookies, если баннер появился")
    public void acceptCookiesIfPresent() {
        safeExecuteJs(
                 "const host = document.querySelector('#usercentrics-cmp-ui');" +
        "if (!host || !host.shadowRoot) return;" +
        "const btn = host.shadowRoot.querySelector('#accept');" +
        "if (btn && btn.offsetParent !== null) btn.click();"
    );}
}

