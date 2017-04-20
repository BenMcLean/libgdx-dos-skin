package net.benmclean.libgdxdos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Benjamin on 4/7/2017.
 */
public class RecoloredAtlas {
    public static Pixmap recolor(Texture texture, Palette4 palette) {
        final int width = texture.getWidth(), height = texture.getHeight();
        FrameBuffer frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false, false);
        SpriteBatch batch = new SpriteBatch();
        Viewport viewport = new FitViewport(width, height);
        viewport.getCamera().position.set(width / 2, height / 2, 0);
        viewport.update(width, height);
        batch.enableBlending();
        frameBuffer.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setShader(Palette4.makeShader());
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        viewport.apply();
        palette.bind(batch.getShader());
        batch.draw(texture, 0, 0);
        batch.end();
        Pixmap answer = ScreenUtils.getFrameBufferPixmap(0, 0, frameBuffer.getWidth(), frameBuffer.getHeight());
        frameBuffer.end();
        batch.dispose();
        frameBuffer.dispose();
        return answer;
    }
}
