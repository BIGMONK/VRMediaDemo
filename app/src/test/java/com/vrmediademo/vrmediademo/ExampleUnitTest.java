package com.vrmediademo.vrmediademo;

import org.junit.Test;

import java.util.HashMap;
import java.util.Hashtable;

import static java.lang.System.currentTimeMillis;
import static junit.framework.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void mapTest() {
        HashMap<Integer, Float> hashMap = new HashMap<>();
        Hashtable<Integer, Float> hashTable = new Hashtable<>();
        for (int i = 0; i < 100000; i++) {
            hashMap.put(i, (float) i);
            hashTable.put(i, (float) i);
        }
        long mapS = currentTimeMillis();
        for (Integer i : hashMap.keySet()) {
//            System.out.println("hashMap=" + hashMap.get(i));
        }
        System.out.println("hashMap time end=" + (currentTimeMillis() - mapS));

        long tableS = System.currentTimeMillis();
        for (Integer i : hashTable.keySet()) {
//            System.out.println("hashTable=" + hashTable.get(i));
        }
        System.out.println("hashMap time end=" + (currentTimeMillis() - tableS));

        long s = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            if (hashMap.containsKey(5000)) ;
        }
        System.out.println("sss" + (System.currentTimeMillis() - s));

        long s1 = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            if (hashTable.containsKey(5000)) ;
        }
        System.out.println("sss" + (System.currentTimeMillis() - s1));
        long s11 = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            if (hashTable.get(5000) != null) ;
        }
        System.out.println("sss" + (System.currentTimeMillis() - s11));

    }

    @Test
    public void objTest(){
        class CC{
            int i;
            int k;
        }



    }
}