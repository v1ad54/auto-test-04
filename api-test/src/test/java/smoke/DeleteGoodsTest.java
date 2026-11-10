package smoke;

import assertion.BasicApiAssert;
import dto.GoodsDto;
import endpoint.GoodsApi;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.util.Random;

@Tag("TestApi")
@DisplayName("DELETE /goods/{id}")
public class DeleteGoodsTest {

    GoodsDto goods;    // Товар для проверки
    Integer id;     // Id проверяемого товара

    /**
     * Тест проверяет что DELETE /goods/{id} правильно удаляет ранее созданный товар
     */
    @Test
    @DisplayName("200")
    void deleteGoods200Test() {
        if (id != null) {
            // Проверим что DELETE возвращает код 200 при правильном id
            Response response = GoodsApi.deleteGoods(id);
            BasicApiAssert.assertThet(response)
                    .statusCodeIsEqual(200);

            // Убедимся что товара с id не осталось
            response = GoodsApi.getList();
            BasicApiAssert.assertThet(response)
                    .pathNotExists("goods.find { it.id == %d }".formatted(id));

            // Что бы лишний раз не вызывать DELETE занулим id
            id = null;
        }
        else {
            Assertions.fail("Не удалось создать товар для выполнения проверки.");
        }
    }

    /**
     * Тест проверяет что DELETE /goods/{id} возвращает 404 если id не существует
     */
    @Test
    @DisplayName("404")
    void deleteGoods404Test() {
        Response response = GoodsApi.deleteGoods(new Random().nextInt(-100, 0));

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
