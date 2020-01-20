package org.kx.util.express;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;

/**
 * Description ： 查看https://blog.csdn.net/weixin_33910460/article/details/92531087
 *                  https://mopheiok.github.io/uncategorized/antlr4/
 *
 *   ANTLR v4 grammar plugin
 * Created by  xianguang.skx
 * Since 2020/1/9
 */

public class XQLExpressRunner {

    public static void main(String[] args) throws Exception {
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        context.put("立马",1);
        context.put("回头",2);
        context.put("杭州",3);
        String express = "立马+回头*杭州-1";
        Object r = runner.execute(express, context, null, true, false);
        System.out.println(r);
    }


}
