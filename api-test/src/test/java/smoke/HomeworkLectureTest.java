package smoke;

import dto.Goods;
import endpoint.Urls;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.notNullValue;


/**
 * Домашнее задание по лекции
 *
 * Написать 4 метода с автотестами, которые будут делать запрос к эндпоинту GET /goods/list учебного API:
 *
 * 1. Метод с использованием инструкций given(), when(), then() — проверить:
 * - код ответа;
 * - что тело ответа пустое;
 *
 * 2. Метод с использованием RequestSpecification — проверить:
 * - код ответа;
 * - что тело ответа пустое;
 *
 * 3. Метод, который будет создавать один товар через эндпоинт POST /goods/add и проверять,
 *    что GET /goods/list вернул его в списке через встроенные проверки REST Assured.
 *
 * 4. Метод, который будет создавать один товар через эндпоинт POST /goods/add и проверять,
 *    что GET /goods/list вернул его в списке через AssertJ.
 */
public class HomeworkLectureTest {

    /**
     * 1. Метод с использованием инструкций given(), when(), then() — проверить:
     *  - код ответа;
     *  - что тело ответа пустое;
     */
    @Test
    public void givenWhenThenTest() {

        Response response = (Response) given()
                .baseUri("http://localhost:8080")
                .basePath("/goods")
                .queryParam("page", 0)
                .queryParam("size", 1000)
                .log().all()
        .when()
                .get(Urls.LIST)
        .then()
                .log().all()
                .statusCode(200)
                .body(emptyString());
    }

    private RequestSpecification getRS() {
        return given()
                .baseUri("http://localhost:8080")
                .basePath("/goods")
                .queryParam("page", 0)
                .queryParam("size", 100000)
                .auth().basic("admin", "secret123")
                .log().all();
    }

    /**
     *  2. Метод с использованием RequestSpecification — проверить:
     *   - код ответа;
     *   - что тело ответа пустое;
     */
    @Test
    public void requestSpecificationTest() {

        RequestSpecification rs = getRS();

        given()
                .spec(rs)
        .when()
                .get(Urls.LIST)
        .then()
                .log().all()
                .statusCode(200)
                .body(emptyString());
    }

    /**
     *  3. Метод, который будет создавать один товар через эндпоинт POST /goods/add и проверять,
     *     что GET /goods/list вернул его в списке через встроенные проверки REST Assured.
     */
    @Test
    public void addAndListGoodsRestAssuredTest() {
        // Создаем спецификацию подключения к сервису
        RequestSpecification rs = getRS();

        // Создаем товар (POST /add) и запоминаем его id
        int id = given()
                .spec(rs)
        .when()
                .contentType(ContentType.JSON)
                .body(new Goods(
                            "Goods " + new Random().nextInt(1000, 10000),
                            new Random().nextInt(0, 100)
                        ))
                .post(Urls.ADD)
        .then()
                .log().all()
                .statusCode(200)
        .extract()
                .path("data.id");

        // Запрашиваем список товаров (GET /list) и проверяем что в нем есть товар с нужным id
        given()
                .spec(rs)
        .when()
                .get(Urls.LIST)
        .then()
                .log().all()
                .statusCode(200)
                .body(String.format("goods.find { it.id == %d }", id), notNullValue());
    }


    /**
     *  4. Метод, который будет создавать один товар через эндпоинт POST /goods/add и проверять,
     *     что GET /goods/list вернул его в списке через AssertJ.
     */
    @Test
    public void addAndListGoodsAssertJTest() {
        // Создаем спецификацию подключения к сервису
        RequestSpecification rs = getRS();

        // Создаем товар (POST /add) и запоминаем его id
        Integer id = given()
                .spec(rs)
                .when()
                .contentType(ContentType.JSON)
                .body(new Goods(
                        "Goods " + new Random().nextInt(1000, 10000),
                        new Random().nextInt(0, 100)
                ))
                .post(Urls.ADD)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .path("data.id");

        // Запрашиваем список товаров (GET /list)
        Response response = given()
                .spec(rs)
                .when()
                .get(Urls.LIST)
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        // Проверка того что в теле ответа, в массиве goods есть элемент содержащий "id" равный id
        Assertions.assertThat(response.jsonPath().getList("goods.id", Integer.class))
                .as("Список должен содержать товар с id = '%d'", id)
                .contains(id);

    }

}
