package net.benmclean.libgdxdos;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by Benjamin on 4/7/2017.
 */
public class RecoloredAtlas {
    public static final int transparent = Color.rgba8888(0f, 0f, 0f, 0f);

    public static TextureAtlas repackAtlas(TextureAtlas atlas, Palette4 palette) {
        PixmapPacker packer = new PixmapPacker(1024, 1024, Pixmap.Format.RGBA8888, 0, false);
        packIn(atlas, packer, palette);
        TextureAtlas textureAtlas = packer.generateTextureAtlas(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest, false);
        for (TextureAtlas.AtlasRegion region : textureAtlas.getRegions()) {
            TextureAtlas.AtlasRegion raw = atlas.findRegion(region.name);
            if (raw.pads != null) {
                region.pads = new int[raw.pads.length];
                System.arraycopy(raw.pads, 0, region.pads, 0, raw.pads.length);
            }
            if (raw.splits != null) {
                region.splits = new int[raw.splits.length];
                System.arraycopy(raw.splits, 0, region.splits, 0, raw.splits.length);
            }
        }
        return textureAtlas;
    }

    public static void packIn(TextureAtlas raw, PixmapPacker packer) {
        packIn("", raw, packer);
    }

    public static void packIn(String category, TextureAtlas raw, PixmapPacker packer) {
        for (TextureAtlas.AtlasRegion region : raw.getRegions())
            if (region.name.startsWith(category))
                packIn(region, packer);
    }

    public static void packIn(TextureAtlas.AtlasRegion region, PixmapPacker packer) {
        Texture texture = region.getTexture();
        if (!texture.getTextureData().isPrepared()) texture.getTextureData().prepare();
        Pixmap pixmap = texture.getTextureData().consumePixmap();

        Pixmap result = new Pixmap(region.getRegionWidth(), region.getRegionHeight(), Pixmap.Format.RGBA8888);
        for (int x = 0; x < region.getRegionWidth(); x++)
            for (int y = 0; y < region.getRegionHeight(); y++)
                result.drawPixel(x, y, pixmap.getPixel(region.getRegionX() + x, region.getRegionY() + y));

        packer.pack(region.toString(), result);
        texture.dispose();
    }

    public static void packIn(TextureAtlas raw, PixmapPacker packer, Palette4 palette) {
        packIn("", raw, packer, palette);
    }

    public static void packIn(String category, TextureAtlas raw, PixmapPacker packer, Palette4 palette) {
        for (TextureAtlas.AtlasRegion region : raw.getRegions())
            if (region.name.startsWith(category))
                packIn(region, packer, palette);
    }

    public static void packIn(TextureAtlas.AtlasRegion region, PixmapPacker packer, Palette4 palette) {
        Texture texture = region.getTexture();
        if (!texture.getTextureData().isPrepared()) texture.getTextureData().prepare();
        Pixmap pixmap = texture.getTextureData().consumePixmap();
        Pixmap result = new Pixmap(region.getRegionWidth(), region.getRegionHeight(), Pixmap.Format.RGBA8888);
        Color color = new Color();
        for (int x = 0; x < region.getRegionWidth(); x++)
            for (int y = 0; y < region.getRegionHeight(); y++) {
                color.set(pixmap.getPixel(region.getRegionX() + x, region.getRegionY() + y));
                if (color.a > .05)
                    result.drawPixel(x, y, Color.rgba8888(palette.get((int) (color.r * 3.9999))));
                else
                    result.drawPixel(x, y, transparent);
            }
        packer.pack(region.toString(), result);
        texture.dispose();
    }
}
