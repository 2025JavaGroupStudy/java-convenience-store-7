package store.model.repository;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import store.model.domain.Promotion;
import store.model.repository.interfaces.RepositoryProvider;
import store.utility.Pair;

public class PromotionRepository implements RepositoryProvider {
    private final Map<String, List<Promotion>> promotionRepository;
    private static final String errorHeader = "PromotionRepository : ";

    public PromotionRepository(){
        promotionRepository = new HashMap<>();
    }

    @Override
    public void addItem(String key, Object item) {
        try{
            Promotion promotion = (Promotion) item;
            List<Promotion> newEntry = promotionRepository.get(key);
            if(newEntry!=null){
                //validation process
                newEntry.forEach(entry -> entry.isPromotionPeriodsOverlap(promotion));
                newEntry.add(promotion);
                promotionRepository.replace(key, newEntry);

            }else{
                newEntry= List.of(promotion);
                promotionRepository.put(key, newEntry);
            }

        } catch (Exception e) {
            throw new IllegalArgumentException(errorHeader + e.getMessage());
        }
    }

    @Override
    public Pair<Boolean, Promotion> isItem(String name){
        return itemInDateRangeExists(name, DateTimes.now());
    }

    //Boolean defines whether there actually is promotion
    public Pair<Boolean, Promotion> itemInDateRangeExists (String key, LocalDateTime date){
        List<Promotion> registeredPromotions = promotionRepository.get(key);
        if(registeredPromotions.isEmpty()) return new Pair<>(false,null);
        Optional<Promotion> foundPromotion = registeredPromotions.stream()
                .filter(promotion -> promotion.isDatePromotionPeriod(date)).findFirst();
        return new Pair<>(true, foundPromotion.orElse(null));
    }

    //확장을 위해 미리 형태만 잡아둠
    public void removeOldestPromotion(){}

    public void removeAllFinishedPromotions(){}

    @Override
    public String toString(){
        return promotionRepository.values().toString();
    }
}
