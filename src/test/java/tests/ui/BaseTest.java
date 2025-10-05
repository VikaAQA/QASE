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
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.*;
import tests.TestListener;
import utils.PropertyReader;

import java.io.ByteArrayInputStream;

import static adapters.ProjectAPI.deleteAllProject;
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
        } else if (browser.equalsIgnoreCase("firefox")) {
            Configuration.browser = "firefox";
        } else {
            throw new IllegalArgumentException("Unknown browser: " + browser);
        }

        Configuration.baseUrl = "https://app.qase.io";
        Configuration.timeout = 5000;
        Configuration.clickViaJs = true;//по умолчанию все клики через JS
        Configuration.headless = false;// для работы в CI,true - тесты крутяться на удаленном сервере
        Configuration.browserSize = "1920x1080";

        // Подключаем Allure listener — именно он прикрепляет скрины к шагам
        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide()
                        .screenshots(true)      // включаем авто-скрины
                        .savePageSource(true)   // сохраняем html страницы
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
        if (getWebDriver() != null) {
            getWebDriver().quit();
        }

        // Удаляем проекты после теста
        deleteAllProject();
    }
}
