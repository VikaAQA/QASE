package utils.factories.api;

import com.github.javafaker.Faker;
import models.create.CreateCaseRq;

import java.util.List;
import java.util.Random;

public final class CaseRequestFactory {

    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    /**
     * Фабрика для API DTO
     */
    public static CreateCaseRq valid() {
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
}

