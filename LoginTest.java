
import org.junit.Test;
import static org.junit.Assert.*;

public class LoginTest {

    @Test
    public void testValidUsername() {
        Login login = new Login();
        login.registerUser("abc_d", "Password1!", "+27821234567", "John", "Smith");
        assertTrue(login.checkUserName());
    }

    @Test
    public void testInvalidUsernameWithoutUnderscore() {
        Login login = new Login();
        login.registerUser("abcde", "Password1!", "+27821234567", "John", "Smith");
        assertFalse(login.checkUserName());
    }

    @Test
    public void testInvalidUsernameTooLong() {
        Login login = new Login();
        login.registerUser("abcdef_", "Password1!", "+27821234567", "John", "Smith");
        assertFalse(login.checkUserName());
    }

    @Test
    public void testValidPassword() {
        Login login = new Login();
        login.registerUser("abc_d", "Password1!", "+27821234567", "John", "Smith");
        assertTrue(login.checkPasswordComplexity());
    }

    @Test
    public void testPasswordTooShort() {
        Login login = new Login();
        login.registerUser("abc_d", "Pass1!", "+27821234567", "John", "Smith");
        assertFalse(login.checkPasswordComplexity());
    }

    @Test
    public void testPasswordWithoutUppercase() {
        Login login = new Login();
        login.registerUser("abc_d", "password1!", "+27821234567", "John", "Smith");
        assertFalse(login.checkPasswordComplexity());
    }

    @Test
    public void testPasswordWithoutNumber() {
        Login login = new Login();
        login.registerUser("abc_d", "Password!", "+27821234567", "John", "Smith");
        assertFalse(login.checkPasswordComplexity());
    }

    @Test
    public void testPasswordWithoutSpecialCharacter() {
        Login login = new Login();
        login.registerUser("abc_d", "Password123", "+27821234567", "John", "Smith");
        assertFalse(login.checkPasswordComplexity());
    }

    @Test
    public void testValidCellPhoneNumber() {
        Login login = new Login();
        login.registerUser("abc_d", "Password1!", "+27821234567", "John", "Smith");
        assertTrue(login.checkCellPhoneNumber());
    }

    @Test
    public void testInvalidCellPhoneNumber() {
        Login login = new Login();
        login.registerUser("abc_d", "Password1!", "0821234567", "John", "Smith");
        assertFalse(login.checkCellPhoneNumber());
    }

    @Test
    public void testSuccessfulRegistration() {
        Login login = new Login();
        String result = login.registerUser(
                "abc_d", "Password1!", "+27821234567", "John", "Smith");

        assertTrue(result.contains("Registration successful"));
    }

    @Test
    public void testFailedRegistration() {
        Login login = new Login();
        String result = login.registerUser(
                "abcdef", "Password1!", "+27821234567", "John", "Smith");

        assertTrue(result.contains("Username is not correctly formatted"));
    }

    @Test
    public void testSuccessfulLogin() {
        Login login = new Login();

        login.registerUser(
                "abc_d", "Password1!", "+27821234567", "John", "Smith");

        assertTrue(login.loginUser("abc_d", "Password1!"));
    }

    @Test
    public void testIncorrectLoginPassword() {
        Login login = new Login();

        login.registerUser(
                "abc_d", "Password1!", "+27821234567", "John", "Smith");

        assertFalse(login.loginUser("abc_d", "WrongPassword1!"));
    }

    @Test
    public void testIncorrectLoginUsername() {
        Login login = new Login();

        login.registerUser(
                "abc_d", "Password1!", "+27821234567", "John", "Smith");

        assertFalse(login.loginUser("wrong_", "Password1!"));
    }

    @Test
    public void testLoginStatusMessage() {
        Login login = new Login();

        login.registerUser(
                "abc_d", "Password1!", "+27821234567", "John", "Smith");

        String result = login.returnLoginStatus(true, "abc_d");

        assertTrue(result.contains("Welcome John Smith"));
    }
}
