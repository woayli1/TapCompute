package com.gc.tapCompute.frgament;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gc.tapCompute.R;
import com.gc.tapCompute.adapter.OnItemClickListener;
import com.gc.tapCompute.adapter.RefreshItemAdapter;
import com.gc.tapCompute.bean.DataBean;
import com.gc.tapCompute.data.DataHelper;
import com.gc.tapCompute.view.AddPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Description: Description
 * Author: gc
 * CreateDate: 2024/7/12 10:37
 * Version: 1.0
 */
public class ChildFragment extends Fragment {

    protected Context context;

    /**
     * 判断界面状态
     * tap_man 男主
     * tap_woman 女主
     */
    public String status = "tap_man";
    public static String statusTag = "tapName";

    private String dataBaseName = "TapCompute";
    private static String dataBaseNameTag = "Tap_Compute";

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RefreshItemAdapter refreshItemAdapter;
    private Button btAdd;

    private DataHelper dataHelper;
    public ArrayList<DataBean> dataBeanList;

    private AddPopup addPopup;

    @NonNull
    public static ChildFragment create(String databaseName, String tapName) {
        final Bundle args = new Bundle();
        args.putString(dataBaseNameTag, databaseName);
        args.putString(statusTag, tapName);
        final ChildFragment fragment = new ChildFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        final View rootView;
        if (setLayout() instanceof Integer) {
            rootView = inflater.inflate((int) setLayout(), container, false);
        } else if (setLayout() instanceof View) {
            rootView = (View) setLayout();
        } else {
            throw new NullPointerException("viewBinding is null");
        }
        if (rootView != null) {
            context = getContext();
            onBindView(savedInstanceState, rootView);
        }
        return rootView;
    }

    private Object setLayout() {
        return R.layout.fragment_child;
    }

    private void onBindView(Bundle savedInstanceState, View rootView) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            status = bundle.getString(statusTag);
            dataBaseName = bundle.getString(dataBaseNameTag);
        }

        swipeRefreshLayout = rootView.findViewById(R.id.id_swipe_Layout);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        btAdd = rootView.findViewById(R.id.bt_add);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                setData();
            }
        });

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDialog();
            }
        });

        addPopup = new AddPopup(context, status);
        addPopup.setOnConfirmClick(new AddPopup.PopupBackData() {
            @Override
            public void onConfirmClick(String tapName, String tapAttack, String tapCost) {
                dataHelper.insertInto(status, tapName, tapAttack, tapCost);
                ToastUtils.showShort("添加成功");
                setData();
            }
        });

        dataHelper = new DataHelper(context, dataBaseName, null, 1);
        setData();
    }

    public void setData() {
        dataBeanList = new ArrayList<>();
        ArrayList<String> itemsName = dataHelper.getSelectStringItem(status, "tap_name");
        ArrayList<String> itemsAttack = dataHelper.getSelectStringItem(status, "tap_attack");
        ArrayList<String> itemsCost = dataHelper.getSelectStringItem(status, "tap_cost");

        for (int i = 0; i < itemsName.size(); i++) {
            DataBean dataBean = new DataBean(itemsName.get(i), itemsAttack.get(i), itemsCost.get(i));
            dataBeanList.add(dataBean);
        }

        refreshItemAdapter = new RefreshItemAdapter(context, dataBeanList);
        refreshItemAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, long itemId) {
                String strName = refreshItemAdapter.getDate().get(position).getName();
                String strAttack = refreshItemAdapter.getDate().get(position).getAttack();
                deleteDialog(position, strName, strAttack);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(refreshItemAdapter);

        if (ObjectUtils.isNotEmpty(btAdd)) {
            String addText;

            if (status.equals("tap_man")) {
                addText = "男主添加";
            } else {
                addText = "女主添加";
            }

            if (dataBeanList.size() > 0) {
                addText = addText + "(" + dataBeanList.size() + "个)";
            }
            btAdd.setText(addText);
        }

        if (refreshItemAdapter.getItemCount() == 0) {
            ToastUtils.showShort("无数据，请添加");
        }

    }

    public void addDialog() {
        new XPopup.Builder(getContext())
                .asCustom(addPopup)
                .show();
    }

    public void deleteDialog(int position, String strName, String strAttack) {
        new XPopup.Builder(getContext()).asConfirm(strName, "确认删除吗？",
                new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        dataHelper.delete(status, strName, strAttack);
                        ToastUtils.showShort("删除成功");
                        dataBeanList.remove(position);
                        refreshItemAdapter.notifyDataSetChanged();
                    }
                })
                .show();
    }


}
