package smoke;

import assertion.BasicApiAssert;
import endpoint.GoodsApi;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

@DisplayName("/goods/add")
public class CreateGoodTest {

    String goodName = "Macbook 'Кабачек' " + new Random().nextInt(1000, 9999);
    Double price = new Random().nextDouble(0, 20);
    Integer id;

    @Test
    @DisplayName("200")
    void createTest() {
        Response response = GoodsApi.addGoods(goodName, price);
        id = response.jsonPath().getInt("data.id");
        BasicApiAssert.assertThet(response)
                .statusCodeIsEqual(200)
                .bodyFieldExists("message")
                .bodyFieldEquals("message", "success");
    }

    @Test
    @DisplayName("400")
    void createGoodWithEmptyNameTest() {
        Response response = GoodsApi.addGoods("", price);
        BasicApiAssert.assertThet(response)
                .statusCodeIsEqual(400);
    }

    @AfterEach
    void deleteTestData() {
        if (id != null) {
            GoodsApi.deleteGood(id);
        }
    }

}
