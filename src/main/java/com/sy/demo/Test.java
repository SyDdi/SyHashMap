package main.java.com.sy.demo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by myMsi on 2017/5/2.
 */
public class Test {
    public static void main(String[] args) {
        long start1 = System.currentTimeMillis();
        SyMap syMap = new SYHashMap();
        for (int i = 0 ;i <10000 ;i++){
            syMap.put("key"+i,"value"+i);
        }

        for (int i = 0 ;i <10000 ;i++){
            System.out.println("key:"+i +" value:" +syMap.get("key"+i));
        }

        long end1 = System.currentTimeMillis() - start1;
        long start2 = System.currentTimeMillis();
        System.out.println("-------------------------标准割-------------------------");
        Map map= new HashMap();
        for (int i = 0 ;i <10000 ;i++){
            map.put("key"+i,"value"+i);
        }

        for (int i = 0 ;i <10000 ;i++){
            System.out.println("key:"+i +" value:" +map.get("key"+i) );
        }
        long end2 = System.currentTimeMillis() - start2;
        System.out.println("SyMapTime:"+end1 +"JdkMapTime:"+end2);

    }
}
