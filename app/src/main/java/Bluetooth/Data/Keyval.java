package Bluetooth.Data;

/**
 * Created by 우철 on 2016-07-09.
 */
public class Keyval{
    private String key;
    private String primeNum;
    private String date;

    public Keyval(String key, String primeNum){
        this.key = key;
        this.primeNum = primeNum;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPrimeNum() {
        return primeNum;
    }

    public void setPrimeNum(String primeNum) {
        this.primeNum = primeNum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}