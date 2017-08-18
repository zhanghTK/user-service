package tk.zhangh.service.user.service;

import lombok.extern.slf4j.Slf4j;
import tk.zhangh.service.user.commons.util.IdentityGenerator;
import tk.zhangh.service.user.commons.util.Validator;
import tk.zhangh.service.user.crypto.PasswordSecurity;
import tk.zhangh.service.user.domain.model.user.ConfirmationToken;
import tk.zhangh.service.user.domain.model.user.Password;
import tk.zhangh.service.user.domain.model.user.User;
import tk.zhangh.service.user.domain.model.user.UserRepository;
import tk.zhangh.service.user.domain.model.user.exceptions.*;
import tk.zhangh.service.user.domain.model.userevent.UserEvent;
import tk.zhangh.service.user.domain.model.userevent.UserEventEmitter;

import java.util.Objects;
import java.util.Optional;

import static tk.zhangh.service.user.domain.model.user.ConfirmationTokenType.PASSWORD_RESET;
import static tk.zhangh.service.user.domain.model.userevent.UserEventType.*;

/**
 * Created by ZhangHao on 2017/8/18.
 */
@Slf4j
public class UserServiceImpl implements UserService {

    private static final int NEXT_SCREEN_NAME_MAX_TRIES = 20;

    private final PasswordSecurity passwordSecurity;
    private final UserEventEmitter userEventEmitter;
    private final UserRepository userRepository;

    public UserServiceImpl(PasswordSecurity passwordSecurity, UserEventEmitter userEventEmitter, UserRepository userRepository) {
        Objects.requireNonNull(passwordSecurity);
        Objects.requireNonNull(userEventEmitter);
        Objects.requireNonNull(userRepository);

        this.passwordSecurity = passwordSecurity;
        this.userEventEmitter = userEventEmitter;
        this.userRepository = userRepository;
    }

    @Override
    public User changeEmail(Long userId, String newEmail) throws EmailIsAlreadyTakenException, InvalidEmailException, NoSuchUserException {
        Objects.requireNonNull(userId, "userId");
        Objects.requireNonNull(newEmail, "newEmail");

        User user = getUser(userId);
        checkEmail(user, newEmail);
        if (newEmail.equals(user.getEmail())) {
            return user;
        }

        user.setEmail(newEmail);
        user = store(user);

        userEventEmitter.emit(new UserEvent(userId, EMAIL_CHANGED));

        return user;
    }

    @Override
    public User changePassword(Long userId, String rawPassword) throws NoSuchUserException {
        Objects.requireNonNull(userId, "userId");
        Objects.requireNonNull(rawPassword, "rawPassword");

        User user = getUser(userId);
        Password newPassword = passwordSecurity.ecrypt(rawPassword);
        user.setPassword(newPassword);
        user = store(user);

        userEventEmitter.emit(new UserEvent(userId, PASSWORD_CHANGED));

        return user;
    }

    @Override
    public User changeScreenName(Long userId, String newScreenName) throws NoSuchUserException, ScreenNameIsAlreadyTakenException {
        Objects.requireNonNull(userId, "userId");
        Objects.requireNonNull(newScreenName, "newScreenName");

        User user = getUser(userId);
        checkScreenName(user, newScreenName);
        user.setScreenName(newScreenName);
        user = store(user);

        userEventEmitter.emit(new UserEvent(userId, SCREEN_NAME_CHANGED));

        return user;
    }

    @Override
    @SuppressWarnings("unchecked")
    public User confirmEmail(Long userId, String token) throws InvalidConfirmationTokenException, NoSuchUserException {
        Objects.requireNonNull(userId, "userId");
        Objects.requireNonNull(token, "token");

        User user = getUser(userId);

        ConfirmationToken<String> confirmationToken = user.useConfirmationToken(token);

        Optional<String> newEmail = confirmationToken.getPayload();
        if (!newEmail.isPresent()) {
            boolean confirmed = user.isConfirmed();
            user.setConfirmed(true);
            user = store(user);
            if (!confirmed) {
                userEventEmitter.emit(new UserEvent(userId, EMAIL_CONFIRMED));
            }
        } else {
            try {
                user = changeEmail(userId, newEmail.get());
            } catch (EmailIsAlreadyTakenException | InvalidEmailException e) {
                log.warn(e.getMessage(), e);
            }
        }

        return user;
    }

    @Override
    public User confirmPasswordReset(Long userId, String token) throws InvalidConfirmationTokenException, NoSuchUserException {
        Objects.requireNonNull(userId, "userId");
        Objects.requireNonNull(token, "token");

        User user = getUser(userId);
        user.useConfirmationToken(token);
        user = store(user);

        userEventEmitter.emit(new UserEvent(userId, PASSWORD_RESET_CONFIRMED));

        return user;
    }

    @Override
    public void delete(Long userId) throws NoSuchUserException {
        userRepository.delete(userId);
        userEventEmitter.emit(new UserEvent(userId, DELETED));
    }

    @Override
    public Optional<User> findUser(Long userId) {
        Objects.requireNonNull(userId);
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> findUser(String emailOrScreenName) {
        Objects.requireNonNull(emailOrScreenName);
        Optional<User> user = null;
        if (Validator.isEmail(emailOrScreenName)) {
            user = userRepository.findByEmail(emailOrScreenName);
        } else {
            user = userRepository.findByScreenName(emailOrScreenName);
        }
        return user;
    }

    @Override
    public User getUser(Long userId) throws NoSuchUserException {
        Optional<User> user = findUser(userId);
        return user.orElseThrow(NoSuchUserException::new);
    }

    @Override
    public User getUser(String emailOrScreenName) throws NoSuchUserException {
        Optional<User> user = findUser(emailOrScreenName);
        return user.orElseThrow(NoSuchUserException::new);
    }

    @Override
    public boolean isEmailTaken(String email) {
        Objects.requireNonNull(email);
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }

    @Override
    public boolean isScreenNameTaken(String screenName) {
        Objects.requireNonNull(screenName);
        Optional<User> user = userRepository.findByScreenName(screenName);
        return user.isPresent();
    }

    @Override
    public User login(String emailOrScreenName, String rawPassword) throws NoSuchUserException, UnconfirmedUserException {
        Objects.requireNonNull(emailOrScreenName, "emailOrScreenName");
        Objects.requireNonNull(rawPassword, "rawPassword");

        User user = getUser(emailOrScreenName);

        if (!user.isConfirmed()) {
            throw new UnconfirmedUserException();
        }

        if (passwordSecurity.check(user.getPassword(), rawPassword)) {
            // TODO
            userEventEmitter.emit(new UserEvent(user.getId(), SIGNIN_SUCCEEDED));
            return user;
        }

        userEventEmitter.emit(new UserEvent(user.getId(), SIGNIN_FAILED));
        throw new NoSuchUserException();
    }

    @Override
    public String nextScreenName(String email) throws InvalidEmailException {
        Objects.requireNonNull(email);
        if (!Validator.isEmail(email)) {
            throw new InvalidEmailException();
        }

        String screenName = email.split("@")[0];

        int index = 1;
        String possibleScreenName = screenName;
        while (isScreenNameTaken(possibleScreenName) && index < NEXT_SCREEN_NAME_MAX_TRIES) {
            possibleScreenName = screenName + (index++);
        }

        if (index < NEXT_SCREEN_NAME_MAX_TRIES) {
            return possibleScreenName;
        }

        if (!isScreenNameTaken(possibleScreenName)) {
            return possibleScreenName;
        } else {
            return screenName + IdentityGenerator.generate();
        }
    }

    @Override
    public void requestEmailChange(Long userId, String newEmail) throws InvalidEmailException, EmailIsAlreadyTakenException, NoSuchUserException {
        Objects.requireNonNull(userId, "userId");
        Objects.requireNonNull(newEmail, "newEmail");

        User user = getUser(userId);
        checkEmail(user, newEmail);
        if (newEmail.equals(user.getEmail())) {
            return;
        }

        ConfirmationToken confirmationToken = user.addConfirmationToken(PASSWORD_RESET);
        store(user);

        userEventEmitter.emit(new UserEvent<>(userId, EMAIL_CHANGE_REQUESTED, confirmationToken));
    }

    @Override
    public void requestPasswordReset(Long userId) throws NoSuchUserException {
        Objects.requireNonNull(userId, "userId");

        User user = getUser(userId);
        ConfirmationToken confirmationToken = user.addConfirmationToken(PASSWORD_RESET);
        store(user);

        userEventEmitter.emit(new UserEvent<>(userId, PASSWORD_RESET_REQUESTED, confirmationToken));
    }

    @Override
    public void signUp(User user, String rawPassword) throws InvalidEmailException, EmailIsAlreadyTakenException, ScreenNameIsAlreadyTakenException {
        Objects.requireNonNull(user, "user");
        Objects.requireNonNull(rawPassword, "rawPassword");

        String email = user.getEmail();
        if (!Validator.isEmail(email)) {
            throw new InvalidEmailException();
        }

        if (isEmailTaken(email)) {
            throw new EmailIsAlreadyTakenException();
        }

        if (isScreenNameTaken(user.getScreenName())) {
            throw new ScreenNameIsAlreadyTakenException();
        }

        Password password = passwordSecurity.ecrypt(rawPassword);
        user.setPassword(password);
        user = store(user);

        userEventEmitter.emit(new UserEvent(user.getId(), SIGNUP_REQUESTED));
    }

    @Override
    public User store(User user) {
        return userRepository.save(user);
    }

    private void checkEmail(User user, String newEmail)
            throws EmailIsAlreadyTakenException, InvalidEmailException {

        if (!Validator.isEmail(newEmail)) {
            throw new InvalidEmailException();
        }

        Optional<User> otherUser = findUser(newEmail);
        if (otherUser.isPresent() && !user.equals(otherUser.get())) {
            throw new EmailIsAlreadyTakenException();
        }
    }

    private void checkScreenName(User user, String newScreenName)
            throws ScreenNameIsAlreadyTakenException {

        Optional<User> otherUser = findUser(newScreenName);
        if (otherUser.isPresent() && !user.equals(otherUser.get())) {
            throw new ScreenNameIsAlreadyTakenException();
        }
    }
}
