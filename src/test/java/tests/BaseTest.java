import com.codeborne.selenide.AssertionMode;
import com.codeborne.selenide.Configuration;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.BeforeMethod;


public class BaseTest {

    @BeforeMethod
    public void setUp(){
        Configuration.browser = "chrome";
        Configuration.baseUrl = "https://app.qase.io/login";
        Configuration.timeout = 5000;
        Configuration.clickViaJs = true;//по умолчанию все клики через JS
        Configuration.headless = true;// для работы в CI
        Configuration.browserSize = "1000*1000";
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maxinized");
        Configuration.browserCapabilities = options;
        Configuration.assertionMode = AssertionMode.SOFT;
    }
}
