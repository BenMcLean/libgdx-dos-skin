package net.benmclean.libgdxdos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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
        batch.enableBlending();

        frameBuffer.begin();
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        batch.setShader(Palette4.makeShader());
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        viewport.apply();
        //palette.bind(batch.getShader());
        batch.draw(texture, 0, 0);
        batch.end();
        frameBuffer.end();

        Texture answer = new Texture(frameBuffer.getColorBufferTexture().getTextureData().consumePixmap());
        answer.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
//        batch.dispose();
//        frameBuffer.dispose();
        return answer;
    }
}
