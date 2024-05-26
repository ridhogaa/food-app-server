package org.ergea.foodapp.seeder;

import org.ergea.foodapp.entity.User;
import org.ergea.foodapp.entity.oauth.Role;
import org.ergea.foodapp.entity.oauth.RolePath;
import org.ergea.foodapp.repository.RolePathRepository;
import org.ergea.foodapp.repository.RoleRepository;
import org.ergea.foodapp.repository.UserRepository;
import org.ergea.foodapp.security.AuthServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
@Service
public class DatabaseSeeder implements ApplicationRunner {

    private static final String TAG = "DatabaseSeeder {}";

    private Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthServerConfig authServerConfig;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolePathRepository rolePathRepository;

    private String defaultPassword = "password";

    private String[] users = new String[]{
            "admin@mail.com:ROLE_SUPERUSER ROLE_USER ROLE_ADMIN",
            "user@mail.com:ROLE_USER"
    };

    private String[] clients = new String[]{
            "my-client-apps:ROLE_READ ROLE_WRITE", // mobile
            "my-client-web:ROLE_READ ROLE_WRITE" // web
    };

    private String[] roles = new String[]{
            "ROLE_SUPERUSER:user_role:^/.*:GET|PUT|POST|PATCH|DELETE|OPTIONS",
            "ROLE_ADMIN:user_role:^/.*:GET|PUT|POST|PATCH|DELETE|OPTIONS",
            "ROLE_USER:user_role:^/.*:GET|PUT|POST|PATCH|DELETE|OPTIONS",
            "ROLE_READ:oauth_role:^/.*:GET|PUT|POST|PATCH|DELETE|OPTIONS",
            "ROLE_WRITE:oauth_role:^/.*:GET|PUT|POST|PATCH|DELETE|OPTIONS"
    };


    @Override
    @Transactional
    public void run(ApplicationArguments applicationArguments) {
        String password = encoder.encode(defaultPassword);

        this.insertRoles();
        this.insertUser(password);
    }

    @Transactional
    public void insertRoles() {
        for (String role : roles) {
            String[] str = role.split(":");
            String name = str[0];
            String type = str[1];
            String pattern = str[2];
            String[] methods = str[3].split("\\|");
            Role oldRole = roleRepository.findOneByName(name);
            if (null == oldRole) {
                oldRole = new Role();
                oldRole.setName(name);
                oldRole.setType(type);
                oldRole.setRolePaths(new ArrayList<>());
                for (String m : methods) {
                    String rolePathName = name.toLowerCase() + "_" + m.toLowerCase();
                    RolePath rolePath = rolePathRepository.findOneByName(rolePathName);
                    if (null == rolePath) {
                        rolePath = new RolePath();
                        rolePath.setName(rolePathName);
                        rolePath.setMethod(m.toUpperCase());
                        rolePath.setPattern(pattern);
                        rolePath.setRole(oldRole);
                        rolePathRepository.save(rolePath);
                        oldRole.getRolePaths().add(rolePath);
                    }
                }
            }

            roleRepository.save(oldRole);
        }
    }

//    @Transactional
//    public void insertClients(String password) {
//        for (String c : clients) {
//            String[] s = c.split(":");
//            String clientName = s[0];
//            String[] clientRoles = s[1].split("\\s");
//            RegisteredClient registeredClient = authServerConfig.registeredClientRepository().findByClientId(clientName);
//            if (registeredClient == null) {
//                registeredClient = RegisteredClient
//                        .withId(clientName)
//                        .clientId(clientName)
//                        .clientSecret(password)
//                        .redirectUri("")
//                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                        .redirectUri("")
//                        .scope(OidcScopes.OPENID)
//                        .tokenSettings(TokenSettings.builder()
//                                .accessTokenTimeToLive(Duration.ofHours(1))
//                                .refreshTokenTimeToLive(Duration.ofDays(3))
//                                .build())
//                        .clientSettings(ClientSettings.builder()
//                                .requireAuthorizationConsent(true)
//                                .build())
//                        .build();
//            }
//            authServerConfig.registeredClientRepository().save(registeredClient);
////            Client oldClient = clientRepository.findOneByClientId(clientName);
////            if (null == oldClient) {
////                oldClient = new Client();
////                oldClient.setClientId(clientName);
////                oldClient.setAccessTokenValiditySeconds(28800);//1 jam 3600 :token valid : seharian kerja : normal 1 jam
////                oldClient.setRefreshTokenValiditySeconds(7257600);// refresh
////                oldClient.setGrantTypes("password refresh_token authorization_code");
////                oldClient.setClientSecret(password);
////                oldClient.setApproved(true);
////                oldClient.setRedirectUris("");
////                oldClient.setScopes("read write");
////                List<Role> rls = roleRepository.findByNameIn(clientRoles);
////
////                if (!rls.isEmpty()) {
////                    oldClient.getAuthorities().addAll(rls);
////                }
////            }
////            clientRepository.save(oldClient);
//        }
//    }

    @Transactional
    public void insertUser(String password) {
        for (String userNames : users) {
            String[] str = userNames.split(":");
            String username = str[0];
            String[] roleNames = str[1].split("\\s");

            User oldUser = userRepository.findByEmailAddress(username).orElse(null);
            if (null == oldUser) {
                oldUser = new User();
                oldUser.setUsername(username);
                oldUser.setEmailAddress(username);
                oldUser.setPassword(password);
                List<Role> r = roleRepository.findByNameIn(roleNames);
                oldUser.setRoles(r);
            }

            userRepository.save(oldUser);
        }
    }
}
