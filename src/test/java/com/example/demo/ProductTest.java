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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create_new_user_before.sql", "/create_products_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = { "/create_products_after.sql" ,"/create_new_user_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ProductTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void productListsCheck() throws Exception {
        this.mockMvc
                .perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.xpath("//div[@id='product-list']/div").nodeCount(2))
                .andExpect(MockMvcResultMatchers.xpath("//div[@id='product-list-2']/div").nodeCount(2));
    }

    @Test
    public void productPageTest() throws Exception {
        this.mockMvc
                .perform(get("/product/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .xpath("//div[@id='product-info']/div/div/span")
                        .string("product 1"))
                .andExpect(MockMvcResultMatchers
                        .xpath("//div[@id='product-info']/div/form/div/div/input")
                        .nodeCount(4));
    }

}
