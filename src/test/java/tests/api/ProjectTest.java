package tests.api;

import com.github.javafaker.Faker;
import models.project.get.GetProjectErrorResponseDto;
import models.project.create.CreateProjectRequestDto;
import models.project.create.CreateProjectResponseErrorDto;
import org.testng.annotations.DataProvider;
import tests.BaseTest;
import io.qameta.allure.*;
import models.project.get.GetProjectResponseDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.factories.api.ProjectRequestFactory;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Epic("API Tests")
public class ProjectTest extends BaseTest {
    @BeforeMethod
    public void cleanUp(){projectAPI.deleteAllProject();}

    @Test(description = "Создание проекта" )
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка успешного создания проекта и возможности получить его по коду.")
    public void shouldCreateProjectAndBeAvailableByCode() {
        String createdProjectCode = projectAPI.createProject();

        GetProjectResponseDto fetchedProject =
                projectAPI.getProjectByCode(createdProjectCode);

        assertThat(fetchedProject.getResult().getCode())
                .as("Код проекта, полученного по GET, должен совпадать с кодом созданного проекта")
                .isEqualTo(createdProjectCode);
    }
    @Test( description = "Удаление проекта"   )
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка успешного удаления проекта и его недоступности после удаления.")
    public void shouldDeleteProjectAndMakeItUnavailable() {
        String createdProjectCode = projectAPI.createProject();

        projectAPI.deleteProject(createdProjectCode);

        GetProjectErrorResponseDto errorResponse =
                projectAPI.getProjectByCodeExpectError(createdProjectCode);

        assertThat(errorResponse.getStatus())
                .as("После удаления проект должен быть недоступен")
                .isFalse();
    }
    @Test(groups = "smoke",  description = "Создание проекта: проверка полей title и code")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка корректности данных, возвращаемых при создании проекта")
    public void  shouldCreateProjectAndReturnCorrectTitleAndCode() {
        CreateProjectRequestDto request = ProjectRequestFactory.valid();

        String createdProjectCode = projectAPI.createProjectAndReturnCode(request);

            GetProjectResponseDto fetchedProject = projectAPI.getProjectByCode(createdProjectCode);
            assertThat(fetchedProject.getResult().getTitle())
                    .as("Title проекта должен совпадать с отправленным при создании")
                    .isEqualTo(request.getTitle());
            assertThat(fetchedProject.getResult().getCode())
                    .as("Code проекта должен совпадать с code созданного проекта")
                    .isEqualTo(createdProjectCode);
    }
    @DataProvider(name = "invalidProjectRequestsWithoutTitle")
    public Object[][] invalidProjectRequestsWithoutTitle() {
        return new Object[][]{
                {ProjectRequestFactory.validWithTitle("")},
                {ProjectRequestFactory.validWithTitle(null)}
        };
    }
    @Test(  description = "Создание проекта без Title должно вернуть ошибку 400",
            dataProvider = "invalidProjectRequestsWithoutTitle"
    )
    @Description("Проверка валидации: при создании проекта без обязательного поля Title сервер возвращает ошибку.")
    public void shouldFailToCreateProjectWithoutTitle(CreateProjectRequestDto request) {
        CreateProjectResponseErrorDto errorResponse =
                projectAPI.createProjectExpectErrorDto(request);

        assertThat(errorResponse.errorMessage)
                .as("При невалидных данных должна возвращаться общая ошибка")
                .isEqualTo("Data is invalid.");

        assertThat(errorResponse.getErrorFields().get(0).getError())
                .as("Должна вернуться ошибка о том, что Title обязателен")
                .isEqualTo("Title is required.");
    }

    @Test(description = "Создание проекта с уже существующим code должно вернуть ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка уникальности project code: повторное создание проекта с тем же code запрещено.")
    public void shouldFailToCreateProjectWithDuplicateCode() {
        Faker faker = new Faker();
        String existingProjectCode = faker.letterify("????").toUpperCase();

        projectAPI.createProjectAndReturnCode(ProjectRequestFactory.validWithCode(existingProjectCode));

        CreateProjectResponseErrorDto errorResponse = projectAPI.createProjectExpectErrorDto
                (ProjectRequestFactory.validWithCode(existingProjectCode));

            assertThat(errorResponse.getStatus())
                    .as("Повторное создание проекта с уже существующим code должно завершаться ошибкой")
                    .isFalse();
        }
    }


