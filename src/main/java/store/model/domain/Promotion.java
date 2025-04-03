package store.model.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import store.utility.ErrorMessage;
import store.utility.Parser;

public class Promotion {
    private static final int MOST_GET_NUM = 5, LEAST_GET_NUM = 1;
    private static final String errorHeader = "Promotion : ";
    private static final Rule currentRule = Rule.BUYNGET1;
    private final String name;
    private final int buy;
    private final int get;
    private final LocalDateTime start_date;
    private final LocalDateTime end_date;

    private Promotion(Builder builder) {
        this.name = builder.name;
        this.buy = builder.buy;
        this.get = builder.get;
        this.start_date = builder.start_date;
        this.end_date = builder.end_date;
    }

    public String getName() {
        return name;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }

    public boolean isDatePromotionPeriod(LocalDateTime date) {
        return start_date.isBefore(date)&&end_date.isAfter(date);
    }

    public void isPromotionPeriodsOverlap(Promotion promotion){
         if(this.isDatePromotionPeriod(promotion.start_date) ||
                 this.isDatePromotionPeriod(promotion.end_date)) {
             throw new IllegalStateException(ErrorMessage.PROMOTION_DATE_OVERLAP.getMessage());
         }
    }

    public static void isMatchingRule(int getNumber){
        if(!currentRule.matches(getNumber)) {
            throw new IllegalArgumentException
                    (String.format(ErrorMessage.RESOURCE_RANGE_WRONG.getMessage(), getNumber, currentRule));
        }
    }

    private enum Rule{
        BUYNGET1(1, 1),
        BUYNGETN(LEAST_GET_NUM, MOST_GET_NUM);

        private final int minGet, maxGet;

        Rule(int startGetNumber, int endGetNumber){
            this.minGet = startGetNumber;
            this.maxGet = endGetNumber;
        }

        public boolean matches(int getNumber) {
            return this.minGet <=getNumber && getNumber<=this.maxGet;
        }
    }

    public static class Builder{
        private String name;
        private int buy;
        private int get;
        private LocalDateTime start_date;
        private LocalDateTime end_date;

        public static Function<List<String>, Builder> createBuilderByColumns(List<String> columns){
            return (List<String> values)->{
                Builder builder = new Builder();
                IntStream.range(0, columns.size())
                        .forEach(i -> builder.returnBuilderByName(columns.get(i)).apply(values.get(i)));
                return builder;
            };
        }

        Function<String, Builder> returnBuilderByName(String name){
            return switch (name) {
                case "name" -> this::setName;
                case "buy" -> this::setBuy;
                case "get" -> this::setGet;
                case "start_date" -> this::setStartDate;
                case "end_date" -> this::setEndDate;
                default -> throw new IllegalArgumentException(
                        errorHeader + String.format(ErrorMessage.RESOURCE_COLUMN_WRONG.getMessage(), name));
            };
        }

        Builder setName(String input){
            this.name = input;
            return this;
        }

        Builder setBuy(String input){
            try{
                this.buy = Parser.NumberParse(input);
                return this;
            } catch (Exception e) {
                throw new IllegalArgumentException(errorHeader + e.getMessage());
            }
        }

        Builder setGet(String input){
            try{
                int get = Parser.NumberParse(input);
                isMatchingRule(get);
                this.get = get;
                return this;
            } catch (Exception e) {
                throw new IllegalArgumentException(errorHeader + e.getMessage());
            }
        }

        Builder setStartDate(String input){
            try{
                this.start_date = Parser.DateParse(input);
                return this;
            } catch (Exception e) {
                throw new IllegalArgumentException(errorHeader + e.getMessage());
            }
        }

        Builder setEndDate(String input) {
            try {
                this.end_date = Parser.DateParse(input);
                return this;
            } catch (Exception e) {
                throw new IllegalArgumentException(errorHeader + String.format(ErrorMessage.INPUT_NOT_FORMAT.getMessage(),"날짜"));
            }
        }

        public Promotion build() {
            return new Promotion(this);
        }
    }
}
