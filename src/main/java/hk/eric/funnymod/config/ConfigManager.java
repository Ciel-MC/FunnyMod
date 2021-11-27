package hk.eric.funnymod.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lukflug.panelstudio.setting.IModule;
import hk.eric.funnymod.FunnyModClient;
import hk.eric.funnymod.exceptions.ConfigLoadingFailedException;
import hk.eric.funnymod.modules.Category;
import hk.eric.funnymod.utils.ObjectUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.TextComponent;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class ConfigManager {

    private static ObjectMapper mapper = ObjectUtil.getMapper();

    public static void save(String configName) {
        ObjectNode root = mapper.createObjectNode();
        Category.getAllModules().forEach(module -> {
            root.set(module.getDisplayName(), module.save());
        });
        saveToFile(configName, root);
        FunnyModClient.mc.gui.getChat().addMessage(new TextComponent("FunnyMod: Config saved: " + configName));
    }

    public static void load(String configName) throws ConfigLoadingFailedException {
        FunnyModClient.mc.gui.getChat().addMessage(new TextComponent("FunnyMod: Loading config " + configName));
        mapper = new ObjectMapper();
        ObjectNode root = null;
        try {
            root = loadFromFile(configName);
        } catch (IOException e) {
            if(e instanceof FileNotFoundException fileNotFoundException) {
                FunnyModClient.mc.gui.getChat().addMessage(new TextComponent("FunnyMod: Config file not found"));
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
            String result = Base64.getEncoder().encodeToString(node.toString().getBytes());
            writer.write(result);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ObjectNode loadFromFile(String configName) throws IOException {
        File configFile = getConfigFile(configName);
//        FileReader reader = new FileReader(configFile);
        String config = new String(Base64.getDecoder().decode(Files.readAllBytes(configFile.toPath())));
        return (ObjectNode) mapper.readTree(config);
    }

    private static Path getConfigPath() {
        Path path = FabricLoader.getInstance().getGameDir().resolve("FunnyMod");
        path.toFile().mkdirs();
        return path;
    }

    private static File getConfigFile(String configName) {
        File file = new File(getConfigPath().toFile(), configName + ".json");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
