package wrappers;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;

public class DropDawn {

    /**
     * Выбирает элемент из кастомного выпадающего списка по тексту.
     */
    public static void selectFromCustomDropdown(String dropdownLocator,
                                                String dropdownName,
                                                String optionLocator,
                                                String optionText) {
        String dropdownXpath = String.format(dropdownLocator, dropdownName);
        $(byXpath(dropdownXpath)).click(); // открываем список

        String optionXpath = String.format(optionLocator, optionText);
        $(byXpath(optionXpath)).click(); // выбираем элемент
    }
}

