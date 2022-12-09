package com.inclusive.finance.room;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.enums.RouteType;
import com.blankj.utilcode.util.SPUtils;
import com.inclusive.finance.R;
import com.inclusive.finance.config.Constants;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.beanutils.BeanUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OrderDao {
    private static final String TAG = "OrdersDao";
    public String BaseCreateTime;
    public String BaseModifyTime;
    public String BaseCreatorId;
    public String BaseModifierId;
    public String UserName;//登录名称
    public String Password;//登录密码
    public String RealName;//姓名
    public String DepartmentId;
    public String DepartmentName;//部门名称
    public String UserStatus;//用户状态
    public String IsSystem;//用户角色
    public String LoginCount;
    // 列定义

    private final String[] ORDER_COLUMNS = new String[]{"id", "BaseCreateTime", "BaseModifyTime", "BaseCreatorId", "BaseModifierId",
            "UserName", "Password", "RealName", "DepartmentId",
            "DepartmentName", "UserStatus", "IsSystem", "LoginCount"
    };

    private Context context;
    private final OrderDBHelper ordersDBHelper;

    public OrderDao(Context context) {
        this.context = context;
        ordersDBHelper = new OrderDBHelper(context);

    }

    /**
     * 判断表中是否有数据
     */
    public boolean isDataExist() {
        int count = 0;

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = ordersDBHelper.getReadableDatabase();
            // select count(Id) from Orders
            cursor = db.query(OrderDBHelper.SysUser_TABLE_NAME, new String[]{"COUNT(Id)"}, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            if (count > 0) return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return false;
    }
    /**
     * 添加标准体系
     */
    public void initBiaoTable() {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();

            db.execSQL("insert into " + OrderDBHelper.Department_TABLE_NAME + " (BaseCreateTime ,BaseModifyTime ,BaseCreatorId ,BaseModifierId ,DepartmentName ,Telephone ,DepartmentSort ,Remark ,DepartmentType,DepartmentTwoName,DepartmentId ) values (" +
                    " '2020-11-28', '2020-11-28', '2020-11-28','2020-11-28', '财务部', '13612536985','1', '批准单位', '市本级','财务1部,财务2部,财务3部','1')");
            db.execSQL("insert into " + OrderDBHelper.Department_TABLE_NAME + " (BaseCreateTime ,BaseModifyTime ,BaseCreatorId ,BaseModifierId ,DepartmentName ,Telephone ,DepartmentSort ,Remark ,DepartmentType,DepartmentTwoName,DepartmentId ) values (" +
                    " '2020-11-28', '2020-11-28', '2020-11-28','2020-11-28', '财务部', '13612536985','1', '批准单位', '市本级','财务1部,财务2部,财务3部','1')");
            db.execSQL("insert into " + OrderDBHelper.Department_TABLE_NAME + " (BaseCreateTime ,BaseModifyTime ,BaseCreatorId ,BaseModifierId ,DepartmentName ,Telephone ,DepartmentSort ,Remark ,DepartmentType ,DepartmentTwoName,DepartmentId) values (" +
                    " '2020-11-28', '2020-11-28', '2020-11-28','2020-11-28', '财务部', '13612536985','1', '批准单位', '市本级','财务1部|财务2部|财务3部','1')");
            db.execSQL("insert into " + OrderDBHelper.Department_TABLE_NAME + " (BaseCreateTime ,BaseModifyTime ,BaseCreatorId ,BaseModifierId ,DepartmentName ,Telephone ,DepartmentSort ,Remark ,DepartmentType,DepartmentTwoName,DepartmentId ) values (" +
                    " '2020-11-28', '2020-11-28', '2020-11-28','2020-11-28', '水利部', '13612536985','1', '批准单位', '市本级','','')");
            db.execSQL("insert into " + OrderDBHelper.Department_TABLE_NAME + " (BaseCreateTime ,BaseModifyTime ,BaseCreatorId ,BaseModifierId ,DepartmentName ,Telephone ,DepartmentSort ,Remark ,DepartmentType ,DepartmentTwoName,DepartmentId) values (" +
                    " '2020-11-28', '2020-11-28', '2020-11-28','2020-11-28', '小卖部', '13612536985','1', '提出单位', '市本级','','')");

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }
    /**
     * 添加部门库数据
     */
    public void initBuMenTable(String BaseCreateTime ,String BaseModifyTime ,String BaseCreatorId ,String BaseModifierId
            ,String DepartmentName , String Telephone ,String DepartmentSort ,String Remark ,
                               String DepartmentType,String DepartmentId) {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();

            db.execSQL("insert into " + OrderDBHelper.Department_TABLE_NAME + " (BaseCreateTime ,BaseModifyTime ,BaseCreatorId ,BaseModifierId ,DepartmentName ,Telephone ,DepartmentSort ,Remark ,DepartmentType,DepartmentTwoName,DepartmentId ) values (" +
                    " '"+BaseCreateTime+"', '"+BaseModifyTime+"', '"+BaseCreatorId+"','"+BaseModifierId+"', '"+DepartmentName+"'," +
                    " '"+Telephone+"','"+DepartmentSort+"', '"+Remark+"', '"+DepartmentType+"'," +
                    "'"+DepartmentType+"','"+DepartmentId+"')");

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    /**
     * 添加标注文件库数据
     */
    public Integer initStatandTable(String BaseCreateTime ,String BaseModifyTime ,String BaseCreatorId ,
                                 String BaseModifierId ,String Name ,String Status ,
                                 String StartTime ,String ReleaseTime ,String TypeNum ,
                                 String RatifyDepartmentId , String RatifyDepartmentName ,String StartDepartmentId ,
                                 String StartDepartmentName ,String EditorDepartmentId ,String EditorDepartmentName ,
                                 String Drafter ,String Referencesd ,String Standards ,
                                 String FilePath ,String Remark,String CollectType,
                                 String BiaoNo,String TiChuDepartmentName,String SourceName,
                                 String Pages,String SameClassNo,String ReplaceNo,
                                 String ByReplaceNo,String BzKeywords,String CagegoryName,
                                 String SameTopicNo) {
        SQLiteDatabase db = null;



        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();
            db.execSQL("insert into " + OrderDBHelper.Standard_TABLE_NAME + " (BaseCreateTime ,BaseModifyTime ,BaseCreatorId ,BaseModifierId ," +
                    "Name ," +
                    "Status ,StartTime ,ReleaseTime ,TypeNum ," +
                    "RatifyDepartmentId ,RatifyDepartmentName ,StartDepartmentId ,StartDepartmentName ," +
                    "EditorDepartmentId ,EditorDepartmentName ,Drafter ,Referencesd ," +
                    "Standards ,FilePath ,Remark,CollectType, " +
                    "BiaoNo," +
                    "TiChuDepartmentName,SourceName, Pages, SameClassNo," +
                    " ReplaceNo, ByReplaceNo, BzKeywords, CagegoryName," +
                    " SameTopicNo) values (" +
                    " '"+BaseCreateTime+"', '"+BaseModifyTime+"', '"+BaseCreatorId+"','"+BaseModifierId+"', " +
                    "'"+Name+"', " + "'"+Status+"','"+StartTime+"', '"+ReleaseTime+"', " +
                    "'"+TypeNum+"', '"+RatifyDepartmentId+"','"+RatifyDepartmentName+"', " + "'"+StartDepartmentId+"', " +
                    "'"+StartDepartmentName+"','"+EditorDepartmentId+"', '"+EditorDepartmentName+"', '"+Drafter+"','"+
                    Referencesd+"', " +"'"+Standards+"'," +" '"+FilePath+"', " + "'"+Remark
                    +"', '"+CollectType+"'" +" , '"+BiaoNo+"', '"+TiChuDepartmentName+"' , " + "'"+SourceName+"', '"
                    +Pages+"' , " + "'"+SameClassNo+"', '"+ReplaceNo+"' , " + "'"+ByReplaceNo+"', '"
                    +BzKeywords+"' , " + "'"+CagegoryName+"', '"+SameTopicNo+"')");
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("输出日志ssss","****"+e.toString());

            Log.e(TAG, "", e);
            return -1;
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return 0;

    }

    /**
     * 初始化数据
     */
    public void initStatandData(String wenxianname, String Status, String shishishijina, String fabushijian, String TypeNum, String RatifyDepartmentName, String StartDepartmentName, String EditorDepartmentName, String Drafter, String FilePath, String Remark) {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();

            db.execSQL("insert into " + OrderDBHelper.Standard_TABLE_NAME + " (BaseCreateTime ,BaseModifyTime ,BaseCreatorId ,BaseModifierId ,Name ,Status ,StartTime ,ReleaseTime ,TypeNum ,RatifyDepartmentId ,RatifyDepartmentName ,StartDepartmentId ,StartDepartmentName ,EditorDepartmentId ,EditorDepartmentName ,Drafter ,Referencesd ,Standards ,FilePath ,Remark) values (" +
                    " '2020-11-28', '2020-11-28', '2020-11-28','2020-11-28', '" + wenxianname + "', '" + Status + "','" + shishishijina + "', '" + fabushijian + "', '" + TypeNum + "', '" + RatifyDepartmentName + "','" + RatifyDepartmentName + "', '" + StartDepartmentName + "', '" + StartDepartmentName + "','" + EditorDepartmentName + "', '" + EditorDepartmentName + "', '" + Drafter + "','编写人', 'Arc', '" + FilePath + "', '" + Remark + "')");

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    /**
     * 初始化数据
     */
    public void deleteStatandData(Integer id) {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();
//            db.execSQL("insert into " + OrderDBHelper.Standard_TABLE_NAME + " (BaseCreateTime  ) values (" +
//                    " 'BaseCreateTime')");
//            db.execSQL("insert into " + OrderDBHelper.Standard_TABLE_NAME + " (BaseCreateTime ,BaseModifyTime ,BaseCreatorId ,BaseModifierId ,Name ,Status ,StartTime ,ReleaseTime  ,TypeNum ) values (" +
//                    " 'BaseCreateTime', 'BaseModifyTime', 'BaseCreatorId','BaseCreatorId', 'BaseModifierId', 'China','Arc', 'Arc' ,'Arc')");
            db.execSQL("delete FROM " + OrderDBHelper.Standard_TABLE_NAME + " where id = " + id + "");

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    /**
     * 初始化数据
     */
    public void updateStatandData(String Name ,String Status ,
                                  String StartTime ,String ReleaseTime ,String TypeNum ,
                                  String RatifyDepartmentId , String RatifyDepartmentName ,String StartDepartmentId ,
                                  String StartDepartmentName ,String EditorDepartmentId ,String EditorDepartmentName ,
                                  String Drafter ,String Referencesd ,String Standards ,
                                  String FilePath ,String Remark,String CollectType,
                                  String BiaoNo,String TiChuDepartmentName,String SourceName,
                                  String Pages,String SameClassNo,String ReplaceNo,
                                  String ByReplaceNo,String BzKeywords,String CagegoryName,
                                  String SameTopicNo, Integer id) {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();
//            db.execSQL("insert into " + OrderDBHelper.Standard_TABLE_NAME + " (BaseCreateTime  ) values (" +
//                    " 'BaseCreateTime')");
//            db.execSQL("insert into " + OrderDBHelper.Standard_TABLE_NAME + " (BaseCreateTime ,BaseModifyTime ,BaseCreatorId ,BaseModifierId ,Name ,Status ,StartTime ,ReleaseTime  ,TypeNum ) values (" +
//                    " 'BaseCreateTime', 'BaseModifyTime', 'BaseCreatorId','BaseCreatorId', 'BaseModifierId', 'China','Arc', 'Arc' ,'Arc')");
            db.execSQL("update " + OrderDBHelper.Standard_TABLE_NAME + " set Name = '" + Name + "', RatifyDepartmentId = '" + RatifyDepartmentId
                    + "', TiChuDepartmentName = '" + TiChuDepartmentName
                    + "', SourceName = '" + SourceName
                    + "', Pages = '" + Pages
                    + "', SameClassNo = '" + SameClassNo
                    + "', SourceName = '" + SourceName
                    + "', ReplaceNo = '" + ReplaceNo
                    + "', ByReplaceNo = '" + ByReplaceNo
                    + "', BzKeywords = '" + BzKeywords
                    + "', CagegoryName = '" + CagegoryName
                    + "', SameTopicNo = '" + SameTopicNo

                    + "', RatifyDepartmentId = '" + RatifyDepartmentId
                    + "', StartDepartmentId = '" + StartDepartmentId
                    + "', EditorDepartmentId = '" + EditorDepartmentId
                    + "', Referencesd = '" + Referencesd
                    + "', RatifyDepartmentId = '" + RatifyDepartmentId
                    + "', Standards = '" + Standards
                    + "', BiaoNo = '" + BiaoNo
                    + "', TiChuDepartmentName = '" + TiChuDepartmentName
                    + "', StartTime = '" + StartTime + "', ReleaseTime = '" + ReleaseTime + "', Status = '" + Status + "' , TypeNum = '" + TypeNum +
                    "' , RatifyDepartmentName = '" + RatifyDepartmentName + "' , StartDepartmentName = '" + StartDepartmentName + "' , EditorDepartmentName = '" + EditorDepartmentName + "' , Drafter = '" + Drafter + "' , FilePath = '" + FilePath + "' , Remark = '" + Remark +
                    "' where id = " + id + "");

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }
    /**
     * 修改部门
     */
    public Integer updateDepart(String DepartmentName, Integer id) {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();

            db.execSQL("update " + OrderDBHelper.Department_TABLE_NAME + " SET DepartmentName = '" + DepartmentName + "' where id = " + id);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "", e);
            return -1;
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return 0;
    }
    /**
     * 修改密码
     */
    public Integer updatepwd(String pwd, Integer id) {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();

            db.execSQL("update " + OrderDBHelper.SysUser_TABLE_NAME + " SET Password = '" + pwd + "' where id = " + id);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "", e);
            return -1;
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return 0;
    }
    //修改收藏状态
    public Integer changeCollect(String CollectType, Integer standid ) {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();


             db.execSQL("update " + OrderDBHelper.Standard_TABLE_NAME + " set CollectType = '"  +CollectType+
                    "' where id = " + standid + "");
            db.setTransactionSuccessful();
        } catch (Exception e) {
            return -1;
//            Log.e(TAG,   e.toString());
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return 0;
    }
    public Integer deleteCollect(Integer userid, Integer standid ) {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");
            Calendar selectedDate = Calendar.getInstance();//系统当前时间
            Date mdate = selectedDate.getTime();

            db.execSQL("delete  FROM " + OrderDBHelper.SysCollect_TABLE_NAME +  " where UserId = " +userid+ " AND StandardId = " + standid);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("ddd",   e.toString());

            return -1;
         } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return 0;
    }
    //增加部门
    public Integer intserBuMen(String DepartmentName) {
        SQLiteDatabase db = null;
        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();
             SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");
            Calendar selectedDate = Calendar.getInstance();//系统当前时间
            Date mdate = selectedDate.getTime();
            db.execSQL("insert into " + OrderDBHelper.Department_TABLE_NAME + " (BaseCreateTime ,DepartmentName ) values (" +
                    " '"+dateFormat.format(mdate)+"' , '"+DepartmentName +"')");
            db.setTransactionSuccessful();
            return 0;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return -1;

    }
    public Integer intserCollect(Integer userid, Integer standid ) {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");
            Calendar selectedDate = Calendar.getInstance();//系统当前时间
            Date mdate = selectedDate.getTime();

            db.execSQL("insert into " + OrderDBHelper.SysCollect_TABLE_NAME + " ( UserId , StandardId  , BaseCreateTime ) values ( '" + userid + "' , '" + standid + "' , '" + dateFormat.format(mdate) + "' )  ");

            db.setTransactionSuccessful();
        } catch (Exception e) {
            return -1;
//            Log.e(TAG,   e.toString());
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return 0;
    }
    public void intserUser(String username, String realname, String danwei,String Password,String status) {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();

            db.execSQL("insert into " + OrderDBHelper.SysUser_TABLE_NAME + " ( UserName , RealName  , DepartmentName , Password , UserStatus ) values ( '" + username + "' , '" + realname + "' , '" + danwei + "' , '" + Password+ "' , '" + status + "' )  ");

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }
    public void updateStatus(String Status,  Integer id) {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();

            db.execSQL("update " + OrderDBHelper.SysUser_TABLE_NAME + " SET UserStatus = '" + Status + "'  where id = " + id);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }
    public void updateUser(String pwd, String realname, String danwei, Integer id) {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();

            db.execSQL("update " + OrderDBHelper.SysUser_TABLE_NAME + " SET UserName = '" + pwd + "' , RealName = '" + realname + "'" + " , DepartmentName = '" + danwei + "' where id = " + id);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    /**
     * 初始化数据
     */
    public void initTable() {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();

            db.execSQL("insert into " + OrderDBHelper.SysUser_TABLE_NAME + " (Id, CustomName, OrderPrice, Country) values (1, 'Arc', 100, 'China')");
            db.execSQL("insert into " + OrderDBHelper.SysUser_TABLE_NAME + " (Id, CustomName, OrderPrice, Country) values (2, 'Bor', 200, 'USA')");
            db.execSQL("insert into " + OrderDBHelper.SysUser_TABLE_NAME + " (Id, CustomName, OrderPrice, Country) values (3, 'Cut', 500, 'Japan')");
            db.execSQL("insert into " + OrderDBHelper.SysUser_TABLE_NAME + " (Id, CustomName, OrderPrice, Country) values (4, 'Bor', 300, 'USA')");
            db.execSQL("insert into " + OrderDBHelper.SysUser_TABLE_NAME + " (Id, CustomName, OrderPrice, Country) values (5, 'Arc', 600, 'China')");
            db.execSQL("insert into " + OrderDBHelper.SysUser_TABLE_NAME + " (Id, CustomName, OrderPrice, Country) values (6, 'Doom', 200, 'China')");

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    /**
     * 执行自定义SQL语句
     */
    public void execSQL(String sql) {
        SQLiteDatabase db = null;

        try {
            if (sql.contains("select")) {
                Toast.makeText(context, R.string.strUnableSql, Toast.LENGTH_SHORT).show();
            } else if (sql.contains("insert") || sql.contains("update") || sql.contains("delete")) {
                db = ordersDBHelper.getWritableDatabase();
                db.beginTransaction();
                db.execSQL(sql);
                db.setTransactionSuccessful();
                Toast.makeText(context, R.string.strSuccessSql, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, R.string.strErrorSql, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    /**
     * 查询数据库中部门
     */
    String EditorDepartmentName_sql = "";
    String Drafter_sql = "";
    String StartTime_sql = "";
    String DepartmentName_sql = "";

    public List<User> getUserInfo(Integer name) throws Exception {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String sql = "";

        try {
            db = ordersDBHelper.getReadableDatabase();
            sql = "select * from " + OrderDBHelper.SysUser_TABLE_NAME + " where id = '" + name + "' Limit 10 Offset 0";
            cursor = db.rawQuery(sql, null);
            if (cursor.getCount() > 0) {
                List<User> orderList = new ArrayList<User>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseOrder(cursor));
                }
                return orderList;
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }
    //查询标准体系
    public List<StandardSystem> getListBiaoZhunTiXi(String StandardName,String StandardsSecondName) throws Exception {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String sql = "";
        String standardSql="";
        String StandardsSecondNameSql="";

        if (StandardName!=""){
            standardSql=" and StandardName = '"+StandardName+"'";
        }
        if (StandardsSecondName!=""){
            StandardsSecondNameSql=" and StandardsSecondName = '"+StandardsSecondName+"'";
        }
        try {
            db = ordersDBHelper.getReadableDatabase();
            sql = "select * from " + OrderDBHelper.StandardSystem_TABLE_NAME+ " where 1=1 "+standardSql+StandardsSecondNameSql+"   Limit 10 Offset 0";
            cursor = db.rawQuery(sql, null);

            if (cursor.getCount() > 0) {
                List<StandardSystem> orderList = new ArrayList<StandardSystem>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseOrder5(cursor));
                }
                return orderList;
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }
    //查询分类列表
    public List<SysLaiYuan> getListFenLei(String name,Integer pager) throws Exception {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String sql = "";

        try {

            db = ordersDBHelper.getReadableDatabase();
            if (name == "") {
                sql = "select * from " + OrderDBHelper.StandardLaiYuan_TABLE_NAME + "   Limit 100 Offset "+pager;
            } else {
                sql = "select * from " + OrderDBHelper.StandardLaiYuan_TABLE_NAME + " where Name = '" + name + "' Limit 100 Offset "+pager;
            }


            cursor = db.rawQuery(sql, null);

            if (cursor.getCount() > 0) {
                List<SysLaiYuan> orderList = new ArrayList<SysLaiYuan>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseOrder6(cursor));
                }
                return orderList;
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }
    //查询来源
    public List<SysLaiYuan> getListLaiYuan(String name,Integer pager) throws Exception {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String sql = "";

        try {

            db = ordersDBHelper.getReadableDatabase();
            if (name == "") {
                sql = "select * from " + OrderDBHelper.StandardLaiYuan_TABLE_NAME + "   Limit 100 Offset "+pager;
            } else {
                sql = "select * from " + OrderDBHelper.StandardLaiYuan_TABLE_NAME + " where Name = '" + name + "' Limit 100 Offset "+pager;
            }


            cursor = db.rawQuery(sql, null);

            if (cursor.getCount() > 0) {
                List<SysLaiYuan> orderList = new ArrayList<SysLaiYuan>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseOrder6(cursor));
                }
                return orderList;
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }
    //查询部门
    public List<SysDepartment> getListBuMenDate(String name) throws Exception {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String sql = "";

        try {

            db = ordersDBHelper.getReadableDatabase();

            if (name == "") {
                sql = "select * from " + OrderDBHelper.Department_TABLE_NAME + "   Limit 10 Offset 0";
            } else {
                sql = "select * from " + OrderDBHelper.Department_TABLE_NAME + " where DepartmentName = '" + name + "' Limit 10 Offset 0";
            }

            cursor = db.rawQuery(sql, null);

            if (cursor.getCount() > 0) {
                List<SysDepartment> orderList = new ArrayList<SysDepartment>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseOrder3(cursor));
                }
                return orderList;
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }
    public List<SysLog> getSyslogHis(Integer pager) throws Exception {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String sql = "";

        try {
            db = ordersDBHelper.getReadableDatabase();

            sql = "select  * from " + OrderDBHelper.SysLog_TABLE_NAME +" ORDER BY id DESC Limit 100 Offset "+pager;

            // select * from Orders
//            cursor = db.query(OrderDBHelper.SysUser_TABLE_NAME, ORDER_COLUMNS, null, null, null, null, null);

            cursor = db.rawQuery(sql, null);

            if (cursor.getCount() > 0) {
                List<SysLog> orderList = new ArrayList<SysLog>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseOrder4(cursor));
                }
                return orderList;
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }
    //查询历史记录
    public List<SysLog> getSyslogHis(Integer TypeNum,Integer pager) throws Exception {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String sql = "";

        try {
            db = ordersDBHelper.getReadableDatabase();

            sql = "select  * from " + OrderDBHelper.SysLog_TABLE_NAME + " where UserId = " + TypeNum + " ORDER BY id DESC Limit 10 Offset "+pager;



            cursor = db.rawQuery(sql, null);

            if (cursor.getCount() > 0) {
                List<SysLog> orderList = new ArrayList<SysLog>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseOrder4(cursor));
                }
                return orderList;
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }
    //查询文件详情
    public List<Standard> getBiaozhunHao(String TypeNum) throws Exception {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String sql = "";

        try {
            db = ordersDBHelper.getReadableDatabase();

            sql = "select  * from " + OrderDBHelper.Standard_TABLE_NAME + " where BiaoNo = '" + TypeNum + "'";



            cursor = db.rawQuery(sql, null);

            if (cursor.getCount() > 0) {
                List<Standard> orderList = new ArrayList<Standard>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseOrder2(cursor));
                }
                return orderList;
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }
    //查询文件详情
    public List<Standard> getInfo(String TypeNum) throws Exception {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String sql = "";

        try {
            db = ordersDBHelper.getReadableDatabase();

            sql = "select  * from " + OrderDBHelper.Standard_TABLE_NAME + " where id = " + TypeNum + "  Limit 10 Offset 0";



            cursor = db.rawQuery(sql, null);

            if (cursor.getCount() > 0) {
                List<Standard> orderList = new ArrayList<Standard>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseOrder2(cursor));
                }
                return orderList;
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }
    //查询分类好
    public List<String> getListFenLeiNum(String TypeNum) throws Exception {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String sql = "";

        try {
            db = ordersDBHelper.getReadableDatabase();

            sql = "select " + TypeNum + " from " + OrderDBHelper.Standard_TABLE_NAME + "   Limit 10 Offset 0";

            // select * from Orders
//            cursor = db.query(OrderDBHelper.SysUser_TABLE_NAME, ORDER_COLUMNS, null, null, null, null, null);

            cursor = db.rawQuery(sql, null);

            if (cursor.getCount() > 0) {
                List<String> orderList = new ArrayList<String>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(cursor.getString(cursor.getColumnIndex(TypeNum)));
                }
                return orderList;
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }

    String ReleaseTime_sql = "";
    String TypeNum_sql = "";
    String RatifyDepartmentName_sql = "";
    String StartDepartmentName_sql = "";
    String status_sql = "";
    String Name_sql = "";

    String msql="";
    //查询标准文件
    String sql = "";
    String Standards_sql = "";
    public long getListBiaoZhunGaoJiCount( String andsql, String orsql ,Integer pager) throws Exception {

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = ordersDBHelper.getReadableDatabase();

            Log.e("dddd","查到数据sql***sss"+andsql);

            sql=sql+" ( "+andsql+" ) " + orsql;

            String ssql = "select count(*) from " + OrderDBHelper.Standard_TABLE_NAME + " where  1 = 1  and "  + sql   ;

            Log.e("dddd","查到数据sql***"+ssql);

            cursor = db.rawQuery(ssql, null);
            cursor.moveToFirst();
            long count = cursor.getLong(0);
            cursor.close();
            return count;
        } catch (Exception e) {
            Log.e("dddd","ffff"+e.toString());
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }

        }

        return 0;
    }

    public List<Standard> getListBiaoZhunGaoJi( String andsql, String orsql ,Integer pager) throws Exception {

        SQLiteDatabase db = null;
        Cursor cursor = null;
         try {
            db = ordersDBHelper.getReadableDatabase();

             Log.e("dddd","查到数据sql***sss"+andsql);

             sql=sql+" ( "+andsql+" ) " + orsql;

            String ssql = "select * from " + OrderDBHelper.Standard_TABLE_NAME + " where  1 = 1  and "  + sql  + " Limit 10 Offset " + pager + "";

             Log.e("dddd","查到数据sql***"+ssql);

            cursor = db.rawQuery(ssql, null);

            if (cursor.getCount() > 0) {
                List<Standard> orderList = new ArrayList<Standard>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseOrder2(cursor));
                }
                return orderList;
            }
        } catch (Exception e) {
            Log.e("dddd","ffff"+e.toString());
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }

        }

        return null;
    }
    String yinyong_sql="";
    String caiyong_sql="";

    //查询标准文件列表
    public List<Standard> getListBiaoZhunDate3(Boolean metemp, String StartTime, String ReleaseTime, String TypeNum,
                                              String RatifyDepartmentName, String StartDepartmentName, String EditorDepartmentName,
                                              String Drafter, int page,String Name,String status,String StartTimeend,String ReleaseTimeend,String ASC
            ,String yinyong ,String caiyong ) throws Exception {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String sql = "";
        try {
            db = ordersDBHelper.getReadableDatabase();
            if (!caiyong.trim().equals("")) {
                caiyong_sql = "and Standards like '%" + caiyong + "%' ";
            } else {
                caiyong_sql = " ";
            }
            if (!yinyong.trim().equals("")) {
                yinyong_sql = "and Referencesd like '%" + yinyong + "%' ";
            } else {
                yinyong_sql = " ";
            }
            if (!status.trim().equals("")) {
                status_sql = "and Status like '%" + status + "%' ";
            } else {
                status_sql = " ";
            }
            if (!Name.trim().equals("")) {
                Name_sql = "and Name like '%" + Name + "%' ";
            } else {
                Name_sql = " ";
            }
            if (!StartDepartmentName.trim().equals("")) {
                StartDepartmentName_sql = "and TiChuDepartmentName = '" + StartDepartmentName + "'";
            } else {
                StartDepartmentName_sql = "";
            }
            if (!RatifyDepartmentName.trim().equals("")) {
                RatifyDepartmentName_sql = "and RatifyDepartmentName = '" + RatifyDepartmentName + "'";
            } else {
                RatifyDepartmentName_sql = "";
            }
            if (!TypeNum.trim().equals("")) {
                TypeNum_sql = "and TypeNum = '" + TypeNum + "' ";
            } else {
                TypeNum_sql = "";
            }
            if (!ReleaseTime.trim().equals("")) {
                ReleaseTime_sql = "and ReleaseTime BETWEEN '" + ReleaseTime + "' AND '"+ReleaseTimeend+"'";
            } else {
                ReleaseTime_sql = "";
            }
            if (!EditorDepartmentName.trim().equals("")) {
                EditorDepartmentName_sql = "and  EditorDepartmentName = '" + EditorDepartmentName + "' ";
            } else {
                EditorDepartmentName_sql = "";
            }
            if (!Drafter.trim().equals("")) {
                Drafter_sql = "and Drafter = '" + Drafter + "' ";
            } else {
                Drafter_sql = "";
            }
            if (!StartTime.trim().equals("")) {
                StartTime_sql = "and StartTime BETWEEN '" + StartTime + "' AND '"+StartTimeend+"'";
            } else {
                StartTime_sql = "";
            }
            if(StartDepartmentName_sql!=""){
                msql=StartDepartmentName_sql;
            }else {

            }
            if(msql==""){
                msql=Name_sql;
            }else if(EditorDepartmentName_sql!=""){
                msql=msql+""+Name_sql;
            }
            if(msql==""){
                msql=EditorDepartmentName_sql;
            }else if(EditorDepartmentName_sql!=""){
                msql=msql+"  "+EditorDepartmentName_sql;
            }
            if(msql==""){
                msql=StartTime_sql;
            }else if(StartTime_sql!=""){
                msql=msql+"  "+StartTime_sql;
            }
            if(msql==""){
                msql=Drafter_sql;
            }else if(Drafter_sql!=""){
                msql=msql+"  "+Drafter_sql;
            }
            if(msql==""){
                msql=ReleaseTime_sql;
            }else if(ReleaseTime_sql!=""){
                msql=msql+"  "+ReleaseTime_sql;
            }
            if(msql==""){
                msql=TypeNum_sql;
            }else if(TypeNum_sql!=""){
                msql=msql+"  "+TypeNum_sql;
            }
            if(msql==""){
                msql=status_sql;
            }else if(status_sql!=""){
                msql=msql+"  "+status_sql;
            }
            if(msql==""){
                msql=yinyong_sql;
            }else if(yinyong_sql!=""){
                msql=msql+"  "+yinyong_sql;
            }
            if(msql==""){
                msql=caiyong_sql;
            }else if(caiyong_sql!=""){
                msql=msql+"  "+caiyong_sql;
            }
            if(msql==""){
                msql=RatifyDepartmentName_sql;
            }else if(RatifyDepartmentName_sql!=""){
                msql=msql+"  "+RatifyDepartmentName_sql;
            }
//            if (metemp) {
//                sql = "select * from " + OrderDBHelper.Standard_TABLE_NAME + "   Limit 10 Offset " + page + "";
//            } else {
            sql = "select * from " + OrderDBHelper.Standard_TABLE_NAME + " where  1=1 "  + msql  ;
//            }
            // select * from Orders
//            cursor = db.query(OrderDBHelper.SysUser_TABLE_NAME, ORDER_COLUMNS, null, null, null, null, null);
            Log.e("dddd","ffff"+sql);

            cursor = db.rawQuery(sql, null);

            if (cursor.getCount() > 0) {
                List<Standard> orderList = new ArrayList<Standard>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseOrder2(cursor));
                }
                return orderList;
            }
        } catch (Exception e) {
            Log.e("dddd","ffff"+e.toString());
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }
    //查询标准文件列表
    public List<Standard> getListBiaoZhunDate(Boolean metemp, String StartTime, String ReleaseTime, String TypeNum,
                                              String RatifyDepartmentName, String StartDepartmentName, String EditorDepartmentName,
                                              String Drafter, int page,String Name,String status,String StartTimeend,String ReleaseTimeend,String ASC
            ,String yinyong ,String caiyong ) throws Exception {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String sql = "";
        try {
            db = ordersDBHelper.getReadableDatabase();
            if (!caiyong.trim().equals("")) {
                caiyong_sql = "and Standards like '%" + caiyong + "%' ";
            } else {
                caiyong_sql = " ";
            }
            if (!yinyong.trim().equals("")) {
                yinyong_sql = "and Referencesd like '%" + yinyong + "%' ";
            } else {
                yinyong_sql = " ";
            }
            if (!status.trim().equals("")) {
                status_sql = "and Status like '%" + status + "%' ";
            } else {
                status_sql = " ";
            }
            if (!Name.trim().equals("")) {
                Name_sql = "and Name like '%" + Name + "%' ";
            } else {
                Name_sql = " ";
            }
            if (!StartDepartmentName.trim().equals("")) {
                StartDepartmentName_sql = "and TiChuDepartmentName = '" + StartDepartmentName + "'";
            } else {
                StartDepartmentName_sql = "";
            }
            if (!RatifyDepartmentName.trim().equals("")) {
                RatifyDepartmentName_sql = "and RatifyDepartmentName = '" + RatifyDepartmentName + "'";
            } else {
                RatifyDepartmentName_sql = "";
            }
            if (!TypeNum.trim().equals("")) {
                TypeNum_sql = "and TypeNum = '" + TypeNum + "' ";
            } else {
                TypeNum_sql = "";
            }
            if (!ReleaseTime.trim().equals("")) {
                ReleaseTime_sql = "and ReleaseTime BETWEEN '" + ReleaseTime + "' AND '"+ReleaseTimeend+"'";
            } else {
                ReleaseTime_sql = "";
            }
            if (!EditorDepartmentName.trim().equals("")) {
                EditorDepartmentName_sql = "and  EditorDepartmentName = '" + EditorDepartmentName + "' ";
            } else {
                EditorDepartmentName_sql = "";
            }
            if (!Drafter.trim().equals("")) {
                Drafter_sql = "and Drafter = '" + Drafter + "' ";
            } else {
                Drafter_sql = "";
            }
            if (!StartTime.trim().equals("")) {
                StartTime_sql = "and StartTime BETWEEN '" + StartTime + "' AND '"+StartTimeend+"'";
            } else {
                StartTime_sql = "";
            }
              if(StartDepartmentName_sql!=""){
                 msql=StartDepartmentName_sql;
             }else {

              }
            if(msql==""){
                msql=Name_sql;
            }else if(EditorDepartmentName_sql!=""){
                msql=msql+""+Name_sql;
            }
              if(msql==""){
                  msql=EditorDepartmentName_sql;
              }else if(EditorDepartmentName_sql!=""){
                msql=msql+"  "+EditorDepartmentName_sql;
              }
            if(msql==""){
                msql=StartTime_sql;
            }else if(StartTime_sql!=""){
                msql=msql+"  "+StartTime_sql;
            }
            if(msql==""){
                msql=Drafter_sql;
            }else if(Drafter_sql!=""){
                msql=msql+"  "+Drafter_sql;
            }
            if(msql==""){
                msql=ReleaseTime_sql;
            }else if(ReleaseTime_sql!=""){
                msql=msql+"  "+ReleaseTime_sql;
            }
            if(msql==""){
                msql=TypeNum_sql;
            }else if(TypeNum_sql!=""){
                msql=msql+"  "+TypeNum_sql;
            }
            if(msql==""){
                msql=status_sql;
            }else if(status_sql!=""){
                msql=msql+"  "+status_sql;
            }
            if(msql==""){
                msql=yinyong_sql;
            }else if(yinyong_sql!=""){
                msql=msql+"  "+yinyong_sql;
            }
            if(msql==""){
                msql=caiyong_sql;
            }else if(caiyong_sql!=""){
                msql=msql+"  "+caiyong_sql;
            }
            if(msql==""){
                msql=RatifyDepartmentName_sql;
            }else if(RatifyDepartmentName_sql!=""){
                msql=msql+"  "+RatifyDepartmentName_sql;
            }
//            if (metemp) {
//                sql = "select * from " + OrderDBHelper.Standard_TABLE_NAME + "   Limit 10 Offset " + page + "";
//            } else {
                sql = "select * from " + OrderDBHelper.Standard_TABLE_NAME + " where  1=1 "  + msql   + " ORDER BY id "+ASC+ " Limit 10 Offset " + page;
//            }
            // select * from Orders
//            cursor = db.query(OrderDBHelper.SysUser_TABLE_NAME, ORDER_COLUMNS, null, null, null, null, null);
            Log.e("dddd","ffff"+sql);

            cursor = db.rawQuery(sql, null);

            if (cursor.getCount() > 0) {
                List<Standard> orderList = new ArrayList<Standard>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseOrder2(cursor));
                }
                return orderList;
            }
        } catch (Exception e) {
            Log.e("dddd","ffff"+e.toString());
             Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }
    String SourceName_sql="";
    String CagegoryName_sql="";
    //查询标准文件列表
    public long getListCount(Boolean metemp, String StartTime, String ReleaseTime, String TypeNum,
                                               String RatifyDepartmentName, String StartDepartmentName, String EditorDepartmentName,
                                               String Drafter, int page,String Name,String status,String Standards,String biaozhunlaiyuanname,String CagegoryName) throws Exception {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String sql = "";
        try {
            db = ordersDBHelper.getReadableDatabase();
            if (!CagegoryName.trim().equals("")) {
                CagegoryName_sql = "and CagegoryName = '" + CagegoryName + "'";
            } else {
                CagegoryName_sql = " ";
            }
            if (!biaozhunlaiyuanname.trim().equals("")) {
                SourceName_sql = "and SourceName = '" + biaozhunlaiyuanname + "'";
            } else {
                SourceName_sql = " ";
            }
            if (!status.trim().equals("")) {
                status_sql = "and Status like '%" + status + "%'";
            } else {
                status_sql = " ";
            }
            if (!Name.trim().equals("")) {
                Name_sql = "and Name = '" + Name + "'";
            } else {
                Name_sql = " ";
            }
            if (!StartDepartmentName.trim().equals("")) {
                StartDepartmentName_sql = "and TiChuDepartmentName = '" + StartDepartmentName + "'";
            } else {
                StartDepartmentName_sql = "";
            }
            if (!RatifyDepartmentName.trim().equals("")) {
                RatifyDepartmentName_sql = "and RatifyDepartmentName = '" + RatifyDepartmentName + "'";
            } else {
                RatifyDepartmentName_sql = "";
            }
            if (!TypeNum.trim().equals("")) {
                TypeNum_sql = "and Referencesd like '%" + TypeNum + "%' ";
            } else {
                TypeNum_sql = "";
            }
            if (!Standards.trim().equals("")) {
                Standards_sql = "and Standards like '%" + Standards + "%'";
            } else {
                Standards_sql = "";
            }
//            if (!ReleaseTime.trim().equals("")) {
//                ReleaseTime_sql = "and ReleaseTime = '" + ReleaseTime + "' ";
//            } else {
//                ReleaseTime_sql = "";
//            }
            if (!EditorDepartmentName.trim().equals("")) {
                EditorDepartmentName_sql = "and  EditorDepartmentName = '" + EditorDepartmentName + "'";
            } else {
                EditorDepartmentName_sql = "";
            }
            if (!Drafter.trim().equals("")) {
                Drafter_sql = "and Drafter = '" + Drafter + "'";
            } else {
                Drafter_sql = "";
            }
            if (!StartTime.trim().equals("")) {
                StartTime_sql = "and StartTime  BETWEEN '" + StartTime + "' AND '"+ReleaseTime+"'";
            } else {
                StartTime_sql = "";
            }
            if(StartDepartmentName_sql!=""){
                msql=StartDepartmentName_sql;
            }else {

            }
            if(msql==""){
                msql=Name_sql;
            }else if(EditorDepartmentName_sql!=""){
                msql=msql+""+Name_sql;
            }
            if(msql==""){
                msql=EditorDepartmentName_sql;
            }else if(EditorDepartmentName_sql!=""){
                msql=msql+"  "+EditorDepartmentName_sql;
            }
            if(msql==""){
                msql=StartTime_sql;
            }else if(StartTime_sql!=""){
                msql=msql+"  "+StartTime_sql;
            }
            if(msql==""){
                msql=Drafter_sql;
            }else if(Drafter_sql!=""){
                msql=msql+"  "+Drafter_sql;
            }
            if(msql==""){
                msql=ReleaseTime_sql;
            }else if(ReleaseTime_sql!=""){
                msql=msql+"  "+ReleaseTime_sql;
            }
            if(msql==""){
                msql=TypeNum_sql;
            }else if(TypeNum_sql!=""){
                msql=msql+"  "+TypeNum_sql;
            }
            if(msql==""){
                msql=status_sql;
            }else if(status_sql!=""){
                msql=msql+"  "+status_sql;
            }
            if(msql==""){
                msql=Standards_sql;
            }else if(status_sql!=""){
                msql=msql+"  "+Standards_sql;
            }
            if(msql==""){
                msql=RatifyDepartmentName_sql;
            }else if(RatifyDepartmentName_sql!=""){
                msql=msql+"  "+RatifyDepartmentName_sql;
            }
            if(msql==""){
                msql=SourceName_sql;
            }else if(SourceName_sql!=""){
                msql=msql+"  "+SourceName_sql;
            }
            if(msql==""){
                msql=CagegoryName_sql;
            }else if(CagegoryName_sql!=""){
                msql=msql+"  "+CagegoryName_sql;
            }
//            if (metemp) {
//                sql = "select * from " + OrderDBHelper.Standard_TABLE_NAME + "   Limit 10 Offset " + page + "";
//            } else {
            sql = "select count(*) from " + OrderDBHelper.Standard_TABLE_NAME + " where  1=1 "  + msql ;
//            }
            // select * from Orders
//            cursor = db.query(OrderDBHelper.SysUser_TABLE_NAME, ORDER_COLUMNS, null, null, null, null, null);
            Log.e("dddd","ffff"+sql);

            cursor = db.rawQuery(sql, null);
            Log.e("dddd","数量"+cursor.toString());

            Log.e("dddd","数量"+cursor.getCount());
            cursor.moveToFirst();
            long count = cursor.getLong(0);
            cursor.close();
           return count;
        } catch (Exception e) {
            Log.e("dddd","ffff"+e.toString());
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return 0;
    }
    //查询标准文件列表
    public List<Standard> getListBiaoZhunDate2(Boolean metemp, String StartTime, String ReleaseTime, String TypeNum,
                                              String RatifyDepartmentName, String StartDepartmentName, String EditorDepartmentName,
                                              String Drafter, int page,String Name,String status,String Standards,String biaozhunlaiyuanname,String CagegoryName) throws Exception {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String sql = "";
        try {
            db = ordersDBHelper.getReadableDatabase();
            if (!CagegoryName.trim().equals("")) {
                CagegoryName_sql = "and CagegoryName = '" + CagegoryName + "' ";
            } else {
                CagegoryName_sql = " ";
            }
            if (!biaozhunlaiyuanname.trim().equals("")) {
                SourceName_sql = "and SourceName = '" + biaozhunlaiyuanname + "' ";
            } else {
                SourceName_sql = " ";
            }
            if (!status.trim().equals("")) {
                status_sql = "and Status like '%" + status + "%' ";
            } else {
                status_sql = " ";
            }
            if (!Name.trim().equals("")) {
                Name_sql = "and Name like '%" + Name + "%' ";
            } else {
                Name_sql = " ";
            }
            if (!StartDepartmentName.trim().equals("")) {
                StartDepartmentName_sql = "and TiChuDepartmentName = '" + StartDepartmentName + "'";
            } else {
                StartDepartmentName_sql = "";
            }
            if (!RatifyDepartmentName.trim().equals("")) {
                RatifyDepartmentName_sql = "and RatifyDepartmentName = '" + RatifyDepartmentName + "'";
            } else {
                RatifyDepartmentName_sql = "";
            }
            if (!TypeNum.trim().equals("")) {
                TypeNum_sql = "and Referencesd like '%" + TypeNum + "%' ";
            } else {
                TypeNum_sql = "";
            }
            if (!Standards.trim().equals("")) {
                Standards_sql = "and Standards like '%" + Standards + "%' ";
            } else {
                Standards_sql = "";
            }
//            if (!ReleaseTime.trim().equals("")) {
//                ReleaseTime_sql = "and ReleaseTime = '" + ReleaseTime + "' ";
//            } else {
//                ReleaseTime_sql = "";
//            }
            if (!EditorDepartmentName.trim().equals("")) {
                EditorDepartmentName_sql = "and  EditorDepartmentName = '" + EditorDepartmentName + "' ";
            } else {
                EditorDepartmentName_sql = "";
            }
            if (!Drafter.trim().equals("")) {
                Drafter_sql = "and Drafter = '" + Drafter + "' ";
            } else {
                Drafter_sql = "";
            }
            if (!StartTime.trim().equals("")) {
                StartTime_sql = "and StartTime  BETWEEN '" + StartTime + "' AND '"+ReleaseTime+"'";
            } else {
                StartTime_sql = "";
            }
            if(StartDepartmentName_sql!=""){
                msql=StartDepartmentName_sql;
            }else {

            }
            if(msql==""){
                msql=Name_sql;
            }else if(EditorDepartmentName_sql!=""){
                msql=msql+""+Name_sql;
            }
            if(msql==""){
                msql=EditorDepartmentName_sql;
            }else if(EditorDepartmentName_sql!=""){
                msql=msql+"  "+EditorDepartmentName_sql;
            }
            if(msql==""){
                msql=StartTime_sql;
            }else if(StartTime_sql!=""){
                msql=msql+"  "+StartTime_sql;
            }
            if(msql==""){
                msql=Drafter_sql;
            }else if(Drafter_sql!=""){
                msql=msql+"  "+Drafter_sql;
            }
            if(msql==""){
                msql=ReleaseTime_sql;
            }else if(ReleaseTime_sql!=""){
                msql=msql+"  "+ReleaseTime_sql;
            }
            if(msql==""){
                msql=TypeNum_sql;
            }else if(TypeNum_sql!=""){
                msql=msql+"  "+TypeNum_sql;
            }
            if(msql==""){
                msql=status_sql;
            }else if(status_sql!=""){
                msql=msql+"  "+status_sql;
            }
            if(msql==""){
                msql=Standards_sql;
            }else if(status_sql!=""){
                msql=msql+"  "+Standards_sql;
            }
            if(msql==""){
                msql=RatifyDepartmentName_sql;
            }else if(RatifyDepartmentName_sql!=""){
                msql=msql+"  "+RatifyDepartmentName_sql;
            }
            if(msql==""){
                msql=SourceName_sql;
            }else if(SourceName_sql!=""){
                msql=msql+"  "+SourceName_sql;
            }
            if(msql==""){
                msql=CagegoryName_sql;
            }else if(CagegoryName_sql!=""){
                msql=msql+"  "+CagegoryName_sql;
            }
//            if (metemp) {
//                sql = "select * from " + OrderDBHelper.Standard_TABLE_NAME + "   Limit 10 Offset " + page + "";
//            } else {
            sql = "select * from " + OrderDBHelper.Standard_TABLE_NAME + " where  1=1 "  + msql +"order by ReleaseTime , StartTime ASC "+ " Limit 10 Offset " + page + "";
//            }
            // select * from Orders
//            cursor = db.query(OrderDBHelper.SysUser_TABLE_NAME, ORDER_COLUMNS, null, null, null, null, null);
            Log.e("dddd","ffff"+sql);

            cursor = db.rawQuery(sql, null);

            if (cursor.getCount() > 0) {
                List<Standard> orderList = new ArrayList<Standard>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseOrder2(cursor));
                }
                return orderList;
            }
        } catch (Exception e) {
            Log.e("dddd","ffff"+e.toString());
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");
            Calendar selectedDate = Calendar.getInstance();//系统当前时间
            Date mdate = selectedDate.getTime();

            insertDate(SPUtils.getInstance().getInt(Constants.SPUtilsConfig.SP_PHONE), ""+Name, ""+dateFormat.format(mdate),""+SPUtils.getInstance().getString(Constants.SPUtilsConfig.SP_LOGINNAME),
                    SPUtils.getInstance().getString(Constants.SPUtilsConfig.SP_REALNAME),SPUtils.getInstance().getString(Constants.SPUtilsConfig.SP_DepartmentName));

        }

        return null;
    }
    //查询标准文件列表
    public List<Standard> getSearchListDate( String key, String name,Integer page) throws Exception {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String sql = "";
        try {
            db = ordersDBHelper.getReadableDatabase();


            if (page==-1) {
                sql = "select * from " + OrderDBHelper.Standard_TABLE_NAME + " where  "  + key + " = '"+name+"' "  ;
            } else {
            sql = "select * from " + OrderDBHelper.Standard_TABLE_NAME + " where  "  + key + " = '"+name+"' " + " Limit 100 Offset " + page + "";
            }
            // select * from Orders
//            cursor = db.query(OrderDBHelper.SysUser_TABLE_NAME, ORDER_COLUMNS, null, null, null, null, null);

            cursor = db.rawQuery(sql, null);

            if (cursor.getCount() > 0) {
                List<Standard> orderList = new ArrayList<Standard>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseOrder2(cursor));
                }
                return orderList;
            }
        } catch (Exception e) {
            Log.e("dddd","ffff"+e.toString());
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }
    public List<Standard> getListStandardCollect(String collectType, int page) throws Exception {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String sql = "";

        try {
            db = ordersDBHelper.getReadableDatabase();

                sql = "select * from " + OrderDBHelper.Standard_TABLE_NAME + " where CollectType = '" + collectType + "' Limit 10 Offset " + page + "";

            // select * from Orders
//            cursor = db.query(OrderDBHelper.SysUser_TABLE_NAME, ORDER_COLUMNS, null, null, null, null, null);

            cursor = db.rawQuery(sql, null);

            if (cursor.getCount() > 0) {
                List<Standard> orderList = new ArrayList<Standard>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseOrder2(cursor));
                }
                return orderList;
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");
            Calendar selectedDate = Calendar.getInstance();//系统当前时间
            Date mdate = selectedDate.getTime();

//                            String start1 = "2020-02-28";//格式必须与formatter的格式⼀致
//                          var date = formatter.parse(start1);
           }

        return null;
    }
    public Integer getListStandardCount(String name, String EditorDepartmentName, String Drafter, String StartTime, int page) throws Exception {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String sql = "";

        try {
            db = ordersDBHelper.getReadableDatabase();
            if (!EditorDepartmentName.trim().equals("")) {
                EditorDepartmentName_sql = "and EditorDepartmentName = '" + EditorDepartmentName + "'";
            } else {
                EditorDepartmentName_sql = "";
            }
            if (!Drafter.trim().equals("")) {
                Drafter_sql = "and Drafter = '" + Drafter + "'";
            } else {
                Drafter_sql = "";
            }
            if (!StartTime.trim().equals("")) {
                StartTime_sql = "and StartTime = '" + StartTime + "'";
            } else {
                StartTime_sql = "";
            }
            if (name == "") {
                sql = "select * from " + OrderDBHelper.Standard_TABLE_NAME;
            } else {
                sql = "select * from " + OrderDBHelper.Standard_TABLE_NAME + " where Name like '%" + name + "%' " + EditorDepartmentName_sql ;
            }
            // select * from Orders
//            cursor = db.query(OrderDBHelper.SysUser_TABLE_NAME, ORDER_COLUMNS, null, null, null, null, null);

            cursor = db.rawQuery(sql, null);

            if (cursor.getCount() > 0) {
//                List<Standard> orderList = new ArrayList<Standard>(cursor.getCount());
//                while (cursor.moveToNext()) {
//                    orderList.add(parseOrder2(cursor));
//                }
                return cursor.getCount();
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");
            Calendar selectedDate = Calendar.getInstance();//系统当前时间
            Date mdate = selectedDate.getTime();

//                            String start1 = "2020-02-28";//格式必须与formatter的格式⼀致
//                          var date = formatter.parse(start1);
           }

        return 0;
    }

    public List<Standard> getListStandardDate(String name, String EditorDepartmentName, String Drafter, String StartTime, int page) throws Exception {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String sql = "";
        Log.e("dddddd","***"+page);
        try {
            db = ordersDBHelper.getReadableDatabase();
            if (!EditorDepartmentName.trim().equals("")) {
                EditorDepartmentName_sql = "and EditorDepartmentName = '" + EditorDepartmentName + "'";
            } else {
                EditorDepartmentName_sql = "";
            }
            if (!Drafter.trim().equals("")) {
                Drafter_sql = "and Drafter = '" + Drafter + "'";
            } else {
                Drafter_sql = "";
            }
            if (!StartTime.trim().equals("")) {
                StartTime_sql = "and StartTime = '" + StartTime + "'";
            } else {
                StartTime_sql = "";
            }
            if (name == "") {
                sql = "select * from " + OrderDBHelper.Standard_TABLE_NAME + "   Limit 10 Offset " + page + "";
            } else {
                sql = "select * from " + OrderDBHelper.Standard_TABLE_NAME + " where Name like '%" + name + "%' " + EditorDepartmentName_sql + " Limit 10 Offset " + page + "";
            }
            // select * from Orders
//            cursor = db.query(OrderDBHelper.SysUser_TABLE_NAME, ORDER_COLUMNS, null, null, null, null, null);

            cursor = db.rawQuery(sql, null);

            if (cursor.getCount() > 0) {
                List<Standard> orderList = new ArrayList<Standard>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseOrder2(cursor));
                }
                return orderList;
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");
            Calendar selectedDate = Calendar.getInstance();//系统当前时间
            Date mdate = selectedDate.getTime();

//                            String start1 = "2020-02-28";//格式必须与formatter的格式⼀致
//                          var date = formatter.parse(start1);
            insertDate(SPUtils.getInstance().getInt(Constants.SPUtilsConfig.SP_PHONE), name, dateFormat.format(mdate),SPUtils.getInstance().getString(Constants.SPUtilsConfig.SP_LOGINNAME),
                    SPUtils.getInstance().getString(Constants.SPUtilsConfig.SP_REALNAME),SPUtils.getInstance().getString(Constants.SPUtilsConfig.SP_DepartmentName));
        }

        return null;
    }

    /**
     * 查询数据库中所有数据
     */
    String RealName_sql = "";

    public List<User> getSelectDate(String name, String RealName, String DepartmentName, Integer page) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        if (!RealName.trim().equals("")) {
            RealName_sql = "and RealName = '" + RealName + "'";
        } else {
            RealName_sql = "";
        }
        if (!DepartmentName.trim().equals("")) {
            DepartmentName_sql = " and DepartmentName = '" + DepartmentName + "'";
        } else {
            DepartmentName_sql = "";
        }
        try {
            db = ordersDBHelper.getReadableDatabase();
            // select * from Orders
            String sql = "select * from " + OrderDBHelper.SysUser_TABLE_NAME + " where UserName like '%" + name + "%' " + RealName_sql + DepartmentName_sql + " Limit 10 Offset " + page + "";
            cursor = db.rawQuery(sql, null);

            if (cursor.getCount() > 0) {
                List<User> orderList = new ArrayList<User>(cursor.getCount());
                while (cursor.moveToNext()) {
                    try {
                        orderList.add(parseOrder(cursor));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return orderList;
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }

    public List<User> getSelectDate2(String name, String RealName, String DepartmentName, Integer page) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        if (!RealName.trim().equals("")) {
            RealName_sql = "and RealName = '" + RealName + "'";
        } else {
            RealName_sql = "";
        }
        if (!DepartmentName.trim().equals("")) {
            DepartmentName_sql = " and DepartmentName = '" + DepartmentName + "'";
        } else {
            DepartmentName_sql = "";
        }
        try {
            db = ordersDBHelper.getReadableDatabase();
            // select * from Orders
            String sql = "select * from " + OrderDBHelper.SysUser_TABLE_NAME + " where UserName = '" + name + "' " + RealName_sql + DepartmentName_sql  ;
            cursor = db.rawQuery(sql, null);

            if (cursor.getCount() > 0) {
                List<User> orderList = new ArrayList<User>(cursor.getCount());
                while (cursor.moveToNext()) {
                    try {
                        orderList.add(parseOrder(cursor));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return orderList;
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }
    /**
     * 查询数据库中所有数据
     */
    public List<User> getAllDate() {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = ordersDBHelper.getReadableDatabase();
            // select * from Orders
            cursor = db.query(OrderDBHelper.SysUser_TABLE_NAME, ORDER_COLUMNS, null, null, null, null, null);

            if (cursor.getCount() > 0) {
                List<User> orderList = new ArrayList<User>(cursor.getCount());
                while (cursor.moveToNext()) {
                    try {
                        orderList.add(parseOrder(cursor));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return orderList;
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }

    /**
     * 用户表新增一条数据
     */

    public boolean insertStatandDate(String id, String accountName, String userName, String company, String userpwd) {
        SQLiteDatabase db = null;
        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();
            // insert into Orders(Id, CustomName, OrderPrice, Country) values (7, "Jne", 700, "China");
            ContentValues contentValues = new ContentValues();
//            contentValues.put("id", id);
            contentValues.put("UserName", accountName);
            contentValues.put("RealName", userName);
            contentValues.put("DepartmentName", company);
            contentValues.put("Password", userpwd);
            contentValues.put("BaseCreateTime", userpwd);
            contentValues.put("BaseModifyTime", userpwd);
            contentValues.put("BaseCreatorId", userpwd);
            contentValues.put("BaseModifierId", userpwd);
            contentValues.put("DepartmentId", userpwd);
            contentValues.put("UserStatus", userpwd);
            contentValues.put("IsSystem", userpwd);
            contentValues.put("LoginCount", userpwd);
            db.insertOrThrow(OrderDBHelper.SysUser_TABLE_NAME, null, contentValues);

            db.setTransactionSuccessful();
            return true;
        } catch (SQLiteConstraintException e) {
            Toast.makeText(context, "主键重复", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    /**
     * 新增一条数据
     */

    public boolean insertUserDate(String id, String accountName, String userName, String company, String userpwd) {
        SQLiteDatabase db = null;
        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();
            // insert into Orders(Id, CustomName, OrderPrice, Country) values (7, "Jne", 700, "China");
            ContentValues contentValues = new ContentValues();
//            contentValues.put("id", id);
            contentValues.put("UserName", accountName);
            contentValues.put("RealName", userName);
            contentValues.put("DepartmentName", company);
            contentValues.put("Password", userpwd);
            contentValues.put("BaseCreateTime", userpwd);
            contentValues.put("BaseModifyTime", userpwd);
            contentValues.put("BaseCreatorId", userpwd);
            contentValues.put("BaseModifierId", userpwd);
            contentValues.put("DepartmentId", userpwd);
            contentValues.put("UserStatus", "待审核");
            contentValues.put("IsSystem", userpwd);
            contentValues.put("LoginCount", userpwd);
            db.insertOrThrow(OrderDBHelper.SysUser_TABLE_NAME, null, contentValues);

            db.setTransactionSuccessful();
            return true;
        } catch (SQLiteConstraintException e) {
            Toast.makeText(context, "主键重复", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }
    /**
     * 增加来源记录
     */
    public Integer insertBiaoZhunTiXi(String SourseName,String mkey) {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();
            db.execSQL("insert into " + OrderDBHelper.StandardSystem_TABLE_NAME + " ( "+mkey+" ) values (  '" + SourseName + "')");
            db.setTransactionSuccessful();
            return 0;
        } catch (SQLiteConstraintException e) {
            Toast.makeText(context, "主键重复", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            return -1;
//            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return -1;
    }
    /**
     * 增加来源记录
     */
    public Integer updateBiaoZhunTiXi(String SourseName,String mkey,String soursename) {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();
            db.execSQL("update  " + OrderDBHelper.StandardSystem_TABLE_NAME + " set "+mkey+" = '" + SourseName + "' where StandardName = '"+soursename+"' ");
            db.setTransactionSuccessful();
            return 0;
        } catch (SQLiteConstraintException e) {
            Toast.makeText(context, "主键重复", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("dddddd",  e.toString());

            return -1;
         } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return -1;
    }
    /**
     * 增加来源记录
     */
    public Integer insertLaiYuan(String SourseName) {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();
            db.execSQL("insert into " + OrderDBHelper.StandardLaiYuan_TABLE_NAME + " ( Name ) values (  '" + SourseName + "')");
            db.setTransactionSuccessful();
            return 0;
        } catch (SQLiteConstraintException e) {
            Toast.makeText(context, "主键重复", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            return -1;
//            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return -1;
    }
    /**
     * 修改来源记录
     */
    public Integer updateLaiYuan(Integer id,String SourseName) {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();
//            db.execSQL("update into " + OrderDBHelper.StandardLaiYuan_TABLE_NAME + " ( Name ) values (  '" + SourseName + "')");
//            db.setTransactionSuccessful();
//            db.beginTransaction();

            // update Orders set OrderPrice = 800 where Id = 6
            ContentValues cv = new ContentValues();
            cv.put("Name", SourseName);
            db.update(OrderDBHelper.StandardLaiYuan_TABLE_NAME,
                    cv,
                    "id = ?",
                    new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
            return 0;
        } catch (SQLiteConstraintException e) {
            Toast.makeText(context, "主键重复", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "", e);
            return -1;
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return -1;
    }
    /**
     * 新增一条数据
     */
    public boolean insertDate(Integer id, String Keyword, String BaseCreateTime,String loginname, String username, String DepartmentName) {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();
            db.execSQL("insert into " + OrderDBHelper.SysLog_TABLE_NAME + " (UserId, Keyword, BaseCreateTime , UserName , RealName , DepartmentName ) values ( " + id + " , '" + Keyword + "' , '" + BaseCreateTime + "' , '" + loginname+ "' , '" + username+ "' , '" + DepartmentName + "')");
            db.setTransactionSuccessful();
            return true;
        } catch (SQLiteConstraintException e) {
            Toast.makeText(context, "主键重复", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    /**
     * 新增一条数据
     */
    public boolean insertLogDate() {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();

            // insert into Orders(Id, CustomName, OrderPrice, Country) values (7, "Jne", 700, "China");
            ContentValues contentValues = new ContentValues();
            contentValues.put("Id", 7);
            contentValues.put("CustomName", "Jne");
            contentValues.put("OrderPrice", 700);
            contentValues.put("Country", "China");
            db.insertOrThrow(OrderDBHelper.SysUser_TABLE_NAME, null, contentValues);

            db.setTransactionSuccessful();
            return true;
        } catch (SQLiteConstraintException e) {
            Toast.makeText(context, "主键重复", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    //删除分类
    public boolean deleteFenLei(String id,String name) {
        SQLiteDatabase db = null;
        String sql="";
        if(name==""){
            sql="and StandardName = '"+id+"'";
        }else {
            sql="and StandardsSecondName = '"+name+"' ";

        }
        try {
//            db = ordersDBHelper.getWritableDatabase();
//            db.beginTransaction();
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();
            db.execSQL("delete from " + OrderDBHelper.StandardSystem_TABLE_NAME +" where 1=1 "+ sql );
            db.setTransactionSuccessful();

            return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }
    //删除部门
    public boolean deleteDepart(Integer id) {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();

            // delete from Orders where Id = 7
            db.delete(OrderDBHelper.Department_TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }
    /**
     *
     */
    public boolean deleteOrder(Integer id) {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();

            // delete from Orders where Id = 7
            db.delete(OrderDBHelper.SysUser_TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    /**
     * 修改一条数据  此处将Id为6的数据的OrderPrice修改了800
     */
    public boolean updateOrder() {
        SQLiteDatabase db = null;
        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();

            // update Orders set OrderPrice = 800 where Id = 6
            ContentValues cv = new ContentValues();
            cv.put("OrderPrice", 800);
            db.update(OrderDBHelper.SysUser_TABLE_NAME,
                    cv,
                    "Id = ?",
                    new String[]{String.valueOf(6)});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }

        return false;
    }

//    /**
//     * 数据查询  此处将用户名为"Bor"的信息提取出来
//     */
//    public List<Order> getBorOrder(){
//        SQLiteDatabase db = null;
//        Cursor cursor = null;
//
//        try {
//            db = ordersDBHelper.getReadableDatabase();
//
//            // select * from Orders where CustomName = 'Bor'
//            cursor = db.query(OrderDBHelper.TABLE_NAME,
//                    ORDER_COLUMNS,
//                    "CustomName = ?",
//                    new String[] {"Bor"},
//                    null, null, null);
//
//            if (cursor.getCount() > 0) {
//                List<Order> orderList = new ArrayList<Order>(cursor.getCount());
//                while (cursor.moveToNext()) {
//                    Order order = parseOrder(cursor);
//                    orderList.add(order);
//                }
//                return orderList;
//            }
//        }
//        catch (Exception e) {
//            Log.e(TAG, "", e);
//        }
//        finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            if (db != null) {
//                db.close();
//            }
//        }
//
//        return null;
//    }

    /**
     * 统计查询  此处查询Country为China的用户总数
     */
    public int getChinaCount() {
        int count = 0;

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = ordersDBHelper.getReadableDatabase();
            // select count(Id) from Orders where Country = 'China'
            cursor = db.query(OrderDBHelper.SysUser_TABLE_NAME,
                    new String[]{"COUNT(Id)"},
                    "Country = ?",
                    new String[]{"China"},
                    null, null, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return count;
    }

    //    /**
//     * 比较查询  此处查询单笔数据中OrderPrice最高的
//     */
//    public Order getMaxOrderPrice(){
//        SQLiteDatabase db = null;
//        Cursor cursor = null;
//
//        try {
//            db = ordersDBHelper.getReadableDatabase();
//            // select Id, CustomName, Max(OrderPrice) as OrderPrice, Country from Orders
//            cursor = db.query(OrderDBHelper.TABLE_NAME, new String[]{"Id", "CustomName", "Max(OrderPrice) as OrderPrice", "Country"}, null, null, null, null, null);
//
//            if (cursor.getCount() > 0){
//                if (cursor.moveToFirst()) {
//                    return parseOrder(cursor);
//                }
//            }
//        }
//        catch (Exception e) {
//            Log.e(TAG, "", e);
//        }
//        finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            if (db != null) {
//                db.close();
//            }
//        }
//
//        return null;
//    }
    private SysLog parseOrder4(Cursor cursor) {
        SysLog order = new SysLog();
        int id = cursor.getInt(cursor.getColumnIndex("id"));
        order.id = id;
        order.BaseCreateTime = cursor.getString(cursor.getColumnIndex("BaseCreateTime"));

        order.Keyword = cursor.getString(cursor.getColumnIndex("Keyword"));
        ;
        order.UserId = cursor.getString(cursor.getColumnIndex("UserId"));
        order.DepartmentName = cursor.getString(cursor.getColumnIndex("DepartmentName"));
        order.RealName = cursor.getString(cursor.getColumnIndex("RealName"));
        order.UserName = cursor.getString(cursor.getColumnIndex("UserName"));

        return order;
    }
    private StandardSystem parseOrder5(Cursor cursor) {
        StandardSystem order = new StandardSystem();
        int id = cursor.getInt(cursor.getColumnIndex("id"));
        order.id = id;
        order.StandardId = cursor.getString(cursor.getColumnIndex("StandardId"));
        ;
        order.StandardName = cursor.getString(cursor.getColumnIndex("StandardName"));
        ;
        order.StandardsSecondId = cursor.getString(cursor.getColumnIndex("StandardsSecondId"));
        ;
        order.StandardsSecondName = cursor.getString(cursor.getColumnIndex("StandardsSecondName"));
        ;
        order.StandardsThirdId = cursor.getString(cursor.getColumnIndex("StandardsThirdId"));
        ;
        order.StandardsThirdName = cursor.getString(cursor.getColumnIndex("StandardsThirdName"));
        ;

        return order;
    }
    /**
     * 将查找到的数据转换成Order类
     */
    private SysLaiYuan parseOrder6(Cursor cursor) {
        SysLaiYuan order = new SysLaiYuan();
        int id = cursor.getInt(cursor.getColumnIndex("id"));
        order.id = id;
        order.Name = cursor.getString(cursor.getColumnIndex("Name"));
        return order;
    }
    /**
     * 将查找到的数据转换成Order类
     */
    private SysDepartment parseOrder3(Cursor cursor) {
        SysDepartment order = new SysDepartment();
        int id = cursor.getInt(cursor.getColumnIndex("id"));
        order.id = id;
        order.BaseCreateTime = cursor.getString(cursor.getColumnIndex("BaseCreateTime"));
        ;
        order.BaseCreatorId = cursor.getString(cursor.getColumnIndex("BaseCreatorId"));
        ;
        order.DepartmentName = cursor.getString(cursor.getColumnIndex("DepartmentName"));
        ;
        order.DepartmentId = cursor.getString(cursor.getColumnIndex("DepartmentId"));
        ;
        order.BaseModifierId = cursor.getString(cursor.getColumnIndex("BaseModifierId"));
        ;
        order.BaseModifyTime = cursor.getString(cursor.getColumnIndex("BaseModifyTime"));
        ;
        order.DepartmentSort = cursor.getString(cursor.getColumnIndex("DepartmentSort"));
        ;
        order.DepartmentTwoName = cursor.getString(cursor.getColumnIndex("DepartmentTwoName"));
        ;
        order.DepartmentType = cursor.getString(cursor.getColumnIndex("DepartmentType"));
        ;
        order.Telephone = cursor.getString(cursor.getColumnIndex("Telephone"));
        ;
        order.Remark = cursor.getString(cursor.getColumnIndex("Remark"));
        ;
        return order;
    }

    /**
     * 将查找到的数据转换成Order类
     */
    private Standard parseOrder2(Cursor cursor) {
        Standard order = new Standard();
        try {

//            BeanUtils.CursorToList(cursor, orderd );
        } catch (Exception e) {
            e.printStackTrace();
        }
//        cursor.getClass();
        int id = cursor.getInt(cursor.getColumnIndex("id"));
//        String UserName=cursor.getString(cursor.getColumnIndex("UserName"));
//        String RealName=cursor.getString(cursor.getColumnIndex("RealName"));
//        String DepartmentName=cursor.getString(cursor.getColumnIndex("DepartmentName"));
//        String DepartmentId=cursor.getString(cursor.getColumnIndex("DepartmentId"));
//        String Password=cursor.getString(cursor.getColumnIndex("Password"));
//
        order.id = id;
        order.BaseCreateTime = cursor.getString(cursor.getColumnIndex("BaseCreateTime"));
        ;
        order.BaseCreatorId = cursor.getString(cursor.getColumnIndex("BaseCreatorId"));
        ;
//        order.DepartmentName = cursor.getString(cursor.getColumnIndex("DepartmentName"));;
//        order.DepartmentId = cursor.getString(cursor.getColumnIndex("DepartmentId"));;
        order.BaseModifierId = cursor.getString(cursor.getColumnIndex("BaseModifierId"));
        ;
        order.BaseModifyTime = cursor.getString(cursor.getColumnIndex("BaseModifyTime"));
        ;
        order.Drafter = cursor.getString(cursor.getColumnIndex("Drafter"));
        order.Drafter = cursor.getString(cursor.getColumnIndex("Drafter"));
        order.CollectType = cursor.getString(cursor.getColumnIndex("CollectType"));
        ;
        order.EditorDepartmentName = cursor.getString(cursor.getColumnIndex("EditorDepartmentName"));
        ;
        order.FilePath = cursor.getString(cursor.getColumnIndex("FilePath"));
        ;
        order.Name = cursor.getString(cursor.getColumnIndex("Name"));
        ;
        order.RatifyDepartmentId = cursor.getString(cursor.getColumnIndex("RatifyDepartmentId"));
        ;
        order.RatifyDepartmentName = cursor.getString(cursor.getColumnIndex("RatifyDepartmentName"));
        ;
        order.Referencesd = cursor.getString(cursor.getColumnIndex("Referencesd"));
        ;
        order.ReleaseTime = cursor.getString(cursor.getColumnIndex("ReleaseTime"));
        ;
        order.Remark = cursor.getString(cursor.getColumnIndex("Remark"));
        ;
        order.Standards = cursor.getString(cursor.getColumnIndex("Standards"));
        ;
        order.StartDepartmentId = cursor.getString(cursor.getColumnIndex("StartDepartmentId"));
        ;
        order.StartDepartmentName = cursor.getString(cursor.getColumnIndex("StartDepartmentName"));
        ;
        order.StartTime = cursor.getString(cursor.getColumnIndex("StartTime"));
        ;
        order.Status = cursor.getString(cursor.getColumnIndex("Status"));
        ;
        order.TypeNum = cursor.getString(cursor.getColumnIndex("TypeNum"));
        ;
        order.BiaoNo = cursor.getString(cursor.getColumnIndex("BiaoNo"));
        ;
        order.SourceName = cursor.getString(cursor.getColumnIndex("SourceName"));
        ;
        order.SameClassNo = cursor.getString(cursor.getColumnIndex("SameClassNo"));
        ;
        order.SameTopicNo = cursor.getString(cursor.getColumnIndex("SameTopicNo"));
        ;
        order.ReplaceNo = cursor.getString(cursor.getColumnIndex("ReplaceNo"));
        ;
        order.ByReplaceNo = cursor.getString(cursor.getColumnIndex("ByReplaceNo"));
        ;
        order.TiChuDepartmentName = cursor.getString(cursor.getColumnIndex("TiChuDepartmentName"));
        ;
        order.BzKeywords = cursor.getString(cursor.getColumnIndex("BzKeywords"));
        order.CagegoryName = cursor.getString(cursor.getColumnIndex("CagegoryName"));
        order.Pages = cursor.getString(cursor.getColumnIndex("Pages"));
        order.TypeName = cursor.getString(cursor.getColumnIndex("TypeName"));

        return order;
    }

    /**
     * 将查找到的数据转换成User类
     */
    private User parseOrder(Cursor cursor) {
//        User order =new User();

        User order = new User();
//        try {
////            BeanUtils.copyProperties(cursor, orderd);
////           BeanUtils.copyProperty(orderd,"",cursor);
////            Convert.convert(Class<T> target, Object source)
////            BeanUtils.CursorToList(cursor, orderd );
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        int id = cursor.getInt(cursor.getColumnIndex("id"));
        String UserName = cursor.getString(cursor.getColumnIndex("UserName"));
        String RealName = cursor.getString(cursor.getColumnIndex("RealName"));
        String DepartmentName = cursor.getString(cursor.getColumnIndex("DepartmentName"));
        String DepartmentId = cursor.getString(cursor.getColumnIndex("DepartmentId"));
        String Password = cursor.getString(cursor.getColumnIndex("Password"));
        String userStatus = cursor.getString(cursor.getColumnIndex("UserStatus"));


        order.id = id;
        order.UserName = UserName;
        order.RealName = RealName;
        order.DepartmentName = DepartmentName;
        order.DepartmentId = DepartmentId;
        order.Password = Password;
        order.setUserStatus(userStatus);

        return order;
    }
}
