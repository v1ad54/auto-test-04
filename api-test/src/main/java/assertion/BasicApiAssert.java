package assertion;

import io.restassured.response.Response;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.util.Collection;
import java.util.List;

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
    public BasicApiAssert listSizeIsEqual(String path, int size) {
        isNotNull(); // Проверяем, что сам Response не null

        // Получим список по указанному пути
        List<Object> actualList = actual.jsonPath().getList(path);

        // Проверяем, что список существует и его размер соответствует ожидаемому
        Assertions.assertThat(actualList)
                .as("List for path '%s' must be exist.", path)
                .isNotNull()
                .as("List for path '%s' must be size %d elements. Actual size: %d",
                        path, size, actualList != null ? actualList.size() : 0)
                .hasSize(size); // Проверка размера

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

    /**
     * Метод проверяет существование элемента по переданному JsonPath-пути.
     * <p>
     * Примеры путей для RestAssured (GPath):
     * - "goods.find { it.id == 34 }" (поиск элемента массива по id)
     * - "goods[0].name" (конкретное поле)
     * </p>
     * @param path - JsonPath/GPath выражение
     * @return - this, для возможности каскадного вызова проверок
     */
    public BasicApiAssert isPathExists(String path) {
        isNotNull(); // Проверяем, что сам Response не null

        // Получаем объект по заданному пути
        Object actualValue = actual.jsonPath().get(path);

        // Проверка на отсутствие элемента по указанному пути
        Assertions.assertThat(actualValue)
                .as("Path '%s' must be exist", path)
                .isNotNull();

        // Проверка на пустой элемент
        if (actualValue instanceof Collection) {
            Assertions.assertThat((Collection<?>) actualValue)
                    .as("Element at path '%s' is empty", path)
                    .isNotEmpty();
        }

        return this;
    }

    /**
     * Метод проверяет, что элемент по переданному JsonPath отсутствует в теле ответа.
     * <p>
     * Примеры использования:
     * - "goods.find { it.id == 999 }" (проверить, что товара с id 999 нет)
     * - "errors" (проверить, что в ответе нет блока с ошибками)
     * </p>
     * @param path - JsonPath/GPath выражение
     * @return - this, для возможности каскадного вызова проверок
     */
    public BasicApiAssert pathNotExists(String path) {
        isNotNull(); // Проверяем, что сам Response не null

        // Получаем объект по заданному пути
        Object actualValue = actual.jsonPath().get(path);

        // Проверяем на null или на пустую коллекцию
        if (actualValue != null) {
            if (actualValue instanceof Collection) {
                Assertions.assertThat((Collection<?>) actualValue)
                        .as("Expected path '%s' to be missing or empty, but a collection of %d elements was found",
                                path, ((Collection<?>) actualValue).size())
                        .isEmpty();
            } else {
                failWithMessage("The element at path '%s' was expected to be missing, but it was found and is equal to: <%s>",
                        path, actualValue);
            }
        }

        return this;
    }

}
