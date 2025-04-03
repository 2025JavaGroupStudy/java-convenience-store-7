package store.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import store.model.domain.Product;
import store.model.domain.Stock;
import store.model.domain.Promotion;
import store.model.domain.Promotion.Builder;
import store.service.RepositoryService.AddItemType;
import store.service.RepositoryService.isItemType;
import store.utility.ErrorMessage;
import store.utility.FileReadTool;
import store.utility.Pair;
import store.utility.Parser;
import java.util.function.Function;

public class StoreInitializeService {
    private final RepositoryService repositoryService;
    private List<List<String>> table;

    public StoreInitializeService(RepositoryService repositoryService){
        this.repositoryService = repositoryService;
    }

    public void setPromotions() {
        List<String> rawTable = FileReadTool.readFile("promotions.md");
        table = new ArrayList<>(Parser.stringListParse(",", rawTable));

        Function<List<String>, Builder> builderRowInjector = Promotion.Builder.createBuilderByColumns(table.getFirst());
        table.removeFirst();
        table.forEach(row -> addPromotion(builderRowInjector.apply(row)));
    }

    public void setProductsAndStocks() {
        List<String> rawTable = FileReadTool.readFile("products.md");
        table = new ArrayList<>(Parser.stringListParse(",", rawTable));

        List<Integer> productFieldIndex = getProductFieldMatchingIndexList(table.getFirst());
        if (productFieldIndex.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.RESOURCE_COLUMN_WRONG.getMessage());
        }
        Integer promotionIndex = getPromotionIndex(table.getFirst());
        table.stream().skip(1).forEach(row -> rowToStock(row, productFieldIndex, promotionIndex));
    }

    //------------------------------------------------------------------------------------------------//
    // private methods from here

    private void addPromotion(Builder promotionBuilder) {
        Promotion promotion = promotionBuilder.build();
        repositoryService.addItem(AddItemType.PROMOTION, promotion.getName(), promotion);
    }

    private void rowToStock(List<String> row, List<Integer> productFieldIndex, int promotionIndex) {
        Product product = extractProduct(row, productFieldIndex);
        List<String> storeValues = extractStoreValues(row, productFieldIndex, promotionIndex);
        Stock stock = Stock.createStore(product, storeValues);
        if(promotionIndex>-1){
            stock.setPromotion(extractPromotion(row, promotionIndex));
        }
        repositoryService.addItem(stock);
    }

    private Product extractProduct(List<String> row, List<Integer> productFieldIndex) {
        // need to reformat this code if dataset length > 10
        List<String> productValues = new ArrayList<>(Collections.nCopies(productFieldIndex.size(), null));
        for (int i = 0; i < row.size(); i++) {
            if (productFieldIndex.contains(i)) {
                productValues.set(productFieldIndex.indexOf(i), row.get(i));
            }
        }
        return (Product) repositoryService.isItem(productValues);
    }

    private Promotion extractPromotion(List<String> row, int promotionIndex) {
        String value = row.get(promotionIndex);
        if(Objects.equals(value, "null")) return null;
        Pair<Boolean,Promotion> promotion = (Pair<Boolean, Promotion>) repositoryService.isItem(isItemType.PROMOTION, value);
        if (!promotion.getFirst()) {
            throw new NoSuchElementException(
                    String.format(ErrorMessage.RESOURCE_NO_SUCH_INSTANCE.getMessage(), value)
            );
        }
        return promotion.getSecond();

    }

    private List<String> extractStoreValues(List<String> row, List<Integer> productFieldIndex, int promotionIndex) {
        List<String> storeValues = new ArrayList<>();
        for (int i = 0; i < row.size(); i++) {
            if (!productFieldIndex.contains(i) && promotionIndex!=i) {
                storeValues.add(row.get(i));
            }
        }
        return storeValues;
    }

    private List<Integer> getProductFieldMatchingIndexList(List<String> columnLabels) {
        Class<?> product = Product.class;
        Field[] fields = product.getDeclaredFields();
        return Arrays.stream(fields)
                .map(field -> columnLabels.indexOf(field.getName()))
                .filter(index -> index >= 0)
                .toList();
    }

    private Integer getPromotionIndex(List<String> columnLabels) {
        return columnLabels.indexOf("promotion");
    }
}
