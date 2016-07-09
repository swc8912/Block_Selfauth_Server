package selfauth.s4.com.self_auth.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import Database.MyDatabaseOpenHelper;
import selfauth.s4.com.self_auth.R;

public class MainActivity extends AppCompatActivity {


    private final String TAG = "log_MainActivity";

    private MyDatabaseOpenHelper helper;
    private SQLiteDatabase database;
    private int version = 1;


    //-------------view
    private Button btn_regist_iot;
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

        //helper.removeAllTuple(database);
        //helper.selectAll(database);


    }


    public void registIoT(String name, String types){

    }

    public void setViews(){
        btn_regist_iot = (Button)findViewById(R.id.act_main_btn1);
    }

    public void setListener() {
        btn_regist_iot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Act_regist.class);
                startActivity(intent);

            }
        });

    }
}
