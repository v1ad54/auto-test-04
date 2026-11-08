package endpoint;

import dto.Goods;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class GoodsApi {

    public static Response getList() {
        return RestApiBuilder.getBaseRS()
                .get(Urls.LIST);
    }

    public static Response addGoods(String name, double price) {
        return RestApiBuilder.getBaseRS()
                .contentType(ContentType.JSON)
                .body(new Goods(name, price))
                .post(Urls.ADD)
                .then()
                .log().all().extract().response();
    }

    public static Response addGoodsManual(String name, double price) {
        return RestApiBuilder.getBaseRS()
                .contentType("application/json")
                .body("{\"name\": \"%s\", \"price\": %.2f}".formatted(name, price))
                .body(String.format("{\"name\": \"%s\", \"price\": %.2f}",name, price))
                .post(Urls.ADD)
                .then()
                .log().all().extract().response();
    }

    public static Response deleteGood(int id){
        return RestApiBuilder.getBaseRS()
                .pathParam("id", id)
                .delete(Urls.DELETE);
    }

}
