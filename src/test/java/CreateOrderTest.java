import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreateOrderTest {

    User user;
    UserClient userClient;

    @Before
    public void setup() {
        user = User.getRandomUser();
        userClient = new UserClient();
        userClient.createUser(user);
    }

    @After
    public void delete() {
        userClient.deleteUser(user);
    }

    @Test
    @DisplayName("Тест получения данных об ингридентах")
    public void getIngredientsTest() {
        userClient.getIngredientsList();
    }

    @Test
    @DisplayName("Тест создания заказа с авторизацией и с ингредиентами")
    public void createOrderWithAuthorizationWithIngredients() {
        UserData data = UserData.from(user);
        String token = userClient.loginUser(data).extract().path("accessToken");
        String json = "{\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa70\"]}";
        userClient.createOrder(token, json).statusCode(200);
        String name = userClient.createOrder(token, json).extract().path("name");
        assertEquals("Метеоритный флюоресцентный бургер", name);
    }

    @Test
    @DisplayName("Тест создания заказа без авторизации и с ингредиентами")
    public void createOrderNoAuthorizationWithIngredients() {
        String json = "{\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa70\"]}";
        userClient.createOrderNoAuthorization(json).statusCode(200);
        String name = userClient.createOrderNoAuthorization(json).extract().path("name");
        assertEquals("Метеоритный флюоресцентный бургер", name);
    }

    @Test
    @DisplayName("Тест невозможности создания заказа с авторизацией и без ингредиентов")
    public void createOrderWithAuthorizationNoIngredients() {
        UserData data = UserData.from(user);
        String token = userClient.loginUser(data).extract().path("accessToken");
        String json = "{\"ingredients\": []}";
        userClient.createOrder(token, json).statusCode(400);
        String message = userClient.createOrder(token, json).extract().path("message");
        assertEquals("Ingredient ids must be provided", message);
    }

    @Test
    @DisplayName("Тест невозможности создания заказа без авторизации и без ингредиентов")
    public void createOrderNoAuthorizationNoIngredients() {
        String json = "{\"ingredients\": []}";
        userClient.createOrderNoAuthorization(json).statusCode(400);
        String massage = userClient.createOrderNoAuthorization(json).extract().path("message");
        assertEquals("Ingredient ids must be provided", massage);
    }

    @Test
    @DisplayName("Тест невозможности создания заказа с невалидным хэшем ингридиентов")
    public void createOrderWrongIngredients() {
        UserData data = UserData.from(user);
        String token = userClient.loginUser(data).extract().path("accessToken");
        String json = "{\"ingredients\": [\"две мясных котлеты гриль\",\"специальный соус, сыр\"]}";
        userClient.createOrder(token, json).statusCode(500);
    }
}
