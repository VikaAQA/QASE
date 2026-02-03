package wrappers;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class Input {
    private static final String TEXT_AREA_XPATH = "(//*[text()='%s']/following-sibling::*//p)[last()]";

    public String getTextAreaText(String label) {
        String text = $x(String.format(TEXT_AREA_XPATH, label)).getText();
        return (text.isEmpty() ? null : text);
    }

     public void setTextInLexicalEditor(String locator, String text) {
        SelenideElement editor = $(locator)
                .shouldBe(Condition.visible, Duration.ofSeconds(20));
         editor.click();
         dismissAlertIfPresent();
         editor.sendKeys(Keys.chord(Keys.CONTROL, "a"));
         editor.sendKeys(Keys.BACK_SPACE);
         editor.sendKeys(text);
    }

    protected void dismissAlertIfPresent() {
        try {
            WebDriverRunner.getWebDriver()
                    .switchTo()
                    .alert()
                    .dismiss();
        } catch (NoAlertPresentException ignored) {
        }
    }
}
