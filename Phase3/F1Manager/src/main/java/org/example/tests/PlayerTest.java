package org.example.tests;

import org.example.business.users.Player;
import org.example.data.PlayerDAO;
import org.example.exceptions.authentication.UsernameAlreadyExistsException;
import org.example.exceptions.authentication.UsernameDoesNotExistException;
import org.example.exceptions.authentication.WrongPasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.Map;

public class PlayerTest {
    private final PlayerDAO udb = PlayerDAO.getInstance();

    private void createUsers(int n){
        for (int i=1;i<=n;i++){
            Player u = new Player("user:"+i);
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
        Player u1 = new Player(username);
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
        Player n = new Player("test");
        createUsers(5);
        Player v = udb.get("user:1");

        Assertions.assertFalse(udb.containsValue(n));
        Assertions.assertTrue(udb.containsValue(v));
    }
    @Test
    public void removeTest(){
        createUsers(10);
        Player v = udb.get("user:1");
        Assertions.assertEquals(udb.remove(v.getUsername()),v);
        Assertions.assertFalse(udb.containsValue(v));
        Assertions.assertEquals(udb.size(),9);
    }
    @Test
    public void putAllTest(){
        Player u1 = new Player("user1");
        Player u2 = new Player("user2");
        Player u3 = new Player("user3");
        Map<String,Player> umap = new HashMap<>();
        umap.put(u1.getUsername(),u1);
        umap.put(u2.getUsername(),u2);
        umap.put(u3.getUsername(),u3);

        udb.putAll(umap);
        Assertions.assertTrue(udb.containsValue(u1));
        Assertions.assertTrue(udb.containsValue(u2));
        Assertions.assertTrue(udb.containsValue(u3));
        Assertions.assertEquals(udb.size(),3);
    }

    @Test
    public void register() throws UsernameAlreadyExistsException {
        Player u1 = new Player("user1");
        u1.setPassword("test");
        Player r=Player.register("user1","test");
        Assertions.assertEquals(u1,r);
        Assertions.assertThrows(UsernameAlreadyExistsException.class,
                ()->{Player.register("user1","test");});
    }
    @Test
    public void login() throws UsernameAlreadyExistsException, UsernameDoesNotExistException, WrongPasswordException {
        Player r=Player.register("user1","test");
        Player l=Player.login("user1","test");
        Assertions.assertEquals(r,l);
        Assertions.assertThrows(UsernameDoesNotExistException.class,
                ()->{Player.login("user2","test");});
        Assertions.assertThrows(WrongPasswordException.class,
                ()->{Player.login("user1","wrongPassword");});
    }

}
