package com.example.bottomnavigationabar2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bottomnavigationabar2.adapter.CommentExpandAdapter;
import com.example.bottomnavigationabar2.bean.CommentDetailBean;
import com.example.bottomnavigationabar2.bean.User;

public class MoerReply extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MoerReply";
    private CommentExpandAdapter adapter;
    private ProgressBar progressBar;
    private BottomSheetDialog dialog;
    private com.example.bottomnavigationabar2.MyImageView userimg;
    private TextView username;
    private TextView text;
    private TextView floor;
    private TextView Time;
    private TextView see;
    private LinearLayout back;
    private User userData;
    private TextView content;
    private int postId;
    private TextView bt_comment;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moer_reply);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //修改为深色，因为我们把状态栏的背景色修改为主题色白色，默认的文字及图标颜色为白色，导致看不到了。
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        initView();
        initdata();
        click();
    }
    private void initView() {
        see = findViewById(R.id.tiezi_see);
        bt_comment = (TextView) findViewById(R.id.detail_page_do_comment);
        userimg = findViewById(R.id.moer_user_img);
        username = findViewById(R.id.moer_username);
        text = findViewById(R.id.moer_Text);
        floor = findViewById(R.id.moer_floor);
        Time = findViewById(R.id.more_time);
        back = findViewById(R.id.back);
    }
    private void click(){
        bt_comment.setOnClickListener(this);
        see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initdata(){
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String data = intent.getStringExtra("data");
        String time1 = intent.getStringExtra("time");
        String url = intent.getStringExtra("url");
        userimg.setImageURL(url);
        username.setText(name);
        text.setText(data);
        Time.setText(time1);
    }
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.detail_page_do_comment){
            showCommentDialog();
        }
    }
    /**
     *2019/10/16
     * 方法：弹出评论框
     */
    private void showCommentDialog(){
        dialog = new BottomSheetDialog(this,R.style.BottomSheetEdit);
        final View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        dialog.setContentView(commentView);
        View parent = (View) commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0,0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentContent = commentText.getText().toString().trim();
                //后期需要检查token的值 查看是否被更改了喔
                if(!TextUtils.isEmpty(commentContent)){
                    progressBar.setVisibility(View.VISIBLE);
//                    addComment(commentContent,userData.getUsername(),content.getText().toString(),postId);
                    dialog.dismiss();
                    CommentDetailBean detailBean = new CommentDetailBean(userData.getUsername(), commentContent,"刚刚");
                    adapter.addTheCommentData(detailBean);
                    Toast.makeText(MoerReply.this,"评论成功",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(MoerReply.this,"评论内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

}
