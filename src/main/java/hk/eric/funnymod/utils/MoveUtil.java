package hk.eric.funnymod.utils;

public class MoveUtil {
    public static void goTo(Mode mode, int x, int y, int z) {
        switch (mode) {
            case FLASH -> {

            }
            case Pathfind -> {

            }
            case STRAIGHT -> {

            }
        }
    }

    public enum Mode {
        FLASH, //Teleport there in 1 tick and flash back
        Pathfind, //Pathfind to there
        STRAIGHT //Straight line to there
    }
}
