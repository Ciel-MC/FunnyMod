package hk.eric.funnymod.modules;

import com.lukflug.panelstudio.setting.ICategory;
import com.lukflug.panelstudio.setting.IClient;
import com.lukflug.panelstudio.setting.IModule;
import hk.eric.funnymod.FunnyModClient;
import hk.eric.funnymod.modules.combat.CriticalsModule;
import hk.eric.funnymod.modules.combat.KillAuraModule;
import hk.eric.funnymod.modules.combat.VelocityModule;
import hk.eric.funnymod.modules.debug.DebugModule;
import hk.eric.funnymod.modules.exploit.BowInstantKillModule;
import hk.eric.funnymod.modules.mcqp.MCQPAura.MCQPAuraModule;
import hk.eric.funnymod.modules.mcqp.*;
import hk.eric.funnymod.modules.misc.BindModule;
import hk.eric.funnymod.modules.misc.CommandModule;
import hk.eric.funnymod.modules.misc.TooltipScrollingModule;
import hk.eric.funnymod.modules.movement.*;
import hk.eric.funnymod.modules.player.*;
import hk.eric.funnymod.modules.visual.AnimationModule;
import hk.eric.funnymod.modules.visual.EspModule;
import hk.eric.funnymod.modules.visual.LogoModule;
import hk.eric.funnymod.modules.visual.TabGUIModule;
import hk.eric.funnymod.modules.world.ChatterModule;
import hk.eric.funnymod.modules.world.FreecamModule;
import hk.eric.funnymod.modules.world.TimerModule;

import java.util.*;
import java.util.stream.Stream;

public enum Category implements ICategory {
	COMBAT("Combat"),
	MOVEMENT("Movement"),
	PLAYER("Player"),
	VISUAL("Visual"),
	WORLD("World"),
	MISC("Misc"),
	EXPLOIT("Exploit"),
	MCQP("文靜"),
	DEBUG("Debug");

	public static final Set<HasComponents> hudComponents = new HashSet<>();
	public final String displayName;
	public final List<IModule> modules= new ArrayList<>();
	
	Category(String displayName) {
		this.displayName=displayName;
	}
	
	public static void init() {
		addModule(COMBAT,
				new CriticalsModule(),
				new KillAuraModule(),
				new VelocityModule()
		);
		addModule(MOVEMENT,
				new AntiVineModule(),
				new FlightModule(),
				new SprintModule(),
				new NoSlowModule(),
				new KeepSprintModule()
		);
		addModule(PLAYER,
				new AutoFishModule(),
				new FastplaceModule(),
				new InventoryManagerModule(),
				new HeightModule(),
				new NoFallModule(),
				new NoJumpDelayModule()
		);
		addModule(VISUAL,
				new EspModule(),
				new ClickGUIModule(),
				new TabGUIModule(),
				new LogoModule(),
				new AnimationModule()
		);
		addModule(WORLD,
				new ChatterModule(),
				new FreecamModule(),
				new TimerModule()
		);
		addModule(MISC,
				new BindModule(),
				new CommandModule(),
				new TooltipScrollingModule()
		);
		addModule(EXPLOIT,
				new BowInstantKillModule()
		);
		addModule(MCQP,
				new MCQPAutoForgeModule(),
				new MCQPAuraModule(),
				new MCQPAutoClickerModule(),
				new MCQPAutoFarmModule(),
				new MCQPFastReviveModule(),
				new MCQPHudModule(),
				new MCQPNoGhostHitModule(),
				new MCQPatchModule(),
				new MCQPPreventDropModule()
		);
		if (FunnyModClient.debug) {
			addModule(DEBUG,
					new DebugModule()
			);
		}
		for (Category value : values()) {
			value.modules.sort(Comparator.comparing(IModule::getDisplayName));
		}
	}

	public static void addModule(Category category,IModule... modules) {
		for (IModule module : modules) {
			category.modules.add(module);
			if(module instanceof HasComponents componentModule) {
				hudComponents.add(componentModule);
			}
		}
    }

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public Stream<IModule> getModules() {
		return modules.stream();
	}

	public static Stream<IModule> getAllModules() {
		return Stream.of(values()).flatMap(Category::getModules);
	}

	public static void reloadModules() {
		for (Category category : values()) {
			category.modules.clear();
		}
		init();
	}

	public static IClient getClient() {
		return () -> Arrays.stream(
				FunnyModClient.debug?
						new Category[]{COMBAT, MOVEMENT, PLAYER, VISUAL, WORLD, MISC, EXPLOIT, MCQP, DEBUG} :
						new Category[]{COMBAT, MOVEMENT, PLAYER, VISUAL, WORLD, MISC, EXPLOIT, MCQP});
	}
}
