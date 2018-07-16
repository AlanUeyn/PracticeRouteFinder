package com.alanueyn.projects.practiceroutefinder.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.widget.ArrayAdapter;

import com.alanueyn.projects.practiceroutefinder.db.CheckpointEntity;
import com.alanueyn.projects.practiceroutefinder.logic.Checkpoint;
import com.alanueyn.projects.practiceroutefinder.util.CheckpointDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;


public class CheckpointViewModel extends ViewModel {

    private final CheckpointDataSource mDataSource;



    public CheckpointViewModel(CheckpointDataSource mDataSource) {
        this.mDataSource = mDataSource;
    }

    public Flowable<List<CheckpointEntity>> getCheckpoints() {
        return mDataSource.getCheckpoints();

    }

    public Completable insertAllCheckpoints(ArrayList<CheckpointEntity> checkpoints) {
        return Completable.fromAction(() -> {
           mDataSource.insertAll(checkpoints);
        });
    }

    public Completable insertCheckpoint(CheckpointEntity checkpointEntity) {
        return Completable.fromAction(() -> {
            mDataSource.insertCheckpoint(checkpointEntity);
        });
    }

    public Completable deleteCheckpoint(CheckpointEntity checkpoint) {
        return Completable.fromAction(() -> {
            mDataSource.deleteCheckpoint(checkpoint);
        });
    }

    public Completable deleteAll() {
        return Completable.fromAction(mDataSource::deleteAll);
    }

}
