package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        //Note that the reservationId always exists
        Payment payment = new Payment();
        Reservation reservation;
        Spot spot;
        int bill;
        mode = mode.toUpperCase();
        //If the amountSent is less than bill, throw "Insufficient Amount" exception, otherwise update payment attributes
        try {
            reservation = reservationRepository2.findById(reservationId).get();
            spot = reservation.getSpot();
            bill = spot.getPricePerHour() * reservation.getNumberOfHours();
            if(amountSent < bill){
                throw new Exception();
            }
        }catch (Exception e){
            payment.setPaymentCompleted(false);
            paymentRepository2.save(payment);
            throw new Exception("Insufficient Amount");
        }
        //If the mode contains a string other than "cash", "card", or "upi" (any character in uppercase or lowercase),
        // throw "Payment mode not detected" exception.
        boolean marker = true;
        switch (mode) {
            case "CASH":
                marker = false;
                break;
            case "CARD":
                marker = false;
                break;
            case "UPI":
                marker = false;
                break;
        }
        try {
            if(marker) throw new Exception();
        }catch (Exception e){
            payment.setPaymentCompleted(false);
            paymentRepository2.save(payment);
            throw new Exception("Payment mode not detected");
        }
        //Attempt a payment of amountSent for reservationId using the given mode ("cASh", "card", or "upi")
        payment.setPaymentMode(PaymentMode.valueOf(mode));
        payment.setReservation(reservation);
        payment.setPaymentCompleted(true);
        reservation.setPayment(payment);
        paymentRepository2.save(payment);
        return payment;
    }
}
