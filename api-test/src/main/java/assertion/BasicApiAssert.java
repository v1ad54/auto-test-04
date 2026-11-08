package assertion;

import io.restassured.response.Response;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class BasicApiAssert extends AbstractAssert<BasicApiAssert, Response> {

    protected BasicApiAssert(Response actual) {
        super(actual, BasicApiAssert.class);
    }

    public static BasicApiAssert assertThet(Response actual) {
        return new BasicApiAssert(actual);
    }

    /**
     * Метод проверяет код ответа
     * @param code
     * @return
     */
    public BasicApiAssert statusCodeIsEqual(int code) {
        Assertions.assertThat(actual.statusCode())
                .as("Status code must be equals to %d".formatted(code))
                .isEqualTo(code);

        return this;
    }

    /**
     * Метод проверяет наличие поля в теле ответа
     * @param path
     * @return
     */
    public BasicApiAssert bodyFieldExists(String path) {
        Assertions.assertThat((actual.jsonPath().getString(path)))
                .as("Field '%s' must be not null".formatted(path))
                .isNotNull();

        return this;
    }

    public BasicApiAssert listIsEmpty(String path) {
        Assertions.assertThat(actual.jsonPath().getList(path))
                .as("List for path '%s' must be empty".formatted(path))
                .isEmpty();

        return this;
    }

    public BasicApiAssert bodyFieldEquals(String path, String value) {
        Assertions.assertThat(actual.jsonPath().getString(path))
                .as("Field by path %s must be equals '%s'".formatted(path, value))
                .isEqualToIgnoringCase(value);

        return this;
    }

    // Метод проверяет размер массива в теле ответа
    public BasicApiAssert listSizeIsEqual() {
        // todo: реализовать метод listSizeIsEqual

        return this;
    }

    // Метод проверяет наличие либо содержимое заголовка
    public BasicApiAssert headerIsEqual() {
        // todo: реализовать метод headerIsEqual

        return this;
    }

    // Метод проверяет значение поля в теле ответа
    public BasicApiAssert bodyFieldIsEqual() {
        // todo: реализовать метод bodyFieldIsEqual

        return this;
    }

    // Метод проверяет наличие элемента в массиве в теле ответа
    public BasicApiAssert listContains() {
        // todo: реализовать метод listContains

        return this;
    }

}
