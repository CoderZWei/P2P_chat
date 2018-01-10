package com.example.zw.p2p_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zw on 2016/10/25.
 */
public class Msg_adapter  extends ArrayAdapter<Msg>{
    private int resourceId;
    private List<Msg>Msgs;
    public Msg_adapter(Context context, int resource, List<Msg>objects) {
        super(context, resource,objects);
        this.resourceId = resource;
        this.Msgs=objects;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Msg msg=getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder=new ViewHolder();
            viewHolder.leftLayout=(LinearLayout)view.findViewById(R.id.left_layout);
            viewHolder.rightLayout=(LinearLayout)view.findViewById(R.id.right_layout);
            viewHolder.leftMsg=(TextView) view.findViewById(R.id.left_msg);
            viewHolder.rightMsg=(TextView)view.findViewById(R.id.right_msg);
            viewHolder.head1=(ImageView)view.findViewById(R.id.head_left);
            viewHolder.head2=(ImageView)view.findViewById(R.id.head_right);
            view.setTag(viewHolder);
        }else{
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
    if(msg.getType()==Msg.TYPE_RECEIVED){
        viewHolder.leftLayout.setVisibility(View.VISIBLE);
        viewHolder.rightLayout.setVisibility(View.GONE);
        viewHolder.head1.setVisibility(View.VISIBLE);
        viewHolder.head2.setVisibility(View.GONE);
        viewHolder.leftMsg.setText(msg.getContent());

    }else if(msg.getType()==Msg.TYPE_SENT){
        viewHolder.leftLayout.setVisibility(View.GONE);
        viewHolder.rightLayout.setVisibility(View.VISIBLE);
        viewHolder.head1.setVisibility(View.GONE);
        viewHolder.head2.setVisibility(View.VISIBLE);
        viewHolder.rightMsg.setText(msg.getContent());
    }
        return view;
    }
    class ViewHolder{
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        ImageView head1;
        ImageView head2;
    }
}
