package hk.eric.funnymod.utils.classes;

import hk.eric.funnymod.gui.setting.Setting;
import hk.eric.funnymod.gui.setting.children.HasChildren;

import java.util.ArrayList;

public class Settings extends ArrayList<Setting<?>> {
    @Override
    public boolean add(Setting<?> setting) {
        boolean ret = super.add(setting);
        if(setting instanceof HasChildren hasChildren) {
            (hasChildren.getAllChildren()).forEach(child -> add((Setting<?>) child));
        }
        return ret;
    }
}
