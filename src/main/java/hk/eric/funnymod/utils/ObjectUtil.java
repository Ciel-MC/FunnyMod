package hk.eric.funnymod.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.function.Function;

public class ObjectUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    public static < T, T2 > T2 nullOrMethod(T object, Function<T, T2> function) {
        if(object == null) {
            return null;
        }else {
            return function.apply(object);
        }
    }

    public static ObjectNode getObjectNode() {
        return mapper.createObjectNode();
    }
}
