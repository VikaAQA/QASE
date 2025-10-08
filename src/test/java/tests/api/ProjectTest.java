package tests.api;

import adapters.BaseAPI;
import io.qameta.allure.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.project.create.CreateProjectRs;
import models.project.get.GetProjectRs;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tests.BaseTest;
import utils.ProjectRequestFactory;

import static adapters.ProjectAPI.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.testng.Assert.assertTrue;
@Epic("API Tests")
public class ProjectTest extends BaseTest {
    @BeforeMethod
    public void delete(){projectAPI.deleteAllProject();}

    @Test(description = "Создание и удаление проекта")
    @Severity(SeverityLevel.CRITICAL)
    @Description(" Проверка базового сценария создания удаления нового проекта")
        public void checkCreateAndDeleteProject() {
        CreateProjectRs rs = projectAPI.createProject(ProjectRequestFactory.validProject());
        assertTrue(rs.status);
        String code = rs.getResult().getCode();
        projectAPI.deleteProject(code);
    }

    @Test(groups = "smoke", description = "Создание проекта: проверка корректности полей title и code")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка корректности данных, возвращаемых при создании проекта")
    public void cheсkFieldCreateFormNewProject() {

        CreateProjectRs rs = projectAPI.createProject(ProjectRequestFactory.validProject());//передаем тело запроса, возвращает обьект
        String code = rs.getResult().getCode();

        GetProjectRs getRs = projectAPI.getProject(code);//получаем созданный проект по коду

        assertThat(getRs.getResult().getTitle())//в responce json приходит со списком обьектов , то есть мы сначало должны получить лист проектов и выбрать первый элемент
                .isEqualTo(ProjectRequestFactory.validProject().getTitle());
       assertThat(getRs.getResult().getCode()).isEqualTo(code);
        projectAPI.deleteProject(code);
    }

    @Test(description = "Создание проекта без обязательного поля Title должно вернуть ошибку 400")
    @Description("Проверка, что при создании проекта без обязательного поля Title сервер корректно возвращает ошибку (status=false, code=400).")
       public void checkCreateProjectFailsWithoutTitle() {
        Response response = projectAPI.createProjectWithValidation(ProjectRequestFactory.projectWithEmptyTitle());
        // Проверяем, что API корректно обрабатывает ошибку
        assertThat(response.getStatusCode())//возвращаем респонс так как упадет в 400 и не сможем десериализовать по схеме
                .isEqualTo(400);
        // Детальная проверка с использованием JsonPath
        JsonPath jsonPath = response.jsonPath();

        // Проверка основного сообщения
        assertThat(jsonPath.getBoolean("status"))
                .as("Статус должен быть false")
                .isFalse();
    }
}

