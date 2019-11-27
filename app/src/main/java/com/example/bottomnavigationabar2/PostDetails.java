package com.example.bottomnavigationabar2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bottomnavigationabar2.R;
import com.example.bottomnavigationabar2.adapter.CommentExpandAdapter;
import com.example.bottomnavigationabar2.bean.CommentBean;
import com.example.bottomnavigationabar2.bean.CommentDetailBean;
import com.example.bottomnavigationabar2.bean.ReplyDetailBean;
import com.example.bottomnavigationabar2.bean.User;
import com.example.bottomnavigationabar2.model.NineGridTestModel;
import com.example.bottomnavigationabar2.utils.FileCacheUtil;
import com.example.bottomnavigationabar2.utils.NetWorkUtil;
import com.example.bottomnavigationabar2.view.CommentExpandableListView;
import com.example.bottomnavigationabar2.view.NineGridTestLayout;
import com.example.util.DateTimeUtil;
import com.example.util.JsonTOBeanUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;

import org.angmarch.views.NiceSpinner;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.scwang.smartrefresh.layout.internal.InternalClassics.ID_IMAGE_PROGRESS;
import static com.scwang.smartrefresh.layout.internal.InternalClassics.ID_TEXT_TITLE;


public class PostDetails extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    public static final int HANDLER_DATA=1;
    public static final int CANCEL_PROGRESS=2;
    public static final int NOTIFY=3;
    public static final int NOTIFY_NOCOMMENT=4;
    public static final int NOTIFY_COMMENT=5;
    public static final String REQUEST_POST_DETAILS_STR="http://106.54.134.17/app/getPostDetailsById";//mode=1
    public static final String REQUEST_COMMENT_STR="http://106.54.134.17/app/getPopularComments";//mode=2
    public static final String REQUEST_ADD_COMMENT_STR="http://106.54.134.17/app/addComment";//mode=3
    private android.support.v7.widget.Toolbar toolbar;
    private TextView bt_comment;
    private CommentExpandableListView expandableListView;
    private CommentExpandAdapter adapter;
    private int postId;
    private int commentPage=1;
    private int status=0;
    private CommentBean commentBean;
    private List<CommentDetailBean> commentsList=new ArrayList<>();
    private BottomSheetDialog dialog;
    private TextView username;
    private TextView dateTime;
    private TextView content;
    private TextView message;
    private TextView loveNumStr;
    private LinearLayout back;
    private TextView commentStr;
    private MyImageView userImg;
    private ImageView loveNum;
    private LinearLayout messageLayout;
    private LinearLayout commentLayout;
    private NineGridTestLayout nineGridTestLayout;
    private LinearLayout loveLayout;
    private ProgressBar progressBar;
    private SmartRefreshLayout refreshLayout;
    private NetWorkUtil netWorkUtil;
    private User userData;
    private NiceSpinner niceSpinner;
    private List<String> spinnerData = new LinkedList<>(Arrays.asList("热度排序", "点赞排序","时间排序"));
    private  Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            final Post post = (Post) msg.obj;
            switch (msg.what){
                case HANDLER_DATA:
                    String str=Html.fromHtml(post.getContent()).toString();
                    String imgUrls=post.getImgUrl();
                    username.setText(post.getUsername());
                    userImg.setImageURL(post.getUimg());
                    dateTime.setText(DateTimeUtil.handlerDateTime(post.getPcreateTime()));
                    content.setText(str);
                    commentStr.setText(String.valueOf(post.getCommentCount()));
                    if(imgUrls==null||imgUrls.trim().equals("")){
                        nineGridTestLayout.setVisibility(View.GONE);
                    }else {
                        nineGridTestLayout.setUrlList(Arrays.asList(imgUrls.split(",")));
                        nineGridTestLayout.setIsShowAll(post.isShowAll());
                    }
                    nineGridTestLayout.setContent(str);
                    status=post.getStatus();
                    loveNumStr.setText(String.valueOf(post.getLoveCount()));
                    loveLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (status==1){
                                loveNum.setImageDrawable(getResources().getDrawable(R.drawable.thumbs_up_white));
                                status=0;
                                loveNumStr.setText(String.valueOf(Integer.valueOf(loveNumStr.getText().toString())-1));
                            }else{
                                loveNum.setImageDrawable(getResources().getDrawable(R.drawable.thumbs_up_complete));
                                status=1;
                                loveNumStr.setText(String.valueOf(Integer.valueOf(loveNumStr.getText().toString())+1));
                            }
                            netWorkUtil.updatePostLove(postId,userData.getToken());
                        }
                    });
                    if(status==1){
                        loveNum.setImageDrawable(getResources().getDrawable(R.drawable.thumbs_up_complete));
                    }else{
                        loveNum.setImageDrawable(getResources().getDrawable(R.drawable.thumbs_up_white));
                    }
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
                    if(index<6){
                        refreshLayout.setEnableLoadMore(false);
                        Log.i(TAG, "handleMessage: 不能够加载更多");
                    }
                    messageLayout.setVisibility(View.VISIBLE);
                    message.setText("暂无更多");
                    message.setTextSize(14);
                    break;
                case NOTIFY_NOCOMMENT:
                    Log.i(TAG, "handleMessage: 喔有运行？");
                    commentLayout.setMinimumHeight(500);
                    refreshLayout.setEnableLoadMore(false);
                    messageLayout.setVisibility(View.VISIBLE);
                    break;
                case NOTIFY_COMMENT:
                    int index1=adapter.getCommentBeanList().size();
                    commentStr.setText(String.valueOf(Integer.valueOf(commentStr.getText().toString())+1));
                    progressBar.setVisibility(View.GONE);
                    if(index1>=6)
                        messageLayout.setVisibility(View.GONE);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.post_details);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //修改为深色，因为我们把状态栏的背景色修改为主题色白色，默认的文字及图标颜色为白色，导致看不到了。
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        userData=FileCacheUtil.getUser(this);
        postId = getPostId();
        initView();
        click();
        initDetailsLayout();
        initRefreshLayout();
    }
    //初始化
    private void initView() {
        //获取实列
        back = findViewById(R.id.back);
        userImg=findViewById(R.id.tieze_user_img);
        message = findViewById(R.id.message);
        messageLayout=findViewById(R.id.messageLayout);
        commentLayout=findViewById(R.id.detail_page_comment_container);
        expandableListView = (CommentExpandableListView) findViewById(R.id.detail_page_lv_comment);
        bt_comment = (TextView) findViewById(R.id.detail_page_do_comment);
        bt_comment.setOnClickListener(this);
        adapter = new CommentExpandAdapter(this,commentsList,userData.getToken());
        expandableListView.setGroupIndicator(null);
        //默认展开所有回复
        expandableListView.setAdapter(adapter);
        initExpandableListView();
        getPopularComments();
        progressBar=findViewById(R.id.progress);
        Log.i(TAG, "initView: userData="+userData);
        niceSpinner = findViewById(R.id.nice_spinner);
        niceSpinner.attachDataSource(spinnerData);
        niceSpinner.setBackgroundResource(R.drawable.textview_round_border);
        niceSpinner.setTextColor(Color.BLACK);
        niceSpinner.setTextSize(13);
        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    private void click(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("loveNum",loveNumStr.getText().toString());
                intent.putExtra("talkNum",commentStr.getText().toString());
                intent.putExtra("status",status);
                setResult(HomeFragment.POSTDETAILS,intent);
                finish();
            }
        });
    }
    private void initRefreshLayout(){
        refreshLayout=findViewById(R.id.refreshLayout);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        initRefreshFootLayout();
    }
    private void initExpandableListView(){
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                boolean isExpanded = expandableListView.isGroupExpanded(groupPosition);
                Log.e(TAG, "onGroupClick: 当前的评论id>>>"+adapter.getCommentBeanList().get(groupPosition).getCid());
                showReplyDialog(groupPosition);
                return true;
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                if(childPosition==2) {
                    Log.i(TAG, "onChildClick: 点击查看更多");
                    String name = adapter.getCommentBeanList().get(groupPosition).getUsername();
                    String data = adapter.getCommentBeanList().get(groupPosition).getContent();
                    String time = DateTimeUtil.handlerDateTime(adapter.getCommentBeanList().get(groupPosition).getCcreateTime());
                    String url = adapter.getCommentBeanList().get(groupPosition).getUimg();
                    Intent intent = new Intent(PostDetails.this, MoerReply.class);
                    intent.putExtra("data",data);
                    intent.putExtra("url",url);
                    intent.putExtra("time",time);
                    intent.putExtra("name",name);
                    Log.d(TAG, "名字为----------------"+name);
                    Log.d(TAG, "时间为----------------"+time);
                    Log.d(TAG, "内容为----------------"+data);
                    Log.d(TAG, "url为----------------"+url);
                    startActivity(intent);
                    return false;
                }
                Toast.makeText(PostDetails.this, "点击了回复", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onGroupClick: -----当前的评论id>>>" + adapter.getCommentBeanList().get(groupPosition).getCid());
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
                //后期需要检查token的值 查看是否被更改了喔
                if(!TextUtils.isEmpty(commentContent)){
                    progressBar.setVisibility(View.VISIBLE);
                    addComment(commentContent,userData.getUsername(),content.getText().toString(),postId);
                    dialog.dismiss();
                    CommentDetailBean detailBean = new CommentDetailBean(userData.getUsername(), commentContent,"刚刚");
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
        commentText.setHint("回复 " +adapter.getCommentBeanList().get(position).getUsername() + " 的评论:");
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
                    Log.i(TAG, "onClick: commentId="+adapter.getCommentBeanList().get(position).getCid());
                    CommentDetailBean commentDetailBean=adapter.getCommentBeanList().get(position);
                    addReply(replyContent,token,commentDetailBean.getUid(),commentDetailBean.getContent(),commentDetailBean.getCid());
                    //等会在搞
                    Log.i(TAG, "onClick: 账号名为:"+userData.getUsername());
                    ReplyDetailBean detailBean = new ReplyDetailBean(userData.getUsername(),replyContent);
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
    private void getPostById(){
        final Request request = new Request.Builder()
                .url(getRequestStr(1))
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
        netWorkUtil=new NetWorkUtil(this);
        username=findViewById(R.id.tiezi_username);
        dateTime=findViewById(R.id.tiezi_time);
        content=findViewById(R.id.tieze_Text);
        nineGridTestLayout=findViewById(R.id.layout_nine_grid);
        Log.i(TAG, "initDetailsLayout: ---------id=="+postId);
        loveNum=findViewById(R.id.loveNum);
        loveNumStr=findViewById(R.id.loveNumStr);
        loveLayout=findViewById(R.id.loveLayout);
        commentStr=findViewById(R.id.commentStr);
        getPostById();
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
    private void addComment(String content,String username,String postContent,int puid){
        RequestBody requestBody = new FormBody.Builder()
                .add("cpid", String.valueOf(postId))
                .add("content",content)
                .add("token",userData.getToken())
                .add("username",username)
                .add("postContent",postContent)
                .add("puid",String.valueOf(puid))
                .build();
        final Request request = new Request.Builder()
                .url(REQUEST_ADD_COMMENT_STR)
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
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataStr);
                    int code = jsonObject.getInt("code");
                    if(code==0){
                        Log.i(TAG, "onResponse:失败咯");
                        return;
                    }
                    Message message=new Message();
                    message.what=NOTIFY_COMMENT;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void addReply(String content,String token,int commentUserId,String commentContent,int cid){
    //修改回复 设置参数
        RequestBody requestBody = new FormBody.Builder()
                .add("rcid", String.valueOf(commentUserId))
                .add("content",content)
                .add("tcuid", String.valueOf(commentUserId))
                .add("token","HnpMvU%2BV3ZHjrbMhOaOuCA%3D%3D")
                .add("pid", String.valueOf(postId))
                .add("commentContent",commentContent)
                .add("cid",String.valueOf(cid))
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
                .url(getRequestStr(2))
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
                        message.what=NOTIFY_NOCOMMENT;

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
    private String getRequestStr(int mode){
        String urlStr=null;
        String token=null;
        switch (mode){
            case 1:urlStr=REQUEST_POST_DETAILS_STR+"?postId="+postId;
                            break;
            case 2:urlStr=REQUEST_COMMENT_STR+"?startPage="+commentPage+"&postId="+postId;
                Log.i(TAG, "getRequestStr: postId="+postId);
                            break;
            default:break;
        }
        token=userData.getToken();
        if(token==null) {
            return urlStr;
        }
        return urlStr+"&token="+token;
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("loveNum",loveNumStr.getText().toString());
        intent.putExtra("talkNum",commentStr.getText().toString());
        intent.putExtra("status",status);
        setResult(HomeFragment.POSTDETAILS,intent);
        finish();
    }
    private void initRefreshFootLayout(){
        refreshLayout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
        final TextView tv = refreshLayout.getLayout().findViewById(ID_TEXT_TITLE);
        final ImageView iv2 = refreshLayout.getLayout().findViewById(ID_IMAGE_PROGRESS);
        final AtomicBoolean net = new AtomicBoolean(true);
        final AtomicInteger mostTimes = new AtomicInteger(0);//假设只有三屏数据
        //设置多监听器，包括顶部下拉刷新、底部上滑刷新
        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener(){

            /**
             * 根据上拉的状态，设置文字，并且判断条件
             * @param refreshLayout
             * @param oldState
             * @param newState
             */
            @Override
            public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
                switch (newState) {
                    case None:
                    case PullUpToLoad:
                        break;
                    case Loading:
                    case LoadReleased:
                        tv.setText("正在加载..."); //在这里修改文字
                        if (!NetWorkUtil.isNetworkConnected(getApplicationContext())) {
                            net.set(false);
                        } else {
                            net.set(true);
                        }
                        break;
                    case ReleaseToLoad:
                        tv.setText("release");
                        break;
                    case Refreshing:
                        tv.setText("refreshing");
                        break;
                }
            }

            /**
             * 添加是否可以加载更多数据的条件
             * @param refreshLayout
             */
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mostTimes.get() < 3) {

                    mostTimes.getAndIncrement();
                }
                refreshLayout.finishLoadMore(1000); //这个记得设置，否则一直转圈
            }

            /**
             *  在这里根据不同的情况来修改加载完成后的提示语
             * @param footer
             * @param success
             */
            @Override
            public void onFooterFinish(RefreshFooter footer, boolean success) {
                super.onFooterFinish(footer, success);
                if (net.get() == false) {
                    tv.setText("请检查网络设置");
                } else if (mostTimes.get() >= 3) {
                    tv.setText("没有更多消息拉");
                } else {
                    tv.setText("加载完成");
                    if (mostTimes.get() == 2) {
                        mostTimes.getAndIncrement();
                    }
                }
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                Log.i(TAG, "onLoadMore: 下拉加载");
                refreshLayout.autoLoadMore();
            }
        });
    }
}
