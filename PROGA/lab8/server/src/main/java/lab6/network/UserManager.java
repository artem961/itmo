package lab6.network;

import common.network.Response;
import common.network.User;
import common.network.enums.ResponseType;
import lab6.collection.database.DBException;
import lab6.collection.database.UserRepository;
import lab6.collection.database.connection.DBManager;
import lab6.network.server.RequestHandlerModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UserManager {
    private final UserRepository userRepository;
    private final Set<Integer> authUsersId;

    public UserManager(DBManager dbManager) {
        this.userRepository = new UserRepository(dbManager);
        this.authUsersId = new HashSet<>();
    }

    public boolean checkUser(User user) {
        return authUsersId.stream().anyMatch(id -> id.equals(user.id()));
    }

    public Response authUser(User user) {
        User newUser = userRepository.selectByNameAndPass(user.name(), user.hashedPassword());
        if (newUser == null) {
            return Response.builder()
                    .setType(ResponseType.EXCEPTION)
                    .setMessage("Не удалось войти!")
                    .build();
        }

        Collection<User> collection = new ArrayList<>(); //костыль чтоб id как то передать можно было обратно
        collection.add(newUser);
        authUsersId.add(newUser.id());
        return Response.builder().setType(ResponseType.AUTH)
                .setMessage("Аутентификация прошла успешно!")
                .setCollection(collection)
                .build();
    }

    public Response regUser(User user) {
        try {
            int id = userRepository.insert(user);
            User newUser = new User(id, user.name(), user.hashedPassword());

            Collection<User> collection = new ArrayList<>(); //костыль чтоб id как то передать можно было обратно
            collection.add(newUser);
            authUsersId.add(newUser.id());
            return Response.builder().setType(ResponseType.AUTH)
                    .setMessage("Зарегистрирован новый пользователь " + user.name() + "!")
                    .setCollection(collection)
                    .build();
        } catch (DBException e) {
            return Response.builder()
                    .setType(ResponseType.EXCEPTION)
                    .setMessage("Такой пользователь уже существует!")
                    .build();
        }
    }
}
