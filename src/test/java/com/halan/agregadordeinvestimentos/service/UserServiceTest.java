package com.halan.agregadordeinvestimentos.service;

import com.halan.agregadordeinvestimentos.controller.dto.CreateUserDto;
import com.halan.agregadordeinvestimentos.controller.dto.UpdateUserDto;
import com.halan.agregadordeinvestimentos.entity.User;
import com.halan.agregadordeinvestimentos.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Nested
    class createUser {

        @Test
        @DisplayName("Should create a user with success")
        void shouldCreateAUserWithSuccess() {
            var user = new User(
                    UUID.randomUUID(),
                    "Halan Germano Bacca",
                    "halanbacca@outlook.com",
                    "123456789",
                    Instant.now(),
                    null
            );

            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());

            var input = new CreateUserDto(
                    "Halan",
                    "halanbacca@outlook.com",
                    "123456789");

            var output = userService.createUser(input);
            var userCaptured = userArgumentCaptor.getValue();

            assertNotNull(output);
            assertEquals(input.username(), userCaptured.getUsername());
            assertEquals(input.password(), userCaptured.getPassword());
            assertEquals(input.email(), userCaptured.getEmail());
        }

        @Test
        @DisplayName("Should throw exception when error occurs")
        void shouldThrowExceptionWhenErrorOccurs() {
            doThrow(new RuntimeException()).when(userRepository).save(userArgumentCaptor.capture());

            var input = new CreateUserDto(
                    "Halan",
                    "halanbacca@outlook.com",
                    "123456789");

            assertThrows(RuntimeException.class, () -> userService.createUser(input));
        }
    }

    @Nested
    class getUserById {

        @Test
        @DisplayName("Should get user by id with success when optional is present")
        void shouldGetUserByIdWithSuccessWhenOptionalIsPresent() {
            var user = new User(
                    UUID.randomUUID(),
                    "Halan Germano Bacca",
                    "halanbacca@outlook.com",
                    "123456789",
                    Instant.now(),
                    null
            );

            doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());

            var output = userService.getUserById(user.getUserId().toString());

            assertTrue(output.isPresent());
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("Should not get user by id when optional is empty")
        void shouldNotGetUserByIdWhenOptionalIsEmpty() {
            var userId = UUID.randomUUID();

            doReturn(Optional.empty()).when(userRepository).findById(uuidArgumentCaptor.capture());

            var output = userService.getUserById(userId.toString());

            assertTrue(output.isEmpty());
            assertEquals(userId, uuidArgumentCaptor.getValue());
        }
    }

    @Nested
    class listUsers {

        @Test
        @DisplayName("Should return all users with success")
        void shouldReturnAllUsersWithSuccess() {
            var user = new User(
                    UUID.randomUUID(),
                    "Halan Germano Bacca",
                    "halanbacca@outlook.com",
                    "123456789",
                    Instant.now(),
                    null
            );

            doReturn(List.of(user)).when(userRepository).findAll();

            var output = userService.findAll();

            assertNotNull(output);
            assertEquals(1, output.size());
        }
    }

    @Nested
    class deleteById {

        @Test
        @DisplayName("Should delete user with success when user exists")
        void shouldDeleteUserWithSuccessWhenUserExists() {

            doReturn(true).when(userRepository).existsById(uuidArgumentCaptor.capture());
            doNothing().when(userRepository).deleteById(uuidArgumentCaptor.capture());

            var userId = UUID.randomUUID();
            userService.deleteById(userId.toString());
            var idList = uuidArgumentCaptor.getAllValues();

            assertEquals(userId, idList.get(0));
            assertEquals(userId, idList.get(1));

            verify(userRepository, times(1)).existsById(idList.get(0));
            verify(userRepository, times(1)).deleteById(idList.get(1));
        }

        @Test
        @DisplayName("Should not delete user when user do not exists")
        void shouldNotDeleteUserWhenUserDoNotExists() {

            doReturn(false).when(userRepository).existsById(uuidArgumentCaptor.capture());

            var userId = UUID.randomUUID();
            userService.deleteById(userId.toString());

            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(userRepository, times(1)).existsById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(0)).deleteById(any());
        }
    }

    @Nested
    class updateUserById {

        @Test
        @DisplayName("Should update user by id when user exists and username and password are filled")
        void shouldUpdateUserByIdWhenUserExistsAndUsernameAndPasswordAreFilled() {

            var updateUserDto = new UpdateUserDto(
                    "new username",
                    "new password");

            var user = new User(
                    UUID.randomUUID(),
                    "Halan Germano Bacca",
                    "halanbacca@outlook.com",
                    "123456789",
                    Instant.now(),
                    null
            );

            doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());
            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());

            userService.updateById(user.getUserId().toString(), updateUserDto);

            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());

            var userCaptured = userArgumentCaptor.getValue();

            assertEquals(updateUserDto.username(), userCaptured.getUsername());
            assertEquals(updateUserDto.password(), userCaptured.getPassword());

            verify(userRepository, times(1)).findById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(1)).save(user);
        }

        @Test
        @DisplayName("Should not update user when user does not exist")
        void shouldNotUpdateUserWhenUserDoesNotExist() {

            var updateUserDto = new UpdateUserDto(
                    "new username",
                    "new password");

            var userId = UUID.randomUUID();

            doReturn(Optional.empty()).when(userRepository).findById(uuidArgumentCaptor.capture());

            userService.updateById(userId.toString(), updateUserDto);

            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(userRepository, times(1)).findById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(0)).save(any());
        }
    }
}