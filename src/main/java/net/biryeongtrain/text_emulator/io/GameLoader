//제가 방향성을 잘 못잡아서 일단 gpt에다가 처음에는 해보고 뜯어보고 해봤는데 아니라면 다시 하겠습니다.

package net.biryeongtrain.text_emulator.io;

import com.google.gson.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

// Serializable 인터페이스 정의
public interface Serializable<T> {
    Codec<T> getCodec();
    T deserialize(JsonElement element);
    JsonElement toJson();  // 추가된 부분
    void save(String filePath);
    T load(String filePath);
}

// 신 클래스 정의
public class God implements Serializable<God> {
    private String name;
    private int powerLevel;

    // Codec 정의
    public static final Codec<God> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(God::getName),
            Codec.INT.fieldOf("powerLevel").forGetter(God::getPowerLevel)
    ).apply(instance, God::new));

    // 생성자
    public God(String name, int powerLevel) {
        this.name = name;
        this.powerLevel = powerLevel;
    }

    // Getter
    public String getName() {
        return name;
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    // JsonElement를 통해 역직렬화 처리
    @Override
    public God deserialize(JsonElement element) {
        JsonObject jsonObject = element.getAsJsonObject();
        this.name = jsonObject.get("name").getAsString();
        this.powerLevel = jsonObject.get("powerLevel").getAsInt();
        return this;
    }

    // 객체를 JSON으로 직렬화
    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", this.name);
        jsonObject.addProperty("powerLevel", this.powerLevel);
        return jsonObject;
    }

    @Override
    public Codec<God> getCodec() {
        return CODEC;
    }

    // 세이브 메소드
    @Override
    public void save(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(toJson().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 로드 메소드
    @Override
    public God load(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JsonElement jsonElement = JsonParser.parseString(content);
            return deserialize(jsonElement);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

// Item 클래스 정의
public class Item implements Serializable<Item> {
    private String name;

    // 생성자
    public Item(String name) {
        this.name = name;
    }

    // Getter
    public String getName() {
        return name;
    }

    // 역직렬화 처리
    @Override
    public Item deserialize(JsonElement element) {
        JsonObject jsonObject = element.getAsJsonObject();
        this.name = jsonObject.get("name").getAsString();
        return this;
    }

    // 직렬화 처리
    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", this.name);
        return jsonObject;
    }

    @Override
    public Codec<Item> getCodec() {
        // Codec 설정을 원하는 경우 추가 가능
        return null;
    }

    @Override
    public void save(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(toJson().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Item load(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JsonElement jsonElement = JsonParser.parseString(content);
            return deserialize(jsonElement);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

// Entity 클래스 정의
public class Entity implements Serializable<Entity> {
    private String name;

    // 생성자
    public Entity(String name) {
        this.name = name;
    }

    // Getter
    public String getName() {
        return name;
    }

    // 역직렬화 처리
    @Override
    public Entity deserialize(JsonElement element) {
        JsonObject jsonObject = element.getAsJsonObject();
        this.name = jsonObject.get("name").getAsString();
        return this;
    }

    // 직렬화 처리
    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", this.name);
        return jsonObject;
    }

    @Override
    public Codec<Entity> getCodec() {
        // Codec 설정을 원하는 경우 추가 가능
        return null;
    }

    @Override
    public void save(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(toJson().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Entity load(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JsonElement jsonElement = JsonParser.parseString(content);
            return deserialize(jsonElement);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

// Scenario 클래스 정의
public class Scenario implements Serializable<Scenario> {
    private String name;

    // 생성자
    public Scenario(String name) {
        this.name = name;
    }

    // Getter
    public String getName() {
        return name;
    }

    // 역직렬화 처리
    @Override
    public Scenario deserialize(JsonElement element) {
        JsonObject jsonObject = element.getAsJsonObject();
        this.name = jsonObject.get("name").getAsString();
        return this;
    }

    // 직렬화 처리
    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", this.name);
        return jsonObject;
    }

    @Override
    public Codec<Scenario> getCodec() {
        // Codec 설정을 원하는 경우 추가 가능
        return null;
    }

    @Override
    public void save(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(toJson().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Scenario load(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JsonElement jsonElement = JsonParser.parseString(content);
            return deserialize(jsonElement);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

// 아이템 및 엔티티 로더를 비동기로 처리
public class AsyncLoader {

    // 아이템 로드
    public static CompletableFuture<Item> loadItemAsync(String filePath) {
        return CompletableFuture.supplyAsync(() -> {
            try (FileReader reader = new FileReader(filePath)) {
                JsonElement element = JsonParser.parseReader(reader);
                Item item = new Item(""); // 빈 아이템 생성 후 deserialize 호출
                return item.deserialize(element); // 역직렬화 후 반환
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    // 엔티티 로드
    public static CompletableFuture<Entity> loadEntityAsync(String filePath) {
        return CompletableFuture.supplyAsync(() -> {
            try (FileReader reader = new FileReader(filePath)) {
                JsonElement element = JsonParser.parseReader(reader);
                Entity entity = new Entity(""); // 빈 엔티티 생성 후 deserialize 호출
                return entity.deserialize(element); // 역직렬화 후 반환
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    // 시나리오 로드
    public static CompletableFuture<Scenario> loadScenarioAsync(String filePath) {
        return CompletableFuture.supplyAsync(() -> {
            try (FileReader reader = new FileReader(filePath)) {
                JsonElement element = JsonParser.parseReader(reader);
                Scenario scenario = new Scenario(""); // 빈 시나리오 생성 후 deserialize 호출
                return scenario.deserialize(element); // 역직렬화 후 반환
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }
}

// 메인 클래스
public class GameLoader {
    public static void main(String[] args) {
        // 신 객체 생성 및 JSON 저장
        God god = new God("Zeus", 100);
        god.save("god.json");

        // 아이템 비동기 로드
        AsyncLoader.loadItemAsync("item.json").thenAccept(item -> {
            if (item != null) {
                System.out.println("Item loaded: " + item.getName());
            }
        });

        // 엔티티 비동기 로드
        AsyncLoader.loadEntityAsync("entity.json").thenAccept(entity -> {
            if (entity != null) {
                System.out.println("Entity loaded: " + entity.getName());
            }
        });

        // 시나리오 비동기 로드
        AsyncLoader.loadScenarioAsync("scenario.json").thenAccept(scenario -> {
            if (scenario != null) {
                System.out.println("Scenario loaded: " + scenario.getName());
            }
        });
    }
}
