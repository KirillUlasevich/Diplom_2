import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient {

    private final String REGISTER = "/auth/register";
    private final String LOGIN = "/auth/login ";
    private final String USER = "/auth/user ";
    private final String ORDERS = "/orders ";
    private final String INGREDIENTS = "/ingredients ";

    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return given().log().all().header("Content-Type", "application/json").baseUri(Config.URL).body(user).when().post(REGISTER).then().log().all();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(User user) {
        UserData data = UserData.from(user);
        String token = loginUser(data).extract().path("accessToken");
        return given().log().all().header("Content-Type", "application/json").baseUri(Config.URL).body(user).auth().oauth2(token.substring(7)).when().delete(USER).then().log().all();
    }

    @Step("Логин пользователя")
    public ValidatableResponse loginUser(UserData data) {
        return given().log().all().header("Content-Type", "application/json").baseUri(Config.URL).body(data).when().post(LOGIN).then().log().all();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse getDataUser(String token) {
        return given().log().all().header("Content-Type", "application/json").baseUri(Config.URL).auth().oauth2(token.substring(7)).get(USER).then().log().all();
    }

    @Step("Изменение данных пользователя")
    public ValidatableResponse changeDataUser(String token, String json) {
        return given().log().all().baseUri(Config.URL).header("Content-Type", "application/json").auth().oauth2(token.substring(7)).body(json).when().patch(USER).then().log().all();
    }

    @Step("Изменение данных пользователя без авторизации")
    public ValidatableResponse changeDataUserNoAuthorization(String json) {
        return given().log().all().baseUri(Config.URL).header("Content-Type", "application/json").body(json).when().patch(USER).then().log().all();
    }

    @Step("Создание заказа")
    public ValidatableResponse createOrder(String token, String json) {
        return given().log().all().header("Content-Type", "application/json").body(json).baseUri(Config.URL).auth().oauth2(token.substring(7)).post(ORDERS).then().log().all();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderNoAuthorization(String json) {
        return given().log().all().header("Content-Type", "application/json").body(json).baseUri(Config.URL).post(ORDERS).then().log().all();
    }

    @Step("Получение списка ингредиентов")
    public ValidatableResponse getIngredientsList() {
        return given().log().all().header("Content-Type", "application/json").baseUri(Config.URL).get(INGREDIENTS).then().log().all();
    }

    @Step("Получение списка заказов с авторизацией")
    public ValidatableResponse getOrdersWithAuthorization(String token) {
        return given().log().all().header("Content-Type", "application/json").baseUri(Config.URL).auth().oauth2(token.substring(7)).get(ORDERS).then().log().all();
    }

    @Step("Получение списка заказов без авторизации")
    public ValidatableResponse getOrdersNoAuthorization() {
        return given().log().all().header("Content-Type", "application/json").baseUri(Config.URL).get(ORDERS).then().log().all();
    }
}
