package com.inclusive.finance.room;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class OrderDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "myTest3.db";
    public static final String SysUser_TABLE_NAME = "SysUser";
    public static final String Standard_TABLE_NAME = "SysStandard";
    public static final String Department_TABLE_NAME = "SysDepartment";
    public static final String SysLog_TABLE_NAME = "SysLog";
    public static final String SysCollect_TABLE_NAME = "SysCollect";
    public static final String StandardSystem_TABLE_NAME = "StandardSystem";
    public static final String StandardLaiYuan_TABLE_NAME = "StandardLaiYuan";

    private SQLiteDatabase msqLiteDatabase;


    public OrderDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    public String BiaoNo;//标准号
    public String TypeName;//分类名称
    public String TiChuDepartmentName;//提出单位
    public String SourceId;//标准来源
    public String SourceName;//标准来源名称
    public String Pages;//页数
    public String SameClassNo;//同一分类号标准
    public String ReplaceNo;//代替好
    public String ByReplaceNo;//被代替号
    public String BzKeywords;//关键字
    public String CagegoryId;//标准体系分类id
    public String CagegoryName;//标准体系分类id
    public String SameTopicNo;//同一专题标准
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create table Orders(Id integer primary key, CustomName text, OrderPrice integer, Country text);
        String sql = "create table if not exists " + SysUser_TABLE_NAME + " (id integer primary key, BaseCreateTime text, BaseModifyTime text, BaseCreatorId text, " +
                "BaseModifierId text,UserName text, Password text, RealName text, DepartmentId text, DepartmentName text, UserStatus text, IsSystem text, LoginCount text)";


        String sql3 = "create table if not exists " + Department_TABLE_NAME + " (id integer primary key, BaseCreateTime text, BaseModifyTime text, BaseCreatorId text, " +
                "BaseModifierId text,DepartmentName text, Telephone text, DepartmentSort text, Remark text, DepartmentType text ,DepartmentTwoName text,DepartmentId text)";
        String sql5 = "create table if not exists " + StandardSystem_TABLE_NAME + "  (id integer primary key, StandardName text, StandardId text, StandardsSecondName text , StandardsSecondId text, StandardsThirdName text" +
                ", StandardsThirdId text )";
        String sql6 = "create table if not exists " + SysCollect_TABLE_NAME + "  (id integer primary key, UserId text, StandardId text, BaseCreateTime text  )";

        String sql7 = "create table if not exists " + StandardLaiYuan_TABLE_NAME + "  (id integer primary key, Name text  )";

        sqLiteDatabase.execSQL(sql7);
        try{
            String sql2 = "create table if not exists " + Standard_TABLE_NAME +
                    " (id integer primary key, BaseCreateTime text, BaseModifyTime text, BaseCreatorId text, " + "BaseModifierId text," +
                    "Name text, Status text, StartTime text, ReleaseTime text, " +
                    "TypeNum text, RatifyDepartmentId text, RatifyDepartmentName text, StartDepartmentId text," +
                    " StartDepartmentName text, EditorDepartmentId text, EditorDepartmentName text, Drafter text," +
                    "Referencesd text, Standards text, FilePath text, Remark text, " +
                    "CollectType Text," +"BiaoNo text,TypeName text,TiChuDepartmentName text," +
                    "SourceName text,Pages text,SameClassNo text,ReplaceNo text," +
                    "ByReplaceNo text" + ",BzKeywords text,CagegoryName text,SameTopicNo text)";
            sqLiteDatabase.execSQL(sql2);
        }catch (Exception e){
            Log.e("输出日志ssss","****"+e.toString());
        }
        sqLiteDatabase.execSQL(sql5);
        sqLiteDatabase.execSQL(sql3);
        sqLiteDatabase.execSQL(sql6);

//        sqLiteDatabase.execSQL(sql2);
        sqLiteDatabase.execSQL(sql);
        try{
            String sql4 = "create table if not exists " + SysLog_TABLE_NAME + "  (id integer primary key, UserId text, Keyword text, BaseCreateTime text , UserName text, RealName text, DepartmentName text )";
            Log.e("输出日志","****dddd" );

            sqLiteDatabase.execSQL(sql4);
        }catch (Exception e){
            Log.e("输出日志","****"+e.toString());
        }
    }
    public void cretableTbale(){
//        try{
//            String sql4 = "create table if not exists " + SysLog_TABLE_NAME + " (id integer primary key, UserId text, Keyword text, BaseCreateTime text )";
//
//            msqLiteDatabase.execSQL(sql4);
//        }catch (Exception e){
//            Log.e("输出日志","****"+e.toString());
//        }

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + SysUser_TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }
}