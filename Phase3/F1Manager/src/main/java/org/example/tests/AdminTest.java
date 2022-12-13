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
import java.util.Map;

public class AdminTest {
    private final AdminDAO adb = AdminDAO.getInstance();

    private void createUsers(int n){
        for (int i=1;i<=n;i++){
            Admin u = new Admin("user:"+i,false);
            u.setPassword("123456");
            adb.put(u);
        }
    }
    @BeforeEach
    public void init() {
        adb.clear();
    }
    @Test
    public void createAdminTest() {
        String username = "user1";
        String password = "123456";
        Admin u1 = new Admin(username,false);
        u1.setPassword(password);
        adb.put(u1);
        Assertions.assertEquals(u1,adb.get(u1.getUsername()));
        Assertions.assertEquals(u1.getHashedPassword(),adb.get(u1.getUsername()).getHashedPassword());
        Assertions.assertNull(adb.get("user2"));
    }
    @Test
    public void isEmptyTest(){
        Assertions.assertTrue(adb.isEmpty());
        createUsers(10);
        Assertions.assertFalse(adb.isEmpty());
    }
    @Test
    public void sizeTest(){
        Assertions.assertEquals(adb.size(),0);
        createUsers(10);
        Assertions.assertEquals(adb.size(),10);
        createUsers(20);
        Assertions.assertEquals(adb.size(),20);
    }
    @Test
    public void containsKeyTest(){
        createUsers(5);
        Assertions.assertFalse(adb.containsKey("user:6"));
        Assertions.assertTrue(adb.containsKey("user:5"));
    }
    @Test
    public void containsValueTest(){
        Admin n = new Admin("test",false);
        createUsers(5);
        Admin v = adb.get("user:1");

        Assertions.assertFalse(adb.containsValue(n));
        Assertions.assertTrue(adb.containsValue(v));
    }
    @Test
    public void removeTest(){
        createUsers(10);
        Admin v = adb.get("user:1");
        Assertions.assertEquals(adb.remove(v.getUsername()),v);
        Assertions.assertFalse(adb.containsValue(v));
        Assertions.assertEquals(adb.size(),9);
    }
    @Test
    public void putAllTest(){
        Admin u1 = new Admin("user1",false);
        Admin u2 = new Admin("user2",false);
        Admin u3 = new Admin("user3",false);
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
        Admin u1 = new Admin("user1",false);
        u1.setPassword("test");
        Admin r=Admin.register("user1","test",false);
        Assertions.assertEquals(u1,r);
        Assertions.assertThrows(UsernameAlreadyExistsException.class,
                ()->{Admin.register("user1","test",false);});
    }
    @Test
    public void login() throws UsernameAlreadyExistsException, UsernameDoesNotExistException, WrongPasswordException {
        Admin r=Admin.register("user1","test",false);
        Admin l=Admin.login("user1","test");
        Assertions.assertEquals(r,l);
        Assertions.assertThrows(UsernameDoesNotExistException.class,
                ()->{Admin.login("user2","test");});
        Assertions.assertThrows(WrongPasswordException.class,
                ()->{Admin.login("user1","wrongPassword");});
    }
}
