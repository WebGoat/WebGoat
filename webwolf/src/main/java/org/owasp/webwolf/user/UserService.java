package org.owasp.webwolf.user;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserRepository userRepository;

    @Override
    public WebGoatUser loadUserByUsername(final String username) throws UsernameNotFoundException {
        WebGoatUser webGoatUser = userRepository.findByUsername(username);
        if (webGoatUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        webGoatUser.createUser();
        return webGoatUser;
    }


    public void addUser(final String username, final String password) {
        userRepository.save(new WebGoatUser(username, password));
    }
}
