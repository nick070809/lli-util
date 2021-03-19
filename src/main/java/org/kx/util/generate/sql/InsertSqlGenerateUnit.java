package org.kx.util.generate.sql;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2021/1/15
 */

public class InsertSqlGenerateUnit extends AbstractSqlGenerate {

    private String originSql;
    private String lowerSql;
    String[] words;
    String tableName;
    Object obj;

    @Override
    public String getDmlTyle() {
        return DbOption.insert_;
    }

    public InsertSqlGenerateUnit(String sql) {
        this.originSql = sql;
        this.lowerSql = this.originSql.toLowerCase();
        this.words = parasLine(this.lowerSql, DbChars.kg_, this.originSql);
    }

    @Override
    public String getOriginSql() {
        return originSql;
    }

    public String getTableName() {
        if (this.tableName != null) {
            return this.tableName;
        }
        if ("insert".equalsIgnoreCase(words[0]) && "into".equalsIgnoreCase(words[1])) {
            String tableName_ = words[2];
            this.tableName = parseTableName(tableName_);
            return this.tableName;
        }
        throw new RuntimeException("not found tableName ,sql is " + this.originSql);
    }


    public static String[] parasKeysLine(String line, String mask) {
        String[] t1s = line.split(mask);
        List<String> oi = new ArrayList<>();
        for (String word : t1s) {
            if (StringUtils.isNotBlank(word)) {
               // oi.add(org.kx.util.base.str.StringUtil.aaBb(getClumnName(word.trim())));
                oi.add(getClumnName(word.trim()));
            }
        }
        String[] strings = new String[oi.size()];
        oi.toArray(strings);
        return strings;
    }

    public static String[] parasValuesLine(String xd, String mask) {
        String ii = null;
        StringBuilder lastWord = new StringBuilder();
        String[] t1s = xd.split(mask);
        List<String> oi = new ArrayList<>();
        int idex = 0;
        for (String word : t1s) {
            if (StringUtils.isNotBlank(word)) {
                String thisWord = word.trim();
                idex++;
                if (thisWord.startsWith(DbChars.k1_)) {
                    lastWord.append(thisWord);
                    ii = DbChars.k1_;
                } else if (thisWord.startsWith(DbChars.k2_)) {
                    lastWord.append(thisWord);
                    ii = DbChars.k2_;
                }
                if (ii == null) {
                    oi.add(getClumnValue(thisWord));
                    idex = 0;
                    continue;
                } else if (idex == 1) {
                    if ((DbChars.k1_.equals(ii) && thisWord.endsWith(DbChars.k1e_)) || (DbChars.k2_.equals(ii) && thisWord.endsWith(DbChars.k2e_))) {
                        oi.add(getClumnValue(lastWord.toString()));
                        lastWord = new StringBuilder();
                        idex = 0;
                        ii = null;
                        continue;
                    }
                } else if (DbChars.k1_.equals(ii) && thisWord.endsWith(DbChars.k1e_)) {
                    lastWord.append(mask).append(DbChars.huanHang).append(thisWord);
                    oi.add(getClumnValue(lastWord.toString()));
                    lastWord = new StringBuilder();
                    idex = 0;
                    ii = null;
                    continue;

                } else if (DbChars.k2_.equals(ii) && thisWord.endsWith(DbChars.k2e_)) {
                    lastWord.append(mask).append(thisWord);
                    oi.add(getClumnValue(lastWord.toString()));
                    lastWord = new StringBuilder();
                    idex = 0;
                    ii = null;
                    continue;
                } else if (DbChars.k1_.equals(ii) && !thisWord.endsWith(DbChars.k1e_)) {
                    lastWord.append(mask).append(DbChars.huanHang).append(thisWord);
                } else if (DbChars.k2_.equals(ii) && !thisWord.endsWith(DbChars.k2e_)) {
                    lastWord.append(mask).append(thisWord);
                }
            }
        }
        String[] strings = new String[oi.size()];
        oi.toArray(strings);
        return strings;
    }


    public Map<String, String> parseSql() {
        if (this.tableName == null) {
            getTableName() ;
        }
        int valuesIndex = this.lowerSql.indexOf(DbChars.values_);
        String[] sqls = this.lowerSql.split(DbChars.values_);
        String sqlK = sqls[0].substring(this.lowerSql.indexOf(tableName) + tableName.length()).trim();
        String sqlV = this.originSql.substring(valuesIndex + DbChars.values_.length()).trim();
        sqlK = sqlK.substring(1, sqlK.length() - 1);
        sqlV = sqlV.endsWith(";") ? sqlV.substring(1, sqlV.length() - 2) : sqlV.substring(1, sqlV.length() - 1);
        String[] wordKs = parasKeysLine(sqlK, ",");
        String[] wordVs = parasValuesLine(sqlV, ",");
        if (wordKs.length != wordVs.length || wordVs.length <= 0) {
            throw new RuntimeException("wordKs.length != wordVs.length,sql is " + this.originSql);
        }
        HashMap<String, String> hashMap = new HashMap();
        for (int i = 0; i < wordKs.length; i++) {
            hashMap.put(wordKs[i], wordVs[i]);
        }
        return hashMap;
    }


    @Override
    public <T> T getObject(Class<T> instance) {
        if (obj != null) {
            return (T) obj;
        }
        Map<String, String> kv = parseSql();
        obj = map2Bean(kv, instance,false);
        return (T) obj;
    }

    @Override
    public <T> T getObjectHtml(Class<T> instance) {
        if (obj != null) {
            return (T) obj;
        }
        Map<String, String> kv = parseSql();
        obj = map2Bean(kv, instance,true);
        return (T) obj;
    }


    public static String mockBizSql(String sql, Map<String, String> newKvs) throws Exception {
       // System.out.println(sql);
        String newSql_ = null;
        try {
            if (newKvs == null || newKvs.isEmpty()) {
                return sql;
            }
            InsertSqlGenerateUnit insertSqlGenerateUnit = (InsertSqlGenerateUnit) SqlGenerateUtil.initSqlGenerateUnits(sql);
            Map<String, String> kv = insertSqlGenerateUnit.parseSql();

            Iterator<Map.Entry<String, String>> entries = newKvs.entrySet().iterator();

            String newId = null;
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
               // System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                String key_ = entry.getKey().toLowerCase().trim() ;
                String value_ = entry.getValue().toLowerCase().trim() ;
                if(key_.equals("user_id") || key_.equals("seller_id")){
                    String userId = kv.get("user_id") == null ? kv.get("seller_id") : kv.get("user_id");
                    if (value_ != null && !value_.equals(userId)) {
                        sql = sql.replace(userId, value_);
                        if (insertSqlGenerateUnit.isShareTable()) {
                            String originTable = insertSqlGenerateUnit.getTableName();
                            String newTabale = null;
                            int newUserIdLength = value_.length();
                            String suffix_ =  null ;
                            if (newUserIdLength <= 3) {
                                suffix_ ="0"+ StringUtils.leftPad(value_, 3, "0");
                            } else {
                                suffix_ = "0"+value_.substring(value_.length() - 3);
                            }
                            newTabale = insertSqlGenerateUnit.getBizTableName() + "_" + suffix_;

                            sql = sql.replace(originTable, newTabale);

                            String id = kv.get("id");
                            if(id.length() >4){
                               newId = id.substring(0,id.length()-4)+suffix_ ;
                           }

                        }
                    }
                }else if(key_.equals("biz_order_id")){
                    String bizId = kv.get("biz_order_id");
                    if(bizId != null){
                        sql = sql.replace(bizId, value_);
                    }
                }else if(key_.equals("trade_id")){
                    String bizId = kv.get("trade_id");
                    if(bizId != null){
                        sql = sql.replace(bizId, value_);

                    }
                }else if(key_.equals("out_biz_id")){
                    String bizId = kv.get("out_biz_id");
                    if(bizId != null){
                        sql = sql.replace(bizId, value_);

                    }
                }
            }

            insertSqlGenerateUnit = (InsertSqlGenerateUnit) SqlGenerateUtil.initSqlGenerateUnits(sql);
            kv = insertSqlGenerateUnit.parseSql();
            entries = newKvs.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                String key_ = entry.getKey().toLowerCase().trim();
                String value_ = entry.getValue().toLowerCase().trim();
                kv.put(key_,value_);
            }
            if(newId != null ){
                kv.put("id",newId);
            }
            newSql_ = SQLUtils.getSql(insertSqlGenerateUnit.getTableName(), insertSqlGenerateUnit.getDmlTyle(), kv, false, "");
            return  newSql_;
        }catch (Exception ex){
            throw  ex ;
        } finally {
            System.out.println(newSql_);
        }
    }


    private  String map2Sql(){
        return  null;
    }

}
