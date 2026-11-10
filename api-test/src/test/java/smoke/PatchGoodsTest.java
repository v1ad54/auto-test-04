package smoke;

import assertion.BasicApiAssert;
import dto.GoodsDto;
import endpoint.GoodsApi;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.Random;

@Tag("TestApi")
@DisplayName("PATCH /goods/{id}")
public class PatchGoodsTest {

    GoodsDto goods;    // Товар для проверки
    Integer id;     // Id проверяемого товара

    /**
     * Метод тестирования PATCH /goods/{id}
     */
    @Test
    @DisplayName("200")
    void patch200Test(){
        // Меняем наименование товара и его цену
        goods = new GoodsDto("Товар " + new Random().nextInt(1000, 10000),
                new Random().nextInt(0, 100));
        Response response = GoodsApi.patchGoods(id, goods);

        // Проверяем статус запроса
        BasicApiAssert.assertThet(response)
                .statusCodeIsEqual(200);

        // Получим товар с помощью метода GET goods/{id} и проверим его наименование и цену
        response = GoodsApi.getGoods(id);
        BasicApiAssert.assertThet(response)
                .statusCodeIsEqual(200)
                .bodyFieldExists("name")
                .bodyFieldEquals("name", goods.name())
                .bodyFieldExists("price")
                .bodyFieldEquals("price", String.valueOf(goods.price()));
    }

    /**
     * Метод тестирования PATCH /goods/{id}, когда мы обновляем товар на уже существующее наименование
     */
    @Test
    @DisplayName("200")
    void patch400GoodsExistsTest(){
        // Товар с наименованием которое мы попробуем присвоить проверяемому товару
        GoodsDto newGoods = new GoodsDto("Goods " + new Random().nextInt(1000, 10000),
                0);
        Response response = GoodsApi.addGoods(newGoods);
        int idNewGoods = response.jsonPath().getInt("data.id");

        // Меняем наименование товара и его цену yf
        response = GoodsApi.patchGoods(id, newGoods);

        // Проверяем статус запроса
        BasicApiAssert.assertThet(response)
                .statusCodeIsEqual(400);

        // Получим товар с помощью метода GET goods/{id} и проверим что на нем не изменились наименование и цена
        response = GoodsApi.getGoods(id);
        BasicApiAssert.assertThet(response)
                .statusCodeIsEqual(200)
                .bodyFieldExists("name")
                .bodyFieldEquals("name", goods.name())
                .bodyFieldExists("price")
                .bodyFieldEquals("price", String.valueOf(goods.price()));

        // Уберем за собой
        GoodsApi.deleteGoods(idNewGoods);
    }


    /**
     * Метод тестирования PATCH /goods/{id} с пустым именем
     */
    @Test
    @DisplayName("400 EmptyName")
    void patchEmptyNameTest(){
        // Меняем наименование товара и его цену
        GoodsDto newGoods = new GoodsDto(null,
                new Random().nextInt(0, 100));
        Response response = GoodsApi.patchGoods(id, newGoods);

        // Проверяем статус запроса
        BasicApiAssert.assertThet(response)
                .statusCodeIsEqual(400)
                .bodyFieldExists("name")
                .bodyFieldEquals("name", "Name cannot be empty");

        // Получим товар с помощью метода GET goods/{id} и проверим что его наименование и цена остались прежними
        response = GoodsApi.getGoods(id);
        BasicApiAssert.assertThet(response)
                .statusCodeIsEqual(200)
                .bodyFieldExists("name")
                .bodyFieldEquals("name", goods.name())
                .bodyFieldExists("price")
                .bodyFieldEquals("price", String.valueOf(goods.price()));
    }

    /**
     * Метод тестирования PATCH /goods/{id} с нулевой суммой
     */
    @Test
    @DisplayName("200 ZeroPrice")
    void patchZeroPriceTest(){
        // Меняем наименование товара и его цену
        GoodsDto newGoods = new GoodsDto("Goods " + new Random().nextInt(1000, 10000),
                0);
        Response response = GoodsApi.patchGoods(id, newGoods);

        // Проверяем статус запроса
        BasicApiAssert.assertThet(response)
                .statusCodeIsEqual(200)
                .bodyFieldExists("name")
                .bodyFieldEquals("name", newGoods.name())
                .bodyFieldExists("price")
                .bodyFieldEquals("price", String.valueOf(newGoods.price()));

        // Получим товар с помощью метода GET goods/{id} и проверим что его наименование и цена действительно изменились
        response = GoodsApi.getGoods(id);
        BasicApiAssert.assertThet(response)
                .statusCodeIsEqual(200)
                .bodyFieldExists("name")
                .bodyFieldEquals("name", newGoods.name())
                .bodyFieldExists("price")
                .bodyFieldEquals("price", String.valueOf(newGoods.price()));
    }

    /**
     * Метод тестирования PATCH /goods/{id} с отрицательной суммой
     */
    @Test
    @DisplayName("200 NegativePrice")
    void patchNegativePriceTest(){
        // Меняем наименование товара и его цену
        GoodsDto newGoods = new GoodsDto("Goods " + new Random().nextInt(1000, 10000),
                new Random().nextInt(-100, 0));
        Response response = GoodsApi.patchGoods(id, newGoods);

        // Проверяем статус запроса
        BasicApiAssert.assertThet(response)
                .statusCodeIsEqual(400)
                .bodyFieldExists("price")
                .bodyFieldEquals("price", "Price can not be less, than 0");

        // Получим товар с помощью метода GET goods/{id} и проверим что его наименование и цена не изменились
        response = GoodsApi.getGoods(id);
        BasicApiAssert.assertThet(response)
                .statusCodeIsEqual(200)
                .bodyFieldExists("name")
                .bodyFieldEquals("name", goods.name())
                .bodyFieldExists("price")
                .bodyFieldEquals("price", String.valueOf(goods.price()));
    }

    @Test
    @DisplayName("404")
    void patch404Test(){
        // Меняем наименование товара и его цену
        goods = new GoodsDto("Товар " + new Random().nextInt(1000, 10000),
                new Random().nextInt(0, 100));
        // Обновляем товар с заведомо не существующим id
        Response response = GoodsApi.patchGoods(new Random().nextInt(-100, 0), goods);

        // Проверяем статус запроса
        BasicApiAssert.assertThet(response)
                .statusCodeIsEqual(404);
    }


    /**
     * Подготовка тестовых данных
     */
    @BeforeEach
    void createTestData(){
        // Создаем товар для проведения теста
        goods = new GoodsDto(
                "Goods " + new Random().nextInt(1000, 10000),
                new Random().nextInt(0, 100) // Что бы не бороться с округлениями, упростим себе жизнь - используем целые
        );
        Response response = GoodsApi.addGoods(goods);
        // Получаем его id
        id = response.jsonPath().getInt("data.id");
    }

    /**
     * Удаление тестовых данных, после окончания теста
     */
    @AfterEach
    void deleteTestData() {
        if (id != null) {
            GoodsApi.deleteGoods(id);
        }
    }

}
