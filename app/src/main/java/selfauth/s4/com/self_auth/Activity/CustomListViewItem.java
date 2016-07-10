package selfauth.s4.com.self_auth.Activity;


import bigjava.math.BigInteger;

/**
 * Created by user on 2016-07-09.
 */
public class CustomListViewItem {
    private String addr;
    private int imgRes;
    private String text;
    private boolean isAlready;
    private boolean isSelected;
    //-------
    private String keyValue;
    private BigInteger primeNumber;

    //----- test
    public String testName;


    public CustomListViewItem(int imgRes, String text, String addr, boolean isAlready, BigInteger primeNumber){
        this.imgRes=imgRes;
        this.text=text;
        this.isAlready = isAlready;
        this.addr=addr;
        isSelected=false;
        this.primeNumber = primeNumber;
        this.keyValue = "";
    }

    public int getImgRes(){
        return imgRes;
    }

    public String getText(){
        return text;
    }

    public boolean isAlready() {
        return isAlready;
    }

    public void setKeyValue(String key){
        keyValue = key;
    }
    public String getKeyValue(){
        return keyValue;
    }

    public void setPrimeNumber(BigInteger prime){
        primeNumber = prime;
    }

    public BigInteger getPrimeNumber(){
        return primeNumber;
    }

    public boolean isSelected(){
        return isSelected;
    }
    public void setSelected(boolean Selected){
        isSelected = Selected;
    }

    public void setAddr(String add) {
        addr=add;
    }

    public String getAddr(){
        return addr;
    }
}
