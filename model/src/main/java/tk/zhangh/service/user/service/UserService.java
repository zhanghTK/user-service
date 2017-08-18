package tk.zhangh.service.user.service;

import tk.zhangh.service.user.domain.model.user.User;
import tk.zhangh.service.user.domain.model.user.exceptions.*;

import java.util.Optional;

/**
 * Created by ZhangHao on 2017/8/18.
 */
public interface UserService {

    User changeEmail(Long userId, String newEmail)
            throws EmailIsAlreadyTakenException, InvalidEmailException, NoSuchUserException;

    User changePassword(Long userId, String rawPassword) throws NoSuchUserException;

    User changeScreenName(Long userId, String newScreenName)
            throws NoSuchUserException, ScreenNameIsAlreadyTakenException;

    User confirmEmail(Long userId, String token)
            throws InvalidConfirmationTokenException, NoSuchUserException;

    User confirmPasswordReset(Long userId, String token)
            throws InvalidConfirmationTokenException, NoSuchUserException;

    void delete(Long userId) throws NoSuchUserException;

    Optional<User> findUser(Long userId);

    Optional<User> findUser(String emailOrScreenName);

    User getUser(Long userId) throws NoSuchUserException;

    User getUser(String emailOrScreenName) throws NoSuchUserException;

    boolean isEmailTaken(String email);

    boolean isScreenNameTaken(String screenName);

    User login(String emailOrScreenName, String rawPassword)
            throws NoSuchUserException, UnconfirmedUserException;

    String nextScreenName(String email) throws InvalidEmailException;

    void requestEmailChange(Long userId, String newEmail)
            throws InvalidEmailException, EmailIsAlreadyTakenException, NoSuchUserException;

    void requestPasswordReset(Long userId) throws NoSuchUserException;

    void signUp(User user, String rawPassword)
            throws InvalidEmailException, EmailIsAlreadyTakenException, ScreenNameIsAlreadyTakenException;

    User store(User user)
            throws InvalidEmailException, EmailIsAlreadyTakenException, ScreenNameIsAlreadyTakenException;
}
