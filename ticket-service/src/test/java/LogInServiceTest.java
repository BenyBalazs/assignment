
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.epam.training.ticketservice.ActiveUserStore;
import com.epam.training.ticketservice.service.LogInService;
import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.data.repository.UserRepository;
import com.epam.training.ticketservice.exception.NotAuthorizedLogInException;
import com.epam.training.ticketservice.exception.UserAlreadyLoggedInException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = {LogInService.class, ActiveUserStore.class })
public class LogInServiceTest {

    @Autowired
    private LogInService logInService;
    @Autowired
    private ActiveUserStore activeUserStore;
    @MockBean
    private UserRepository userRepository;
    private List<User> users;

    @BeforeEach
    public void setUp(){
        users = new ArrayList<>();
        activeUserStore.setActiveUser(null);
        when(this.userRepository.findById(Mockito.any(String.class))).
                then( x -> users.stream().filter(y -> x.getArgument(0).equals(y.getUsername())).findFirst());
    }

    @Test
    public void testShouldReturnTureWhenExistingAndCorrectUsernameAndPasswordComboIsGiven() throws UserAlreadyLoggedInException {
        users.add(new User("admin","admin",User.Role.ADMIN));

        assertThat(logInService.logIn("admin", "admin"), equalTo(true));
    }

    @Test
    public void testShouldReturnFalseWhenPasswordIsInvalid() throws UserAlreadyLoggedInException {
        users.add(new User("admin","asdasdasdasdasd",User.Role.ADMIN));

        assertThat(logInService.logIn("admin", "admin"), equalTo(false));
    }

    @Test
    public void testShouldReturnFalseWhenUsernameIsInvalid() throws UserAlreadyLoggedInException {
        users.add(new User("admin","asdasdasdasdasd",User.Role.ADMIN));

        assertThat(logInService.logIn("adminasdasdasdasd", "asdasdasdasdasd"), equalTo(false));
    }

    @Test
    public void testShouldThrowUserAlreadyLoggedInExceptionWhenUserAlreadyLoggedIn() throws UserAlreadyLoggedInException, NotAuthorizedLogInException {
        users.add(new User("admin","admin", User.Role.ADMIN));
        logInService.logIn("admin","admin");

        assertThrows(UserAlreadyLoggedInException.class, () -> {logInService.logIn("admin","admin");});

    }

    @Test
    public void testShouldSetTheActiveUserWhenTheLogInWasSuccessful() throws UserAlreadyLoggedInException {
        users.add(new User("admin","admin",User.Role.ADMIN));
        logInService.logIn("admin","admin");

        assertThat(activeUserStore.getActiveUser(), is(notNullValue()));
    }

    @Test
    public void testShouldNotSetTheActiveUserWhenTheLogInWasNotSuccessful() throws UserAlreadyLoggedInException {
        users.add(new User("admin","admin",User.Role.ADMIN));
        logInService.logIn("admin","admin2");

        assertThat(activeUserStore.getActiveUser(), is(nullValue()));
    }
}
