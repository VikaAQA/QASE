package utils.factories.ui;

import com.github.javafaker.Faker;
import dto.TestCase;

public final class TestCaseFactory {
    private static final Faker faker = new Faker();
    /**
     * Фабрика для UI DTO
     * Создает валидный тест-кейс для UI форм
     */
    public static TestCase valid() {
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
