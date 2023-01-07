package org.example.data;

import org.example.business.circuit.Circuit;
import org.example.business.circuit.CircuitSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CircuitDAO implements Map<String , Circuit> {

    private static CircuitDAO singleton = null;

    public static CircuitDAO getInstance() {

        if (CircuitDAO.singleton == null) CircuitDAO.singleton = new CircuitDAO();
        return CircuitDAO.singleton;

    }

    private Map<String,Circuit> circuits=Map.of(
            "Monza",new Circuit("Monza",5.793F,4, new ArrayList<CircuitSection>(Arrays.asList(
                    new CircuitSection(CircuitSection.CircuitSectionType.STRAIGHT, 0.75F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CHICANE, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.2F),
                    new CircuitSection(CircuitSection.CircuitSectionType.STRAIGHT, 0.5F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CHICANE, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.STRAIGHT, 0.05F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.STRAIGHT, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.STRAIGHT, 0.6F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.STRAIGHT, 0.8F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.15F)
            ))),
            "SPA",new Circuit("SPA",7.004F,44,new ArrayList<CircuitSection>(Arrays.asList(
                    new CircuitSection(CircuitSection.CircuitSectionType.STRAIGHT, 0.5F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.2F),
                    new CircuitSection(CircuitSection.CircuitSectionType.STRAIGHT, 0.2F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.5F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.05F),
                    new CircuitSection(CircuitSection.CircuitSectionType.STRAIGHT, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.6F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.STRAIGHT, 0.8F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.STRAIGHT, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.STRAIGHT, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.STRAIGHT, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.STRAIGHT, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.STRAIGHT, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.STRAIGHT, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.STRAIGHT, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CURVE, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.STRAIGHT, 0.15F),
                    new CircuitSection(CircuitSection.CircuitSectionType.CHICANE,0.2F)
            )))
    );

    @Override
    public int size() {
        return circuits.size();
    }

    @Override
    public boolean isEmpty() {
        return circuits.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return circuits.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return circuits.containsValue(value);
    }

    @Override
    public Circuit get(Object key) {
        return circuits.get(key);
    }

    @Nullable
    @Override
    public Circuit put(String key, Circuit value) {
        return circuits.put(key, value);
    }

    @Override
    public Circuit remove(Object key) {
        return circuits.remove(key);
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ? extends Circuit> m) {
        circuits.putAll(m);
    }

    @Override
    public void clear() {
        circuits.clear();
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        return circuits.keySet();
    }

    @NotNull
    @Override
    public Collection<Circuit> values() {
        return circuits.values();
    }

    @NotNull
    @Override
    public Set<Entry<String, Circuit>> entrySet() {
        return circuits.entrySet();
    }
}
