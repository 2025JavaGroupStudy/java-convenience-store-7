package store.model.repository;

import java.util.HashMap;
import java.util.Map;
import store.model.domain.User;
import store.model.repository.interfaces.RepositoryProvider;

public class UserRepository implements RepositoryProvider {
    private final Map<String, User> userRepository;
    private static final String errorHeader = "UserRepository : ";

    public UserRepository(){
        userRepository = new HashMap<>();
    }

    @Override
    public void addItem(String key, Object item) {
        try{
            User user = (User) item;
            userRepository.putIfAbsent(key, user);
        } catch (Exception e) {
            throw new IllegalArgumentException(errorHeader + e.getMessage());
        }
    }

    @Override
    public User isItem(String userKey){
        return userRepository.get(userKey);
    }

    public void removeItem(String userKey){
        userRepository.remove(userKey);
    }
}
