package org.mandrader.sv.filesystem.tools.actions;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.text.StyledDocument;
import org.mandrader.sv.filesystem.tools.model.ComboItem;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.EditorCookie;
import org.openide.loaders.DataObject;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

@ActionID(
        category = "Mandrader",
        id = "org.mandrader.sv.filesystem.tools.actions.JBIFixHtml5"
)
@ActionRegistration(
        iconBase = "org/mandrader/sv/filesystem/tools/resources/images/file-check.png",
        displayName = "#CTL_JBIFixHtml5"
)
@ActionReference(path = "Toolbars/Mandrader",
        position = 300)
@Messages("CTL_JBIFixHtml5=Check the current file")
public final class JBIFixHtml5 implements ActionListener {

    protected final DataObject context;
    public static List<ComboItem> SPANISH_SYMBOLS = Arrays.asList(
            new ComboItem("space", "&nbsp;", "&#32;"),
            new ComboItem("á", "&aacute;", "&#225;"),
            new ComboItem("Á", "&Aacute;", "&#193;"),
            new ComboItem("é", "&eacute;", "&#233;"),
            new ComboItem("É", "&Eacute;", "&#201;"),
            new ComboItem("í", "&iacute;", "&#237;"),
            new ComboItem("Í", "&Iacute;", "&#205;"),
            new ComboItem("ó", "&oacute;", "&#243;"),
            new ComboItem("Ó", "&Oacute;", "&#211;"),
            new ComboItem("ú", "&uacute;", "&#250;"),
            new ComboItem("Ú", "&Uacute;", "&#218;"),
            new ComboItem("ñ", "&ntilde;", "&#241;"),
            new ComboItem("Ñ", "&Ntilde;", "&#209;"),
            new ComboItem("¿", "&iquest", "&#191;"),
            new ComboItem("¡", "&iexcl;", "&#161;"));

    public JBIFixHtml5(DataObject context) {
        this.context = context;
    }

    public static boolean isLatin1Supplement(String s) {
        return s.matches("\\p{InLATIN_1_SUPPLEMENT}+");
    }

    public static String toHtml(Character latin1) {
        return "&#" + String.format("%04d", ((int) latin1)) + ";";
    }

    private int countSpecialChars(StyledDocument document) {
        int ret = 0;
        int nsize = document.getLength();
        for (int i = 0; i < nsize; i++) {
            try {
                String s = document.getText(i, 1);
                if (isLatin1Supplement(s)) {
                    ret++;
                }
            } catch (Exception ex) {
                // nothing
            }
        }
        return ret;
    }

    private void fixFirstSpecialChar(StyledDocument document) {
        int nsize = document.getLength();
        for (int i = 0; i < nsize; i++) {
            try {
                String s = document.getText(i, 1);
                if (isLatin1Supplement(s)) {
                    document.remove(i, 1);
                    document.insertString(i, toHtml(s.charAt(0)), null);
                    break;
                }
            } catch (Exception ex) {
                // nothing
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent activeTC = TopComponent.getRegistry().getActivated();
        if (activeTC != null) {
            DataObject dataLookup = activeTC.getLookup().lookup(DataObject.class);
            if (dataLookup != null) {
                EditorCookie editorCookie = dataLookup.getLookup().lookup(EditorCookie.class);
                if (editorCookie != null) {
                    JEditorPane jEditorPane = getEditorPane(editorCookie);
                    if (jEditorPane != null) {
                        StyledDocument document = editorCookie.getDocument();
                        if (document != null) {
                            int selends = jEditorPane.getSelectionEnd();
                            int specialChars = countSpecialChars(document);
                            for (int i = 0; i < specialChars; i++) {
                                fixFirstSpecialChar(document);
                            }
                            jEditorPane.setSelectionStart(selends);
                            jEditorPane.setSelectionEnd(selends);
                            try {
                                editorCookie.saveDocument();
                            } catch (Exception ex) {
                                // nothing
                            }
                            NotifyDescriptor nd = new NotifyDescriptor.Message("Done. Replaced " + specialChars + " latin characters");
                            DialogDisplayer.getDefault().notify(nd);
                        }
                    }
                }
            }
        }

    }

    private static JEditorPane getEditorPane_(EditorCookie editorCookie) {
        JEditorPane ret = null;
        if (editorCookie != null) {
            JEditorPane[] op = editorCookie.getOpenedPanes();
            if ((op != null) && (op.length > 0)) {
                ret = op[0];
            }
        }
        return ret;
    }

    private static JEditorPane getEditorPane(final EditorCookie editorCookie) {
        if (SwingUtilities.isEventDispatchThread()) {
            return getEditorPane_(editorCookie);
        } else {
            final JEditorPane[] ce = new JEditorPane[1];
            try {
                EventQueue.invokeAndWait(
                        new Runnable() {

                    @Override
                    public void run() {
                        ce[0] = getEditorPane_(editorCookie);
                    }
                });
            } catch (InvocationTargetException | InterruptedException ex) {
            }
            return ce[0];
        }
    }

}
