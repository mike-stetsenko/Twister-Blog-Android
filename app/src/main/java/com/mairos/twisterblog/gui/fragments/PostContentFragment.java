package com.mairos.twisterblog.gui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mairos.twisterblog.R;
import com.mairos.twisterblog.gui.adapters.CommentsAdapter;
import com.mairos.twisterblog.model.Comment;
import com.mairos.twisterblog.model.Post;
import com.mairos.twisterblog.network.CommentsRequest;
import com.mairos.twisterblog.network.TwisterBlogService;
import com.mairos.twisterblog.storage.Storage;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EFragment(R.layout.fragment_post_content)
@OptionsMenu({R.menu.menu_post_content})
public class PostContentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @InstanceState
    @FragmentArg
    protected Post mPostArg;

    @ViewById(R.id.post_content)
    protected TextView mPostContent;

    @ViewById(R.id.swipe_container)
    SwipeRefreshLayout updater;

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
        // restore data from local database
        List<Comment> comments = Storage.get().getCommentsByPost(mPostArg.id);
        if (comments.size() > 0) updateList(comments);

        mPostContent.setText(mPostArg.body);
        // try to get the actual one
        commentsRequest = new CommentsRequest(mPostArg.id);
        getSpiceManager().execute(commentsRequest, "twister_comments", DurationInMillis.ONE_SECOND, new ListCommentsRequestListener());
    }

    @AfterViews
    protected void initGUI(){
        updater.setOnRefreshListener(this);
        updater.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @UiThread
    void updateList(List<Comment> comments) {
        mListComments.setAdapter(new CommentsAdapter(comments, getActivity()));
    }

    @OptionsItem(R.id.action_add)
    void addPost() {
        AddCommentDialogFragment dialog = AddCommentDialogFragment.newInstance(mPostArg);
        dialog.setTargetFragment(PostContentFragment.this, 0);
        dialog.show(getFragmentManager(), "dialog");
    }

    protected SpiceManager getSpiceManager() {
        return spiceManager;
    }

    @Override
    public void onRefresh() {
        commentsRequest = new CommentsRequest(mPostArg.id);
        getSpiceManager().execute(commentsRequest, "twister_comments", DurationInMillis.ONE_SECOND, new ListCommentsRequestListener());
    }

    public final class ListCommentsRequestListener implements RequestListener<Comment.List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getActivity(), "failure comments update", Toast.LENGTH_SHORT).show();
            updater.setRefreshing(false);
        }

        @Override
        public void onRequestSuccess(final Comment.List result) {
            if (result != null) {
                Toast.makeText(getActivity(), "success comments update : " + result.size(), Toast.LENGTH_SHORT).show();
                updateList(result);
            } else Toast.makeText(getActivity(), "no comments to this post", Toast.LENGTH_SHORT).show();
            updater.setRefreshing(false);
        }
    }
}
