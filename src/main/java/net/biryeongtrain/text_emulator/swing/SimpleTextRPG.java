package net.biryeongtrain.text_emulator.swing;

import net.biryeongtrain.text_emulator.utils.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SimpleTextRPG extends JFrame {
    Dimension centerSize = new Dimension(640, 685);

    private TextAreaPanel textAreaPanel;
    private PlayerInfoPanel playerInfoPanel;
    private PlayerInventoryPanel playerInventoryPanel;
    private SceneInfoPanel sceneInfoPanel;
    private EntityInfoPanel entityInfoPanel;

    public SimpleTextRPG() {
        // 창 설정
        setTitle("Simple Text RPG");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        Container c = getContentPane();

        // 텍스트 패널 위치 및 크기 설정
        textAreaPanel = new TextAreaPanel();

        textAreaPanel.setSize(centerSize);
        textAreaPanel.setLocation((1280 - centerSize.width) / 2,0);
        c.add(textAreaPanel);

        // 플레이어 정보 패널 크기 설정
        JPanel PlayerPanel = new JPanel(new GridLayout(2,1,1,1));
        PlayerPanel.setSize(centerSize.width/2 - 1, centerSize.height);
        add(PlayerPanel);

        playerInfoPanel = new PlayerInfoPanel();
        playerInventoryPanel = new PlayerInventoryPanel();
        PlayerPanel.add(playerInfoPanel);
        PlayerPanel.add(playerInventoryPanel);


        // 씬 및 엔티티 정보 패널 위치 및 크기 설정
        JPanel ScenePanel = new JPanel(new GridLayout(2,1,1,1));
        ScenePanel.setSize(centerSize.width/2 - 1, centerSize.height);
        ScenePanel.setLocation(1280 - centerSize.width / 2 +1,0);
        add(ScenePanel);

        sceneInfoPanel = new SceneInfoPanel();
        entityInfoPanel = new EntityInfoPanel();
        ScenePanel.add(sceneInfoPanel);
        ScenePanel.add(entityInfoPanel);


        // 창 크기 설정 및 표시
        setSize(1280, 720);
        setVisible(true);
        this.addWindowListener(new OnCloseEventHandler());
    }



    public TextAreaPanel getTextAreaPanel() {
        return textAreaPanel;
    }
    public PlayerInfoPanel getPlayerInfoPanel() {
        return playerInfoPanel;
    }
    public PlayerInventoryPanel getPlayerInventoryPanel() {
        return playerInventoryPanel;
    }
    public SceneInfoPanel getSceneInfoPanel() {
        return sceneInfoPanel;
    }
    public EntityInfoPanel getEntityInfoPanel() {
        return entityInfoPanel;
    }

    public void addTextSerializations(String... str) {
        this.textAreaPanel.setTextArray(str);
    }

    private static class OnCloseEventHandler extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            Util.shutdownExecutors();
            System.exit(0);
        }
    }
}


