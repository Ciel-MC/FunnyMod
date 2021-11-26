package hk.eric.funnymod.modules.player;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.FunnyModClient;
import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.EventManager;
import hk.eric.funnymod.event.events.TickEvent;
import hk.eric.funnymod.gui.setting.BooleanSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.gui.setting.children.BooleanSettingWithChildren;
import hk.eric.funnymod.modules.ToggleableModule;
import hk.eric.funnymod.utils.PacketUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InventoryManagerModule extends ToggleableModule {

    private final Set<String> autoThrow = new HashSet<>();
    private static List<String> MCQPList = List.of("普通強化石","中級強化石","高級強化石","超級強化石","抽取鍛晶","職人的鑲嵌槌","稀有素質捲軸");
    private static Set<String> MCQPEnabled = new HashSet<>();

    private static InventoryManagerModule instance;
    public static final BooleanSettingWithChildren MCQPDropEnabled = new BooleanSettingWithChildren("MCQP掉落", "InvManMCQPDrop", "Drops MCQP junks", ()->true, false);
    public static final BooleanSetting dropHotbar = new BooleanSetting("Drop Hotbar", "InvManDropHotbar", "Also drop items in hotbar", ()->true, false);
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "InventoryManKeybind", "", () -> true, -1, () -> instance.toggle());

    private static final EventHandler<TickEvent> inventoryManager = new EventHandler<>() {
        @Override
        public void handle(TickEvent tickEvent) {
            Player p = FunnyModClient.mc.player;
            if (p == null) return;
            Inventory inventory = p.getInventory();
            if (inventory == null) return;
            NonNullList<ItemStack> items = inventory.items;
            for (int i = 0; i < items.size(); i++) {
                if(shouldThrow(items.get(i))) {
                    dropItem(p, i);
                }
            }
        }
    };

    public InventoryManagerModule() {
        super("InventoryManager", "Manages Inventory", () -> true);
        instance = this;
        updateMCQPThrow();
        settings.add(dropHotbar);
        settings.add(MCQPDropEnabled);
        settings.add(keybind);
    }

    public static IToggleable getToggle() {
        return instance.isEnabled();
    }

    public static void addToMCQPThrow(String name) {
        MCQPList.add(name);
    }

    public static void removeFromMCQPThrow(String name) {
        MCQPList.remove(name);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        EventManager.getInstance().register(inventoryManager);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        EventManager.getInstance().unregister(inventoryManager);
    }

    public static void updateMCQPThrow() {
        MCQPDropEnabled.removeAllChildren();
        MCQPList.forEach(s -> MCQPDropEnabled.addChildren(true, new BooleanSetting(s, "InvManMCQPDrop" + s, "Drop " + s, () -> true, false, (bool)->{
            if(bool) {
                MCQPEnabled.add(s);
            }else {
                MCQPEnabled.remove(s);
            }
        })));
    }

    public static boolean shouldThrow(ItemStack itemStack) {
        if(itemStack.getItem() instanceof AirItem) return false;
//        System.out.println("Should throw " + itemStack.getDisplayName().getString());
        String itemName = itemStack.getDisplayName().getString();
        if (MCQPDropEnabled.isOn()) {
            for (String s : MCQPEnabled) {
                if (itemName.contains(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void dropItem(Player p, int slot) {
        if(slot < 9) {
            //drop hotbar
            if(dropHotbar.isOn()) {
                Inventory inventory = p.getInventory();
                inventory.removeItem(inventory.getItem(slot));
                PacketUtil.sendPacket(new ServerboundSetCarriedItemPacket(slot));
                PacketUtil.sendPacket(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.DROP_ALL_ITEMS, BlockPos.ZERO, Direction.DOWN));
            }
        } else {
            PacketUtil.sendPacket(new ServerboundContainerClickPacket(p.inventoryMenu.containerId, p.inventoryMenu.getStateId(), slot, 1, ClickType.THROW, p.getInventory().getItem(slot), new Int2ObjectOpenHashMap<ItemStack>()));
        }
    }

}