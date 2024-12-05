package net.biryeongtrain.text_emulator;

import net.biryeongtrain.text_emulator.entity.Entity;
import net.biryeongtrain.text_emulator.io.LoadManager;
import net.biryeongtrain.text_emulator.utils.Util;

import java.util.concurrent.CompletableFuture;

/**
 * GameManager는 플레이어가 게임을 플레이 할 때 게임의 전반적인 플레이를 관리하는 클래스입니다.
 * GameManager의 인스턴스는 매 게임마다 하나여야하며, 중복될 수 없습니다.
 * GameManager는 프로그램에 실행되는게 아니라 플레이어가 게임을 플레이 할 떄 생성됩니다.
 */
public class GameManager {
    private Entity player;
    private volatile boolean isLoaded = false;
    private LoadManager loadManager;

    private GameManager() {
        this.loadManager = new LoadManager();

    }

    public void load() {
        CompletableFuture.runAsync(() -> {
           // TODO : load scenarios
            // TODO : load saves
            this.isLoaded = true;
        }, Util.IOExecutor);
    }

}
