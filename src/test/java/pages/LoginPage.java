package pages;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPages {
    private final String USER_FIELD = "[name=email]";
    private final String PASSWORD_FIELD = "[name=password]";

    public void openPage() {
        open("/login");
    }

    public void login(String user, String password) {
        $(USER_FIELD).setValue(user);
        $(PASSWORD_FIELD).setValue(password).submit();
  
    }
}
