package net.benmclean.libgdxdos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class sscce extends ApplicationAdapter {
    public static final int VIRTUAL_WIDTH = 640;
    public static final int VIRTUAL_HEIGHT = 480;

    private Stage stage;
    private Skin skin;

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("default/uiskin.json"));

        stage = new Stage(new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT));

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        Pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(Color.BLACK);
        pixmap.drawPixel(0, 0);
        TextureRegionDrawable black = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

        final TextField.TextFieldStyle normal = new TextField.TextFieldStyle(
                new BitmapFont(),
                //skin.getFont("default-font"),
                Color.WHITE,
                skin.getDrawable("cursor"),
                skin.getDrawable("selection"),
                skin.getDrawable("textfield")
        );

        final TextField.TextFieldStyle custom = new TextField.TextFieldStyle(
                new BitmapFont(),
                //skin.getFont("default-font"),
                Color.WHITE,
                skin.getDrawable("cursor"),
                skin.getDrawable("selection"),
                black
        );

        final VerticalGroup group = new VerticalGroup();
        group.space(16);

        final TextField textField = new TextField("Normal TextField", normal);
        group.addActor(textField);
        final TextField textField2 = new TextField("Custom TextField", custom);
        group.addActor(textField2);
        final TextArea textArea = new TextArea("Normal TextArea", normal);
        group.addActor(textArea);
        final TextArea textArea2 = new TextArea("Custom TextArea", custom);
        group.addActor(textArea2);

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
