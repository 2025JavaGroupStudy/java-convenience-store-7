package store.utility;

import static store.utility.SystemConstantVariable.INPUT_CONFIRM_STRING;
import static store.utility.SystemConstantVariable.INPUT_REJECT_STRING;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputFormatter {
    private static final String OUTER_BRACKET = "[";
    private static final String INNER_BRACKET = "]";
    private static final String CONNECTOR = "-";
    // Regex of FORMAT rejects 0 or negative value of input's quantity place.
    private static final Pattern FULL_FORMAT = Pattern.compile("\\[([^\\]-]+)-([1-9]\\d*)\\](,\\[([^\\]-]+)-([1-9]\\d*)\\])*");
    private static final Pattern PART_FORMAT = Pattern.compile("\\[([^\\]-]+)-([1-9]\\d*)\\]");

    public static String[] isStockFormat(String inputLine){
        String[] result = patternExtractor(inputLine);
        if(result.length>0 && !Objects.equals(result[0], inputLine)) return result;
        throw new IllegalArgumentException(
                String.format(ErrorMessage.INPUT_NOT_FORMAT.getMessage(),
                        OUTER_BRACKET+"문자"+CONNECTOR+"양의 정수"+INNER_BRACKET));
    }

    public static String stockFormat(List<String> inputItem){
        if(inputItem.size()%2!=0) {
            throw new IllegalArgumentException(
                    String.format(ErrorMessage.PARAMETER_NOT_ACCEPTABLE.getMessage(), "리스트", "짝수"));
        }
        StringBuilder resultFormat = new StringBuilder();
        for(int i=0; i<inputItem.size(); i++){
            if(i%2!=0) resultFormat.append(CONNECTOR).append(inputItem.get(i)).append(INNER_BRACKET);
            else resultFormat.append(OUTER_BRACKET).append(inputItem.get(i));
        }
        return resultFormat.toString();
    }

    public static String isConfirmRejectFormat(String input){
        if(input.equals(SystemConstantVariable.INPUT_CONFIRM_STRING) || input.equals(SystemConstantVariable.INPUT_REJECT_STRING)){
            return input;
        }
        throw new IllegalArgumentException(
                String.format(ErrorMessage.INPUT_NOT_FORMAT.getMessage(), INPUT_CONFIRM_STRING +"/"+ INPUT_REJECT_STRING));
    }

    private static String[] patternExtractor(String inputLine){
        Matcher matcher = PART_FORMAT.matcher(inputLine);
        List<String> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(matcher.group(1));
            matches.add(matcher.group(2));// Extract full match
        }
        return matches.toArray(new String[0]);
    }

}
