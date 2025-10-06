package tests.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.*;
import tests.TestListener;
import utils.PropertyReader;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import static adapters.ProjectAPI.deleteAllProject;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
@Log4j2
@Listeners(TestListener.class)
public class BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;
    ProjectPage projectPage;
    ModalCreateProjectPage modalCreateProjectPage;
    CasePage casePage;
    String user = System.getProperty("user", PropertyReader.getProperty("user"));//скрытие кредов указаны в config.properties
    String password = System.getProperty("password", PropertyReader.getProperty("password"));//скрытие кредов

    @BeforeSuite(alwaysRun = true)
    public void globalSetup() {
        log.info("Очистка тестовых данных перед запуском сьюта");
        deleteAllProject();
    }

    @Parameters({"browser"})
    @BeforeMethod(description = "Browser setup", alwaysRun = true)
    public void setUp(@Optional("chrome") String browser) {
        log.info("Opening browser {}", browser);

        if (browser.equalsIgnoreCase("chrome")) {
            Configuration.browser = "chrome";

            ChromeOptions options = new ChromeOptions();

            // Устанавливаем язык интерфейса и сообщений
            options.addArguments("--lang=ru");
            options.addArguments("--accept-lang=ru");
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");

            // Принудительно задаем русскую локаль (влияет на validationMessage)
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("intl.accept_languages", "ru,ru_RU");
            options.setExperimentalOption("prefs", prefs);

            Configuration.browserCapabilities = options;
            log.info("Chrome language set to ru-RU");

        } else if (browser.equalsIgnoreCase("firefox")) {
            Configuration.browser = "firefox";

            FirefoxOptions options = new FirefoxOptions();
            options.addPreference("intl.accept_languages", "ru,ru_RU");
            Configuration.browserCapabilities = options;
            log.info("Firefox language set to ru-RU");

        } else {
            throw new IllegalArgumentException("Unknown browser: " + browser);
        }

        Configuration.baseUrl = "https://app.qase.io";
        Configuration.timeout = 5000;
        Configuration.clickViaJs = true;
        Configuration.headless = true;
        Configuration.browserSize = "1920x1080";

        // Allure listener
        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide()
                        .screenshots(true)
                        .savePageSource(true)
        );

        loginPage = new LoginPage();
        productsPage = new ProductsPage();
        projectPage = new ProjectPage();
        modalCreateProjectPage = new ModalCreateProjectPage();
        casePage = new CasePage();
    }



    @AfterMethod(alwaysRun = true, description = "Browser teardown")
    public void tearDown(ITestResult result) {
        log.info("Closing browser");

        // Если тест упал — прикрепляем скриншот в Allure
        if (!result.isSuccess()) {
            try {
                byte[] screenshot = ((TakesScreenshot) WebDriverRunner.getWebDriver())
                        .getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("Screenshot on failure", new ByteArrayInputStream(screenshot));
                log.error("Тест упал: {} — скриншот добавлен в отчет Allure", result.getName());
            } catch (Exception e) {
                log.error("Не удалось сделать скриншот при падении теста {}: {}", result.getName(), e.getMessage());
            }
        }

        // Закрываем браузер
        closeWebDriver();

        // Удаляем проекты после теста
        deleteAllProject();
    }
}
