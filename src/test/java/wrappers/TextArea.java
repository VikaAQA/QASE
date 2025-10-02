package wrappers;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class TextArea {

    public void enterText(String ByLocator,String text) {
        $(ByLocator).setValue(text);
    }
}
