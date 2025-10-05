package wrappers;

import static com.codeborne.selenide.Selenide.$;

public class TextArea {

    public void enterText(String ByLocator, String text) {
        $(ByLocator).setValue(text);
    }
}
