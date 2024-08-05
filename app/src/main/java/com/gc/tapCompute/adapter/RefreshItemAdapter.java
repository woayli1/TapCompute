package com.gc.tapCompute.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ObjectUtils;
import com.gc.tapCompute.R;
import com.gc.tapCompute.bean.DataBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gc on 2017/6/3.
 */
public class RefreshItemAdapter extends RecyclerView.Adapter<RefreshItemAdapter.RefreshItemHolder> {

    private final Context context;
    private final ArrayList<DataBean> dataBeanList;
    public OnItemClickListener onItemClickListener;

    public RefreshItemAdapter(Context context, ArrayList<DataBean> dataBeanList) {
        this.context = context;
        this.dataBeanList = dataBeanList;
    }

    @NonNull
    @NotNull
    @Override
    public RefreshItemHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.content_view, parent, false);
        return new RefreshItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RefreshItemHolder holder, int position) {
        holder.llItem.setOnClickListener(view -> {
            if (ObjectUtils.isNotEmpty(onItemClickListener)) {
                onItemClickListener.onItemClick(holder.getAbsoluteAdapterPosition(), 0);
            }
        });

        String sNAme = dataBeanList.get(position).getName();
        String sAttack = dataBeanList.get(position).getAttack();
        String sCost = dataBeanList.get(position).getCost();
        holder.tvName.setText(sNAme);
        holder.tvAttack.setText(sAttack);
        holder.tvCost.setText(sCost);

        float a = Float.parseFloat(sAttack);
        float b = Float.parseFloat(sCost);
        String c = Float.toString(a / b);
        holder.tvValue.setText(c);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<DataBean> getDate() {
        return dataBeanList;
    }

    @Override
    public int getItemCount() {
        return dataBeanList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static class RefreshItemHolder extends RecyclerView.ViewHolder {
        LinearLayout llItem;
        TextView tvName, tvAttack, tvCost, tvValue;

        public RefreshItemHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            llItem = itemView.findViewById(R.id.ll_item);
            tvName = itemView.findViewById(R.id.tv_name); //用于接收项目名称
            tvAttack = itemView.findViewById(R.id.tv_attack); //用于接收攻击力
            tvCost = itemView.findViewById(R.id.tv_cost); //用于接收项目花费
            tvValue = itemView.findViewById(R.id.tv_value); //用于接收项目性价比
        }
    }
}
