package net.biryeongtrain.text_emulator.utils;

import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nullable;

@SuppressWarnings({"unused"})
public class JsonHelper {
    private static final Gson GSON = new GsonBuilder().create();

    public static boolean hasString(JsonObject object, String element) {
        if (!JsonHelper.hasPrimitive(object, element)) {
            return false;
        }
        return object.getAsJsonPrimitive(element).isString();
    }

    public static boolean isString(JsonElement element) {
        if (!element.isJsonPrimitive()) {
            return false;
        }
        return element.getAsJsonPrimitive().isString();
    }

    public static boolean hasNumber(JsonObject object, String element) {
        if (!JsonHelper.hasPrimitive(object, element)) {
            return false;
        }
        return object.getAsJsonPrimitive(element).isNumber();
    }

    public static boolean isNumber(JsonElement element) {
        if (!element.isJsonPrimitive()) {
            return false;
        }
        return element.getAsJsonPrimitive().isNumber();
    }

    public static boolean hasBoolean(JsonObject object, String element) {
        if (!JsonHelper.hasPrimitive(object, element)) {
            return false;
        }
        return object.getAsJsonPrimitive(element).isBoolean();
    }

    public static boolean isBoolean(JsonElement object) {
        if (!object.isJsonPrimitive()) {
            return false;
        }
        return object.getAsJsonPrimitive().isBoolean();
    }

    public static boolean hasArray(JsonObject object, String element) {
        if (!JsonHelper.hasElement(object, element)) {
            return false;
        }
        return object.get(element).isJsonArray();
    }

    public static boolean hasJsonObject(JsonObject object, String element) {
        if (!JsonHelper.hasElement(object, element)) {
            return false;
        }
        return object.get(element).isJsonObject();
    }

    public static boolean hasPrimitive(JsonObject object, String element) {
        if (!JsonHelper.hasElement(object, element)) {
            return false;
        }
        return object.get(element).isJsonPrimitive();
    }

    public static boolean hasElement(@Nullable JsonObject object, String element) {
        if (object == null) {
            return false;
        }
        return object.get(element) != null;
    }

    public static JsonElement getElement(JsonObject object, String name) {
        JsonElement jsonElement = object.get(name);
        if (jsonElement == null || jsonElement.isJsonNull()) {
            throw new JsonSyntaxException("Missing field " + name);
        }
        return jsonElement;
    }

    public static String asString(JsonElement element, String name) {
        if (element.isJsonPrimitive()) {
            return element.getAsString();
        }
        throw new JsonSyntaxException("Expected " + name + " to be a string, was " + JsonHelper.getType(element));
    }

    public static String getString(JsonObject object, String element) {
        if (object.has(element)) {
            return JsonHelper.asString(object.get(element), element);
        }
        throw new JsonSyntaxException("Missing " + element + ", expected to find a string");
    }

    @Nullable
    @Contract(value="_,_,!null->!null;_,_,null->_")
    public static String getString(JsonObject object, String element, @Nullable String defaultStr) {
        if (object.has(element)) {
            return JsonHelper.asString(object.get(element), element);
        }
        return defaultStr;
    }

    public static boolean asBoolean(JsonElement element, String name) {
        if (element.isJsonPrimitive()) {
            return element.getAsBoolean();
        }
        throw new JsonSyntaxException("Expected " + name + " to be a Boolean, was " + JsonHelper.getType(element));
    }

    public static boolean getBoolean(JsonObject object, String element) {
        if (object.has(element)) {
            return JsonHelper.asBoolean(object.get(element), element);
        }
        throw new JsonSyntaxException("Missing " + element + ", expected to find a Boolean");
    }

    public static boolean getBoolean(JsonObject object, String element, boolean defaultBoolean) {
        if (object.has(element)) {
            return JsonHelper.asBoolean(object.get(element), element);
        }
        return defaultBoolean;
    }

    public static double asDouble(JsonElement object, String name) {
        if (object.isJsonPrimitive() && object.getAsJsonPrimitive().isNumber()) {
            return object.getAsDouble();
        }
        throw new JsonSyntaxException("Expected " + name + " to be a Double, was " + JsonHelper.getType(object));
    }

    public static double getDouble(JsonObject object, String element) {
        if (object.has(element)) {
            return JsonHelper.asDouble(object.get(element), element);
        }
        throw new JsonSyntaxException("Missing " + element + ", expected to find a Double");
    }

    public static double getDouble(JsonObject object, String element, double defaultDouble) {
        if (object.has(element)) {
            return JsonHelper.asDouble(object.get(element), element);
        }
        return defaultDouble;
    }

    public static float asFloat(JsonElement element, String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsFloat();
        }
        throw new JsonSyntaxException("Expected " + name + " to be a Float, was " + JsonHelper.getType(element));
    }

    public static float getFloat(JsonObject object, String element) {
        if (object.has(element)) {
            return JsonHelper.asFloat(object.get(element), element);
        }
        throw new JsonSyntaxException("Missing " + element + ", expected to find a Float");
    }

    public static float getFloat(JsonObject object, String element, float defaultFloat) {
        if (object.has(element)) {
            return JsonHelper.asFloat(object.get(element), element);
        }
        return defaultFloat;
    }

    public static long asLong(JsonElement element, String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsLong();
        }
        throw new JsonSyntaxException("Expected " + name + " to be a Long, was " + JsonHelper.getType(element));
    }

    public static long getLong(JsonObject object, String name) {
        if (object.has(name)) {
            return JsonHelper.asLong(object.get(name), name);
        }
        throw new JsonSyntaxException("Missing " + name + ", expected to find a Long");
    }

    public static long getLong(JsonObject object, String element, long defaultLong) {
        if (object.has(element)) {
            return JsonHelper.asLong(object.get(element), element);
        }
        return defaultLong;
    }

    public static int asInt(JsonElement element, String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsInt();
        }
        throw new JsonSyntaxException("Expected " + name + " to be a Int, was " + JsonHelper.getType(element));
    }

    public static int getInt(JsonObject object, String element) {
        if (object.has(element)) {
            return JsonHelper.asInt(object.get(element), element);
        }
        throw new JsonSyntaxException("Missing " + element + ", expected to find a Int");
    }

    public static int getInt(JsonObject object, String element, int defaultInt) {
        if (object.has(element)) {
            return JsonHelper.asInt(object.get(element), element);
        }
        return defaultInt;
    }

    public static byte asByte(JsonElement element, String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsByte();
        }
        throw new JsonSyntaxException("Expected " + name + " to be a Byte, was " + JsonHelper.getType(element));
    }

    public static byte getByte(JsonObject object, String element) {
        if (object.has(element)) {
            return JsonHelper.asByte(object.get(element), element);
        }
        throw new JsonSyntaxException("Missing " + element + ", expected to find a Byte");
    }

    public static byte getByte(JsonObject object, String element, byte defaultByte) {
        if (object.has(element)) {
            return JsonHelper.asByte(object.get(element), element);
        }
        return defaultByte;
    }

    public static char asChar(JsonElement element, String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsCharacter();
        }
        throw new JsonSyntaxException("Expected " + name + " to be a Character, was " + JsonHelper.getType(element));
    }

    public static char getChar(JsonObject object, String element) {
        if (object.has(element)) {
            return JsonHelper.asChar(object.get(element), element);
        }
        throw new JsonSyntaxException("Missing " + element + ", expected to find a Character");
    }

    public static char getChar(JsonObject object, String element, char defaultChar) {
        if (object.has(element)) {
            return JsonHelper.asChar(object.get(element), element);
        }
        return defaultChar;
    }

    public static String getType(@org.jetbrains.annotations.Nullable JsonElement element) {
        String string = StringUtils.abbreviateMiddle(String.valueOf(element), "...", 10);
        if (element == null) {
            return "null (missing)";
        }
        if (element.isJsonNull()) {
            return "null (json)";
        }
        if (element.isJsonArray()) {
            return "an array (" + string + ")";
        }
        if (element.isJsonObject()) {
            return "an object (" + string + ")";
        }
        if (element.isJsonPrimitive()) {
            JsonPrimitive jsonPrimitive = element.getAsJsonPrimitive();
            if (jsonPrimitive.isNumber()) {
                return "a number (" + string + ")";
            }
            if (jsonPrimitive.isBoolean()) {
                return "a boolean (" + string + ")";
            }
        }
        return string;
    }
}
