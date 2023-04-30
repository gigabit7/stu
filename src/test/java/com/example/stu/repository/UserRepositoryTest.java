package com.example.stu.repository;

import com.example.stu.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @Test
    public void testFindByEmail() {
        // arrange
        String email = "user@example.com";
        User expectedUser = new User();
        expectedUser.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(expectedUser);

        // act
        User actualUser = userRepository.findByEmail(email);

        // assert
        assertEquals(expectedUser, actualUser);
        verify(userRepository).findByEmail(email);
    }

    @Test
    public void testExistsByEmail() {
        // arrange
        String email = "user@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // act
        Boolean userExists = userRepository.existsByEmail(email);

        // assert
        assertTrue(userExists);
        verify(userRepository).existsByEmail(email);
    }
}
