package com.asx.assignment.ums.service;

import com.asx.assignment.ums.model.User;
import com.asx.assignment.ums.repository.UserRepository;
import com.asx.assignment.ums.rest.dto.UserDTO;
import com.asx.assignment.ums.util.JpaToRestModelMapper;
import com.asx.assignment.ums.util.JpaToRestModelMapperImpl;
import com.asx.assignment.ums.util.RestToJpaModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static java.util.Optional.of;

@Service
@Transactional
public class UserService {

    final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO saveOrUpdate(UserDTO newUserDto) {
        User newUser = RestToJpaModelMapper.INSTANCE.map(newUserDto);
        return of(newUser.getId())
                .filter(id -> id > 0)
                .flatMap(userRepository::findById)
                .map(user -> user.copyFrom(newUser))
                .map(userRepository::save)
                .map(JpaToRestModelMapperImpl.INSTANCE::map)
                .orElseGet(() -> JpaToRestModelMapperImpl.INSTANCE.map(userRepository.save(newUser)));
    }

    public Optional<UserDTO> findById(long id) {
        return userRepository.findById(id)
                .map(JpaToRestModelMapper.INSTANCE::map);
    }
}
