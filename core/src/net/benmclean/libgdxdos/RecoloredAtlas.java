package net.benmclean.libgdxdos;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

/**
 * Created by Benjamin on 4/7/2017.
 */
public class RecoloredAtlas {
    public static Texture recolor (Texture texture, Palette4 palette) {
        FrameBuffer frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, texture.getWidth(), texture.getHeight(), false, false);
        SpriteBatch batch = new SpriteBatch();

        frameBuffer.begin();
        batch.setShader(Palette4.makeShader());
        batch.begin();
        palette.bind(batch.getShader());
        batch.draw(texture, 0, 0);
        batch.end();
        frameBuffer.end();
        Texture answer = frameBuffer.getColorBufferTexture();
        batch.dispose();
        frameBuffer.dispose();
        return answer;
    }
}
