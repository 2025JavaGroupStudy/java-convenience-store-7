package store.model.domain;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import store.utility.ErrorMessage;
import store.utility.Parser;

public class Stock implements Comparable<Stock>{
    private Product product;
    private int quantity;
    private Promotion promotion;

    Stock(Product product, Object quantity){
        this.product = product;
        this.quantity = (int) quantity;
    }

    public Stock setPromotion(Promotion promotion){
        this.promotion = promotion;
        return this;
    }

    public Stock removePromotion(){
        promotion = null;
        return this;
    }

    public static Stock createStore(Product product, List<String> storeValues){
        List<Field> fields = new java.util.ArrayList<>(List.of(Stock.class.getDeclaredFields()));
        fields.removeIf(field -> field.getName().equals("product") || field.getName().equals("promotion"));
        if (storeValues == null || storeValues.size() != fields.size()) {
            throw new IllegalArgumentException(String.format(
                    ErrorMessage.RESOURCE_LENGTH_WRONG.getMessage(),fields.size()));
        }
        return new Stock(product, Parser.castValue(fields.getFirst().getType(), storeValues.getFirst()));
    }

    public static Stock createStore(Product product, Promotion promotion, int quantity){
        return new Stock(product, quantity).setPromotion(promotion);
    }

    public void deltaQuantity(int delta) {
        quantity+=delta;
    }

    public void removeQuantity(){
        quantity=0;
    }

    public String getProductName() {
        return product.getName();
    }

    public Product getProduct(){
        return product;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public int getTotalPrice(){
        return product.getPrice() * quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isEmpty(){
        return product==null&&promotion==null&&quantity==0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(product.toString()).append(" ");
        if(quantity==0){
            builder.append("재고 없음");
            return builder.toString();
        }
        builder.append(quantity).append("개").append(" ");
        if(promotion != null) {
            builder.append(promotion.getName());
        }
        return builder.toString();
    }

    @Override
    public int compareTo(Stock other) {
        return this.product.compareTo(other.product); // Sorts alphabetically
    }
}
