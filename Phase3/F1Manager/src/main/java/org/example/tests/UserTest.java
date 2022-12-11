package org.example.tests;

import org.example.business.User;
import org.example.data.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.Map;

public class UserTest {
    private final UserDAO udb = UserDAO.getInstance();

    private void createUsers(int n){
        for (int i=1;i<=n;i++){
            User u = new User("user:"+i);
            u.setPassword("123456");
            udb.put(u);
        }
    }
    @BeforeEach
    public void init() {
        udb.clear();
    }
    @Test
    public void createUserTest() {
        String username = "user1";
        String password = "123456";
        User u1 = new User(username);
        u1.setPassword(password);
        udb.put(u1);
        Assertions.assertEquals(u1,udb.get(u1.getUsername()));
        Assertions.assertEquals(u1.getHashedPassword(),udb.get(u1.getUsername()).getHashedPassword());
        Assertions.assertNull(udb.get("user2"));
    }
    @Test
    public void isEmptyTest(){
        Assertions.assertTrue(udb.isEmpty());
        createUsers(10);
        Assertions.assertFalse(udb.isEmpty());
    }
    @Test
    public void sizeTest(){
        Assertions.assertEquals(udb.size(),0);
        createUsers(10);
        Assertions.assertEquals(udb.size(),10);
        createUsers(20);
        Assertions.assertEquals(udb.size(),20);
    }
    @Test
    public void containsKeyTest(){
        createUsers(5);
        Assertions.assertFalse(udb.containsKey("user:6"));
        Assertions.assertTrue(udb.containsKey("user:5"));
    }
    @Test
    public void containsValueTest(){
        User n = new User("test");
        createUsers(5);
        User v = udb.get("user:1");

        Assertions.assertFalse(udb.containsValue(n));
        Assertions.assertTrue(udb.containsValue(v));
    }
    @Test
    public void removeTest(){
        createUsers(10);
        User v = udb.get("user:1");
        Assertions.assertEquals(udb.remove(v.getUsername()),v);
        Assertions.assertFalse(udb.containsValue(v));
        Assertions.assertEquals(udb.size(),9);
    }
    @Test
    public void putAllTest(){
        User u1 = new User("user1");
        User u2 = new User("user2");
        User u3 = new User("user3");
        Map<String,User> umap = new HashMap<>();
        umap.put(u1.getUsername(),u1);
        umap.put(u2.getUsername(),u2);
        umap.put(u3.getUsername(),u3);

        udb.putAll(umap);
        Assertions.assertTrue(udb.containsValue(u1));
        Assertions.assertTrue(udb.containsValue(u2));
        Assertions.assertTrue(udb.containsValue(u3));
        Assertions.assertEquals(udb.size(),3);
    }
}
