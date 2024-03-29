package com.my.rpc.exception;

/**
 * @Author : Williams
 * Date : 2023/12/5 18:06
 */
public class RegistryException extends RuntimeException {
    private String msg;

    public RegistryException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public RegistryException(Exception msg) {
        super(msg);
    }

}
