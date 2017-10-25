package net.benmclean.libgdxdos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class LibGDXDOSGame extends ApplicationAdapter {
    public static final int VIRTUAL_WIDTH = 640;
    public static final int VIRTUAL_HEIGHT = 480;

    private Skin skin;
    private Stage stage;

    @Override
    public void create() {
        //skin = new Skin(Gdx.files.internal("DOS/uiskin.json"));
        stage = new Stage(new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT));
        skin = new Skin(
                Gdx.files.internal("DOS/uiskin.json"),
                RecoloredAtlas.repackAtlas(
                        new TextureAtlas(Gdx.files.internal("DOS/uiskin.atlas")),
                        Palette4.blueUI()
                )
        );

        final VerticalGroup group = new VerticalGroup();
        group.space(16);

        final CheckBox checkBox = new CheckBox("Apply Shader", skin);
//        checkBox.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                setShader(checkBox.isChecked());
//            }
//        });
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
