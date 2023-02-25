package pan.affiliation.shared.helpers;

import java.util.regex.Pattern;

public class StringHelpers {
    public static String keepOnlyNumbers(String param) {
        if (StringHelpers.isNullOrEmpty(param))
            return "";
        var pattern = Pattern.compile("[^0-9]");
        var matcher = pattern.matcher(param);
        return matcher.replaceAll("");
    }

    public static Boolean isNullOrEmpty(String param) {
        return param == null || param.isEmpty();
    }
}