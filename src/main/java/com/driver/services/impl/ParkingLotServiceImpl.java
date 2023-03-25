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
        // choose spot type
        if(numberOfWheels == 2){
            spot.setSpotType(SpotType.TWO_WHEELER);
        }else if (numberOfWheels == 4) {
            spot.setSpotType(SpotType.FOUR_WHEELER);
        }else if(numberOfWheels > 4) {
            spot.setSpotType(SpotType.OTHERS);
        }
        // add spot
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        spot.setPricePerHour(pricePerHour);
        spot.setParkingLot(parkingLot);
        spot.setOccupied(false);
        // save in database
        spotRepository1.save(spot);
        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {
        // find all records from db
        List<ParkingLot> parkingLotList = parkingLotRepository1.findAll();
        // if parkinglot have a F.K i.e = spotId then delete that spot
        for (ParkingLot parkingLot : parkingLotList){
            List<Spot> spotList = parkingLot.getSpotList();
            for (Spot spot : spotList){
                if(spot.getId() == spotId){
                    spotRepository1.deleteById(spotId);
                    return;
                }
            }
        }
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        Spot spot = spotRepository1.findById(spotId).get();
        if(spot.getOccupied())spot.setOccupied(false);
        spot.setPricePerHour(pricePerHour);
        parkingLot.getSpotList().add(spot);
        parkingLotRepository1.save(parkingLot);
        return spot;
    }
    @Override
    public void deleteParkingLot(int parkingLotId) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        List<Spot> parkingLotSpotList = parkingLot.getSpotList();
        List<Spot> spotList = spotRepository1.findAll();
        for (Spot spot : spotList){
            if(parkingLotSpotList.contains(spot)){
                parkingLotSpotList.remove(spot);
                spotRepository1.deleteById(spot.getId());
            }
        }
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
