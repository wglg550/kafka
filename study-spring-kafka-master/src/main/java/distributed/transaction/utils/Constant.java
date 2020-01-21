package distributed.transaction.utils;

import java.util.HashMap;
import java.util.Map;

/**
 *  k12业务常量<br>
 *
 *
 * @author zhangqy
 * @date 2018-09-11
 * @Copyright (c) 2018-? http://qmth.com.cn All Rights Reserved.
 */
public class Constant {
    /**
     * 学生默认密码
     */
    public static final String DEFAULT_PWD = "123456";
    /**
     * 初始化导入学生信息的表头字段对应关系
     */
    public static final Map<String,String> studentImportHeadMap = new HashMap<>();

    /**
     * 初始化导入课堂信息的表头字段对应关系
     */
    public static final Map<String,String> classImportHeadMap = new HashMap<>();

    static{
        studentImportHeadMap.put("姓名","name");
        studentImportHeadMap.put("身份证","idno");
        studentImportHeadMap.put("手机","phone");
        studentImportHeadMap.put("学段","schoolSection");
        studentImportHeadMap.put("公立校","school");
        studentImportHeadMap.put("年级","grade");
        studentImportHeadMap.put("班级","cls");
        studentImportHeadMap.put("出生日期","birth");
        studentImportHeadMap.put("学号","stdNo");

        studentImportHeadMap.put("所属机构","ecRootOrgName");
        studentImportHeadMap.put("所属校区","ecOrgName");


        classImportHeadMap.put("名称","name");
        classImportHeadMap.put("班主任","headTeacherName");
        classImportHeadMap.put("预招人数","prospect");
        classImportHeadMap.put("开班时间","startTime");
        classImportHeadMap.put("结业时间","endTime");
        classImportHeadMap.put("所属机构","ecRootOrgName");
        classImportHeadMap.put("所属校区","ecOrgName");
        classImportHeadMap.put("课程名称","courseName");
        classImportHeadMap.put("任课老师","instructor");
    }

    public final static String COURSE_CODE = "k12";
    public final static String PAPERTYPE = "X";

    public final static String ZIP_PPT_PATH = "/Users/king/qmth/work_text_local/";
    public final static String COURSE_WARE_PPT_PATH = "/courseware/**";
    public final static String COURSE_WARE_PPT_TRUE_PATH = "/courseware";
    public final static String INDEX_HTML = "index.html";
}
