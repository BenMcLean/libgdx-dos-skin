package net.benmclean.libgdxdos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class LibGDXDOSGame extends ApplicationAdapter {
    public static final int VIRTUAL_WIDTH = 640;
    public static final int VIRTUAL_HEIGHT = 480;

    private Skin skin;
    private Skin defaultSkin;
    private Stage stage;
    protected ShaderProgram shader;
    protected Palette4 uiPalette;
    private boolean applyShader = false;
    private boolean shaderChanged = true;

    Texture recolored;

    @Override
    public void create() {
        shader = Palette4.makeShader();
        uiPalette = Palette4.blueUI();

        skin = new Skin(Gdx.files.internal("DOS/uiskin.json"));
        defaultSkin = new Skin(Gdx.files.internal("default/uiskin.json"));

        stage = new Stage(new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT));

        Texture texture = new Texture(Gdx.files.internal("DOS/uiskin.png"));

        recolored = new Texture(RecoloredAtlas.recolor(texture, uiPalette));

        final VerticalGroup group = new VerticalGroup();
        group.space(16);

        final Image image = new Image(recolored);
        group.addActor(image);

        final CheckBox checkBox = new CheckBox("Apply Shader", skin);
        checkBox.setChecked(applyShader);
        checkBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setShader(checkBox.isChecked());
            }
        });
        group.addActor(checkBox);

        final CheckBox debugCheckBox = new CheckBox("Enable Debug Rendering", skin);
        debugCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.setDebugAll(debugCheckBox.isChecked());
            }
        });
        group.addActor(debugCheckBox);

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

    public void setShader(boolean applyShader) {
        shaderChanged = true;
        this.applyShader = applyShader;
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
