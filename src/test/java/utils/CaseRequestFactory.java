package utils;

import com.github.javafaker.Faker;
import dto.TestCase;
import models.create.CreateCaseRq;

import java.util.List;
import java.util.Random;

public class CaseRequestFactory {

    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    /**
     * Фабрика для API DTO
     */
    public static CreateCaseRq caseRq() {
        return CreateCaseRq.builder()
                .title(faker.book().title())
                .description(faker.lorem().sentence())
                .priority(2)
                .severity(1)
                .type(3)
                .steps(List.of(
                        CreateCaseRq.Step.builder()
                                .action("Открыть сайт")
                                .expectedResult("Сайт открыт")
                                .build(),
                        CreateCaseRq.Step.builder()
                                .action("Авторизоваться")
                                .expectedResult("Успешно")
                                .build()
                ))
                .build();
    }

    /**
     * Фабрика для UI DTO
     */
    public static TestCase ui() {
        return TestCase.builder()
                .title(faker.book().title())
                .description(faker.lorem().sentence())
                .status("Actual")
                .severity("Major")
                .priority("Medium")
                .type("Functional")
                .build();
    }
}

