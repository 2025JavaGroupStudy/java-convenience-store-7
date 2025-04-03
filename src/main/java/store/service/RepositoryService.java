package store.service;

import java.util.List;
import store.model.domain.Stock;
import store.model.repository.ProductRepository;
import store.model.repository.PromotionRepository;
import store.model.repository.interfaces.RepositoryProvider;
import store.model.repository.StockRepository;

public class RepositoryService {
    private final RepositoryProvider promotionRepository;
    private final RepositoryProvider productRepository;
    private final RepositoryProvider stockRepository;

    public RepositoryService(PromotionRepository promotionRepository, ProductRepository productRepository, StockRepository stockRepository){
        this.promotionRepository = promotionRepository;
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
    }

    public enum AddItemType {
        PROMOTION,
        PRODUCT,
        @Deprecated
        STOCK;
    }

    public enum isItemType{
        PROMOTION,
        @Deprecated
        PRODUCT,
        STOCK;
    }

    void addItem(AddItemType type, String key, Object item){
        if(type == AddItemType.PROMOTION) promotionRepository.addItem(key, item);
        if(type == AddItemType.PRODUCT) productRepository.addItem(key, item);
        //if(type == Type.STOCK) : 현재는 사용되지 않으므로 주석처리함
    };

    void addItem(Object item){
        stockRepository.addItem(item);
    };

    Object isItem(isItemType type, String name){
        if(type == isItemType.PROMOTION) return promotionRepository.isItem(name);
        if(type == isItemType.STOCK) return stockRepository.isItem(name);
        //if(type == Type.PRODUCT) : 현재는 사용되지 않으므로 주석처리함
        return null;
    }

    Object isItem(List<String> generatedFields){
        return productRepository.isItem(generatedFields);
    }

    public List<String> getCurrentStockStringList(){
        StockRepository repository = (StockRepository) stockRepository;
        return repository.allEntriesToString();
    }

    public void deductStock(String productName, int promotionStockDelta,int normalStockDelta){
        StockRepository repository = (StockRepository) stockRepository;
        repository.deductItem(productName, promotionStockDelta, normalStockDelta);
    }

    public String getPromotionRepository() {
        return promotionRepository.toString();
    }
}
