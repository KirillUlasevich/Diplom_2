import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChangeDataUserTest {

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
    @DisplayName("Тест изменения данных пользователя с авторизацией")
    public void changeDataUserTest() {
        UserData data = UserData.from(user);
        String token = userClient.loginUser(data)
                .extract().path("accessToken");
        String email = userClient.loginUser(data)
                .extract().path("user.email");
        String name = userClient.loginUser(data)
                .extract().path("user.name");
        userClient.getDataUser(token);
        String json = "{\"email\": \"" + RandomStringUtils.randomAlphabetic(6) + "@ya.ru\", \"name\": \"" + RandomStringUtils.randomAlphabetic(6) + "\"}";
        String newName = userClient.changeDataUser(token, json)
                .extract().body().path("user.name");
        String newEmail = userClient.changeDataUser(token, json)
                .extract().body().path("user.email");
        Assert.assertNotEquals(name, newName);
        Assert.assertNotEquals(email, newEmail);
        String jsonNew = "{\"email\": \"" + email + "\", \"name\": \"" + name + "\"}";
        userClient.changeDataUser(token, jsonNew);
    }

    @Test
    @DisplayName("Тест изменения данных пользователя без авторизации")
    public void changeDataUserNoAuthorizationTest() {
        UserData data = UserData.from(user);
        userClient.loginUser(data);
        String json = "{\"email\": \"" + RandomStringUtils.randomAlphabetic(6) + "@ya.ru\", \"name\": \"" + RandomStringUtils.randomAlphabetic(6) + "\"}";
        userClient.changeDataUserNoAuthorization(json)
                .statusCode(401);
        String message = userClient.changeDataUserNoAuthorization(json)
                .extract().body().path("message");
        Assert.assertEquals("You should be authorised", message);
    }
}
