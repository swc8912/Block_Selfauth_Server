package selfauth.s4.com.self_auth.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import Database.MyDatabaseOpenHelper;
import selfauth.s4.com.self_auth.R;


public class Act_trade extends AppCompatActivity {

    private Button btn_trade;
    private TextView text_total;
    private TextView text_cur;
    private int cnt = 0;

    private com.github.lzyzsd.circleprogress.DonutProgress progress;
    private int val;
    private ListView listview;
    private CustomTradeListViewAdapter adapter;

    //---------------- db
    private MyDatabaseOpenHelper helper;
    private SQLiteDatabase database;


    private BluetoothAdapter mBlueToothAdapter;

    private ArrayList<String> deviceAddrList;
    private ArrayList<String> check;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);


                if (deviceAddrList.contains(device.getAddress()) && !check.contains(device.getAddress())) {
                    cnt++;

                    adapter.add(new CustomTradeListViewItem(device.getName()));

                    text_cur.setText("" + cnt);
                    adapter.notifyDataSetChanged();
                    check.add(device.getAddress());
                    val = (int) (((double) cnt / (double)deviceAddrList.size()) * 100);
                    Log.i("test","test val="+val);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progress.setProgress(val);
                                }
                            });
                        }
                    }).start();


                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_trade);


        setViews();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        //--------
        helper = new MyDatabaseOpenHelper(Act_trade.this, MyDatabaseOpenHelper.tableName_keys, null, 1);
        database = helper.getWritableDatabase();
        deviceAddrList = helper.getAlreadyList(database);
        check = new ArrayList<String>();


        if (deviceAddrList.size() != 0 && deviceAddrList != null) {
            Log.i("test", "test size=" + deviceAddrList.size());
            text_total.setText("" + deviceAddrList.size());
        }

        Intent bluetoothsearchintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(bluetoothsearchintent, 1);

        mBlueToothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBlueToothAdapter.startDiscovery();


    }

    public void setViews() {
        progress = (com.github.lzyzsd.circleprogress.DonutProgress) findViewById(R.id.act_trade_donut_progress);
        listview = (ListView) findViewById(R.id.act_trade_listview);
        adapter = new CustomTradeListViewAdapter();
        listview.setAdapter(adapter);


        text_cur = (TextView) findViewById(R.id.act_trade_text_cur);
        text_total = (TextView) findViewById(R.id.act_trade_text_total);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBlueToothAdapter.cancelDiscovery();
    }
}
