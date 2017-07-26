package com.logic.classload;

/**
 * Created by Wang Guodong on 2017/7/27.
 */
public class Sample {
    public Sample(){
        System.out.println("Sample is loaded by :"+ this.getClass().getClassLoader());
    }
    public static void main(){
        new Sample();
    }
}
