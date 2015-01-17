package com.mairos.twisterblog;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mairos.twisterblog.model.Post;
import com.mairos.twisterblog.network.PostsRequest;
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

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_posts_list)
@OptionsMenu({R.menu.menu_posts_list})
public class PostsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    interface Callback {
        void onPostSelect(Post post);
    }

    private SpiceManager spiceManager = new SpiceManager(TwisterBlogService.class);
    private PostsRequest postsRequest;

    @ViewById(R.id.swipe_container)
    SwipeRefreshLayout updater;

    @ViewById(R.id.list_posts)
    protected ListView mListPosts;

    public PostsListFragment() {
        // Required empty public constructor
    }

    public static PostsListFragment newInstance() {
        return PostsListFragment_.builder().build();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postsRequest = new PostsRequest();
    }

    @Override
    public void onStart() {
        super.onStart();
        spiceManager.start(getActivity());
        getSpiceManager().execute(postsRequest, "twister_posts", DurationInMillis.ONE_SECOND, new ListPostsRequestListener());
    }

    @Override
    public void onStop() {
        super.onStop();
        spiceManager.shouldStop();
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
    }

    protected SpiceManager getSpiceManager() {
        return spiceManager;
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
        mListPosts.setAdapter(new PostsAdapter(posts));
    }

    @Override
    public void onRefresh() {
        getSpiceManager().execute(postsRequest, "twister_posts", DurationInMillis.ONE_SECOND, new ListPostsRequestListener());
    }

    public final class ListPostsRequestListener implements RequestListener<Post.List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getActivity(), "failure update posts", Toast.LENGTH_SHORT).show();
            updater.setRefreshing(false);
        }

        @Override
        public void onRequestSuccess(final Post.List result) {
            Toast.makeText(getActivity(), "success update posts", Toast.LENGTH_SHORT).show();
            updater.setRefreshing(false);
            updateList(result);
        }
    }

    private class PostsAdapter extends BaseAdapter {
        private List<Post> mPosts;

        public PostsAdapter(List<Post> posts) {
            mPosts = posts;
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

        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            ViewHolder holder = null;
            final Post item = (Post) getItem(position);

            if (rowView == null) {
                rowView = getActivity().getLayoutInflater().inflate(R.layout.list_item_post, parent, false);
                holder = new ViewHolder();
                holder.title = (TextView) rowView.findViewById(R.id.textPostTitle);
                rowView.setTag(holder);
            } else {
                holder = (ViewHolder) rowView.getTag();
            }

            holder.title.setText(item.title);

            return rowView;
        }

        private class ViewHolder {
            public TextView title;
        }
    }
}
