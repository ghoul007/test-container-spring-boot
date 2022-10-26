package com.example.demo;

import com.example.demo.controller.UserController;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class UserRepositoryTest extends BaseTest {

  private final MockMvc mockMvc;
  private final UserRepository userRepository;
  @Autowired
  UserRepositoryTest(MockMvc mockMvc, UserRepository userRepository) {
    this.mockMvc = mockMvc;
    this.userRepository = userRepository;
  }



//  @Autowired
//  @Qualifier("UserRepository")
//  private final UserRepository userRepository;


  static {
    tenant_init_script_file = "import.sql";
  }




  @Test
  @Sql("/import.sql")
  public void testUsers() throws Exception {
    this.mockMvc.perform(get("/api/users"))
      .andExpect(status().isOk());
    Assertions.assertEquals(4, userRepository.findAll().size());
  }
}
