package com.elegion.test.behancer.ui.projects;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elegion.test.behancer.AppDelegate;
import com.elegion.test.behancer.databinding.ProjectsBinding;
import com.elegion.test.behancer.ui.profile.ProfileActivity;
import com.elegion.test.behancer.ui.profile.ProfileFragment;

import javax.inject.Inject;

import toothpick.Toothpick;

/**
 * Created by Vladislav Falzan.
 */

public class ProjectsFragment extends Fragment {

    @Inject
    ProjectsViewModel mProjectsViewModel;
    private ProjectsAdapter.OnItemClickListener mOnItemClickListener = username -> {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        Bundle args = new Bundle();
        args.putString(ProfileFragment.PROFILE_KEY, username);
        intent.putExtra(ProfileActivity.USERNAME_KEY, args);
        startActivity(intent);
    };

    public static ProjectsFragment newInstance() {
        return new ProjectsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Toothpick.inject(this, AppDelegate.getAppScope());
        mProjectsViewModel.mOnItemClickListener = mOnItemClickListener;
        mProjectsViewModel.onAttach();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ProjectsBinding binding = ProjectsBinding.inflate(inflater, container, false);
        binding.setVm(mProjectsViewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

}
