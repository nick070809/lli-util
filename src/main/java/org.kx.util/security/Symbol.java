package org.kx.util.security;

/**
 * Description ： 不要加数字
 * Created by  xianguang.skx
 * Since PRO0PRO0/6/15
 */

public enum Symbol {

    DIAN_("."),
    FENHAO_(";"),
    XIAOYU_("<"),
    DAYU_(">"),
    KUOHU_("("),
    KUOHUPRO_(")"),
    DAKUOHU_("{"),
    DAKUOHUPRO_("}"),
    MAOHAO_(":"),
    DOUHAO_(","),
    DOUHAOPRO_("\\,"),
    AIT_("@"),
    SHUANYINHAO_("\""),
    YINHAO_("\'"),
    WENHAO_("?"),
    GANTANHAO_("!"),
    SHANGJIANHAO_("^"),
    XIAODENGHAO_("<="),
    DADENGHAO_(">="),
    DENGHAO_("="),
    JIAJIAHAO_("++"),
    JIAHAO_("+"),
    JIANJIANHAO_("--"),
    JIANHAO_("-"),


    YUHAO_("%"),
    JINGHAO_("#"),
    //XIAHUAXIAN_("\\_"),



    BINGQIE_("&&"),
    YU_("&"),
    HUOZHE_("||"),
    HUO_("|"),

    BEIZHUMAX_("///"),
    BEIZHU_("//"),
    BEIZHUMEMO_("/**"),
    BEIZHUMEMOPRO_("/*"),
    BEIZHUMEMOPROS_("*/"),
    SHENGHAO_("*"),
    CHUHAO_("/"),

    SHUZI_("SHUZI_"),
    ZHONGWEN_("ZHONGWEN_"),
   // KONGGE(" "),
    ZHONGWENKONGGE_(" "),
    HUANHANG("\\n"),
    ;





    private String symbol;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    Symbol(String symbol) {
        this.symbol = symbol;
    }

    public static Symbol[] values= Symbol.values() ;



    public static Symbol  nameOf(String name){
        for(Symbol symbol : values){
            if(name.equals(symbol.name())){
                return  symbol;
            }
        }
        return  null;
    }


}
