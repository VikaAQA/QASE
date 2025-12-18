package wrappers;

import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class DropDawn {
    private static final String PICKLIST_XPATH = "//label[text()='%s']//following::input";
    private static final String PICKLIST_ITEM_XPATH = "(//*[@id='modals']/*)[last()]//*[text()='%s']";
    private static final String PICKLIST_TEXT_XPATH = "//*[text()='%s']/following-sibling::*//*[text()]";

    /**
     * Выбирает элемент из кастомного выпадающего списка по тексту.
     */
    public void selectFromCustomDropdown(String dropdownLocator,
                                                String dropdownName,
                                                String optionLocator,
                                                String optionText) {
        String dropdownXpath = String.format(dropdownLocator, dropdownName);
        $(byXpath(dropdownXpath)).click(); // открываем список

        String optionXpath = String.format(optionLocator, optionText);
        $(byXpath(optionXpath)).click(); // выбираем элемент
    }
    public void select(String label, String option) {
        if (option != null) {
            $x(String.format(PICKLIST_XPATH, label)).click();
            $x(String.format(PICKLIST_ITEM_XPATH, option)).click();
        }
    }
    public String getPickListText(String label) {
        return $x(String.format(PICKLIST_TEXT_XPATH, label)).getText();
    }
}


