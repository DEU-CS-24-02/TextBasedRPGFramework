package net.biryeongtrain.text_emulator;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ConditionalTextRPG extends JFrame {
    private JTextArea textArea; // 텍스트 출력 영역
    private JButton nextButton; // 다음 씬 버튼
    private Map<String, Scene> scenes; // 씬 데이터 저장
    private Set<String> inventory; // 플레이어가 소유한 아이템
    private Set<String> visitedRandomScenes; // 방문한 랜덤 씬
    private String currentScene; // 현재 씬 이름

    public ConditionalTextRPG() {
        // 창 설정
        setTitle("Conditional Text RPG");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 텍스트 출력 영역
        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        // 버튼
        nextButton = new JButton("Next Scene");
        nextButton.addActionListener(e -> moveToNextScene());
        add(nextButton, BorderLayout.SOUTH);

        // 데이터 초기화
        inventory = new HashSet<>(); // 플레이어의 아이템 목록
        visitedRandomScenes = new HashSet<>(); // 방문한 랜덤 씬 목록
        initializeScenes();
        currentScene = "start"; // 첫 번째 씬 설정

        // 첫 번째 씬 출력
        displayTextWithEffect(scenes.get(currentScene).getDescription());

        // 창 크기 설정 및 표시
        setSize(600, 400);
        setVisible(true);
    }

    // 씬 데이터 초기화
    private void initializeScenes() {
        scenes = new HashMap<>();

        // 고정 전환 씬
        scenes.put("start", new Scene("start", "You are at the start of your journey.", "forest", null, null));
        scenes.put("forest", new Scene("forest", "You enter a dark forest and find a mysterious key.", "village", "key", null));

        // 조건부 전환 씬 (key가 필요)
        scenes.put("village", new Scene("village",
                "You arrive at a bustling village. The gatekeeper will only let you pass if you have the key.",
                "castle", null, "key"));

        // 랜덤 전환 씬
        scenes.put("castle", new Scene("castle",
                "You enter the grand castle. Where will you explore?",
                null, null, null, Arrays.asList("throne_room", "dungeon", "garden", "library", "armory")));
        scenes.put("garden", new Scene("garden", "A peaceful garden with blooming flowers.", null, null, null, Arrays.asList("throne_room", "dungeon")));
        scenes.put("library", new Scene("library", "An ancient library filled with dusty tomes.", null, null, null, Arrays.asList("throne_room", "dungeon")));
        scenes.put("armory", new Scene("armory", "A room full of weapons and armor.", null, null, null, Arrays.asList("throne_room", "dungeon")));

        // 고정 종결 씬
        scenes.put("throne_room", new Scene("throne_room", "You are in the throne room. The king grants you a reward.", "end", "golden_crown", null));
        scenes.put("dungeon", new Scene("dungeon", "You find treasure in the dungeon.", "end", "treasure", null));
        scenes.put("end", new Scene("end", "Your journey ends here. Thank you for playing!", null, null, null));
    }

    // 다음 씬으로 이동
    private void moveToNextScene() {
        if (!nextButton.isEnabled()) {
            return;
        }

        Scene scene = scenes.get(currentScene);

        if (scene == null) {
            displayTextWithEffect("No more scenes. The adventure ends here!");
            nextButton.setEnabled(false); // 마지막 씬에서는 버튼 비활성화
            return;
        }

        // **엔드 씬 처리**: 엔드 씬에서 버튼 클릭 시 프로그램 종료
        if ("end".equals(currentScene)) {
            displayTextWithEffect("The game has ended. Goodbye!");
            Timer exitTimer = new Timer(2000, e -> System.exit(0)); // 2초 후 프로그램 종료
            exitTimer.setRepeats(false);
            exitTimer.start();
            nextButton.setEnabled(false); // 버튼 비활성화
            return;
        }

        // 랜덤 씬 방문 확인
        if (Arrays.asList("garden", "library", "armory", "throne_room", "dungeon").contains(currentScene)) {
            visitedRandomScenes.add(currentScene);
            System.out.println("Visited random scenes: " + visitedRandomScenes);
        }

        // **모든 랜덤 씬 방문 시 end로 전환**
        if (visitedRandomScenes.containsAll(Arrays.asList("garden", "library", "armory", "throne_room", "dungeon"))) {
            currentScene = "end";
            displayTextWithEffect(scenes.get(currentScene).getDescription());
            return;
        }

        // 아이템 획득 처리
        if (scene.getItemToGain() != null) {
            String item = scene.getItemToGain();
            inventory.add(item); // 아이템을 인벤토리에 추가
            displayTextWithEffect("You gained: " + item);

            // **씬 전환을 계속 진행하도록 설정**
            currentScene = scene.getNextScene();
            return;
        }

        // 조건부 전환 처리
        if (scene.getRequiredItem() != null && !inventory.contains(scene.getRequiredItem())) {
            displayTextWithEffect("You need " + scene.getRequiredItem() + " to proceed.");
            return; // 조건을 만족하지 못하면 씬 전환하지 않음
        }

        // 랜덤 전환 처리
        if (scene.getRandomOptions() != null) {
            List<String> options = scene.getRandomOptions();
            Random random = new Random();
            currentScene = options.get(random.nextInt(options.size())); // 랜덤으로 다음 씬 선택
        } else {
            // 고정 전환 처리
            currentScene = scene.getNextScene();
        }

        // 새로운 씬 출력
        if (currentScene == null) {
            displayTextWithEffect("The adventure has ended.");
            nextButton.setEnabled(false);
        } else {
            displayTextWithEffect(scenes.get(currentScene).getDescription());
        }
    }

    // 텍스트를 한 글자씩 출력
    private void displayTextWithEffect(String text) {
        textArea.setText(""); // 기존 텍스트 삭제
        nextButton.setEnabled(false); // 버튼 비활성화
        Timer timer = new Timer(50, null); // 50ms 간격으로 글자 출력
        char[] chars = text.toCharArray(); // 텍스트를 문자 배열로 변환
        final int[] index = {0}; // 현재 출력할 글자의 인덱스

        timer.addActionListener(e -> {
            if (index[0] < chars.length) {
                textArea.append(String.valueOf(chars[index[0]++])); // 한 글자씩 출력
            } else {
                timer.stop(); // 모든 글자를 출력하면 타이머 중지
                nextButton.setEnabled(true); // 버튼 활성화
            }
        });

        timer.start(); // 타이머 시작
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ConditionalTextRPG::new);
    }
}

// Scene 클래스: 씬 데이터를 저장
class Scene {
    private final String name;
    private final String description;
    private final String nextScene;
    private final String itemToGain;
    private final String requiredItem;
    private final List<String> randomOptions;

    public Scene(String name, String description, String nextScene, String itemToGain, String requiredItem) {
        this.name = name;
        this.description = description;
        this.nextScene = nextScene;
        this.itemToGain = itemToGain;
        this.requiredItem = requiredItem;
        this.randomOptions = null;
    }

    public Scene(String name, String description, String nextScene, String itemToGain, String requiredItem, List<String> randomOptions) {
        this.name = name;
        this.description = description;
        this.nextScene = nextScene;
        this.itemToGain = itemToGain;
        this.requiredItem = requiredItem;
        this.randomOptions = randomOptions;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getNextScene() {
        return nextScene;
    }

    public String getItemToGain() {
        return itemToGain;
    }

    public String getRequiredItem() {
        return requiredItem;
    }

    public List<String> getRandomOptions() {
        return randomOptions;
    }
}