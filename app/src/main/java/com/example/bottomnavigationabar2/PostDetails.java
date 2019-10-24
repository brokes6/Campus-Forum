package com.example.bottomnavigationabar2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bottomnavigationabar2.R;
import com.example.bottomnavigationabar2.adapter.CommentExpandAdapter;
import com.example.bottomnavigationabar2.bean.CommentBean;
import com.example.bottomnavigationabar2.bean.CommentDetailBean;
import com.example.bottomnavigationabar2.bean.ReplyDetailBean;
import com.example.bottomnavigationabar2.model.NineGridTestModel;
import com.example.bottomnavigationabar2.utils.FileCacheUtil;
import com.example.bottomnavigationabar2.view.CommentExpandableListView;
import com.example.bottomnavigationabar2.view.NineGridTestLayout;
import com.example.util.JsonTOBeanUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class PostDetails extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    public static final int HANDLER_DATA=1;
    public static final int CANCEL_PROGRESS=2;
    public static final int NOTIFY=3;
    public static final int NOTIFY_NOCOMMENT=4;
    private android.support.v7.widget.Toolbar toolbar;
    private TextView bt_comment;
    private CommentExpandableListView expandableListView;
    private CommentExpandAdapter adapter;
    private int postId;
    private int commentPage=1;
    private CommentBean commentBean;
    private List<CommentDetailBean> commentsList=new ArrayList<>();
    private BottomSheetDialog dialog;
    private TextView username;
    private TextView dateTime;
    private TextView content;
    private NineGridTestLayout nineGridTestLayout;
    private ProgressBar progressBar;
    private SmartRefreshLayout refreshLayout;
    private  Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Post post = (Post) msg.obj;
            switch (msg.what){
                case HANDLER_DATA:
                    username.setText(post.getUsername());
                    dateTime.setText(post.getPcreateTime());
                    content.setText(Html.fromHtml(post.getContent()));
                    nineGridTestLayout.setUrlList(Arrays.asList(post.getImgUrl().split(",")));
                    nineGridTestLayout.setIsShowAll(post.isShowAll());
                    break;
                case CANCEL_PROGRESS:
                    progressBar.setVisibility(View.GONE);
                    break;
                case NOTIFY:
                    int index=adapter.getCommentBeanList().size();
                    adapter.setCommentBeanList(commentsList);
                    for(int i =index; i<commentsList.size()+index; i++){
                        expandableListView.expandGroup(i);
                    }
                    adapter.notifyDataSetChanged();
                    break;

            }
        }
    };
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.post_details);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        initDetailsLayout();
        initView();
        initRefreshLayout();
    }
    //初始化
    private void initView() {
        //获取实列
        expandableListView = (CommentExpandableListView) findViewById(R.id.detail_page_lv_comment);
        bt_comment = (TextView) findViewById(R.id.detail_page_do_comment);
        bt_comment.setOnClickListener(this);
        adapter = new CommentExpandAdapter(this,commentsList);
        expandableListView.setGroupIndicator(null);
        //默认展开所有回复
        expandableListView.setAdapter(adapter);
        initExpandableListView(commentsList);
        getPopularComments();
        progressBar=findViewById(R.id.progress);
    }
    private void initRefreshLayout(){
        refreshLayout=findViewById(R.id.refreshLayout);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setRefreshFooter(new BallPulseFooter(PostDetails.this));
        refreshLayout.setFooterHeight(100);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.autoLoadMore();
                getPopularComments();
                refreshLayout.finishLoadMore();
            }
        });
    }
    private void initExpandableListView(final List<CommentDetailBean> commentsList){
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                boolean isExpanded = expandableListView.isGroupExpanded(groupPosition);
                Log.e(TAG, "onGroupClick: 当前的评论id>>>"+commentsList.get(groupPosition).getCid());
                showReplyDialog(groupPosition);
                return true;
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Toast.makeText(PostDetails.this,"点击了回复",Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onGroupClick: 当前的评论id>>>"+commentsList.get(groupPosition).getCid());
                showReplyDialog(groupPosition);
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
                String token = FileCacheUtil.getCache(PostDetails.this,"token");
                //后期需要检查token的值 查看是否被更改了喔
                if(!TextUtils.isEmpty(commentContent)){
                    progressBar.setVisibility(View.VISIBLE);
                    addComment(commentContent,token);
                    dialog.dismiss();
                    CommentDetailBean detailBean = new CommentDetailBean("czj", commentContent,"刚刚");
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
         * by   czj 2019/10/23
         * 方法:弹出回复框
         */
    private void showReplyDialog(final int position){
        dialog = new BottomSheetDialog(this,R.style.BottomSheetEdit);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        Log.i(TAG, "showReplyDialog: view="+commentView);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        commentText.setHint("回复 " + commentsList.get(position).getUsername() + " 的评论:");
        dialog.setContentView(commentView);
        View parent = (View) commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0,0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token= FileCacheUtil.getCache(PostDetails.this,"token");
                String replyContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(replyContent)){
                    dialog.dismiss();
                    Log.i(TAG, "onClick: commentId="+commentsList.get(position).getCid());
                    addReply(replyContent,token,commentsList.get(position).getCid());
                    //等会在搞
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
    private void getPostById(int postId){
        final Request request = new Request.Builder()
                .url("http://106.54.134.17/app/getPostDetailsById?postId="+postId)
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
                String dataStr = response.body().string();
                System.out.println("帖子数据"+dataStr);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataStr);
                    int code = jsonObject.getInt("code");
                    if(code==0){
                        Log.i(TAG, "onResponse:失败咯");
                        return;
                    }
                    Gson gson = new Gson();
                    Post post = gson.fromJson(jsonObject.getString("data"),Post.class);
                    if(post==null){
                        Log.i(TAG, "onResponse: 解析json数据失败");
                        return;
                    }
                    handlerData(post);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void initDetailsLayout(){
        username=findViewById(R.id.tiezi_username);
        dateTime=findViewById(R.id.tiezi_time);
        content=findViewById(R.id.tieze_Text);
        nineGridTestLayout=findViewById(R.id.layout_nine_grid);
        postId = getPostId();
        getPostById(postId);
    }
    private int getPostId(){
        Intent intent = getIntent();
        return intent.getIntExtra("postId",-1);
    }
    private void handlerData(Post post){
        Message message=new Message();
        message.what=HANDLER_DATA;
        message.obj=post;
        handler.sendMessage(message);
    }
    private void addComment(String content,String token){
        RequestBody requestBody = new FormBody.Builder()
                .add("cpid", String.valueOf(postId))
                .add("content",content)
                .add("token","HnpMvU%2BV3ZHjrbMhOaOuCA%3D%3D")
                .build();
        final Request request = new Request.Builder()
                .url("http://106.54.134.17/app/addComment")
                .post(requestBody)
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
                String dataStr = response.body().string();
                System.out.println("帖子数据"+dataStr);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataStr);
                    int code = jsonObject.getInt("code");
                    if(code==0){
                        Log.i(TAG, "onResponse:失败咯");
                        return;
                    }
                    Log.i(TAG, "onResponse:信息"+jsonObject.getString("msg"));
                    Message message=new Message();
                    message.what=CANCEL_PROGRESS;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void addReply(String content,String token,int commentUserId){
    //修改回复 设置参数
        RequestBody requestBody = new FormBody.Builder()
                .add("rcid", String.valueOf(commentUserId))
                .add("content",content)
                .add("tcuid", String.valueOf(commentUserId))
                .add("token","HnpMvU%2BV3ZHjrbMhOaOuCA%3D%3D")
                .build();
        final Request request = new Request.Builder()
                .url("http://106.54.134.17/app/addReply")
                .post(requestBody)
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
                String dataStr = response.body().string();
                System.out.println("帖子数据"+dataStr);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataStr);
                    int code = jsonObject.getInt("code");
                    if(code==0){
                        Log.i(TAG, "onResponse:失败咯");
                        return;
                    }
                    Log.i(TAG, "onResponse:信息"+jsonObject.getString("msg"));
                    Message message=new Message();
                    message.what=CANCEL_PROGRESS;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void getPopularComments(){
        final Request request =new Request.Builder()
                .url("http://106.54.134.17/app/getPopularComments?startPage="+commentPage+"&postId="+postId)
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
                String responseStr = response.body().string();
                Log.i(TAG, "onResponse: 正文"+responseStr);
                try {
                    JSONObject jsonObject = new JSONObject(responseStr);
                    int code=jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    if(code==0){
                        Log.i(TAG, "onResponse: 返回评论失败:"+msg);
                        Message message = new Message();
                        commentsList=new ArrayList<>();
                        message.what=NOTIFY;
                        handler.sendMessage(message);
                        return;
                    }
                    Gson gson = new Gson();
                    commentsList=gson.fromJson(jsonObject.getString("data"),new TypeToken<List<CommentDetailBean>>(){}.getType());
                    Message message = new Message();
                    message.what=NOTIFY;
                    handler.sendMessage(message);
                    commentPage++;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
