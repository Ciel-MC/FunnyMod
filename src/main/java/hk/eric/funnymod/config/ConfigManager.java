package hk.eric.funnymod.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lukflug.panelstudio.setting.IModule;
import hk.eric.funnymod.exceptions.ConfigLoadingFailedException;
import hk.eric.funnymod.modules.Category;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;

public class ConfigManager {

    private static ObjectMapper mapper;

    public static void save(String configName) {
        ObjectNode root = mapper.createObjectNode();
        Category.getAllModules().forEach(module -> {
            root.set(module.getDisplayName(), module.save());
        });
        saveToFile(configName, root);
    }

    public static void load(String configName) throws ConfigLoadingFailedException {
        mapper = new ObjectMapper();
        ObjectNode root = null;
        try {
            root = loadFromFile(configName);
        } catch (IOException e) {
            if(e instanceof FileNotFoundException fileNotFoundException) {
                //TODO: Send Chat message
            }else {
                e.printStackTrace();
            }
        }
        ObjectNode finalRoot = root;
        for (IModule module : Category.getAllModules().toList()) {
            module.load(finalRoot != null ? (ObjectNode) finalRoot.get(module.getDisplayName()) : null);
        }
    }

    private static void saveToFile(String configName, ObjectNode node) {
        try {
            File configFile = getConfigFile(configName);

            FileWriter writer = new FileWriter(configFile);
            writer.write(node.toPrettyString()); //TODO: Dont pretty print
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ObjectNode loadFromFile(String configName) throws IOException {
        File configFile = getConfigFile(configName);
        FileReader reader = new FileReader(configFile);
        return (ObjectNode) mapper.readTree(reader);
    }

    private static Path getConfigPath() {
        Path path = FabricLoader.getInstance().getGameDir().resolve("FunnyMod");
        path.toFile().mkdirs();
        return path;
    }

    private static File getConfigFile(String configName) {
        File file = getConfigPath().resolve(configName).toFile();
        file.mkdirs();
        return file;
    }
}
