package nick.doc;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/10/16
 */

public enum MyDocLocationEnum {

    日常常用("/Users/xianguang/IdeaProjects/nick070809/lli-util/src/main/resources/docs/private/日常常用.txt" ,
            "/Users/xianguang/IdeaProjects/nick070809/lli-util/temp/docs/日常常用.txt"),

    帐期("/Users/xianguang/IdeaProjects/nick070809/lli-util/src/main/resources/docs/private/帐期.txt",
            "/Users/xianguang/IdeaProjects/nick070809/lli-util/temp/docs/帐期.md"),

    查理房产("/Users/xianguang/IdeaProjects/nick070809/lli-util/src/main/resources/docs/private/查理房产.txt",
            "/Users/xianguang/IdeaProjects/nick070809/lli-util/temp/docs/查理房产.md"),

    国际业务新增("/Users/xianguang/IdeaProjects/nick070809/lli-util/src/main/resources/docs/private/国际业务新增.txt",
            "/Users/xianguang/IdeaProjects/nick070809/lli-util/temp/docs/国际业务新增.md"),

    问题定位("/Users/xianguang/IdeaProjects/nick070809/lli-util/src/main/resources/docs/private/问题定位.txt",
            "/Users/xianguang/IdeaProjects/nick070809/lli-util/temp/docs/问题定位.txt"),

    ;

    private String privateSource;
    private String originSource;

    private MyDocLocationEnum(String privateSource, String originSource) {
        this.privateSource =privateSource ;
        this.originSource =originSource ;
    }


    public String getPrivateSource() {
        return privateSource;
    }

    public String getOriginSource() {
        return originSource;
    }
}
