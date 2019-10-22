package com.example.bottomnavigationabar2;

import android.graphics.Color;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bottomnavigationabar2.R;
import com.example.bottomnavigationabar2.adapter.CommentExpandAdapter;
import com.example.bottomnavigationabar2.bean.CommentBean;
import com.example.bottomnavigationabar2.bean.CommentDetailBean;
import com.example.bottomnavigationabar2.bean.ReplyDetailBean;
import com.example.bottomnavigationabar2.model.NineGridTestModel;
import com.example.bottomnavigationabar2.view.CommentExpandableListView;
import com.example.util.JsonTOBeanUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.support.constraint.Constraints.TAG;


public class PostDetails extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private android.support.v7.widget.Toolbar toolbar;
    private TextView bt_comment;
    private CommentExpandableListView expandableListView;
    private CommentExpandAdapter adapter;
    private int page=1;
    private CommentBean commentBean;
    private List<CommentDetailBean> commentsList;
    private List<NineGridTestModel> mList = new ArrayList<>();
    private BottomSheetDialog dialog;
    //图片
    private String[] mUrls = new String[]{
            "http://106.54.134.17/image/topicalimg/test.png",
            "http://106.54.134.17/image/topicalimg/test.png",
            "http://106.54.134.17/image/topicalimg/test.png",
            "http://106.54.134.17/image/topicalimg/test.png",
            "http://106.54.134.17/image/topicalimg/test.png",
            "http://106.54.134.17/image/topicalimg/test.png",
            "http://106.54.134.17/image/topicalimg/test.png",
            "http://106.54.134.17/image/topicalimg/test.png",
            "http://106.54.134.17/image/topicalimg/test.png",
    };
    //文字
    private String testJson = "{\n" +
            "\t\"code\": 1000,\n" +
            "\t\"message\": \"查看评论成功\",\n" +
            "\t\"data\": {\n" +
            "\t\t\"total\": 3,\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\t\"id\": 42,\n" +
            "\t\t\t\t\"nickName\": \"程序猿\",\n" +
            "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\"content\": \"时间是一切财富中最宝贵的财富。\",\n" +
            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
            "\t\t\t\t\"replyTotal\": 1,\n" +
            "\t\t\t\t\"createDate\": \"三分钟前\",\n" +
            "\t\t\t\t\"replyList\": [{\n" +
            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\t\"id\": 40,\n" +
            "\t\t\t\t\t\"commentId\": \"42\",\n" +
            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
            "\t\t\t\t\t\"status\": \"01\",\n" +
            "\t\t\t\t\t\"createDate\": \"一个小时前\"\n" +
            "\t\t\t\t}]\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"id\": 41,\n" +
            "\t\t\t\t\"nickName\": \"设计狗\",\n" +
            "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\"content\": \"这世界要是没有爱情，它在我们心中还会有什么意义！这就如一盏没有亮光的走马灯。\",\n" +
            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
            "\t\t\t\t\"replyTotal\": 1,\n" +
            "\t\t\t\t\"createDate\": \"一天前\",\n" +
            "\t\t\t\t\"replyList\": [{\n" +
            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\t\"commentId\": \"41\",\n" +
            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
            "\t\t\t\t\t\"status\": \"01\",\n" +
            "\t\t\t\t\t\"createDate\": \"三小时前\"\n" +
            "\t\t\t\t}]\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"id\": 40,\n" +
            "\t\t\t\t\"nickName\": \"产品喵\",\n" +
            "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\"content\": \"笨蛋自以为聪明，聪明人才知道自己是笨蛋。\",\n" +
            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
            "\t\t\t\t\"replyTotal\": 0,\n" +
            "\t\t\t\t\"createDate\": \"三天前\",\n" +
            "\t\t\t\t\"replyList\": []\n" +
            "\t\t\t}\n" +
            "\t\t]\n" +
            "\t}\n" +
            "}";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_details);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        getPostList();
        initView();
    }
    //初始化
    private void initView() {
        //获取实列
        expandableListView = (CommentExpandableListView) findViewById(R.id.detail_page_lv_comment);
        bt_comment = (TextView) findViewById(R.id.detail_page_do_comment);
        bt_comment.setOnClickListener(this);
        commentsList = generateTestData();
        initExpandableListView(commentsList);
    }



    private void initExpandableListView(final List<CommentDetailBean> commentList){
        expandableListView.setGroupIndicator(null);
        //默认展开所有回复
        adapter = new CommentExpandAdapter(this, commentList);
        expandableListView.setAdapter(adapter);
        for(int i = 0; i<commentList.size(); i++){
            expandableListView.expandGroup(i);
        }
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                boolean isExpanded = expandableListView.isGroupExpanded(groupPosition);
                Log.e(TAG, "onGroupClick: 当前的评论id>>>"+commentList.get(groupPosition).getId());
                showReplyDialog(groupPosition);
                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Toast.makeText(PostDetails.this,"点击了回复",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //toast("展开第"+groupPosition+"个分组");

            }
        });

    }

    /**
     * by moos on 2019/10/16
     * 作用:生成测试数据
     * 评论数据
     */
    private List<CommentDetailBean> generateTestData(){
        Gson gson = new Gson();
        commentBean = gson.fromJson(testJson, CommentBean.class);
        List<CommentDetailBean> commentList = commentBean.getData().getList();
        return commentList;
    }
    //onOptionsItemSelected方法
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //重写onClick方法
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
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        dialog.setContentView(commentView);
        /**
         * 解决bsd显示不全的情况
         */
        //弹出对话框
        View parent = (View) commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0,0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());
        //commentView.getMeasuredHeight()获得的参数是 632 嘻嘻
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(commentContent)){
                    //commentOnWork(commentContent);
                    dialog.dismiss();
                    CommentDetailBean detailBean = new CommentDetailBean("付鑫博", commentContent,"刚刚");
                    adapter.addTheCommentData(detailBean);
                    Toast.makeText(PostDetails.this,"评论成功",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(PostDetails.this,"评论内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }
/**
 * by moos on 2019/10/16
 * 方法:弹出回复框
 */
private void showReplyDialog(final int position){
    dialog = new BottomSheetDialog(this);
    View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
    final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
    final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
    commentText.setHint("回复 " + commentsList.get(position).getNickName() + " 的评论:");
    dialog.setContentView(commentView);
    bt_comment.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String replyContent = commentText.getText().toString().trim();
            if(!TextUtils.isEmpty(replyContent)){

                dialog.dismiss();
                ReplyDetailBean detailBean = new ReplyDetailBean("付鑫博",replyContent);
                adapter.addTheReplyData(detailBean, position);
                expandableListView.expandGroup(position);
                Toast.makeText(PostDetails.this,"回复成功",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(PostDetails.this,"回复内容不能为空",Toast.LENGTH_SHORT).show();
            }
        }
    });
    commentText.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
            }else {
                bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
            }
        }
        @Override
        public void afterTextChanged(Editable editable) {
        }
    });
    dialog.show();
}
    private void getPostList(){
        final Request request = new Request.Builder()
                .url("http://106.54.134.17/app/getPopularPost?startPage="+page)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d(TAG, "onFailure:失败呃");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                String responseStr = response.body().string();
                Log.i(TAG, "onResponse:---"+responseStr);
                JSONObject jsonObject = new JSONObject(responseStr);
                int code=jsonObject.getInt("code");
                if(code==0){
                    Toast.makeText(PostDetails.this,"别搞拉，去看看其他的地方把",Toast.LENGTH_SHORT).show();
                    return;
                }
                String dataStr = jsonObject.getString("data");
                Gson gson = new Gson();
                List<Post> posts = gson.fromJson(dataStr, new TypeToken<List<Post>>() {
                }.getType());
                for(Post post:posts){
                    NineGridTestModel model1 = new NineGridTestModel();
                    String[]imgurls = post.getImgUrl().split(",");
                    for(String url:imgurls){
                        model1.urlList.add(url);
                    }
                    model1.username=post.getUsername();
                    model1.uimg=post.getUimg();
                    model1.datetime=post.getPcreateTime();
                    System.out.println("正文内容"+post.getContent());
                    model1.content=post.getContent();
                    mList.add(model1);
                }
                page++;
                }catch(Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }



}
