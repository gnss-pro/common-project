package com.gnss.common.proto;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.gnss.common.constants.RpcEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>Description: RPC protobuf定义</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2018/2/3
 */
@Data
@ProtobufClass
public class RpcProto implements Serializable {

    /**
     * RPC类型
     */
    @Protobuf(fieldType = FieldType.ENUM, order = 1, required = true)
    private RpcEnum rpcType;

    /**
     * 内容
     */
    @Protobuf(fieldType = FieldType.STRING, order = 2)
    private String content;

    public RpcProto() {
    }

    public RpcProto(RpcEnum rpcType) {
        this.rpcType = rpcType;
    }

    public RpcProto(RpcEnum rpcType, String content) {
        this.rpcType = rpcType;
        this.content = content;
    }
}
