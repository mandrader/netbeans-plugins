package org.mandrader.sv.filesystem.tools.utils;

import java.util.List;
import javax.swing.Action;
import org.netbeans.modules.editor.impl.EditorActionsProvider;

public class EditorUtil {

    private static Action ZOOM_IN;
    private static Action ZOOM_OUT;
    private static List<Action> ACTIONS = EditorActionsProvider.getEditorActions("text/html");

    public static Action getZoomIn() {
        if (null == ZOOM_IN) {
            for (Action action : ACTIONS) {
                Object name = action.getValue("Name");
                if ("zoom-text-in".equals(name)) {
                    ZOOM_IN = action;
                    break;
                }
            }
        }
        return ZOOM_IN;
    }

    public static Action getZoomOut() {
        if (null == ZOOM_OUT) {
            for (Action action : ACTIONS) {
                Object name = action.getValue("Name");
                if ("zoom-text-out".equals(name)) {
                    ZOOM_OUT = action;
                    break;
                }
            }
        }
        return ZOOM_OUT;
    }
}
