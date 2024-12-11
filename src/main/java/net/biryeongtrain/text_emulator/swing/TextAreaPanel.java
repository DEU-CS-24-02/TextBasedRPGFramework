package net.biryeongtrain.text_emulator.swing;

import net.biryeongtrain.text_emulator.level.scene.ActionType;
import net.biryeongtrain.text_emulator.level.scene.SceneAction;
import net.biryeongtrain.text_emulator.level.scene.SceneDecision;
import net.biryeongtrain.text_emulator.level.scene.Unit;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class TextAreaPanel extends JPanel {
    Timer timer;
    private JTextArea textArea; // 텍스트를 출력할 영역
    private Queue<String> TextBuffer; // 씬과 텍스트 데이터를 저장
    private static JPanel ButtonPanel;
    private Queue<Character> CharBuffer;
    private String[] testText = {
            "험난한 여정이 계속되는 가운데, 당신은 외진 길에서 상인을 만났습니다.",
            "그는 당신을 보고, 싸게 해줄태니 아이템을 조금 보고 가라고 합니다.",
            "당신은 어느정도 흥미가 당겨, 물품을 보기 시작했습니다."
    };
    private SceneDecision textDecision = new SceneDecision("테스트", "test", Collections.singletonList(new SceneAction(ActionType.PRINT, Unit.GOLD, "테스트 성공")));

    public TextAreaPanel() {
        setLayout(new BorderLayout());
        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 20));
        textArea.setBackground(new Color(0xF5F5DC));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.addMouseListener(new textAreaMouseListener());
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        // 버튼 페널 추가
        ButtonPanel = new JPanel();
        ButtonPanel.setLayout(new GridLayout(1,1));
        add(ButtonPanel, BorderLayout.SOUTH);


        // 씬 초기화
        initializeScenes();

        // 테스트용 문자열 삽입
        setTextArray(testText);
        // 버튼 생성 테스트
        new Button(textDecision);

        // 첫 번째 씬 출력
        moveToNextText();

        setSize(1920, 1080);
        setVisible(true);
    }

    // 버튼 패널 초기화
    public void clearButtons() {
        ButtonPanel.removeAll();
        ButtonPanel.setLayout(new GridLayout(1,1));
    }

    // 버튼 추가
    static class Button extends JButton {
        SceneDecision decision;
        public Button(SceneDecision decision) {
            ButtonPanel.setLayout(new GridLayout(ButtonPanel.getComponentCount() + 1,1));
            JButton newButton = new JButton();
            newButton.setText(decision.text());
            newButton.addActionListener(e -> buttonExe(decision.actions()));
            ButtonPanel.add(newButton);
            this.decision = decision;
        }
    }

     static private void buttonExe(List<SceneAction> actions) {
        for (SceneAction action : actions) {
            action.execute();
        }
    }

    // 씬 초기화
    private void initializeScenes() {
        TextBuffer = new LinkedList<String>();
        CharBuffer = new LinkedList<Character>();
        textArea.setText("");
    }

    // 한 화면에 출력할 문자열들 삽입
    public void setTextArray(String... texts) {
        TextBuffer.addAll(Arrays.asList(texts));
    }

    //한 번에 출력할 문자열 삽입
    private void addText(String text) {
        char[] chars = text.toCharArray();
        for (char c : chars) {
            CharBuffer.add(c);
        }
    }

    // 남은 문자열 텍스트 에니메이션 스킵
    private void skipCurrentText() {
        timer.stop();
        while(!CharBuffer.isEmpty()) {
            textArea.append(String.valueOf(CharBuffer.poll()));
        }
    }

    class textAreaMouseListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            moveToNextText();
        }
    }

    // 다음 텍스트 출력
    private void moveToNextText() {
        // 버퍼에 문자가 남아있으면 남은 문자열의 텍스트 애니메이션 스킵
        if(!CharBuffer.isEmpty()) {
            skipCurrentText();
            return;
        }
        // 모두 출력했으면 리턴
        if(TextBuffer.isEmpty()) {
            return;
        }
        // 새로운 씬 출력
        displayTextWithEffect(TextBuffer.poll()+"\n");
    }

    // 텍스트 애니메이션 구현
    private void displayTextWithEffect(String text) {
        timer  = new Timer(50, null); // 50ms 간격으로 글자 출력
        final int[] index = {0}; // 현재 출력할 글자의 인덱스
        addText(text);

        timer.addActionListener(e -> {
            if (!CharBuffer.isEmpty()) {
                textArea.append(String.valueOf(CharBuffer.poll())); // 한 글자씩 출력
            } else {
                timer.stop(); // 모든 글자를 출력하면 타이머 중지
            }

        });

        timer.start(); // 타이머 시작
    }
}
