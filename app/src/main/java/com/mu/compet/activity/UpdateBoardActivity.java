package com.mu.compet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.mu.compet.R;
import com.mu.compet.data.Board;
import com.mu.compet.data.ResultMessage;
import com.mu.compet.manager.NetworkManager;
import com.mu.compet.manager.NetworkRequest;
import com.mu.compet.request.UpdateBoardRequest;

import java.io.File;

public class UpdateBoardActivity extends BaseActivity {

    private String TAG = "UpdateBoardActivity";

    private EditText contentEdit;
    private Board board;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_write);
        initToolBar(getString(R.string.activity_update_post));
        Intent intent = getIntent();
        board = (Board) intent.getSerializableExtra("board");
        contentEdit = (EditText)findViewById(R.id.edit_content);
        contentEdit.setText(board.getBoardContent());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_update_my_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String boardNum = String.valueOf(board.getBoardNum());
        String boardContent = board.getBoardContent();
        File[] files = null;
        File[] delFiles = null;
        int id = item.getItemId();
        if (id == R.id.action_complete) {
            UpdateBoardRequest request = new UpdateBoardRequest(this, boardNum, boardContent, files, delFiles);
            NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ResultMessage<String>>() {
                @Override
                public void onSuccess(NetworkRequest<ResultMessage<String>> request, ResultMessage<String> result) {
                    Log.d(TAG, result.getMessage());
                    finish();
                }

                @Override
                public void onFail(NetworkRequest<ResultMessage<String>> request, int errorCode, String errorMessage, Throwable e) {
                    Log.d(TAG, errorMessage);

                }
            });

        }

        return super.onOptionsItemSelected(item);
    }
}
