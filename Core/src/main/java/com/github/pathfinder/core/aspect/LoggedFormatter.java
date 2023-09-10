package com.github.pathfinder.core.aspect;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.Signature;
import org.springframework.stereotype.Component;

@Component
public class LoggedFormatter {

    private static final char DOT           = '.';
    private static final char OPENING_BRACE = '(';
    private static final char CLOSING_BRACE = ')';
    private static final char SPACE         = ' ';
    private static final char EQUALS        = '=';
    private static final char COMMA         = ',';

    public String methodName(Signature signature) {
        return signature.getDeclaringType().getSimpleName() + DOT + signature.getName();
    }

    public String header(String methodName, String[] names, Object[] arguments) {
        if (ArrayUtils.isEmpty(names)) {
            return methodName;
        }

        return methodName + OPENING_BRACE + formatArguments(names, arguments) + CLOSING_BRACE;
    }

    public String formatArguments(String[] names, Object[] arguments) {
        var result = new StringBuilder();

        for (var i = 0; i < names.length && i < arguments.length; i++) {
            var name = names[i];

            if (StringUtils.isEmpty(name)) {
                continue;
            }

            result.append(name).append(EQUALS).append(arguments[i]);

            if (i != names.length - 1) {
                result.append(COMMA).append(SPACE);
            }
        }

        return result.toString();
    }

}
