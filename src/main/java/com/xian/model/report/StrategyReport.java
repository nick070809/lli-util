package com.xian.model.report;

import lombok.Data;
import org.kx.util.common.db.ChineseName;


@Data
public class StrategyReport {
    @ChineseName("主键")
    private Long id;
    @ChineseName("外部请求号")
    private String outBizId;
    @ChineseName("机构")
    private String orgCode;


}