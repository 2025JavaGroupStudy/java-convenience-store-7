package store.model.repository.interfaces;

import java.util.List;

public interface RepositoryProvider {
    default void addItem(String key, Object item){};
    default void addItem(Object item){};
    default Object isItem(String name){return null;}
    default Object isItem(List<String> generatedFields){return null;}
}
