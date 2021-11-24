package hk.eric.funnymod.utils;

import hk.eric.funnymod.gui.setting.Setting;
import hk.eric.funnymod.gui.setting.StringSetting;
import hk.eric.funnymod.modules.Module;

public class Random {
    private static final java.util.Random random = new java.util.Random();

    public static Module getRandomModule() {
        return new Module(randomString(3,10),randomString(1,10),()->true){};
    }
    public static Setting<?> getRandomSetting() {
        return new StringSetting(randomString(3,10),randomString(1,10),randomString(1,20),()->true,randomString(1,10));
    }

    public static boolean getRandomBoolean() {
        return random.nextBoolean();
    }

    public static int getRandomInt(int min, int max) {
        return getRandomInt(max - min) + min;
    }

    public static int getRandomInt(int max) {
        return random.nextInt(max);
    }

    public static String randomString(int minLength, int maxLength) {
        int length = random.nextInt(maxLength - minLength) + minLength;
        return randomString(length);
    }

    public static String randomString(int length) {
        char[] chars = new char[length];
        for(int i = 0; i < length; i++) {
            chars[i] = (char) (random.nextInt(26) + 'a');
        }
        return new String(chars);
    }
}
