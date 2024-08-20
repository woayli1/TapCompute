package com.gc.tapCompute;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.blankj.utilcode.util.ToastUtils;
import com.gc.tapCompute.frgament.ChildFragment;
import com.lxj.xpopup.XPopup;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends FragmentActivity {

    private static final String dataBaseName = "TapCompute";

    boolean isInt = false;  //用于判断输入的关卡是否为整数
    private int mStage; //接收输入的关卡

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 viewPager = findViewById(R.id.view_pager);
        ScreenSlidePagerAdapter slidePagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(slidePagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.change_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.item_stage) {
            countStageDialog();
        } else if (item.getItemId() == R.id.item_level) {
            countLevelDialog();
        }
        return true;
    }

    //计算关卡
    private void countStageDialog() {
        new XPopup.Builder(this).asInputConfirm("计算关卡", "请输入当前关卡", null, null, text -> {
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

    private void countLevelDialog() {
        new XPopup.Builder(this).asInputConfirm("计算等级", "请输入当前等级", null, null, text -> {
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
        new XPopup.Builder(this).asConfirm(title, msg, "", "确认", null, null, true).show();
    }

    private static class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        private static final int NUM_PAGES = 2;

        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NotNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return ChildFragment.create(dataBaseName, "tap_man");
            }
            return ChildFragment.create(dataBaseName, "tap_woman");
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

}
