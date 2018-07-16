package com.alanueyn.projects.practiceroutefinder.db;

import com.alanueyn.projects.practiceroutefinder.util.CheckpointDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;

public class LocalCheckpointDataSource implements CheckpointDataSource {

    private final CheckpointDao mCheckpointDao;

    public LocalCheckpointDataSource(CheckpointDao checkpointDao) {
        mCheckpointDao = checkpointDao;
    }


    @Override
    public Flowable<List<CheckpointEntity>> getCheckpoints() {
        return mCheckpointDao.getListOfCheckpoints();
    }

    @Override
    public void insertCheckpoint(CheckpointEntity checkpointEntity) {
        mCheckpointDao.insertCheckpoint(checkpointEntity);
    }

    @Override
    public void insertAll(ArrayList<CheckpointEntity> checkpoints) {
        mCheckpointDao.insertCheckpoint(checkpoints.toArray(new CheckpointEntity[checkpoints.size()]));
    }


    @Override
    public void deleteCheckpoint(CheckpointEntity checkpoint) {
        mCheckpointDao.deleteCheckpoint(checkpoint);
    }

    @Override
    public void deleteAll() {
        mCheckpointDao.deleteAllCheckpoints();
    }


}
