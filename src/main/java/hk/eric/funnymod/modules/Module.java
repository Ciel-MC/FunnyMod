package hk.eric.funnymod.modules;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.setting.IModule;
import com.lukflug.panelstudio.setting.ISetting;
import com.lukflug.panelstudio.setting.Savable;
import hk.eric.funnymod.FunnyModClient;
import hk.eric.funnymod.event.EventManager;
import hk.eric.funnymod.exceptions.ConfigLoadingFailedException;
import hk.eric.funnymod.utils.Constants;
import hk.eric.funnymod.utils.ObjectUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class Module implements IModule {

	public final String displayName,description;
	public final IBoolean visible;
	public final List<ISetting<?>> settings= new ArrayList<>();

	protected static final Minecraft mc = FunnyModClient.mc;
	protected static EventManager eventManager = EventManager.getInstance();

	public Module(String displayName, String description) {
		this(displayName, description, Constants.alwaysTrue);
	}

	public Module (String displayName, String description, IBoolean visible) {
		this.displayName=displayName;
		this.description=description;
		this.visible=visible;
	}
	
	@Override
	public String getDisplayName() {
		return displayName;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public IBoolean isVisible() {
		return visible;
	}

	@Override
	public IToggleable isEnabled() {
		return null;
	}

	@Override
	public Stream<ISetting<?>> getSettings() {
		return settings.stream().filter(Objects::nonNull);
	}

	@Override
	public ObjectNode save() {
		ObjectNode node = ObjectUtil.getObjectNode();
		getSettings().forEach(setting -> {
			if (setting instanceof Savable<?> savable) {
				node.set(savable.getConfigName(), savable.save());
			}
		});
		return node;
	}

	@Override
	public void load(ObjectNode node) throws ConfigLoadingFailedException {
		if (node == null) {
			throw new ConfigLoadingFailedException();
		}
		for (ISetting<?> setting : getSettings().toList()) {
			if (setting instanceof Savable<?> savable) {
				savable.load((ObjectNode) node.get(savable.getConfigName()));
			}
		}
	}

	protected static LocalPlayer getPlayer() {
		return mc.player;
	}

	protected static ClientLevel getLevel() {
		return mc.level;
	}

	protected static MultiPlayerGameMode getGameMode() {
		return mc.gameMode;
	}
}
