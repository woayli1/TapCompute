package com.tapcompute;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.AndroidCharacter;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends Activity {

    public static String status = "tap_man"; //判断界面状态
    Intent t = new Intent();

    DataHelper dataHelper;

    Spinner editText; //用于读取项目名称
    EditText editText2; //用于读取攻击力
    EditText editText3; //用于读取项目花费

    ListView listView; //界面数据容器

    boolean a = false;  //用于判断输入的关卡是否为整数
    int b; //接收输入的关卡

    TextView textView;
    AlertDialog alertDialog;

    ArrayAdapter<String> adapter_man;
    ArrayAdapter<String> adapter_woman;

    public static ArrayList<String> items_name;
    public static ArrayList<String> items_name2;
    public static ArrayList<String> items_name3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        if (status.equals("tap_man")) {
            showmessgae("当前为男主");
        } else {
            showmessgae("当前为女主");
        }
        dataHelper = new DataHelper(this, "TapCompute", null, 1);
        refush();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.changeitem, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.woman_item:
                showmessgae("女主");
                status = "tap_woman";
                refush();
                break;
            case R.id.man_item:
                showmessgae("男主");
                status = "tap_man";
                refush();
                break;
            case R.id.compute:
                dialog3();
                break;
            case R.id.grade:
                dialog5();
                break;
        }
        return true;
    }

    public void onSelectClick(View view) {
        showmessgae("已刷新");
        refush();
    }

    public void onAddClick(View view) {
        dialog();
    }

    public void showmessgae(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        return;
    }

    public void dialog() {
        final LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.additem, (ViewGroup) findViewById(R.id.additem));

        editText = (Spinner) layout.findViewById(R.id.editText); //获取输入的项目名称
        editText2 = (EditText) layout.findViewById(R.id.editText2); //获取输入的攻击力
        editText3 = (EditText) layout.findViewById(R.id.editText3); //获取输入的项目花费

        Resources res = getResources();
        String[] ite_man = res.getStringArray(R.array.items_man);
        String[] ite_woman = res.getStringArray(R.array.items_woman);

        if (status.equals("tap_man")) {
            adapter_man = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ite_man);
            adapter_man.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            editText.setAdapter(adapter_man);
        } else if (status.equals("tap_woman")) {
            adapter_woman = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ite_woman);
            adapter_woman.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            editText.setAdapter(adapter_woman);
        }

        editText2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText3.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        alertDialog = new AlertDialog.Builder(this).setView(layout).setTitle("添加新项目").setPositiveButton("添加",

                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String tap_name = editText.getSelectedItem().toString();
                        String tap_atc = editText2.getText().toString();
                        String tap_cost = editText3.getText().toString();
                        if (tap_atc.trim().equals("") || tap_cost.trim().equals("") || tap_atc.trim().equals(".") || tap_cost.trim().equals(".")) {
                            showmessgae("请输入具体数据");
                            preventDismissDialog();
                        } else {
                            dataHelper.insertinto(status, tap_name, tap_atc, tap_cost);
                            //dialog.dismiss();
                            dismissDialog();
                            showmessgae("添加成功");
                            refush();
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismissDialog();
            }
        }).show();
    }

    public void dialog2(final String i, final String j) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认删除吗？");
        builder.setTitle(i);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dataHelper.delete(status, i, j);
                showmessgae("删除成功");
                refush();
                dialog.dismiss();
            }
        }).setNegativeButton("取消", null).show();
    }

    public void refush() {
        items_name = new ArrayList<String>();
        items_name2 = new ArrayList<String>();
        items_name3 = new ArrayList<String>();
        items_name = dataHelper.getSelectStringItem(status, "tap_name");
        items_name2 = dataHelper.getSelectStringItem(status, "tap_attack");
        items_name3 = dataHelper.getSelectStringItem(status, "tap_cost");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.textView);
                TextView textView2 = (TextView) view.findViewById(R.id.textView2);
                String str = textView.getText().toString();
                String str2 = textView2.getText().toString();
                dialog2(str, str2);
            }
        });
        RefushItem refushItem = new RefushItem(this);
        listView.setAdapter(refushItem);
        if (listView.getCount() == 0) {
            showmessgae("无数据，请添加");
        }
    }

    /**
     * 关闭对话框
     */
    private void dismissDialog() {
        try {
            Field field = alertDialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(alertDialog, true);
        } catch (Exception e) {
        }
        alertDialog.dismiss();
    }

    /**
     * 通过反射 阻止关闭对话框
     */
    private void preventDismissDialog() {
        try {
            Field field = alertDialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            //设置mShowing值，欺骗android系统
            field.set(alertDialog, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dialog3() {  //计算关卡蜕变量
        final EditText editText1 = new EditText(this);
        editText1.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText1.setGravity(Gravity.CENTER);
        alertDialog = new AlertDialog.Builder(this).setTitle("请输入当前关卡").setView(editText1).setPositiveButton("计算", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    b = Integer.parseInt(editText1.getText().toString().trim());
                    a = true;
                } catch (Exception e) {
                    showmessgae("请输入整数");
                    a = false;
                }
                if (b < 80) {
                    showmessgae("请完成90关卡后再进行计算");
                    preventDismissDialog();
                } else if (a) {
                    int c = (b - 90) / 15 + 1;
                    int d = 15 - (b - 90) % 15;
                    refush();
                    dismissDialog();
                    dialog4(c,d,"关卡");
                } else {
                    preventDismissDialog();
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismissDialog();
            }
        }).show();
    }

    public void dialog4(final int i, final int j,final String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("当前的蜕变量为: " + i + " ,还需" + j + s +"满足下一要求");
        builder.setTitle("蜕变量");
        builder.setPositiveButton("确认",null).setNegativeButton("关闭", null).show();
    }
    public void dialog5() {  //计算英雄等级蜕变量
        final EditText editText1 = new EditText(this);
        editText1.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText1.setGravity(Gravity.CENTER);
        alertDialog = new AlertDialog.Builder(this).setTitle("请输入当前等级").setView(editText1).setPositiveButton("计算", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    b = Integer.parseInt(editText1.getText().toString().trim());
                    a = true;
                } catch (Exception e) {
                    showmessgae("请输入整数");
                    a = false;
                }
                if (b < 500) {
                    showmessgae("请达到500级后再进行计算");
                    preventDismissDialog();
                } else if (a) {
                    int c = (b - 500) / 1000 + 1;
                    int d = 1000 - (b - 500) % 1000;
                    refush();
                    dismissDialog();
                    dialog4(c,d,"级");
                } else {
                    preventDismissDialog();
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismissDialog();
            }
        }).show();
    }

}
