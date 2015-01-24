package com.mairos.twisterblog.gui.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mairos.twisterblog.R;
import com.mairos.twisterblog.model.Comment;

import java.util.List;

/**
 * Created by Mike on 24.01.2015.
 */
public class CommentsAdapter extends BaseAdapter {
    private List<Comment> mComments;
    private Activity mActivity;

    public CommentsAdapter(List<Comment> comments, Activity activity) {
        mActivity = activity;
        mComments = comments;
    }

    public int getCount() {
        return mComments.size();
    }

    public Object getItem(int position) {
        return mComments.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder holder = null;
        final Comment item = (Comment) getItem(position);

        if (rowView == null) {
            rowView = mActivity.getLayoutInflater().inflate(R.layout.list_item_post, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) rowView.findViewById(R.id.text_title);
            holder.date = (TextView) rowView.findViewById(R.id.text_date);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.title.setText(item.body);
        holder.date.setText(item.created_at);

        return rowView;
    }

    private class ViewHolder {
        public TextView title;
        public TextView date;
    }
}