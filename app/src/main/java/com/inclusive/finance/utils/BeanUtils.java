package com.inclusive.finance.utils;// 下面这个类是根据项目实际情况写的，写的有点死。

import android.database.Cursor;
import android.util.Log;

import com.inclusive.finance.room.MyObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class BeanUtils {
 	public static List CursorToList(Cursor cursor, MyObject bean){
		List resultList=new ArrayList();
		if(cursor!=null){
			String type="";
			String name="";
			HashMap<String,String> hashMap=new HashMap<>();
			List<HashMap<String,String>> hashMapList=getFiledsInfo(bean.getClass());
			while (cursor.moveToNext()){
				MyObject bean1;
				bean1=(MyObject)bean.clone();
				for (int i = 0; i < hashMapList.size(); i++) {
					hashMap=hashMapList.get(i);
					type=hashMap.get("type");
					name=hashMap.get("name");
					if("class java.lang.Integer".equals(type) || "int".equalsIgnoreCase(type)){
						int typeResult=cursor.getInt(cursor.getColumnIndex(name));
						bean1=setFieldValueByName(bean,name,"int",typeResult);
					}else if("class java.lang.String".equals(type)){
						String typeResult=cursor.getString(cursor.getColumnIndex(name));
						bean1=setFieldValueByName(bean,name,"string",typeResult);
					}else if("class java.lang.Long".equals(type)){
						long typeResult=cursor.getLong(cursor.getColumnIndex(name));
						bean1=setFieldValueByName(bean,name,"long",typeResult);
					}
				}
				resultList.add(bean1);
			}
		}
		return resultList;
	}
	/**
	 * 获取属性类型(type)，属性名(name)的map组成的list
	 *
	 */
	private static List getFiledsInfo(Class<?> o){
		Field[] fields=o.getDeclaredFields();
		List list = new ArrayList();
		Map infoMap=null;
		for(int i=0;i<fields.length;i++){
			if(!fields[i].isSynthetic() && !"serialVersionUID".equals(fields[i].getName())) {
				infoMap = new HashMap();
				infoMap.put("type", fields[i].getType().toString());
				infoMap.put("name", fields[i].getName());
				list.add(infoMap);
			}
		}
		return list;
	}
	/**
	 * 根据属性名赋予属性值
	 *
	 */
	private static MyObject setFieldValueByName(MyObject o, String fieldName, String types, Object values) {
		if(o!=null)
			try {
				String firstLetter = fieldName.toUpperCase();
				String getter = "set" + firstLetter + fieldName;
				Method method=null;
				if("int".equalsIgnoreCase(types))
					//Integer和int不同，如果object里面用的是int，这里就用int.class
					method = o.getClass().getMethod(getter,int.class);
				else if("string".equalsIgnoreCase(types))
					method = o.getClass().getMethod(getter,String.class);
				else if("long".equalsIgnoreCase(types))
					method = o.getClass().getMethod(getter,long.class);
				method.invoke(o, values);
			} catch (Exception e) {

			}
		return o;
	}


}