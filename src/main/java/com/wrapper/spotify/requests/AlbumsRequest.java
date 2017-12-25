package com.wrapper.spotify.requests;

import com.google.common.base.Joiner;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonParser;
import com.wrapper.spotify.exceptions.*;
import com.wrapper.spotify.objects.model_objects.Album;

import java.io.IOException;
import java.util.List;

public class AlbumsRequest extends AbstractRequest {

  private AlbumsRequest(final Builder builder) {
    super(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  public Album[] get() throws
          IOException,
          NoContentException,
          BadRequestException,
          UnauthorizedException,
          ForbiddenException,
          NotFoundException,
          TooManyRequestsException,
          InternalServerErrorException,
          BadGatewayException,
          ServiceUnavailableException {
    return new Album.JsonUtil().createModelObjectArray(new JsonParser().parse(getJson()).getAsJsonObject().get("albums").getAsJsonArray());
  }

  public SettableFuture<Album[]> getAsync() throws
          IOException,
          NoContentException,
          BadRequestException,
          UnauthorizedException,
          ForbiddenException,
          NotFoundException,
          TooManyRequestsException,
          InternalServerErrorException,
          BadGatewayException,
          ServiceUnavailableException {
    return getAsync(new Album.JsonUtil().createModelObjectArray(new JsonParser().parse(getJson()).getAsJsonObject().get("albums").getAsJsonArray()));
  }

  public static final class Builder extends AbstractRequest.Builder<Builder> {

    public Builder id(final List<String> ids) {
      assert (ids != null);
      String idsParameter = Joiner.on(",").join(ids);
      setPath("/v1/albums");
      return setParameter("ids", idsParameter);
    }

    @Override
    public AlbumsRequest build() {
      return new AlbumsRequest(this);
    }

  }
}