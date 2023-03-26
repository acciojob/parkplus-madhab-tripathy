package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    private int getWheelerType(SpotType spotType){
        if(spotType == SpotType.TWO_WHEELER){
            return 2;
        } else if (spotType == SpotType.FOUR_WHEELER) {
            return 4;
        }
        return Integer.MAX_VALUE;
    }
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        //Reserve a spot in the given parkingLot such that the total price is minimum. Note that the price per hour for each spot is different
        //Note that the vehicle can only be parked in a spot having a type equal to or larger than given vehicle
        //If parkingLot is not found, user is not found, or no spot is available, throw "Cannot make reservation" exception.
        User user;
        ParkingLot parkingLot;
        Spot bookedSpot = null;
        Reservation reservation = new Reservation();

        int totalPrice = Integer.MAX_VALUE;
        boolean isSpotAvailable = false;
        try {
            user = userRepository3.findById(userId).get();
            parkingLot = parkingLotRepository3.findById(parkingLotId).get();
            List<Spot> spotList = parkingLot.getSpotList();
            for (Spot spot1 : spotList){
                int wheels = getWheelerType(spot1.getSpotType());
                if(!spot1.getOccupied() && spot1.getPricePerHour() < totalPrice && wheels >= numberOfWheels){
                    totalPrice = spot1.getPricePerHour();
                    spot1.setOccupied(true);
                    bookedSpot = spot1;
                    isSpotAvailable = true;
                }
            }
            if(!isSpotAvailable){
                throw new Exception();
            }
        }
        catch (Exception e){
            throw new Exception("Cannot make reservation");
        }

        reservation.setNumberOfHours(timeInHours);
        reservation.setSpot(bookedSpot);
        reservation.setUser(user);
        user.getReservationList().add(reservation);
        bookedSpot.getReservationList().add(reservation);
        parkingLot.getSpotList().add(bookedSpot);
        reservationRepository3.save(reservation);
        return reservation;
    }
}
