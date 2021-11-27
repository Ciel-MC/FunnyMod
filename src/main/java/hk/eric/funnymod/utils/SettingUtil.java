package hk.eric.funnymod.utils;

import com.lukflug.panelstudio.setting.ISetting;
import com.lukflug.panelstudio.setting.Savable;

import java.util.stream.Stream;

public class SettingUtil {
    public static Stream<Savable<?>> getSavableStream(Stream<ISetting<?>> stream) {
        return stream.filter(obj -> obj instanceof Savable).map(obj -> (Savable<?>) obj);
    }
}
