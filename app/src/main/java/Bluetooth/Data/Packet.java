package Bluetooth.Data;

import java.util.ArrayList;

/**
 * Created by 우철 on 2016-07-09.
 */
public class Packet {
    private ArrayList<Keyval> auth_info;
    private int cmd;

    public Packet(){
        auth_info = new ArrayList<Keyval>();
    }

    public ArrayList<Keyval> getAuthinfo() {
        return auth_info;
    }

    private void setAuthinfo(ArrayList<Keyval> auth_info) {
        this.auth_info = auth_info;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }
}
