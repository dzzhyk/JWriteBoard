package com.yankaizhang.board.headless;

import java.util.Scanner;

/**
 * 控制台版本服务端
 */
public class ServerHeadless {

    private final ServerAcceptorHeadless acceptor = new ServerAcceptorHeadless();
    private final Scanner in = new Scanner(System.in);

    public ServerHeadless(){
        System.out.print("请输入服务器监听端口: ");
        int port = in.nextInt();
        acceptor.setPort(port);
        acceptor.start();
    }

    public static void main(String[] args) {
        new ServerHeadless();
    }
}
