package org.kx.util.base;

import lombok.Data;

import java.io.Serializable;

/**
 * Description ： Created by  xianguang.skx Since 2019/7/29
 */

@Data
public class ResultRich<T> implements Serializable {
    private static final long serialVersionUID = -7930454800828564981L;
    private boolean success;
    private String code;
    private String msg;
    private T model;

    public ResultRich() {
    }

    public ResultRich(boolean success) {
        this.success = success;
    }

    public ResultRich(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }
}