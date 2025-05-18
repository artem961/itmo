package lab6.collection.database.connection;

import lombok.experimental.Delegate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;

public class PooledConnection implements Connection {
    @Delegate(excludes = AutoCloseable.class)
    private final Connection delegate;
    private final BlockingQueue<PooledConnection> pool;

    public PooledConnection(Connection connection, BlockingQueue<PooledConnection> pool){
        this.delegate = connection;
        this.pool = pool;
    }

    @Override
    public void close() throws SQLException {
        synchronized (pool) {
            pool.offer(this);
        }
    }
}
