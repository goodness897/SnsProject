package com.mu.compet;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mu.compet.data.Reply;
import com.mu.compet.data.ReplyView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mu on 2016-10-20.
 */
public class ReplyAdapter extends BaseAdapter implements ReplyView.OnReplySuccessListener {

    private List<Reply> items = new ArrayList<>();
    private String boardNum;

    public void add(Reply reply) {
        items.add(reply);
        notifyDataSetChanged();
    }

    public void addItem(List<Reply> replyList) {

        items.addAll(replyList);
        notifyDataSetChanged();
    }


    public void addAll(List<Reply> replyList) {
        if (!items.isEmpty()) items.clear();
        items.addAll(replyList);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public ReplyAdapter(String boardNum) {
        this.boardNum = boardNum;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReplyView view;
        if (convertView == null) {
            view = new ReplyView(parent.getContext(), boardNum, items.get(position));
            view.setOnReplySuccessListener(this);
        } else {
            view = (ReplyView)convertView;
        }

        if (items.size() > 0) {
            view.setReplyTextView(items.get(position));
        }
        return view;
    }


    @Override
    public void onReplySuccess(int code) {
        if(mListener != null) {
            mListener.onAdapterReplyClick(code);
        }

    }

    public interface OnAdapterReplyClickListener {
        public void onAdapterReplyClick(int code);
    }

    OnAdapterReplyClickListener mListener;

    public void setOnAdapterReplyListener(OnAdapterReplyClickListener listener) {
        mListener = listener;


    }
}
