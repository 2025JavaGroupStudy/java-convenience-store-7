package store.service;

import java.util.UUID;
import store.model.domain.User;
import store.model.repository.UserRepository;

public class UserService {
    UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public String createUser(){
        User user = new User();
        String userKey = String.valueOf(UUID.randomUUID());
        userRepository.addItem(userKey, user);
        return userKey;
    }

    public User retrieveUser(String userKey){
        return userRepository.isItem(userKey);
    }

    public void removeUser(String userKey){
        userRepository.removeItem(userKey);
    }

}
