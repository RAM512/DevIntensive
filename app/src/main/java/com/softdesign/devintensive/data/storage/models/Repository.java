package com.softdesign.devintensive.data.storage.models;

import com.softdesign.devintensive.data.network.res.UserModelRes;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Repository {

    @Id
    private Long id;

    @NotNull
    @Unique
    private String remoteId;

    private String repositoryName;

    private String userRemoteId;

    public Repository(UserModelRes.Repo repositoryRes, String userId) {
        repositoryName = repositoryRes.getGit();
        userRemoteId = userId;
        remoteId = repositoryRes.getId();
    }

    @Generated(hash = 1976272162)
    public Repository(Long id, @NotNull String remoteId, String repositoryName,
            String userRemoteId) {
        this.id = id;
        this.remoteId = remoteId;
        this.repositoryName = repositoryName;
        this.userRemoteId = userRemoteId;
    }

    @Generated(hash = 984204935)
    public Repository() {
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public String getUserRemoteId() {
        return this.userRemoteId;
    }

    public void setUserRemoteId(String userRemoteId) {
        this.userRemoteId = userRemoteId;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getRemoteId() {
        return this.remoteId;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
