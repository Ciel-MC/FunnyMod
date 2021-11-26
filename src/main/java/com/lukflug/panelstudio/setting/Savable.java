package com.lukflug.panelstudio.setting;

import com.fasterxml.jackson.databind.node.ObjectNode;
import hk.eric.funnymod.exceptions.ConfigLoadingFailedException;
import hk.eric.funnymod.utils.classes.TwoWayFunction;

public interface Savable<T> {

    TwoWayFunction<T, String> getConverter();

    ObjectNode save();

    void load(ObjectNode node) throws ConfigLoadingFailedException;

    default T fromString(String value) {
        return getConverter().revert(value);
    }


    ObjectNode saveThis();

    void loadThis(ObjectNode node) throws ConfigLoadingFailedException;
}
