package store.model.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import jdk.jfr.Description;
import store.model.domain.Stock;
import store.model.repository.interfaces.RepositoryProvider;
import store.utility.Pair;

public class StockRepository implements RepositoryProvider {
    private final Map<String, Pair<Stock, Stock>> stockRepository;

    public StockRepository(){
        stockRepository = new HashMap<>();
    }

    @Override
    public void addItem(Object item){
        Stock stock = (Stock)item;
        String productName = stock.getProductName();
        Pair<Stock, Stock> repositoryValue = stockRepository.get(productName);
        if(stockRepository.replace(productName, changedStockField(repositoryValue, stock))==null){
            stockRepository.put(productName, changedStockField(repositoryValue, stock));
        }
    }

    @Override
    @Description("First value : Promotion stock\nSecond Value : Normal Stock")
    public Pair<Stock,Stock> isItem(String name){
        return stockRepository.get(name);
    }

    public List<String> allEntriesToString() {
        return stockRepository.values().stream().flatMap(StockRepository::streamList).toList();
    }

    public static Stream<String> streamList(Pair<Stock, Stock> inputPair){
        return Stream.of(inputPair.getFirst(), inputPair.getSecond())
                .filter(Objects::nonNull)
                .map(Object::toString);
    }


    //------------------------------------------------------------------------------------------------//
    // private methods from here

    private Pair<Stock, Stock> changedStockField(Pair<Stock, Stock> repositoryValue, Stock newItem){
        if(repositoryValue == null){ repositoryValue = new Pair<>(null, null);}
        Stock promotionStock = repositoryValue.getFirst(),
              normalStock = repositoryValue.getSecond();

        if(newItem.getPromotion()!=null){
            repositoryValue.setFirst(processedStock(promotionStock, newItem));
            if(normalStock==null) {
                newItem = Stock.createStore(newItem.getProduct(), null, 0);
            }
        }
        repositoryValue.setSecond(processedStock(normalStock, newItem));

        return repositoryValue;
    }

    private Stock processedStock(Stock originalStock, Stock newStock){
        if(originalStock == null) originalStock = newStock;
        else originalStock.deltaQuantity(newStock.getQuantity());
        return originalStock;
    }


}
