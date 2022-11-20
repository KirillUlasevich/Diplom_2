import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GetOrderTest {
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
    @DisplayName("Тест получения заказа авторизованным пользователем")
    public void getOrdersWithAuthorization() {
        UserData data = UserData.from(user);
        String token = userClient.loginUser(data).extract().path("accessToken");
        String json = "{\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa6f\"]}";
        int total = userClient.getOrdersWithAuthorization(token).extract().path("total");
        userClient.createOrder(token, json);
        userClient.getOrdersWithAuthorization(token);
        int newTotal = userClient.getOrdersWithAuthorization(token).extract().path("total");
        System.out.println(total);
        assertEquals(total + 1, newTotal);
    }

    @Test
    @DisplayName("Тест ошибки при получении заказа неавторизованным пользователем")
    public void getOrdersNoAuthorization() {
        String message = userClient.getOrdersNoAuthorization().extract().path("message");
        assertEquals("You should be authorised", message);

    }
}
