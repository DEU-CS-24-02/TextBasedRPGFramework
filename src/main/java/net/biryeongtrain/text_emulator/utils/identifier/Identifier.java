package net.biryeongtrain.text_emulator.utils.identifier;

import com.google.gson.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.biryeongtrain.text_emulator.utils.JsonHelper;

import java.lang.reflect.Type;
import java.util.function.UnaryOperator;

// Imported Code in Identifier class from yarn
public class Identifier implements Comparable<Identifier>{
    public static final Codec<Identifier> CODEC = Codec.STRING.comapFlatMap(Identifier::validate, Identifier::toString).stable();

    private final String namespace;
    private final String path;
    public static final char NAMESPACE_SEPARATOR = ':';
    public static final String DEFAULT_NAMESPACE = "emulator";

    private Identifier(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
    }

    private static Identifier ofValidated(String namespace, String path) {
        return new Identifier(Identifier.validateNamespace(namespace, path), Identifier.validatePath(namespace, path));
    }

    public static Identifier of(String namespace, String path) {
        return Identifier.ofValidated(namespace, path);
    }

    public static Identifier of(String id) {
        return Identifier.splitOn(id, ':');
    }

    public static Identifier ofDefault(String path) {
        return new Identifier(DEFAULT_NAMESPACE, Identifier.validatePath(DEFAULT_NAMESPACE, path));
    }

    private static String validatePath(String namespace, String path) {
        if (!Identifier.isPathValid(path)) {
            throw new InvalidIdentifierException("Non [a-z0-9/._-] character in path of location: " + namespace + ":" + path);
        }
        return path;
    }

    public Identifier withPath(String path) {
        return new Identifier(this.namespace, Identifier.validatePath(this.namespace, path));
    }

    public Identifier withPrefixedPath(String prefix) {
        return this.withPath(prefix + this.path);
    }

    public Identifier withSuffixedPath(String suffix) {
        return this.withPath(this.path + suffix);
    }

    public String toString() {
        return this.namespace + ":" + this.path;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Identifier) {
            Identifier identifier = (Identifier)o;
            return this.namespace.equals(identifier.namespace) && this.path.equals(identifier.path);
        }
        return false;
    }

    public int hashCode() {
        return 31 * this.namespace.hashCode() + this.path.hashCode();
    }

    public String getNamespace() {
        return namespace;
    }

    public static DataResult<Identifier> validate(String id) {
        try {
            return DataResult.success(Identifier.of(id));
        } catch (InvalidIdentifierException invalidIdentifierException) {
            return DataResult.error(() -> "Not a valid resource location: " + id + " " + invalidIdentifierException.getMessage());
        }
    }

    public String getPath() {
        return path;
    }

    public Identifier withPath(UnaryOperator<String> pathFunction) {
        return this.withPath((String)pathFunction.apply(this.path));
    }

    public static Identifier splitOn(String id, char delimiter) {
        int i = id.indexOf(delimiter);
        if (i >= 0) {
            String string = id.substring(i + 1);
            if (i != 0) {
                String string2 = id.substring(0, i);
                return Identifier.ofValidated(string2, string);
            }
            return Identifier.ofDefault(string);
        }
        return Identifier.ofDefault(id);
    }


    public static boolean isNamespaceValid(String namespace) {
        for (int i = 0; i < namespace.length(); ++i) {
            if (Identifier.isNamespaceCharacterValid(namespace.charAt(i))) continue;
            return false;
        }
        return true;
    }

    private static String validateNamespace(String namespace, String path) {
        if (!Identifier.isNamespaceValid(namespace)) {
            throw new InvalidIdentifierException("Non [a-z0-9_.-] character in namespace of location: " + namespace + ":" + path);
        }
        return namespace;
    }

    public static boolean isPathCharacterValid(char character) {
        return character == '_' || character == '-' || character >= 'a' && character <= 'z' || character >= '0' && character <= '9' || character == '/' || character == '.';
    }

    /**
     * {@return whether {@code character} is valid for use in identifier namespaces}
     */
    private static boolean isNamespaceCharacterValid(char character) {
        return character == '_' || character == '-' || character >= 'a' && character <= 'z' || character >= '0' && character <= '9' || character == '.';
    }

    public static boolean isCharValid(char c) {
        return c >= '0' && c <= '9' || c >= 'a' && c <= 'z' || c == '_' || c == ':' || c == '/' || c == '.' || c == '-';
    }

    /**
     * {@return whether {@code path} can be used as an identifier's path}
     */
    public static boolean isPathValid(String path) {
        for (int i = 0; i < path.length(); ++i) {
            if (Identifier.isPathCharacterValid(path.charAt(i))) continue;
            return false;
        }
        return true;
    }


    @Override
    public int compareTo(Identifier identifier) {
        int i = this.path.compareTo(identifier.path);
        if (i == 0) {
            i = this.namespace.compareTo(identifier.namespace);
        }
        return i;
    }

    public static class Serializer
            implements JsonDeserializer<Identifier>,
            JsonSerializer<Identifier> {
        @Override
        public Identifier deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return Identifier.of(JsonHelper.asString(jsonElement, "location"));
        }

        @Override
        public JsonElement serialize(Identifier identifier, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(identifier.toString());
        }
    }
}
