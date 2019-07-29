package org.kx.util.common.db;

import java.io.*;

public class PLSQL {

    public static void main(String[] args) throws UnsupportedEncodingException {

        new PLSQL().exeSqlplus("cbpaydev", "cbpaydev", "SERVERTAF", "test.sql", "E:/","E:/test.log");
    }

    public void exeSqlplus(String username, String password, String sid, String fileName, String dir,String result) {
        InputStream in = null;
        FileOutputStream fos = null;
        Process p = null;
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("sqlplus ");
            sb.append(username);
            sb.append("/");
            sb.append(password);
            sb.append("@");
            sb.append(sid);
            sb.append(" @");
            sb.append(fileName);
            String cmd = sb.toString();
            Runtime rt = Runtime.getRuntime();
            p = rt.exec(cmd, null, new File(dir));
            in = p.getInputStream();
            byte[] b = new byte[1024];
         /*  int br = 0;
            while ((br = in.read(b)) != -1) {
                String str = new String(b, 0, br);
                System.out.println("org : "+str);
                int i = str.indexOf("SP2-0310");
                int j = str.indexOf("SQL>");
                System.out.println(new String(str.getBytes("utf8"),"GBK"));
                if (i != -1) {
                    p.destroy();
                }
                if (j != -1) {
                    p.destroy();
                }
            }
            p.waitFor();

            in.close();
            p.destroy();
            System.out.println("执行结束");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }*/
            System.out.println(result);
           fos = new FileOutputStream(result);
            int br = 0;
            while ((br = in.read(b)) != -1) {
                String str = new String(b, 0, br);
                int i = str.indexOf("SP2-0310");
                int j = str.indexOf("SQL>");
                if (i != -1) {
                    p.destroy();
                }
                fos.write(b, 0, br);
                if (j != -1) {
                    p.destroy();
                }
            }
            p.waitFor();
            fos.flush();
            fos.close();
            in.close();
            p.destroy();
            System.out.println("执行结束");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
