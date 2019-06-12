package com.adaptris.monitor.agent;

import com.adaptris.core.AdaptrisConnectionImp;
import com.adaptris.core.CoreException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class UDPConnection extends AdaptrisConnectionImp {
    private int port;
    private String group;
    private int timeout = 300000;
    private boolean reuseAddress = true;

    private MulticastSocket socket;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getGroup() {
      return group;
    }

    public void setGroup(String group) {
      this.group = group;
    }

    public MulticastSocket getSocket() {
        return socket;
    }

    @Override
    protected void prepareConnection() throws CoreException {

    }

    @Override
    protected void initConnection() throws CoreException {
        try {
            socket = new MulticastSocket(this.getPort());
            socket.setSoTimeout(timeout);
            socket.setReuseAddress(reuseAddress);
            socket.joinGroup(InetAddress.getByName(this.getGroup()));
        } catch (IOException e) {
            throw new CoreException("Failed to create UDP socket", e);
        }
    }

    @Override
    protected void startConnection() throws CoreException {

    }

    @Override
    protected void stopConnection() {

    }

    @Override
    protected void closeConnection() {
        if (socket != null)
            socket.close();
    }
}
