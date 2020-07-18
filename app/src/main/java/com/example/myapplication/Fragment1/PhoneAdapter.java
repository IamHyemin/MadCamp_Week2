package com.example.myapplication.Fragment1;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.Retrofit.User;

import java.util.ArrayList;

public class PhoneAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<User> list;
    private LayoutInflater inflate;
    private UserViewHolder viewHolder;

    public PhoneAdapter(ArrayList<User> list, Context context) {
        this.list = list;
        this.mContext = context;
        this.inflate = LayoutInflater.from(context);
    }

    public PhoneAdapter() {
    }

    public PhoneAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public User getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.phonebook_listview, null);

            viewHolder = new UserViewHolder();
            viewHolder.txtstate = convertView.findViewById(R.id.friendState);
            viewHolder.txtname = convertView.findViewById(R.id.friendName);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (UserViewHolder) convertView.getTag();
        }

        // 리스트에 있는 데이터를 리스트뷰 셀에 뿌린다.
        //viewHolder.txticon.setImageResource(list.get(position).getIcon());
        viewHolder.txtname.setText(list.get(position).getName());
        viewHolder.txtstate.setText(list.get(position).getState());

        return convertView;
    }

    class UserViewHolder {
        TextView txtname;
        TextView txtstate;
    }

}