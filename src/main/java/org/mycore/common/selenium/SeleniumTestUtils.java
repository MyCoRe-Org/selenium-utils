/**
 * 
 */
package org.mycore.common.selenium;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @author Sebastian Röher (basti890)
 *
 */
public class SeleniumTestUtils {
    public static InetAddress getLocalAdress(String remoteHost, int port) throws IOException {
        InetSocketAddress socketAddr = new InetSocketAddress(remoteHost, port);
        SocketChannel socketChannel = SocketChannel.open(socketAddr);
        InetSocketAddress localAddress = (InetSocketAddress) socketChannel.getLocalAddress();
        return localAddress.getAddress();
    }
}
