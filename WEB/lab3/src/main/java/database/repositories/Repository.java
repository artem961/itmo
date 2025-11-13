package database.repositories;

import java.util.List;

public interface Repository <T>{
    public void save(T entity);
    public void delete(T entity);
    public List<T> getAll();

}
