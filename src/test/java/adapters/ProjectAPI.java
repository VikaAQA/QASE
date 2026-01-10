package adapters;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import models.project.create.CreateProjectRequestDto;
import models.project.create.CreateProjectResponseDto;
import models.project.create.CreateProjectResponseErrorDto;
import models.project.get.GetProjectResponseDto;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.testng.AssertJUnit.assertTrue;

@Log4j2
public class ProjectAPI extends BaseAPI {

    @Step("Создание проекта (POST /project)")
    public Response createProjectRaw(CreateProjectRequestDto dto) {
        return post("project", dto);
           }

    @Step("GET /project/{code} (raw)")
    public Response getProjectRaw(String code) {
        return get("project/" + code);
    }

    @Step("GET /project?limit={limit}&offset={offset} (raw)")
    public Response getProjectsRaw(int limit, int offset) {
        return get("project?limit=" + limit + "&offset=" + offset);
    }

    @Step("DELETE /project/{code} (raw)")
    public Response deleteProjectRaw(String code) {
        return delete("project/" + code);
    }

    @Step("Создание проекта и возврат projectCode (POST /project)")
    public String createProjectAndReturnCode(CreateProjectRequestDto project) {
        Response response = createProjectRaw(project);
        assertThat(response.statusCode()).isIn(200, 201);

        validateSchema(response, "schema/create_project_rs.json");
        CreateProjectResponseDto rs = gson.fromJson(response.asString(), CreateProjectResponseDto.class);;
        assertTrue(rs.status);
        String code = rs.getResult().getCode();
        log.info("Проект '{}' успешно создан, code = {}", project.getTitle(), code);
        return code;
    }

    @Step("Удаление проекта {code}")
    public void deleteProject(String code) {
        Response response = deleteProjectRaw(code);
        assertThat(response.statusCode())
                .as("Delete project HTTP status, code=%s, body: %s", code, response.asString())
                .isIn(200, 204);
    }

    @Step("Получение данных проекта по коду {code}")
    public GetProjectResponseDto getProjectByCode(String code) {
        Response response = getProjectRaw(code);

        assertThat(response.statusCode())
                .as("Get project HTTP status, code=%s, body: %s", code, response.asString())
                .isEqualTo(200);

        validateSchema(response, "schema/get_project_by_code_rs.json");

        return gson.fromJson(response.asString(), GetProjectResponseDto.class);
    }
    @Step("Создание проекта ожидаемо падает с кодом 400")
    public CreateProjectResponseErrorDto createProjectExpectErrorDto(CreateProjectRequestDto dto) {
        Response response = createProjectRaw(dto);

        assertThat(response.statusCode())
                .as("Create project error HTTP status, body=%s", response.asString())
                .isEqualTo(400);

        CreateProjectResponseErrorDto err = gson.fromJson(response.asString(), CreateProjectResponseErrorDto.class);

        assertThat(err.getStatus()).isFalse();
        assertThat(err.getErrorFields()).isNotNull();

        return err;
    }

    @Step("Получение списка всех проектов")
    public List<String> getAllProjectCodes() {
        Response response = getProjectsRaw(10, 0);

        assertThat(response.statusCode())
                .as("Get projects HTTP status, body: %s", response.asString())
                .isEqualTo(200);

        return json(response).getList("result.entities.code", String.class);
    }

    @Step("Удаление всех проектов")
    public void deleteAllProject() {
        getAllProjectCodes().forEach(this::deleteProject);
    }

}

