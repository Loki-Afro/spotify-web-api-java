package com.wrapper.spotify.requests;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.TestUtil;
import com.wrapper.spotify.model_objects.ProductType;
import com.wrapper.spotify.objects.model_objects.User;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class CurrentUserRequestTest {

  @Test
  public void shouldGetCurrentUser_async() throws Exception {
    final Api api = Api.DEFAULT_API;

    final CurrentUserRequest request = api.getMe()
            .accessToken("myLongAccessToken")
            .setHttpManager(TestUtil.MockedHttpManager.returningJson("current-user.json"))
            .build();

    final CountDownLatch asyncCompleted = new CountDownLatch(1);

    final SettableFuture<User> userFuture = request.getAsync();

    Futures.addCallback(userFuture, new FutureCallback<User>() {
      @Override
      public void onSuccess(User user) {
        assertNotNull(user);
        assertEquals("Michael", user.getDisplayName());
        assertEquals("thelinmichael+test@gmail.com", user.getEmail());
        assertEquals("https://open.spotify.com/user/thelinmichael", user.getExternalUrls().get(
                "spotify"));
        assertEquals("https://api.spotify.com/v1/users/thelinmichael", user.getHref());
        assertEquals("thelinmichael", user.getId());
        assertEquals(CountryCode.SE, user.getCountry());
        assertNotNull(user.getFollowers());
        assertEquals(0, user.getImages()[0].getHeight());
        assertEquals(0, user.getImages()[0].getWidth());
        assertEquals("http://media.giphy.com/media/Aab07O5PYOmQ/giphy.gif", user.getImages()[0].getUrl());
        assertEquals(ProductType.PREMIUM, user.getProduct());
        assertEquals("spotify:user:thelinmichael", user.getUri());
        assertEquals("1989-07-04", user.getBirthdate());

        asyncCompleted.countDown();
      }

      @Override
      public void onFailure(Throwable throwable) {
        fail("Failed to resolve future: " + throwable.getMessage());
      }
    });

    asyncCompleted.await(1, TimeUnit.SECONDS);
  }

  @Test
  public void shouldGetCurrentUser_sync() throws Exception {
    final Api api = Api.DEFAULT_API;

    final CurrentUserRequest request = api.getMe()
            .accessToken("myLongAccessToken")
            .setHttpManager(TestUtil.MockedHttpManager.returningJson("current-user.json"))
            .build();

    final User user = request.get();

    assertNotNull(user);
    assertEquals("Michael", user.getDisplayName());
    assertEquals("thelinmichael+test@gmail.com", user.getEmail());
    assertEquals("https://open.spotify.com/user/thelinmichael", user.getExternalUrls().get(
            "spotify"));
    assertEquals("https://api.spotify.com/v1/users/thelinmichael", user.getHref());
    assertEquals("thelinmichael", user.getId());
    assertEquals(CountryCode.SE, user.getCountry());
    assertNotNull(user.getFollowers());
    assertEquals(0, user.getImages()[0].getHeight());
    assertEquals(0, user.getImages()[0].getWidth());
    assertEquals("http://media.giphy.com/media/Aab07O5PYOmQ/giphy.gif", user.getImages()[0].getUrl());
    assertEquals(ProductType.PREMIUM, user.getProduct());
    assertEquals("spotify:user:thelinmichael", user.getUri());
    assertEquals("1989-07-04", user.getBirthdate());
  }
}