package json;

import javax.json.*;

/**
 * Created by Basil on 26/12/2015.
 */
public class JsonUtils {

    /**
     * http://docs.oracle.com/middleware/1213/wls/WLPRG/java-api-for-json-proc.htm#WLPRG1061
     * @param tree
     * @param key
     */
    public static Object navigateTree(JsonValue tree, String key) {

        Object ret = null;

        if (key != null)
            System.out.print("Key " + key + ": ");

        switch(tree.getValueType()) {
            case OBJECT:
                System.out.println("OBJECT");
                JsonObject object = (JsonObject) tree;

                for (String name : object.keySet())
                    navigateTree(object.get(name), name);
                break;
            case ARRAY:
                System.out.println("ARRAY");
                JsonArray array = (JsonArray) tree;
                for (JsonValue val : array)
                    navigateTree(val, null);
                break;
            case STRING:
                JsonString st = (JsonString) tree;
                System.out.println("STRING " + st.getString());
                break;
            case NUMBER:
                JsonNumber num = (JsonNumber) tree;
                System.out.println("NUMBER " + num.toString());
                break;
            case TRUE:
                System.out.println("TRUE");
            case FALSE:
                System.out.println("FALSE");
            case NULL:
                System.out.println(tree.getValueType().toString());
                break;
        }

        return ret;

    }

}
