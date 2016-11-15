package com.mu.compet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mu.compet.R;
import com.mu.compet.ReplyAdapter;
import com.mu.compet.data.Board;
import com.mu.compet.data.FileData;
import com.mu.compet.data.ListData;
import com.mu.compet.data.Reply;
import com.mu.compet.data.ResultMessage;
import com.mu.compet.manager.NetworkManager;
import com.mu.compet.manager.NetworkRequest;
import com.mu.compet.manager.PropertyManager;
import com.mu.compet.request.AddReplyRequest;
import com.mu.compet.request.DeleteBoardRequest;
import com.mu.compet.request.ListFileRequest;
import com.mu.compet.request.ListReplyRequest;
import com.mu.compet.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class DetailBoardActivity extends BaseActivity {

    private static final String TAG = DetailBoardActivity.class.getSimpleName();
    private ListView listView;
    private ReplyAdapter mAdapter;
    private EditText replyEdit;
    private Button writeButton;

    private ImageView profileImage;
    private TextView nickNameText;
    private TextView postDateText;
    private ImageView firstImage;
    private ImageView secondImage;
    private ImageView thirdImage;
    private TextView contentText;

    private ImageView images[] = new ImageView[3];

    private Board board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);
        initToolBar(getString(R.string.activity_detail_post));

        Intent intent = getIntent();
        board = (Board) intent.getSerializableExtra("board");
        listView = (ListView) findViewById(R.id.listView);
        Log.d("DetailBoardActivity", "세션 유저 넘버 : " + PropertyManager.getInstance().getUserNum() + "보드 유저 넘버 : " + board.getUserNum());

        replyEdit = (EditText) findViewById(R.id.edit_content);
        replyEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    listView.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            listView.smoothScrollBy(0, 800);
                        }

                    }, 100);

                }
            }
        });
        writeButton = (Button) findViewById(R.id.btn_write_comment);

        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String replyContent = replyEdit.getText().toString();
                String boardNum = String.valueOf(board.getBoardNum());

                if (!TextUtils.isEmpty(replyContent)) {
                    addReplyRequest(replyContent, boardNum);
                } else {
                    Toast.makeText(DetailBoardActivity.this, "댓글을 입력하세요.", Toast.LENGTH_LONG).show();
                }
            }
        });


        View headerView = LayoutInflater.from(this).inflate(R.layout.view_detail_header, null, false);
        initHeader(headerView, board);
        listView.addHeaderView(headerView);
        String boardNum = String.valueOf(board.getBoardNum());
        mAdapter = new ReplyAdapter(boardNum);
        mAdapter.setOnAdapterReplyListener(new ReplyAdapter.OnAdapterReplyClickListener() {
            @Override
            public void onAdapterReplyClick(int code) {
                listReplyRequest();
            }
        });
        listView.setAdapter(mAdapter);

        initData();


    }

    private void addReplyRequest(String replyContent, String boardNum) {
        AddReplyRequest request = new AddReplyRequest(this, boardNum, replyContent);
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ResultMessage>() {
            @Override
            public void onSuccess(NetworkRequest<ResultMessage> request, ResultMessage result) {
                Log.d(TAG, "성공 : " + result.getMessage());
                replyEdit.setText("");
                listReplyRequest();
                listView.smoothScrollToPosition(0);

            }

            @Override
            public void onFail(NetworkRequest<ResultMessage> request, int errorCode, String errorMessage, Throwable e) {
                Log.d(TAG, "실패 : " + errorMessage);

            }
        });
    }

    private void initHeader(View v, Board board) {
        profileImage = (ImageView) v.findViewById(R.id.image_profile);
        nickNameText = (TextView) v.findViewById(R.id.text_nickname);
        postDateText = (TextView) v.findViewById(R.id.text_post_date);
        firstImage = (ImageView) v.findViewById(R.id.image_post_first_image);
        secondImage = (ImageView) v.findViewById(R.id.image_post_second_image);
        thirdImage = (ImageView) v.findViewById(R.id.image_post_third_image);
        contentText = (TextView) v.findViewById(R.id.text_post_content);

        images[0] = firstImage;
        images[1] = secondImage;
        images[2] = thirdImage;

        setHeaderView(board);

    }

    private void setHeaderView(Board board) {
        nickNameText.setText(board.getUserNick());
        postDateText.setText(StringUtil.calculateDate(board.getBoardRegDate()));
        imageRequest(board);
        contentText.setText(board.getBoardContent());
    }

    private void imageRequest(Board board) {
        ListFileRequest request = new ListFileRequest(this, String.valueOf(board.getBoardNum()));
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ListData<FileData>>() {
            @Override
            public void onSuccess(NetworkRequest<ListData<FileData>> request, ListData<FileData> result) {

                Log.d(TAG, "성공 : " + result.getMessage());
                insertImage(result.getData());

            }

            @Override
            public void onFail(NetworkRequest<ListData<FileData>> request, int errorCode, String errorMessage, Throwable e) {
                Log.d(TAG, "실패 : " + errorMessage);
            }
        });
    }

    private void insertImage(List<FileData> list) {

        ArrayList<String> imageCount = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getFileType().equals("image/jpg") && list.get(i).getFileUrl() != null) {
                imageCount.add(list.get(i).getFileUrl());
                Log.d(TAG, "파일 : " + list.get(i).getFileUrl());
            }
        }
        for (int j = 0; j < imageCount.size(); j++) {
            images[j].setVisibility(View.VISIBLE);
            Glide.with(this).load(imageCount.get(j)).into(images[j]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (PropertyManager.getInstance().getUserNum().equals(String.valueOf(board.getUserNum()))) {
            getMenuInflater().inflate(R.menu.activity_ud_menu, menu);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_update) {

            Intent intent = new Intent(DetailBoardActivity.this, UpdateBoardActivity.class);
            intent.putExtra("board", board);
            startActivity(intent);


        } else if (id == R.id.action_delete) {
            String boardNum = String.valueOf(board.getBoardNum());
            DeleteBoardRequest request = new DeleteBoardRequest(this, boardNum);
            NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ResultMessage>() {
                @Override
                public void onSuccess(NetworkRequest<ResultMessage> request, ResultMessage result) {
                    Log.d(TAG, "성공 : " + result.getMessage());
                }

                @Override
                public void onFail(NetworkRequest<ResultMessage> request, int errorCode, String errorMessage, Throwable e) {
                    Log.d(TAG, "실패 : " + errorMessage);

                }
            });
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {

        listReplyRequest();

    }

    public void listReplyRequest() {

        String boardNum = String.valueOf(board.getBoardNum());
        String pageNum = "";
        String lastReplyNum = "";
        ListReplyRequest request = new ListReplyRequest(this, boardNum, pageNum, lastReplyNum);
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ListData<Reply>>() {
            @Override
            public void onSuccess(NetworkRequest<ListData<Reply>> request, ListData<Reply> result) {

                Log.d("DetailBoardActivity", "성공 : " + result.getMessage());
                mAdapter.addAll(result.getData());

            }

            @Override
            public void onFail(NetworkRequest<ListData<Reply>> request, int errorCode, String errorMessage, Throwable e) {
                Log.d("DetailBoardActivity", "실패 : " + errorMessage);

            }
        });
    }
}
