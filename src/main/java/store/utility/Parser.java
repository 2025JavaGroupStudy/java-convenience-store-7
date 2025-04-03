package store.utility;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class Parser {
    public static List<List<String>> stringListParse(String regex, List<String> inputList){
        return inputList.stream().map(row-> List.of(row.split(regex))).toList();
    }

    public static List<String> stringParse(String regex, String inputLine){
        return List.of(inputLine.split(regex));
    }

    public static int NumberParse(String inputString){
        try{
            return Integer.parseInt(inputString);
        } catch (Exception e) {
            throw new IllegalArgumentException(ErrorMessage.INPUT_NOT_FORMAT.getMessage());
        }
    }

    public static LocalDateTime DateParse(String inputString){
        try{
            return LocalDate.parse(inputString, DateTimeFormatter.ofPattern("uuuu-MM-dd")).atStartOfDay();
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format(ErrorMessage.INPUT_NOT_FORMAT.getMessage(),inputString+" : 날짜"));
        }
    }

    public static Object castValue(Class<?> type, String value){
        if(type == int.class) return NumberParse(value);
        //double, boolean 검사해야하면 추후 여기에 코드 추가
        return value;
    }
}
