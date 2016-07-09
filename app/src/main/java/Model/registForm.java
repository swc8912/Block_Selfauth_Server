package Model;

/**
 * Created by user on 2016-07-10.
 */
public class registForm {

    private String iotAddr;
    private String keyValue;
    private String primeValue;
    public registForm(String addr, String key, String prime){
        iotAddr = addr;
        keyValue = key;
        primeValue = prime;
    }

    public String getIotAddr(){
        return iotAddr;
    }

    public String getKeyValue(){
        return keyValue;
    }

    public String getPrimeValue(){
        return primeValue;
    }

}
