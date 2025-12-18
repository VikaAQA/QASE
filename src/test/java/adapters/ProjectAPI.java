package adapters;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import models.project.create.CreateProjectRq;
import models.project.create.CreateProjectRs;
import models.project.get.GetProjectRs;

import java.util.List;

@Log4j2
public class ProjectAPI extends BaseAPI {

    @Step("Создание проекта (POST /project)")
    public CreateProjectRs createProject(CreateProjectRq project) {
        Response response = post("project", project);
        return gson.fromJson(response.asString(), CreateProjectRs.class);
    }

    @Step("Создание проекта и возврат projectCode (POST /project)")
    public String createProjectAndReturnCode(CreateProjectRq project) {
        CreateProjectRs rs = createProject(project);
        String code = rs.getResult().getCode();
        log.info("Проект '{}' успешно создан, code = {}", project.getTitle(), code);
        return code;
    }

    @Step("Удаление проекта {code}")
    public void deleteProject(String code) {
        delete("project/" + code);
    }

    @Step("Получение проекта по коду {code}")
    public GetProjectRs getProject(String code) {
        Response response = get("project/" + code);
        return gson.fromJson(response.asString(), GetProjectRs.class);
    }

    @Step("Получение списка всех проектов")
    public List<String> getAllProject() {
        Response response = get("project?limit=10&offset=0");
        return json(response).getList("result.entities.code", String.class);
    }

    @Step("Удаление всех проектов")
    public void deleteAllProject() {
        getAllProject().forEach(this::deleteProject);
    }

    @Step("Создание проекта (валидация без десериализации)")
    public Response createProjectWithValidation(CreateProjectRq project) {
        log.info("Creating project with validation, body: {}", project);
        return post("project", project);
    }
}

