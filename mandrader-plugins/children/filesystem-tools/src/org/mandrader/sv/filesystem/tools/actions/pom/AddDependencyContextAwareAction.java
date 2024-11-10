package org.mandrader.sv.filesystem.tools.actions.pom;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JEditorPane;
import javax.swing.JMenuItem;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.mandrader.sv.filesystem.tools.utils.EditorCookieUtil;
import org.openide.cookies.EditorCookie;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.actions.Presenter;

public class AddDependencyContextAwareAction extends AbstractAction
        implements Presenter.Popup {

  private final EditorCookie context;

  public AddDependencyContextAwareAction(Lookup ctx) {
    context = ctx.lookup(EditorCookie.class);
    putValue(NAME, Bundle.CTL_AddDependencyMethodAction());
    DataObject dobj = ctx.lookup(DataObject.class);
    String path = dobj.getPrimaryFile().getPath();
    if (path.endsWith("pom.xml")) {
      setEnabled(true);
    } else {
      setEnabled(false);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (context != null) {
      JEditorPane jEditorPane = EditorCookieUtil.getEditorPane(context);
      if (jEditorPane != null) {
        StyledDocument document = context.getDocument();
        if (document != null) {
          int selstarts = jEditorPane.getSelectionStart();
          int selends = jEditorPane.getSelectionEnd();
          try {
            if (selends > selstarts) {
              document.remove(selstarts, selends - selstarts);
            }
            document.insertString(selstarts,
                    getTemplate(),
                    null);
          } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
          }
        }
      }
    }
  }

  @Override
  public JMenuItem getPopupPresenter() {
    JMenuItem ret = new JMenuItem(this);
    ret.setVisible(this.isEnabled());
    return ret;
  }

  private String getTemplate() {
    return "\t<dependency>"
            + "\n\t</dependency>";
  }
}
