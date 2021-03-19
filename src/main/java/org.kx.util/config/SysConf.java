package org.kx.util.config;

import org.kx.util.rsa.RsaCommon;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/10/16
 */


public class SysConf {

    public  static String  upLoadPath ="/Users/xianguang/Downloads/dfiles/";

    public  static      String dc ="J5lbNsIGnQ+1l8RfcQNTcbuLW57+Ua+pptvI6F7lp3NDhCDCW6POEY3PutPQ2xtCvIk7Ka5CWyuaOXwSP1TZlI0r+06sUNJft+7R2VCsq4bgsw8Tb++L78R6k+81DP15rchaypbUyAwQlQyxrEBFYwcewH71OykKt5SkSabtfzI=";
    public  static      String op ="SRqcjwalZaeraiiyxiyHpqn/txuU0eVvFYbq9tij7qMIlmdLZjoU/Zv8KKYGpDeRZ6VsyZ90IxLt635X0eBl/HWulfIzjDxPzENEmHXe5Ts1FrXJp+H8k530t1im48lxov39G+ng5RReSTObM/sLvgk+k721MKxnnjSfG5Ie7Sg=";
    public  static      String ef ="Xfop2TblvbHmXMBzcO16aJjcAd6xLBoAIUWuquDAlzLXBMLmoGFtvgg8lPOSSM/cPZ2DEbXBijrQY+b4zrNa5q0t3cIrhCFQr7MzZDlo/M/3Dd5taYBxJ1GWPwcMxsb/x8ZjHtypdqNEoGWjExKBaDRB3nLeiHHHsywFn3y4YJQ=";

    public  static      String mc ="module_code";
    public  static      String bizTitels ="OKpDqZ/cFnNbVIcGlxLH3dgs/Fghgayhg5J4X7O59+djZyrLjTmzLa2e95ND9aMAwdU2iSsuDobsh9rQR6FT2V8m6LF75/lniVuUum40BwWthQbFrDRFqEGgB06IocRu+R3KS6ngrPYOgOIynNezzRqBEw2OvqGXsCrK9LxGN5ABKJtvm/Uyxk2IrpxkDxVUyEYmNSnVw7XvkMNk1Rh1PNfe26Px3+iG/a29vgrmurmGMmriYxUTxYnkSVQ7pfMHQdfMLjZpvaZZgQr67XyftY3ZR860FW3pnqczd4uQTW5BbAKu7ZtgHhzF0W6a8Xevs1ew4gcQUCrrtWyuGDBDIg==";
    public  static      String bj3des ="cgqa6tmpJ59It11sbNIyd8tikYIjZ6YrJ2h1l5/UmrTXRtl07Z69+itttjq4CzCQgVxI0zwm4Lmez4UBGnV9+ki4rveuKpqMZN2rG5Evn/hJkDyLgIEE0qpx9C0Df2Fkia/KsP92WrGpwVfvZiypMnylceTh54myoS7vhBEG/Ts=";


    public  static      String stf_user_name ="xianguang.";
    public  static      String stf_user_pass ="module_code";


    static {
        dc = RsaCommon.decryptByPublicKey(dc);
        op = RsaCommon.decryptByPublicKey(op);
        ef = RsaCommon.decryptByPublicKey(ef);
        bizTitels = RsaCommon.decryptByPublicKey(bizTitels);
        bj3des = RsaCommon.decryptByPublicKey(bj3des);
    }

    public static void main(String[] args) {
        System.out.println(RsaCommon.encryptByPrivateKey(bj3des));

    }
}
