package com.gc.tapCompute.view;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gc.tapCompute.R;
import com.lxj.xpopup.core.CenterPopupView;

import org.jetbrains.annotations.NotNull;

/**
 * Description: Description
 * Author: gc
 * CreateDate: 2024/7/12 11:25
 * Version: 1.0
 */
public class AddPopup extends CenterPopupView {

    private Spinner etName;
    private EditText etAttack, etCost;
    private PopupBackData mPopupBackData;
    public String tapName = "tap_man";

    public AddPopup(@NonNull @NotNull Context context, String tapName) {
        super(context);
        this.tapName = tapName;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_add_item;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        etName = findViewById(R.id.et_name); //获取输入的项目名称
        etAttack = findViewById(R.id.et_attack); //获取输入的攻击力
        etCost = findViewById(R.id.et_cost); //获取输入的项目花费

        Resources res = getResources();
        String[] ite_man = res.getStringArray(R.array.items_man);
        String[] ite_woman = res.getStringArray(R.array.items_woman);


        if (tapName.equals("tap_man")) {
            ArrayAdapter<String> adapter_man = new ArrayAdapter<>(contentView.getContext(), android.R.layout.simple_spinner_dropdown_item, ite_man);
            adapter_man.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            etName.setAdapter(adapter_man);
        } else if (tapName.equals("tap_woman")) {
            ArrayAdapter<String> adapter_woman = new ArrayAdapter<>(contentView.getContext(), android.R.layout.simple_spinner_dropdown_item, ite_woman);
            adapter_woman.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            etName.setAdapter(adapter_woman);
        }

//        etAttack.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//        etCost.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        findViewById(R.id.tv_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // 关闭弹窗
            }
        });

        findViewById(R.id.tv_confirm).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String tapName = etName.getSelectedItem().toString();
                String tapAttack = etAttack.getText().toString();
                String tapCost = etCost.getText().toString();
                if (tapName.trim().equals("") || tapCost.trim().equals("") || tapAttack.trim().equals(".") || tapCost.trim().equals(".")) {
                    ToastUtils.showShort("请输入具体数据");
                } else if (ObjectUtils.isNotEmpty(mPopupBackData)) {
                    mPopupBackData.onConfirmClick(tapName, tapAttack, tapCost);
                    dismiss();
                }
            }
        });
    }

    public void setOnConfirmClick(PopupBackData popupBackData) {
        mPopupBackData = popupBackData;
    }

    public interface PopupBackData {

        /**
         * @param tapName   项目名称
         * @param tapAttack 攻击力
         * @param tapCost   花费
         */
        void onConfirmClick(String tapName, String tapAttack, String tapCost);
    }
}
