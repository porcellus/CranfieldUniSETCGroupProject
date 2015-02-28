/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package astral;

import com.jcraft.jsch.UserInfo;

/**
 *
 * @author madfist
 */
public class AstralUserInfo implements UserInfo {
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
        
    }
    
    private String password;
}
