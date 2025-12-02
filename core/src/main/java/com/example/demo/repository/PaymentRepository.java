package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByReservationId(Long reservationId);
}
