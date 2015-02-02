package com.mairos.twisterblog.gui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

import com.mairos.twisterblog.R;
import com.mairos.twisterblog.gui.adapters.PostsAdapter;
import com.mairos.twisterblog.model.Post;
import com.mairos.twisterblog.model.RequestResult;
import com.mairos.twisterblog.network.DeletePostRequest;
import com.mairos.twisterblog.network.PostsRequest;
import com.mairos.twisterblog.network.RequestStatusObject;
import com.mairos.twisterblog.network.TwisterBlogService;
import com.mairos.twisterblog.storage.Storage;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import de.timroes.android.listview.EnhancedListView;

@EFragment(R.layout.fragment_posts_list)
@OptionsMenu({R.menu.menu_posts_list})
public class PostsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public interface Callback {
        void onPostSelect(Post post);
    }

    private SpiceManager mSpiceManager = new SpiceManager(TwisterBlogService.class);
    private PostsRequest mPostsRequest;
    private DeletePostRequest mDeletePostRequest;

    @ViewById(R.id.swipe_container)
    SwipeRefreshLayout updater;

    @ViewById(R.id.list_posts)
    protected EnhancedListView mListPosts;

    public PostsListFragment() {
        // Required empty public constructor
    }

    public static PostsListFragment newInstance() {
        return PostsListFragment_.builder().build();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPostsRequest = new PostsRequest();
    }

    @Override
    public void onStart() {
        super.onStart();
        mSpiceManager.start(getActivity());
        RequestStatusObject.getInstance().setStarted();
        getSpiceManager().execute(mPostsRequest, "twister_posts", DurationInMillis.ONE_SECOND, new ListPostsRequestListener());
    }

    @Override
    public void onStop() {
        super.onStop();
        mSpiceManager.shouldStop();
    }

    @AfterViews
    protected void initGUI(){
        List<Post> posts = Storage.get().getPosts();
        if (posts.size() > 0) updateList(posts);

        updater.setOnRefreshListener(this);
        updater.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mListPosts.setUndoStyle(EnhancedListView.UndoStyle.MULTILEVEL_POPUP);
        mListPosts.setRequireTouchBeforeDismiss(false);
        mListPosts.setUndoHideDelay(3000);
        mListPosts.setSwipeDirection(EnhancedListView.SwipeDirection.BOTH);
        mListPosts.setDismissCallback(dismissCalback);
        mListPosts.enableSwipeToDismiss();
    }

    private EnhancedListView.OnDismissCallback dismissCalback = new EnhancedListView.OnDismissCallback() {
        @Override
        public EnhancedListView.Undoable onDismiss(EnhancedListView listView, final int position) {
            final PostsAdapter adapter = (PostsAdapter) mListPosts.getAdapter();
            final Post post = (Post) adapter.getItem(position);

            if (post == null) return null;

            adapter.remove(position);
            Log.d("unit_tests", "PostsListFragment - swipe delete, remove item");

            return new EnhancedListView.Undoable() {
                @Override
                public void undo() {
                    adapter.insert(position, post);
                }
                @Override
                public String getTitle() {
                    return "post removed";
                }
                @Override
                public void discard() {
                    Log.d("unit_tests", "PostsListFragment - execute delete request");
                    mDeletePostRequest = new DeletePostRequest(post.id);
                    RequestStatusObject.getInstance().setStarted();
                    getSpiceManager().execute(mDeletePostRequest, post.id,
                            DurationInMillis.ALWAYS_EXPIRED, new DeleteRequestListener());
                }
            };
        }
    };

    protected SpiceManager getSpiceManager() {
        return mSpiceManager;
    }

    @ItemClick(R.id.list_posts)
    void postListItemClicked(Post selectedPost) {
        if (getActivity() instanceof Callback){
            ((Callback) getActivity()).onPostSelect(selectedPost);
        }
    }

    @OptionsItem(R.id.action_add)
    void addPost() {
        AddPostDialogFragment dialog = AddPostDialogFragment.newInstance();
        dialog.setTargetFragment(PostsListFragment.this, 0);
        dialog.show(getFragmentManager(), "dialog");
    }

    @UiThread
    void updateList(List<Post> posts) {
        mListPosts.setAdapter(new PostsAdapter(posts, getActivity()));
    }

    @Override
    public void onRefresh() {
        RequestStatusObject.getInstance().setStarted();
        getSpiceManager().execute(mPostsRequest, "twister_posts", DurationInMillis.ONE_SECOND, new ListPostsRequestListener());
    }

    public final class ListPostsRequestListener implements RequestListener<Post.List> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getActivity(), "failure update posts", Toast.LENGTH_SHORT).show();
            updater.setRefreshing(false);
            RequestStatusObject.getInstance().setFinished();
        }
        @Override
        public void onRequestSuccess(final Post.List result) {
            Toast.makeText(getActivity(), "success update posts", Toast.LENGTH_SHORT).show();
            updater.setRefreshing(false);
            Log.d("unit_tests", "PostsListFragment - result received");
            updateList(result);
            RequestStatusObject.getInstance().setFinished();
        }
    }

    public final class DeleteRequestListener implements RequestListener<RequestResult> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            RequestStatusObject.getInstance().setFinished();
        }
        @Override
        public void onRequestSuccess(final RequestResult result) {
            Toast.makeText(getActivity(), "posts success deleted", Toast.LENGTH_SHORT).show();
            RequestStatusObject.getInstance().setFinished();
        }
    }
}
