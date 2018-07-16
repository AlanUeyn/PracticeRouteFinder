package com.alanueyn.projects.practiceroutefinder.util;

import android.content.Context;

import com.alanueyn.projects.practiceroutefinder.db.CheckpointDatabase;
import com.alanueyn.projects.practiceroutefinder.db.LocalCheckpointDataSource;
import com.alanueyn.projects.practiceroutefinder.viewmodel.ViewModelFactory;

public class Injection {

    public static CheckpointDataSource provideCheckpointDataSource(Context context) {
        CheckpointDatabase checkpontDatabase = CheckpointDatabase.getInstance(context);
        return new LocalCheckpointDataSource(checkpontDatabase.checkpointDao());
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        CheckpointDataSource dataSource = provideCheckpointDataSource(context);
        return new ViewModelFactory(dataSource);
    }

}
