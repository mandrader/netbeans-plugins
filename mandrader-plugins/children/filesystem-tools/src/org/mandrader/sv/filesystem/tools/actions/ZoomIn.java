package org.mandrader.sv.filesystem.tools.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.NAME;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import org.mandrader.sv.filesystem.tools.utils.EditorUtil;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.Presenter;

@ActionID(category = "Project", id = "org.mandrader.sv.filesystem.tools.actions.ZoomIn")
@ActionRegistration(displayName = "#CTL_ZoomIn", lazy = false)
@ActionReferences({
    @ActionReference(path = "Editors/TabActions", position = 12),
    @ActionReference(path = "Editors/Toolbars/Default", position = 12)
})
@NbBundle.Messages("CTL_ZoomIn=Zoom In")
public class ZoomIn
        extends AbstractAction
        implements ContextAwareAction {

    @Override
    public void actionPerformed(ActionEvent e) {
        assert false;
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new ZoomIn.ContextAction(lkp);
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
                putValue(NAME, Bundle.CTL_ZoomIn());
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
                Action action = EditorUtil.getZoomIn();
                if (null != action) {
                    action.actionPerformed(e);
                }
            }
        }

        @Override
        public Component getToolbarPresenter() {
            ImageIcon btnIcon = new ImageIcon(
                    ZoomIn.class.getResource("/org/mandrader/sv/filesystem/tools/resources/images/zoom-in.png"));
            JButton btn = new JButton(this);
            btn.setText("");
            btn.setToolTipText(Bundle.CTL_ZoomIn());
            btn.setIcon(btnIcon);
            return btn;
        }
    }
}