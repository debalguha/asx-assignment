package com.asx.assignment.ums.service;

import com.asx.assignment.ums.model.State;
import com.asx.assignment.ums.repository.UserRepository;
import com.asx.assignment.ums.rest.dto.AddressDTO;
import com.asx.assignment.ums.rest.dto.UserDTO;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.jeasy.random.TypePredicates;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import java.util.Optional;

import static java.lang.Long.parseLong;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserServiceTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UserRepository userRepository;

    private UserService userService;

    EasyRandom easyRandom;

    @BeforeEach
    public void setup() {
        userService = new UserService(userRepository);
        easyRandom = new EasyRandom(
                new EasyRandomParameters().excludeField(
                        FieldPredicates.named("id")
                                .and(FieldPredicates.ofType(String.class))
                                .and(FieldPredicates.inClass(UserDTO.class))
                ).excludeField(FieldPredicates.named("id")
                        .and(FieldPredicates.ofType(String.class))
                        .and(FieldPredicates.inClass(AddressDTO.class))
                )
        );
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(userRepository).isNotNull();
    }

    @Test
    void saveOrUpdate_save() {
        UserDTO userDTO = easyRandom.nextObject(UserDTO.class);
        UserDTO savedUserDTO = userService.saveOrUpdate(userDTO);
        Assertions.assertNotNull(savedUserDTO);
        Assertions.assertTrue(parseLong(savedUserDTO.getId()) > 0);
    }

    @Test
    void saveOrUpdate_update() {
        UserDTO userDTO = easyRandom.nextObject(UserDTO.class);
        UserDTO user = userService.saveOrUpdate(userDTO);
        Assertions.assertNotNull(user);
        Assertions.assertTrue(parseLong(user.getId()) > 0);
        userDTO.setId(user.getId());
        userDTO.setAddress(new AddressDTO(userDTO.getAddress().getId(),"Jenkins Road", "Carlingford", State.ACT, 2111));
        UserDTO updatedUser = userService.saveOrUpdate(userDTO);
        Assertions.assertEquals("Jenkins Road", updatedUser.getAddress().getStreet());
        Assertions.assertEquals("Carlingford", updatedUser.getAddress().getCity());
        Assertions.assertEquals(State.ACT, updatedUser.getAddress().getState());
        Assertions.assertEquals(2111, updatedUser.getAddress().getPostCode());
    }

    @Test
    void findById() {
        UserDTO userDTO = easyRandom.nextObject(UserDTO.class);
        UserDTO savedUserDTO = userService.saveOrUpdate(userDTO);
        Optional<UserDTO> foundUserDTO = userService.findById(parseLong(savedUserDTO.getId()));
        assertThat(foundUserDTO.get()).isEqualTo(savedUserDTO);
    }
}