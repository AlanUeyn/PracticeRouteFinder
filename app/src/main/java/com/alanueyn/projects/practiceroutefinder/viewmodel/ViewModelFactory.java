package com.alanueyn.projects.practiceroutefinder.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.alanueyn.projects.practiceroutefinder.util.CheckpointDataSource;

public class ViewModelFactory implements ViewModelProvider.Factory {


    private final CheckpointDataSource mDataSource;

    public ViewModelFactory(CheckpointDataSource dataSource) {
        mDataSource = dataSource;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(CheckpointViewModel.class)) {
            return (T) new CheckpointViewModel(mDataSource);
        }

        else
            throw new IllegalArgumentException("Unknow ViewModel class");
    }
}
