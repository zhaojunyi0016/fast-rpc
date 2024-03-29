package com.my.rpc.exception;

/**
 * @Author : Williams
 * Date : 2023/12/5 18:06
 */
public class ZookeeperException extends RuntimeException {
    private String msg;

    public ZookeeperException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public ZookeeperException() {
        super();
    }
}
