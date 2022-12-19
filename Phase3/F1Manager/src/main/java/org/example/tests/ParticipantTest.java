package org.example.tests;

import org.example.business.drivers.Driver;
import org.example.business.cars.*;
import org.example.business.users.Player;
import org.example.data.DriverDAO;
import org.example.data.ParticipantDAO;
import org.example.data.PlayerDAO;
import org.example.data.RaceCarDAO;
import org.example.business.participants.Participant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class ParticipantTest {
    private static final ParticipantDAO pdb = ParticipantDAO.getInstance();

    public static Set<String> createParticipant(int n){
        Set<String> participants = new HashSet<>();
        List<String> drivers = new ArrayList<>(DriverTest.createDriver(n));
        List<Integer> cars = new ArrayList<>(RaceCarTest.createRaceCar(n));
        List<String> players = new ArrayList<>(PlayerTest.createPlayer(n));
        for (int i=0;i<n;i++){
            Participant u = new Participant(0,
                    RaceCarDAO.getInstance().get(cars.get(i)),
                    DriverDAO.getInstance().get(drivers.get(i)),
                    PlayerDAO.getInstance().get(players.get(i))
            );
            pdb.put(u);
            participants.add(u.getManager().getUsername());
        }
        return participants;
    }
    @BeforeEach
    public void init() {
        pdb.clear();
    }
    @Test
    public void createParticipantTest() {
        String username = "player1";
        String password = "123456";
        Player player = new Player(username);
        player.setPassword(password);
        PlayerDAO.getInstance().put(player);
        String name = "driver";
        Driver driver = new Driver(name,0.1F,0.1F);
        DriverDAO.getInstance().put(driver);
        CombustionRaceCar rc=new CombustionRaceCar(new S1Class(),new Tyre(Tyre.TyreType.HARD),
                new CombustionEngine(Engine.EngineMode.HIGH,6000),
                new BodyWork(BodyWork.DownforcePackage.LOW)
        );
        RaceCarDAO.getInstance().put(rc);
        Participant u1 = new Participant(0,rc,driver,player);
        pdb.put(u1);

        Assertions.assertEquals(u1,pdb.get(username));
        Assertions.assertNull(pdb.get("player2"));
    }
    @Test
    public void isEmptyTest(){
        Assertions.assertTrue(pdb.isEmpty());
        createParticipant(20);
        Assertions.assertFalse(pdb.isEmpty());
    }
    @Test
    public void sizeTest(){
        Assertions.assertEquals(pdb.size(),0);
        createParticipant(10);
        Assertions.assertEquals(pdb.size(),10);
    }
    @Test
    public void containsKeyTest(){
        createParticipant(5);
        Assertions.assertFalse(pdb.containsKey("player:6"));
        Assertions.assertTrue(pdb.containsKey("player:5"));
    }
    @Test
    public void containsValueTest(){
        String username = "test";
        String password = "123456";
        Player player = new Player(username);
        player.setPassword(password);
        PlayerDAO.getInstance().put(player);
        String name = "driver";
        Driver driver = new Driver(name,0.1F,0.1F);
        DriverDAO.getInstance().put(driver);
        CombustionRaceCar rc=new CombustionRaceCar(new S1Class(),new Tyre(Tyre.TyreType.HARD),
                new CombustionEngine(Engine.EngineMode.HIGH,6000),
                new BodyWork(BodyWork.DownforcePackage.LOW)
        );
        RaceCarDAO.getInstance().put(rc);
        Participant n = new Participant(0,rc,driver,player);
        Set<String> s=createParticipant(5);
        Participant v = pdb.get(s.stream().min(String::compareTo).get());

        Assertions.assertFalse(pdb.containsValue(n));
        Assertions.assertTrue(pdb.containsValue(v));
    }
    @Test
    public void removeTest(){
        Set<String> s= createParticipant(10);
        Participant v = pdb.get(s.stream().min(String::compareTo).get());
        Assertions.assertEquals(pdb.remove(v.getManager().getUsername()),v);
        Assertions.assertFalse(pdb.containsValue(v));
        Assertions.assertEquals(pdb.size(),9);
    }
    @Test
    public void putAllTest(){
        String username = "player1";
        String password = "123456";
        Player player = new Player(username);
        player.setPassword(password);
        PlayerDAO.getInstance().put(player);
        String name = "driver1";
        Driver driver = new Driver(name,0.1F,0.1F);
        DriverDAO.getInstance().put(driver);
        CombustionRaceCar rc=new CombustionRaceCar(new S1Class(),new Tyre(Tyre.TyreType.HARD),
                new CombustionEngine(Engine.EngineMode.HIGH,6000),
                new BodyWork(BodyWork.DownforcePackage.LOW)
        );
        RaceCarDAO.getInstance().put(rc);
        Participant u1 = new Participant(0,rc,driver,player);
        username = "player2";
        password = "123456";
        player = new Player(username);
        player.setPassword(password);
        PlayerDAO.getInstance().put(player);
        name = "driver2";
        driver = new Driver(name,0.1F,0.1F);
        DriverDAO.getInstance().put(driver);
        rc=new CombustionRaceCar(new S1Class(),new Tyre(Tyre.TyreType.HARD),
                new CombustionEngine(Engine.EngineMode.HIGH,6000),
                new BodyWork(BodyWork.DownforcePackage.LOW)
        );
        RaceCarDAO.getInstance().put(rc);
        Participant u2 = new Participant(0,rc,driver,player);


        Map<String,Participant> umap = new HashMap<>();
        umap.put(u1.getManager().getUsername(),u1);
        umap.put(u2.getManager().getUsername(),u2);

        pdb.putAll(umap);
        Assertions.assertTrue(pdb.containsValue(u1));
        Assertions.assertTrue(pdb.containsValue(u2));
        Assertions.assertEquals(pdb.size(),2);
    }

    @Test
    public void increaseNumberOfSetupChangesTest(){
        String username = "player1";
        String password = "123456";
        Player player = new Player(username);
        player.setPassword(password);
        PlayerDAO.getInstance().put(player);
        String name = "driver1";
        Driver driver = new Driver(name,0.1F,0.1F);
        DriverDAO.getInstance().put(driver);
        CombustionRaceCar rc=new CombustionRaceCar(new S1Class(),new Tyre(Tyre.TyreType.HARD),
                new CombustionEngine(Engine.EngineMode.HIGH,6000),
                new BodyWork(BodyWork.DownforcePackage.LOW)
        );
        RaceCarDAO.getInstance().put(rc);
        Participant u1 = new Participant(0,rc,driver,player);
        pdb.put(u1);
        Assertions.assertEquals(0,u1.getNumberOfSetupChanges());
        u1.increaseNumberOfSetupChanges();
        Assertions.assertEquals(1,u1.getNumberOfSetupChanges());
        Assertions.assertEquals(1,pdb.get(u1.getManager().getUsername()).getNumberOfSetupChanges());
    }

}
