package store.model.dto;

import store.model.domain.Product;
import store.model.domain.Promotion;
import store.model.domain.Stock;
import store.utility.Pair;

public class CheckPromotionDTO {
    boolean isPurchasable;
    boolean isRelatedStock;
    Product product;
    Promotion promotion;
    int infoQuantity;
    int normalQuantity;
    int promotionQuantity;
    boolean isAdditionSuggest;


    public enum Type{
        NORMAL,
        PROMOTION
    }

    public CheckPromotionDTO(boolean isPurchasable, boolean isRelatedStock, boolean isAdditionSuggest,
                             Stock baseStock, int infoQuantity, int normalQuantity, int promotionQuantity){
        this.isPurchasable = isPurchasable;
        this.isRelatedStock = isRelatedStock;
        this.isAdditionSuggest = isAdditionSuggest;
        this.product = baseStock.getProduct();
        this.promotion = baseStock.getPromotion();
        this.infoQuantity = infoQuantity;
        this.normalQuantity = normalQuantity;
        this.promotionQuantity = promotionQuantity;
    }

    public boolean isPurchasable(){ return isPurchasable; }

    public boolean isRelatedStock() {
        return isRelatedStock;
    }

    public boolean isAdditionSuggest() {
        return isAdditionSuggest;
    }

    public Product getProduct() {
        return product;
    }

    public Promotion getPromotion() { return promotion; }

    public int getInfoQuantity() {
        return infoQuantity;
    }

    public int getNormalQuantity() { return normalQuantity; }

    public int getPromotionQuantity() { return promotionQuantity; }

    public void changeQuantity(Type type, int deltaQuantity) {
        if(type==Type.NORMAL) normalQuantity += deltaQuantity;
        if(type==Type.PROMOTION) promotionQuantity += deltaQuantity;
    }
}
