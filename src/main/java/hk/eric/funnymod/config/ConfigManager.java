package hk.eric.funnymod.config;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lukflug.panelstudio.setting.IModule;
import hk.eric.funnymod.chat.ChatManager;
import hk.eric.funnymod.exceptions.ConfigLoadingFailedException;
import hk.eric.funnymod.modules.Category;
import hk.eric.funnymod.utils.ObjectUtil;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class ConfigManager {

    private static ObjectMapper mapper = ObjectUtil.getMapper();
    private static final Logger logger = LogManager.getLogger();

    public static void save(String configName) {
        ChatManager.sendMessage("Saving config: " + configName);
        ObjectNode root = mapper.createObjectNode();
        Category.getAllModules().forEach(module -> root.set(module.getDisplayName(), module.save()));
        saveToFile(configName, root);
    }

    public static void load(String configName) throws ConfigLoadingFailedException {
        ChatManager.sendMessage("Loading config " + configName);
        mapper = new ObjectMapper();
        ObjectNode root = null;
        try {
            root = loadFromFile(configName);
        } catch (IOException e) {
            if (e instanceof FileNotFoundException fileNotFoundException) {
                ChatManager.sendMessage("Config file not found");
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
            logger.info("Writing to file: " + configFile.toPath());
            writer.write(node.toPrettyString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ObjectNode loadFromFile(String configName) throws IOException, ConfigLoadingFailedException {
        File configFile = getConfigFile(configName);
        logger.info("Reading from file: " + configFile.toPath());
        String config;
        JsonNode jsonNode;
        try {
            config = new String(Files.readAllBytes(configFile.toPath()));
            jsonNode = mapper.readTree(config);
        }catch (JsonParseException e) {
            try {
                config = new String(Base64.getDecoder().decode(Files.readAllBytes(configFile.toPath())));
                jsonNode = mapper.readTree(config);
                ChatManager.sendMessage("You're using legacy config file, do ;c save <Config Name> to overwrite it with the newer format");
            }catch (Exception e1) {
                logger.info("Config file couldn't be loaded");
                throw new ConfigLoadingFailedException();
            }
        }
        if (!(jsonNode instanceof ObjectNode)) {
            throw new ConfigLoadingFailedException();
        }
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
