package de.hawilux.mapper.net;

import java.util.ArrayList;

public class OscMessagePaths {
    // connect
    public final static String HELLO = "/mapper/hello";

    // info
    public final static String EFFECTLIST = "/mapper/info/effectlist";
    public final static String SELECTEDPOINT = "/mapper/info/selectedpoint";

    // mode
    public final static String MODEFILE = "/mapper/mode/file";
    public final static String MODESETUP = "/mapper/mode/setup";
    public final static String MODEEFFECT = "/mapper/mode/effect";
    // edit
    public final static String PREVIOUS = "/mapper/edit/previous";
    public final static String NEXT = "/mapper/edit/next";
    public final static String UP = "/mapper/edit/up";
    public final static String LEFT = "/mapper/edit/left";
    public final static String RIGHT = "/mapper/edit/right";
    public final static String DOWN = "/mapper/edit/down";
    public final static String HELPER = "/mapper/edit/helper";
    public final static String SELECTMODE = "/mapper/edit/selectmode";
    public final static String MOUSECOORDS = "/mapper/edit/mousecoords";
    public final static String MOUSEBUTTON = "/mapper/edit/mousebutton";

    // file
    public final static String NEWCONFIG = "/mapper/file/newconfig";
    public final static String REQFILES = "/mapper/file/requestfilelist";
    public final static String FILELIST = "/mapper/file/filelist";
    public final static String SAVECONFIG = "/mapper/file/saveconfig";
    public final static String LOADCONFIG = "/mapper/file/loadconfig";
    public final static String SAVEFILE = "/mapper/file/savefile";
    public final static String LOADFILE = "/mapper/file/loadfile";

    // effects
    private final static ArrayList<String> effectPaths = new ArrayList<String>();

    public static void addEffectPath(String effectName) {
        effectPaths.add("/mapper/effect/" + effectName);
    }

    public static ArrayList<String> getEffectPaths() {
        return effectPaths;
    }
}
