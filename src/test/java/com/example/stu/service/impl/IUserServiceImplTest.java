package com.example.stu.service.impl;

import com.example.stu.entity.Role;
import com.example.stu.entity.RoleName;
import com.example.stu.entity.ServiceProvider;
import com.example.stu.entity.User;
import com.example.stu.repository.RoleRepository;
import com.example.stu.repository.UserRepository;
import com.example.stu.service.IProviderService;
import com.example.stu.web.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.stu.entity.RoleName.ROLE_PROVIDER;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
public class IUserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private IProviderService providerService;

    @InjectMocks
    private IUserServiceImpl userService;

    @Test
    public void testSaveUserWithRoleProvider() {
        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("johndoe@example.com");
        userDto.setPhone("555-1234");
        userDto.setPassword("password");
        userDto.setRoleName(ROLE_PROVIDER);

        Role role = new Role();
        role.setId(1L);
        role.setName(ROLE_PROVIDER);

        Mockito.when(roleRepository.findByName(ROLE_PROVIDER)).thenReturn(role);

        userService.saveUser(userDto);

        Mockito.verify(userRepository, Mockito.times(1)).saveAndFlush(Mockito.any(User.class));
        Mockito.verify(providerService, Mockito.times(1)).save(Mockito.any(ServiceProvider.class));
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@example.com");
        user.setPhone("555-1234");

        userService.update(user);

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    public void testFindByEmail() {
        String email = "johndoe@example.com";
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail(email);
        user.setPhone("555-1234");

        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);

        User result = userService.findByEmail(email);

        assertEquals(user, result);
    }

    @Test
    public void testFindAllUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("johndoe@example.com");
        user1.setPhone("555-1234");

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setEmail("janedoe@example.com");
        user2.setPhone("555-5678");

        List<User> users = Arrays.asList(user1, user2);

        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<UserDto> result = userService.findAllUsers();

        assertEquals(2, result.size());
    }

    @Test
    public void testExistsByEmail() {
        String email = "johndoe@example.com";

        Mockito.when(userRepository.existsByEmail(email)).thenReturn(true);

        Boolean result = userService.existsByEmail(email);

        assertTrue(result);
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@example.com");
        user.setPhone("555-1234");

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User result = userService.findById(id);

        assertEquals(user, result);
    }

    @Test
    public void testConvertEntityToDto() {
        // Create a User object
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@example.com");
        user.setPhone("1234567890");

        // Call the method being tested
        UserDto result = userService.convertEntityToDto(user);

        // Verify that the UserDto object has the correct values
        assertEquals(result.getFirstName(), "John");
        assertEquals(result.getLastName(), "Doe");
        assertEquals(result.getEmail(), "johndoe@example.com");
        assertEquals(result.getPhone(), "1234567890");
    }

}