package com.mu.compet.data;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mu.compet.R;
import com.mu.compet.request.AbstractRequest;
import com.mu.compet.util.StringUtil;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Mu on 2016-11-03.
 */

public class BoardView extends FrameLayout {

    private LinearLayout linearLayout;
    private RelativeLayout relativeLayout;
    private ImageView profileImageView;
    private TextView nickNameTextView;
    private TextView dateView;
    private ImageView postImageView;
    private TextView imageCountView;
    private TextView postContentView;
    private ImageView replyImageView;
    private TextView replyCountView;
    private int position;

    private Board board;
    private Context context;

    public BoardView(Context context) {
        super(context);
        this.context = context;

        View view = inflate(getContext(), R.layout.view_post, this);
        initUserView(view);
        initView(view);
    }

    private void initUserView(View view) {
        linearLayout = (LinearLayout) view.findViewById(R.id.user_info_layout);
        profileImageView = (ImageView) view.findViewById(R.id.image_profile);
        nickNameTextView = (TextView) view.findViewById(R.id.text_nickname);
        dateView = (TextView) view.findViewById(R.id.text_post_date);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onUserClick(BoardView.this, board);
                }

            }
        });

    }

    public interface OnUserClickListener {
        public void onUserClick(BoardView view, Board board);
    }

    OnUserClickListener mListener;

    public void setOnUserClickListener(OnUserClickListener listener) {
        mListener = listener;
    }


    private void initView(View view) {

        relativeLayout = (RelativeLayout) view.findViewById(R.id.post_info_layout);
        postImageView = (ImageView) view.findViewById(R.id.image_post_first_image);
        imageCountView = (TextView) view.findViewById(R.id.text_image_count);
        postContentView = (TextView) view.findViewById(R.id.text_post_content);
        replyImageView = (ImageView) view.findViewById(R.id.image_comment);
        replyCountView = (TextView) view.findViewById(R.id.text_comment_count);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pListener != null) {
                    pListener.onPostClick(BoardView.this, board);
                }
            }
        });

    }

    public void setUserBoardView(Board board) {

        String imgPath = "http://" + AbstractRequest.getHOST() + ":"
                + AbstractRequest.getHttpPort() + "/user/" + board.getUserNum() + "/image";

        this.board = board;
        linearLayout.setVisibility(View.VISIBLE);

        Glide.with(context).load(imgPath).placeholder(R.drawable.image_default_profile).error(R.drawable.image_default_profile)
                .bitmapTransform(new CropCircleTransformation(context)).into(profileImageView);



        nickNameTextView.setText(board.getUserNick());
        dateView.setText(StringUtil.calculateDate(board.getBoardRegDate()));
    }


    public void setBoardView(Board board) {

        this.board = board;
        imageCountView.setText("+" + board.getBoardImgCnt());
        replyCountView.setText(String.valueOf(board.getBoardReplyCnt()));
        postContentView.setText(board.getBoardContent());

        if (!TextUtils.isEmpty(board.getFirstImageUrl())) {
            postImageView.setVisibility(View.VISIBLE);
            imageCountView.setVisibility(View.VISIBLE);
            Glide.with(context).load(board.getFirstImageUrl()).into(postImageView);
        } else {
            postImageView.setVisibility(View.GONE);
            imageCountView.setVisibility(View.GONE);

        }
    }

    public interface OnPostClickListener {
        public void onPostClick(BoardView view, Board board);
    }

    OnPostClickListener pListener;

    public void setOnPostClickListener(OnPostClickListener listener) {
        pListener = listener;
    }
}
