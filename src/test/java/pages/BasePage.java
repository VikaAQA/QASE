package pages;

import wrappers.DropDawn;
import wrappers.Input;

public abstract class BasePage {
    protected DropDawn dropDawn;
    protected Input input;
    public abstract BasePage isPageOpened();
}
