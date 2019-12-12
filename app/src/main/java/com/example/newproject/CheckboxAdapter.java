package com.example.newproject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;

public class CheckboxAdapter extends SimpleAdapter {
    LayoutInflater inflater;
    Activity context;
    ArrayList<HashMap<String, String>> arrayList;
    ArrayList checked;

    public CheckboxAdapter(Activity context, ArrayList<HashMap<String, String>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        checked = new ArrayList();
        this.context = context;
        this.arrayList = data;
        inflater.from(context);
    }

    private class ViewHolder {
        TextView sd;
        TextView type;
        CheckBox imageView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.perokok_list, null, true);
        final CheckBox imageView = (CheckBox) view.findViewById(R.id.checkBox);
        TextView sd = (TextView)view.findViewById(R.id.description);
        TextView type = (TextView)view.findViewById(R.id.question);
        ListView lv = (ListView)view.findViewById(R.id.list);
        sd.setText(arrayList.get(position).get("description"));
        type.setText(arrayList.get(position).get("question"));
        imageView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(imageView.isChecked()){
                    checked.add(arrayList.get(position).get("id2"));

                }
                else
                    checked.remove(arrayList.get(position).get("id2"));
            }
        });
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(imageView.isChecked()){
//                    checked.add(arrayList.get(position).get("id2"));
//                }
//                else
//                    checked.remove(arrayList.get(position).get("id2"));
//            }
//        });
        return view;
    }

    public ArrayList getCheckedQuestion(){
        return checked;
    }
}