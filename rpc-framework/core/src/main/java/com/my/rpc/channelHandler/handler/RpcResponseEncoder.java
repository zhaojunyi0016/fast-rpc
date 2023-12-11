package com.my.rpc.channelHandler.handler;

import com.my.rpc.enums.SerializeEnum;
import com.my.rpc.serialize.Serializer;
import com.my.rpc.serialize.SerializerFactory;
import com.my.rpc.transport.message.MessageFormatConstant;
import com.my.rpc.transport.message.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 响应编码器 : 出站时 -> 将报文转成二进制编码
 * 4B magic 魔数
 * 1B version 版本
 * 2B header length 头部的长度
 * 4B full length  报文总长度
 * 1B code  响应码
 * 1B serialize type  序列化的类型
 * 1B compress  type 压缩的类型
 * 8B requestId 请求 id
 * Body  通过总报文长度减去其他所有加起来的长度获取
 *
 * @Author : Williams
 * Date : 2023/12/8 14:23
 */
@Slf4j
public class RpcResponseEncoder extends MessageToByteEncoder<RpcResponse> {
    @Override
    // 传入的是 rpcRequest 对象,  通过 byteBuf 写出去
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse, ByteBuf byteBuf) throws Exception {
        // 4 个字节 魔数值
        byteBuf.writeBytes(MessageFormatConstant.MAGIC);
        // 1个字节 版本号
        byteBuf.writeByte(MessageFormatConstant.VERSION);
        // 2个字节 header 信息长度
        byteBuf.writeShort(MessageFormatConstant.HEADER_LENGTH);
        // full length  先空出 4个比特位置
        byteBuf.writerIndex(byteBuf.writerIndex() + MessageFormatConstant.FULL_FIELD_LENGTH);
        // code
        byteBuf.writeByte(rpcResponse.getCode());
        // serialize type
        byteBuf.writeByte(rpcResponse.getSerializeType());
        // compress  type
        byteBuf.writeByte(rpcResponse.getCompressType());
        // 8个字节  请求id
        byteBuf.writeLong(rpcResponse.getRequestId());

        Serializer serializer = SerializerFactory.getSerializer(SerializeEnum.getDescByCode(rpcResponse.getSerializeType()));
        // 写入 body
        byte[] bodyBytes = serializer.serialize(rpcResponse.getBody());
        // TODO 压缩
        if (Objects.nonNull(bodyBytes)) {
            byteBuf.writeBytes(bodyBytes);
        }
        int bodyLength = Objects.nonNull(bodyBytes) ? bodyBytes.length : 0;
        // 写指针最后的位置
        int lastIndex = byteBuf.writerIndex();
        // 将写指针的位置移动到总长度的位置上
        byteBuf.writerIndex(
                MessageFormatConstant.MAGIC.length
                        + MessageFormatConstant.VERSION_LENGTH
                        + MessageFormatConstant.HEADER_FIELD_LENGTH);
        byteBuf.writeInt(MessageFormatConstant.HEADER_LENGTH + bodyLength);
        // 写指针归为
        byteBuf.writerIndex(lastIndex);
    }

}
