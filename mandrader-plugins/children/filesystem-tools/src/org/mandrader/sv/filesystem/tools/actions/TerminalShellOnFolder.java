package org.mandrader.sv.filesystem.tools.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

@ActionID(category = "Project", id = "org.mandrader.sv.filesystem.tools.actions.TerminalShellOnFolder")
@ActionRegistration(displayName = "#CTL_TerminalShellOnFolder", lazy = false)
@ActionReferences({
    @ActionReference(path = "Projects/Actions", position = 11),
    @ActionReference(path = "Loaders/folder/any/Actions", position = 11),
    @ActionReference(path = "Editors/TabActions", position = 11),
    @ActionReference(path = "Editors/Toolbars/Default", position = 11)
})
@NbBundle.Messages("CTL_TerminalShellOnFolder=Launch Terminal")
public class TerminalShellOnFolder
        extends AbstractAction
        implements ContextAwareAction {

    @Override
    public void actionPerformed(ActionEvent e) {
        assert false;
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new TerminalShellOnFolder.ContextAction(lkp);
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
                putValue(NAME, Bundle.CTL_TerminalShellOnFolder());
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
                if (context.isFolder()) {
                    folderPath = context.getPath();
                } else if (parent != null && parent.isFolder()) {
                    folderPath = parent.getPath();
                }
                if (folderPath != null) {
                    try {
                        final Map<String, String> env = new HashMap<>(System.getenv());
                        env.put("JAVA_HOME", "C:\\Tools\\JDK8");
                        env.put("ANT_HOME", "C:\\Tools\\apache-ant-1.9.16");
                        env.put("WL_HOME", "C:\\Tools\\Oracle\\Middleware\\wlserver_10.3");
                        fixPath(env, "JAVA_HOME", "ANT_HOME");
                        fixClassPath(env);
                        String[] envp = mapToStringArray(env);
                        Runtime.getRuntime().exec(
                                String.format("cmd /c start cmd.exe /K cd \"%s\"", folderPath),
                                envp);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
        }

        static void fixPath(Map<String, String> env, String... keys) {
            if (keys != null && keys.length > 0) {
                String path = env.get("Path");
                for (String key : keys) {
                    path += addToPath(env, key);
                }
                env.put("Path", path);
            }
        }

        static void fixClassPath(Map<String, String> env) {
//            String userHome = System.getProperty("user.home");
//            String antHome = env.get("ANT_HOME");
//            String javaHome = env.get("JAVA_HOME");
//            String wlHome = env.get("WL_HOME");
//            String classpath = javaHome + "\\lib\\tools.jar";
//            classpath += ";" + wlHome + "\\server\\lib\\weblogic.jar";
//            List<String> filesInAntLibFolder = findFileNames(antHome + "\\lib");
//            if (filesInAntLibFolder != null && !filesInAntLibFolder.isEmpty()) {
//                for (String fn : filesInAntLibFolder) {
//                    if (fn.trim().endsWith(".jar")) {
//                        classpath += ";" + antHome + "\\lib\\" + fn;
//                    }
//                }
//            }
//            classpath += ";" + userHome + "\\.m2\\repository\\weblogic\\1035\\com.bea.core.xml.xmlbeans\\2.2.0.0\\com.bea.core.xml.xmlbeans-2.2.0.0.jar";
//            //JOptionPane.showMessageDialog(null, "CLASSPATH: " + classpath);
//            env.put("CLASSPATH", classpath);
            env.put("CLASSPATH", "C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\org.apache.ant\\1.10.5.0.0\\apache-ant-1.10.5\\lib\\ant-launcher.jar;C:\\Tools\\JDK8\\lib\\tools.jar;C:\\Tools\\wls12214\\wlserver\\server\\lib\\weblogic.jar;C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\ant-contrib-1.0b3.jar;C:\\Tools\\wls12214\\wlserver\\modules\\features\\oracle.wls.common.nodemanager.jar;C:\\Tools\\wls12214\\wlserver\\common\\derby\\lib\\derbynet.jar;C:\\Tools\\wls12214\\wlserver\\common\\derby\\lib\\derbyclient.jar;C:\\Tools\\wls12214\\wlserver\\common\\derby\\lib\\derby.jar;C:\\Tools\\wls12214\\USER_P~1\\domains\\BASE_D~1\\telus_app-config\\;C:\\Tools\\JDK8\\lib\\tools.jar;C:\\Tools\\APACHE~1.16\\lib\\ANT-AN~1.JAR;C:\\Tools\\APACHE~1.16\\lib\\ANT-AP~1.JAR;C:\\Tools\\APACHE~1.16\\lib\\ANT-AP~2.JAR;C:\\Tools\\APACHE~1.16\\lib\\ANT-AP~3.JAR;C:\\Tools\\APACHE~1.16\\lib\\ANT-AP~4.JAR;C:\\Tools\\APACHE~1.16\\lib\\AN60D5~1.JAR;C:\\Tools\\APACHE~1.16\\lib\\AN4C3D~1.JAR;C:\\Tools\\APACHE~1.16\\lib\\AN963C~1.JAR;C:\\Tools\\APACHE~1.16\\lib\\ANT-CO~1.JAR;C:\\Tools\\APACHE~1.16\\lib\\ANT-CO~2.JAR;C:\\Tools\\APACHE~1.16\\lib\\ant-jai.jar;C:\\Tools\\APACHE~1.16\\lib\\ANT-JA~1.JAR;C:\\Tools\\APACHE~1.16\\lib\\ANT-JD~1.JAR;C:\\Tools\\APACHE~1.16\\lib\\ant-jmf.jar;C:\\Tools\\APACHE~1.16\\lib\\ant-jsch.jar;C:\\Tools\\APACHE~1.16\\lib\\ANT-JU~1.JAR;C:\\Tools\\APACHE~1.16\\lib\\ANT-JU~2.JAR;C:\\Tools\\APACHE~1.16\\lib\\ANT-LA~1.JAR;C:\\Tools\\APACHE~1.16\\lib\\ANT-NE~1.JAR;C:\\Tools\\APACHE~1.16\\lib\\ANT-SW~1.JAR;C:\\Tools\\APACHE~1.16\\lib\\ANT-TE~1.JAR;C:\\Tools\\APACHE~1.16\\lib\\ant.jar;C:\\Users\\MARLON~1.AND\\M2E4AB~1\\REPOSI~1\\weblogic\\1035\\COMBEA~1.XML\\220~1.0\\COMBEA~1.JAR;C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\org.apache.ant\\1.10.5.0.0\\apache-ant-1.10.5\\lib\\ant-antlr.jar;C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\org.apache.ant\\1.10.5.0.0\\apache-ant-1.10.5\\lib\\ant-apache-bcel.jar;C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\org.apache.ant\\1.10.5.0.0\\apache-ant-1.10.5\\lib\\ant-apache-bsf.jar;C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\org.apache.ant\\1.10.5.0.0\\apache-ant-1.10.5\\lib\\ant-apache-log4j.jar;C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\org.apache.ant\\1.10.5.0.0\\apache-ant-1.10.5\\lib\\ant-apache-oro.jar;C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\org.apache.ant\\1.10.5.0.0\\apache-ant-1.10.5\\lib\\ant-apache-regexp.jar;C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\org.apache.ant\\1.10.5.0.0\\apache-ant-1.10.5\\lib\\ant-apache-resolver.jar;C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\org.apache.ant\\1.10.5.0.0\\apache-ant-1.10.5\\lib\\ant-apache-xalan2.jar;C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\org.apache.ant\\1.10.5.0.0\\apache-ant-1.10.5\\lib\\ant-commons-logging.jar;C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\org.apache.ant\\1.10.5.0.0\\apache-ant-1.10.5\\lib\\ant-commons-net.jar;C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\org.apache.ant\\1.10.5.0.0\\apache-ant-1.10.5\\lib\\ant-jai.jar;C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\org.apache.ant\\1.10.5.0.0\\apache-ant-1.10.5\\lib\\ant-javamail.jar;C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\org.apache.ant\\1.10.5.0.0\\apache-ant-1.10.5\\lib\\ant-jdepend.jar;C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\org.apache.ant\\1.10.5.0.0\\apache-ant-1.10.5\\lib\\ant-jmf.jar;C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\org.apache.ant\\1.10.5.0.0\\apache-ant-1.10.5\\lib\\ant-jsch.jar;C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\org.apache.ant\\1.10.5.0.0\\apache-ant-1.10.5\\lib\\ant-junit.jar;C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\org.apache.ant\\1.10.5.0.0\\apache-ant-1.10.5\\lib\\ant-junit4.jar;C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\org.apache.ant\\1.10.5.0.0\\apache-ant-1.10.5\\lib\\ant-junitlauncher.jar;C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\org.apache.ant\\1.10.5.0.0\\apache-ant-1.10.5\\lib\\ant-launcher.jar;C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\org.apache.ant\\1.10.5.0.0\\apache-ant-1.10.5\\lib\\ant-netrexx.jar;C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\org.apache.ant\\1.10.5.0.0\\apache-ant-1.10.5\\lib\\ant-swing.jar;C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\org.apache.ant\\1.10.5.0.0\\apache-ant-1.10.5\\lib\\ant-testutil.jar;C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\org.apache.ant\\1.10.5.0.0\\apache-ant-1.10.5\\lib\\ant-xz.jar;C:\\Tools\\wls12214\\oracle_common\\modules\\thirdparty\\org.apache.ant\\1.10.5.0.0\\apache-ant-1.10.5\\lib\\ant.jar;C:\\Tools\\JDK8\\lib\\tools.jar");
        }

        public static List<String> findFileNames(String folderPath) {
            List<String> ret = new ArrayList<>();
            File folder = new File(folderPath);
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    ret.add(listOfFiles[i].getName().trim());
                }
            }
            return ret;
        }

        static String addToPath(Map<String, String> env, String key, boolean addBin) {
            return ";" + env.get(key) + (addBin ? "\\bin" : "");
        }

        static String addToPath(Map<String, String> env, String key) {
            return addToPath(env, key, true);
        }

        static String[] mapToStringArray(Map<String, String> map) {
            final String[] strings = new String[map.size()];
            int i = 0;
            for (Map.Entry<String, String> e : map.entrySet()) {
                strings[i] = e.getKey() + '=' + e.getValue();
                i++;
            }
            return strings;
        }

        @Override
        public Component getToolbarPresenter() {
            ImageIcon btnIcon = new ImageIcon(
                    TerminalShellOnFolder.class.getResource("/org/mandrader/sv/filesystem/tools/resources/images/console.png"));
            JButton btn = new JButton(this);
            btn.setText("");
            btn.setToolTipText(Bundle.CTL_TerminalShellOnFolder());
            btn.setIcon(btnIcon);
            return btn;
        }
    }
}
