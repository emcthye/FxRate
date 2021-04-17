package io.github.emcthye.fxrate.entity.local;

import android.util.Log;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory;

import java.io.IOException;
import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.emcthye.fxrate.entity.remote.CurrencyModelAPI;

/**
 * Json Serializer/Deserializer.
 */
@Singleton
public class Serializer {

  private final Moshi moshi = new Moshi.Builder()
          .add(PolymorphicJsonAdapterFactory.of(Object.class, "")
                  .withSubtype(CurrencyModelAPI.class, ""))
          .build();

  @Inject Serializer() {
  }

  public String serialize(Object object) {

    JsonAdapter<Object> jsonGenericAdapter = moshi.adapter(Object.class);
    return jsonGenericAdapter.toJson(object);
  }

  public <T> T deserialize(String string) throws IOException {
    Type type = Types.newParameterizedType(Object.class, Class.class);
    JsonAdapter<T> jsonGenericAdapter = moshi.adapter(type);
    return jsonGenericAdapter.fromJson(string);
  }
}
