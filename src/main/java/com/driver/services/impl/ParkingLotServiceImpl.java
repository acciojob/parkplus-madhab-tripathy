package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.w3c.dom.css.CSS2Properties;

@Service
public class ParkingLotServiceImpl implements ParkingLotService{
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);
        parkingLotRepository1.save(parkingLot);
        return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        Spot spot = new Spot();
        spot.setPricePerHour(pricePerHour);
        spot.setOccupied(false);
        // choose spot type
        if(numberOfWheels <= 2){
            spot.setSpotType(SpotType.TWO_WHEELER);
        }else if (numberOfWheels <= 4) {
            spot.setSpotType(SpotType.FOUR_WHEELER);
        }else {
            spot.setSpotType(SpotType.OTHERS);
        }
        // add spot
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        parkingLot.getSpotList().add(spot);
        spot.setParkingLot(parkingLot);
        // save in database
        parkingLotRepository1.save(parkingLot);
        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {
        // find all records from db
        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        List<Spot> spotList = parkingLot.getSpotList();
        Spot updateSpot = null;
        for(Spot spot : spotList){
            if(spot.getId() == spotId){
                spot.setPricePerHour(pricePerHour);
                updateSpot = spot;
                spotRepository1.save(spot);
                break;
            }
        }
        return updateSpot;
    }
    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
