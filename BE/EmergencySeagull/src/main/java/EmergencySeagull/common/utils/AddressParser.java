package EmergencySeagull.common.utils;

import static EmergencySeagull.common.exception.ExceptionCode.INVALID_ADDRESS_FORMAT;

import EmergencySeagull.common.exception.CustomException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressParser {

    private static final Pattern CITY_PATTERN = Pattern.compile(
        ".*?([가-힣]+(?:군|구)).*"
    );

    public static String parseCity(String fullAddress) {
        Matcher matcher = CITY_PATTERN.matcher(fullAddress);
        if (!matcher.matches()) {
            throw new CustomException(INVALID_ADDRESS_FORMAT);
        }
        return matcher.group(1).trim();
    }
}