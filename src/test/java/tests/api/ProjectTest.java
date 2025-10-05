package tests.api;

import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.project.create.CreateProjectRs;
import models.project.get.GetProjectRs;
import org.testng.annotations.Test;
import tests.ui.BaseTest;
import utils.ProjectRequestFactory;

import static adapters.ProjectAPI.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.testng.Assert.assertTrue;

public class ProjectTest extends BaseTest {

    @Test
    @Step("Создание и удаление проекта")
    public void checkCreateAndDeleteProject() {
        CreateProjectRs rs = createProject(ProjectRequestFactory.validProject());
        assertTrue(rs.status);
        String code = rs.getResult().getCode();
        deleteProject(code);
    }

    @Test(groups = "smoke")
    @Step("Создание проекта: проверка полей title, description, access")
    public void cheсkFieldCreateFormNewProject() {

        CreateProjectRs rs = createProject(ProjectRequestFactory.validProject());//передаем тело запроса, возвращает обьект
        String code = rs.getResult().getCode();

        GetProjectRs getRs = getProject(code);//получаем созданный проект по коду

        assertThat(getRs.getProjects().get(0).getTitle())//в responce json приходит со списком обьектов , то есть мы сначало должны получить лист проектов и выбрать первый элемент
                .isEqualTo(ProjectRequestFactory.validProject().getTitle());
        assertThat(getRs.getProjects().get(0).description).isEqualTo(ProjectRequestFactory.validProject().getDescription());
        assertThat(getRs.getProjects().get(0).isPrivate).isEqualTo(ProjectRequestFactory.validProject().getAccess());
        deleteProject(code);
    }

    @Test
    @Step("Проверка негативного сценария: создание проекта без обязательного поля Title")
    public void checkCreateProjectFailsWithoutTitle() {
        Response response = createProjectWithValidation(ProjectRequestFactory.projectWithEmptyTitle());
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

