package com.example.demo.services;

import com.example.demo.models.Consumer;
import com.example.demo.models.Role;
import com.example.demo.repos.ConsumerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.UUID;

@Component
public class ConsumerService implements UserDetailsService {

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Consumer consumer = consumerRepository.findByEmail(email);

        if(consumer != null) {
            return new ConsumerDetails(consumer);
        } else throw new UsernameNotFoundException("User not found");
    }

    public boolean addUser(Consumer consumer) {
        if (consumerRepository.findByEmail(consumer.getEmail()) != null) {
            return false;
        }

        consumer.setHashPassword(passwordEncoder.encode(consumer.getPassword()));
        consumer.setIsActivated(false);
        consumer.setActivationCode(UUID.randomUUID().toString());

        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        if (consumer.getEmail().equals("lyakhovskiym@gmail.com")) {
            roles.add(Role.ADMIN);
        }
        consumer.setRoles(roles);
        consumerRepository.save(consumer);

        if (!StringUtils.isEmpty(consumer.getEmail())){
            String message = String.format(
                    "Hello, %s! \n" + "Welcome to The Shop. Visit this link to activate your account:\n" + "http://localhost:8080/activate/%s",
                    consumer.getFirstName(), consumer.getActivationCode()
            );
            mailSender.send(consumer.getEmail(), "The Shop - Activation", message);
        }

        return true;
    }

    public boolean activateConsumer(String code) {
        Consumer consumer = consumerRepository.findByActivationCode(code);
        if (consumer == null) {
            return false;
        }

        consumer.setActivationCode(null);
        consumer.setIsActivated(true);
        consumerRepository.save(consumer);

        return true;
    }

    public Consumer getCurrentConsumer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        return consumerRepository.findByEmail(authentication.getName());
    }

    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().contains(Role.ADMIN);
    }

    public boolean isModerator() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().contains(Role.MODERATOR);
    }
}
