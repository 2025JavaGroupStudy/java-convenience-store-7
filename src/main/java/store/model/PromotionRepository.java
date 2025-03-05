package store.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PromotionRepository {
    private static final Map<String, List<Promotion>> promotionRepository = new HashMap<>();
    private static final String errorHeader = "PromotionRepository : ";

    public static void addPromotion(String key, Promotion promotion) {
        try{
            List<Promotion> newEntry = promotionRepository.get(key);
            newEntry.forEach(entry -> entry.isPromotionPeriodsOverlap(promotion));
            newEntry.add(promotion);
            promotionRepository.replace(key, newEntry);
        } catch (Exception e) {
            throw new IllegalArgumentException(errorHeader + e.getMessage());
        }
    }

    public static Optional<Promotion> isPromotion(String key, LocalDate date){
        List<Promotion> registeredPromotions = promotionRepository.get(key);
        if(registeredPromotions == null) return Optional.empty();
        return registeredPromotions.stream()
                .filter(promotion -> promotion.isDatePromotionPeriod(date)).findFirst();
    }

    //확장을 위해 미리 형태만 잡아둠
    public void removeOldestPromotion(){}

    public void removeAllFinishedPromotions(){}
}
