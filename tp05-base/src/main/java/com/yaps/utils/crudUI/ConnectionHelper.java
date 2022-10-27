package com.yaps.utils.crudUI;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import com.yaps.petstore.config.PetstoreConnectionFactory;
import com.yaps.utils.textUi.SimpleIO;

public class ConnectionHelper {
    public static void  doWithConnection(SimpleIO io, Consumer<Connection> consumer) {
        try (Connection connection = PetstoreConnectionFactory.getInstance().createConnection()) {
            consumer.accept(connection);
        } catch (SQLException e) {
            io.println("error while closing");            
            throw new RuntimeException(e);
        }
    }

    public static<T> T computeWithConnection(SimpleIO io, Function<Connection,T> function) {
        try (Connection connection = PetstoreConnectionFactory.getInstance().createConnection()) {
            return function.apply(connection);
        } catch (SQLException e) {
            io.println("error while closing");
            throw new RuntimeException(e);
        }
    }
}
