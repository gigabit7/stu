package com.example.stu.service;

import com.example.stu.entity.Booking;

import java.util.List;

/**
 * This interface defines the methods that will be used to manage bookings.
 */
public interface IBookingService {
    /**
     * This method will save a booking to the database.
     *
     * @param booking The booking to be saved.
     * @return The saved booking.
     */
    Booking save(Booking booking);

    /**
     * This method will return a list of all bookings in the database.
     *
     * @param userId The id of the user to get bookings for.
     * @return A list of all bookings in the database.
     */
    List<Booking> getBookingsByUserId(Long userId);

    /**
     * This method will return a list of all unconfirmed bookings in the database.
     *
     * @param providerId The id of the provider to get bookings for.
     * @return A list of all unconfirmed bookings in the database.
     */
    List<Booking> getUnconfirmedBookingsByProviderId(Long providerId);

    /**
     * This method will return a list of all confirmed bookings in the database.
     *
     * @param providerId The id of the provider to get bookings for.
     * @return A list of all confirmed bookings in the database.
     */
    List<Booking> getConfirmedBookingsByProviderId(Long providerId);

    /**
     * This method will update a booking and set is_confirmed = true.
     *
     * @param bookingId The id of the booking to update booking for.
     * @return A booking that updated in the database.
     */
    Booking confirmBooking(Long bookingId);

    /**
     * This method will update a booking and set is_confirmed = false.
     *
     * @param id The id of the booking to update booking for.
     * @return A booking that updated in the database.
     */
    Booking rejectBooking(Long id);
}
