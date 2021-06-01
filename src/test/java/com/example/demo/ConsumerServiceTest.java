package com.example.demo;

import com.example.demo.models.Consumer;
import com.example.demo.models.Role;
import com.example.demo.repos.ConsumerRepository;
import com.example.demo.services.ConsumerService;
import com.example.demo.services.MailSender;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConsumerServiceTest {

    @Autowired
    private ConsumerService consumerService;

    @MockBean
    private ConsumerRepository consumerRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private MailSender mailSender;

    @Test
    public void addConsumerTest() {
        Consumer consumer = new Consumer();
        consumer.setEmail("example@gmail.com");

        boolean isCreated = consumerService.addUser(consumer);

        Assert.assertTrue(isCreated);
        Assert.assertTrue(consumer.getRoles().contains(Role.USER));
        Assert.assertNotNull(consumer.getActivationCode());

        Mockito.verify(consumerRepository, Mockito.times(1)).save(consumer);
        Mockito.verify(mailSender, Mockito.times(1)).send(
                ArgumentMatchers.eq(consumer.getEmail()),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString());
    }

    @Test
    public void addConsumerFailTest() {
        Consumer consumer = new Consumer();
        consumer.setEmail("example@gmail.com");

        Mockito.doReturn(new Consumer())
                .when(consumerRepository)
                .findByEmail("example@gmail.com");

        boolean isCreated = consumerService.addUser(consumer);

        Assert.assertFalse(isCreated);
    }

    @Test
    public void activateConsumerTest() {
        Consumer consumer = new Consumer();
        consumer.setIsActivated(false);
        consumer.setActivationCode("1234");

        Mockito.doReturn(consumer)
                .when(consumerRepository)
                .findByActivationCode("1234");

        boolean isActivated = consumerService.activateConsumer("1234");

        Assert.assertTrue(isActivated);
        Assert.assertTrue(consumer.isActivated());
        Assert.assertNull(consumer.getActivationCode());

        Mockito.verify(consumerRepository, Mockito.times(1)).save(consumer);
    }

    @Test
    public void activateConsumerFailTest() {
        Mockito.doReturn(null)
                .when(consumerRepository)
                .findByActivationCode("1234");

        boolean isActivated = consumerService.activateConsumer("1234");

        Assert.assertFalse(isActivated);
    }
}
