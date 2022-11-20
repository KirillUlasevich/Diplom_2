import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginTest {

    User user;
    UserClient userClient;

    @Before
    public void setUp() {
        user = User.getRandomUser();
        userClient = new UserClient();
        userClient.createUser(user);
    }

    @After
    public void deleteUser() {
        userClient.deleteUser(user);
    }

    @Test
    @DisplayName("Тест входа с существующими данными")
    public void userLoginTest() {
        UserData data = UserData.from(user);
        Boolean isOk = userClient.loginUser(data).extract().path("success");
        assertTrue(isOk);
    }

    @Test
    @DisplayName("Тест ошибки входа с несуществующими данными")
    public void userLoginTestWrongData() {
        UserData data = UserData.getWrongLoginPassword(user);
        String massage = userClient.loginUser(data).extract().path("message");
        assertEquals("email or password are incorrect", massage);
    }
}
