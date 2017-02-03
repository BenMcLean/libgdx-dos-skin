package net.benmclean.libgdxdos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class LibGDXDOSGame extends ApplicationAdapter {
    public static final int VIRTUAL_WIDTH = 640;
    public static final int VIRTUAL_HEIGHT = 480;

    private Skin skin;
    private Stage stage;
    protected ShaderProgram shader;
    protected Palette4 uiPalette;
    public boolean applyShader = false;
    public boolean shaderChanged = true;

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("DOS/uiskin.json"));
        stage = new Stage(new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT));
        shader = new ShaderProgram(Palette4.vertexShader, Palette4.fragmentShaderYieldTransparency);
        if (!shader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());

        uiPalette = Palette4.blueUI();

        final VerticalGroup group = new VerticalGroup();
        group.space(16);
        group.addActor(new Label("Label", skin));

        final TextButton shaderButton = new TextButton("TextButton", skin, "default");

        group.addActor(shaderButton);

        final CheckBox checkBox = new CheckBox("Apply Shader", skin);
        checkBox.setChecked(applyShader);

        checkBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                applyShader = checkBox.isChecked();
                shaderChanged = true;
            }
        });

        group.addActor(checkBox);

        final Window window = new Window("", skin);
        window.add(group);

        window.setSize(window.getPrefWidth(), window.getPrefHeight());

        stage.addActor(window);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render() {
        if (shaderChanged) {
            shade();
            shaderChanged = false;
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    public void shade() {
        shade(applyShader);
    }

    public void shade(boolean applyShader) {
        if (applyShader) {
            stage.getBatch().setShader(shader);
            stage.getBatch().begin();
            uiPalette.bind(stage.getBatch().getShader());
            stage.getBatch().end();
        } else {
            stage.getBatch().setShader(null);
        }
    }
}
