package org.kx.util.common.db;

import org.kx.util.FileUtil;
import org.kx.util.base.StringUtil;
import org.kx.util.generate.ObjectGenerate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunkx on 2017/5/17.
 * 数据操作语言(DML)，例如：INSERT（插入）、UPDATE（修改）、DELETE（删除）语句。
 * 数据查询语言(DQL)，例如：SELECT语句。（一般不会单独归于一类，因为只有一个语句
 * 数据控制语言(DCL)，例如：GRANT、REVOKE等语句。
 * 事务控制语句（TCL），例如：COMMIT、ROLLBACK等语句。
 */
public class MakeDMLCompent extends DBCompent {

     private Logger logger = LoggerFactory.getLogger(MakeDMLCompent.class);

    private String typeName;    //org.nick.web.model.Aaa
    private String typeSimpleName;    //Aaa
    private String absoluteModulePath;
    private String tableName;  //表名
    private String lowerstandardName;
    private String daoPath;    //
    private String daoName;
    private String daoPackgeName;
    private String daoPackgeSimplrName;

    //private String moduleName;
    private String modulePath;  //org.nick.web

    private String iServiceName;
    private String serviceName;
    private List<ModelFiled> list = new ArrayList<>();

    //private String id;
    private String idType;
    private String comunId; //主键的表字段名称
    private Class clazz;
    private String primary;  //主键属性名称
    private String second;  //非主键属性名称
    private String standardName ;
    //Connection conn = null;
    ModelWapper modelWapper = null;





    public MakeDMLCompent analy(Class clazz, String srcPath, String daoMark, NameRule nameRule, NameProfix nameProfix) throws SQLException {
         if (this.modelWapper == null) {
            modelWapper = getModelWapper(DbType.MYSQL, clazz, nameRule, nameProfix);
            //hrow  new BaseException(ErrorCode.SystemError,"modelWapper is null");
        }
        this.clazz = clazz;
        //获取类名称
        typeName = clazz.getName();
        
        //获取类简名称
        typeSimpleName = clazz.getSimpleName();

        if(typeName.endsWith("TO") || typeName.endsWith("VO") || typeName.endsWith("To") || typeName.endsWith("Vo")|| typeName.endsWith("VO") || typeName.endsWith("Vo")){
            standardName = typeSimpleName.substring(0,typeSimpleName.length()-2);
        }else if(typeName.endsWith("DTO") || typeName.endsWith("DTo")){
            standardName = typeSimpleName.substring(0,typeSimpleName.length()-3);
        }else {
            standardName = typeSimpleName;
        }
        
        
        //获取类的绝对路径
        absoluteModulePath = System.getProperty("user.dir") + "/" + srcPath + "/" + getSafeModulePath(typeName).replace(".", "/");
        //获取表名称
        tableName = modelWapper.getTableName();
        //获取service前缀
        //if(StringUtils.isNotBlank(deletePre))standardName= StringUtils.stripStart(typeSimpleName, deletePre);
        lowerstandardName = StringUtil.LowerFirstWord(standardName);
        //首字母小写
        modulePath = getSafeModulePath(typeName);
        //获取dao
        if (daoMark.startsWith("map")) {
            daoPackgeSimplrName = "mapping";
            daoName = standardName + "Mapper";
            daoPackgeName = modulePath + ".mapping";
            daoPath = absoluteModulePath + "/mapping/" + daoName + ".java";
        } else if (daoMark.startsWith("dal")) {
            daoPackgeSimplrName = "dal";
            daoName = standardName + "Mapper";
            daoPackgeName = modulePath + ".dal";
            daoPath = absoluteModulePath + "/dal/" + daoName + ".java";
        } else {
            daoPackgeSimplrName = "dao";
            daoName = standardName + "Dao";
            daoPackgeName = modulePath + ".dao";
            daoPath = absoluteModulePath + "/dao/" + daoName + ".java";
        }

        //获取Service
        iServiceName = "I" + standardName + "Service";
        serviceName = standardName + "ServiceImpl";
        list = modelWapper.getModelFileds();
        primary = modelWapper.getIdName();  //主键
        idType = list.get(0).getJavaType();
        comunId = list.get(0).getClomunName(); //主键表字段
        second = modelWapper.getSecond();
        return this;
    }

    private String getSafeModulePath(String typeName) {
        return typeName.replace("." + typeSimpleName, "").replace(".vo", "")
                .replace(".pojo", "")
                .replace(".model", "")
                .replace(".javabeans", "")
                .replace(".javabean", "")
                .replace(".domains ", "")
                .replace(".domain", "");

    }

    public MakeDMLCompent makeXmlFile() throws IOException {
        String xmlPath = (absoluteModulePath + "/" + daoPackgeSimplrName + "/" + daoName).replace(".", "/") + ".xml";
        File file = FileUtil.createFile(xmlPath);
        FileUtil.writeStringToFile(makeXmlContent(), xmlPath);
        System.out.println("created file " + file.getAbsolutePath());
        return this;
    }

    public MakeDMLCompent makeMapperFile() throws IOException {
        String mapperPath = (absoluteModulePath + "/" + daoPackgeSimplrName + "/" + daoName).replace(".", "/") + ".java";
        File file = FileUtil.createFile(mapperPath);
        FileUtil.writeStringToFile(makeMapperContent(), mapperPath);
        System.out.println("created file " + file.getAbsolutePath());
        return this;
    }

    public MakeDMLCompent makeIServiceFile() throws IOException {
        String iServicePath = (absoluteModulePath + "/service/" + iServiceName).replace(".", "/") + ".java";
        FileUtil.createFile(iServicePath);
        FileUtil.writeStringToFile(makeIServiceContent(), iServicePath);
        return this;
    }

    public MakeDMLCompent makeServiceImplFile() throws IOException {
        String serviceImplPath = (absoluteModulePath + "/service/impl/" + serviceName).replace(".", "/") + ".java";
        FileUtil.createFile(serviceImplPath);
        FileUtil.writeStringToFile(makeServiceImplContent(), serviceImplPath);
        return this;
    }

    public MakeDMLCompent makeOracleServiceFile() throws IOException {
        String oracleServicePath = (absoluteModulePath + "/service/oracle/Oracle" + standardName + "Service").replace(".", "/") + ".java";
        FileUtil.createFile(oracleServicePath);
        FileUtil.writeStringToFile(makeOracleServiceContent(), oracleServicePath);
        return this;
    }

    public MakeDMLCompent makeRedisServiceFile() throws IOException {
        String redisServicePath = (absoluteModulePath + "/service/redis/Redis" + standardName + "Service").replace(".", "/") + ".java";
        FileUtil.createFile(redisServicePath);
        FileUtil.writeStringToFile(makeRedisServiceContent(), redisServicePath);
        return this;
    }

    public MakeDMLCompent makeControllerFile() throws IOException {
        String ontrallerPath = (absoluteModulePath + "/controller/" + standardName + "Controller").replace(".", "/") + ".java";
        FileUtil.createFile(ontrallerPath);
        FileUtil.writeStringToFile(makeControllerContent(), ontrallerPath);
        return this;
    }


    private String makeXmlContent() {
        String resultMapid = standardName + "ResultMap";
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n");
        sb.append("  <mapper namespace=\"" + daoPackgeName + "." + daoName + "\">\n");
        sb.append("    <resultMap id=\"" + resultMapid + "\" type=\"" + typeName + "\" >\n");
        for (ModelFiled f : list) {
            sb.append("      <result column=\"" + f.getClomunName() + "\" property=\"" + f.getFieldName() + "\" jdbcType=\"" + f.getJdbcType() + "\" />\n");
        }
        sb.append("    </resultMap>\n\n");

        //////////////////// column_List
        sb.append("    <sql id=\"column_List\">\n");
        sb.append("      " + modelWapper.filedsToStirng() + "\n     </sql>\n\n");


        //////////////////// where_Clause
        sb.append("    <sql id=\"where_Clause\" >\n");
        sb.append("      <trim prefix=\"WHERE\" prefixOverrides=\"AND|OR\">\n");
      //  sb.append("         1 =1 \n");
        for (ModelFiled f : list) {
            if (f.getJdbcType().equals("VARCHAR")) {
                sb.append("        <if test=\"" + f.getFieldName() + " != null and " + f.getFieldName() + " != ''\">\n");
            } else {

                sb.append("        <if test=\"" + f.getFieldName() + " != null\">\n");
            }
            sb.append("          and " + f.getClomunName() + " = #{" + f.getFieldName() + ",jdbcType=" + f.getJdbcType() + "}\n");
            sb.append("        </if>\n");
        }
        sb.append("      </trim>\n");
        sb.append("    </sql>\n\n");

        //////////////////// insert
        sb.append("    <!--mysql useGeneratedKeys keyProperty  primary 为java属性 -->\n");
        sb.append("    <!-- 如果id为自增长，需要屏蔽主键行 -->\n");
        sb.append("    <insert id=\"insert\"   useGeneratedKeys=\"true\" keyProperty=\""+ primary + "\" parameterType=\"" + typeName + "\">\n");
        //TODO 不依赖数据库主键
        ///sb.append("      <!-- 依靠是序列还是uuid删减 ,默认第一个属性为主键-->\n");
        //sb.append("      <selectKey resultType=\"java.lang.Long\" order=\"BEFORE\" keyProperty=\""+id+"\">\n");
        //sb.append("          <![CDATA[select "+tableName+"_SEQ.nextval AS "+id+" from DUAL]]>\n");
        // sb.append("      </selectKey>\n");
        //  sb.append("      <!-- 防止列名顺序被调整，不用include -->\n");
        sb.append("      insert into " + tableName + "\n");
        sb.append("        <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >\n");
        for (ModelFiled f : list) {
            if(f.getFieldName().equals(primary)){ //<!--
                sb.append("          <!--<if test=\"" + f.getFieldName() + " != null\">" + f.getClomunName() + ",</if>-->\n");
            }else {
                sb.append("          <if test=\"" + f.getFieldName() + " != null\">" + f.getClomunName() + ",</if>\n");
            }
        }
        sb.append("        </trim>\n        values\n        <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >\n");
        for (ModelFiled f : list) {
            if(f.getFieldName().equals(primary)){ //<!--
                sb.append("          <!--<if test=\"" + f.getFieldName() + " != null\">#{" + f.getFieldName() + ",jdbcType=" + f.getJdbcType() + "},</if>-->\n");
            }else {
                sb.append("          <if test=\"" + f.getFieldName() + " != null\">#{" + f.getFieldName() + ",jdbcType=" + f.getJdbcType() + "},</if>\n");
            }

        }
        sb.append("        </trim>\n      </insert>\n\n");


        //////////////////// insertList
        sb.append("    <!-- 如果id为自增长，需要屏蔽主键行 -->\n");
        sb.append("    <insert id=\"insertList\" parameterType=\"java.util.List\">\n");
        sb.append("          INSERT INTO "+tableName+" ( \n");

        StringBuffer sbf = new StringBuffer();
        for(ModelFiled f:modelWapper.getModelFileds()){
            if(f.getFieldName().equals(primary)){
                sbf.append("<!--"+f.getClomunName()+",-->");
            }else {
                sbf.append(f.getClomunName()+",");
            }

        }
        String str = sbf.toString();
        String sss =  str.endsWith(",")?str.substring(0, str.length()-1):str;



        //sb.append("               " + modelWapper.filedsToStirng() + "\n ");
        sb.append("               " + sss + "\n ");

        sb.append("           ) \n");
        sb.append("      <foreach collection=\"list\" item=\"item\" index=\"index\" separator=\" union all \"> \n");
        sb.append("          select \n");
        StringBuilder sb1= new StringBuilder();
        for (ModelFiled f : list) {
            if(f.getFieldName().equals(primary)){ //<!--
                sb.append("          <!--#{item."+f.getFieldName()+",jdbcType="+f.getJdbcType()+"}-->\n");
            }else {
                sb1.append("          #{item."+f.getFieldName()+",jdbcType="+f.getJdbcType()+"},\n");
            }

        }
        String sb1s = sb1.toString();
        if(sb1s.endsWith(",\n")) sb.append(sb1s.substring(0,sb1s.length()-2));
        sb.append("\n           from dual\n ");
        sb.append("       </foreach>\n");
        sb.append("    </insert> \n\n");
        //////////////////// deleteById
        sb.append("    <delete id=\"deleteById\" >\n");
        sb.append("      delete from " + tableName + " where " + comunId + " = #{" + primary + "} \n");
        sb.append("    </delete> \n\n");
        ////////////////////delete
        //TODO 危险操作不提供
        // sb.append("    <delete id=\"delete\" >\n");
        // sb.append("      delete from "+tableName+" where <include refid=\"where_Clause\" />\n");
        //  sb.append("    </delete> \n\n");

        //////////////////// update
        sb.append("    <update id=\"update\" parameterType=\"" + typeName + "\">\n");
        sb.append("      update " + tableName + "\n");
        sb.append("        <trim prefix=\"SET\" suffixOverrides=\",\">  \n");
        //sb.append("      <!-- 主键不要求修改 -->  \n");
        for (ModelFiled f : list) {
            //TODO 主键不更新
            if (f.getFieldName().equals(primary)) continue;
            //TODO  创建时间不更新
            if(f.getFieldName().equalsIgnoreCase("createTime"))continue;
            if(f.getFieldName().equalsIgnoreCase("createdTime"))continue;
            if(f.getFieldName().equalsIgnoreCase("createAt"))continue;
            if(f.getFieldName().equalsIgnoreCase("createBy"))continue;
            if(f.getFieldName().equalsIgnoreCase("creator"))continue;
            sb.append("        <if test=\"" + f.getFieldName() + " != null\">\n");
            sb.append("             " + f.getClomunName() + " = #{" + f.getFieldName() + ",jdbcType=" + f.getJdbcType() + "},\n");
            sb.append("        </if>\n");
        }
        sb.append("        <!--自定义主键-->\n");
        sb.append("        </trim>\n      where  " + comunId + "= #{" + primary + "}\n ");
        sb.append("    </update>\n\n ");

        //////////////////// get
        sb.append("    <select id=\"get\"  parameterType=\"" + typeName + "\"  resultMap=\"" + resultMapid + "\">\n");
        sb.append("        select <include refid=\"column_List\" /> from " + tableName + " <include refid=\"where_Clause\" />\n ");
        sb.append("    </select>\n\n ");

        sb.append("    <select id=\"getById\"  parameterType=\"" + idType + "\"  resultMap=\"" + resultMapid + "\">\n");
        sb.append("        select <include refid=\"column_List\" /> from " + tableName + " where "+ comunId + " =  #{" + primary + "}\n ");
        sb.append("    </select>\n\n ");


        sb.append("    <sql id=\"orderKey\">\n");
        sb.append("        ORDER BY "+primary+" desc\n");
        sb.append("    </sql>\n\n");


        /*sb.append("        <!-- 分页头 -->\n");
        sb.append("        <sql id=\"pagnationBegin\"> \n");
        sb.append("                 SELECT *\n");
        sb.append("                 FROM (SELECT A.*, ROWNUM RN\n");
        sb.append("                 FROM (\n");
        sb.append("        </sql>\n\n");
        sb.append("         <!-- 分页尾 -->\n");
        sb.append("        <sql id=\"pagnationEnd\">\n");
        sb.append("            ) A\n");
        sb.append("            <if test=\" pageSize != null and pageSize != 0\">\n");
        sb.append("                 WHERE ${pageSize*(pageNum)} >= ROWNUM\n");
        sb.append("            </if>\n");
        sb.append("            )\n");
        sb.append("           <if test=\" pageSize != null and pageSize != 0\">\n");
        sb.append("                 WHERE RN >= ${pageSize*(pageNum-1)+1}\n");
        sb.append("            </if>\n");
        sb.append("        </sql>\n\n");

        */
     //////////////////// getPaging
        //升级
       // sb.append("    <select id=\"getPaging\"  parameterType=\"hashMap\"  resultMap=\"" + resultMapid + "\">\n");
        /*sb.append("    <select id=\"getPaging\"  parameterType=\"" + typeName + "\"   resultMap=\"" + resultMapid + "\">\n");
        sb.append("        <include refid=\"pagnationBegin\"/>\n");
        sb.append("        select\n");
        sb.append("        <include refid=\"column_List\"/>\n");
        sb.append("        from  " + tableName + " \n");
        sb.append("        <include refid=\"where_Clause\"/>\n");
        sb.append("        <include refid=\"orderKey\"/>\n");
        sb.append("        <include refid=\"pagnationEnd\"/>\n");
        sb.append("    </select> \n\n");*/


        //////////////////// getList
        //TODO getList
        sb.append("    <select id=\"getList\"  parameterType=\"" + typeName + "\"  resultMap=\"" + resultMapid + "\">\n");
        sb.append("      select <include refid=\"column_List\" /> from " + tableName + " <include refid=\"where_Clause\" />\n");
        sb.append("      <include refid=\"orderKey\"/>\n");
        sb.append("    </select>\n\n");

        //////////////////// getTotal
        sb.append("    <select id=\"getTotal\" resultType=\"INTEGER\" parameterType=\"" + typeName + "\"> \n ");
        sb.append("      select count(1) from " + tableName + " <include refid=\"where_Clause\" />\n");
        sb.append("    </select>\n\n");
        sb.append("</mapper>");
        return sb.toString();
    }

    private String makeMapperContent() {
        StringBuilder sb = new StringBuilder("package " + daoPackgeName + ";\n\n");
        sb.append("import java.util.List;\n");
        sb.append("import java.util.Map;\n");
        sb.append("import " + typeName + ";\n");
        sb.append("import org.apache.ibatis.annotations.Param;\n");
        sb.append("public interface " + daoName + "{\n\n");
        sb.append("    int insert(" + typeSimpleName + "  " + lowerstandardName + ");\n\n");
        sb.append("    int insertList(List<" + typeSimpleName + ">  list);\n\n");
       // sb.append("    int delete(" + typeSimpleName + "  " + lowerstandardName + ");\n\n");
        sb.append("    int deleteById(@Param(\"" + primary + "\") " + idType + "	" + primary + ");\n\n");
        sb.append("    int update(" + typeSimpleName + "  " + lowerstandardName + ");\n\n");
        sb.append("    " + typeSimpleName + " get(" + typeSimpleName + "  " + lowerstandardName + ");\n\n");
        sb.append("    " + typeSimpleName + " getById(@Param(\"" + primary + "\") " + idType + "	" + primary + ");\n\n");
        //sb.append("    List<" + typeSimpleName + "> getPaging(Map<String, Object> map);\n\n");
        //sb.append("    List<" + typeSimpleName + "> getPaging(" + typeSimpleName + "  " + lowerstandardName + ");\n\n");
        sb.append("    List<" + typeSimpleName + "> getList(" + typeSimpleName + "  " + lowerstandardName + ");\n\n");
        sb.append("    int  getTotal(" + typeSimpleName + "  " + lowerstandardName + ");\n\n");
        sb.append("}");
        return sb.toString();
    }

    private String makeOracleServiceContent() {
        String lowerDaoName = StringUtil.LowerFirstWord(daoName);
        StringBuilder sb = new StringBuilder("package " + modulePath + ".service.oracle;\n\n");
        sb.append("import java.util.List;\n");
        sb.append("import java.util.Map;\n");
        sb.append("import org.apache.log4j.Logger;\n");
        sb.append("import " + daoPackgeName + "." + daoName + ";\n");
        sb.append("import " + typeName + ";\n");
        sb.append("import " + modulePath + ".service." + iServiceName + ";\n");
        sb.append("import org.ali.common.ex.BootExecption;\n");
        sb.append("import org.ali.common.ex.NoServiceExecption;\n");
        sb.append("import org.ali.common.SpringBeanUtil;\n");
        sb.append("import org.ali.common.Pam;\n");
        sb.append("import java.util.HashMap;\n");
        sb.append("import org.springframework.stereotype.Service;\n");
        sb.append("import org.springframework.beans.factory.InitializingBean;\n");
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        sb.append("import org.springframework.beans.factory.annotation.Qualifier;\n\n");
        sb.append("@Service(\"" + lowerstandardName + "Service\")\n");
        sb.append("public class Oracle" + standardName + "Service implements " + iServiceName + " ,InitializingBean{\n\n");
        sb.append("private Logger logger = Logger.getLogger(Oracle" + standardName + "Service.class);\n\n");

        sb.append("@Autowired(required=false)\n");
        sb.append("@Qualifier(\"" + lowerstandardName + "ServiceImpl\")\n");
        sb.append(iServiceName + " " + lowerstandardName + "ServiceImpl;\n\n");

        sb.append("@Autowired(required=false)\n");
        sb.append(daoName + " " + lowerDaoName + ";\n\n");

        sb.append("@Override\n");
        sb.append("public void afterPropertiesSet() throws Exception {\n");
        sb.append("logger.debug(\"" + lowerstandardName + "ServiceImpl is null ?\"+(null == " + lowerstandardName + "ServiceImpl));\n");
        sb.append("	if(null == " + lowerstandardName + "ServiceImpl)" + lowerstandardName + "ServiceImpl = (" + iServiceName + ") SpringBeanUtil.getBean(\"" + lowerstandardName + "ServiceImpl\");\n");
        sb.append("if(null == " + lowerstandardName + "ServiceImpl){\n");
        sb.append("	throw new BootExecption(\"Oracle" + standardName + "Service.init-->can't get a instance of " + lowerstandardName + "ServiceImpl !\");\n");
        sb.append("}\n");

        sb.append("	" + lowerstandardName + "ServiceImpl.set" + standardName + "Service(this);\n");
        sb.append("	logger.debug(\"" + lowerDaoName + "is null ?\"+(null == " + lowerDaoName + "));\n");
        sb.append("	if(null == " + lowerDaoName + ")" + lowerDaoName + " = (" + daoName + ") SpringBeanUtil.getBean(\"" + lowerDaoName + "\");\n");
        sb.append("if(null == " + lowerDaoName + "){\n");
        sb.append("	throw new BootExecption(\"Oracle" + standardName + "Service.init-->can't get a instance of " + lowerDaoName + " !\");\n");
        sb.append("}\n");

        sb.append("	logger.info(\"Oracle" + lowerstandardName + "Service launched success !\");\n\n");
        sb.append("}\n");

        sb.append("@Override\n");
        sb.append("public int insert(" + typeSimpleName + " vo)throws NoServiceExecption {\n");
        sb.append("	return " + lowerDaoName + ".insert(vo);\n");
        sb.append("}\n\n");

        sb.append("@Override\n");
        sb.append("public int insertList(List<" + typeSimpleName + "> list)throws NoServiceExecption {\n");
        sb.append("	return " + lowerDaoName + ".insertList(list);\n");
        sb.append("}\n\n");

        sb.append("@Override\n");
        sb.append("public int update(" + typeSimpleName + " vo)throws NoServiceExecption {\n");
        sb.append("	return " + lowerDaoName + ".update(vo);\n");
        sb.append("}\n\n");

        sb.append("@Override\n");
        sb.append("public int delete(" + typeSimpleName + " vo)throws NoServiceExecption {\n");
        sb.append("return " + lowerDaoName + ".delete(vo);\n");
        sb.append("}\n\n");

        sb.append("@Override\n");
        sb.append("public " + typeSimpleName + " get(" + typeSimpleName + " vo)throws NoServiceExecption {\n");
        sb.append("	return " + lowerDaoName + ".get(vo);\n");
        sb.append("}\n\n");

        sb.append("@Override\n");
        sb.append("public List<" + typeSimpleName + "> getList(" + typeSimpleName + " vo)throws NoServiceExecption {\n");
        sb.append("	return " + lowerDaoName + ".getList(vo);\n");
        sb.append("}\n\n");

        //sb.append("@SuppressWarnings({ \"unchecked\", \"rawtypes\" })\n");
        sb.append("@Override\n");
        sb.append("public List<" + typeSimpleName + "> getListPaging(" + typeSimpleName + " vo,Pam pam)throws NoServiceExecption {\n");
        sb.append("Map<String,Object> map = new HashMap< >();\n");
        sb.append("map.put(\"vo\", vo);\n");
        sb.append("map.put(\"pam\", pam);\n");
        sb.append("return " + lowerDaoName + ".getPaging(map);\n");
        sb.append("}\n\n");

        sb.append("@Override\n");
        sb.append("public int getTotal(" + typeSimpleName + " vo)throws NoServiceExecption{\n");
        sb.append("	return " + lowerDaoName + ".getTotal(vo);\n");
        sb.append("}\n\n");

        sb.append("@Override\n");
        sb.append("public void set" + standardName + "Service(" + iServiceName + " " + lowerstandardName + ")throws NoServiceExecption{\n");
        sb.append("}\n");
        sb.append("}\n");

        return sb.toString();
    }

    private String makeRedisServiceContent() {
        StringBuilder sb = new StringBuilder("package " + modulePath + ".service.redis;\n\n");
        sb.append("import java.util.List;\n");
        //sb.append("import java.util.Map;\n");
        sb.append("import java.util.ArrayList;\n");
        sb.append("import java.util.Set;\n");
        sb.append("import org.apache.log4j.Logger;\n");
        sb.append("import org.ali.common.StringUtil;\n");
        sb.append("import " + typeName + ";\n");
        sb.append("import " + modulePath + ".service." + iServiceName + ";\n");
        sb.append("import org.ali.common.ex.BootExecption;\n");
        sb.append("import org.ali.common.ex.NoServiceExecption;\n");
        sb.append("import org.nici.core.redis.RedisService;\n");
        sb.append("import org.ali.common.SpringBeanUtil;\n");
        sb.append("import org.ali.common.Pam;\n");
        sb.append("import java.util.Collections;\n");
        sb.append("import org.springframework.stereotype.Service;\n");
        sb.append("import org.springframework.beans.factory.InitializingBean;\n");
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        sb.append("import org.springframework.beans.factory.annotation.Qualifier;\n\n");

        sb.append("@Service(\"" + lowerstandardName + "Service\")\n");
        sb.append("public class Redis" + standardName + "Service implements " + iServiceName + " ,InitializingBean{\n\n");
        sb.append("private Logger logger = Logger.getLogger(Redis" + standardName + "Service.class);\n\n");
        sb.append("@SuppressWarnings(\"rawtypes\")\n");
        sb.append("@Autowired(required=false)\n"); /*<"+typeSimpleName+">*/
        sb.append("RedisService  redisService;\n\n");

        sb.append("@Autowired(required=false)\n");
        sb.append("@Qualifier(\"" + lowerstandardName + "ServiceImpl\")\n");
        sb.append(iServiceName + " " + lowerstandardName + "ServiceImpl;\n\n");

        sb.append("@SuppressWarnings({ \"rawtypes\", \"unchecked\" })\n");
        sb.append("@Override\n");
        sb.append("public void afterPropertiesSet() throws Exception {\n");
        sb.append("	logger.debug(\"redisService is null ?\"+(null == redisService));\n");
        sb.append("	if(null == redisService)redisService = (RedisService) SpringBeanUtil.getBean(\"redisService\");\n");
        sb.append("if(null == redisService){\n");
        sb.append("	throw new BootExecption(\"Redis" + standardName + "Service.init-->can't get a instance of redisService !\");\n");
        sb.append("}\n");


        sb.append("logger.debug(\"" + lowerstandardName + "ServiceImpl is null ?\"+(null == " + lowerstandardName + "ServiceImpl));\n");
        sb.append("	if(null == " + lowerstandardName + "ServiceImpl)" + lowerstandardName + "ServiceImpl = (" + iServiceName + ") SpringBeanUtil.getBean(\"" + lowerstandardName + "ServiceImpl\");\n");
        sb.append("if(null == " + lowerstandardName + "ServiceImpl){\n");
        sb.append("	throw new BootExecption(\"Redis" + standardName + "Service.init-->can't get a instance of " + lowerstandardName + "ServiceImpl !\");\n");
        sb.append("}\n");

        sb.append("	" + lowerstandardName + "ServiceImpl.set" + standardName + "Service(this);\n");
        sb.append("	logger.info(\"Redis" + lowerstandardName + "Service launched success !\");\n\n");
        sb.append("}\n");
        sb.append("@Override\n");
        sb.append("public int insert(" + typeSimpleName + " vo)throws  NoServiceExecption {\n");
        sb.append("// TODO vo.set****Id(Long.parseLong(StringUtil.generateTransID()));\n");
        sb.append("	return redisService.putKV(vo);\n");
        sb.append("}\n\n");

        //sb.append("@SuppressWarnings({ \"rawtypes\", \"unchecked\" })\n");
        sb.append("@Override\n");
        sb.append("public int insertList(List<" + typeSimpleName + "> list)throws  NoServiceExecption {\n");
        sb.append("	return redisService.putListKV(list);\n");
        sb.append("}\n\n");

        sb.append("@Override\n");
        sb.append("public int update(" + typeSimpleName + " vo)throws NoServiceExecption {\n");
        sb.append("	return redisService.putKV(vo);\n");
        sb.append("}\n\n");

        sb.append("@Override\n");
        sb.append("public int delete(" + typeSimpleName + " vo) {\n");
        sb.append("return redisService.delete(vo);\n");
        sb.append("}\n\n");

        sb.append("@Override\n");
        sb.append("public " + typeSimpleName + " get(" + typeSimpleName + " vo)throws  NoServiceExecption {\n");
        sb.append("	return  redisService.getOnlyKV(vo);\n");
        sb.append("}\n\n");

        //sb.append("@SuppressWarnings({ \"rawtypes\" })\n");
        sb.append("@Override\n");
        sb.append("public List<" + typeSimpleName + "> getList(" + typeSimpleName + " vo)throws  NoServiceExecption {\n");
        sb.append("	return redisService.getListKV(vo);\n");
        sb.append("}\n\n");

        //sb.append("@SuppressWarnings({\"rawtypes\" })\n");
        sb.append("@Override\n");
        sb.append("public List<" + typeSimpleName + "> getListPaging(" + typeSimpleName + " vo,Pam pam)throws  NoServiceExecption {\n");
        sb.append("String key = vo.getRedisKey();\n");
        sb.append("Set<String> allKey = redisService.listKey(key);\n");
        sb.append("if(allKey ==null || allKey.isEmpty()) return null;\n");
        sb.append("List<String> list = new ArrayList<>(allKey);\n");
        sb.append("list.sort((k1,k2)->(StringUtil.getId(k1)).compareTo(StringUtil.getId(k2)));\n");

        sb.append("if (pam.getSortOrder().equalsIgnoreCase(\"desc\")) {\n");
        sb.append("	Collections.reverse(list);\n");
        sb.append("}\n");


        sb.append("List<String> keys = new ArrayList<>();\n");
        sb.append("// list分页\n");
        sb.append("if (pam.getPageIndex() != 0 && pam.getPageSize() != 0) {\n");
        sb.append("	int pageIndex = pam.getPageIndex();\n");
        sb.append("	int pageSize = pam.getPageSize();\n");
        sb.append("	int startn = (pageIndex - 1) * pageSize + 1;\n");
        sb.append("	int endn = pageIndex * pageSize;\n");
        sb.append("	for(int i= startn-1;i<endn && i<list.size();i++){\n");
        sb.append("		keys.add(list.get(i));\n");
        sb.append("	}\n");
        sb.append("}\n");
        sb.append("return redisService.getListPagingKV(keys," + typeSimpleName + ".class);\n");
        sb.append("}\n\n");

        sb.append("@Override\n");
        sb.append("public int getTotal(" + typeSimpleName + " vo)throws  NoServiceExecption {\n");
        sb.append("	return redisService.getTotal(vo);\n");
        sb.append("}\n\n");

        sb.append("@Override\n");
        sb.append("public void set" + standardName + "Service(" + iServiceName + " " + lowerstandardName + ")throws NoServiceExecption {\n");
        sb.append("}\n");
        sb.append("}\n");

        return sb.toString();

    }

    private String makeServiceImplContent0() {
        StringBuilder sb = new StringBuilder("package " + modulePath + ".service.impl;\n\n");
        sb.append("import java.util.List;\n");
        sb.append("import java.util.Map;\n");
        sb.append("import org.ali.common.ex.NoServiceExecption;\n");
        //sb.append("import org.apache.log4j.Logger;\n");
        sb.append("import " + typeName + ";\n");
        sb.append("import " + modulePath + ".service." + iServiceName + ";\n");
        sb.append("import org.ali.common.Pam;\n");
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        sb.append("import org.springframework.stereotype.Service;\n");
        sb.append("import org.springframework.beans.factory.annotation.Qualifier;\n\n");

        sb.append("@Service(\"" + lowerstandardName + "ServiceImpl\")\n");
        sb.append("public class " + standardName + "ServiceImpl implements " + iServiceName + " {\n\n");
        //sb.append("private Logger logger = Logger.getLogger("+standardName+"ServiceImpl.class);\n\n");

        sb.append("@Autowired(required=false)\n");
        sb.append("@Qualifier(\"" + lowerstandardName + "Service\")\n");
        sb.append(iServiceName + "  " + lowerstandardName + "Service;\n");

        sb.append("@Override\n");
        sb.append("public int insert(" + typeSimpleName + " vo)throws NoServiceExecption {\n");
        sb.append("  check" + standardName + "Service();\n");
        sb.append("	return " + lowerstandardName + "Service.insert(vo);\n");
        sb.append("}\n");

        //sb.append("@SuppressWarnings({ \"rawtypes\", \"unchecked\" })\n");
        sb.append("@Override\n");
        sb.append("public int insertList(List<" + typeSimpleName + "> list)throws  NoServiceExecption {\n");
        sb.append("  check" + standardName + "Service();\n");
        sb.append("	return " + lowerstandardName + "Service.insertList(list);\n");
        sb.append("}\n");

        sb.append("@Override\n");
        sb.append("public int update(" + typeSimpleName + " vo)throws   NoServiceExecption {\n");
        sb.append("  check" + standardName + "Service();\n");
        sb.append("	return " + lowerstandardName + "Service.update(vo);\n");
        sb.append("}\n");

        sb.append("@Override\n");
        sb.append("public int delete(" + typeSimpleName + " vo)throws  NoServiceExecption {\n");
        sb.append("  check" + standardName + "Service();\n");
        sb.append("	return " + lowerstandardName + "Service.delete(vo);\n");
        sb.append("}\n\n");

        sb.append("@Override\n");
        sb.append("public " + typeSimpleName + " get(" + typeSimpleName + " vo)throws  NoServiceExecption {\n");
        sb.append("  check" + standardName + "Service();\n");
        sb.append("return (" + typeName + ")" + lowerstandardName + "Service.get(vo);\n");
        sb.append("}\n");

        //sb.append("@SuppressWarnings({ \"rawtypes\", \"unchecked\" })\n");
        sb.append("@Override\n");
        sb.append("public List<" + typeSimpleName + "> getList(" + typeSimpleName + " vo)throws  NoServiceExecption {\n");
        sb.append("  check" + standardName + "Service();\n");
        sb.append("return " + lowerstandardName + "Service.getList(vo);\n");
        sb.append("}\n");

        //sb.append("@SuppressWarnings({ \"rawtypes\", \"unchecked\" })\n");
        sb.append("@Override\n");
        sb.append("public List<" + typeSimpleName + "> getListPaging(" + typeSimpleName + " vo,Pam pam)throws  NoServiceExecption {\n");
        sb.append("  check" + standardName + "Service();\n");
        sb.append("	return " + lowerstandardName + "Service.getListPaging(vo,pam);\n");
        sb.append("}\n");

        sb.append("@Override\n");
        sb.append("public int getTotal(" + typeSimpleName + " vo)throws NoServiceExecption {\n");
        sb.append("  check" + standardName + "Service();\n");
        sb.append("	return " + lowerstandardName + "Service.getTotal(vo);\n");
        sb.append("}\n");

        sb.append("@Override\n");
        sb.append("public void set" + standardName + "Service(" + iServiceName + " " + lowerstandardName + "Service)");
        sb.append("throws   NoServiceExecption{\n");
        sb.append("if(" + lowerstandardName + "Service == null) throw new NoServiceExecption(\"" + lowerstandardName + "Service is null\");\n");
        sb.append("	this." + lowerstandardName + "Service = " + lowerstandardName + "Service;\n");
        sb.append("}\n");

        sb.append("public void check" + standardName + "Service()");
        sb.append("throws   NoServiceExecption{\n");
        sb.append("if(this." + lowerstandardName + "Service == null) throw new NoServiceExecption(\"this." + lowerstandardName + "Service is null\");\n");
        sb.append("}\n");

        sb.append("}\n");
        return sb.toString();

    }


    private String makeServiceImplContent() {
        StringBuilder sb = new StringBuilder("package " + modulePath + ".service.impl;\n\n");
        sb.append("import " + typeName + ";\n");
        sb.append("import " + modulePath + ".service." + iServiceName + ";\n");
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        sb.append("import org.springframework.stereotype.Service;\n");

        sb.append("@Slf4j\n");
        sb.append("@Service(\"" + lowerstandardName + "Service\")\n");
        sb.append("public class " + standardName + "ServiceImpl extends BaseService implements " + iServiceName + " {\n\n");
        //sb.append("private Logger logger = Logger.getLogger("+standardName+"ServiceImpl.class);\n\n");

        sb.append("    @Autowired\n");
        String mapper = daoName.substring(0,1).toLowerCase() +daoName.substring(1);
        sb.append("    " + daoName + " " + mapper+ ";\n\n");


        //sb.append("    /** \n");
        //sb.append("     * add适用于新增\n");
        //sb.append("     */\n");

        sb.append("    public RichResult<Integer>  add"+standardName+"( "+typeSimpleName+" dto) {\n");
        sb.append("        try {\n");
        sb.append("            beforeAdd(dto);\n");
        sb.append("            return new RichResult("+mapper+".insert(dto));\n");
        sb.append("          } catch (Exception e) {\n");

        sb.append("            log.error(\"新增失败,信息:{}-系统异常,原因:{}\" , JSON.toJSONString(dto) , e);\n");
        sb.append("            return format(e);\n");
        sb.append("          }\n");

        sb.append("    }\n\n");


        sb.append("    public RichResult<"+typeSimpleName+">  get"+standardName+"( Long id) {\n");
        sb.append("        try {\n");
        sb.append("            return new RichResult("+mapper+".getById(id));\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            log.error(\"获取数据查询失败,主键:{}-系统异常,原因:{}\" , id , e);\n");
        sb.append("            return format(e);\n");
        sb.append("        }\n");

        sb.append("    }\n\n");



        sb.append("    public RichResult<PageInfo>  query"+standardName+"( "+typeSimpleName+" dto) {\n");
        sb.append("        try {\n");
        sb.append("            beforeQuery(dto);\n");
        sb.append("            PageHelper.startPage(dto.getPageNumber(), dto.getPageSize());\n");
        sb.append("            List<"+typeSimpleName+"> list = "+mapper+".getList(dto);\n");
        sb.append("            PageInfo<"+typeSimpleName+"> pageInfo = new PageInfo<>(list);\n");
        sb.append("            return new RichResult(pageInfo);\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            log.error(\"获取数据列表查询失败,请求:{}-系统异常,原因:{}\" , JSON.toJSONString(dto), e);\n");
        sb.append("            return format(e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");




        sb.append("    public RichResult<Integer>  update"+standardName+"("+typeSimpleName+" dto) {\n");
        sb.append("        try {\n");
        sb.append("            beforeUpdate(dto);\n");
        sb.append("            if(done ==1)return new RichResult("+mapper+".update(dto));\n");
        sb.append("            return new RichResult(ErrorCode.SystemError.getCode(),String.format(ErrorCode.SystemError.getMsg(), \"更新失败\"));\n");
        sb.append("         } catch (Exception e) {\n");
        sb.append("            log.error(\"获取数据列表查询失败,请求:{}-系统异常,原因:{}\" , JSON.toJSONString(dto), e);\n");
        sb.append("            return format(e);\n");
        sb.append("        }\n");
        sb.append("     }\n\n");


        sb.append("    private void beforeAdd("+typeSimpleName+" dto)throws BaseException {\n");
        sb.append("        dto.sedId(null);//TODO\n");

        for (ModelFiled f : list) {
            if(f.getFieldName().equals(primary))continue;
            if(f.getJavaType().equals("Long") || f.getJavaType().equals("Integer")|| f.getJavaType().equals("Date")) {
                sb.append("        if(dto.get" + StringUtil.UpperFirstWord(f.getFieldName()) + "() == null)throw  new BaseException(ErrorCode.WrongDataSpecified, \""+f.getClomunChnName()+"为空\");\n");
            }else{
                sb.append("        if(StringUtils.isBlank(dto.get"+StringUtil.UpperFirstWord(f.getFieldName())+"()))throw  new BaseException(ErrorCode.WrongDataSpecified, \""+f.getClomunChnName()+"为空\");\n");
            }
        }
        sb.append("        Date now = new Date();//TODO\n");
        sb.append("    }\n\n");

        //update
        sb.append("    private void beforeUpdate("+typeSimpleName+" dto)throws BaseException {\n");
        sb.append("        if(dto.getId() == null )" +
                "throw  new BaseException(ErrorCode.WrongDataSpecified, \"主键为空\");\n");
        for (ModelFiled f : list) {
            if(f.getFieldName().equals(primary))continue;
            if(f.getJavaType().equals("Long") || f.getJavaType().equals("Integer")|| f.getJavaType().equals("Date")) {
                sb.append("        if(dto.get" + StringUtil.UpperFirstWord(f.getFieldName()) + "() == null)throw  new BaseException(ErrorCode.WrongDataSpecified, \"更新"+f.getClomunChnName()+"不合规\");\n");
            }else{
                sb.append("        if(StringUtils.isBlank(dto.get"+StringUtil.UpperFirstWord(f.getFieldName())+"()))throw  new BaseException(ErrorCode.WrongDataSpecified, \"更新"+f.getClomunChnName()+"不合规\");\n");
            }
        }
        sb.append("        Date now = new Date();//TODO\n");
        sb.append("    }\n\n");


        //query
        sb.append("    private void beforeQuery("+typeSimpleName+" dto)throws BaseException {\n");
        for (ModelFiled f : list) {
            if(f.getJavaType().equals("Long") || f.getJavaType().equals("Integer")|| f.getJavaType().equals("Date")) {
                sb.append("        if(dto.get" + StringUtil.UpperFirstWord(f.getFieldName()) + "() == null)throw  new BaseException(ErrorCode.WrongDataSpecified, \""+f.getClomunChnName()+"参数不合规\");\n");
            }else{
                sb.append("        if(StringUtils.isBlank(dto.get"+StringUtil.UpperFirstWord(f.getFieldName())+"()))throw  new BaseException(ErrorCode.WrongDataSpecified, \""+f.getClomunChnName()+"参数不合规\");\n");
            }
        }
        sb.append("        Date now = new Date();//TODO\n");
        sb.append("    }\n\n");
        sb.append(" }\n\n");
        return  sb.toString();

    }
    /*public String makeIServiceContent() {
        StringBuilder sb = new StringBuilder("package " + modulePath + ".service;\n\n");
        sb.append("import org.ali.common.ex.NoServiceExecption;\n");
        sb.append("import " + typeName + ";\n");
        sb.append("import org.ali.common.IISuperService;\n");
        sb.append("public interface " + iServiceName + " extends IISuperService<" + typeSimpleName + ">{\n");
        sb.append("	public void set" + standardName + "Service(" + iServiceName + "  " + lowerstandardName + "Service)throws NoServiceExecption;\n");
        sb.append("}\n");
        return sb.toString();
    }*/


    public String makeIServiceContent() {
        StringBuilder sb = new StringBuilder("package " + modulePath + ".service;\n\n");

        sb.append("import " + typeName + ";\n");
        sb.append("import com.github.pagehelper.PageInfo;\n");
        sb.append("import tech.fullink.eaglehorn.common.model.RichResult;\n");
        sb.append("import tech.fullink.eaglehorn.engine.dao.model.OutResult;\n");
        sb.append("public interface " + iServiceName + " {\n");
        sb.append("    public RichResult<Integer>  add"+standardName+"(  "+typeSimpleName+" dto) ;\n\n");

        sb.append("    public RichResult<"+typeSimpleName+">  get"+standardName+"( Integer id) ;\n\n");

        sb.append("    public RichResult<PageInfo>  query"+standardName+"(  "+typeSimpleName+"  dto);\n\n");

        sb.append("    public RichResult<Integer>  update"+standardName+"( "+typeSimpleName+" dto) ;\n\n");

        sb.append("}\n");
        return sb.toString();
    }

    public String makeControllerContent() {
        String lowerKey = StringUtil.UpperFirstWord(primary);
        StringBuilder sb = new StringBuilder("package " + modulePath + ".controller;\n\n");
        /*sb.append("import java.util.HashMap;\n");
        sb.append("import java.util.Map;\n");
        sb.append("import javax.servlet.http.HttpServletRequest;\n");
        sb.append("import javax.servlet.http.HttpServletResponse;\n");
        sb.append("import org.apache.log4j.Logger;\n");
        sb.append("import org.ali.common.ex.NoServiceExecption;\n");
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        sb.append("import org.springframework.web.bind.annotation.RequestBody;\n");
        sb.append("import org.springframework.web.bind.annotation.RequestMapping;\n");
        sb.append("import org.springframework.web.bind.annotation.RestController;\n");
        sb.append("import " + typeName + ";\n");
        sb.append("import " + modulePath + ".service." + iServiceName + ";\n");
        sb.append("import org.ali.common.Constants;\n\n");*/


        sb.append("import com.alibaba.fastjson.JSON;\n");
        sb.append("import com.project.appName.AppAddress;\n");
        sb.append("import io.swagger.annotations.Api;\n");
        sb.append("import io.swagger.annotations.ApiOperation;\n");
        sb.append("import lombok.extern.slf4j.Slf4j;\n");
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        sb.append("import org.springframework.web.bind.annotation.*;\n");
        sb.append("import org.apache.commons.lang3.StringUtils;\n");
        //sb.append("import tech.fullink.eaglehorn.common.BaseService;\n");
        sb.append("import tech.fullink.eaglehorn.common.ErrorCode;\n");
        sb.append("import java.util.Date;\n");
        sb.append("import tech.fullink.eaglehorn.common.BaseException;\n");
        sb.append("import com.github.pagehelper.PageHelper;\n");
        sb.append("import com.github.pagehelper.PageInfo;\n");
        //sb.append("import tech.fullink.eaglehorn.model.RichResult;\n");
        sb.append("import java.util.List;\n\n\n");


        sb.append("@Slf4j\n"); //不能是汉字
        sb.append("@Api(\"Your Company  "+standardName+" Manage\")\n");
        //sb.append("@RequestMapping(\"/" + lowerstandardName + "Dispatcher\")\n");
        sb.append("@RestController\n");
        sb.append("@RequestMapping(value = AppAddress.PATH+\""+lowerstandardName+"/\")\n");
         


        sb.append("public class " + standardName + "Controller  extends CommonController {\n\n");
        //sb.append("	@Autowired\n");
        //sb.append("	" + iServiceName + " " + serviceName + ";\n\n");
        //sb.append("	private Logger logger =Logger.getLogger(" + standardName + "Contraller.class);\n");
        sb.append("    @Autowired\n");
        String mapper = daoName.substring(0,1).toLowerCase() +daoName.substring(1);
        sb.append("    " + daoName + " " + mapper+ ";\n\n");


        //sb.append("    /** \n");
        //sb.append("     * add适用于新增\n");
        //sb.append("     */\n");
        sb.append("    @ApiOperation(\"新增"+typeSimpleName+"\")\n");
        sb.append("    @RequestMapping(value = \"/"+lowerstandardName+"\",method = RequestMethod.POST)\n");
        sb.append("    public RichResult<Integer>  add(@RequestBody "+typeSimpleName+" dto) {\n");
        sb.append("        try {\n");
        sb.append("            beforeAdd(dto);\n");
        sb.append("            return new RichResult("+mapper+".insert(dto));\n");
        sb.append("          } catch (Exception e) {\n");

        sb.append("            log.error(\"新增失败,信息:{}-系统异常,原因:{}\" , JSON.toJSONString(dto) , e);\n");
        sb.append("            return format(e);\n");
        sb.append("          }\n");

        sb.append("    }\n\n");

        sb.append("    @ApiOperation(\"获取"+typeSimpleName+"\")\n");
        sb.append("    @RequestMapping(value = \"/"+lowerstandardName+"/{id}\",method = RequestMethod.GET)\n");
        sb.append("    public RichResult<"+typeSimpleName+">  get(@PathVariable(name = \"id\") Long id) {\n");
        sb.append("        try {\n");
        sb.append("            return new RichResult("+mapper+".getById(id));\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            log.error(\"获取数据查询失败,主键:{}-系统异常,原因:{}\" , id , e);\n");
        sb.append("            return format(e);\n");
        sb.append("        }\n");

        sb.append("    }\n\n");


        sb.append("    @ApiOperation(\"分页查询"+typeSimpleName+"\")\n");
        sb.append("    @RequestMapping(value = \"/"+lowerstandardName+"s\",method = RequestMethod.POST)\n");
        sb.append("    public RichResult<PageInfo>  queryPost(@RequestBody "+typeSimpleName+" dto) {\n");
        sb.append("        try {\n");
        sb.append("            beforeQuery(dto);\n");
        sb.append("            PageHelper.startPage(dto.getPageNumber(), dto.getPageSize());\n");
        sb.append("            List<"+typeSimpleName+"> list = "+mapper+".getList(dto);\n");
        sb.append("            PageInfo<"+typeSimpleName+"> pageInfo = new PageInfo<>(list);\n");
        sb.append("            return new RichResult(pageInfo);\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            log.error(\"获取数据列表查询失败,请求:{}-系统异常,原因:{}\" , JSON.toJSONString(dto), e);\n");
        sb.append("            return format(e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    @ApiOperation(\"分页查询"+typeSimpleName+"\")\n");
        sb.append("    @RequestMapping(value = \"/"+lowerstandardName+"s\",method = RequestMethod.GET)\n");
        sb.append("    public RichResult<PageInfo>  queryGet(@Validated "+typeSimpleName+" dto) {\n");
        sb.append("        try {\n");
        sb.append("            beforeQuery(dto);\n");
        sb.append("            PageHelper.startPage(dto.getPageNumber(), dto.getPageSize());\n");
        sb.append("            List<"+typeSimpleName+"> list = "+mapper+".getList(dto);\n");
        sb.append("            PageInfo<"+typeSimpleName+"> pageInfo = new PageInfo<>(list);\n");
        sb.append("            return new RichResult(pageInfo);\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            log.error(\"获取数据列表查询失败,请求:{}-系统异常,原因:{}\" , JSON.toJSONString(dto), e);\n");
        sb.append("            return format(e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
        

        sb.append("    @ApiOperation(\"更新"+typeSimpleName+"\")\n");
        sb.append("    @RequestMapping(value = \"/"+lowerstandardName+"/{"+lowerKey+"}\",method = RequestMethod.PUT)\n");
        sb.append("    public RichResult<Integer>  update(@PathVariable(name = \""+lowerKey+"\")    "+list.get(0).getJavaType()+" "+lowerKey+",@RequestBody "+typeSimpleName+" dto) {\n");
        sb.append("        try {\n");
        sb.append("            if("+lowerKey+"== null )throw  new BaseException(ErrorCode.WrongDataSpecified, \"主键为空\");\n");
        sb.append("            dto.set"+StringUtil.UpperFirstWord(list.get(0).getFieldName())+"("+lowerKey+");\n");
        sb.append("            beforeUpdate(dto);\n");
        sb.append("            int done = "+mapper+".update(dto);\n");
        sb.append("            if(done ==1)return new RichResult(1);\n");
        sb.append("            return new RichResult(ErrorCode.SystemError.getCode(),String.format(ErrorCode.SystemError.getMsg(), \"更新失败\"));\n");
        sb.append("         } catch (Exception e) {\n");
        sb.append("            log.error(\"获取数据列表查询失败,请求:{}-系统异常,原因:{}\" , JSON.toJSONString(dto), e);\n");
        sb.append("            return format(e);\n");
        sb.append("        }\n");
        sb.append("     }\n\n");


        sb.append("    private void beforeAdd("+typeSimpleName+" dto)throws BaseException {\n");
        sb.append("        dto.set"+lowerKey+"(null);//TODO\n");

        for (ModelFiled f : list) {
            if(f.getFieldName().equals(primary))continue;
            if(f.getJavaType().equals("Long") || f.getJavaType().equals("Integer")|| f.getJavaType().equals("Date")) {
                sb.append("        if(dto.get" + StringUtil.UpperFirstWord(f.getFieldName()) + "() == null)throw  new BaseException(ErrorCode.WrongDataSpecified, \""+f.getClomunChnName()+"为空\");\n");
            }else{
                sb.append("        if(StringUtils.isBlank(dto.get"+StringUtil.UpperFirstWord(f.getFieldName())+"()))throw  new BaseException(ErrorCode.WrongDataSpecified, \""+f.getClomunChnName()+"为空\");\n");
            }
        }
        sb.append("        Date now = new Date();//TODO\n");
        sb.append("    }\n\n");

        //update
        sb.append("    private void beforeUpdate("+typeSimpleName+" dto)throws BaseException {\n");
        sb.append("        if(dto.get"+lowerKey+"() == null )" +
                "throw  new BaseException(ErrorCode.WrongDataSpecified, \"主键为空\");\n");
        for (ModelFiled f : list) {
            if(f.getFieldName().equals(primary))continue;
            if(f.getJavaType().equals("Long") || f.getJavaType().equals("Integer")|| f.getJavaType().equals("Date")) {
                sb.append("        if(dto.get" + StringUtil.UpperFirstWord(f.getFieldName()) + "() == null)throw  new BaseException(ErrorCode.WrongDataSpecified, \"更新"+f.getClomunChnName()+"不合规\");\n");
            }else{
                sb.append("        if(StringUtils.isBlank(dto.get"+StringUtil.UpperFirstWord(f.getFieldName())+"()))throw  new BaseException(ErrorCode.WrongDataSpecified, \"更新"+f.getClomunChnName()+"不合规\");\n");
            }
        }
        sb.append("        Date now = new Date();//TODO\n");
        sb.append("    }\n\n");


        //query
        sb.append("    private void beforeQuery("+typeSimpleName+" dto)throws BaseException {\n");
        for (ModelFiled f : list) {
            if(f.getJavaType().equals("Long") || f.getJavaType().equals("Integer")|| f.getJavaType().equals("Date")) {
                sb.append("        if(dto.get" + StringUtil.UpperFirstWord(f.getFieldName()) + "() == null)throw  new BaseException(ErrorCode.WrongDataSpecified, \""+f.getClomunChnName()+"参数不合规\");\n");
            }else{
                sb.append("        if(StringUtils.isBlank(dto.get"+StringUtil.UpperFirstWord(f.getFieldName())+"()))throw  new BaseException(ErrorCode.WrongDataSpecified, \""+f.getClomunChnName()+"参数不合规\");\n");
            }
        }
        sb.append("        Date now = new Date();//TODO\n");
        sb.append("    }\n\n");
        sb.append(" }\n\n");
        return  sb.toString();
    }


    public String makeTestContent() {
        String nes = ObjectGenerate.makeNewObj(clazz);
        StringBuilder sb = new StringBuilder("package " + modulePath + ".test;\n\n");
        sb.append("import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;\n");
        sb.append("import org.springframework.boot.test.context.SpringBootTest;\n");
        sb.append("import com.project.appName.Application;\n");
        sb.append("import java.util.Date;\n\n");

        sb.append("@RunWith(SpringJUnit4ClassRunner.class)\n");
        sb.append("@SpringBootTest(classes = "+standardName+"Application.class)\n");
        sb.append("public class " + standardName + "Test {\n\n");
        sb.append("    @Autowired\n");
        String mapper = StringUtil.LowerFirstWord(daoName);
        sb.append("    " + daoName + " " + mapper+ ";\n\n");

        sb.append("    public "+typeSimpleName+" generateNewPojo() throws Exception {\n");
        sb.append("        "+nes+"\n");
        //sb.append("  return new "+typeSimpleName+"();\n");
        sb.append("    }\n");

        sb.append("    @Test\n");
        sb.append("    public void add() throws Exception {\n");
        sb.append("       "+typeSimpleName+"   "+lowerstandardName+" = generateNewPojo();\n");
        sb.append("       System.out.println("+mapper+".insert("+lowerstandardName+"));\n");
        //sb.append("       System.out.println("+lowerTypeName+".getId());\n");
        sb.append("    }\n\n");


        sb.append("    @Test\n");
        sb.append("    public void get() throws Exception {\n");
        sb.append("        System.out.println("+mapper+".getById(1l));\n");
        sb.append("    }\n\n");

        sb.append("    @Test\n");
        sb.append("    public void update() throws Exception {\n");
        sb.append("       "+typeSimpleName+" "+lowerstandardName+"  = "+mapper+".getById(1l);\n");
        sb.append("       "+lowerstandardName+" .set"+StringUtil.UpperFirstWord(second)+"("+ObjectGenerate.makeParm(second.getClass())+");\n");
        sb.append("       System.out.println("+mapper+".update("+lowerstandardName+"));\n");
        sb.append("    }\n");
        sb.append("    @Test\n");
        sb.append("    public void list() throws Exception {\n");
        sb.append("       System.out.println("+mapper+".getList(new "+typeSimpleName+"()));\n");
        sb.append("    }\n");
        sb.append("}\n");
         return  sb.toString();
    }

    public MakeDMLCompent makeTestFile() throws IOException {
        String testPath = (absoluteModulePath + "/"  + "/test/"+standardName+"Test" ).replace(".", "/") + ".java";
        FileUtil.createFile(testPath);
        FileUtil.writeStringToFile(makeTestContent(), testPath);
        System.out.println("created  test file ");
        System.out.println(toString());
        return this;
    }

    public String makeFeignClientContent() {
        //String lowerTypeName = StringUtil.LowerFirstWord(typeSimpleName);
        StringBuilder sb = new StringBuilder("package " + modulePath + ".feign;\n\n");
        sb.append("import com.github.pagehelper.PageInfo;\n");
        sb.append("import org.springframework.validation.annotation.Validated;\n");
        sb.append("import org.springframework.web.bind.annotation.PathVariable;\n");
        sb.append("import org.springframework.web.bind.annotation.RequestBody;\n");
        sb.append("import org.springframework.web.bind.annotation.RequestMapping;\n");
        sb.append("import org.springframework.web.bind.annotation.RequestMethod;\n");
        sb.append("import org.springframework.cloud.netflix.feign.FeignClient;\n");
        sb.append("import com.project.appName.AppAddress;\n");
        sb.append("@FeignClient(value = AppAddress.SERVICE_NAME)\n");
        sb.append("public interface "+standardName+"Client {\n\n");

        sb.append("    @RequestMapping(value = AppAddress.PATH+\""+lowerstandardName+"\",method = RequestMethod.POST)\n");
        sb.append("    public RichResult<Integer>  add"+standardName+"(@RequestBody "+typeSimpleName+" dto) ;\n");
        sb.append("    \n\n");

        sb.append("    @RequestMapping(value = AppAddress.PATH+\""+lowerstandardName+"/{id}\",method = RequestMethod.GET)\n");
        sb.append("    public RichResult<"+typeSimpleName+">  get"+standardName+"(@PathVariable(name =\"id\") Integer id) ;\n\n");


        sb.append("    @RequestMapping(value = AppAddress.PATH+\""+lowerstandardName+"s\",method = RequestMethod.GET)\n");
        sb.append("    public RichResult<PageInfo>  query"+standardName+"(@Validated "+typeSimpleName+"  dto);\n\n");


        sb.append("    @RequestMapping(value = AppAddress.PATH+\"/"+lowerstandardName+"/{id}\",method = RequestMethod.PUT)\n");
        sb.append("    public RichResult<Integer>  update"+standardName+"(@PathVariable(name =\"id\") Integer id,@RequestBody "+typeSimpleName+" dto) ;\n\n");

        sb.append("}\n");

        return  sb.toString();
    }

    public MakeDMLCompent makefeignFile() throws IOException {
        String testPath = (absoluteModulePath + "/feign/"+standardName+"Client" ).replace(".", "/") + ".java";
        FileUtil.createFile(testPath);
        FileUtil.writeStringToFile(makeFeignClientContent(), testPath);
        System.out.println("created  feign file ");
        return this;
    }

    public String makeFeignControllerContent() {

        StringBuilder sb = new StringBuilder("package " + modulePath + ".feign;\n\n");

        sb.append("import com.github.pagehelper.PageInfo;\n");
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        sb.append("import org.springframework.validation.annotation.Validated;\n");
        sb.append("import org.springframework.web.bind.annotation.*;\n");


        sb.append("@RestController\n");
        sb.append("public class "+standardName+"Controller extends CommonController{\n\n");
        sb.append("    @Autowired(required = false)\n");
        String mapper = StringUtil.LowerFirstWord(daoName);
        sb.append("    " + standardName + "Client  " +lowerstandardName+ "Client;\n\n");


        sb.append("    @RequestMapping(value = \"/add\",method = RequestMethod.POST)\n");
        sb.append("    public RichResult<Integer>  add(@RequestBody "+typeSimpleName+" dto){\n");
        sb.append("        try {\n");
        sb.append("             return "+lowerstandardName+ "Client.add(dto);\n");
        sb.append("         } catch (Exception e) {\n");
        sb.append("             return format(e);\n");
        sb.append("         }\n");
        sb.append("    }\n\n");

        sb.append("    @RequestMapping(value = \"/get/{id}\",method = RequestMethod.GET)\n");
        sb.append("    public RichResult<"+typeSimpleName+">  get(@PathVariable(name=\"id\") Integer id){\n");
        sb.append("        try {\n");
        sb.append("             return "+lowerstandardName+ "Client.get(id);\n");
        sb.append("         } catch (Exception e) {\n");
        sb.append("             return format(e);\n");
        sb.append("         }\n");
        sb.append("    }\n\n");

        sb.append("    @RequestMapping(value = \"/list\",method = RequestMethod.GET)\n");
        sb.append("    public RichResult<PageInfo>  query(@Validated "+typeSimpleName+"  dto){\n");
        sb.append("        try {\n");
        sb.append("             return "+lowerstandardName+ "Client.query(dto);\n");
        sb.append("         } catch (Exception e) {\n");
        sb.append("             return format(e);\n");
        sb.append("         }\n");
        sb.append("    }\n\n");

        sb.append("    @RequestMapping(value = \"/update/{id}\",method = RequestMethod.PUT)\n");
        sb.append("    public RichResult<Integer>  update(@PathVariable(name=\"id\") Integer id,@RequestBody "+typeSimpleName+" dto){\n");
        sb.append("        try {\n");
        sb.append("             return "+lowerstandardName+ "Client.update(id,dto);\n");
        sb.append("         } catch (Exception e) {\n");
        sb.append("             return format(e);\n");
        sb.append("         }\n");
        sb.append("    }\n\n");


        sb.append("}\n\n");
        return  sb.toString();
    }

    public MakeDMLCompent makefeignControllerFile() throws IOException {
        String testPath = (absoluteModulePath + "/feign/"+standardName+"Controller" ).replace(".", "/") + ".java";
        FileUtil.createFile(testPath);
        FileUtil.writeStringToFile(makeFeignControllerContent(), testPath);
        System.out.println("created  feign  controller file ");
        return this;
    }

    @Override
    public String toString() {
        return "MakeDMLCompent{" +
                "  typeName='" + typeName + '\'' +
                ", typeSimpleName='" + typeSimpleName + '\'' +
                ", absoluteModulePath='" + absoluteModulePath + '\'' +
                ", tableName='" + tableName + '\'' +
                ", standardName='" + standardName + '\'' +
                ", lowerstandardName='" + lowerstandardName + '\'' +
                ", daoPath='" + daoPath + '\'' +
                ", daoName='" + daoName + '\'' +
                ", daoPackgeName='" + daoPackgeName + '\'' +
                ", daoPackgeSimplrName='" + daoPackgeSimplrName + '\'' +
                ", modulePath='" + modulePath + '\'' +
                ", iServiceName='" + iServiceName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", idType='" + idType + '\'' +
                ", comunId='" + comunId + '\'' +
                ", primary='" + primary + '\'' +
                '}';
    }
}

