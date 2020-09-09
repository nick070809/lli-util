package nick.doc;

import lombok.Data;

import java.io.Serializable;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/9/7
 */

@Data
public class MdDoc implements Serializable {

    private  boolean isDoc;

    private String name ;

    private  String path ;

    private  String origContent ;

    private  String htmlContent ;

    private  String cachePath ;

    private  boolean isPrivate;

    private  boolean isFile;

    private  boolean isCode ;

    public MdDoc() {}

    public MdDoc(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public boolean getIsDoc() {
        return isDoc;
    }

    public boolean getIsPrivate() {
        return isPrivate;
    }

    public boolean getIsFile() {
        return isFile;
    }

    public boolean getIsCode() {
        return isCode;
    }

    public boolean isDoc() {
        return isDoc;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public boolean isFile() {
        return isFile;
    }

    public boolean isCode() {
        return isCode;
    }
}
