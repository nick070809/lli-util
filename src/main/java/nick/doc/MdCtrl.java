package nick.doc;

import org.kx.util.FileUtil;
import org.kx.util.JarFileReader;
import org.kx.util.base.MarkDownParser;
import org.kx.util.generate.HtmlGenerate;
import org.kx.util.io.IoUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/9/7
 */

public class MdCtrl {

    private boolean init = false;
    private String myJarPath = System.getProperty("user.home") + File.separator + "myDocs" + File.separator + "lli-util.jar";
    List<MdDoc> docs = new ArrayList<>();
    private static MdCtrl mdCtrl = new MdCtrl();

    private MdCtrl() {
        init();
    }

    ;

    public static MdCtrl getMdCtrl() {
        return mdCtrl;
    }


    private void read2Html(MdDoc mdDoc) {
        //TODO 解密
        if (mdDoc.isDoc()) {
            MarkDownParser md = new MarkDownParser(mdDoc.getName(), mdDoc.getOrigContent());
            mdDoc.setHtmlContent(md.toHtmlString());
        }
    }

    private void saveHtml2Cache(MdDoc mdDoc) throws IOException {
        if (mdDoc.isDoc()) {
            String html = mdDoc.getHtmlContent();
            String filepath = System.getProperty("user.home") + File.separator + "myDocs" + File.separator + mdDoc.getName() + ".html";
            FileUtil.writeStringToFile(html, filepath);
            mdDoc.setCachePath(filepath);
        }
    }


    public void init() {
        try {
            if (!init) {
                String appJarpath = "/Users/xianguang/IdeaProjects/nick070809/xian_app/xian/target/xian_app.jar";
                File appJarFile = new File(appJarpath);
                if (!appJarFile.exists()) {
                    appJarpath = System.getProperty("user.home") + File.separator + "xian_app.jar";
                    appJarFile = new File(appJarpath);
                    if (!appJarFile.exists()) {
                        throw new RuntimeException("not found xian_app jar path !");
                    }
                }
                org.springframework.boot.loader.jar.JarFile jarFile = new org.springframework.boot.loader.jar.JarFile(appJarFile);
                InputStream inputStream = jarFile.getInputStream(jarFile.getJarEntry("BOOT-INF/lib/lli-util-1.0-SNAPSHOT.jar"));
                IoUtil.DataInput2File(inputStream, myJarPath);
                docs = JarFileReader.list(myJarPath);

                docs.stream().forEach(mdDoc -> {
                    mdDoc.setName(FileUtil.getFileName(mdDoc.getPath()));
                    try {
                        mdDoc.setOrigContent(JarFileReader.read(myJarPath, mdDoc.getPath()));
                        read2Html(mdDoc);
                        saveHtml2Cache(mdDoc);
                    } catch (IOException e) {
                        throw new RuntimeException("init mdDoc error " + mdDoc.getPath(), e);
                    }

                });
            }
        } catch (Throwable ex) {
            System.out.println("nick.doc.MdCtrl.init ex " + ex);
        }
    }



    public void loadself() {
        try {
            String myUtilPath = "/Users/xianguang/IdeaProjects/nick070809/lli-util/target/lli-util-1.0-SNAPSHOT.jar";
            String content = JarFileReader.read(myUtilPath, "docs/public/java核心卷.md");
            MdDoc mdDoc = new MdDoc();
            mdDoc.setName("docs/public/java核心2");
            mdDoc.setOrigContent(content);
            read2Html(mdDoc);
            saveHtml2Cache(mdDoc);
        } catch (Throwable ex) {
            System.out.println("nick.doc.MdCtrl.init ex " + ex);
        }

    }



    public String getHtmlContent(MdDoc mdDoc1) {
        return docs.stream().filter(mdDoc -> mdDoc1.getPath().equals(mdDoc.getPath())).findFirst().get().getHtmlContent();
    }


    public String getDocList() throws Exception {
        HtmlGenerate htmlGenerate = new HtmlGenerate();
        return htmlGenerate.generateHerfList(docs, "name", "path");
    }
}
