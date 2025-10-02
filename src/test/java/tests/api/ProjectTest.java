package tests.api;

import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.project.create.CreateProjectRq;
import models.project.create.CreateProjectRs;
import models.project.get.GetProjectRs;
import org.testng.annotations.Test;
import utils.ProjectRequestFactory;

import static adapters.ProjectAPI.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.testng.Assert.assertTrue;

public class ProjectTest {

    @Test
    @Step("Создание и удаление проекта")
    public void checkCreateAndDeleteProject() {
        CreateProjectRs rs = createProject(ProjectRequestFactory.validProject());
        assertTrue(rs.status);//проверка статуса
        String code = rs.getResult().getCode();//получение из респонс значения
        deleteProject(code);
    }

    @Test
    @Step("Создание проекта: проверка полей title, description, access")
    public void cheсkFieldCreateFormNewProject() {
        CreateProjectRq rq = CreateProjectRq.builder()//собираем тело запроса
                .title("TMSAPI")
                .code("API")
                .description("System for description case")
                .group("all")
                .access("all")
                .build();
        CreateProjectRs rs = createProject(rq);//передаем тело запроса, возвращает обьект
        String code = rs.getResult().getCode();

        GetProjectRs getRs = getProject(code);//получаем созданный проект по коду

        assertThat(getRs.getProjects().get(0).getTitle())//в responce json приходит со списком обьектов , то есть мы сначало должны получить лист проектов и выбрать первый элемент
                .isEqualTo(rq.getTitle());
        assertThat(getRs.getProjects().get(0).description).isEqualTo(rq.getDescription());
        assertThat(getRs.getProjects().get(0).isPrivate).isEqualTo(rq.getAccess());
        deleteProject(code);
    }

    @Test
    @Step("Проверка негативного сценария: создание проекта без обязательного поля Title")
    public void checkCreateProjectFailsWithoutTitle() {
        CreateProjectRq rq = CreateProjectRq.builder()//собираем тело запроса
                .title("")
                .code("API")
                .description("System for description case")
                .group("all")
                .access("all")
                .build();
        Response response = createProjectWithValidation(rq);
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

