package Bluetooth.Data;

/**
 * Created by 우철 on 2016-07-10.
 */
public class Recvdata {
    private String addr;
    private byte[] buffer;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }
}
