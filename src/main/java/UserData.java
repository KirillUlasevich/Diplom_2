public class UserData {

    private String email;
    private String password;

    public UserData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserData from(User user) {
        return new UserData(user.getEmail(), user.getPassword());
    }

    public static UserData getWrongLoginPassword(User user) {
        return new UserData("wrongEmail", "wrongPass");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
