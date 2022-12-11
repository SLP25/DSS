package org.example.tests;

import org.example.buisness.User;
import org.example.data.UserDAO;

import java.util.HashMap;
import java.util.Map;

public class UserTest {
    public UserTest(){
        User u1 = new User("user1");
        u1.setPassword("123456");
        User u2 = new User("user2");
        u2.setPassword("123456");
        User u3 = new User("user3");
        u3.setPassword("123456");
        User u4 = new User("user4");
        u4.setPassword("123456");

        Map<String,User> umap = new HashMap<>();

        umap.put(u1.getUsername(),u1);
        umap.put(u2.getUsername(),u2);
        umap.put(u3.getUsername(),u3);


        UserDAO ud = UserDAO.getInstance();
        ud.clear();
        System.out.println(ud.isEmpty());
        ud.put(u1.getUsername(),u1);
        System.out.println(ud.get(u1.getUsername()));
        System.out.println(ud.size());
        System.out.println(ud.isEmpty());
        System.out.println(ud.containsKey(u1.getUsername()));
        System.out.println(ud.remove(u1.getUsername()));
        System.out.println(ud.isEmpty());
        ud.putAll(umap);
        System.out.println(ud.size());
        System.out.println(ud.keySet());
        System.out.println(ud.values());
        System.out.println(ud.entrySet());
        System.out.println(ud.containsValue(u4));
        System.out.println(ud.containsValue(u3));
    }
}
