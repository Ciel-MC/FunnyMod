package hk.eric.funnymod.modules;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.setting.IModule;
import com.lukflug.panelstudio.setting.ISetting;
import hk.eric.funnymod.utils.Settings;

import java.util.stream.Stream;

public abstract class Module implements IModule {
	public final String displayName,description;
	public final IBoolean visible;
	public final Settings settings= new Settings();
	
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
		return settings.stream().filter(setting->setting instanceof ISetting)/*.sorted(Comparator.comparing(a -> a.displayName))*/.map(setting->(ISetting<?>)setting);
	}
}
