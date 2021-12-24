package com.lukflug.panelstudio.setting;

/**
 * Interface representing a keybind.
 * @author lukflug
 */
public interface IKeybindSetting extends ISetting<Integer> {
	/**
	 * Get the value of the keybind.
	 * @return the scancode of the key
	 */
    int getKey();
	
	/**
	 * Set the value of the keybind.
	 * @param key the scancode of the key
	 */
    void setKey(int key);
    
    /**
     * Get the name of the key that is bound.
     * @return name of the key
     */
    String getKeyName();
	
	@Override
    default Class<Integer> getSettingClass() {
		return Integer.class;
	}
}
