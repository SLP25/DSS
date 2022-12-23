package org.example.tests;

import org.example.business.drivers.Driver;
import org.example.data.DriverDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DriverTest {
    private static final DriverDAO ddb = DriverDAO.getInstance();

    public static Set<String> createDriver(int n){
        Set<String> s = new HashSet<>();
        int k=ddb.size();
        for (int i=1;i<=n;i++){
            Driver u = new Driver("driver:"+i+k,0.1F,0.1F);
            ddb.put(u);
            s.add(u.getDriverName());
        }
        return s;
    }
    @BeforeEach
    public void init() {
        ddb.clear();
    }
    @Test
    public void createPlayerTest() {
        String name = "driver";
        Driver u1 = new Driver(name,0.1F,0.1F);
        ddb.put(u1);
        Assertions.assertEquals(u1,ddb.get(u1.getDriverName()));
        Assertions.assertNull(ddb.get("driver2"));
    }
    @Test
    public void isEmptyTest(){
        Assertions.assertTrue(ddb.isEmpty());
        createDriver(10);
        Assertions.assertFalse(ddb.isEmpty());
    }
    @Test
    public void sizeTest(){
        Assertions.assertEquals(ddb.size(),0);
        createDriver(10);
        Assertions.assertEquals(ddb.size(),10);
        createDriver(20);
        Assertions.assertEquals(ddb.size(),30);
    }
    @Test
    public void containsKeyTest(){
        Set<String> ids=createDriver(5);
        Assertions.assertFalse(ddb.containsKey(ids.stream().max(String::compareTo).get()+"1234"));
        Assertions.assertTrue(ddb.containsKey(ids.stream().max(String::compareTo).get()));
    }
    @Test
    public void containsValueTest(){
        Driver n = new Driver("test",0.12F,0.12F);
        Set<String>s=createDriver(5);
        Driver v = ddb.get(s.stream().min(String::compareTo).get());

        Assertions.assertFalse(ddb.containsValue(n));
        Assertions.assertTrue(ddb.containsValue(v));
    }
    @Test
    public void removeTest(){
        Set<String>s=createDriver(10);
        Driver v = ddb.get(s.stream().min(String::compareTo).get());
        Assertions.assertEquals(ddb.remove(v.getDriverName()),v);
        Assertions.assertFalse(ddb.containsValue(v));
        Assertions.assertEquals(ddb.size(),9);
    }
    @Test
    public void putAllTest(){
        Driver u1 = new Driver("driver1",0.1F,0.1F);
        Driver u2 = new Driver("driver2",0.1F,0.1F);
        Driver u3 = new Driver("driver3",0.1F,0.1F);
        Map<String,Driver> umap = new HashMap<>();
        umap.put(u1.getDriverName(),u1);
        umap.put(u2.getDriverName(),u2);
        umap.put(u3.getDriverName(),u3);

        ddb.putAll(umap);
        Assertions.assertTrue(ddb.containsValue(u1));
        Assertions.assertTrue(ddb.containsValue(u2));
        Assertions.assertTrue(ddb.containsValue(u3));
        Assertions.assertEquals(ddb.size(),3);
    }
}
