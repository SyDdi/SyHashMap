package main.java.com.sy.demo;

/**
 * Created by myMsi on 2017/5/2.
 * 双列集合借口SyMap
 * @author shenyong
 */
public interface SyMap<K ,V> {

    public V put(K k,V v);

    public V get(K k);

    public interface Entry<K,V>{
        public K getKey();
        public V getValue();
    }
}
