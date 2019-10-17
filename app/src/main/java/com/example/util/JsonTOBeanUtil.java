package com.example.util;

import android.util.Log;

import com.example.bottomnavigationabar2.CommonJSONParser;
import com.example.bottomnavigationabar2.Post;
import com.example.bottomnavigationabar2.bean.ResultBean;
import com.example.bottomnavigationabar2.bean.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.beans.*;

/**
 * 创建于2019/9/25 14:54🐎
 */
public class JsonTOBeanUtil<T>{
    private static final String TAG = "JsonTOBeanUtil";

    public static <T> Object getBean(Class<T> bean, String responseData) {
        Map<String, Object> map = CommonJSONParser.parse(responseData);
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("data");
        System.out.println("list长度"+list.size());
        Iterator<String> iterator = map.keySet().iterator();
        List<T> posts = new ArrayList<>();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd:mm:ss");
        T t = null;
        for (Map<String, Object> param : list) {
            int i=0;
            try {
                t = bean.newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            Iterator<String> paramName = param.keySet().iterator();
            while (paramName.hasNext()) {
                String s = paramName.next();
                System.out.println("键----=" + s);
                String name = s.substring(0, 1).toUpperCase() + s.substring(1);
                System.out.println(s);
                Object args = param.get(s);
                System.out.println(args);
                if (args != null) {
                    try {
                        Method method = bean.getMethod("set" + name, args.getClass());
                        method.invoke(t, args);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
            posts.add(t);
        }
        if(posts.size()<=1)
            return t;
        else
            return posts;
    }

    public static  ResultBean getResultBean(String responseData){
        Map<String, Object> map = CommonJSONParser.parse(responseData);
        ResultBean bean = new ResultBean();
        bean.setCode((Integer) map.get("code"));
        bean.setMsg((String) map.get("msg"));
        return bean;
    }

    public static <T> T getBeanSingleData(Class<T> bean, String responseData) {
        Map<String, Object> map = CommonJSONParser.parse(responseData);
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        Iterator<String> iterator =data.keySet().iterator();
        List<T> posts = new ArrayList<>();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd:mm:ss");
        T t = null;
        try {
            t = bean.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        while (iterator.hasNext()) {
            String s = iterator.next();
            System.out.println("键----=" + s);
            String name = s.substring(0, 1).toUpperCase() + s.substring(1);
            System.out.println(s);
            Object args = data.get(s);
            System.out.println(args);
            if (args != null) {
                try {
                    Method method = bean.getMethod("set" + name, args.getClass());
                    method.invoke(t, args);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return t;
        }


/*    private static void getMethod(Class clazz){
        Field[] fields = clazz.getDeclaredFields();
        Method[] methods = new Method[fields.length];

        int index=0;
        for(Field field:fields){
            String name = field.getName().substring(0,1).toUpperCase()+field.getName().substring(1);
            Method method = null;
            try {
                method = clazz.getMethod("set"+name,field.getType());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            methods[index++]=method;
       }
        for(Method method:methods){
            System.out.println(method.getName());
        }
        return methods;
    }*/
}

