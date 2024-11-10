package org.mandrader.sv.filesystem.tools.actions;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import javax.swing.AbstractAction;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.mandrader.sv.filesystem.tools.panels.Utf8HtmlCodePanel;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.EditorCookie;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;
import org.openide.windows.TopComponent;

@ActionID(
        category = "Mandrader",
        id = "org.mandrader.sv.filesystem.tools.actions.AddUtf8HtmlCode"
)
@ActionRegistration(
        lazy = false,
        displayName = "#CTL_AddUtf8HtmlCode"
)
@ActionReference(path = "Toolbars/Mandrader", position = 400)
@Messages("CTL_AddUtf8HtmlCode=Add UTF-8 HTML Code")
public class JBIAddUtf8HtmlCode extends AbstractAction implements Presenter.Toolbar {

    private static final long serialVersionUID = 3266309461089990135L;
    Utf8HtmlCodePanel textoPanel = new Utf8HtmlCodePanel();

    @Override
    public void actionPerformed(ActionEvent evt) {
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
                            int selstarts = jEditorPane.getSelectionStart();
                            int selends = jEditorPane.getSelectionEnd();
                            try {
                                if (selends > selstarts) {
                                    document.remove(selstarts, selends - selstarts);
                                }
                                document.insertString(selstarts,
                                        textoPanel != null 
                                                && textoPanel.getSelectedItem() != null 
                                                && textoPanel.getSelectedItem().getValue() != null ? textoPanel.getSelectedItem().getValue().toString().trim() : "",
                                        null);
                            } catch (BadLocationException ex) {
                                Exceptions.printStackTrace(ex);
                            }
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

    @Override
    public Component getToolbarPresenter() {
        textoPanel.setOwner(this);
        return textoPanel;
    }
}
