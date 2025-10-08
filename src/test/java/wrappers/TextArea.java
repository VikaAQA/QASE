package wrappers;

import static com.codeborne.selenide.Selenide.$x;
public class TextArea {
    private static final String TEXT_AREA_XPATH = "(//*[text()='%s']/following-sibling::*//p)[last()]";

        public String getTextAreaText(String label) {
        String text = $x(String.format(TEXT_AREA_XPATH, label)).getText();
        return (text.isEmpty() ? null : text);
    }
  }
