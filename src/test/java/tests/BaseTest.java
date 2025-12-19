package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.*;
import utils.PropertyReader;

import java.io.ByteArrayInputStream;
import static com.codeborne.selenide.Selenide.closeWebDriver;

import adapters.CaseAPI;
import adapters.ProjectAPI;


@Log4j2
@Listeners(TestListener.class)
public class BaseTest {

    protected LoginPage loginPage;
    protected ProductsPage productsPage;
    protected ProjectPage projectPage;
    protected ModalCreateProjectPage modalCreateProjectPage;
    protected CasePage casePage;
    protected SuitePage suitePage;

    protected ProjectAPI projectAPI;
    protected CaseAPI caseAPI;

    protected String user = System.getProperty("user", PropertyReader.getProperty("user"));
    protected String password = System.getProperty("password", PropertyReader.getProperty("password"));

    @BeforeSuite(alwaysRun = true)
    public void globalSetup() {
        log.info("Очистка тестовых данных перед запуском сьюта");
        new ProjectAPI().deleteAllProject();
    }

    @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true, description = "Environment setup")
    public void setUp(@Optional("chrome") String browser) {
        log.info("Инициализация окружения: browser={}, baseUrl={}", browser, "https://app.qase.io");

        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();

            // 🔴 ВАЖНО: отвечать "НЕТ" на ВСЕ native alert/confirm
            options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.DISMISS);

            Configuration.browser = "chrome";
            Configuration.browserCapabilities = options;
        } else if (browser.equalsIgnoreCase("firefox")) {
            Configuration.browser = "firefox";
        } else {
            throw new IllegalArgumentException("Unknown browser: " + browser);
        }

        Configuration.baseUrl = "https://app.qase.io";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 5000;

        Configuration.clickViaJs = true;
        Configuration.headless = true;

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
        suitePage = new SuitePage();

        projectAPI = new ProjectAPI();
        caseAPI = new CaseAPI();
        log.info("UI и API окружение успешно инициализировано");
    }

    @AfterMethod(alwaysRun = true, description = "Browser teardown")
    public void tearDown(ITestResult result) {
        log.info("Завершение теста: {}", result.getName());

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

        closeWebDriver();

             new ProjectAPI().deleteAllProject();
        log.info("Окружение очищено после теста {}", result.getName());
    }

    @Step("Авторизация и открытие страницы Projects")
    protected void loginAndOpenProductsPage() {
        log.info("Авторизация и переход на страницу Projects");
        loginPage.openPage().isPageOpened();
        loginPage      .login(user, password);
        productsPage.isPageOpened();
    }
}

