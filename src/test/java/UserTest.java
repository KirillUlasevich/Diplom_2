import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest {

    User user;
    UserClient userClient;

    @Before
    public void setUp() {
        user = User.getRandomUser();
        userClient = new UserClient();
    }
    @After
    public void deleteUser() {
        userClient.deleteUser(user);
    }

    @Test
    @DisplayName("Тест создания уникального пользователя")
    public void userCreateTest() {
        userClient.createUser(user)
                .assertThat()
                .statusCode(200);
    }

    @Test
    @DisplayName("Тест ошибки создания уже созданного пользователя")
    public void userCreateAlreadyExistsTest() {
        userClient.createUser(user);
        String userAlreadyExists = userClient.createUser(user)
                .assertThat()
                .statusCode(403)
                .extract().path("message");
        assertEquals("User already exists", userAlreadyExists);
    }

    @Test
    @DisplayName("Тест ошибки создания пользователя при незаполненных обязательных полях")
    public void userCreateWithoutPasswordTest() {
        userClient.createUser(user);
        User userWithoutPassword = User.getWithoutPassword();
        String expected = userClient.createUser(userWithoutPassword)
                .assertThat()
                .statusCode(403)
                .extract().path("message");
        assertEquals("Email, password and name are required fields", expected);
    }
}
