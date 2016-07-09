package selfauth.s4.com.self_auth.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import Bluetooth.BluetoothChatService;
import Bluetooth.BluetoothConnect;
import Bluetooth.Data.Packet;
import Database.MyDatabaseOpenHelper;
import selfauth.s4.com.self_auth.R;

public class MainActivity extends AppCompatActivity {


    private final String TAG = "log_MainActivity";

    private MyDatabaseOpenHelper helper;
    private SQLiteDatabase database;
    private int version = 1;


    //-------------view
    private Button btn_regist_iot;
    private Button btn_regist_test_remove;
    private Button btn_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-------- view setting
        setViews();
        setListener();

        //-------- database
        helper = new MyDatabaseOpenHelper(MainActivity.this, MyDatabaseOpenHelper.tableName_keys, null, version);
        database = helper.getWritableDatabase();

        //-------- test
        //helper.insertIntokeys(database, primeGenerator.getPrimeNumber(256).toString());


        //helper.selectAll(database);


    }


    public void registIoT(String name, String types){

    }

    public void setViews(){
        btn_regist_iot = (Button)findViewById(R.id.act_main_btn1);
        btn_regist_test_remove = (Button) findViewById(R.id.act_main_btn2);
        btn_pay = (Button)findViewById(R.id.act_main_btn3);
    }

    public void setListener() {
        btn_regist_iot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Act_regist.class);
                startActivity(intent);

            }
        });

        btn_regist_test_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.removeAllTuple(database, MyDatabaseOpenHelper.tableName_selected);
                helper.removeAllTuple(database, MyDatabaseOpenHelper.tableName_keys);
                Toast.makeText(getApplicationContext(),"데이터베이스 초기화",Toast.LENGTH_SHORT).show();
            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sample="{\"auth_info\":[{\"date\":\"2016-07-09 23:29\",\"primeNum\":\"\",\"key\":\"Key1\"}], \"cmd\":7}";
                Packet pa = new Packet();
                pa.setCmd(BluetoothConnect.MESSAGE_DATA_LOAD);
                            /*for(int i=0; i<p.getAuthinfo().size(); i++){
                                String val = pref.getString(p.getAuthinfo().get(i).getKey(), "");
                                pa.getAuthinfo().add(new Keyval(p.getAuthinfo().get(i).getKey(), val));
                            }*/
                Act_regist.bluetoothConnect.sendMsg(sample);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Act_regist.bluetoothConnect.serviceStop();
    }
}
