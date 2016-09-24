package com.chirikhin.network1udp;

import java.net.InetAddress;


public class InfoAboutOtherPC {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InfoAboutOtherPC that = (InfoAboutOtherPC) o;

        if (port != that.port) return false;
        return inetAddress != null ? inetAddress.equals(that.inetAddress) : that.inetAddress == null;

    }

    @Override
    public int hashCode() {
        int result = port;
        result = 31 * result + (inetAddress != null ? inetAddress.hashCode() : 0);
        return result;
    }

    private final int port;
    private final InetAddress inetAddress;
    private long date;

    private final static long MAX_GAP = 5000;

    public InfoAboutOtherPC(int port, InetAddress inetAddress, long dateTime) {
        this.date = dateTime;
        this.port = port;
        this.inetAddress = inetAddress;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }


    public boolean isActual (long date) {
        if (date - this.date > MAX_GAP) {
            return false;
        }

        return true;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
