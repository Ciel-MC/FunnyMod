package com.lukflug.panelstudio.setting;

import com.fasterxml.jackson.databind.node.ObjectNode;
import hk.eric.ericLib.utils.classes.TwoWayFunction;
import hk.eric.funnymod.exceptions.ConfigLoadingFailedException;

public interface Savable<T> {

    TwoWayFunction<T, String> getConverter();

    ObjectNode save();

    void load(ObjectNode node) throws ConfigLoadingFailedException;

    ObjectNode saveThis();

    void loadThis(ObjectNode node) throws ConfigLoadingFailedException;

    String getConfigName();
}
