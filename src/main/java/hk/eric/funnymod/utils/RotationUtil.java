package hk.eric.funnymod.utils;

import hk.eric.funnymod.utils.classes.XYRot;

import static hk.eric.funnymod.FunnyModClient.player;

public class RotationUtil {

    public static void setRot(XYRot xyRot) {
        player().setYRot(xyRot.getYRot());
        player().setXRot(xyRot.getXRot());
    }
}
