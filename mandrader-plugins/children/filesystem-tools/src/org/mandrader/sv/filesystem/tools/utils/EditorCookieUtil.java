package org.mandrader.sv.filesystem.tools.utils;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.openide.cookies.EditorCookie;
import org.openide.util.Exceptions;

public class EditorCookieUtil {

    public static void generateCode(EditorCookie editorCookie, String template) {
        StyledDocument document = EditorCookieUtil.getDocument(editorCookie);
        if (document != null) {
            JEditorPane jEditorPane = EditorCookieUtil.getEditorPane(editorCookie);
            int selstarts = jEditorPane.getSelectionStart();
            int selends = jEditorPane.getSelectionEnd();
            try {
                if (selends > selstarts) {
                    document.remove(selstarts, selends - selstarts);
                }
                document.insertString(selstarts,
                        template,
                        null);
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    public static StyledDocument getDocument(EditorCookie editorCookie) {
        StyledDocument ret = null;
        JEditorPane jEditorPane = EditorCookieUtil.getEditorPane(editorCookie);
        if (jEditorPane != null) {
            ret = editorCookie.getDocument();
        }
        return ret;
    }

    public static boolean isRestController(EditorCookie editorCookie) {
        String text = EditorCookieUtil.getEditorText(editorCookie);
        return (text != null && text.contains("@RestController"));
    }

    public static String getEditorText(EditorCookie editorCookie) {
        String ret = null;
        try {
            StyledDocument document = editorCookie.getDocument();
            ret = document.getText(0, document.getLength());
        } catch (Exception e) {
            // do nothing
        }
        return ret;
    }

    public static JEditorPane getEditorPane_(EditorCookie editorCookie) {
        JEditorPane ret = null;
        if (editorCookie != null) {
            JEditorPane[] op = editorCookie.getOpenedPanes();
            if ((op != null) && (op.length > 0)) {
                ret = op[0];
            }
        }
        return ret;
    }

    public static JEditorPane getEditorPane(final EditorCookie editorCookie) {
        if (SwingUtilities.isEventDispatchThread()) {
            return getEditorPane_(editorCookie);
        } else {
            final JEditorPane[] ce = new JEditorPane[1];
            try {
                EventQueue.invokeAndWait(() -> {
                    ce[0] = getEditorPane_(editorCookie);
                });
            } catch (InvocationTargetException | InterruptedException ex) {
            }
            return ce[0];
        }
    }
}
