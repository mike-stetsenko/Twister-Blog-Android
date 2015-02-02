package com.mairos.twisterblog.gui.fragments;

import android.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.mairos.twisterblog.R;
import com.mairos.twisterblog.model.RequestResult;
import com.mairos.twisterblog.network.AddPostRequest;
import com.mairos.twisterblog.network.RequestStatusObject;
import com.mairos.twisterblog.network.TwisterBlogService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_add_post_dialog)
public class AddPostDialogFragment extends DialogFragment {

    private AddPostRequest mAddPostRequest;

    private SpiceManager mSpiceManager = new SpiceManager(TwisterBlogService.class);
    protected SpiceManager getSpiceManager() {
        return mSpiceManager;
    }

    @ViewById(R.id.text_title)
    protected EditText mPostTitle;
    @ViewById(R.id.text_body)
    protected EditText mPostBody;

    public AddPostDialogFragment() {
        // Required empty public constructor
    }

    public static AddPostDialogFragment newInstance() {
        return AddPostDialogFragment_.builder().build();
    }

    @Override
    public void onStart() {
        super.onStart();
        mSpiceManager.start(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
        mSpiceManager.shouldStop();
    }

    @AfterViews
    protected void init(){
        getDialog().setTitle("Add new post to blog");
    }

    @Click(R.id.button_add)
    protected void onAddClick(){
        mAddPostRequest = new AddPostRequest(mPostTitle.getText().toString(), mPostBody.getText().toString());
        Log.d("unit_tests", "AddPostDialog execute request");
        if (getTargetFragment() instanceof PostsListFragment){
            RequestStatusObject.getInstance().setStarted();
        }
        getSpiceManager().execute(mAddPostRequest, "add_post", DurationInMillis.ONE_SECOND, new AddPostRequestListener());
    }

    public final class AddPostRequestListener implements RequestListener<RequestResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getActivity(), "failure add post", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final RequestResult result) {
            Toast.makeText(getActivity(), "success add post", Toast.LENGTH_SHORT).show();
            if (getTargetFragment() instanceof SwipeRefreshLayout.OnRefreshListener){
                ((SwipeRefreshLayout.OnRefreshListener) getTargetFragment()).onRefresh();
            }
            Log.d("unit_tests","AddPostDialog before dismiss");
            dismiss();
        }
    }
}
