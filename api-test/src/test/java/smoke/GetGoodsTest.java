package smoke;

import assertion.BasicApiAssert;
import dto.GoodsDto;
import endpoint.GoodsApi;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.util.Random;

@Tag("TestApi")
@DisplayName("GET /goods/{id}")
public class GetGoodsTest {

    GoodsDto goods;    // Товар для проверки
    Integer id;     // Id проверяемого товара

    /**
     * Тест проверяет что GET /goods/{id} правильно возвращает ранее созданный товар
     */
    @Test
    @DisplayName("200")
    void getGoods200Test() {

        if (id != null) {
            Response response = GoodsApi.getGoods(id);

            BasicApiAssert.assertThet(response)
                    .statusCodeIsEqual(200)
                    .bodyFieldExists("id")
                    .bodyFieldEquals("id", String.valueOf(id))
                    .bodyFieldExists("name")
                    .bodyFieldEquals("name", goods.name())
                    .bodyFieldExists("price")
                    .bodyFieldEquals("price", String.valueOf(goods.price()));
        }
        else {
            Assertions.fail("Не удалось создать товар для выполнения проверки.");
        }
    }

    /**
     * Тест проверяет что GET /goods/{id} возвращает 404 если id не существует
     */
    @Test
    @DisplayName("404")
    void getGoods404Test() {
        Response response = GoodsApi.getGoods(new Random().nextInt(-100, 0));

        BasicApiAssert.assertThet(response)
                .statusCodeIsEqual(404);
    }



    @BeforeEach
    void createTestData(){
        // Создаем товар для проверки метода GET
        goods = new GoodsDto(
                "Goods " + new Random().nextInt(1000, 10000),
                new Random().nextInt(0, 100) // Что бы не бороться с округлениями, упростим себе жизнь - используем целые
        );
        Response response = GoodsApi.addGoods(goods);
        // Получаем его id
        id = response.jsonPath().getInt("data.id");
    }

    @AfterEach
    void deleteTestData() {
        if (id != null) {
            GoodsApi.deleteGoods(id);
        }
    }
}
