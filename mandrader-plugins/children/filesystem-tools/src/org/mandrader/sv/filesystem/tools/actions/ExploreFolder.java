package org.mandrader.sv.filesystem.tools.actions;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.NAME;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.util.ContextAwareAction;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.Presenter;

@ActionID(category = "Project", id = "org.mandrader.sv.filesystem.tools.actions.ExploreFolder")
@ActionRegistration(displayName = "#CTL_ExploreFolder", lazy = false)
@ActionReferences({
    @ActionReference(path = "Projects/Actions", position = 11),
    @ActionReference(path = "Loaders/folder/any/Actions", position = 11),
    @ActionReference(path = "Editors/TabActions", position = 11),
    @ActionReference(path = "Editors/Toolbars/Default", position = 11)
})
@NbBundle.Messages("CTL_ExploreFolder=Explore Folder")
public class ExploreFolder
        extends AbstractAction
        implements ContextAwareAction {

    @Override
    public void actionPerformed(ActionEvent e) {
        assert false;
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new ExploreFolder.ContextAction(lkp);
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
                putValue(NAME, Bundle.CTL_ExploreFolder());
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
                String folderPath = null;
                FileObject parent = context.getParent();
                Desktop desktop = Desktop.getDesktop();
                if (context.isFolder()) {
                    folderPath = context.getPath();
                } else if (parent != null && parent.isFolder()) {
                    folderPath = parent.getPath();
                }
                if (folderPath != null) {
                    try {
                        desktop.open(new File(folderPath));
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
        }

        @Override
        public Component getToolbarPresenter() {
            ImageIcon btnIcon = new ImageIcon(
                    ExploreFolder.class.getResource("/org/mandrader/sv/filesystem/tools/resources/images/symlink-directory.png"));
            JButton btn = new JButton(this);
            btn.setText("");
            btn.setToolTipText(Bundle.CTL_ExploreFolder());
            btn.setIcon(btnIcon);
            return btn;
        }
    }
}
