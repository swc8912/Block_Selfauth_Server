package selfauth.s4.com.self_auth.Activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Bluetooth.BluetoothConnect;
import Keygenerator.primeGenerator;
import selfauth.s4.com.self_auth.R;

/**
 * Created by user on 2016-07-09.
 */
public class CustomListViewAdapter extends BaseAdapter {
    private ArrayList<ImageView> img_list;
    private ArrayList<CustomListViewItem> item_list;
    private Activity act;
    private BluetoothConnect bluetoothConnect;

    public CustomListViewAdapter(Activity act, BluetoothConnect bluetoothConnect) {
        item_list = new ArrayList<CustomListViewItem>();
        img_list = new ArrayList<ImageView>();

        this.act = act;
        this.bluetoothConnect = bluetoothConnect;
    }

    @Override
    public int getCount() {
        return item_list.size();
    }

    @Override
    public CustomListViewItem getItem(int position) {
        return item_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_listview_regist, parent, false);

            // TextView에 현재 position의 문자열 추가
            TextView text = (TextView) convertView.findViewById(R.id.custom_listview_regist_text);
            img_list.add((ImageView) convertView.findViewById(R.id.custom_listview_regist_img_state));
            ImageView img_ready = (ImageView) convertView.findViewById(R.id.custom_listview_regist_img_state);

            if(item_list.get(pos).isAlready()==true)
                img_ready.setImageResource(R.drawable.already);

            text.setText(item_list.get(position).getText());

            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (item_list.get(pos).isAlready() == false && item_list.get(pos).isSelected() == false ) {
                        // 터치 시 해당 아이템 이름 출력
                        LayoutInflater inflater = act.getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);

                        AlertDialog.Builder buider = new AlertDialog.Builder(act);
                        buider.setTitle("IoT equipment Information");
                        buider.setIcon(android.R.drawable.ic_menu_add);
                        buider.setView(dialogView);
                        buider.setPositiveButton("Complite", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText edit_name = (EditText) dialogView.findViewById(R.id.dialog_edit);
                                RadioGroup rg = (RadioGroup) dialogView.findViewById(R.id.dialog_rg);
                                String name = edit_name.getText().toString();

                                int checkedType = rg.getCheckedRadioButtonId();
                                RadioButton rb = (RadioButton) rg.findViewById(checkedType);
                                String type;
                                if (rb != null)
                                    type = rb.getText().toString();
                                else {
                                    type = "empty";
                                }

                                // 페이링 및 연결
                                boolean isSecure = true;
                                bluetoothConnect.connectDevice(item_list.get(pos).getAddr(), isSecure);

                                Toast.makeText(act, "새로운 IoT 장비를 추가했습니다", Toast.LENGTH_SHORT).show();
                                ImageView temp = img_list.get(pos);
                                temp.setImageResource(R.drawable.checked);

                                CustomListViewItem item = item_list.get(pos);

                                item.setSelected(true);
                                item.setPrimeNumber(primeGenerator.getPrimeNumber(256));

                            }
                        });
                        buider.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(act, "IoT 장비 추가를 취소합니다", Toast.LENGTH_SHORT).show();
                            }
                        });

                        AlertDialog dialog = buider.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                }
            });

            // 리스트 아이템을 길게 터치 했을 떄 이벤트 발생
            convertView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    // 터치 시 해당 아이템 이름 출력
                    Toast.makeText(context, "리스트 롱 클릭 : " + item_list.get(pos), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }
        return convertView;
    }

    public void add(CustomListViewItem item) {
        item_list.add(item);
    }

    public ArrayList<CustomListViewItem> getSelectedItems(){
        ArrayList<CustomListViewItem> temp=new ArrayList<CustomListViewItem>();
        for( CustomListViewItem item : item_list){
            if(item.isAlready() || item.isSelected() ){
                temp.add(item);
            }
        }

        return temp;
    }
}
