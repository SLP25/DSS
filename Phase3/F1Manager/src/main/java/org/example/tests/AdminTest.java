package org.example.tests;

import org.example.business.users.Admin;
import org.example.business.users.Admin;
import org.example.data.AdminDAO;
import org.example.data.PlayerDAO;
import org.example.exceptions.authentication.UsernameAlreadyExistsException;
import org.example.exceptions.authentication.UsernameDoesNotExistException;
import org.example.exceptions.authentication.WrongPasswordException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AdminTest {
    private static final AdminDAO adb = AdminDAO.getInstance();

    public static Set<String> createAdmin(int n){
        Set<String>s=new HashSet<>();
        for (int i=1;i<=n;i++){
            Admin u = new Admin("admin:"+i,false);
            u.setPassword("123456");
            adb.put(u);
            s.add(u.getUsername());
        }
        return s;
    }
    @BeforeEach
    public void init() {
        adb.clear();
    }
    @Test
    public void createAdminTest() {
        String username = "admin1";
        String password = "123456";
        Admin u1 = new Admin(username,false);
        u1.setPassword(password);
        adb.put(u1);
        Assertions.assertEquals(u1,adb.get(u1.getUsername()));
        Assertions.assertEquals(u1.getHashedPassword(),adb.get(u1.getUsername()).getHashedPassword());
        Assertions.assertNull(adb.get("admin2"));
    }
    @Test
    public void isEmptyTest(){
        Assertions.assertTrue(adb.isEmpty());
        createAdmin(10);
        Assertions.assertFalse(adb.isEmpty());
    }
    @Test
    public void sizeTest(){
        Assertions.assertEquals(adb.size(),0);
        createAdmin(10);
        Assertions.assertEquals(adb.size(),10);
        createAdmin(20);
        Assertions.assertEquals(adb.size(),20);
    }
    @Test
    public void containsKeyTest(){
        createAdmin(5);
        Assertions.assertFalse(adb.containsKey("admin:6"));
        Assertions.assertTrue(adb.containsKey("admin:5"));
    }
    @Test
    public void containsValueTest(){
        Admin n = new Admin("test",false);
        createAdmin(5);
        Admin v = adb.get("admin:1");

        Assertions.assertFalse(adb.containsValue(n));
        Assertions.assertTrue(adb.containsValue(v));
    }
    @Test
    public void removeTest(){
        createAdmin(10);
        Admin v = adb.get("admin:1");
        Assertions.assertEquals(adb.remove(v.getUsername()),v);
        Assertions.assertFalse(adb.containsValue(v));
        Assertions.assertEquals(adb.size(),9);
    }
    @Test
    public void putAllTest(){
        Admin u1 = new Admin("admin1",false);
        Admin u2 = new Admin("admin2",false);
        Admin u3 = new Admin("admin3",false);
        Map<String,Admin> umap = new HashMap<>();
        umap.put(u1.getUsername(),u1);
        umap.put(u2.getUsername(),u2);
        umap.put(u3.getUsername(),u3);

        adb.putAll(umap);
        Assertions.assertTrue(adb.containsValue(u1));
        Assertions.assertTrue(adb.containsValue(u2));
        Assertions.assertTrue(adb.containsValue(u3));
        Assertions.assertEquals(adb.size(),3);
    }

    @Test
    public void register() throws UsernameAlreadyExistsException {
        Admin u1 = new Admin("admin1",false);
        u1.setPassword("test");
        Admin r=Admin.register("admin1","test",false);
        Assertions.assertEquals(u1,r);
        Assertions.assertThrows(UsernameAlreadyExistsException.class,
                ()->{Admin.register("admin1","test",false);});
    }
    @Test
    public void login() throws UsernameAlreadyExistsException, UsernameDoesNotExistException, WrongPasswordException {
        Admin r=Admin.register("admin1","test",false);
        Admin l=Admin.login("admin1","test");
        Assertions.assertEquals(r,l);
        Assertions.assertThrows(UsernameDoesNotExistException.class,
                ()->{Admin.login("admin2","test");});
        Assertions.assertThrows(WrongPasswordException.class,
                ()->{Admin.login("admin1","wrongPassword");});
    }
}
