package selfauth.s4.com.self_auth.Activity;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import selfauth.s4.com.self_auth.R;

/**
 * Created by user on 2016-07-09.
 */
public class CustomTradeListViewAdapter extends BaseAdapter {

    private ArrayList<CustomTradeListViewItem> item_list;



    public CustomTradeListViewAdapter() {
        item_list = new ArrayList<CustomTradeListViewItem>();
    }

    @Override
    public int getCount() {
        return item_list.size();
    }

    @Override
    public CustomTradeListViewItem getItem(int position) {
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
            convertView = inflater.inflate(R.layout.custom_listview_trade, parent, false);

            // TextView에 현재 position의 문자열 추가
            TextView text = (TextView) convertView.findViewById(R.id.custom_listview_trade_text);
            ImageView img = (ImageView) convertView.findViewById(R.id.custom_listview_trade_img);
            Random rand = new Random();
            int num = rand.nextInt(3)+1;
            if(num==1)
                img.setImageResource(R.drawable.trade01);
            else if(num==2)
                img.setImageResource(R.drawable.trade02);
            else
                img.setImageResource(R.drawable.trade03);

            Log.i("test","test title33 "+item_list.get(pos).getText()+"pos="+pos);
            text.setText(item_list.get(pos).getText());
        }
        return convertView;
    }

    public void add(CustomTradeListViewItem item) {
        item_list.add(item);
        Log.i("test","test title33 add = "+item.getText());
    }
}
