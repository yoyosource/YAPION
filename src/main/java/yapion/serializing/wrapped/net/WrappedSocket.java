/*
 *  // SPDX-License-Identifier: Apache-2.0
 * // YAPION
 * // Copyright (C) 2019,2020 yoyosource
 */

package yapion.serializing.wrapped.net;

import yapion.annotations.object.YAPIONData;
import yapion.annotations.object.YAPIONPostDeserialization;
import yapion.annotations.serialize.YAPIONOptimize;
import yapion.exceptions.YAPIONException;
import yapion.serializing.wrapped.WrappedImplementation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.channels.SocketChannel;

@YAPIONData
// TODO
@WrappedImplementation(since = "0.?.0")
public class WrappedSocket {

    @SuppressWarnings({"java:S2065"})
    private transient Socket socket;

    @YAPIONOptimize
    private Proxy proxy = null;

    @YAPIONOptimize
    private String host = null;

    @YAPIONOptimize
    private Integer port = null;

    @YAPIONOptimize
    private Integer localPort = null;

    @YAPIONOptimize
    private InetAddress address = null;

    @YAPIONOptimize
    private InetAddress localAddr = null;

    public WrappedSocket() {
        socket = new Socket();
    }

    public WrappedSocket(Proxy proxy) {
        socket = new Socket(proxy);
        this.proxy = proxy;
    }

    public WrappedSocket(String host, int port) throws IOException {
        socket = new Socket(host, port);
        this.host = host;
        this.port = port;
    }

    public WrappedSocket(InetAddress address, int port) throws IOException {
        socket = new Socket(address, port);
        this.address = address;
        this.port = port;
    }

    public WrappedSocket(String host, int port, InetAddress localAddr, int localPort) throws IOException {
        socket = new Socket(host, port, localAddr, localPort);
        this.host = host;
        this.port = port;
        this.localAddr = localAddr;
        this.localPort = localPort;
    }

    public WrappedSocket(InetAddress address, int port, InetAddress localAddr, int localPort) throws IOException {
        socket = new Socket(address, port, localAddr, localPort);
        this.address = address;
        this.port = port;
        this.localAddr = localAddr;
        this.localPort = localPort;
    }

    @YAPIONPostDeserialization
    private void open() throws IOException {
        if (socket != null) {
            throw new YAPIONException("Cannot open new Socket if it already exists");
        }
        if (proxy != null) {
            socket = new Socket(proxy);
            return;
        }
        if (address != null && port != null && localAddr != null && localPort != null) {
            socket = new Socket(address, port, localAddr, localPort);
            return;
        }
        if (host != null && port != null && localAddr != null && localPort != null) {
            socket = new Socket(host, port, localAddr, localPort);
            return;
        }
        if (address != null && port != null) {
            socket = new Socket(address, port);
            return;
        }
        if (host != null && port != null) {
            socket = new Socket(host, port);
            return;
        }
        socket = new Socket();
    }

    public Socket getSocket() {
        return socket;
    }

    public void connect(SocketAddress endpoint) throws IOException {
        socket.connect(endpoint);
    }

    public void connect(SocketAddress endpoint, int timeout) throws IOException {
        socket.connect(endpoint, timeout);
    }

    public void bind(SocketAddress bindpoint) throws IOException {
        socket.bind(bindpoint);
    }

    public InetAddress getInetAddress() {
        return socket.getInetAddress();
    }

    public InetAddress getLocalAddress() {
        return socket.getLocalAddress();
    }

    public int getPort() {
        return socket.getPort();
    }

    public int getLocalPort() {
        return socket.getLocalPort();
    }

    public SocketAddress getRemoteSocketAddress() {
        return socket.getRemoteSocketAddress();
    }

    public SocketAddress getLocalSocketAddress() {
        return socket.getLocalSocketAddress();
    }

    public SocketChannel getChannel() {
        return socket.getChannel();
    }

    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }

    public void setTcpNoDelay(boolean on) throws SocketException {
        socket.setTcpNoDelay(on);
    }

    public boolean getTcpNoDelay() throws SocketException {
        return socket.getTcpNoDelay();
    }

    public void setSoLinger(boolean on, int linger) throws SocketException {
        socket.setSoLinger(on, linger);
    }

    public int getSoLinger() throws SocketException {
        return socket.getSoLinger();
    }

    public void sendUrgentData(int data) throws IOException {
        socket.sendUrgentData(data);
    }

    public void setOOBInline(boolean on) throws SocketException {
        socket.setOOBInline(on);
    }

    public boolean getOOBInline() throws SocketException {
        return socket.getOOBInline();
    }

    public void setSoTimeout(int timeout) throws SocketException {
        socket.setSoTimeout(timeout);
    }

    public int getSoTimeout() throws SocketException {
        return socket.getSoTimeout();
    }

    public void setSendBufferSize(int size) throws SocketException {
        socket.setSendBufferSize(size);
    }

    public int getSendBufferSize() throws SocketException {
        return socket.getSendBufferSize();
    }

    public void setReceiveBufferSize(int size) throws SocketException {
        socket.setReceiveBufferSize(size);
    }

    public int getReceiveBufferSize() throws SocketException {
        return socket.getReceiveBufferSize();
    }

    public void setKeepAlive(boolean on) throws SocketException {
        socket.setKeepAlive(on);
    }

    public boolean getKeepAlive() throws SocketException {
        return socket.getKeepAlive();
    }

    public void setTrafficClass(int tc) throws SocketException {
        socket.setTrafficClass(tc);
    }

    public int getTrafficClass() throws SocketException {
        return socket.getTrafficClass();
    }

    public void setReuseAddress(boolean on) throws SocketException {
        socket.setReuseAddress(on);
    }

    public boolean getReuseAddress() throws SocketException {
        return socket.getReuseAddress();
    }

    public void close() throws IOException {
        socket.close();
    }

    public void shutdownInput() throws IOException {
        socket.shutdownInput();
    }

    public void shutdownOutput() throws IOException {
        socket.shutdownOutput();
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    public boolean isBound() {
        return socket.isBound();
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    public boolean isInputShutdown() {
        return socket.isInputShutdown();
    }

    public boolean isOutputShutdown() {
        return socket.isOutputShutdown();
    }

    public static void setSocketImplFactory(SocketImplFactory fac) throws IOException {
        Socket.setSocketImplFactory(fac);
    }

    public void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
        socket.setPerformancePreferences(connectionTime, latency, bandwidth);
    }

}
