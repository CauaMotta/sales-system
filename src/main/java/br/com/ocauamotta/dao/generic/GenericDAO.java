package br.com.ocauamotta.dao.generic;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import annotation.KeyType;
import br.com.ocauamotta.dao.Persistent;
import br.com.ocauamotta.exceptions.KeyTypeNotFoundExcepction;

public abstract class GenericDAO<T extends Persistent, E extends Serializable> implements IGenericDAO<T,E> {

    private SingletonMap singletonMap;

    public abstract Class<T> getClassType();

    public abstract void dataUpdate(T entity, T entityRegistered);

    public GenericDAO() {
        this.singletonMap = SingletonMap.getInstance();
    }

    public E getKey(T entity) throws KeyTypeNotFoundExcepction {
        Field[] fields = entity.getClass().getDeclaredFields();
        E returnValue = null;
        for (Field field : fields) {
            if (field.isAnnotationPresent(KeyType.class)) {
                KeyType keyType = field.getAnnotation(KeyType.class);
                String methodName = keyType.value();
                try {
                    Method method = entity.getClass().getMethod(methodName);
                    returnValue = (E) method.invoke(entity);
                    return returnValue;
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    throw new KeyTypeNotFoundExcepction("Chave principal do objeto " + entity.getClass() + " não encontrada.", e);
                }
            }
        }
        if (returnValue == null) {
            String err = "Chave principal do objeto " + entity.getClass() + " não encontrada.";
            System.out.println("ERROR: " + err);
            throw new KeyTypeNotFoundExcepction(err);
        }
        return null;
    }

    @Override
    public Boolean register(T entity) throws KeyTypeNotFoundExcepction {
        Map<E, T> map = getMap();
        E key = getKey(entity);
        if (map.containsKey(key)) {
            return false;
        }

        map.put(key, entity);
        return true;
    }

    private Map<E, T> getMap() {
        Map<E, T> map = (Map<E, T>) this.singletonMap.getMap().get(getClassType());
        if (map == null) {
            map = new HashMap<>();
            this.singletonMap.getMap().put(getClassType(), map);
        }
        return map;
    }

    @Override
    public void delet(E value) {
        Map<E, T> map = getMap();
        T objectRegistered = map.get(value);
        if (objectRegistered != null) {
            map.remove(value, objectRegistered);
        }
    }

    @Override
    public void change(T entity) throws KeyTypeNotFoundExcepction {
        Map<E, T> map = getMap();
        E key = getKey(entity);
        T objectRegistered = map.get(key);
        if (objectRegistered != null) {
            dataUpdate(entity, objectRegistered);
        }
    }

    @Override
    public T search(E valor) {
        Map<E, T> map = getMap();
        return map.get(valor);
    }

    @Override
    public Collection<T> searchAll() {
        Map<E, T> map = getMap();
        return map.values();
    }

    public abstract void delet();
}