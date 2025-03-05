package com.gc.tapCompute;

import android.app.UiModeManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gc.tapCompute.frgament.ChildFragment;
import com.gc.tapCompute.view.AddPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.AttachPopupView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends FragmentActivity {

    private static final String dataBaseName = "TapCompute";

    private TextView tvTitle;
    private ImageView ivMore;

    private AttachPopupView attachPopupView;
    private ChildFragment tapMan, tapWoman;

    //当前页面位置
    private int currentPosition = 0;
    private ViewPager2 viewPager;

    boolean isInt = false;  //用于判断输入的关卡是否为整数
    private int mStage; //接收输入的关卡

    private boolean isNight = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTitle = findViewById(R.id.tv_title);
        ivMore = findViewById(R.id.iv_more);

        viewPager = findViewById(R.id.view_pager);
        ScreenSlidePagerAdapter slidePagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(slidePagerAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                float appWidth = (float) (ScreenUtils.getAppScreenWidth() - 60) / 2;
                float alpha = Math.abs((appWidth - positionOffsetPixels) / appWidth);

                tvTitle.setAlpha(alpha);

                if (positionOffsetPixels >= appWidth) {
                    tvTitle.setText(R.string.change_item_woman);
                } else if (alpha != 1) {
                    tvTitle.setText(R.string.change_item_man);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
                if (position == 1) {
                    tvTitle.setText(R.string.change_item_woman);
                } else {
                    tvTitle.setText(R.string.change_item_man);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        UiModeManager uiModeManager = (UiModeManager) getApplicationContext().getSystemService(Context.UI_MODE_SERVICE);
        isNight = uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;

        BarUtils.setNavBarLightMode(getWindow(), !isNight);
        BarUtils.setStatusBarLightMode(getWindow(), !isNight);

        ivMore.setOnClickListener(view -> {
            if (ObjectUtils.isEmpty(attachPopupView)) {

                attachPopupView = new XPopup.Builder(this)
                        .hasStatusBarShadow(false)
//                        .isRequestFocus(false)
                        .isCoverSoftInput(true)
                        .hasShadowBg(false)
                        .isDarkTheme(isNight)
                        .offsetX(45)
                        .atView(ivMore)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
                        .asAttachList(new String[]{getResources().getString(R.string.change_item_calculate_levels),
                                        getResources().getString(R.string.change_item_calculate_level),
                                        getResources().getString(R.string.change_item_calculate_addItem),
                                },
                                new int[]{},
                                (position, text) -> {
                                    if (viewPager.getScrollState() == ViewPager2.SCROLL_STATE_DRAGGING) {
                                        ToastUtils.showShort("正在滑动，无法弹窗");
                                        return;
                                    }

                                    if (position == 0) {
                                        countStageDialog();
                                    } else if (position == 1) {
                                        countLevelDialog();
                                    } else if (position == 2) {
                                        addDialog();
                                    }
                                }, 0, 0/*, Gravity.LEFT*/);
            }

            attachPopupView.show();
        });
    }

    public void addDialog() {
        String status = "tap_man";
        if (currentPosition == 1) {
            status = "tap_woman";
        }
        AddPopup addPopup = new AddPopup(this, status);
        addPopup.setOnConfirmClick((tapName, tapAttack, tapCost) -> {
            if (currentPosition == 1) {
                tapWoman.onConfirmClick(tapName, tapAttack, tapCost);
            } else {
                tapMan.onConfirmClick(tapName, tapAttack, tapCost);
            }
        });

        new XPopup.Builder(getApplicationContext()).asCustom(addPopup).show();
    }

    //计算关卡
    private void countStageDialog() {
        new XPopup.Builder(this)
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
        new XPopup.Builder(this)
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
        new XPopup.Builder(this).isDarkTheme(isNight)
                .asConfirm(title, msg, "", "确认", null, null, true).show();
    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        private static final int NUM_PAGES = 2;

        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NotNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                tapMan = ChildFragment.create(dataBaseName, "tap_man");
                return tapMan;
            }
            tapWoman = ChildFragment.create(dataBaseName, "tap_woman");
            return tapWoman;
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

}
