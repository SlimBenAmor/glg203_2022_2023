package com.yaps.common.dao;

public interface IdSequenceDAO {
    int getCurrentMaxId(String tableName);
    void setCurrentMaxId(String tableName, int newMaxId);
}
