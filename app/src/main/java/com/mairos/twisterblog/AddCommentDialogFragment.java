package com.mairos.twisterblog;

import android.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.EditText;
import android.widget.Toast;

import com.mairos.twisterblog.model.Post;
import com.mairos.twisterblog.model.RequestResult;
import com.mairos.twisterblog.network.AddCommentRequest;
import com.mairos.twisterblog.network.TwisterBlogService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_add_comment_dialog)
public class AddCommentDialogFragment extends DialogFragment {

    private AddCommentRequest addCommentRequest;

    @InstanceState
    @FragmentArg
    protected Post mPost;

    private SpiceManager spiceManager = new SpiceManager(TwisterBlogService.class);
    protected SpiceManager getSpiceManager() {
        return spiceManager;
    }

    @ViewById(R.id.comment_text)
    protected EditText mCommentBody;

    public AddCommentDialogFragment() {
        // Required empty public constructor
    }

    public static AddCommentDialogFragment newInstance(Post post) {
        return AddCommentDialogFragment_.builder()
                .mPost(post)
                .build();
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
        getDialog().setTitle("Add new comment to post");
    }

    @Click(R.id.button_add)
    protected void onAddClick(){
        addCommentRequest = new AddCommentRequest(mPost.id, mCommentBody.getText().toString());
        getSpiceManager().execute(addCommentRequest, "add_comment", DurationInMillis.ONE_SECOND, new AddCommentRequestListener());
    }

    public final class AddCommentRequestListener implements RequestListener<RequestResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getActivity(), "failure add comment", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final RequestResult result) {
            Toast.makeText(getActivity(), "success add comment", Toast.LENGTH_SHORT).show();
            if (getTargetFragment() instanceof SwipeRefreshLayout.OnRefreshListener){
                ((SwipeRefreshLayout.OnRefreshListener) getTargetFragment()).onRefresh();
            }
            dismiss();
        }
    }
}
