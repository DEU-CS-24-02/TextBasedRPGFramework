package net.biryeongtrain.text_emulator.io.storage;

import java.nio.file.Path;

public class ScenarioPath {
    public static final ScenarioPath ROOT = new ScenarioPath(".");
    public static final ScenarioPath SCENE = new ScenarioPath("scene");
    public static final ScenarioPath ENTITY = new ScenarioPath("entity");
    public static final ScenarioPath ITEM = new ScenarioPath("item");
    public static final ScenarioPath ENTITY_TAG = new ScenarioPath("entity_tag");

    private final String relativePath;

    private ScenarioPath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getRelativePath() {
        return relativePath;
    }

    @Override
    public String toString() {
        return "/" + this.relativePath;
    }

    public static Path getPath(Path root, ScenarioPath path) {
        return root.resolve(path.relativePath);
    }
}
