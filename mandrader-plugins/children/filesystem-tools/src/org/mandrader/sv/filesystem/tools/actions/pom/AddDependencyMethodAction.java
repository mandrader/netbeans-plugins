package org.mandrader.sv.filesystem.tools.actions.pom;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Tools", id = "org.mandrader.sv.filesystem.tools.actions.AddDependencyMethodAction")
@ActionRegistration(displayName = "#CTL_AddDependencyMethodAction", lazy = false)
@ActionReference(path = "Editors/text/xml/Popup", position = 800)
@Messages("CTL_AddDependencyMethodAction=Add Dependency Section")
public final class AddDependencyMethodAction extends AbstractAction
        implements ContextAwareAction {

  @Override
  public void actionPerformed(ActionEvent e) {
    assert false;
  }

  @Override
  public Action createContextAwareInstance(Lookup lkp) {
    return new AddDependencyContextAwareAction(lkp);
  }

}
