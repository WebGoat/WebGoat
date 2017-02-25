package org.owasp.webgoat.users;

import lombok.AllArgsConstructor;
import org.owasp.webgoat.session.WebGoatUser;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author nbaars
 * @since 3/19/17.
 */
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public WebGoatUser loadUserByUsername(String username) throws UsernameNotFoundException {
        WebGoatUser webGoatUser = userRepository.findByUsername(username);
        webGoatUser.createUser();
        return webGoatUser;
    }

    public void addUser(String username, String password) {
        userRepository.save(new WebGoatUser(username, password));
    }
}
