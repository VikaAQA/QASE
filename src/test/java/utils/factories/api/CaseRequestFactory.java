package utils.factories.api;

import com.github.javafaker.Faker;
import models.testcase.update.UpdateCaseRequestDto;

import java.util.List;

public final class CaseRequestFactory {

    private static final Faker faker = new Faker();

    /**
     * Фабрика для API DTO
     */
    public static models.create.CreateCaseRequestDto valid() {
        return models.create.CreateCaseRequestDto.builder()
                .title(faker.bothify("AUTOTEST-#####"))
                .description(faker.lorem().sentence())
                .priority(2)
                .severity(1)
                .type(3)
                .steps(List.of(
                        models.create.CreateCaseRequestDto.Step.builder()
                                .action(faker.lorem().word())
                                .expectedResult(faker.lorem().sentence())
                                .build(),
                        models.create.CreateCaseRequestDto.Step.builder()
                                .action(faker.lorem().sentence())
                                .expectedResult(faker.lorem().sentence())
                                .build()
                ))
                .build();
    }

    public static models.create.CreateCaseRequestDto validWithType(int numType) {
        return models.create.CreateCaseRequestDto.builder()
                .title(faker.bothify("AUTOTEST-#####"))
                .description(faker.lorem().sentence())
                .priority(2)
                .severity(1)
                .type(numType)
                .steps(List.of(
                        models.create.CreateCaseRequestDto.Step.builder()
                                .action(faker.lorem().word())
                                .expectedResult(faker.lorem().sentence())
                                .build(),
                        models.create.CreateCaseRequestDto.Step.builder()
                                .action(faker.lorem().sentence())
                                .expectedResult(faker.lorem().sentence())
                                .build()
                ))
                .build();
    }

    public static UpdateCaseRequestDto updateTypeCase(int numTypeCase){
      return  UpdateCaseRequestDto.builder()
                .type(numTypeCase)
                .build();
    }
}

