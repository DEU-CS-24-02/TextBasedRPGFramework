package net.biryeongtrain.text_emulator;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SimpleTextRPG extends JFrame {
    private JTextArea textArea; // 텍스트를 출력할 영역
    private JButton nextButton; // 다음 장면으로 이동하는 버튼
    private Map<String, String> scenes; // 씬과 텍스트 데이터를 저장
    private String currentScene; // 현재 씬 이름

    public SimpleTextRPG() {
        // 창 설정
        setTitle("Simple Text RPG");
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

        // 씬 초기화
        initializeScenes();
        currentScene = "start"; // 첫 번째 씬 설정

        // 첫 번째 씬 출력
        displayTextWithEffect(scenes.get(currentScene));

        // 창 크기 설정 및 표시
        setSize(600, 400);
        setVisible(true);
    }

    // 씬 데이터 초기화
    private void initializeScenes() {
        scenes = new HashMap<>();
        scenes.put("start", "You are at the start of your journey.");
        scenes.put("forest", "You enter a dark forest. The trees whisper around you.");
        scenes.put("village", "You arrive at a quiet village. People greet you warmly.");
        scenes.put("end", "Your journey ends here. Thank you for playing!");
    }

    // 다음 씬으로 이동
    private void moveToNextScene() {
        // 간단한 씬 전환 로직
        if (currentScene.equals("start")) {
            currentScene = "forest";
        } else if (currentScene.equals("forest")) {
            currentScene = "village";
        } else if (currentScene.equals("village")) {
            currentScene = "end";
        } else {
            // 마지막 씬에서는 버튼 비활성화
            nextButton.setEnabled(false);
            return;
        }

        // 새로운 씬 출력
        displayTextWithEffect(scenes.get(currentScene));
    }

    // 텍스트를 한 글자씩 출력
    private void displayTextWithEffect(String text) {
        textArea.setText(""); // 기존 텍스트 삭제
        Timer timer = new Timer(50, null); // 50ms 간격으로 글자 출력
        char[] chars = text.toCharArray(); // 텍스트를 문자 배열로 변환
        final int[] index = {0}; // 현재 출력할 글자의 인덱스

        timer.addActionListener(e -> {
            if (index[0] < chars.length) {
                textArea.append(String.valueOf(chars[index[0]++])); // 한 글자씩 출력
            } else {
                timer.stop(); // 모든 글자를 출력하면 타이머 중지
            }
        });

        timer.start(); // 타이머 시작
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimpleTextRPG::new);
    }
}

