package org.duckdb;

import java.util.*;

public class Errors {
    static List<Character> getBytes(String input) {
        var output = new ArrayList<Character>();
        for (byte b : input.getBytes()) {
            output.add((char) b);
        }
        return output;
    }
    public static HashMap<String, String> parse(String message) {
        var q = new ArrayDeque<>(getBytes(message));
        check(q.pop(), '{');
        var values = new HashMap<String, String>();
        while (true) {
            check(q.peek(), '"');
            String key = consumeString(q);
            check(q.pop(), ':');
            String value = consumeString(q);
            values.put(key, value);
            var next = q.pop();
            if (next == '}') {
                break;
            } else {
                check(next, ',');
            }
        }
        return values;
    }

    private static void check(Character next, char c) {
        if (next != c) {
            throw new IllegalStateException(
                    String.format(
                            "%s should have equalled %s",
                            next, c
                    )
            );
        }
    }

    private static String consumeString(ArrayDeque<Character> q) {
        check(q.pop(), '"');
        StringBuilder output = new StringBuilder();
        while (q.peek() != (Character) '"') {
            output.append(q.pop());
        }
        check(q.pop(), '"');
        return output.toString();
    }
}
