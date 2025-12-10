package ru.shokhinsergey.springproject.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shokhinsergey.springproject.dto.UserDtoCreateAndUpdate;
import ru.shokhinsergey.springproject.dto.UserDtoResult;
import ru.shokhinsergey.springproject.kafka.event.UserEvent;
import ru.shokhinsergey.springproject.mapper.UserDtoResultMapper;
import ru.shokhinsergey.springproject.mapper.UserMapper;
import ru.shokhinsergey.springproject.model.User;
import ru.shokhinsergey.springproject.repository.UserRepository;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Service
@Transactional
public class UserService {

    @Value("${springproject.kafka.topic}")
    private String topic;

    private static Logger log = LoggerFactory.getLogger(UserService.class);

    private final KafkaTemplate<Integer, UserEvent> kafkaTemplate;

    private final UserMapper userMapper;
    private final UserDtoResultMapper userDtoMapper;
    private final UserRepository userRepository;

    @Autowired
    public UserService(KafkaTemplate<Integer, UserEvent> kafkaTemplate, UserMapper userMapper, UserDtoResultMapper userDtoMapper, UserRepository userRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.userMapper = userMapper;
        this.userDtoMapper = userDtoMapper;
        this.userRepository = userRepository;
    }

    public Optional<UserDtoResult> get(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) return Optional.empty();
        return userOptional.map(userDtoMapper::mapFrom).stream().findAny();
    }

    public Optional<UserDtoResult> delete(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
            User user = userOptional.get();

            //Async mode!!!
            CompletableFuture<SendResult<Integer, UserEvent>> futureResult = kafkaTemplate.send(topic, user.getId(),
                    UserEvent.instanceOfUserEventOnDelete(user.getEmail()));

            futureResult.whenComplete((sendResult, exception) -> {
                if (exception == null) log.info("Message was sent successfully. " + sendResult.getRecordMetadata());
                else log.error("Message didn't send. Exception: {}, message: {}.", exception.getClass(),
                        exception.getMessage());
            });
            return userOptional.map(userDtoMapper::mapFrom).stream().findAny();
        }
        log.warn("User with specified id = {} not found", id);
        return Optional.empty();
    }


    public UserDtoResult create(UserDtoCreateAndUpdate userCreateDto) {
        User createUser = userMapper.mapFrom(userCreateDto);
        createUser = userRepository.save(createUser);

        //Sync mode!!!
        try {
            SendResult<Integer, UserEvent> message = kafkaTemplate.send(topic, createUser.getId(),
                    UserEvent.instanceOfUserEventOnCreate(createUser.getEmail())).get();
            log.info("Message was sent successfully. " + message.getRecordMetadata());

        } catch (Exception e) {
            log.error("Message didn't send. Exception: {}, message: {}.", e.getClass(), e.getMessage());
        }

        return userDtoMapper.mapFrom(createUser);
    }

    public Optional<UserDtoResult> update(UserDtoCreateAndUpdate userUpdateDto, Integer id) {
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
