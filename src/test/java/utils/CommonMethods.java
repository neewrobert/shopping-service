package utils;

import com.neewrobert.shopping.domain.model.User;

import java.time.LocalDateTime;

public class CommonMethods {

    public static User getUser() {
        return new User(null, "John Doe", "johndoe@example.com", "password", LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1));
    }
}
