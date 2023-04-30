package com.example.stu.repository;

import com.example.stu.entity.Booking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingRepositoryTest {

    @Mock
    private BookingRepository bookingRepository;

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        List<Booking> bookings = List.of(booking1, booking2);

        Mockito.when(bookingRepository.findByUserId(userId)).thenReturn(bookings);

        List<Booking> result = bookingRepository.findByUserId(userId);

        assertEquals(bookings, result);
    }

    @Test
    public void testFindByServicesServiceProviderIdAndIsConfirmedIsFalse() {
        Long providerId = 1L;
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        List<Booking> bookings = List.of(booking1, booking2);

        Mockito.when(bookingRepository.findByServices_ServiceProvider_IdAndIsConfirmedIsFalse(providerId)).thenReturn(bookings);

        List<Booking> result = bookingRepository.findByServices_ServiceProvider_IdAndIsConfirmedIsFalse(providerId);

        assertEquals(bookings, result);
    }

    @Test
    public void testFindByServicesServiceProviderIdAndIsConfirmedIsTrue() {
        Long providerId = 1L;
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        List<Booking> bookings = List.of(booking1, booking2);

        Mockito.when(bookingRepository.findByServices_ServiceProvider_IdAndIsConfirmedIsTrue(providerId)).thenReturn(bookings);

        List<Booking> result = bookingRepository.findByServices_ServiceProvider_IdAndIsConfirmedIsTrue(providerId);

        assertEquals(bookings, result);
    }

    @Test
    public void testConfirmBookingById() {
        Long bookingId = 1L;

        doNothing().when(bookingRepository).confirmBookingById(bookingId);

        bookingRepository.confirmBookingById(bookingId);

        verify(bookingRepository, times(1)).confirmBookingById(bookingId);
    }

    @Test
    public void testConfirmBookingById_ThrowsException() {
        Long bookingId = null;
        doThrow(IllegalArgumentException.class).when(bookingRepository).confirmBookingById(bookingId);

        assertThrows(IllegalArgumentException.class, () -> bookingRepository.confirmBookingById(bookingId));

        verify(bookingRepository, times(1)).confirmBookingById(bookingId);
    }

    @Test
    public void testFindByUserId_ReturnsEmptyList() {
        Long userId = 1L;

        Mockito.when(bookingRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        List<Booking> result = bookingRepository.findByUserId(userId);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByServicesServiceProviderIdAndIsConfirmedIsFalse_ReturnsEmptyList() {
        Long providerId = 1L;

        Mockito.when(bookingRepository.findByServices_ServiceProvider_IdAndIsConfirmedIsFalse(providerId)).thenReturn(Collections.emptyList());

        List<Booking> result = bookingRepository.findByServices_ServiceProvider_IdAndIsConfirmedIsFalse(providerId);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByServicesServiceProviderIdAndIsConfirmedIsTrue_ReturnsEmptyList() {
        Long providerId = 1L;

        Mockito.when(bookingRepository.findByServices_ServiceProvider_IdAndIsConfirmedIsTrue(providerId)).thenReturn(Collections.emptyList());

        List<Booking> result = bookingRepository.findByServices_ServiceProvider_IdAndIsConfirmedIsTrue(providerId);

        assertTrue(result.isEmpty());
    }
}