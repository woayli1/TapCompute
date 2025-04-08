package com.gc.tapCompute.frgament;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gc.tapCompute.R;
import com.gc.tapCompute.adapter.RefreshItemAdapter;
import com.gc.tapCompute.bean.DataBean;
import com.gc.tapCompute.data.DataHelper;
import com.lxj.xpopup.XPopup;

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
    public String status = DataHelper.TABLE_NAME_MAN;
    public static String statusTag = "tapName";

    private String dataBaseName = DataHelper.DATA_BASE_NAME;
    private static final String dataBaseNameTag = "Tap_Compute";

    private boolean isFirst = true;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RefreshItemAdapter refreshItemAdapter;
    private TextView tvCount;

    private DataHelper dataHelper;
    public ArrayList<DataBean> dataBeanList;

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
            onBindView(rootView);
        }
        return rootView;
    }

    private Object setLayout() {
        return R.layout.fragment_child;
    }

    private void onBindView(View rootView) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            status = bundle.getString(statusTag);
            dataBaseName = bundle.getString(dataBaseNameTag);
        }

        swipeRefreshLayout = rootView.findViewById(R.id.id_swipe_Layout);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        tvCount = rootView.findViewById(R.id.tv_count);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isFirst) {
            isFirst = false;

            DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
            defaultItemAnimator.setAddDuration(260);
            defaultItemAnimator.setRemoveDuration(260);
            recyclerView.setItemAnimator(defaultItemAnimator);

            swipeRefreshLayout.setOnRefreshListener(() -> {
                swipeRefreshLayout.setRefreshing(false);
                setData(true);
            });

            dataHelper = new DataHelper(context, dataBaseName, null, 1);

            setData(true);
        } else {
            setData(false);
        }

    }

    public void onConfirmClick(String tapName, String tapAttack, String tapCost) {
        boolean exist = dataHelper.isExist(status, tapName);
        if (exist) {
            ToastUtils.showShort(tapName + " 已存在");
            return;
        }
        dataHelper.insertInto(status, tapName, tapAttack, tapCost);
        ToastUtils.showShort("添加成功");
        dataBeanList.add(new DataBean(tapName, tapAttack, tapCost));
        refreshItemAdapter.notifyItemInserted(dataBeanList.size() - 1);
        setCountText();
    }

    public void setData(boolean playAnim) {
        dataBeanList = new ArrayList<>();
        ArrayList<String> itemsName = dataHelper.getSelectStringItem(status, DataHelper.TAP_NAME);
        ArrayList<String> itemsAttack = dataHelper.getSelectStringItem(status, DataHelper.TAP_ATTACK);
        ArrayList<String> itemsCost = dataHelper.getSelectStringItem(status, DataHelper.TAP_COST);

        for (int i = 0; i < itemsName.size(); i++) {
            DataBean dataBean = new DataBean(itemsName.get(i), itemsAttack.get(i), itemsCost.get(i));
            dataBeanList.add(dataBean);
        }

        refreshItemAdapter = new RefreshItemAdapter(context, dataBeanList);
        refreshItemAdapter.setOnItemClickListener((position, itemId) -> deleteDialog(position, refreshItemAdapter
                .getDate().get(position).getName()));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(refreshItemAdapter);

        if (playAnim) {
            //播放动画
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
            LayoutAnimationController layoutAnimationController = new LayoutAnimationController(animation);
            layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
            layoutAnimationController.setDelay(0.5f);

            recyclerView.setLayoutAnimation(layoutAnimationController);
        }

        setCountText();

        if (refreshItemAdapter.getItemCount() == 0) {
            ToastUtils.showShort("无数据，请添加");
        }
    }

    public void deleteDialog(int position, String strName) {
        new XPopup.Builder(getContext()).asConfirm(strName, "确认删除吗？", () -> {
            dataHelper.delete(status, strName);
            ToastUtils.showShort("删除成功");

            if (dataBeanList.size() > position) {
                dataBeanList.remove(position);
            }
            refreshItemAdapter.notifyItemRemoved(position);
            setCountText();
        }).show();
    }

    private void setCountText() {
        if (ObjectUtils.isNotEmpty(tvCount)) {
            String addText = "";
            if (!dataBeanList.isEmpty()) {
                addText = "共" + dataBeanList.size() + "个";
            }
            tvCount.setText(addText);
        }
    }

}
