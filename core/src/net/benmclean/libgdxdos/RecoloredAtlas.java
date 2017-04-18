package net.benmclean.libgdxdos;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Benjamin on 4/7/2017.
 */
public class RecoloredAtlas {
    public static Texture recolor (Texture texture, Palette4 palette) {
        FrameBuffer frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, texture.getWidth(), texture.getHeight(), false, false);
        SpriteBatch batch = new SpriteBatch();
        Viewport viewport = new FitViewport(texture.getWidth(), texture.getHeight());
        viewport.getCamera().position.set(0, 0, 0);
        viewport.update(texture.getWidth(), texture.getHeight());

        frameBuffer.begin();
        batch.setShader(Palette4.makeShader());
        batch.begin();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        palette.bind(batch.getShader());
        batch.draw(texture, 0, 0);
        batch.end();
        frameBuffer.end();
        Texture answer = frameBuffer.getColorBufferTexture();
        answer.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        batch.dispose();
        frameBuffer.dispose();
        return answer;
    }
}
