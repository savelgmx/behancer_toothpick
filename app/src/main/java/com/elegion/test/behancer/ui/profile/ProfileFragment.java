package com.elegion.test.behancer.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elegion.test.behancer.AppDelegate;
import com.elegion.test.behancer.databinding.ProfileBinding;

import javax.inject.Inject;

import toothpick.Toothpick;

/**
 * Created by Vladislav Falzan.
 */



public class ProfileFragment extends Fragment {

    public static final String PROFILE_KEY = "PROFILE_KEY";
    private String mUsername;
    @Inject
    ProfileViewModel mProfileViewModel;


    public static ProfileFragment newInstance(Bundle args) {
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mUsername=getArguments().getString(PROFILE_KEY);
        Toothpick.inject(this, AppDelegate.getAppScope());
        mProfileViewModel.onAttach(mUsername);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ProfileBinding binding = ProfileBinding.inflate(inflater,container,false);
        binding.setPm(mProfileViewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();

    }

}
