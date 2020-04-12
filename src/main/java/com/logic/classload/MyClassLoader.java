package com.logic.classload;
import sun.reflect.CallerSensitive;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;


/**类加载器
 * Created by Wang Guodong on 2017/7/26.
 */
public class MyClassLoader extends ClassLoader {
    private String name;
    private String path = "d:\\";
    private String fileType = ".class";
    public MyClassLoader(String name) {
        super();
        this.name = name;
        System.out.println("MyClassLoader 被加载通过 :" + this.getClass().getClassLoader());
    }
    public MyClassLoader(ClassLoader parent, String name) {
        super(parent);
        this.name = name;
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] data = this.loadClassData(name);
        return this.defineClass(name, data, 0, data.length);
    }

    private byte[] loadClassData(String name) {
        InputStream is = null;
        byte[] data = null;
        ByteArrayOutputStream baos = null;
        try {
            this.name = this.name.replace(".", "\\");
            is = new FileInputStream(new File(path + name + fileType));
            baos = new ByteArrayOutputStream();
            int ch = 0;
            while (-1 != (ch = is.read())) {
                baos.write(ch);
            }
            data = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    @CallerSensitive
    public static void main(String[] args) {
        MyClassLoader loader1 = new MyClassLoader("loader1");
        System.out.println("========="+loader1);
        loader1.setPath("d:\\app\\");
        try {
            test(loader1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @CallerSensitive
    public static void test(ClassLoader loader) throws Exception {
        Class<?> clazz = loader.loadClass("ADog");

        final Method mainClass = clazz.getMethod("main");
        mainClass.invoke(null);
    }
}
