package endpoint;

import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestApiBuilder {

    RequestSpecification rs;

    public RestApiBuilder() {
        rs = given().log().all();
    }

    public static RestApiBuilder getBuilder() {
        return new RestApiBuilder();
    }

    public static RestApiBuilder getBuilder(String url) {
        return new RestApiBuilder().setURL(url);
    }

    public static RequestSpecification getBaseRS() {
        String url = "http://localhost:8080";
        String path = "/goods";
        String login = "admin";
        String password = "secret123";

        return getBuilder(url)
                .setBasePath(path)
                .addAuth(login, password)
                .getRS();
    }

    public RestApiBuilder setURL(String url) {
        rs = this.rs.baseUri(url);

        return this;
    }

    public RestApiBuilder addAuth(String login, String password) {
        rs = rs.auth().basic(login, password);

        return this;
    }

    public RestApiBuilder addCookie(String name, String value) {
        rs = rs.cookies(Map.of(name, value));

        return this;
    }

    public RestApiBuilder setBasePath(String path) {
        rs = rs.basePath(path);

        return this;
    }

    public RequestSpecification getRS() {
        return rs;
    }

}
