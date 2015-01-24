package com.mairos.twisterblog.gui.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mairos.twisterblog.R;
import com.mairos.twisterblog.model.Post;

import java.util.List;

/**
 * Created by Mike on 24.01.2015.
 */
public class PostsAdapter extends BaseAdapter {
    private List<Post> mPosts;
    private Activity mActivity;

    public PostsAdapter(List<Post> posts, Activity activity) {
        mPosts = posts;
        mActivity = activity;
    }

    public int getCount() {
        return mPosts.size();
    }

    public Object getItem(int position) {
        return mPosts.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void remove(int position) {
        mPosts.remove(position);
        notifyDataSetChanged();
    }

    public void insert(int position, Post message) {
        mPosts.add(position, (Post) message);
        notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder holder = null;
        final Post item = (Post) getItem(position);

        if (rowView == null) {
            rowView = mActivity.getLayoutInflater().inflate(R.layout.list_item_post, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) rowView.findViewById(R.id.text_title);
            holder.date = (TextView) rowView.findViewById(R.id.text_date);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.title.setText(item.title);
        holder.date.setText(item.created_at);

        return rowView;
    }

    private class ViewHolder {
        public TextView title;
        public TextView date;
    }
}
