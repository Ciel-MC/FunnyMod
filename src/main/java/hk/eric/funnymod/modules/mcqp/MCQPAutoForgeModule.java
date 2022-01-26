package hk.eric.funnymod.modules.mcqp;

import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.setting.ILabeled;
import hk.eric.funnymod.chat.ChatManager;
import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.EventState;
import hk.eric.funnymod.event.events.TickEvent;
import hk.eric.funnymod.gui.setting.BooleanSetting;
import hk.eric.funnymod.gui.setting.EnumSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.gui.setting.settingWithSubSettings.BooleanSettingWithSubSettings;
import hk.eric.funnymod.gui.setting.settingWithSubSettings.EnumSettingWithSubSettings;
import hk.eric.funnymod.modules.ToggleableModule;
import hk.eric.funnymod.utils.ContainerUtil;
import hk.eric.funnymod.utils.MouseUtil;
import hk.eric.funnymod.utils.NPCUtil;
import hk.eric.funnymod.utils.WaitUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

import static hk.eric.funnymod.utils.ContainerUtil.*;

public class MCQPAutoForgeModule extends ToggleableModule {

    private static FullAutoState state = FullAutoState.READY;
    private static final Pattern notEnoughPattern = Pattern.compile("加工系統 >> \\[.*] 不足");
    private static Thread currentAutoForgeThread;
    private static int xp = 0;

    private static final EventHandler<TickEvent> forgeTickHandler = new EventHandler<>() {
        @Override
        public void handle(TickEvent tickEvent) {
            if (tickEvent.getState() == EventState.POST) {
                if (enableFullAuto.isOn() && state == FullAutoState.READY) {
                    currentAutoForgeThread = new Thread(() -> {
                        state = FullAutoState.IN_PROGRESS;
                        ItemStack item = getPlayer().getItemInHand(InteractionHand.MAIN_HAND);
                        if (item.getMaxDamage() - item.getDamageValue() <= 3) {
                            ChatManager.sendMessage("已經沒有耐久度了 总共获得" + xp + "经验");
                            enableFullAuto.toggle();
                            return;
                        }
                        NPCUtil.clickNPCByName("§b加工師傅-淬煉匠", EntityType.PLAYER, MouseUtil.MouseButton.RIGHT);
                        WaitUtils.waitForNewScreen();
                        if (!enableFullAuto.isOn()) return;
                        String itemName = forgeType.getValue().getName() + (forgeType.getValue() == MajorForgeTypes.BULK ? bulkMaterial.getValue().getName() : leveled.getValue().getName());
                        if (!clickForge("新加工介面", null, itemName, "下一頁 ->")) {
                            ChatManager.sendMessage(Component.text("鑄造失败", NamedTextColor.RED));
                            enableFullAuto.toggle();
                        }
                        WaitUtils.waitForChat(chatReceivedEvent -> {
                            String message = chatReceivedEvent.getMessage().getString();
                            if (message.contains("加工經驗 +")) {
                                xp += Integer.parseInt(message.substring(message.indexOf("+") + 1));
                                return true;
                            }else if (message.matches(notEnoughPattern.pattern())){
                                ChatManager.sendMessage("鑄造已完成, 总共获得" + xp + "经验");
                                enableFullAuto.toggle();
                                xp = 0;
                                return true;
                            }else {
                                return false;
                            }
                        });
                        state = FullAutoState.READY;
                    });
                    currentAutoForgeThread.start();
                }
                if (autoCaptcha.isOn()) {
                    if (ContainerUtil.titleContains("§b鑄造加工"))
                    findAndClick(Items.IRON_INGOT, "[鑄造]");
                }
            }
        }
    };


    private static MCQPAutoForgeModule instance;

    public static final BooleanSettingWithSubSettings enableFullAuto = new BooleanSettingWithSubSettings("Enable Full Auto", "MCQPAutoForgeFullAuto", "Enable full auto mode", false, bl -> {
        if (!bl) {
            if (currentAutoForgeThread != null) {
                currentAutoForgeThread.interrupt();
            }
            state = FullAutoState.READY;
            xp = 0;
        }
    });
    public static final EnumSettingWithSubSettings<MajorForgeTypes> forgeType = new EnumSettingWithSubSettings<>("Forge Type", "MCQPAutoForgeForgeType", null, MajorForgeTypes.HIGH_LEVEL, MajorForgeTypes.class, e -> ILabeled.of(e.getName()));
    public static final EnumSetting<MajorForgeTypes.LEVELED> leveled = new EnumSetting<>("Material", "MCQPAutoForgeLeveledMaterial", null, MajorForgeTypes.LEVELED.GOLD_INGOT, MajorForgeTypes.LEVELED.class);
    public static final EnumSetting<MajorForgeTypes.BULK_MAT> bulkMaterial = new EnumSetting<>("Material", "MCQPAutoForgeBulkMaterial", null, MajorForgeTypes.BULK_MAT.GOLD_INGOT, MajorForgeTypes.BULK_MAT.class);

    public static final BooleanSetting autoCaptcha = new BooleanSetting("Auto Captcha", "MCQPAutoForgeModule.autoCaptcha", "Automatically completes the captcha", true);
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "MCQPAutoForgeKeybind", null, -1, () -> instance.toggle(), true);

    public MCQPAutoForgeModule() {
        super("MCQPAutoForge", "Automatically clicks smelting QTE for you");
        instance = this;

        settings.add(enableFullAuto);
        enableFullAuto.addGlobalSubSetting(forgeType);
        forgeType.addGlobalSubSetting(leveled);
        leveled.setIsVisible(() -> forgeType.getValue() == MajorForgeTypes.HIGH_LEVEL || forgeType.getValue() == MajorForgeTypes.TOP_LEVEL);
        forgeType.addSubSetting(MajorForgeTypes.BULK, bulkMaterial);

        settings.add(autoCaptcha);

        settings.add(keybind);

        registerOnOffHandler(forgeTickHandler);
    }

    private static boolean clickForge(String title, @Nullable Item item, String itemName, String nextItemName) {
        if (ContainerUtil.titleContains(title)) {
            System.out.println("Found " + title);
            if (findAndClick(item, itemName)) {
                System.out.println("Clicked " + itemName);
                return true;
            }else {
                System.out.println("Failed to click " + itemName);
                Slot slot;
                if ((slot = find(Items.ARROW,nextItemName)) != null) {
                    click(slot);
                    WaitUtils.waitForNewScreen();
                    return clickForge(title, item, itemName, nextItemName);
                }else {
                    return false;
                }
            }
        }
        return false;
    }

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

    public enum FullAutoState {
        READY,
        IN_PROGRESS
    }

    public enum MajorForgeTypes {
        HIGH_LEVEL("高級"),
        TOP_LEVEL("頂級"),
        BULK("大量"),
        ;

        private final String name;

        MajorForgeTypes(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public enum LEVELED {
            GOLD_INGOT("金錠"),
            SILVER_INGOT("銀錠"),
            COPPER_INGOT("銅錠"),
            IRON_INGOT("鐵錠"),
            RUBY("紅寶石"),
            EMERALD("綠寶石"),
            SAPPHIRE("藍寶石"),
            COAL("煤炭"),
            OAK_WOOD("橡木"),
            FIR_WOOD("杉木"),
            BIRCH_WOOD("樺木"),
            JUNGLE_WOOD("叢林"),
            ACACIA_WOOD("相思木"),
            DARK_OAK_WOOD("黑橡木"),
            DIAMOND("鑽石"),
            FEATHER("羽毛"),
            WOOL("羊毛"),
            LEATHER("皮革"),
            ;

            private final String name;

            LEVELED(String name) {
                this.name = name;
            }


            public String getName() {
                return name;
            }
        }

        public enum BULK_MAT {
            GOLD_INGOT("金錠"),
            SILVER_INGOT("銀錠"),
            COPPER_INGOT("銅錠"),
            IRON_INGOT("鐵錠"),
            COAL("煤炭"),
            ;

            private final String name;

            BULK_MAT(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }
    }
}