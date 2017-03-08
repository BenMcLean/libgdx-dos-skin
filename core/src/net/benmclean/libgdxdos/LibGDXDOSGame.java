package net.benmclean.libgdxdos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class LibGDXDOSGame extends ApplicationAdapter {
    public static final int VIRTUAL_WIDTH = 1280;
    public static final int VIRTUAL_HEIGHT = 720;

    private Skin skin;
    private Skin defaultSkin;
    private Stage stage;
    protected ShaderProgram shader;
    protected Palette4 uiPalette;
    private boolean applyShader = false;
    private boolean shaderChanged = true;

    @Override
    public void create() {
        shader = new ShaderProgram(Palette4.vertexShader, Palette4.fragmentShaderYieldTransparency);
        if (!shader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());
        uiPalette = Palette4.blueUI();

        skin = new Skin(Gdx.files.internal("DOS/uiskin.json"));
        defaultSkin = new Skin(Gdx.files.internal("default/uiskin.json"));

        stage = new Stage(new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT));

        final VerticalGroup group = new VerticalGroup();
        group.space(16);


        // SCROLL PANE CODE BEGINS HERE

        Table container = new Table();
        stage.addActor(container);
        container.setFillParent(true);

        Table table = new Table();

        final ScrollPane scroll = new ScrollPane(table, skin);

        scroll.setForceScroll(true, true);

        InputListener stopTouchDown = new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                event.stop();
                return false;
            }
        };

        table.pad(10).defaults().expandX().space(4);
        for (int i = 0; i < 10; i++) {
            table.row();
            table.add(new Label(i + "uno", skin)).expandX().fillX();

            TextButton button = new TextButton(i + "dos", skin);
            table.add(button);
            button.addListener(new ClickListener() {
                public void clicked (InputEvent event, float x, float y) {
                    System.out.println("click " + x + ", " + y);
                }
            });

            Slider slider = new Slider(0, 100, 1, false, skin);
            slider.addListener(stopTouchDown); // Stops touchDown events from propagating to the FlickScrollPane.
            table.add(slider);

            table.add(new Label(i + "tres long0 long1 long2 long3 long4 long5 long6 long7 long8 long9 long10 long11 long12", skin));
        }

        scroll.setSize(100, 16);
        scroll.setOverscroll(false, false);
        scroll.invalidate();
        group.addActor(scroll);

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
