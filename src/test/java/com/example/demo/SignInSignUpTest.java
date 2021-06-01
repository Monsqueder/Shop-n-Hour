package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create_new_user_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create_new_user_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class SignInSignUpTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void signInUserTest() throws Exception {
        this.mockMvc
                .perform(formLogin("/signin").user("email", "example@gmail.com").password("1234"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void signInBadCreditsTest() throws Exception {
        this.mockMvc
                .perform(formLogin("/signin")
                        .user("email", "example@gmail.com")
                        .password("1111"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/signin"));
    }


    /**@Test
    public void signUpUserTest() throws Exception {
        this.mockMvc.perform(post("/signup")
                        .param("email", "example2@gmail.com")
                        .param("firstName", "Eric")
                        .param("lastName", "Fletcher")
                        .param("password", "1234")
                        .param("passwordConfirm", "1234"))
                    .andDo(print())
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/signin"));
    }*/

}
