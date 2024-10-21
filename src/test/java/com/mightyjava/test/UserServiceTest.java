package com.mightyjava.test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.mightyjava.model.Role;
import com.mightyjava.model.Users;
import com.mightyjava.repository.RoleRepository;
import com.mightyjava.repository.UserRepository;
import com.mightyjava.service.impl.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.codehaus.jettison.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testUserList() {
        // Given
        List<Users> users = new ArrayList<>();
        users.add(new Users() {{ setFullName("Tiago Lopes"); setUserId("1"); }});
        users.add(new Users() {{ setFullName("Tiago Santos"); setUserId("1"); }});
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<Users> userList = userService.userList();

        // Then
        assertEquals(2, userList.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testAddUser() throws JSONException {
        // Given
        Users user = new Users();
        user.setFullName("Tiago Lopes");
        user.setUserId("32534563546");
        user.setUserName("tal");
        user.setPassword("$2a$10$XBQ9jnH3tqdUSqeTRfvrQOFyZsqxPym29nGKrlyhYUUYU7jg9dvMC");
        user.setEmail("tlopes@gmail.com");
        user.setMobile("1187635463");
        user.setRoleId(1L);

        Role role = new Role();
        role.setId(1L);
        role.setName("tal");

        when(roleRepository.findOne(user.getRoleId())).thenReturn(role);
        when(userRepository.save(user)).thenReturn(user);

        String response = userService.addUser(user);

        assertNotNull(response);
        assertTrue(response.contains("success"));
        assertTrue(response.contains("Added Confirmation"));
        assertTrue(response.contains("Tiago Lopes Added successfully."));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeleteUser() throws JSONException {
        long userId = 1L;

        String response = userService.deleteUser(userId);

        assertNotNull(response);
        assertTrue(response.contains("User Deleted Successfully."));
        verify(userRepository, times(1)).delete(userId);
    }

    @Test
    void testFindOne() {
        // Given
        Users user = new Users();
        user.setId(1L);
        user.setFullName("Tiago Lopes");
        when(userRepository.findOne(1L)).thenReturn(user);

        Users foundUser = userService.findOne(1L);

        assertNotNull(foundUser);
        assertEquals("Tiago Lopes", foundUser.getFullName());
        verify(userRepository, times(1)).findOne(1L);
    }

    @Test
    void testRoleList() {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role() {{ setId(1L); setName("User"); }});
        roles.add(new Role() {{ setId(2L); setName("Admin"); }});
        when(roleRepository.findAll()).thenReturn(roles);

        // When
        List<Role> roleList = userService.roleList();

        // Then
        assertEquals(2, roleList.size());
        verify(roleRepository, times(1)).findAll();
    }
}
