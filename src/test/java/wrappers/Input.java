package wrappers;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

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
        SelenideElement editor = $(locator)
                .shouldBe(Condition.visible, Duration.ofSeconds(20));
         editor.click();
         disableBeforeUnloadSafe();
         editor.sendKeys(Keys.chord(Keys.CONTROL, "a"));
         editor.sendKeys(Keys.BACK_SPACE);
         editor.sendKeys(text);
    }
    private void disableBeforeUnloadSafe() {
        Selenide.executeJavaScript(DISABLE_BEFOREUNLOAD_JS);
    }
 }
