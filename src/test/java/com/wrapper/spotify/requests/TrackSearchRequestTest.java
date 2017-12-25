package com.wrapper.spotify.requests;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.HttpManager;
import com.wrapper.spotify.TestUtil;
import com.wrapper.spotify.objects.model_objects.Paging;
import com.wrapper.spotify.objects.model_objects.Track;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class TrackSearchRequestTest {

  @Test
  public void shouldGetTracksResult_async() throws Exception {
    final Api api = Api.DEFAULT_API;

    final TrackSearchRequest request = api.searchTracks("tania bowra")
            .offset(0)
            .limit(20)
            .setHttpManager(TestUtil.MockedHttpManager.returningJson("search-track.json"))
            .build();

    final CountDownLatch asyncCompleted = new CountDownLatch(1);

    SettableFuture<Paging<Track>> searchResultFuture = request.getAsync();

    Futures.addCallback(searchResultFuture, new FutureCallback<Paging<Track>>() {
      @Override
      public void onSuccess(Paging<Track> trackSearchResult) {
        assertTrue(trackSearchResult.getTotal() > 0);
        assertEquals(20, trackSearchResult.getLimit());
        assertEquals(0, trackSearchResult.getOffset());

        Track[] tracks = trackSearchResult.getItems();

        Track firstTrack = tracks[0];
        assertNotNull(firstTrack.getId());

        String id = firstTrack.getId();
        assertNotNull(firstTrack.getAlbum());
        assertNotNull(firstTrack.getArtists());
        assertEquals("https://api.spotify.com/v1/tracks/" + id, firstTrack.getHref());

        asyncCompleted.countDown();
      }

      @Override
      public void onFailure(Throwable throwable) {
        fail("Failed to resolve future");
      }
    });

    asyncCompleted.await(1, TimeUnit.SECONDS);
  }

  @Test
  public void shouldGetTracksResult_sync() throws Exception {
    final Api api = Api.DEFAULT_API;
    final HttpManager mockedHttpManager = TestUtil.MockedHttpManager.returningJson("search-track.json");
    final TrackSearchRequest request = api.searchTracks("Mr. Brightside").setHttpManager(mockedHttpManager).build();

    final Paging<Track> trackSearchResult = request.get();

    assertTrue(trackSearchResult.getTotal() > 0);
    assertEquals(20, trackSearchResult.getLimit());
    assertEquals(0, trackSearchResult.getOffset());

    Track[] tracks = trackSearchResult.getItems();

    Track firstTrack = tracks[0];
    assertNotNull(firstTrack.getId());

    String id = firstTrack.getId();
    assertNotNull(firstTrack.getAlbum());
    assertNotNull(firstTrack.getArtists());
    assertEquals("https://api.spotify.com/v1/tracks/" + id, firstTrack.getHref());
  }

}