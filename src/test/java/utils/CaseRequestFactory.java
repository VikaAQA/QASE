package utils;

import models.create.CreateCaseRq;

import java.util.List;

public class CaseRequestFactory {
    public static CreateCaseRq caseRq() {
        return CreateCaseRq.builder()
                .title("Проверка регистрации")
                .description("Описание тест-кейса")
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
