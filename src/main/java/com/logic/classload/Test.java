package com.logic.classload;

import sun.reflect.Reflection;

/**
 * Created by Wang Guodong on 2017/7/27.
 */
public class Test {
    public static void main(String[] args){
        System.out.println("Reflection.getCallerClass "+ Reflection.getCallerClass());
    }
}
