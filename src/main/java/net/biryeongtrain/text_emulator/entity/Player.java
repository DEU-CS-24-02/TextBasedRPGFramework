package net.biryeongtrain.text_emulator.entity;

import net.biryeongtrain.text_emulator.item.ItemStack;
import net.biryeongtrain.text_emulator.level.Scene;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.registry.RegistryKey;
import net.biryeongtrain.text_emulator.utils.Clearable;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Player extends Entity implements Inventory, Clearable {
    private float gold; // 플레이어의 돈
    private float reputation; // 플레이어의 명성
    private final ItemStack[] stacks; // 인벤토리 (아이템 스택 배열)
    private final int maxSize; // 인벤토리 최대 슬롯 수
    private static final int DEFAULT_INVENTORY_SIZE = 30; // 기본 인벤토리 크기

    // 기본 생성자
    public Player(RegistryKey<EntityType> typeKey, LootTableManager lootTableManager, Scene scene) {
        super(Registries.ENTITY_TYPE.get(typeKey), lootTableManager, scene); // Entity 생성자 호출
        this.gold = 0; // 초기 금액 설정
        this.reputation = 0; // 초기 명성 설정
        this.maxSize = DEFAULT_INVENTORY_SIZE; // 기본 인벤토리 크기 설정
        this.stacks = new ItemStack[maxSize]; // 인벤토리 초기화
    }

    // 음수 검사 메서드
    private void checkNonNegative(float amount, String message) {
        if (amount < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    // 돈 관련 메서드
    public float getGold() {
        return gold;
    }

    public void addGold(float amount) {
        checkNonNegative(amount, "금액은 음수가 될 수 없습니다."); // 음수 검사
        this.gold += amount;
        System.out.printf("현재 금액: %.2f%n", this.gold);
    }

    public boolean spendGold(float amount) {
        checkNonNegative(amount, "지출할 금액은 음수가 될 수 없습니다."); // 음수 검사
        if (amount <= gold) {
            this.gold -= amount;
            System.out.printf("금액을 지불했습니다: %.2f. 남은 금액: %.2f%n", amount, this.gold);
            return true;
        } else {
            System.out.println("금액이 부족합니다.");
            return false;
        }
    }

    // 명성 관련 메서드
    public float getReputation() {
        return reputation;
    }

    public void increaseReputation(float amount) {
        checkNonNegative(amount, "명성은 음수가 될 수 없습니다."); // 음수 검사
        this.reputation += amount;
        System.out.printf("명성이 증가했습니다: %.2f. 현재 명성: %.2f%n", amount, this.reputation);
    }

    public void decreaseReputation(float amount) {
        checkNonNegative(amount, "명성은 음수로 감소할 수 없습니다."); // 음수 검사
        this.reputation -= amount;
        System.out.printf("명성이 감소했습니다: %.2f. 현재 명성: %.2f%n", amount, this.reputation);
    }

    // 인벤토리 관련 메서드
    @Override
    public int size() {
        return maxSize; // 인벤토리의 슬롯 수 반환
    }

    @Override
    public boolean isEmpty() {
        return Arrays.stream(stacks).allMatch(stack -> stack == null || stack.isEmpty());
    }

    @Override
    public ItemStack getStack(int slot) {
        if (slot < 0 || slot >= size()) {
            throw new IndexOutOfBoundsException("Invalid slot index: " + slot);
        }
        return stacks[slot]; // 지정된 슬롯의 아이템 스택 반환
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack stack = getStack(slot);
        stacks[slot] = null; // 해당 슬롯 비우기
        return stack; // 제거한 아이템 스택 반환
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack stack = getStack(slot);
        if (stack != null && stack.getCount() >= amount) {
            for (int i = 0; i < amount; i++) {
                stack.shrink(); // 아이템 수량 감소
            }
            if (stack.isEmpty()) {
                stacks[slot] = null; // 수량이 0이면 슬롯 비우기
            }
            return new ItemStack(stack.getItem(), amount); // 제거한 아이템 스택 반환
        } else {
            throw new IllegalArgumentException("Not enough items to remove.");
        }
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (slot < 0 || slot >= size()) {
            throw new IndexOutOfBoundsException("Invalid slot index: " + slot);
        }
        if (stack.getCount() > getMaxCountPerStack()) {
            throw new IllegalArgumentException("Stack size exceeds maximum allowed.");
        }
        stacks[slot] = stack; // 지정된 슬롯에 아이템 스택 설정
    }
    public void dropItem(LootTableManager lootTableManager, Scene scene) {
        // LootTableManager를 통해 적절한 LootTableInstance를 가져옴
        LootTableInstance lootTable = lootTableManager.getLootTable(scene); // scene을 매개변수로 전달

        // LootTableInstance에서 아이템 목록을 가져옴
        List<ItemStack> items = lootTable.items();

        // 아이템 목록에서 드롭할 아이템을 선택
        if (!items.isEmpty()) {
            // 랜덤으로 아이템 선택
            Random random = scene.getRandom();
            ItemStack droppedItem = items.get(random.nextInt(items.size())); // 랜덤으로 아이템 선택

            // 아이템의 키를 가져옴
            String itemKey = Registries.ITEM.getKey(droppedItem.getItem()).toString(); // 아이템의 키를 가져옴

            // 아이템 드롭 처리 로직 (스윙에서 나타내야 하므로 출력하지 않음)
            // 예: scene.addItem(droppedItem); // 아이템을 씬에 추가하는 로직 필요

            // 드롭된 아이템의 키를 사용하여 다른 로직을 구현할 수 있습니다.
            // 필요에 따라 itemKey를 활용할 수 있습니다.
        } else {
            // 드롭할 아이템이 없을 경우 처리 (출력하지 않음)
        }
    }






    // Clearable 인터페이스의 메서드 구현
    @Override
    public void clear() {
        clearGold();
        clearReputation();
        clearInventory();
        System.out.println("플레이어의 상태가 초기화되었습니다.");
    }

    // 각 속성 초기화 메서드
    public void clearGold() {
        this.gold = 0; // 금액 초기화
        System.out.println("금액이 초기화되었습니다.");
    }

    public void clearReputation() {
        this.reputation = 0; // 명성 초기화
        System.out.println("명성이 초기화되었습니다.");
    }

    public void clearInventory() {
        Arrays.fill(stacks, null); // 인벤토리 비우기
        System.out.println("인벤토리가 초기화되었습니다.");
    }

    @Override
    public int getMaxCountPerStack() {
        return 99; // 최대 스택 크기 반환
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return true; // 슬롯의 유효성 검사 (추가 로직 필요 시 수정)
    }
}
