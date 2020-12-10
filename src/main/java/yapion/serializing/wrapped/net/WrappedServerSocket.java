// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.wrapped.net;

import yapion.annotations.object.YAPIONData;
import yapion.annotations.object.YAPIONPostDeserialization;
import yapion.annotations.serialize.YAPIONOptimize;
import yapion.exceptions.YAPIONException;
import yapion.serializing.wrapped.WrappedImplementation;

import java.io.IOException;
import java.net.*;
import java.nio.channels.ServerSocketChannel;

@YAPIONData
@WrappedImplementation(since = "0.20.1")
public class WrappedServerSocket {

    @SuppressWarnings({"java:S2065"})
    private transient ServerSocket serverSocket;

    @YAPIONOptimize
    private Integer port = null;

    @YAPIONOptimize
    private Integer backlog = null;

    @YAPIONOptimize
    private InetAddress bindAddr = null;

    public WrappedServerSocket() throws IOException {
        serverSocket = new ServerSocket();
    }

    public WrappedServerSocket(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        this.port = port;
    }

    public WrappedServerSocket(int port, int backlog) throws IOException {
        serverSocket = new ServerSocket(port, backlog);
        this.port = port;
        this.backlog = backlog;
    }

    public WrappedServerSocket(int port, int backlog, InetAddress bindAddr) throws IOException {
        serverSocket = new ServerSocket(port, backlog, bindAddr);
        this.port = port;
        this.backlog = backlog;
        this.bindAddr = bindAddr;
    }

    @YAPIONPostDeserialization
    private void open() throws IOException {
        if (serverSocket != null) {
            throw new YAPIONException("Cannot open new ServerSocket if it already exists");
        }
        if (port != null && backlog != null && bindAddr != null) {
            serverSocket = new ServerSocket(port, backlog, bindAddr);
            return;
        }
        if (port != null && backlog != null) {
            serverSocket = new ServerSocket(port, backlog);
            return;
        }
        if (port != null) {
            serverSocket = new ServerSocket(port);
            return;
        }
        serverSocket = new ServerSocket();
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void bind(SocketAddress endpoint) throws IOException {
        serverSocket.bind(endpoint);
        bindAddr = serverSocket.getInetAddress();
    }

    public void bind(SocketAddress endpoint, int backlog) throws IOException {
        serverSocket.bind(endpoint, backlog);
        bindAddr = serverSocket.getInetAddress();
        this.backlog = backlog;
    }

    public InetAddress getInetAddress() {
        return serverSocket.getInetAddress();
    }

    public int getLocalPort() {
        return serverSocket.getLocalPort();
    }

    public SocketAddress getLocalSocketAddress() {
        return serverSocket.getLocalSocketAddress();
    }

    public Socket accept() throws IOException {
        return serverSocket.accept();
    }

    public void close() throws IOException {
        serverSocket.close();
    }

    public ServerSocketChannel getChannel() {
        return serverSocket.getChannel();
    }

    public boolean isBound() {
        return serverSocket.isBound();
    }

    public boolean isClosed() {
        return serverSocket.isClosed();
    }

    public void setSoTimeout(int timeout) throws SocketException {
        serverSocket.setSoTimeout(timeout);
    }

    public int getSoTimeout() throws IOException {
        return serverSocket.getSoTimeout();
    }

    public void setReuseAddress(boolean on) throws SocketException {
        serverSocket.setReuseAddress(on);
    }

    public boolean getReuseAddress() throws SocketException {
        return serverSocket.getReuseAddress();
    }

    public static void setSocketFactory(SocketImplFactory fac) throws IOException {
        ServerSocket.setSocketFactory(fac);
    }

    public void setReceiveBufferSize(int size) throws SocketException {
        serverSocket.setReceiveBufferSize(size);
    }

    public int getReceiveBufferSize() throws SocketException {
        return serverSocket.getReceiveBufferSize();
    }

    public void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
        serverSocket.setPerformancePreferences(connectionTime, latency, bandwidth);
    }

}