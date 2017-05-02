package main.java.com.sy.demo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by myMsi on 2017/5/2.
 */
public class SYHashMap<K,V> implements SyMap<K,V > {

    //定义默认数组大小
    private static int defaultLength = 16;
    //扩容标准 所使用数组数量/数组长度 > 0.75
    private static double defaultAddSizeFactor = 0.75;
    //使用数组位置的总数
    private int useSize;
    //定义骨架 之 数组
    private Entry<K,V>[] table = null;

    public SYHashMap(){
        this(defaultLength,defaultAddSizeFactor);
    }

    public SYHashMap(int length , double defaultAddSizeFactor) {
        if(length<0){
            throw new IllegalArgumentException("参数不能为负数"+length);
        }
        if(defaultAddSizeFactor <= 0 || Double.isNaN(defaultAddSizeFactor)){
            throw new IllegalArgumentException("扩容因子必须大于0 "+defaultAddSizeFactor);
        }
        this.defaultLength = length;
        this.defaultAddSizeFactor = defaultAddSizeFactor;
        table = new Entry[defaultLength];
    }

    /**
     * 存
     * @param k
     * @param v
     * @return
     */
    @Override
    public V put(K k, V v) {
        //判断当前数组大小 如果大于默认大小乘上扩容因子 就进行扩容
        if(useSize > defaultLength*defaultAddSizeFactor){
            up2Size();
        }
        //通过key来存储位置
        int index = getIndex(k,table.length);
        Entry<K,V> entry = table[index];
        if(entry == null ){
            table[index] = new Entry<>(k,v,null);
            useSize++;
        }else{
            table[index] = new Entry<>(k,v,entry);
        }
        return table[index].getValue();
    }

    /**
     * 通过自身数组的长度和key 来确定存储的位置
     * @param k
     * @param length 限制hashCode值得最大范围
     * @return
     */
    private int getIndex(K k,int length){
        //hash 与运算
        int m = length -1;
        int index = hash(k.hashCode()) & m;
        //判断一下 保证数据有效性
        return index >= 0 ? index : -index;
    }

    /**
     * 创建自己的hash算法
     * @param hashCode
     * @return
     */
    private int hash(int hashCode) {
        hashCode = hashCode^(( hashCode>>> 20 )^(hashCode>>> 12 ));
        return hashCode^(( hashCode>>> 7 )^(hashCode>>> 4 ));
    }

    /**
     * 扩容2倍
     */
    private void up2Size(){
        Entry<K,V>[] newTable = new Entry[2*defaultLength];
        //老数组里面有很多Entry对象这个对象的位置散落数组的各个位置 需要在次散列
        againHash(newTable);
    }

    private void againHash(SYHashMap<K,V>.Entry<K, V>[] newTable) {
        List<Entry<K,V>> entryList = new ArrayList<>();
        for(int i = 0 ; i < table.length ;i++){
            if(table[i] == null ){
                continue;
            }
            foundEntryByNext(table[i],entryList);
        }
        if(entryList.size()>0){
            useSize = 0;
            defaultLength = 2*defaultLength;
            table = newTable;
            for(Entry<K,V> entry:entryList){
                if(entry.next != null ){
                    //把形成的链表关系取消
                    entry.next = null ;
                }
                put(entry.getKey(),entry.getValue());
            }
        }
    }

    /**
     * 把老数组里面的 数据 递归加到 list里面去
     * @param entry
     * @param entryList
     */
    private void foundEntryByNext(Entry<K, V> entry, List<Entry<K, V>> entryList) {
        if(entry != null && entry.next != null ){
            //说明这个是一个 链表结构
            entryList.add(entry);
            //需要把 所有的 下一个全部加到 list里面去
            foundEntryByNext(entry.next,entryList);
        }else{
            entryList.add(entry);
        }
    }

    /**
     * 取
     * @param k
     * @return
     */
    @Override
    public V get(K k) {
        int index = getIndex(k,table.length);
        if(table[index] == null){
            throw new NullPointerException();
        }
        return findValueByEqualKey(k,table[index]);
    }

    /**
     * 递归查找
     * @param k
     * @param kvEntry
     * @return
     */
    private V findValueByEqualKey(K k, Entry<K, V> kvEntry) {
        if(k == kvEntry.getKey() || k.equals(kvEntry.getKey())){
            return kvEntry.getValue();
        }else if(kvEntry.next != null){
            return findValueByEqualKey(k,kvEntry.next);
        }
        return null;
    }


    class Entry<K,V> implements SyMap.Entry<K,V>{
        K k;
        V v;
        //指向被this 挤压下去的Entry对象
        Entry<K,V> next;

        public Entry(K k, V v,Entry<K,V> next){
            this.k=k;
            this.v=v;
            this.next=next;
        }

        @Override
        public K getKey() {
            return k;
        }

        @Override
        public V getValue() {
            return v;
        }
    }

    public static int getDefaultLength() {
        return defaultLength;
    }

    public static void setDefaultLength(int defaultLength) {
        SYHashMap.defaultLength = defaultLength;
    }

    public static double getDefaultAddSizeFactor() {
        return defaultAddSizeFactor;
    }

    public static void setDefaultAddSizeFactor(double defaultAddSizeFactor) {
        SYHashMap.defaultAddSizeFactor = defaultAddSizeFactor;
    }

    public int getUseSize() {
        return useSize;
    }

    public void setUseSize(int useSize) {
        this.useSize = useSize;
    }

    public Entry<K, V>[] getTable() {
        return table;
    }

    public void setTable(Entry<K, V>[] table) {
        this.table = table;
    }
}
