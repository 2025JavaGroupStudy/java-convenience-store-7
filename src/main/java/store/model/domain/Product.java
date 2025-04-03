package store.model.domain;

import java.lang.reflect.Field;
import java.util.List;
import store.utility.ErrorMessage;
import store.utility.Parser;

public class Product implements Comparable<Product> {
    String name;
    int price;

    //확장성을 위해 이리 복잡하게 설계했습니다. 현재 모델의 필드명의 순서를 바꾸던지 추가해도
    //해당 팩토리 패턴을 그대로 사용할 수 있습니다.
    public static Product createProduct(List<String> generatedFields){
        Product product = new Product();
        Field[] fields = Product.class.getDeclaredFields();

        if (generatedFields == null || generatedFields.size() != fields.length) {
            throw new IllegalArgumentException(String.format(
                    ErrorMessage.RESOURCE_LENGTH_WRONG.getMessage(),fields.length));
        }

        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true); // Allow private field modification
            try {
                Object value = Parser.castValue(fields[i].getType(), generatedFields.get(i));
                fields[i].set(product, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to set field value", e);
            }
        }
        return product;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        String processedPrice = String.format("%,d", price);
        return name + ' ' + processedPrice + "원";
    }

    @Override
    public int compareTo(Product other) {
        return this.name.compareTo(other.name); // Sorts alphabetically
    }
}
