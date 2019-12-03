package com.elegion.test.behancer;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.elegion.test.behancer.data.Storage;
import com.elegion.test.behancer.data.database.BehanceDatabase;
import com.elegion.test.behancer.di.AppModule;
import com.elegion.test.behancer.di.NetworkModule;

import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.configuration.Configuration;
import toothpick.registries.FactoryRegistryLocator;
import toothpick.registries.MemberInjectorRegistryLocator;
import toothpick.smoothie.module.SmoothieApplicationModule;

/**
 * Created by Vladislav Falzan.
 */

public class AppDelegate extends Application {

    private Storage mStorage;
    private static Scope sAppScope;

    @Override
    public void onCreate() {
        super.onCreate();

        final BehanceDatabase database = Room.databaseBuilder(this, BehanceDatabase.class, "behance_database")
                .fallbackToDestructiveMigration()
                .build();

        mStorage = new Storage(database.getBehanceDao());
        //==========Tooth Prick==========================
        Toothpick.setConfiguration(Configuration.forProduction().disableReflection());
        MemberInjectorRegistryLocator.setRootRegistry(new com.elegion.test.behancer.MemberInjectorRegistry());
        FactoryRegistryLocator.setRootRegistry(new com.elegion.test.behancer.FactoryRegistry());

        sAppScope = Toothpick.openScope(AppDelegate.class);
        sAppScope.installModules(new SmoothieApplicationModule(this), new NetworkModule(), new AppModule(this));

        //===================================================
    }
    public static Scope getAppScope() {
        return sAppScope;
    }

    public Storage getStorage() {
        return mStorage;
    }
}
