package smoke;

import assertion.BasicApiAssert;
import endpoint.GoodsApi;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Random;

@Tag("TestApi")
@DisplayName("GET /goods/add")
public class CreateGoodTest {

    String goodName = "Macbook 'Кабачек' " + new Random().nextInt(1000, 9999);
    Double price = new Random().nextDouble(0, 20);
    Integer id;

    /**
     * Успешное создание товара
     *
     */
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

    /**
     * Создание товара с пустым именем
     */
    @Test
    @DisplayName("400")
    void createGoodWithEmptyNameTest() {
        Response response = GoodsApi.addGoods("", price);
        BasicApiAssert.assertThet(response)
                .statusCodeIsEqual(400);
    }

    /**
     * Создание дубля товара
     */
    @Test
    @DisplayName("400")
    void dubleGoodsTest() {
        // Первый раз создаем товар
        Response response = GoodsApi.addGoods(goodName, price);
        id = response.jsonPath().getInt("data.id");

        // Создаем дубль товара
        response = GoodsApi.addGoods(goodName, price);

        BasicApiAssert.assertThet(response)
                .statusCodeIsEqual(400)
                .bodyFieldExists("message")
                .bodyFieldEquals("message", "Good with name '%s' already exists!".formatted(goodName));
    }

    /**
     * Удаление тестовых данных, после завершения теста
     */
    @AfterEach
    void deleteTestData() {
        if (id != null) {
            GoodsApi.deleteGoods(id);
        }
    }

}
