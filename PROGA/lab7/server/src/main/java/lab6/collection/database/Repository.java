package lab6.collection.database;

import java.util.HashSet;

public interface Repository<T> {
    int insert(T entity);
    int updateById(T entity, Integer id);
    int removeById(Integer id);
    T selectById(Integer id);
    HashSet<T> selectAll();
    int removeAll();
}
