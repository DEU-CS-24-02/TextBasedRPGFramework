package net.biryeongtrain.text_emulator.swing;

import net.biryeongtrain.text_emulator.GameManager;
import net.biryeongtrain.text_emulator.entity.Entity;
import net.biryeongtrain.text_emulator.item.ItemStack;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TextRPGSwing extends JFrame {

    private CardLayout cardLayout; // 화면 전환을 위한 CardLayout
    private JPanel mainPanel; // 전체 패널 컨테이너
    private JLabel entityInfoLabel; // 엔티티 정보 표시
    private DefaultListModel<String> inventoryModel; // 소지품 데이터 모델
    private JList<String> inventoryList; // 소지품 리스트
    private JTextArea textArea; // 텍스트 출력 영역
    private JScrollPane scrollPane; // 스크롤 가능한 텍스트 영역
    private JButton nextButton; // Next 버튼

    private List<String> scenarios; // 시나리오 데이터
    private Entity playerEntity; // 플레이어 엔티티 정보
    private List<ItemStack> playerInventory; // 플레이어 소지품
    private int currentIndex; // 현재 시나리오 인덱스

    //생성자. UI 초기화 및 데이터 로드.
    public TextRPGSwing() {
        setTitle("Text-Based RPG");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // CardLayout을 사용한 화면 관리
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 두 개의 화면 생성 및 추가
        createStartScreen();
        createGameScreen();

        add(mainPanel);
        setVisible(true);

        // 데이터 초기화
        initializeGameData();
    }

    //시작 화면 생성.
    private void createStartScreen() {
        JPanel startScreen = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to the Text RPG!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        startScreen.add(welcomeLabel, BorderLayout.CENTER);

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> cardLayout.show(mainPanel, "GameScreen"));
        startScreen.add(startButton, BorderLayout.SOUTH);

        mainPanel.add(startScreen, "StartScreen");
    }

    //게임 화면 생성.
    private void createGameScreen() {
        JPanel gameScreen = new JPanel(new BorderLayout());

        // 좌측 패널
        JPanel leftPanel = new JPanel(new GridLayout(2, 1));
        leftPanel.setPreferredSize(new Dimension(250, 0));

        // 엔티티 정보
        entityInfoLabel = new JLabel();
        entityInfoLabel.setBorder(BorderFactory.createTitledBorder("Entity Info"));
        leftPanel.add(entityInfoLabel);

        // 소지품 정보
        inventoryModel = new DefaultListModel<>();
        inventoryList = new JList<>(inventoryModel);
        inventoryList.setBorder(BorderFactory.createTitledBorder("Inventory"));
        leftPanel.add(new JScrollPane(inventoryList));

        gameScreen.add(leftPanel, BorderLayout.WEST);

        // 텍스트 출력 영역
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        gameScreen.add(scrollPane, BorderLayout.CENTER);

        // 하단 Next 버튼
        nextButton = new JButton("Next");
        nextButton.addActionListener(e -> displayNextText());
        gameScreen.add(nextButton, BorderLayout.SOUTH);

        mainPanel.add(gameScreen, "GameScreen");
    }


    //초기 게임 데이터를 로드합니다.
    private void initializeGameData() {
        // GameManager로 데이터 가져오기
        GameManager gameManager = new GameManager();
        scenarios = gameManager.getScenarios();
        playerEntity = gameManager.getPlayerEntity();
        playerInventory = gameManager.getPlayerInventory();

        // 엔티티 정보 UI 업데이트
        updateEntityInfo();

        // 소지품 정보 UI 업데이트
        updateInventory();

        // 텍스트 초기화
        textArea.setText("Welcome to the RPG Adventure!\n");
    }

    //엔티티 정보를 업데이트합니다.
    private void updateEntityInfo() {
        String entityInfo = String.format("<html>Name: %s<br>Health: %.2f<br>Armor: %.2f<br>Damage: %.2f</html>",
                playerEntity.getType(),
                playerEntity.getHealth(),
                playerEntity.getArmor(),
                playerEntity.getDamage());
        entityInfoLabel.setText(entityInfo);
    }
    //소지품 정보를 업데이트합니다.
    private void updateInventory() {
        inventoryModel.clear();
        for (ItemStack item : playerInventory) {
            inventoryModel.addElement(item.getItemName() + " x" + item.getCount());
        }
    }
    //다음 텍스트를 출력합니다.

    private void displayNextText() {
        if (currentIndex >= scenarios.size()) {
            textArea.append("\nThe story has ended. Thank you for playing!\n");
            nextButton.setEnabled(false);
            return;
        }

        String textToDisplay = scenarios.get(currentIndex++);
        new Thread(() -> {
            try {
                for (char c : textToDisplay.toCharArray()) {
                    SwingUtilities.invokeLater(() -> textArea.append(String.valueOf(c)));
                    Thread.sleep(50); // 한 글자씩 출력
                }
                SwingUtilities.invokeLater(() -> {
                    textArea.append("\n");
                    scrollToBottom();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    //스크롤을 맨 아래로 이동합니다.
    private void scrollToBottom() {
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        verticalBar.setValue(verticalBar.getMaximum());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimpleTextRPG::new);
    }
}