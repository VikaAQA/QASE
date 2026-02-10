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
            "window.onbeforeunload = null;" +
                    "if (!window.__beforeUnloadBlocked) {" +
                    "  window.__beforeUnloadBlocked = true;" +
                    "  const add = window.addEventListener;" +
                    "  window.addEventListener = function(type, listener, options) {" +
                    "    if (type === 'beforeunload') return;" +
                    "    return add.call(this, type, listener, options);" +
                    "  };" +
                    "}";

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
            disableBeforeUnloadSafe();

            SelenideElement editor;
            try {
                editor = $(locator).shouldBe(Condition.visible, Duration.ofSeconds(20));
            } catch (UnhandledAlertException e) {
                log.warn("UnhandledAlertException при поиске элемента — закрываем и пробуем снова");
                dismissAlertIfPresent();
                // Ждём немного перед повтором
                sleep(500);
                editor = $(locator).shouldBe(Condition.visible, Duration.ofSeconds(20));
            }

            editor.click();
            dismissAlertIfPresent();

            // 2. Безопасный ввод текста
            safeSendKeys(editor, Keys.chord(Keys.CONTROL, "a"));
            safeSendKeys(editor, Keys.BACK_SPACE);
            safeSendKeys(editor, text);
        }
     /*   editor.click();
        dismissAlertIfPresent();
        disableBeforeUnloadSafe();
        editor.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        editor.sendKeys(Keys.BACK_SPACE);
        dismissAlertIfPresent();
        editor.sendKeys(text);*/

    private void disableBeforeUnloadSafe() {
        Selenide.executeJavaScript(DISABLE_BEFOREUNLOAD_JS);
    }

    public void dismissAlertIfPresent() {
        try {
            WebDriverRunner.getWebDriver().switchTo().alert().dismiss();
        } catch (NoAlertPresentException ignored) {
          }
    }

    private void safeSendKeys(SelenideElement element, CharSequence... keys) {
        int maxRetries = 3;
        for (int i = 0; i < maxRetries; i++) {
            try {
                element.sendKeys(keys);
                return;
            } catch (UnhandledAlertException e) {
                log.warn("Алерт при sendKeys (попытка {}), закрываем...", i + 1);
                dismissAlertIfPresent();
                if (i == maxRetries - 1) throw e;
            }
        }
    }
}
