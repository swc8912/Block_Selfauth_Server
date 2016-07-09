package selfauth.s4.com.self_auth.Activity;


import android.util.Log;

/**
 * Created by user on 2016-07-09.
 */
public class CustomTradeListViewItem {

    private String addr;
    private String text;


    public CustomTradeListViewItem(String text){
        Log.i("test","test title2222 = "+text);
        this.text=text;

    }



    public String getText(){
        return text;
    }



    public void setAddr(String add) {
        addr=add;
    }

    public String getAddr(){
        return addr;
    }
}
