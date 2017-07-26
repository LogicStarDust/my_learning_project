package com.logic.classload;

import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

class Cus implements Runnable {
    public Cus() {
        System.out.println("Cus is loaded by :" + this.getClass().getClassLoader());
    }
    @CallerSensitive
    public void aa() {

        System.out.println("Reflection.getCallerClass " + Reflection.getCallerClass());
        System.out.println("Reflection.getCallerClass " + Reflection.getCallerClass(1));
        System.out.println("Reflection.getCallerClass " + Reflection.getCallerClass().getClassLoader());
    }

    public void run() {
        System.out.println("Cus ContextClassLoader is " + Thread.currentThread().getContextClassLoader());
        System.out.println("cus get Property of system set by Adog:" + System.getProperty("testPro"));
    }
}

public class ADog {
    public ADog() {
        System.out.println("ADog 被加载通过：" + this.getClass().getClassLoader());
    }
    @CallerSensitive
    public static void main() {


        //设置参数
        System.setProperty("testPro", "ADog is a good dog");

        System.out.println("Cus gets Property of System set by user: " + System.getProperty("test"));
        System.out.println(Thread.currentThread().getContextClassLoader() + "： ADog ContextClassLoader 开始");
        MyClassLoader loader = new MyClassLoader("loader1");
        loader.setPath("D:\\app\\");

        Thread.currentThread().setContextClassLoader(loader);


        try {
            test(Thread.currentThread().getContextClassLoader());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getContextClassLoader() + "：Adog ContextClassLoader end");
        Cus c = new Cus();
        c.aa();
        Thread t = new Thread(c);

        t.setContextClassLoader(Thread.currentThread().getContextClassLoader().getParent());
        t.start();
        System.out.println(Thread.currentThread().getContextClassLoader() + ":ADog ContextClassLoader stop");
    }

    public static void test(ClassLoader loader) throws Exception {

        Class<?> clazz = loader.loadClass("Sample");

        Object object = clazz.newInstance();

        ADog aDog = new ADog();
    }
}
