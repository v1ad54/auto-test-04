import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

// url приложения: http://localhost:8080/
// url swagger: http://localhost:8080/swagger-ui/index.html

/**
 * Класс с материалами с лекции
 *
 */
public class LectureRestAssuredTest {

    /**
     * DTO для сериализации тела запроса
     *
     * @param foo
     * @param foo2
     */
    public record RequestDTO(
            String foo,
            Integer foo2
    ) {
    }

    /**
     * DTO для POST запроса добавления товара
     * @param name  - наименование товара
     * @param price - цена товара
     */
    public record PostRequestDTO(
            String name,
            Double price
    ) {
    }


    // Предварительно настроенный RequestSpecification
    RequestSpecification basicRQ = new RequestSpecBuilder()
            .setBaseUri("http://localhost:8080")
            .log(LogDetail.ALL)
            .addQueryParam("page", 0)
            .build();


    /**
     * Тест REST API
     * В комментариях дополнительные методы
     */
    @Test
    void raTest() {
        given()    // Стадия подготовки, тут мы описываем все, что будет отправляться на сервер
                .baseUri("http://localhost:8080")     // Базовый адрес сервера
//                .basePath("/v1")                   // Общая часть пути
                .header("Accert", "application/json")   // Установка заголовков
                .queryParam("page", 0)            // параметры строки запроса (после знака "?")
                .queryParam("size", 1000)         // параметры строки запроса (после знака "?")
                .log().all()        // Вывод запроса в консоль
                // Еще бывают методы:
                // .auth - аутентификация
                // .pathParam("id", 10)     // динамические части пути (в паре с {id} в URL)
                // .contentType(ContentType.JSON)
                // .body(myObject / "json string")  // тело запроса для POST/PUT методов

//                .body("""     // Ручное составление тела запроса
//                        {
//                            "foo": "bar",
//                            "foo2": 123
//                        }
//                        """)

//                .body(new RequestDTO("bar", 123))     // Сериализация тела запроса через DTO

                .when()
                .get("/goods/list")        // Чтение данных
//                .get("/goods/{id}")   // Чтение с указанием pathParam
//                .post("/goods")       // Создание нового ресурса
//                .put("/goods/1")      // полное обновление ресурса
//                .patch("/goods/1")    // частичное обновление
//                .delete("/goods/1")   // удаление

                .then()         // Проверки ответа
                .log().all()                           // Логируем всегда
//                .log().ifValidationFails()                           // Логируем только если тест упал
                .statusCode(200)                    // Проверка кода
                .contentType(ContentType.JSON)                       // Проверка типа контента
//                .body("data.id", equalTo(10))           // Проверка вложенного поля
//                .body("data.first_name", is(notNullValue()))    // Проверка, что поле не пустое

        ;

    }

    /**
     * Тест с RequestSpecification созданный внутри теста
     */
    @Test
    void rsTest() {
        RequestSpecification reqSpec = given()
                .baseUri("http://localhost:8080")     // Базовый адрес сервера
//                .basePath("/v1")                   // Общая часть пути
                .header("Authorization", "Bearer token123")   // Установка заголовков
                .contentType(ContentType.JSON)                       // Проверка типа контента
                .log().all()        // Вывод запроса в консоль
                ;

        // Можно делать так
        reqSpec.get("/goods/list")
                .then()
                .log().all()
        ;

        // А можно (и лучше) сделать копию RequestSpecification через given и spec
        given()
                .spec(reqSpec)
                .get("/goods/list")
                .then()
                .log().all()
        ;

    }

    /**
     * Тест 1 с использованием RequestSpecification через метод spec
     */
    @Test
    void recSpecTest1() {
        given()
                .spec(basicRQ)
                .queryParam("size", 1)
                // запрос
                .when()
                .get("/goods/list")
                // Проверки
                .then()
                .log().all()
                .statusCode(200)                    // Точное совпадение

                // Примеры дополнительных возможных проверок
                .statusCode(anyOf(is(200), is(201)))     // Проверка на несколько допустимых кодов
                .contentType(ContentType.JSON)                       // Проверка через встроенный перечислитель (Enum)
//                    .contentType("application/json; charset=utf-8")      // Строгая проверка строки
//                    .header("Server", "cloudflare") // Проверка конкретного заголовка
//                .body("id", equalTo(10))        // Простые поля
//                .body("data.first_name", is("Janet"))   // Вложенные объекты
//                .body("data[0].email", containsString("@"))     // Элементы массива
//                .body("data.id", hasItems(7, 8, 9))
        ;
    }

    /**
     * Тест 2 с использованием RequestSpecification через метод spec
     */
    @Test
    void recSpecTest2() {
        given()
                .spec(basicRQ)
                .queryParam("size", 0)
                .get("/goods/list")
                .then()
                .log().all()
                .statusCode(200)
        ;
    }


    /**
     * Простой тест через AssertJ
     */
    @Test
    void assertJTest() {
        Response response = given()
                .spec(basicRQ)
                .queryParam("size", 1)
                .get("/goods/list");

        Assertions.assertThat(response.statusCode())
                .as("Код ответа должен быть 200")
                .isEqualTo(200);

        Assertions.assertThat(response.statusCode())
                .as("Код ответа должен быть 200 или 201")
                .isBetween(200, 201);

        Assertions.assertThat(response.contentType())
                .as("Content-type должен быть application/json")
                .isEqualTo("application/json");

        Assertions.assertThat(response.contentType())
                .as("Content-type должен быть application/json")
                .isIn((Object[]) ContentType.JSON.getContentTypeStrings());

        Assertions.assertThat(response.header("Server"))
                .as("Заголовок 'Server' должен быть равен 'CloudFire'")
                .isEqualTo("CloudFire");

        Assertions.assertThat(response.jsonPath().getInt("id"))
                .as("Поле id должно быть равно 10")
                .isEqualTo(10);

        Assertions.assertThat(response.jsonPath().getString("data[0].email"))
                .as("Поле data[0].email должно содержать '@'")
                .contains("@");

        Assertions.assertThat(response.jsonPath().getList("data.id", Integer.class))
                .as("Список data.id должен содержать 7, 8, 9")
                .contains(7, 8, 9);

        Assertions.assertThat(response.jsonPath().getString("data.first_name"))
                .as("Поле data.first_name должно быть равно 'Janet'")
                .contains("Janet");

    }

    /**
     * Тесть добавления товара с авторизацией
     */
    @Test
    void authTest() {
        given()
                .spec(basicRQ)
//                .queryParam("size", 1)
                .contentType(ContentType.JSON)
                .body(new PostRequestDTO("Товар 1", 123.45))
                .auth()
                .basic("admin", "secret123")
                .post("/goods/add")
                .then()
                .log().all()
                .statusCode(200)
        ;
    }

}
