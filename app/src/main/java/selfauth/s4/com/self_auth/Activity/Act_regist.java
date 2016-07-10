package selfauth.s4.com.self_auth.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import Bluetooth.BluetoothChatService;
import Bluetooth.BluetoothConnect;
import Bluetooth.Data.Keyval;
import Bluetooth.Data.Packet;
import Database.MyDatabaseOpenHelper;
import Keygenerator.primeGenerator;
import Model.registForm;
import bigjava.math.BigInteger;
import selfauth.s4.com.self_auth.R;

public class Act_regist extends AppCompatActivity {
    private final String TAG = "log_actRegist";
    private ArrayList<String> device_set;
    private ListView listview;
    private CustomListViewAdapter adapter;

    private Button btn_scan;
    private boolean isScaning = false;
    private Button btn_regist;

    private final static int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBlueToothAdapter;
    public static BluetoothConnect bluetoothConnect;

    private ArrayList<CustomListViewItem> selectedItems;
    private ArrayList<String> alreadyAddr;
    //---------------- db
    private MyDatabaseOpenHelper helper;
    private SQLiteDatabase database;

    public static ArrayList<registForm> deviceInfo = new ArrayList<registForm>();

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                Log.i("TEST", "find = " + device.getName());
                if(device_set.contains(device.getName()) == false && !TextUtils.isEmpty(device.getName())) {
                    Log.i("TEST", "new = " + device.getName());
                    device_set.add(device.getName());
                    //adapter.add(new CustomListViewItem(0, device.getName(), true )); 수정해야함. 이미 연결했었나 여부 판단

                    BigInteger bigInteger = primeGenerator.getPrimeNumber(256);
                    Log.d(TAG,"prime: " + bigInteger);
                    if(alreadyAddr.contains(device.getAddress()))
                        adapter.add(new CustomListViewItem(0, device.getName(), device.getAddress(), true, bigInteger));
                    else
                        adapter.add(new CustomListViewItem(0, device.getName(), device.getAddress(), false, bigInteger));

                    adapter.notifyDataSetChanged();
                }


                if(device.getBondState() != BluetoothDevice.BOND_BONDED){
                    Log.i("TEST", "이미 연결된 " + device.getName());
                }

                //mBlueToothAdapter.cancelDiscovery();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_regist);

        bluetoothConnect = new BluetoothConnect(getApplicationContext(), mHandler);
        bluetoothConnect.serviceStart();

        //------ view setting
        setViews();
        setListener();

        device_set = new ArrayList<String>();
        selectedItems = new ArrayList<CustomListViewItem>();

        //-------- database
        helper = new MyDatabaseOpenHelper(Act_regist.this, MyDatabaseOpenHelper.tableName_keys, null, 1);
        database = helper.getWritableDatabase();
        alreadyAddr=helper.getAlreadyList(database);

        //-------- test
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        listview.setAdapter(adapter);
    }

    public void setViews(){
        btn_regist=(Button)findViewById(R.id.act_regist_btn2);
        btn_scan=(Button)findViewById(R.id.act_regist_btn1);
        adapter = new CustomListViewAdapter(this, bluetoothConnect);
        listview=(ListView)findViewById(R.id.act_regist_listview);

    }
    public void setListener(){
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isScaning) {
                    Intent bluetoothsearchintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(bluetoothsearchintent, REQUEST_ENABLE_BT);

                    mBlueToothAdapter = BluetoothAdapter.getDefaultAdapter();
                    mBlueToothAdapter.startDiscovery();

                    btn_scan.setText("검색중지");
                    isScaning=true;
                }
                else{
                    mBlueToothAdapter.cancelDiscovery();
                    btn_scan.setText("장비검색하기");
                    isScaning=false;
                }
            }
        });

        //------------------- 수정해야함
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result="";
                for(CustomListViewItem item : adapter.getSelectedItems()){
                    result+=item.getText()+" / ";
                    /*
                        0. 포문 돌면서
                        1. 블루투스로 각각 프라임넘버랑 key 보냄
                        2.
                    */
                }
                Log.i(TAG, result);

                deviceInfo.clear();
                selectedItems = adapter.getSelectedItems();
                String key = "asdf";
                for(CustomListViewItem item : adapter.getSelectedItems()){
                    item.setKeyValue(key);
                    helper.insertIntoSelected(database, item.getAddr());
                    if(item.isSelected()) {
                        deviceInfo.add(new registForm(item.getAddr(), item.getKeyValue(), "" + item.getPrimeNumber()));
                        Log.d(TAG, "addr: " + item.getAddr() + " " + item.getPrimeNumber());
                    }
                }

                helper.selectAll(database, MyDatabaseOpenHelper.tableName_selected);

                // 페이링 및 연결
                boolean isSecure = true;
                for(int i=0; i<deviceInfo.size(); i++) {
                    Log.d(TAG, "item.getAddr(): " + deviceInfo.get(i).getIotAddr());
                    if(!deviceInfo.get(i).isConnected())
                        bluetoothConnect.connectDevice(deviceInfo.get(i).getIotAddr(), isSecure);
                }

                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
        helper.close();

        if(isScaning){
            mBlueToothAdapter.cancelDiscovery();
        }

        unregisterReceiver(mReceiver);

        //bluetoothConnect.serviceStop();
    }

    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothConnect.MESSAGE_STATE_CHANGE:
                    Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            //setStatus(getString(R.string.title_connected_to) + " " + mConnectedDeviceName);
                            //mConversationArrayAdapter.clear();
                            Log.d(TAG, "connected!!!!!");
                            //String sample="{\"auth_info\":[{\"date\":\"2016-07-09 20:59\",\"primeNum\":\"9\",\"key\":\"Key1\"}], \"cmd\":6}";
                            Packet pa = new Packet();
                            pa.setCmd(BluetoothConnect.MESSAGE_DATA_SAVE);
                            String addr = "";
                            for(int i=0; i<deviceInfo.size(); i++) {
                                if(msg.obj != null && deviceInfo.get(i).getIotAddr().equals((String) msg.obj)){
                                    Log.d(TAG,"addr: " + deviceInfo.get(i).getIotAddr() + " " + (String) msg.obj);
                                    Log.d(TAG, "item.getKeyValue(): " + deviceInfo.get(i).getKeyValue());
                                    Log.d(TAG, "item.getPrimeNumber().toString(): " + deviceInfo.get(i).getPrimeValue());
                                    pa.getAuthinfo().add(new Keyval(deviceInfo.get(i).getKeyValue(), deviceInfo.get(i).getPrimeValue()));
                                    deviceInfo.get(i).setIsConnected(true);
                                    addr = deviceInfo.get(i).getIotAddr();
                                    break;
                                }
                            }
                            //bluetoothConnect.sendMsg(sample);
                            Gson gson = new Gson();
                            bluetoothConnect.sendMsg(gson.toJson(pa, Packet.class), addr);
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            //setStatus(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            //setStatus(R.string.title_not_connected);
                            for(int i=0; i<deviceInfo.size(); i++) {
                                deviceInfo.get(i).setIsConnected(false);
                            }
                            break;
                    }
                    break;
                case BluetoothConnect.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    //mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case BluetoothConnect.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.d(TAG,"read: " + readMessage);
                    // json 파싱
                    /*Gson gson = JsonParser.getInstance().getJspGson();
                    Packet p = gson.fromJson(readMessage, Packet.class);
                    SharedPreferences pref = getSharedPreferences("selfauth", MODE_PRIVATE);
                    if(p.getCmd() == BluetoothConnect.MESSAGE_DATA_LOAD){
                        Packet pa = new Packet();
                        pa.setCmd(BluetoothConnect.MESSAGE_DATA_LOAD_ACK);
                        for(int i=0; i<p.getAuthinfo().size(); i++){
                            String val = pref.getString(p.getAuthinfo().get(i).getKey(), "");
                            pa.getAuthinfo().add(new Keyval(p.getAuthinfo().get(i).getKey(), val));
                        }
                        bluetoothConnect.sendMsg(gson.toJson(pa));
                    }
                    else if(p.getCmd() == BluetoothConnect.MESSAGE_DATA_SAVE){
                        SharedPreferences.Editor editor = pref.edit();
                        for(int i=0; i<p.getAuthinfo().size(); i++){
                            editor.putString(p.getAuthinfo().get(i).getKey(), p.getAuthinfo().get(i).getPrimeNum());
                        }
                        editor.commit();
                        Packet pa = new Packet();
                        pa.setCmd(BluetoothConnect.MESSAGE_DATA_LOAD_ACK);
                        bluetoothConnect.sendMsg(gson.toJson(pa));
                    }
                    else if(p.getCmd() == BluetoothConnect.MESSAGE_DATA_SAVE_ACK){
                        Log.d("1", "msg data save ack");
                    }
                    else if(p.getCmd() == BluetoothConnect.MESSAGE_DATA_LOAD_ACK){
                        Log.d("1", "msg data load ack");
                    }*/
                    //mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case BluetoothConnect.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    //mConnectedDeviceName = msg.getData().getString(BluetoothConnect.DEVICE_NAME);
                    //Toast.makeText(getApplicationContext(), "Connected to "
                    //        + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothConnect.MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(BluetoothConnect.TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

}

