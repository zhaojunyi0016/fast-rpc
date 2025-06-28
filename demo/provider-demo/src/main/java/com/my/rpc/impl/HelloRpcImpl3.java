package com.my.rpc.impl;

import com.my.rpc.SayHelloRpc3;

/**
 * @Author : Williams
 * Date : 2023/12/4 17:40
 */
public class HelloRpcImpl3 implements SayHelloRpc3 {
    @Override
    public String sayHi(String msg) {
        return " =http://127.0.0.111:7890 http_proxy=http://127.0.0.1:7890 all_proxy=socks5://127.0.0.1:7890+ + consumer:" + msg;
    }
}
