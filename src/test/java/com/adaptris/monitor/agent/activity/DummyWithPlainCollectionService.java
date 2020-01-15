package com.adaptris.monitor.agent.activity;

import com.adaptris.core.*;

import java.util.List;

public class DummyWithPlainCollectionService extends ServiceImp {

    private List<Service> plainCollection;

    public List<Service> getPlainCollection() {
        return plainCollection;
    }

    public void setPlainCollection(List<Service> plainCollection) {
        this.plainCollection = plainCollection;
    }

    @Override
    public void doService(AdaptrisMessage msg) throws ServiceException {

    }

    @Override
    protected void initService() throws CoreException {

    }

    @Override
    protected void closeService() {

    }

    @Override
    public void prepare() throws CoreException {

    }
}
