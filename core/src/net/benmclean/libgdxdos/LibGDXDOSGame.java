package net.benmclean.libgdxdos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class LibGDXDOSGame extends ApplicationAdapter {
    public static final int VIRTUAL_WIDTH = 640;
    public static final int VIRTUAL_HEIGHT = 400;

    private Skin skin;
    private Stage stage;
    protected ShaderProgram shader;
    protected Palette4 uiPalette;

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("DOS/uiskin.json"));
        stage = new Stage(new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT));
        shader = new ShaderProgram(Palette4.vertexShader, Palette4.fragmentShaderYieldTransparency);
        if (!shader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());

        uiPalette = new Palette4(
                0, 0, 0, 255,
                0, 0, 127, 255,
                0, 0, 255, 255,
                255, 255, 255, 255
        );

        final TextButton button = new TextButton("BUTTON", skin, "default");

        final Dialog dialog = new Dialog("", skin);

        dialog.add(new Label("Message", skin));

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.show(stage);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        dialog.hide();
                    }
                }, 3);
            }
        });
        stage.addActor(button);

        Gdx.input.setInputProcessor(stage);

        stage.getBatch().setShader(shader);
        stage.getBatch().begin();
        uiPalette.bind(stage.getBatch().getShader());
        stage.getBatch().end();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }
}
