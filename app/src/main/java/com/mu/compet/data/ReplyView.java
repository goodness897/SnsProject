package com.mu.compet.data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.mu.compet.R;
import com.mu.compet.manager.NetworkManager;
import com.mu.compet.manager.NetworkRequest;
import com.mu.compet.manager.PropertyManager;
import com.mu.compet.request.DeleteReplyRequest;
import com.mu.compet.request.UpdateReplyRequest;
import com.mu.compet.util.StringUtil;

/**
 * Created by Mu on 2016-11-11.
 */

public class ReplyView extends FrameLayout {

    private Reply reply;

    private ImageView profileImageView;
    private TextView nickNameTextView;
    private TextView dateView;
    private TextView replyTextView;
    private EditText editReplyView;
    private TextView updateView;


    private ViewSwitcher viewSwitcher;
    private ViewSwitcher viewUpdateSwitcher;
    private String boardNum;


    public ReplyView(Context context, String boardNum, Reply reply) {
        super(context);
        this.reply = reply;
        this.boardNum = boardNum;
        View view = inflate(getContext(), R.layout.view_reply, this);
        initView(view);
    }

    public ReplyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    private void initView(View v) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

        viewSwitcher = (ViewSwitcher) v.findViewById(R.id.view_switcher);
        viewUpdateSwitcher = (ViewSwitcher) v.findViewById(R.id.view_update_switcher);
        updateView = (TextView) v.findViewById(R.id.text_update);
        profileImageView = (ImageView) v.findViewById(R.id.image_profile);
        nickNameTextView = (TextView) v.findViewById(R.id.text_nickname);
        dateView = (TextView) v.findViewById(R.id.text_time);
        replyTextView = (TextView) v.findViewById(R.id.text_comment_content);
        editReplyView = (EditText) v.findViewById(R.id.edit_comment_content);
        ImageButton deleteButton = (ImageButton) v.findViewById(R.id.btn_delete_reply);
        ImageButton updateButton = (ImageButton) v.findViewById(R.id.btn_update_reply);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                builder.setMessage("삭제하시겠습니까?")
                        .setCancelable(true)
                        .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                performDelete(view, reply);

                            }
                        })
                        .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewSwitcher.showNext();
                viewUpdateSwitcher.showNext();
                String content = replyTextView.getText().toString();
                editReplyView.setText(content);

            }
        });

        updateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewUpdateSwitcher.showNext();
                viewSwitcher.showNext();
                String content = editReplyView.getText().toString();
                replyTextView.setText(content);
                UpdateReplyRequest request = new UpdateReplyRequest(view.getContext(), boardNum,
                        String.valueOf(reply.getReplyNum()), content);
                NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ResultMessage>() {
                    @Override
                    public void onSuccess(NetworkRequest<ResultMessage> request, ResultMessage result) {
                        Log.d("DetailBoardActivity", "성공 : " + result.getMessage());
                        mListener.onReplySuccess(result.getCode());

                    }

                    @Override
                    public void onFail(NetworkRequest<ResultMessage> request, int errorCode, String errorMessage, Throwable e) {
                        Log.d("DetailBoardActivity", "실패 : " + errorMessage);
                        mListener.onReplySuccess(errorCode);

                    }
                });
            }
        });

    }

    private void performDelete(View view, Reply reply) {
        String replyNum = String.valueOf(reply.getReplyNum());
        DeleteReplyRequest request = new DeleteReplyRequest(view.getContext(), boardNum, replyNum);
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ResultMessage>() {
            @Override
            public void onSuccess(NetworkRequest<ResultMessage> request, ResultMessage result) {

                Log.d("DetailBoardActivity", "성공 : " + result.getMessage());

                mListener.onReplySuccess(result.getCode());
            }

            @Override
            public void onFail(NetworkRequest<ResultMessage> request, int errorCode, String errorMessage, Throwable e) {

                Log.d("DetailBoardActivity", "실패 : " + errorMessage);
                mListener.onReplySuccess(errorCode);


            }
        });
    }

    public void setReplyTextView(Reply reply) {
//        profileImageView.setImageDrawable(rep());
        if (reply.getUserNick().equals(PropertyManager.getInstance().getUserNick())) {
            viewUpdateSwitcher.setVisibility(View.VISIBLE);
        } else {
            viewUpdateSwitcher.setVisibility(View.GONE);
        }
        nickNameTextView.setText(reply.getUserNick());
        dateView.setText(StringUtil.calculateDate(reply.getReplyRegDate()));
        replyTextView.setText(reply.getReplyContent());


    }

    public interface OnReplySuccessListener {
        public void onReplySuccess(int code);
    }

    OnReplySuccessListener mListener;

    public void setOnReplySuccessListener(OnReplySuccessListener listener) {
        mListener = listener;
    }
}
