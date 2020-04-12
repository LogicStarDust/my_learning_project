package com.logic;

import com.logic.functional.programming.chapter2.Test2_2$;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Wang Guodong wangguodong@richinfo.cn
 */
public class TestJava {
    static public void main(String[] args){
        int a=Test2_2$.MODULE$.fib(4);
        System.out.println(a);
        HashMap<String, String> hm=new HashMap<String,String>(100);
    }
}
