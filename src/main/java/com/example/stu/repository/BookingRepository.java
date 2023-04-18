package com.example.stu.repository;

import com.example.stu.entity.Booking;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * This interface is used to interact with the database for Bookings.
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {
    /**
     * This method is used to find all bookings by User Id.
     *
     * @param userId the id of the user/person who-ordered.
     * @return a list of bookings for a user.
     */
    List<Booking> findByUserId(Long userId);

    /**
     * This method is used to find all bookings that are not confirmed.
     *
     * @param providerId the id of the service provider.
     * @return a list of bookings that are not confirmed.
     */
    List<Booking> findByServices_ServiceProvider_IdAndIsConfirmedIsFalse(Long providerId);

    /**
     * This method is used to find all bookings that are confirmed.
     *
     * @param providerId the id of the service provider.
     * @return a list of bookings that are confirmed.
     */
    List<Booking> findByServices_ServiceProvider_IdAndIsConfirmedIsTrue(Long providerId);

    /**
     * This method is used to set confirm = true for bookings that is not confirmed.
     *
     * @param bookingId the id of the booking.
     */
    @Transactional
    @Modifying
    @Query(value = "update bookings b set b.is_confirmed = true where b.id = ?1", nativeQuery = true)
    void confirmBookingById(Long bookingId);
}
