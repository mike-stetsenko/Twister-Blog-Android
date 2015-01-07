package com.mairos.twisterblog;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mairos.twisterblog.model.Comment;
import com.mairos.twisterblog.model.Post;
import com.mairos.twisterblog.network.CommentsRequest;
import com.mairos.twisterblog.network.TwisterBlogService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EFragment(R.layout.fragment_post_content)
public class PostContentFragment extends Fragment {

    @InstanceState
    @FragmentArg
    protected Post mPostArg;

    @ViewById(R.id.post_content)
    protected TextView mPostContent;

    @ViewById(R.id.list_comments)
    protected ListView mListComments;

    private SpiceManager spiceManager = new SpiceManager(TwisterBlogService.class);
    private CommentsRequest commentsRequest;

    public static PostContentFragment newInstance(Post post) {
        return PostContentFragment_.builder()
                .mPostArg(post)
                .build();
    }

    public PostContentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        spiceManager.start(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
        spiceManager.shouldStop();
    }

    @AfterViews
    protected void init(){
        mPostContent.setText(mPostArg.body);
        commentsRequest = new CommentsRequest(mPostArg.id);
        getSpiceManager().execute(commentsRequest, "twister_comments", DurationInMillis.ONE_SECOND, new ListCommentsRequestListener());
    }

    @UiThread
    void updateList(Comment.List comments) {
        mListComments.setAdapter(new CommentsAdapter(comments));
    }

    protected SpiceManager getSpiceManager() {
        return spiceManager;
    }

    public final class ListCommentsRequestListener implements RequestListener<Comment.List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getActivity(), "failure comments", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final Comment.List result) {
            if (result != null) {
                Toast.makeText(getActivity(), "success comments " + result.size(), Toast.LENGTH_SHORT).show();
                updateList(result);
            } else Toast.makeText(getActivity(), "no comments to this post", Toast.LENGTH_SHORT).show();
        }
    }

    private class CommentsAdapter extends BaseAdapter {
        private List<Comment> mComments;

        public CommentsAdapter(List<Comment> comments) {
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
                rowView = getActivity().getLayoutInflater().inflate(R.layout.list_item_post, parent, false);
                holder = new ViewHolder();
                holder.title = (TextView) rowView.findViewById(R.id.textPostTitle);
                rowView.setTag(holder);
            } else {
                holder = (ViewHolder) rowView.getTag();
            }

            holder.title.setText(item.body);

            return rowView;
        }

        private class ViewHolder {
            public TextView title;
        }
    }
}
