package tests.api;

import com.github.javafaker.Faker;
import models.project.create.CreateProjectRequestDto;
import tests.BaseTest;
import io.qameta.allure.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.project.get.GetProjectResponseDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.factories.api.ProjectRequestFactory;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Epic("API Tests")
public class ProjectTest extends BaseTest {
    @BeforeMethod
    public void cleanUp(){projectAPI.deleteAllProject();}

    @Test(description = "Создание и удаление проекта")
    @Severity(SeverityLevel.CRITICAL)
    @Description(" Проверка сценария создание и удаление нового проекта")
        public void checkCreateAndDeleteProject() {
        CreateProjectRequestDto rq = ProjectRequestFactory.valid();
        String code = projectAPI.createProjectAndReturnCode(rq);

        projectAPI.deleteProject(code);
        Response after = projectAPI.getProjectRaw(code);
        assertThat(after.jsonPath().getBoolean("status")).isFalse();
        }

    @Test(groups = "smoke", description = "Создание проекта: проверка корректности полей title и code")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка корректности данных, возвращаемых при создании проекта")
    public void cheсkFieldCreateFormNewProject() {
        CreateProjectRequestDto rq = ProjectRequestFactory.valid();
        String code = projectAPI.createProjectAndReturnCode(rq);

        GetProjectResponseDto getRs = projectAPI.getProjectByCode(code);

        assertThat(getRs.getResult().getTitle()).isEqualTo(rq.getTitle());
        assertThat(getRs.getResult().getCode()).isEqualTo(code);

        projectAPI.deleteProject(code);
    }

    @Test(description = "Создание проекта без обязательного поля Title должно вернуть ошибку 400")
    @Description("Проверка, что при создании проекта без обязательного поля Title сервер корректно возвращает ошибку (status=false, code=400).")
       public void checkCreateProjectFailsWithoutTitle() {
        Response response = projectAPI.createProjectRaw(ProjectRequestFactory.projectWithEmptyTitle());

        assertThat(response.statusCode()).isEqualTo(400);

        JsonPath jsonPath = response.jsonPath();
        assertThat(jsonPath.getBoolean("status")).isFalse();
    }

    @Test(description = "Создание проекта с уже существующим code должно вернуть ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка уникальности project code: повторное создание проекта с тем же code запрещено.")
    public void checkCreateProjectFailsWithDuplicateCode() {
        Faker faker = new Faker();
        String duplicateCode = faker.letterify("????").toUpperCase();
        String createdCode =
                projectAPI.createProjectAndReturnCode(
                        ProjectRequestFactory.validWithCode(duplicateCode)
                );

        Response response =
                projectAPI.createProjectRaw(
                        ProjectRequestFactory.validWithCode(duplicateCode)
                );

        assertThat(response.statusCode()).isIn(400, 409, 422);
        assertThat(response.jsonPath().getBoolean("status")).isFalse();
        projectAPI.deleteProject(createdCode);
    }
}

