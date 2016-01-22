package ru.creators.buket.club.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.elirex.fayeclient.FayeClient;
import com.elirex.fayeclient.FayeClientListener;
import com.elirex.fayeclient.MetaMessage;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import ru.creators.buket.club.DataController;
import ru.creators.buket.club.R;
import ru.creators.buket.club.consts.ServerConfig;
import ru.creators.buket.club.model.Order;
import ru.creators.buket.club.model.lists.ListAnswerFlex;
import ru.creators.buket.club.tools.Helper;
import ru.creators.buket.club.view.adapters.ListAnswerFlexAdapter;
import ru.creators.buket.club.web.WebMethods;
import ru.creators.buket.club.web.response.ListAnswerFlexResponse;

public class ChoseShopActivity extends BaseActivity {

    private ListAnswerFlex listAnswerFlex;
    private ListAnswerFlexAdapter listAnswerFlexAdapter;

    private ListView listView;

    private ImageView imageBack;
    private ImageView imageBouquet;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Order order = DataController.getInstance().getOrder();

    private FayeClient fayeClientOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_shop);
        assignView();
        assignListener();
        initView();
        updateArtistsList();

        initFaye();
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_cs_coordinator_root;
    }

    private void assignView(){
        imageBouquet = getViewById(R.id.a_cs_image_bouquet);
        imageBack = getViewById(R.id.i_ab_image_back);
        listView = getViewById(R.id.a_cs_list_view_artists);

        swipeRefreshLayout = getViewById(R.id.a_cs_swipe_refresh);
    }

    private void assignListener(){
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                choseShop(position);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateArtistsList();
            }
        });
    }

    private void initView(){
        imageBack.setVisibility(View.VISIBLE);

        WebMethods.getInstance().loadImage(this, Helper.addServerPrefix(order.getBouquetItem().getImageUrl()), imageBouquet);

        listAnswerFlex = new ListAnswerFlex();
        listAnswerFlexAdapter = new ListAnswerFlexAdapter(this, listAnswerFlex);
        listView.setAdapter(listAnswerFlexAdapter);
    }

    private void initFaye(){
        MetaMessage metaMessageFixOrder = new MetaMessage();
        fayeClientOrder = new FayeClient(ServerConfig.SERVER_FAYE, metaMessageFixOrder);
        fayeClientOrder.setListener(new FayeClientListener() {
            @Override
            public void onConnectedServer(FayeClient fc) {
                fc.subscribeChannel(ServerConfig.SERVER_FAYE_ORDER + Integer.toString(order.getId()));
            }

            @Override
            public void onDisconnectedServer(FayeClient fc) {

            }

            @Override
            public void onReceivedMessage(FayeClient fc, String msg) {
                updateArtistsList();
            }
        });
    }

    private void choseShop(int shopPosition){
        DataController.getInstance().getOrder().setShopId(listAnswerFlex.get(shopPosition).getShop().getId());
        goToPaymentActivity();
    }

    private void goToPaymentActivity() {
        startActivity(new Intent(this, PaymentTypeActivity.class));
    }


    private void updateArtistsList(){
        if (!swipeRefreshLayout.isRefreshing())
            startLoading(false);
        WebMethods.getInstance().getFlexAnswers(
                DataController.getInstance().getSession().getAccessToken(), order.getId(), new RequestListener<ListAnswerFlexResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        if (!swipeRefreshLayout.isRefreshing())
                            stopLoading();
                        else
                            swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onRequestSuccess(ListAnswerFlexResponse listAnswerFlexResponse) {
                        if (!swipeRefreshLayout.isRefreshing())
                            stopLoading();
                        else
                            swipeRefreshLayout.setRefreshing(false);
                        listAnswerFlex.clear();
                        listAnswerFlex.addAll(listAnswerFlexResponse.getListAnswerFlex());
                        listAnswerFlexAdapter.notifyDataSetChanged();
                    }
                });
    }
}
