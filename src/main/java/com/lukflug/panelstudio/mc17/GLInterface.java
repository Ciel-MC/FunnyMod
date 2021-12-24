package com.lukflug.panelstudio.mc17;

import com.lukflug.panelstudio.base.IInterface;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Stack;

/**
 * Implementation of {@link IInterface} for OpenGL in Minecraft.
 * @author lukflug
 */
public abstract class GLInterface implements IInterface {
	/**
	 * Clipping rectangle stack.
	 */
	private final Stack<Rectangle> clipRect = new Stack<>();
	/**
	 * Stored projection matrix.
	 */
	private Matrix4f projection=null;
	/**
	 * Boolean indicating whether to clip in the horizontal direction. 
	 */
	protected final boolean clipX;
	
	/**
	 * Constructor.
	 * @param clipX whether to clip in the horizontal direction
	 */
	public GLInterface (boolean clipX) {
		this.clipX=clipX;
	}
	
	@Override
	public Dimension getWindowSize() {
		return new Dimension((int)Math.ceil(getScreenWidth()),(int)Math.ceil(getScreenHeight()));
	}

	@Override
	public void drawString (Point pos, int height, String s, Color c) {
		PoseStack modelView = RenderSystem.getModelViewStack();
		if (modelView.last().pose() == null) return;
		modelView.pushPose();
		modelView.translate(pos.x,pos.y,0);
		float scale=height/(float)Minecraft.getInstance().font.lineHeight;
		modelView.scale(scale,scale,1);
		RenderSystem.applyModelViewMatrix();
		end(false);
		Minecraft.getInstance().font.drawShadow(getMatrixStack(),s,0,0,c.getRGB());
		begin(false);
		modelView.popPose();
		RenderSystem.applyModelViewMatrix();
	}

	@Override
	public int getFontWidth (int height, String s) {
		double scale=height/(double)Minecraft.getInstance().font.lineHeight;
		return (int)Math.round(Minecraft.getInstance().font.width(s)*scale);
	}

	@Override
	public void fillTriangle (Point pos1, Point pos2, Point pos3, Color c1, Color c2, Color c3) {
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuilder();
		bufferbuilder.begin(Mode.TRIANGLES,DefaultVertexFormat.POSITION_COLOR);
			bufferbuilder.vertex(pos1.x,pos1.y,getZLevel()).color(c1.getRed()/255.0f,c1.getGreen()/255.0f,c1.getBlue()/255.0f,c1.getAlpha()/255.0f).endVertex();
			bufferbuilder.vertex(pos2.x,pos2.y,getZLevel()).color(c2.getRed()/255.0f,c2.getGreen()/255.0f,c2.getBlue()/255.0f,c2.getAlpha()/255.0f).endVertex();
			bufferbuilder.vertex(pos3.x,pos3.y,getZLevel()).color(c3.getRed()/255.0f,c3.getGreen()/255.0f,c3.getBlue()/255.0f,c3.getAlpha()/255.0f).endVertex();
		tessellator.end();
	}

	@Override
	public void drawLine (Point a, Point b, Color c1, Color c2) {
		RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
		float normalX=b.x-a.x,normalY=b.y-a.y;
		float scale=(float)Math.sqrt(normalX*normalX+normalY*normalY);
		normalX/=scale;
		normalY/=scale;
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuilder();
		bufferbuilder.begin(Mode.LINES,DefaultVertexFormat.POSITION_COLOR_NORMAL);
			bufferbuilder.vertex(a.x*256/255f,a.y*256/255f,getZLevel()).color(c1.getRed()/255.0f,c1.getGreen()/255.0f,c1.getBlue()/255.0f,c1.getAlpha()/255.0f).normal(normalX,normalY,0).endVertex();
			bufferbuilder.vertex(b.x*256/255f,b.y*256/255f,getZLevel()).color(c2.getRed()/255.0f,c2.getGreen()/255.0f,c2.getBlue()/255.0f,c2.getAlpha()/255.0f).normal(normalX,normalY,0).endVertex();
		tessellator.end();
	}

	@Override
	public void fillRect (Rectangle r, Color c1, Color c2, Color c3, Color c4) {
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuilder();
		bufferbuilder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
			bufferbuilder.vertex(r.x,r.y+r.height,getZLevel()).color(c4.getRed()/255.0f,c4.getGreen()/255.0f,c4.getBlue()/255.0f,c4.getAlpha()/255.0f).endVertex();
			bufferbuilder.vertex(r.x+r.width,r.y+r.height,getZLevel()).color(c3.getRed()/255.0f,c3.getGreen()/255.0f,c3.getBlue()/255.0f,c3.getAlpha()/255.0f).endVertex();
			bufferbuilder.vertex(r.x+r.width,r.y,getZLevel()).color(c2.getRed()/255.0f,c2.getGreen()/255.0f,c2.getBlue()/255.0f,c2.getAlpha()/255.0f).endVertex();
			bufferbuilder.vertex(r.x,r.y,getZLevel()).color(c1.getRed()/255.0f,c1.getGreen()/255.0f,c1.getBlue()/255.0f,c1.getAlpha()/255.0f).endVertex();
		tessellator.end();
	}

	@Override
	public void drawRect (Rectangle r, Color c1, Color c2, Color c3, Color c4) {
		drawLine(new Point(r.x,r.y+r.height),new Point(r.x+r.width,r.y+r.height),c4,c3);
		drawLine(new Point(r.x+r.width,r.y+r.height),new Point(r.x+r.width,r.y),c3,c2);
		drawLine(new Point(r.x+r.width,r.y),new Point(r.x,r.y),c2,c1);
		drawLine(new Point(r.x,r.y),new Point(r.x,r.y+r.height),c1,c4);
	}
	
	@Override
	public synchronized int loadImage (String name) {
		try {
			ResourceLocation rl=new ResourceLocation(getResourcePrefix()+name);
			InputStream stream=Minecraft.getInstance().getResourceManager().getResource(rl).getInputStream();
			BufferedImage image=ImageIO.read(stream);
			int texture=TextureUtil.generateTextureId();
			RenderSystem.bindTextureForSetup(texture);
			int width=image.getWidth(),height=image.getHeight();
			IntBuffer buffer=ByteBuffer.allocateDirect(4*width*height).order(ByteOrder.nativeOrder()).asIntBuffer();
			buffer.put(image.getRGB(0,0,width,height,null,0,width));
			buffer.flip();
			TextureUtil.initTexture(buffer,width,height);
			return texture;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public void drawImage (Rectangle r, int rotation, boolean parity, int image, Color color) {
		if (image==0) return;
		int[][] texCoords ={{0,1},{1,1},{1,0},{0,0}};
		for (int i=0;i<rotation%4;i++) {
			int temp1=texCoords[3][0],temp2=texCoords[3][1];
			texCoords[3][0]=texCoords[2][0];
			texCoords[3][1]=texCoords[2][1];
			texCoords[2][0]=texCoords[1][0];
			texCoords[2][1]=texCoords[1][1];
			texCoords[1][0]=texCoords[0][0];
			texCoords[1][1]=texCoords[0][1];
			texCoords[0][0]=temp1;
			texCoords[0][1]=temp2;
		}
		if (parity) {
			int temp1=texCoords[1][0];
			texCoords[1][0]=texCoords[0][0];
			texCoords[0][0]=temp1;
			temp1=texCoords[3][0];
			texCoords[3][0]=texCoords[2][0];
			texCoords[2][0]=temp1;
		}
		RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
		RenderSystem.setShaderTexture(0,image);
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuilder();
		GlStateManager._enableTexture();
		bufferbuilder.begin(Mode.QUADS,DefaultVertexFormat.POSITION_COLOR_TEX);
			bufferbuilder.vertex(r.x,r.y+r.height,getZLevel()).color(color.getRed()/255.0f,color.getGreen()/255.0f,color.getBlue()/255.0f,color.getAlpha()/255.0f).uv(texCoords[0][0],texCoords[0][1]).endVertex();
			bufferbuilder.vertex(r.x+r.width,r.y+r.height,getZLevel()).color(color.getRed()/255.0f,color.getGreen()/255.0f,color.getBlue()/255.0f,color.getAlpha()/255.0f).uv(texCoords[1][0],texCoords[1][1]).endVertex();
			bufferbuilder.vertex(r.x+r.width,r.y,getZLevel()).color(color.getRed()/255.0f,color.getGreen()/255.0f,color.getBlue()/255.0f,color.getAlpha()/255.0f).uv(texCoords[2][0],texCoords[2][1]).endVertex();
			bufferbuilder.vertex(r.x,r.y,getZLevel()).color(color.getRed()/255.0f,color.getGreen()/255.0f,color.getBlue()/255.0f,color.getAlpha()/255.0f).uv(texCoords[3][0],texCoords[3][1]).endVertex();
		tessellator.end();
		GlStateManager._disableTexture();
	}
	
	/**
	 * Utility function to set clipping rectangle.
	 * @param r the clipping rectangle
	 */
	protected void scissor (Rectangle r) {
		if (r==null) {
			GL11.glScissor(0,0,0,0);
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			return;
		}
		Point a=guiToScreen(r.getLocation()),b=guiToScreen(new Point(r.x+r.width,r.y+r.height));
		if (!clipX) {
			a.x=0;
			b.x=Minecraft.getInstance().getWindow().getScreenWidth();
		}
		GL11.glScissor(Math.min(a.x,b.x),Math.min(a.y,b.y),Math.abs(b.x-a.x),Math.abs(b.y-a.y));
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
	}

	@Override
	public void window (Rectangle r) {
		if (clipRect.isEmpty()) {
			scissor(r);
			clipRect.push(r);
		} else {
			Rectangle top=clipRect.peek();
			if (top==null) {
				scissor(null);
				clipRect.push(null);
			} else {
				int x1,y1,x2,y2;
				x1=Math.max(r.x,top.x);
				y1=Math.max(r.y,top.y);
				x2=Math.min(r.x+r.width,top.x+top.width);
				y2=Math.min(r.y+r.height,top.y+top.height);
				if (x2>x1 && y2>y1) {
					Rectangle rect=new Rectangle(x1,y1,x2-x1,y2-y1);
					scissor(rect);
					clipRect.push(rect);
				} else {
					scissor(null);
					clipRect.push(null);
				}
			}
		}
	}

	@Override
	public void restore() {
		if (!clipRect.isEmpty()) {
			clipRect.pop();
			if (clipRect.isEmpty()) GL11.glDisable(GL11.GL_SCISSOR_TEST);
			else scissor(clipRect.peek());
		}
	}
	
	/**
	 * Utility function to convert screen pixel coordinates to PanelStudio GUI coordinates.
	 * @param p the screen coordinates 
	 * @return the corresponding GUI coordinates
	 */
	public Point screenToGui (Point p) {
		int resX=getWindowSize().width;
		int resY=getWindowSize().height;
		return new Point(p.x*resX/Minecraft.getInstance().getWindow().getScreenWidth(),resY-p.y*resY/Minecraft.getInstance().getWindow().getScreenHeight()-1);
	}
	
	/**
	 * Utility function to convert PanelStudio GUI coordinates to screen pixel coordinates.
	 * @param p the GUI coordinates 
	 * @return the corresponding screen coordinates
	 */
	public Point guiToScreen (Point p) {
		double resX=getScreenWidth();
		double resY=getScreenHeight();
		return new Point((int)Math.round(p.x*Minecraft.getInstance().getWindow().getScreenWidth()/resX),(int)Math.round((resY-p.y)*Minecraft.getInstance().getWindow().getScreenHeight()/resY));
	}
	
	/**
	 * Get the current screen width.
	 * @return the screen width
	 */
	protected double getScreenWidth() {
		return Minecraft.getInstance().getWindow().getGuiScaledWidth();
	}
	
	/**
	 * Get the current screen height.
	 * @return the screen height
	 */
	protected double getScreenHeight() {
		return Minecraft.getInstance().getWindow().getGuiScaledHeight();
	}
	
	/**
	 * Set OpenGL to the state used by the rendering methods.
	 * Should be called before rendering.
	 * @param matrix whether to set up the model-view matrix
	 */
	public void begin (boolean matrix) {
		if (matrix) {
			projection=RenderSystem.getProjectionMatrix().copy();
			float[] array ={2/(float)getScreenWidth(),0,0,-1, 0,-2/(float)getScreenHeight(),0,1, 0,0,-1/3000f,0, 0,0,0,1};
			FloatBuffer buffer=FloatBuffer.allocate(16).put(array).flip();
			RenderSystem.getProjectionMatrix().loadTransposed(buffer);
			PoseStack modelView=RenderSystem.getModelViewStack();
			modelView.pushPose();
			modelView.setIdentity();
			RenderSystem.applyModelViewMatrix();
		}
		RenderSystem.enableBlend();
		RenderSystem.disableTexture();
		RenderSystem.disableCull();
		RenderSystem.lineWidth(2);
		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA,GL11.GL_ONE,GL11.GL_ZERO);
	}
	
	/**
	 * Restore OpenGL to the state expected by Minecraft.
	 * Should be called after rendering.
	 * @param matrix whether to restore the model-view matrix
	 */
	public void end (boolean matrix) {
		RenderSystem.enableCull();
		RenderSystem.enableTexture();
		RenderSystem.disableBlend();
		if (matrix) {
			RenderSystem.getModelViewStack().popPose();
			RenderSystem.applyModelViewMatrix();
			RenderSystem.getProjectionMatrix().load(projection);
			projection=null;
		}
	}
	
	/**
	 * Get the z-coordinate to render everything.
	 * @return the z-level
	 */
	protected abstract float getZLevel();
	
	/**
	 * Get the matrix stack to be used.
	 * @return the current matrix stack
	 */
	protected abstract PoseStack getMatrixStack();
	
	/**
	 * Get the Minecraft resource location string.
	 * @return the resource prefix
	 */
	protected abstract String getResourcePrefix();
}
