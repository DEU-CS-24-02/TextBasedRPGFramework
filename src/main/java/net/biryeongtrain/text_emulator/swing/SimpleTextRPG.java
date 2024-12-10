package net.biryeongtrain.text_emulator.swing;

import javax.swing.*;
import java.awt.*;

public class SimpleTextRPG extends JFrame {
    Dimension centerSize = new Dimension(640, 685);

    public SimpleTextRPG() {
        // 창 설정
        setTitle("Simple Text RPG");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        Container c = getContentPane();

        // 텍스트 패널 위치 및 크기 설정
        TextAreaPanel textAreaPanel = new TextAreaPanel();
        textAreaPanel.setSize(centerSize);
        textAreaPanel.setLocation((1280 - centerSize.width) / 2,0);
        c.add(textAreaPanel);

        // 플레이어 정보 패널 크기 설정
        JPanel PlayerPanel = new JPanel(new GridLayout(2,1,1,1));
        PlayerPanel.setSize(centerSize.width/2 - 1, centerSize.height);
        add(PlayerPanel);
        PlayerPanel.add(new PlayerInfoPanel());
        PlayerPanel.add(new PlayerInventoryPanel());

        // 씬 및 엔티티 정보 패널 위치 및 크기 설정
        JPanel ScenePanel = new JPanel(new GridLayout(2,1,1,1));
        ScenePanel.setSize(centerSize.width/2 - 1, centerSize.height);
        ScenePanel.setLocation(1280 - centerSize.width / 2 +1,0);
        add(ScenePanel);
        ScenePanel.add(new SceneInfoPanel());
        ScenePanel.add(new EntityInfoPanel());


        // 창 크기 설정 및 표시
        setSize(1280, 720);
        setVisible(true);
    }
}


