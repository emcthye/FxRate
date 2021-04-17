package io.github.emcthye.fxrate.entity.local;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.emcthye.fxrate.entity.remote.CurrencyModelAPI;
import io.github.emcthye.fxrate.entity.CurrencyDataSource;
import io.github.emcthye.fxrate.util.ErrorMessageFactory;
import io.github.emcthye.fxrate.util.threading.ThreadExecutor;
import io.reactivex.Observable;

import static android.text.format.DateUtils.getRelativeTimeSpanString;

@Singleton
public class CurrencyLocalDataSource implements CurrencyDataSource {

    private static final String SETTINGS_FILE_NAME = "io.github.emcthye.fxrate.SETTINGS";
    private static final String SETTINGS_KEY_LAST_CACHE_UPDATE = "last_cache_update";

    private static final String DEFAULT_FILE_NAME = "user_";
    private static final long EXPIRATION_TIME = 12 * 60 * 60 * 1000;

    Context context;
    private final File cacheDir;
    private final Serializer serializer;
    private final FileManager fileManager;
    private final ThreadExecutor threadExecutor;

    @Inject
    public CurrencyLocalDataSource(Context context, Serializer serializer,
                                   FileManager fileManager, ThreadExecutor executor) {
        if (context == null || serializer == null || fileManager == null || executor == null) {
            throw new IllegalArgumentException("Invalid null parameter");
        }
        this.context = context.getApplicationContext();
        this.cacheDir = this.context.getCacheDir();
        this.serializer = serializer;
        this.fileManager = fileManager;
        this.threadExecutor = executor;
    }

    @Override
    public Observable<CurrencyModelAPI> getRateList(String startDate, String baseCurrency) {

        return Observable.create(emitter -> {
            final File userEntityFile = buildFile(baseCurrency);
            final String fileContent = fileManager.readFileContent(userEntityFile);
            final CurrencyModelAPI currencyModelAPI =
                    serializer.deserialize(fileContent);

            if (currencyModelAPI != null) {
                emitter.onNext(currencyModelAPI);
                emitter.onComplete();
            } else {
                emitter.onError(new ErrorMessageFactory.LocalDataException());
            }
        });
    }

    @Override
    public void saveRateList(CurrencyModelAPI rateList) {
        final File userEntityFile = this.buildFile(rateList.base);
        if (!isCached(rateList.base)) {
            final String jsonString = this.serializer.serialize(rateList);
            this.executeAsynchronously(new CacheWriter(this.fileManager, userEntityFile, jsonString));
            setLastCacheUpdateTimeMillis();
        }
    }

    @Override
    public Observable<String> getLastUpdated() {
        return Observable.create(emitter -> {
            emitter.onNext(getRelativeTimeSpanString(getLastCacheUpdateTimeMillis(), System.currentTimeMillis(),
                    DateUtils.SECOND_IN_MILLIS).toString());
            emitter.onComplete();
        });
    }

    public boolean isCached(String baseCurrency) {
        final File userEntityFile = this.buildFile(baseCurrency);
        return this.fileManager.exists(userEntityFile);
    }

    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        long lastUpdateTime = this.getLastCacheUpdateTimeMillis();

        boolean expired = ((currentTime - lastUpdateTime) > EXPIRATION_TIME);

        if (expired) {
            this.evictAll();
        }

        return expired;
    }

    public void evictAll() {
        this.executeAsynchronously(new CacheEvictor(this.fileManager, this.cacheDir));
    }

    protected File buildFile(String baseCurrency) {
        String fileNameBuilder = this.cacheDir.getPath() +
                File.separator +
                DEFAULT_FILE_NAME +
                baseCurrency;
        return new File(fileNameBuilder);
    }

    protected void setLastCacheUpdateTimeMillis() {
        final long currentMillis = System.currentTimeMillis();
        this.fileManager.writeToPreferences(this.context, SETTINGS_FILE_NAME,
                SETTINGS_KEY_LAST_CACHE_UPDATE, currentMillis);
    }

    protected long getLastCacheUpdateTimeMillis() {
        return this.fileManager.getFromPreferences(this.context, SETTINGS_FILE_NAME,
                SETTINGS_KEY_LAST_CACHE_UPDATE);
    }

    protected void executeAsynchronously(Runnable runnable) {
        this.threadExecutor.execute(runnable);
    }

    /**
     * {@link Runnable} class for writing to disk.
     */
    protected static class CacheWriter implements Runnable {
        private final FileManager fileManager;
        private final File fileToWrite;
        private final String fileContent;

        CacheWriter(FileManager fileManager, File fileToWrite, String fileContent) {
            this.fileManager = fileManager;
            this.fileToWrite = fileToWrite;
            this.fileContent = fileContent;
        }

        @Override
        public void run() {
            this.fileManager.writeToFile(fileToWrite, fileContent);
        }
    }

    /**
     * {@link Runnable} class for evicting all the cached files
     */
    protected static class CacheEvictor implements Runnable {
        private final FileManager fileManager;
        private final File cacheDir;

        CacheEvictor(FileManager fileManager, File cacheDir) {
            this.fileManager = fileManager;
            this.cacheDir = cacheDir;
        }

        @Override
        public void run() {
            this.fileManager.clearDirectory(this.cacheDir);
        }
    }
}
