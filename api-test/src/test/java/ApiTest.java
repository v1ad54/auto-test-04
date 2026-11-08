import assertion.BasicApiAssert;
import assertion.GoodsApiAssert;
import endpoint.GoodsApi;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class ApiTest {

    @Test
    void SimpleTest() {
        Response response = GoodsApi.addGoods("Macbook 'Кабачек' " + new Random().nextInt(100, 999), 1.0f);
        response.prettyPrint();
        BasicApiAssert.assertThet(response)
                .statusCodeIsEqual(200);

        GoodsApi.getList().prettyPrint();
    }
}
