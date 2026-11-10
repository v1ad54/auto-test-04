package smoke;

import assertion.BasicApiAssert;
import dto.GoodsDto;
import endpoint.GoodsApi;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Tag;

import java.util.List;
import java.util.Random;

@Tag("TestApi")
@DisplayName("GET /goods/list")
public class GetListGoods {

    /**
     * Метод удаляет из базы все товары
     */
    private void deleteAllGoods() {
        // Получим список товаров
        Response response = GoodsApi.getList();
        List<Integer> listId = response.path("goods.id");

        // Удалим все товары
        if (listId != null) {
            for (Integer it : listId) {
                response = GoodsApi.deleteGoods(it);
            }
        }
    }

    /**
     * Метод генерирует товары в базе
     *
     * @param num - количество товаров
     */
    private void generateGoods(int num) {
        Response response;

        for (int i = 0; i < num; i++) {
            response = GoodsApi.addGoods(new GoodsDto(
                    "Товар " + new Random().nextInt(1000, 10000),
                    new Random().nextInt(0, 100)
            ));
        }
    }

    /**
     * Метод генерирует n записей и проверяет что запрос /goods/list вернет n записей
     * Bug: Но увы, он больше 20 не возвращает. Код возврата 500.
     *
     * Т.к. тест использует удаление товара через метод DELETE /goods/{id},
     * а в нем тоже есть бага с удалением записей с id 50 - 53, то безопасным является
     * создание до 16 товаров (плюс 4 которые не удаляются).
     */
    @RepeatedTest(16)
    @DisplayName("200 All")
    void getList200AllTest(RepetitionInfo repetitionInfo) {
        final int cntGoods = repetitionInfo.getCurrentRepetition();
        Response response;

        // Удалим все товары из базы
        deleteAllGoods();
        // Сгенерируем n товаров
        generateGoods(cntGoods);

        response = GoodsApi.getList(0, cntGoods);
        BasicApiAssert.assertThet(response)
                .statusCodeIsEqual(200)
                .listSizeIsEqual("goods", cntGoods);

        // Уберем за собой - удалим все товары из базы
        deleteAllGoods();
    }

}
