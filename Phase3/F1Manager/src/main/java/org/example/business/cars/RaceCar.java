package org.example.business.cars;

import org.example.data.RaceCarDAO;
import org.example.exceptions.cars.CarCannotHaveRepeatedCarPartsTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class RaceCar {

    private Integer id;
    private Class<? extends CarClass> category;
    private Map<CarPart.CarPartType,CarPart> parts;

    public void setId(Integer id){this.id=id;}

    public Class<?>  getCategory() {return category;}

    public void setCategory(Class<? extends CarClass> category) {this.category = category;}

    public Integer getId() {return id;}

    public Map<CarPart.CarPartType, CarPart> getParts() {
        Map<CarPart.CarPartType,CarPart> partsC= new HashMap<CarPart.CarPartType,CarPart>();
        for (Map.Entry<CarPart.CarPartType,CarPart> e:parts.entrySet()){
            partsC.put(e.getKey(),e.getValue().clone());
        }
        return partsC;
    }

    public void setParts(Map<CarPart.CarPartType, CarPart> parts) {
        this.parts = new HashMap<CarPart.CarPartType,CarPart>();
        for (Map.Entry<CarPart.CarPartType,CarPart> e:parts.entrySet()){
            this.parts.put(e.getKey(),e.getValue().clone());
        }
    }
    public void setParts(Set<CarPart> parts) throws CarCannotHaveRepeatedCarPartsTypes {
        Map<CarPart.CarPartType,CarPart> t = new HashMap<CarPart.CarPartType,CarPart>();
        for (CarPart p:parts){
            if(t.containsKey(p.getType())){
                throw new CarCannotHaveRepeatedCarPartsTypes();
            }
            t.put(p.getType(),p.clone());
        }
        this.parts=t;
    }

    public RaceCar(Class<? extends CarClass> category, Map<CarPart.CarPartType, CarPart> parts){
        this.category=category;
        this.id = null;
        this.setParts(parts);
    }
    public RaceCar(Class<? extends CarClass> category, Set<CarPart> parts) throws CarCannotHaveRepeatedCarPartsTypes {
        this.category=category;
        this.id = null;
        this.setParts(parts);
    }
    public RaceCar(int id,Class<? extends CarClass> category, Map<CarPart.CarPartType, CarPart> parts){
        this.category=category;
        this.id = null;
        this.setParts(parts);
    }
    public RaceCar(int id,Class<? extends CarClass> category, Set<CarPart> parts) throws CarCannotHaveRepeatedCarPartsTypes {
        this.category=category;
        this.id = id;
        this.setParts(parts);
    }
    public RaceCar(int id,Class<? extends CarClass> category,Tyre t,BodyWork b,Engine e,Engine ee){
        this.category=category;
        this.id = id;
        this.parts=new HashMap<>();
        this.parts.put(CarPart.CarPartType.TYRE,t);
        this.parts.put(CarPart.CarPartType.BODYWORK,b);
        this.parts.put(CarPart.CarPartType.COMBUSTION_ENGINE,e);
        if (ee!=null){
            this.parts.put(CarPart.CarPartType.ELECTRIC_ENGINE,ee);
        }
    }
    public RaceCar(Class<? extends CarClass> category,Tyre t,BodyWork b,Engine e,Engine ee){
        this.category=category;
        this.id = null;
        this.parts=new HashMap<>();
        this.parts.put(CarPart.CarPartType.TYRE,t);
        this.parts.put(CarPart.CarPartType.BODYWORK,b);
        this.parts.put(CarPart.CarPartType.COMBUSTION_ENGINE,e);
        if (ee!=null){
            this.parts.put(CarPart.CarPartType.ELECTRIC_ENGINE,ee);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RaceCar raceCar = (RaceCar) o;
        return getCategory()==raceCar.getCategory() && getParts().equals(raceCar.getParts());
    }

    @Override
    public String toString() {
        return "RaceCar{" +
                "id=" + id +
                ", category=" + category +
                ", parts=" + parts +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCategory(), getParts());
    }

    public void changeCarSetup(){

    }

    public void setStrategy(Tyre.TyreType tyre,Engine.EngineMode engineMode){
        ((Tyre)this.parts.get(CarPart.CarPartType.TYRE)).setType(tyre);
        ((Engine)this.parts.get(CarPart.CarPartType.COMBUSTION_ENGINE)).setMode(engineMode);
        if (this.parts.containsKey(CarPart.CarPartType.ELECTRIC_ENGINE)){
            ((Engine)this.parts.get(CarPart.CarPartType.ELECTRIC_ENGINE)).setMode(engineMode);
        }
        RaceCarDAO rdb = RaceCarDAO.getInstance();
        rdb.update(this.getId(),this);
    }


}
