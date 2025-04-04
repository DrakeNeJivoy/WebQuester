    package com.example.WebQuest.service;

    import com.example.WebQuest.model.User;
    import com.example.WebQuest.service.EmailSender;
    import com.example.WebQuest.repository.UserRepository;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Service;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.transaction.annotation.Transactional;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import java.util.Collections;

    import java.util.Optional;
    import java.util.UUID;

    @Service
    public class UserService implements UserDetailsService{
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final EmailSender emailSender;

        @Value("${app.base-url}")
        private String baseUrl;

        public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailSender emailSender) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
            this.emailSender = emailSender;
        }

    //    public boolean registerUser(User user) {
    //        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
    //            return false;
    //        }
    //
    //        user.setPassword(passwordEncoder.encode(user.getPassword()));
    //        user.setVerified(false);
    //        String token = UUID.randomUUID().toString();
    //        user.setToken(token);
    //        userRepository.save(user);
    //
    //        String confirmationLink = baseUrl + "/confirm?token=" + token;
    //        emailSender.sendEmail(user.getEmail(), "Подтверждение регистрации",
    //                "Перейдите по ссылке, чтобы подтвердить регистрацию: " + confirmationLink);
    //
    //        return true;
    //    }

        // Регистрация пользователя
        public boolean registerUser(String username, String email, String password) {
            if (userRepository.findByUsername(username).isPresent()) {
                return false;
            }

            String token = UUID.randomUUID().toString();
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            //String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setVerified(false);

            userRepository.save(user);

            // Отправка email с подтверждением
            String confirmLink = "http://localhost:8080/confirm-email?token=" + token;
            emailSender.sendEmail(email, "Confirm your email",
                    "Click the link to confirm your email: " + confirmLink);

            return true;
        }

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            System.out.println("Попытка найти пользователя: " + email);

            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isEmpty()) {
                System.out.println("Пользователь не найден: " + email);
                throw new UsernameNotFoundException("Пользователь не найден");
            }

            if (!userOptional.get().isConfirmed()) {
                System.out.println("Пользователь не верефицирован: " + email);
                throw new UsernameNotFoundException("Не верефицирован");
            }
            User user = userOptional.get();
            System.out.println("Пользователь найден: " + user.getEmail());
            System.out.println("Пароль в базе данных: " + user.getPassword());

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    Collections.emptyList()
            );
        }

        // Подтверждение email
        @Transactional // Добавляем аннотацию
        public boolean confirmEmail(String token) {
            Optional<User> userOptional = userRepository.findByToken(token);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setConfirmed(true);
                user.setToken(null);
                userRepository.save(user);
                return true;
            }
            return false;
        }

        public User getUserByEmail(String email) {
            return userRepository.findByEmail(email).orElse(null);
        }

    }
