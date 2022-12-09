package com.inclusive.finance.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;

import com.hwangjr.rxbus.RxBus;
import com.inclusive.finance.R;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

	//定义所需传入的参数
    private List<String> users;
    private Context context;
    private OnItemClickListener listener;

	//初始化Adapter时传入相关参数
    public CustomAdapter(List<String> users,Context context) {
        this.users = users;
        this.context = context;
     }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		//使每一个RecycleView中的视图都显示为R.layout.item的样式
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.editcaiyong_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.textView.setText(users.get(position));
        holder.textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                users.set(position,holder.textView.getText().toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.get().post("delyin", ""+position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private AppCompatEditText textView;
        private ImageView ivRemove;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
			//定位要设置的控件
            textView = itemView.findViewById(R.id.tv_yinyongbiaozhu1);
            ivRemove = itemView.findViewById(R.id.sb_yinyong_remove1);

        }
    }

    public interface OnItemClickListener{
		//在初始化时获得事件
        void onClick(int pos,String user);
        void onLongClick(int pos,String user);
    }
}
