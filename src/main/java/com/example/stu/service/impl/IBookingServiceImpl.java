package com.example.stu.service.impl;

import com.example.stu.entity.Booking;
import com.example.stu.entity.Services;
import com.example.stu.repository.BookingRepository;
import com.example.stu.service.IBookingService;
import com.example.stu.service.IServiceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
/**
 * This class implements the methods defined in the IBookingService interface.
 * See the IBookingService interface for method documentation.
 */
@Service
@AllArgsConstructor
public class IBookingServiceImpl implements IBookingService {
    private final BookingRepository bookingRepository;
    private final IServiceService serviceService;

    @Override
    public Booking save(Booking booking) {
        Services services = serviceService.getById(booking.getServices().getId());
        booking.setServices(services);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setIsConfirmed(false);
        bookingRepository.save(booking);
        return booking;
    }

    @Override
    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public List<Booking> getUnconfirmedBookingsByProviderId(Long providerId) {
        return bookingRepository.findByServices_ServiceProvider_IdAndIsConfirmedIsFalse(providerId);
    }

    @Override
    public List<Booking> getConfirmedBookingsByProviderId(Long providerId) {
        return bookingRepository.findByServices_ServiceProvider_IdAndIsConfirmedIsTrue(providerId);
    }

    @Override
    public Booking confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking != null) {
            booking.setIsConfirmed(true);
            return bookingRepository.save(booking);
        }
        throw new IllegalStateException("No Booking Found!");
    }

    @Override
    public Booking rejectBooking(Long id) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking != null) {
            bookingRepository.delete(booking);
            return booking;
        }
        throw new IllegalStateException("No Booking Found!");
    }
}
