package net.benmclean.libgdxdos.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import java.io.File;

public class ArtPacker {
    public static void main(final String[] args) throws Exception {
        TexturePacker.process("../assets-raw/DOS/", ".", "DOS/uiskin");
    }

    private static void delete(final File delete) {
        if (delete.isDirectory()) {
            for (final File file : delete.listFiles()) {
                delete(file);
            }
        }
        delete.delete();
    }
}
