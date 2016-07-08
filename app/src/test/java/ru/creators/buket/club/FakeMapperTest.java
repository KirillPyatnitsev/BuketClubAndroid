package ru.creators.buket.club;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import junit.framework.Assert;

import org.junit.Test;

import ru.creators.buket.club.web.FakeWebMethods;
import ru.creators.buket.club.web.response.ShopListResponse;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class FakeMapperTest {

    private ShopListResponse shops;

    @Test
    public void testShouldLoadResource() {
        String str = FakeWebMethods.loadResource("/assets/OrderResponse.txt");
        Assert.assertTrue(str.startsWith("{"));
    }

    @Test
    public void testShouldMakeTestResponses() throws Exception {
        FakeWebMethods fakeMethods = new FakeWebMethods();
        fakeMethods.setDelay(0);

        shops = null;

        fakeMethods.listShopGetRequest("",
                1, 25, new RequestListener<ShopListResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {

                    }

                    @Override
                    public void onRequestSuccess(ShopListResponse shopListResponse) {
                        shops = shopListResponse;
                    }
                });

        Assert.assertNotNull(shops);
        Assert.assertTrue(!shops.getListShop().isEmpty());
    }
}