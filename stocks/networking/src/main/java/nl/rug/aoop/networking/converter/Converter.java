package nl.rug.aoop.networking.converter;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Class used for converting objects to and from Json strings.
 */
public class Converter {
    private static final Gson GSON = new Gson();

    /**
     * Creates a Json string of an object.
     *
     * @param object object to be converted.
     * @param <T>    Class of object to be converted.
     * @return Converted Json String.
     */
    public static <T> String toJson(T object) {
        return GSON.toJson(object);
    }

    /**
     * Creates an object according to a Json String.
     *
     * @param json      String which holds the info of the object.
     * @param className name of the class of the object to be created.
     * @param <T>       class of created object.
     * @return the created object.
     */
    public static <T> T fromJson(String json, Class<T> className) throws JsonSyntaxException {
        return GSON.fromJson(json, className);
    }
}
