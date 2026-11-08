package assertion;

import io.restassured.response.Response;
import org.assertj.core.api.AbstractAssert;

public class GoodsApiAssert extends AbstractAssert<GoodsApiAssert, Response> {


    protected GoodsApiAssert(Response actial) {
        super(actial, GoodsApiAssert.class);
    }

    public GoodsApiAssert EmptyListIsOK() {
        BasicApiAssert.assertThet(actual)
                .statusCodeIsEqual(200)
                .bodyFieldExists("data")
                .listIsEmpty("data.result");

        return this;
    }
}
