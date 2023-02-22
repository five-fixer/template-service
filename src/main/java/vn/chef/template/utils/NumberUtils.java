package vn.chef.template.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NumberUtils {

    /**
     * Converts a Float to a Double, or returns 0D if the Float is null.
     *
     * @param floatValue The Float value to be converted to Double.
     * @return A double value.
     */
    public static Double floatToDouble(Float floatValue) {
        if (Objects.isNull(floatValue)) {
            return 0D;
        }

        return floatValue.doubleValue();
    }

}
