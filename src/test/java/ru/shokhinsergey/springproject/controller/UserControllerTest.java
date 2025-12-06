package ru.shokhinsergey.springproject.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.shokhinsergey.springproject.dto.UserDtoResult;
import ru.shokhinsergey.springproject.service.UserService;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Mock
    private UserService mockService;

//    @InjectMocks
//    private UserController controller;

    private MockMvc mockMvc;

    private static UserDtoResult dtoUser;

    @BeforeAll
    public static void init() {
        dtoUser = UserDtoResult.Builder
                .builder()
                .setName("Alex")
                .setEmail("alex@mail.ru")
                .setAge(18)
                .setId(1)
                .setCreated_At(LocalDate.now())
                .build();


    }

    @Test
    @DisplayName("Ответ, если указанный \"id\" есть в базе данных.")
    void ifIdExist_ok_methodGet() throws Exception {

        Mockito.doReturn(dtoUser).when(mockService).get(1);

        mockMvc.perform(get("/{id}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(dtoUser.getName()))
                .andExpect(jsonPath("$.email").value(dtoUser.getEmail()))
                .andExpect(jsonPath("$.age").value(dtoUser.getAge()))
                .andExpect(jsonPath("$.id").value(dtoUser.getId()))
                .andExpect(jsonPath("$.created_At").value(dtoUser.getCreated_At()));
    }


}
