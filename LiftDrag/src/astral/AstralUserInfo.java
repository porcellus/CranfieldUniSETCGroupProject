/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package astral;

import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author madfist
 */
public class AstralUserInfo implements UserInfo, UIKeyboardInteractive {
    public AstralUserInfo(String pw)
    {
        password = pw;
    }

    @Override
    public String getPassphrase() {
        return "";
    }

    @Override
    public String getPassword() {
        System.out.println("pass");
        return password;
    }

    @Override
    public boolean promptPassword(String message) {
        System.out.println("pass?");
        return true;
    }

    @Override
    public boolean promptPassphrase(String message) {
        System.out.println("passphrase?");
        return false;
    }

    @Override
    public boolean promptYesNo(String message) {
        return true;
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) {
        String[] result = new String[prompt.length];
        if (prompt.length > 1) {
            JPanel optionPanel = new JPanel(new GridLayout(prompt.length, 2));
            JTextField[] fields = new JTextField[prompt.length];
            for (int i=0; i<prompt.length; ++i) {
                optionPanel.add(new JLabel(prompt[i]));
                if (echo[i]) {
                    fields[i] = new JTextField(10);
                } else {
                    fields[i] = new JPasswordField(10);
                }
                optionPanel.add(fields[i]);
            }
            int option = JOptionPane.showConfirmDialog(null, optionPanel, "SSH prompt", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                for (int i=0; i<prompt.length; ++i) {
                    result[i] = fields[i].getText();
                }
            } else {
                result = null;
            }
        } else {
            if (prompt.length == 1 && prompt[0].contains("Password")) {
                System.out.println("Skipping keyboard interactive password prompt");
                result[0] = password;
            }
        }
        return result;
    }
    
    private final String password;
}
