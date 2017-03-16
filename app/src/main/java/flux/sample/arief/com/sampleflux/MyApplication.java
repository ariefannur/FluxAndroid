package flux.sample.arief.com.sampleflux;

import android.app.Application;

import flux.sample.arief.com.sampleflux.repositori.LocalRepository;

/**
 * Copyright (C) PT. Sebangsa Bersama - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Originally written by Author Name sebangsa, 15/03/17
 */

public class MyApplication extends Application {

    private static LocalRepository localRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        localRepository = new LocalRepository(this);
    }

    public LocalRepository getLocalRepository(){
        return localRepository;
    }


}
