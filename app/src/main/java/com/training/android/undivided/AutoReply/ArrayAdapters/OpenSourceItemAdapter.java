//package com.training.android.undivided.AutoReply.ArrayAdapters;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import com.training.android.undivided.R;
//
///**
// * Created by Hillary Briones on 8/8/2017.
// */
//
//public class OpenSourceItemAdapter extends BaseAdapter {
//
//    private static final String[][] COMPONENTS = new String[][]{
//
//            {"BottomBar", "https://github.com/roughike/BottomBar"},
//            {"GSON",
//                    "https://github.com/google/gson"}
//    };
//
//    private LayoutInflater mInflater;
//
//    public OpenSourceItemAdapter(Context context) {
//        mInflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public int getCount() {
//        return COMPONENTS.length;
//    }
//
//    @Override
//    public Object getItem(int position) {
//
//        return COMPONENTS[position];
//    }
//
//    @Override
//    public long getItemId(int position) {
//
//        return position;
//    }
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            convertView = mInflater.inflate(R.layout.open_source_items, parent, false);
//        }
//
//        TextView title =  convertView.findViewById(R.id.title);
//        TextView url =  convertView.findViewById(R.id.url);
//
//        title.setText(COMPONENTS[position][0]);
//        url.setText(COMPONENTS[position][1]);
//
//        return convertView;
//    }
//
//}
