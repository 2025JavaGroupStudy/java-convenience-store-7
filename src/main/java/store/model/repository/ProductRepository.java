package store.model.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.model.domain.Product;
import store.model.repository.interfaces.RepositoryProvider;

public class ProductRepository implements RepositoryProvider {
    private final Map<String, Product> productRepository;
    private static final String errorHeader = "ProductRepository : ";

    public ProductRepository(){
        productRepository = new HashMap<>();
    }

    @Override
    public void addItem(String key, Object item) {
        try{
            Product product = (Product) item;
            productRepository.putIfAbsent(key, product);
        } catch (Exception e) {
            throw new IllegalArgumentException(errorHeader + e.getMessage());
        }
    }

    @Override
    public Product isItem(List<String> generatedFields){
        Product product = Product.createProduct(generatedFields);
        addItem(generatedFields.getFirst(), product);
        return product;
    }
}
