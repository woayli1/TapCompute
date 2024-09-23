package com.gc.tapCompute.frgament;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
import com.gc.tapCompute.view.AddPopup;
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
    public String status = "tap_man";
    public static String statusTag = "tapName";

    private String dataBaseName = "TapCompute";
    private static final String dataBaseNameTag = "Tap_Compute";

    private boolean isFirst = true;
    boolean isInt = false;  //用于判断输入的关卡是否为整数
    private int mStage; //接收输入的关卡

    private ImageView ivMore;
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

        TextView tvTitle = rootView.findViewById(R.id.tv_title);
        ivMore = rootView.findViewById(R.id.iv_more);
        swipeRefreshLayout = rootView.findViewById(R.id.id_swipe_Layout);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        btAdd = rootView.findViewById(R.id.bt_add);

        if (status.equals("tap_woman")) {
            tvTitle.setText(R.string.change_item_woman);
        } else {
            tvTitle.setText(R.string.change_item_man);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isFirst) {
            isFirst = false;

            PopupMenu popupMenu = new PopupMenu(context, ivMore);
            popupMenu.getMenuInflater().inflate(R.menu.change_item, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.item_stage) {
                    countStageDialog();
                } else if (menuItem.getItemId() == R.id.item_level) {
                    countLevelDialog();
                }
                return true;
            });

            DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
            defaultItemAnimator.setAddDuration(260);
            defaultItemAnimator.setRemoveDuration(260);
            recyclerView.setItemAnimator(defaultItemAnimator);

            swipeRefreshLayout.setOnRefreshListener(() -> {
                swipeRefreshLayout.setRefreshing(false);
                setData(true);
            });

            ivMore.setOnClickListener(view -> popupMenu.show());

            btAdd.setOnClickListener(view -> addDialog());

            addPopup = new AddPopup(context, status);
            addPopup.setOnConfirmClick((tapName, tapAttack, tapCost) -> {
                dataHelper.insertInto(status, tapName, tapAttack, tapCost);
                ToastUtils.showShort("添加成功");
                dataBeanList.add(new DataBean(tapName, tapAttack, tapCost));
                refreshItemAdapter.notifyItemInserted(dataBeanList.size() - 1);
            });

            dataHelper = new DataHelper(context, dataBaseName, null, 1);

            setData(true);
        } else {
            setData(false);
        }

    }

    public void setData(boolean playAnim) {
        dataBeanList = new ArrayList<>();
        ArrayList<String> itemsName = dataHelper.getSelectStringItem(status, "tap_name");
        ArrayList<String> itemsAttack = dataHelper.getSelectStringItem(status, "tap_attack");
        ArrayList<String> itemsCost = dataHelper.getSelectStringItem(status, "tap_cost");

        for (int i = 0; i < itemsName.size(); i++) {
            DataBean dataBean = new DataBean(itemsName.get(i), itemsAttack.get(i), itemsCost.get(i));
            dataBeanList.add(dataBean);
        }

        refreshItemAdapter = new RefreshItemAdapter(context, dataBeanList);
        refreshItemAdapter.setOnItemClickListener((position, itemId) -> {
            String strName = refreshItemAdapter.getDate().get(position).getName();
            String strAttack = refreshItemAdapter.getDate().get(position).getAttack();
            deleteDialog(position, strName, strAttack);
        });
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

        setBtAddText();

        if (refreshItemAdapter.getItemCount() == 0) {
            ToastUtils.showShort("无数据，请添加");
        }

    }

    public void addDialog() {
        new XPopup.Builder(getContext()).asCustom(addPopup).show();
    }

    public void deleteDialog(int position, String strName, String strAttack) {
        new XPopup.Builder(getContext()).asConfirm(strName, "确认删除吗？", () -> {
            dataHelper.delete(status, strName, strAttack);
            ToastUtils.showShort("删除成功");
            dataBeanList.remove(position);
            refreshItemAdapter.notifyItemRemoved(position);
            setBtAddText();
        }).show();
    }

    //计算关卡
    private void countStageDialog() {
        new XPopup.Builder(getContext())
                .asInputConfirm("计算关卡", "请输入当前关卡", null, null, text -> {
                    try {
                        mStage = Integer.parseInt(text);
                        isInt = true;
                    } catch (Exception e) {
                        ToastUtils.showShort("请输入整数");
                        isInt = false;
                    }
                    if (mStage < 90) {
                        ToastUtils.showShort("请完成90关卡后再进行计算");
                    } else if (isInt) {
                        int c = (mStage - 90) / 15 + 1;
                        int d = 15 - (mStage - 90) % 15;
                        msgDialog(c, d, "关卡");
                    }
                }, null, R.layout.popup_center_impl_confirm).show();
    }

    //计算等级
    private void countLevelDialog() {
        new XPopup.Builder(getContext())
                .asInputConfirm("计算等级", "请输入当前等级", null, null, text -> {
                    int mLevel;
                    try {
                        mLevel = Integer.parseInt(text);
                        isInt = true;
                    } catch (Exception e) {
                        ToastUtils.showShort("请输入整数");
                        isInt = false;
                        return;
                    }

                    if (mLevel < 500) {
                        ToastUtils.showShort("请达到500级后再进行计算");
                    } else {
                        int c = (mLevel - 500) / 1000 + 1;
                        int d = 1000 - (mLevel - 500) % 1000;
                        msgDialog(c, d, "级");
                    }
                }, null, R.layout.popup_center_impl_confirm).show();
    }

    public void msgDialog(final int i, final int j, final String s) {
        String title = "蜕变量";
        String msg = "当前的蜕变级别为: " + i + " ,还需" + j + s + "满足下一要求";
        new XPopup.Builder(getContext()).asConfirm(title, msg, "", "确认", null, null, true).show();
    }

    private void setBtAddText() {
        if (ObjectUtils.isNotEmpty(btAdd)) {
            String addText;

            if (status.equals("tap_man")) {
                addText = "男主添加";
            } else {
                addText = "女主添加";
            }

            if (!dataBeanList.isEmpty()) {
                addText = addText + "(" + dataBeanList.size() + "个)";
            }
            btAdd.setText(addText);
        }
    }

}
