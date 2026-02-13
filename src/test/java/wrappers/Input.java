package wrappers;

import com.codeborne.selenide.WebDriverRunner;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.NoAlertPresentException;
import static com.codeborne.selenide.Selenide.*;

@Log4j2
public class Input {
    private static final String TEXT_AREA_XPATH = "(//*[text()='%s']/following-sibling::*//p)[last()]";
    /**
     * Возвращает текст из текстового поля по его label.
     */
    public String getTextAreaText(String label) {
        String text = $x(String.format(TEXT_AREA_XPATH, label)).getText();
        return (text.isEmpty() ? null : text);
    }
    /**
     * Заполняет текстовое поле указанным текстом.
     */
    public void fillInTextArea(String label, String text) {
        if (text != null) {
            log.info("Ввод текста '{}' в текстовое поле с меткой '{}'", text, label);
            dismissAlertIfPresent();
            $x(String.format(TEXT_AREA_XPATH, label)).setValue(text);
        }
    }
    /**
     * Закрывает браузерный alert, если он присутствует.
     */
    public void dismissAlertIfPresent() {
        try {
            WebDriverRunner.getWebDriver().switchTo().alert().dismiss();
        } catch (NoAlertPresentException ignored) {
          }
    }
}
