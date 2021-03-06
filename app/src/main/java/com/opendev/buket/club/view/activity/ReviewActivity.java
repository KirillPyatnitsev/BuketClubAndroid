package com.opendev.buket.club.view.activity;

import android.content.Intent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import com.opendev.buket.club.DataController;
import com.opendev.buket.club.R;
import com.opendev.buket.club.consts.ServerConfig;
import com.opendev.buket.club.model.Order;
import com.opendev.buket.club.web.WebMethods;
import com.opendev.buket.club.web.response.DefaultResponse;

public class ReviewActivity extends BaseActivity {

    private static final String TAG = ServerConfig.TAG_PREFIX + "ReviewActivity";

    private RatingBar ratingBar;
    //private ImageView imageArtistIcon;
    private EditText editComment;
    private Button buttonSendReview;
    private ImageView imageBack;
    private View commentContainer;

    private Order order;

    @Override
    protected void onCreateInternal() {
        setContentView(R.layout.activity_review);

        order = DataController.getInstance().getOrder();

        assignView();
        assignListener();
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_r_coordinator_root;
    }

    private void assignView() {
        ratingBar = getViewById(R.id.a_r_rating_bar);
        //imageArtistIcon = getViewById(R.id.a_r_image_artist_icon);
        editComment = getViewById(R.id.a_r_edit_comment);
        commentContainer = getViewById(R.id.a_r_comment_container);
        buttonSendReview = getViewById(R.id.a_r_button_send_review);
        imageBack = getViewById(R.id.i_ab_image_back);
        imageBack.setVisibility(View.VISIBLE);

        editComment.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }

    private void assignListener() {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                boolean wantComment = rating < 4;
                editComment.setEnabled(wantComment);
                commentContainer.setVisibility(wantComment ? View.VISIBLE : View.GONE);

            }
        });
        buttonSendReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReview();
            }
        });
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void sendReview() {
        String review = editComment.getText().toString();
        int rating = (int) ratingBar.getRating();
        sendReview(review, rating);
    }

    private void sendReview(String comment, int rating) {
        startLoading();
        final Order orderToSend = this.order;
        WebMethods.getInstance().sendReviewRequest(order.getId(), comment, rating,
                new RequestListener<DefaultResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        stopLoading();
                        goToOrderDetails();
                    }

                    @Override
                    public void onRequestSuccess(DefaultResponse defaultResponse) {
                        stopLoading();
                        // Update order to reflect changes in Details view.
                        orderToSend.setStatusIndex(Order.STATUS_DONE_INDEX);
                        DataController.getInstance().setOrder(orderToSend);
                        goToOrderDetails();
                    }
                });
    }

    private void goToOrderDetails() {
        startActivity(new Intent(this, OrderDetailsActivity.class));
    }
}
