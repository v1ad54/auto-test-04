package endpoint;

import dto.GoodsDto;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class GoodsApi {

    /**
     * Метод выполняет запрос /list и возвращает ответ
     * Возвращает первые 1000000 товаров
     * @return
     */
    public static Response getList() {
        return RestApiBuilder.getBaseRS()
                .when()
                    .queryParam("page", 0)
                    .queryParam("size", 1000000)
                    .get(Urls.LIST)
                .then()
                    .log().all()
                .extract()
                    .response();
    }

    /**
     * Метод выполняет запрос /list и возвращает ответ
     * @return
     */
    public static Response getList(int page, int size) {
        return RestApiBuilder.getBaseRS()
                .when()
                .queryParam("page", page)
                .queryParam("size", size)
                .get(Urls.LIST)
                .then()
                .log().all()
                .extract()
                .response();
    }


    /**
     * Метод создает товар - выполняет запрос /add и возвращает ответ
     * @param name  - наименование
     * @param price - цена
     * @return
     */
    public static Response addGoods(String name, double price) {
        return RestApiBuilder.getBaseRS()
                .when()
                    .contentType(ContentType.JSON)
                    .body(new GoodsDto(name, price))
                    .post(Urls.ADD)
                .then()
                    .log().all()
                .extract()
                    .response();
    }

    /**
     * Метод создает товар - выполняет запрос /add и возвращает ответ
     * @param goods - DTO структура товара
     * @return
     */
    public static Response addGoods(GoodsDto goods) {
        return RestApiBuilder.getBaseRS()
                .when()
                    .contentType(ContentType.JSON)
                    .body(goods)
                    .post(Urls.ADD)
                .then()
                    .log().all()
                .extract()
                    .response();
    }

    /**
     * Метод создает товар - выполняет запрос /add и возвращает ответ
     * Ручное создание контекста
     * @param name  - наименование
     * @param price - цена
     * @return
     */
    public static Response addGoodsManual(String name, double price) {
        return RestApiBuilder.getBaseRS()
                .when()
                    .contentType("application/json")
//                    .body("{\"name\": \"%s\", \"price\": %.2f}".formatted(name, price))
                    .body(String.format("{\"name\": \"%s\", \"price\": %.2f}",name, price))
                    .post(Urls.ADD)
                .then()
                    .log().all()
                .extract()
                    .response();
    }

    /**
     * Метод удаляет товар с id - - выполняет DELETE запрос и возвращает ответ
     * @param id
     * @return
     */
    public static Response deleteGoods(int id){
        return RestApiBuilder.getBaseRS()
                .when()
                    .pathParam("id", id)
                    .delete(Urls.DELETE)
                .then()
                    .log().all()
                .extract()
                    .response();
    }

    /**
     * Метод запрашивает товар с id и возвращает ответ
     * @param id
     * @return
     */
    public static Response getGoods(int id) {
        return RestApiBuilder.getBaseRS()
                .when()
                    .pathParam("id", id)
                    .get(Urls.GET)
                .then()
                    .log().all()
                .extract()
                    .response();
    }

    public static Response patchGoods(int id, GoodsDto goods) {
        return RestApiBuilder.getBaseRS()
                .when()
                    .pathParam("id", id)
                    .contentType(ContentType.JSON)
                    .body(goods)
                    .patch(Urls.PATCH)
                .then()
                    .log().all()
                .extract()
                    .response();
    }

}
