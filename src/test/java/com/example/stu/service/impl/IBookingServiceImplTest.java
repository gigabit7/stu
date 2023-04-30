package com.example.stu.service.impl;

import com.example.stu.entity.Booking;
import com.example.stu.entity.Services;
import com.example.stu.repository.BookingRepository;
import com.example.stu.service.IServiceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IBookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private IServiceService serviceService;

    @InjectMocks
    private IBookingServiceImpl bookingService;

    @Test
    public void testSaveBooking() {
        Services service = new Services();
        service.setId(1L);
        Booking booking = new Booking();
        booking.setServices(service);
        when(serviceService.getById(1L)).thenReturn(service);
        bookingService.save(booking);
        verify(bookingRepository, times(1)).save(booking);
        assertNotNull(booking.getCreatedAt());
        assertFalse(booking.getIsConfirmed());
    }

    @Test
    public void testGetBookingsByUserId() {
        when(bookingRepository.findByUserId(1L)).thenReturn(Arrays.asList(new Booking(), new Booking()));
        List<Booking> bookings = bookingService.getBookingsByUserId(1L);
        verify(bookingRepository, times(1)).findByUserId(1L);
        assertEquals(2, bookings.size());
    }

    @Test
    public void testGetUnconfirmedBookingsByProviderId() {
        when(bookingRepository.findByServices_ServiceProvider_IdAndIsConfirmedIsFalse(1L)).thenReturn(Arrays.asList(new Booking(), new Booking()));
        List<Booking> bookings = bookingService.getUnconfirmedBookingsByProviderId(1L);
        verify(bookingRepository, times(1)).findByServices_ServiceProvider_IdAndIsConfirmedIsFalse(1L);
        assertEquals(2, bookings.size());
    }

    @Test
    public void testGetConfirmedBookingsByProviderId() {
        when(bookingRepository.findByServices_ServiceProvider_IdAndIsConfirmedIsTrue(1L)).thenReturn(Arrays.asList(new Booking(), new Booking()));
        List<Booking> bookings = bookingService.getConfirmedBookingsByProviderId(1L);
        verify(bookingRepository, times(1)).findByServices_ServiceProvider_IdAndIsConfirmedIsTrue(1L);
        assertEquals(2, bookings.size());
    }

    @Test
    public void testConfirmBooking() {
        Booking booking = new Booking();
        booking.setIsConfirmed(false);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);

        try {
            Booking confirmedBooking = bookingService.confirmBooking(1L);
            verify(bookingRepository, times(1)).findById(1L);
            assertTrue(confirmedBooking.getIsConfirmed());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testConfirmBookingNotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> bookingService.confirmBooking(1L));
    }

    @Test
    public void testRejectBooking() {
        Booking booking = new Booking();
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        Booking deletedBooking = bookingService.rejectBooking(1L);
        verify(bookingRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).delete(booking);
        assertSame(booking, deletedBooking);
    }

    @Test
    public void testRejectBookingNotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> bookingService.rejectBooking(1L));
    }

    @Test
    public void testDateRangeIsValid() {
        assertTrue(bookingService.dateRangeIsValid(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 2)));
        assertFalse(bookingService.dateRangeIsValid(LocalDate.of(2022, 1, 1), LocalDate.of(2021, 1, 2)));
    }
}