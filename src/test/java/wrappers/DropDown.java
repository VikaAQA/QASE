package wrappers;

import lombok.extern.log4j.Log4j2;
import static com.codeborne.selenide.Selenide.$x;
@Log4j2
public class DropDown {
    private static final String PICKLIST_XPATH = "//label[text()='%s']//following::input";
    private static final String PICKLIST_ITEM_XPATH = "(//*[@id='modals']/*)[last()]//*[text()='%s']";
    private static final String PICKLIST_TEXT_XPATH = "//*[text()='%s']/following-sibling::*//*[text()]";

    /**
     * Выбирает элемент из кастомного выпадающего списка по тексту.
     */
    public void select(String label, String option) {
        if (option != null) {
            log.info("Выбираем значение '{}' в выпадающем списке '{}'", option, label);
            $x(String.format(PICKLIST_XPATH, label)).click();
            $x(String.format(PICKLIST_ITEM_XPATH, option)).click();
        }
    }
    /**
     * Возвращает текущее выбранное значение кастомного выпадающего списка.
    */
    public String getPickListText(String label) {
        return $x(String.format(PICKLIST_TEXT_XPATH, label)).getText();
    }
}


