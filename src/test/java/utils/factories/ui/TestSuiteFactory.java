package utils.factories.ui;

import com.github.javafaker.Faker;
import dto.TestSuite;

public final class TestSuiteFactory {

    private static final Faker faker = new Faker();

    /**
     * Создаёт случайный валидный Suite для теста
     */
    public TestSuite random() {
        return TestSuite.builder()
                .suit_name(faker.lorem().word())
                .description(faker.lorem().sentence())
                .preconditions(faker.lorem().sentence())
                .build();
    }
}
