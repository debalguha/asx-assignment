package com.asx.assignment.ums.service;

import com.asx.assignment.ums.model.Gender;
import com.asx.assignment.ums.model.State;
import com.asx.assignment.ums.rest.dto.AddressDTO;
import com.asx.assignment.ums.rest.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@Profile("!Pact")
public class DataSetupService implements CommandLineRunner {
    final UserService userService;

    public DataSetupService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        final EasyRandom easyRandom = new EasyRandom(
                new EasyRandomParameters().excludeField(
                        FieldPredicates.named("id")
                                .and(FieldPredicates.ofType(String.class))
                                .and(FieldPredicates.inClass(UserDTO.class))
                ).excludeField(FieldPredicates.named("id")
                        .and(FieldPredicates.ofType(String.class))
                        .and(FieldPredicates.inClass(AddressDTO.class))
                )
        );
        final List<UserDTO> users = IntStream.range(0, 100)
                .mapToObj(i -> easyRandom.nextObject(UserDTO.class))
                .map(userService::saveOrUpdate)
                .collect(Collectors.toList());
        users.stream().filter(u -> u.getId().equals("1"))
                .map(u -> new UserDTO(u.getId(), "Mr.", "John", "Doe", Gender.MALE,
                        new AddressDTO(u.getAddress().getId(), "16 Bridge st", "Sydney", State.NSW, 2000)))
                .findFirst()
                .ifPresent(userService::saveOrUpdate);

        log.info("Created {} users", users.size() + 1);
    }
}
