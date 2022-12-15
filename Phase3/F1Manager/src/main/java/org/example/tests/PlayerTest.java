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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlayerTest {
    private static final PlayerDAO udb = PlayerDAO.getInstance();

    public static Set<String> createPlayer(int n){
        Set<String> s = new HashSet<>();
        for (int i=1;i<=n;i++){
            Player u = new Player("player:"+i);
            u.setPassword("123456");
            udb.put(u);
            s.add(u.getUsername());
        }
        return s;
    }
    @BeforeEach
    public void init() {
        udb.clear();
    }
    @Test
    public void createPlayerTest() {
        String username = "player1";
        String password = "123456";
        Player u1 = new Player(username);
        u1.setPassword(password);
        udb.put(u1);
        Assertions.assertEquals(u1,udb.get(u1.getUsername()));
        Assertions.assertEquals(u1.getHashedPassword(),udb.get(u1.getUsername()).getHashedPassword());
        Assertions.assertNull(udb.get("player2"));
    }
    @Test
    public void isEmptyTest(){
        Assertions.assertTrue(udb.isEmpty());
        createPlayer(10);
        Assertions.assertFalse(udb.isEmpty());
    }
    @Test
    public void sizeTest(){
        Assertions.assertEquals(udb.size(),0);
        createPlayer(10);
        Assertions.assertEquals(udb.size(),10);
        createPlayer(20);
        Assertions.assertEquals(udb.size(),20);
    }
    @Test
    public void containsKeyTest(){
        createPlayer(5);
        Assertions.assertFalse(udb.containsKey("player:6"));
        Assertions.assertTrue(udb.containsKey("player:5"));
    }
    @Test
    public void containsValueTest(){
        Player n = new Player("test");
        createPlayer(5);
        Player v = udb.get("player:1");

        Assertions.assertFalse(udb.containsValue(n));
        Assertions.assertTrue(udb.containsValue(v));
    }
    @Test
    public void removeTest(){
        createPlayer(10);
        Player v = udb.get("player:1");
        Assertions.assertEquals(udb.remove(v.getUsername()),v);
        Assertions.assertFalse(udb.containsValue(v));
        Assertions.assertEquals(udb.size(),9);
    }
    @Test
    public void putAllTest(){
        Player u1 = new Player("player1");
        Player u2 = new Player("player2");
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
        Player u1 = new Player("player1");
        u1.setPassword("test");
        Player r=Player.register("player1","test");
        Assertions.assertEquals(u1,r);
        Assertions.assertThrows(UsernameAlreadyExistsException.class,
                ()->{Player.register("player1","test");});
    }
    @Test
    public void login() throws UsernameAlreadyExistsException, UsernameDoesNotExistException, WrongPasswordException {
        Player r=Player.register("player1","test");
        Player l=Player.login("player1","test");
        Assertions.assertEquals(r,l);
        Assertions.assertThrows(UsernameDoesNotExistException.class,
                ()->{Player.login("player2","test");});
        Assertions.assertThrows(WrongPasswordException.class,
                ()->{Player.login("player1","wrongPassword");});
    }

}
