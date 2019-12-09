package com.elegion.test.behancer.ui.profile;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v4.widget.SwipeRefreshLayout;

import com.elegion.test.behancer.data.Storage;
import com.elegion.test.behancer.data.model.user.User;
import com.elegion.test.behancer.utils.ApiUtils;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProfileViewModel extends ViewModel {

   // private Storage mStorage;
   @Inject
   Storage mStorage;


    private String mUsername;

    private Disposable mDisposable;
    private ObservableBoolean mIsLoading = new ObservableBoolean();
    private ObservableBoolean mIsErrorVisible = new ObservableBoolean();
    private ObservableField<User> mProfile = new ObservableField<>();
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = this::loadProfile;

    public ProfileViewModel(Storage storage, String user){
        mStorage=storage;
        mUsername = user;
        loadProfile();
    }


    public void loadProfile() {
        mDisposable = ApiUtils.getApiService().getUserInfo(mUsername)
                .subscribeOn(Schedulers.io())
                .doOnSuccess(response -> mStorage.insertUser(response))
                .onErrorReturn(throwable ->
                        ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.getClass()) ?
                                mStorage.getUser(mUsername) :
                                null)
                .doOnSubscribe(disposable -> mIsLoading.set(true))
                .doFinally(() ->  mIsLoading.set(false))
                .subscribe(
                        response -> { mStorage.insertUser(response);
                            mProfile.set(response.getUser());
                            mIsLoading.set(false);
                        },
                        throwable -> { mIsErrorVisible.set(true);

                        });
    }

    @Override
    public void onCleared(){
        mStorage = null;
        if(mDisposable!=null){
            mDisposable.dispose();
        }

    }

    public ObservableBoolean getIsLoading(){
        return mIsLoading;
    }

    public ObservableBoolean getIsErrorVisible(){
        return mIsErrorVisible;
    }
    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() { return mOnRefreshListener; }

    public ObservableField<User> getProfile() {return mProfile; }



}
