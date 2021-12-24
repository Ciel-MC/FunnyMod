package com.lukflug.panelstudio.mc17;

import com.lukflug.panelstudio.base.IInterface;
import com.lukflug.panelstudio.container.GUI;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

/**
 * Implementation of Minecraft's {@link Screen} that renders a PanelStudio GUI.
 * @author lukflug
 */
public abstract class MinecraftGUI extends Screen {


	/**
	 * The current mouse position.
	 */
	protected Point mouse=new Point();
	/**
	 * Current left mouse button state.
	 */
	protected boolean lButton=false;
	/**
	 * Current right mouse button state.
	 */
	protected boolean rButton=false;
	/**
	 * Current GLFW modifier bits.
	 */
	protected int modifiers=0;
	/**
	 * Last rendering time.
	 */
	protected long lastTime;
	/**
	 * Saved matrix stack;
	 */
	protected PoseStack poseStack =new PoseStack();
	
	/**
	 * Constructor.
	 */
	public MinecraftGUI() {
		super(new TextComponent("PanelStudio GUI"));
	}
	
	/**
	 * Displays the GUI.
	 */
	public void enterGUI() {
		Minecraft.getInstance().setScreen(this);
	}
	
	/**
	 * Closes the GUI.
	 */
	public void exitGUI() {
		Minecraft.getInstance().setScreen(null);
	}
	
	/**
	 * Renders the GUI.
	 */
	protected void renderGUI() {
		lastTime=System.currentTimeMillis();
		getInterface().begin(true);
		getGUI().render();
		getInterface().end(true);
	}
	
	@Override
	public void init() {
		getGUI().enter();
	}
	
	@Override
	public void removed() {
		getGUI().exit();
	}
	
	@Override
	public void render (PoseStack matrices, int mouseX, int mouseY, float partialTicks) {
		poseStack =matrices;
		mouse=new Point(Math.round(mouseX), Math.round(mouseY));
		renderGUI();
	}
	
	@Override
	public boolean mouseScrolled (double mouseX, double mouseY, double scroll) {
		if (!super.mouseScrolled(mouseX,mouseY,scroll)) {
			mouse=new Point((int)Math.round(mouseX),(int)Math.round(mouseY));
			if (scroll!=0) {
				if (scroll>0) getGUI().handleScroll(-getScrollSpeed());
				else getGUI().handleScroll(getScrollSpeed());
			}
		}
		return true;
	}

	@Override
	public boolean mouseClicked (double mouseX, double mouseY, int clickedButton) {
		if (!super.mouseReleased(mouseX,mouseY,clickedButton)) {
			mouse=new Point((int)Math.round(mouseX),(int)Math.round(mouseY));
			switch (clickedButton) {
				case IInterface.LBUTTON -> lButton = true;
				case IInterface.RBUTTON -> rButton = true;
			}
			getGUI().handleButton(clickedButton);
		}
		return true;
	}

	@Override
	public boolean mouseReleased (double mouseX, double mouseY, int releaseButton) {
		if (!super.mouseReleased(mouseX,mouseY,releaseButton)) {
			mouse=new Point((int)Math.round(mouseX),(int)Math.round(mouseY));
			switch (releaseButton) {
				case IInterface.LBUTTON -> lButton = false;
				case IInterface.RBUTTON -> rButton = false;
			}
			getGUI().handleButton(releaseButton);
		}
		return true;
	}
	
	@Override
	public boolean keyPressed (int keyCode, int scanCode, int modifiers) {
		this.modifiers=modifiers;
		if (keyCode==GLFW.GLFW_KEY_ESCAPE) exitGUI();
		else getGUI().handleKey(keyCode);
		return true;
	}
	
	@Override
	public boolean charTyped (char chr, int modifiers) {
		this.modifiers=modifiers;
		getGUI().handleChar(chr);
		return true;
	}
	
	@Override
	public boolean isPauseScreen() {
		return false;
	}
	
	/**
	 * Get the {@link GUI} to be rendered.
	 * @return current GUI
	 */
	protected abstract GUI getGUI();
	
	/**
	 * Get current {@link GUIInterface}.
	 * @return the current interface
	 */
	protected abstract GUIInterface getInterface();
	
	/**
	 * Get current scroll speed.
	 * @return the scroll speed
	 */
	protected abstract int getScrollSpeed();
}
