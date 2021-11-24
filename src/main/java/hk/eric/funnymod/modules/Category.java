package hk.eric.funnymod.modules;

import com.lukflug.panelstudio.setting.ICategory;
import com.lukflug.panelstudio.setting.IClient;
import com.lukflug.panelstudio.setting.IModule;
import hk.eric.funnymod.modules.mcqp.MCQPAuraModule;
import hk.eric.funnymod.modules.mcqp.MCQPAutoClickerModule;
import hk.eric.funnymod.modules.mcqp.MCQPFastReviveModule;
import hk.eric.funnymod.modules.misc.BindModule;
import hk.eric.funnymod.modules.misc.CommandModule;
import hk.eric.funnymod.modules.movement.KeepSprintModule;
import hk.eric.funnymod.modules.movement.NoSlowModule;
import hk.eric.funnymod.modules.movement.SprintModule;
import hk.eric.funnymod.modules.visual.LogoModule;
import hk.eric.funnymod.modules.visual.TabGUIModule;
import hk.eric.funnymod.modules.world.TimerModule;
import hk.eric.funnymod.utils.Random;

import java.util.*;
import java.util.stream.Stream;

public enum Category implements ICategory {
	COMBAT("Combat"),
	MOVEMENT("Movement"),
	PLAYER("Player"),
	VISUAL("Visual"),
	WORLD("World"),
	MISC("Misc"),
	MCQP("文靜");
	public final String displayName;
	public final List<Module> modules= new ArrayList<>();
	
	Category(String displayName) {
		this.displayName=displayName;
	}
	
	public static void init() {
		addModule(COMBAT,
				Random.getRandomModule()
		);
		addModule(MOVEMENT,
				new SprintModule(),
				new NoSlowModule(),
				new KeepSprintModule()
		);
		addModule(PLAYER,
				Random.getRandomModule()
		);
		addModule(VISUAL,
				new ClickGUIModule(),
				new TabGUIModule(),
				new LogoModule()
		);
		addModule(WORLD,
				new TimerModule()
		);
		addModule(MISC,
				new BindModule(),
				new CommandModule()
		);
		addModule(MCQP,
				new MCQPAuraModule(),
				new MCQPAutoClickerModule(),
				new MCQPFastReviveModule()
		);
		for (Category value : values()) {
			value.modules.sort(Comparator.comparing(Module::getDisplayName));
		}
	}

	public static void addModule(Category category,Module... modules) {
		category.modules.addAll(Arrays.asList(modules));
    }

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public Stream<IModule> getModules() {
		return modules.stream().map(module->module);
	}
	
	public static IClient getClient() {
		return () -> Arrays.stream(Category.values());
	}
}
