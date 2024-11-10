package org.mandrader.sv.filesystem.tools.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.NAME;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import org.netbeans.modules.project.ui.ProjectTab;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.Presenter;

@ActionID(category = "Project", id = "org.mandrader.sv.filesystem.tools.actions.CTL_SelectNode")
@ActionRegistration(displayName = "#CTL_SelectNode", lazy = false)
@ActionReferences({
    @ActionReference(path = "Editors/Toolbars/Default", position = -1000000001)
})
@NbBundle.Messages("CTL_SelectNode=Select Node")
public class SelectNode
        extends AbstractAction
        implements ContextAwareAction {

    private static final Logger LOG
            = Logger.getLogger(SelectNode.class.getName());

    @Override
    public void actionPerformed(ActionEvent e) {
        assert false;
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new SelectNode.ContextAction(lkp);
    }

    public static final class ContextAction
            extends AbstractAction
            implements Presenter.Toolbar {

        private final FileObject context;

        public ContextAction(Lookup ctx) {
            context = ctx.lookup(FileObject.class);
            setEnabled(false);
            if (context != null) {
                putValue(NAME, Bundle.CTL_SelectNode());
                setEnabled(true);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (context != null) {
                StatusDisplayer.getDefault().setStatusText("Looking for the node. Please wait...");
                FileObject fo2 = FileUtil.toFileObject(FileUtil.toFile(context));
                ProjectTab pt = ProjectTab.findDefault(ProjectTab.ID_LOGICAL);
                pt.selectNodeAsync(fo2);
            }
        }

        @Override
        public Component getToolbarPresenter() {
            ImageIcon btnIcon = new ImageIcon(
                    SelectNode.class.getResource("/org/mandrader/sv/filesystem/tools/resources/images/tree.png"));
            JButton btn = new JButton(this);
            btn.setText("");
            btn.setToolTipText(Bundle.CTL_SelectNode());
            btn.setIcon(btnIcon);
            return btn;
        }
    }
}
