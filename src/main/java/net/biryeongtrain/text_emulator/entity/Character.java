package net.biryeongtrain.text_emulator.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Character implements Entity {
    private final Identifier identifier; // 고유 식별자
    private final int commentId; // 시스템 ID
    private final String commentName; // 사용자 이름
    private final double health; // 체력
    private final double attackPower; // 공격력
    private final double defense; // 방어력
    private final String description; // 설명
    private final String commentType; // 타입
    private final String type; // 엔티티 타입
    private final String tags; // 태그

    private final Map<Slot, Inventory> equippedItems; // 장착된 아이템

    // 생성자
    public Character(Identifier identifier, int commentId, String commentName, double health, double attackPower,
                     double defense, String description, String commentType, String type, String tags) {
        this.identifier = identifier;
        this.commentId = commentId;
        this.commentName = commentName;
        this.health = health;
        this.attackPower = attackPower;
        this.defense = defense;
        this.description = description;
        this.commentType = commentType;
        this.type = type;
        this.tags = tags;
        this.equippedItems = new HashMap<>();
    }

    @Override
    public Identifier getIdentifier() {
        return identifier; // 식별자 반환
    }

    @Override
    public int getCommentId() {
        return commentId;
    }

    @Override
    public String getCommentName() {
        return commentName;
    }

    @Override
    public double getHealth() {
        return health;
    }

    @Override
    public double getAttackPower() {
        return attackPower;
    }

    @Override
    public double getDefense() {
        return defense;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getCommentType() {
        return commentType;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getTags() {
        return tags;
    }



    @Override
    public Entity deserialize(JsonObject jsonObject) {
        Identifier identifier = Identifier.of(jsonObject.get("identifier").getAsString());
        int commentId = jsonObject.get("commentId").getAsInt();
        String commentName = jsonObject.get("commentName").getAsString();
        double health = jsonObject.get("health").getAsDouble();
        double attackPower = jsonObject.get("attackPower").getAsDouble();
        double defense = jsonObject.get("defense").getAsDouble();
        String description = jsonObject.get("description").getAsString();
        String commentType = jsonObject.get("commentType").getAsString();
        String type = jsonObject.get("type").getAsString();
        String tags = jsonObject.get("tags").getAsString();

        return new Character(identifier, commentId, commentName, health, attackPower, defense, description, commentType, type, tags);
    }
    @Override
    public JsonElement serializeToJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("identifier", identifier.toString());
        jsonObject.addProperty("commentId", commentId);
        jsonObject.addProperty("commentName", commentName);
        jsonObject.addProperty("health", health);
        jsonObject.addProperty("attackPower", attackPower);
        jsonObject.addProperty("defense", defense);
        jsonObject.addProperty("description", description);
        jsonObject.addProperty("commentType", commentType);
        jsonObject.addProperty("type", type);
        jsonObject.addProperty("tags", tags);
        return jsonObject;
    }
    @Override
    public void equip(Slot slot, Inventory inventory) {
        equippedItems.put(slot, inventory);
    }

    @Override
    public void unequip(Slot slot) {
        equippedItems.remove(slot);
    }

    @Override
    public Inventory getEquippedItem(Slot slot) {
        return equippedItems.get(slot);
    }

    @Override
    public Map<Slot, Inventory> getEquippedItems() {
        return new HashMap<>(equippedItems);
    }

    // 사용자 입력을 통해 Character 객체 생성
    public static Character createCharacterFromInput() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("식별자 (예: namespace:path): ");
        String namespaceInput = scanner.nextLine();
        String pathInput = scanner.nextLine();
        Identifier identifier = Identifier.of(namespaceInput, pathInput); // 식별자 생성
        // 주어진 네임스페이스와 경로로 유효한 Identifier를 생성합니다.

        System.out.print("시스템 ID (정수): ");
        int commentId = Integer.parseInt(scanner.nextLine());

        System.out.print("사용자 이름: ");
        String commentName = scanner.nextLine();

        System.out.print("체력: ");
        double health = Double.parseDouble(scanner.nextLine());

        System.out.print("공격력: ");
        double attackPower = Double.parseDouble(scanner.nextLine());

        System.out.print("방어력: ");
        double defense = Double.parseDouble(scanner.nextLine());

        System.out.print("설명: ");
        String description = scanner.nextLine();

        System.out.print("타입 (예: hostile, neutral, friendly): ");
        String commentType = scanner.nextLine();

        System.out.print("엔티티 타입: ");
        String type = scanner.nextLine();

        System.out.print("태그 (쉼표로 구분): ");
        String tags = scanner.nextLine();

        // Character 객체 생성 후 반환
        return new Character(identifier, commentId, commentName, health, attackPower, defense, description, commentType, type, tags);
    }
}
