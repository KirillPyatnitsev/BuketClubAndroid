package ru.creators.buket.club.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.flurry.android.FlurryAgent;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import ru.creators.buket.club.DataController;
import ru.creators.buket.club.R;
import ru.creators.buket.club.model.Order;
import ru.creators.buket.club.web.WebMethods;
import ru.creators.buket.club.web.response.DefaultResponse;

public class ReviewActivity extends BaseActivity {

    RatingBar ratingBar;
    ImageView imageArtistIcon;
    EditText editComment;
    Button buttonSendReview;
    ImageView imageBack;

    Order order = DataController.getInstance().getOrder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        assignView();
        assignListener();

        FlurryAgent.logEvent("ReviewActivity");
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_r_coordinator_root;
    }

    private void assignView(){
        ratingBar = getViewById(R.id.a_r_rating_bar);
        imageArtistIcon = getViewById(R.id.a_r_image_artist_icon);
        editComment = getViewById(R.id.a_r_edit_comment);
        buttonSendReview = getViewById(R.id.a_r_button_send_review);
        imageBack = getViewById(R.id.i_ab_image_back);
        imageBack.setVisibility(View.VISIBLE);
    }

    private void assignListener(){
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating<4){
                    editComment.setEnabled(true);
                }else{
                    editComment.setEnabled(false);
                    editComment.setText("");
                }
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

    private void sendReview(){
        String review = editComment.getText().toString();
        int rating = (int)ratingBar.getRating();
        sendReview(review, rating);
    }

    private void sendReview(String comment, int rating){
        startLoading();
        WebMethods.getInstance().sendReviewRequest(DataController.getInstance().getSession().getAccessToken(),
                order.getId(), comment, rating,
                new RequestListener<DefaultResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        stopLoading();
                        goToOrderDetalis();
                    }

                    @Override
                    public void onRequestSuccess(DefaultResponse defaultResponse) {
                        stopLoading();
                        goToOrderDetalis();
                    }
                });
    }

    private void goToOrderDetalis(){
        startActivity(new Intent(this, OrderDetailsActivity.class));
    }
}
