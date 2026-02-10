package wrappers;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnhandledAlertException;
import pages.BasePage;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

@Log4j2
public class Input {
    private static final String TEXT_AREA_XPATH = "(//*[text()='%s']/following-sibling::*//p)[last()]";

    // CI fix:
    // В Qase (SPA) иногда появляется confirm "Are you sure you want to leave?"
    // из-за beforeunload, особенно при вводе в rich editor (Lexical).
    private static final String DISABLE_BEFOREUNLOAD_JS =
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
                    "} catch(e) {}";

    public String getTextAreaText(String label) {
        String text = $x(String.format(TEXT_AREA_XPATH, label)).getText();
        return (text.isEmpty() ? null : text);
    }

    /**
     * Ввод текста в Lexical editor (contenteditable).
     * В CI может рандомно появляться beforeunload alert,
     * поэтому перед вводом отключаем beforeunload-обработчики.
     */

            public void setTextInLexicalEditor(String locator, String text) {
            // 0. Проверим, нет ли алерта перед началом
            dismissAlertIfPresent();
            // 1. Отключаем beforeunload сразу
        //    disableBeforeUnloadSafe();

            SelenideElement editor = $(locator).shouldBe(Condition.visible, Duration.ofSeconds(20));
          editor.click();
        dismissAlertIfPresent();
       // disableBeforeUnloadSafe();
        editor.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        editor.sendKeys(Keys.BACK_SPACE);
        dismissAlertIfPresent();
        editor.sendKeys(text);}

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

    public void fillInTextArea(String label, String text) {
        if (text != null) {
            log.info("Writing text '{}' into text area with label '{}'", text, label);
            dismissAlertIfPresent();
            $x(String.format(TEXT_AREA_XPATH, label)).setValue(text);
        }
    }

    public void dismissAlertIfPresent() {
        try {
            WebDriverRunner.getWebDriver().switchTo().alert().dismiss();
        } catch (NoAlertPresentException ignored) {
          }
    }
}
