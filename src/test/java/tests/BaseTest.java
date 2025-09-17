package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.LoginPage;
import pages.ModalCreateProjectPage;
import pages.ProductsPage;
import pages.ProjectPage;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;


public class BaseTest {
   LoginPage loginPage;
    ProductsPage productsPage;
    ProjectPage projectPage;
    ModalCreateProjectPage modalCreateProjectPage;

    String user = System.getProperty("user",PropertyReader.getProperty("user"));//скрытие кредов указаны в config.properties
    String password = System.getProperty("password",PropertyReader.getProperty("password"));//скрытие кредов

    @BeforeMethod
    public void setUp() {
        Configuration.browser = "chrome";
        Configuration.baseUrl = "https://app.qase.io";
        Configuration.timeout = 5000;
        Configuration.clickViaJs = true;//по умолчанию все клики через JS
        Configuration.headless = true;// для работы в CI,true - тесты крутяться на удаленном сервере
        Configuration.browserSize = "1920x1080";
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        Configuration.browserCapabilities = options;
        //Configuration.assertionMode = AssertionMode.SOFT;

        Configuration.browserCapabilities = options;

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
    }
    @AfterMethod
    public void tearDown(ITestResult result) {
              // Закрытие драйвера
        if (getWebDriver() != null) {
            getWebDriver().quit();
        }
    }
}
