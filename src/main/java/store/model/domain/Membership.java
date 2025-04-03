package store.model.domain;

public class Membership {
    private Type type;

    Membership(Type type){
        this.type = type;
    }

    public int discount(int price){
        int result = (int) Math.round(price * type.DISCOUNT_PERCENTAGE);
        return Math.min(result, type.DISCOUNT_LIMIT);
    }

    public enum Type{
        NORMAL(0.3, 8000),;

        Type(double discount_percentage,int discount_limit){
            this.DISCOUNT_PERCENTAGE = discount_percentage;
            this.DISCOUNT_LIMIT = discount_limit;
        }

        final double DISCOUNT_PERCENTAGE;
        final int DISCOUNT_LIMIT;

        public Membership create(){
            return new Membership(this);
        }
    }
}
