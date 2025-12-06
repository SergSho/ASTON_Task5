package ru.shokhinsergey.springproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shokhinsergey.springproject.dto.UserDtoCreateAndUpdate;
import ru.shokhinsergey.springproject.dto.UserDtoResult;
import ru.shokhinsergey.springproject.mapper.UserDtoResultMapper;
import ru.shokhinsergey.springproject.mapper.UserMapper;
import ru.shokhinsergey.springproject.model.User;
import ru.shokhinsergey.springproject.repository.UserRepository;

import java.util.Optional;


@Service
@Transactional()
public class UserService {

    private final UserMapper userMapper;
    private final UserDtoResultMapper userDtoMapper;
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserMapper userMapper, UserDtoResultMapper userDtoMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userDtoMapper = userDtoMapper;
        this.userRepository = userRepository;
    }

    public Optional<UserDtoResult> get (Integer id){
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) return Optional.empty();
        return userOptional.map(userDtoMapper::mapFrom).stream().findAny();
    }

    public Optional<UserDtoResult> delete(Integer id){
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) userRepository.deleteById(id);
        else return Optional.empty();
        return userOptional.map(userDtoMapper::mapFrom).stream().findAny();
    }


    public UserDtoResult create(UserDtoCreateAndUpdate userCreateDto){
        User createUser = userMapper.mapFrom(userCreateDto);
        createUser = userRepository.save(createUser);
        return userDtoMapper.mapFrom(createUser);
    }

    public Optional<UserDtoResult> update(UserDtoCreateAndUpdate userUpdateDto, Integer id){
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            updateUser(userUpdateDto, user);
        } else return Optional.empty();
        return userOptional.map(userDtoMapper::mapFrom).stream().findAny();
    }

    private void updateUser(UserDtoCreateAndUpdate userUpdateDto, User user) {
        user.setName(userUpdateDto.getName());
        user.setEmail(userUpdateDto.getEmail());
        user.setAge(userUpdateDto.getAge());
    }

}
