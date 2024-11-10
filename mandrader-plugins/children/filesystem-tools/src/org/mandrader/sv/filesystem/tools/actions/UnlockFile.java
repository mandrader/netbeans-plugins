package org.mandrader.sv.filesystem.tools.actions;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.NAME;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.text.StyledDocument;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.ContextAwareAction;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;

@ActionID(category = "Project", id = "org.mandrader.sv.filesystem.tools.actions.UnlockFile")
@ActionRegistration(displayName = "#CTL_UnlockFile", lazy = false)
@ActionReferences({
    @ActionReference(path = "Editors/TabActions", position = 14),
    @ActionReference(path = "Editors/Toolbars/Default", position = 14)
})
@Messages("CTL_UnlockFile=Unlock File")
public final class UnlockFile
        extends AbstractAction
        implements ContextAwareAction {

    @Override
    public void actionPerformed(ActionEvent e) {
        assert false;
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new UnlockFile.ContextAction(lkp);
    }

    private static final class ContextAction
            extends AbstractAction
            implements Presenter.Popup,
            Presenter.Toolbar {

        private final FileObject context;

        public ContextAction(Lookup ctx) {
            context = ctx.lookup(FileObject.class);
            setEnabled(false);
            if (context != null) {
                putValue(NAME, Bundle.CTL_UnlockFile());
                setEnabled(true);
            }
        }

        @Override
        public JMenuItem getPopupPresenter() {
            JMenuItem ret = new JMenuItem(this);
            ret.setVisible(this.isEnabled());
            return ret;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (context != null) {
                String filePath = context.getPath();
                DataObject dataLookup = context.getLookup().lookup(DataObject.class);
                if (dataLookup != null) {
                    EditorCookie editorCookie = dataLookup.getLookup().lookup(EditorCookie.class);
                    if (editorCookie != null) {
                        JEditorPane jEditorPane = getEditorPane(editorCookie);
                        if (jEditorPane != null) {
                            StyledDocument document = editorCookie.getDocument();
                            unlockFile(filePath, document, jEditorPane, editorCookie, dataLookup);
                        }
                    }
                }
            }
        }

        private void unlockFile(String filePath, StyledDocument document, JEditorPane jEditorPane, EditorCookie editorCookie, DataObject dataLookup) {
            boolean useClipboard = true;
            long counter = 0;
            try {
                int currentPosition = jEditorPane.getCaretPosition();
                int length = document.getLength();
                String content = document.getText(0, length);
                StringBuilder sb = new StringBuilder();
                try (Scanner scanner = new Scanner(content)) {
                    boolean isWsdl = filePath.endsWith(".wsdl");
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        boolean hasNextLine = scanner.hasNextLine();
                        if (isWsdl) {
                            if (!line.trim().equals("")) {
                                if (line.trim().equals("<WL5G3N6:Policy>")) {
                                    line = line.replace("<WL5G3N6:Policy>", "<!-- <WL5G3N6:Policy>");
                                    counter++;
                                } else if (line.trim().equals("</WL5G3N6:Policy>")) {
                                    line = line.replace("</WL5G3N6:Policy>", "</WL5G3N6:Policy> -->");
                                    counter++;
                                } else if (line.trim().equals("<wsp:Policy>")) {
                                    line = line.replace("<wsp:Policy>", "<!-- <wsp:Policy>");
                                    counter++;
                                } else if (line.trim().equals("</wsp:Policy>")) {
                                    line = line.replace("</wsp:Policy>", "</wsp:Policy> -->");
                                    counter++;
                                }
                                sb.append(line);
                                if (hasNextLine) {
                                    sb.append("\n");
                                }
                            }
                        } else {
                            sb.append(line);
                            if (hasNextLine) {
                                sb.append("\n");
                            }
                        }
                    }
                }
                if (!useClipboard) {
                    jEditorPane.setText(sb.toString());
                } else {
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    StringSelection stringSelection = new StringSelection(sb.toString());
                    clipboard.setContents(stringSelection, null);
                    jEditorPane.setSelectionStart(0);
                    jEditorPane.setSelectionEnd(length);
                    jEditorPane.paste();
                }
                jEditorPane.setCaretPosition(currentPosition);
                editorCookie.saveDocument();
                NotifyDescriptor nd = new NotifyDescriptor.Message("Unlocked. Counter: "+counter);
                DialogDisplayer.getDefault().notify(nd);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        @Override
        public Component getToolbarPresenter() {
            ImageIcon btnIcon = new ImageIcon(
                    UnlockFile.class.getResource("/org/mandrader/sv/filesystem/tools/resources/images/unlocked.png"));
            JButton btn = new JButton(this);
            btn.setText("");
            btn.setToolTipText(Bundle.CTL_UnlockFile());
            btn.setIcon(btnIcon);
            return btn;
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
}
