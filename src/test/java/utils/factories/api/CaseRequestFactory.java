package utils.factories.api;

import com.github.javafaker.Faker;

import java.util.List;
import java.util.Random;

public final class CaseRequestFactory {

    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    /**
     * Фабрика для API DTO
     */
    public static models.create.CreateCaseRequestDTO valid() {
        return models.create.CreateCaseRequestDTO.builder()
                .title(faker.book().title())
                .description(faker.lorem().sentence())
                .priority(2)
                .severity(1)
                .type(3)
                .steps(List.of(
                        models.create.CreateCaseRequestDTO.Step.builder()
                                .action(faker.lorem().word())
                                .expectedResult(faker.lorem().sentence())
                                .build(),
                        models.create.CreateCaseRequestDTO.Step.builder()
                                .action(faker.lorem().sentence())
                                .expectedResult(faker.lorem().sentence())
                                .build()
                ))
                .build();
    }
}

