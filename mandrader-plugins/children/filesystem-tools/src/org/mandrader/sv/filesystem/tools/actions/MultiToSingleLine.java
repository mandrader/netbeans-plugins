package org.mandrader.sv.filesystem.tools.actions;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.mandrader.sv.filesystem.tools.panels.MultiToSingleLinePanel;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Edit",
        id = "org.mandrader.sv.filesystem.tools.actions.MultiToSingleLine"
)
@ActionRegistration(
        iconBase = "org/mandrader/sv/filesystem/tools/resources/images/horizontal-merge.png",
        displayName = "#CTL_MultiToSingleLine"
)
@ActionReference(path = "Toolbars/UndoRedo", position = 300)
@Messages("CTL_MultiToSingleLine=Muti to single line text")
public final class MultiToSingleLine implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        MultiToSingleLinePanel frame = new MultiToSingleLinePanel();
        DialogDescriptor descriptor = new DialogDescriptor(frame, "Multi to single line");
        Dialog dialog = DialogDisplayer.getDefault().createDialog(descriptor);
        dialog.setVisible(true);
        frame.setDialog(dialog);
    }
}
