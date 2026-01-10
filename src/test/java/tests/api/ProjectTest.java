package tests.api;

import com.github.javafaker.Faker;
import models.project.create.CreateProjectRequestDto;
import models.project.create.CreateProjectResponseErrorDto;
import org.testng.annotations.DataProvider;
import tests.BaseTest;
import io.qameta.allure.*;
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

    @Test(description = "Создание нового проекта")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка успешного создания проекта")
    public void checkCreateProject() {
        CreateProjectRequestDto rq = ProjectRequestFactory.valid();

        String code = projectAPI.createProjectAndReturnCode(rq);

        Response response = projectAPI.getProjectRaw(code);
        assertThat(response.jsonPath().getBoolean("status")).isTrue();
    }

    @Test(description = "Удаление проекта")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка успешного удаления проекта")
    public void checkDeleteProject() {
        CreateProjectRequestDto rq = ProjectRequestFactory.valid();
        String code = projectAPI.createProjectAndReturnCode(rq);

        projectAPI.deleteProject(code);

        Response after = projectAPI.getProjectRaw(code);
        assertThat(after.jsonPath().getBoolean("status")).isFalse();
    }

    @Test(groups = "smoke", description = "Создание проекта: проверка корректности полей title и code")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка корректности данных, возвращаемых при создании проекта")
    public void cheсkFieldCreateFormNewProject() {//getRs переименовать
        CreateProjectRequestDto rq = ProjectRequestFactory.valid();
        String code = projectAPI.createProjectAndReturnCode(rq);

        GetProjectResponseDto getRs = projectAPI.getProjectByCode(code);

        assertThat(getRs.getResult().getTitle()).isEqualTo(rq.getTitle());
        assertThat(getRs.getResult().getCode()).isEqualTo(code);

        projectAPI.deleteProject(code);
    }

    //добавить тест, когда title пустая строка,+
    //можно сделать 1 тест но с дата провайдером для валидации поля title+

    @DataProvider
    public Object[][]validationProject() {
        return new Object[][]{
                {ProjectRequestFactory.validWithTitle("")
                },
                {ProjectRequestFactory.validWithTitle(null)
                }
            };
    }
    @Test(description = "Создание проекта без обязательного поля Title должно вернуть ошибку 400",dataProvider ="validationProject" )
    @Description("Проверка, что при создании проекта без обязательного поля Title сервер корректно возвращает ошибку (status=false, code=400).")
       public void checkCreateProjectFailsWithoutTitle(CreateProjectRequestDto titleProject) {//две проверки на поля внутри errorFields через ДТО ошибки в ответе
        CreateProjectResponseErrorDto err = projectAPI.createProjectExpectErrorDto(titleProject);

        assertThat(err.errorMessage).isEqualTo("Data is invalid.");
        assertThat(err.getErrorFields().get(0).getError()).isEqualTo("Title is required.");
    }

    @Test(description = "Создание проекта с уже существующим code должно вернуть ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка уникальности project code: повторное создание проекта с тем же code запрещено.")
    public void checkCreateProjectFailsWithDuplicateCode() {
        Faker faker = new Faker();
        String duplicateCode = faker.letterify("????").toUpperCase();//переименовать duplicateCode чтобы было понятнее
        String createdCode = //createdCode - дать понятнее название
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
    //добавить тесты на обновление проекта
    //добавить тест: создали 20 тестов, из них 12 с тегом smoke.В апи тесте получаем все тесты, фильтруем их по тегу smoke и проверяем что их количество 12

}

