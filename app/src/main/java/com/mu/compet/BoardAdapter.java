package com.mu.compet;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mu.compet.data.Board;
import com.mu.compet.data.BoardView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mu on 2016-10-20.
 */
public class BoardAdapter extends BaseAdapter implements BoardView.OnPostClickListener, BoardView.OnUserClickListener {

    protected List<Board> items = new ArrayList<>();
    private int auth = 0;

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    public BoardAdapter(int auth) {
        this.auth = auth;
    }

    public void add(Board board) {
        items.add(board);
        notifyDataSetChanged();
    }

    public void addAll(List<Board> items) {
        if (!this.items.isEmpty()) this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
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
        BoardView view;
        if (convertView == null) {
            view = new BoardView(parent.getContext());
            view.setOnPostClickListener(this);
            if(getAuth() != 1) {
                view.setOnUserClickListener(this);
            }
        } else {
            view = (BoardView) convertView;
        }
        if (items.size() > 0) {
            view.setBoardView(items.get(position));
            if(getAuth() != 1) {
                view.setUserBoardView(items.get(position));
            }

        }

        return view;
    }


    public interface OnAdapterUserClickListener {
        public void onAdapterUserClick(BoardAdapter adapter, BoardView view, Board board);
    }

    public interface OnAdapterPostClickListener {
        public void onAdapterPostClick(BoardAdapter adapter, BoardView view, Board board);
    }

    OnAdapterUserClickListener mUserAdapterListener;
    OnAdapterPostClickListener mPostAdapterListener;

    public void setOnAdapterUserClickListener(OnAdapterUserClickListener listener) {
        mUserAdapterListener = listener;
    }

    public void setOnAdapterPostClickListener(OnAdapterPostClickListener listener) {
        mPostAdapterListener = listener;
    }

    @Override
    public void onUserClick(BoardView view, Board board) {
        if (mUserAdapterListener != null) {
            mUserAdapterListener.onAdapterUserClick(this, view, board);
        }
    }

    @Override
    public void onPostClick(BoardView view, Board board) {
        if (mPostAdapterListener != null) {
            mPostAdapterListener.onAdapterPostClick(this, view, board);
        }

    }

}
