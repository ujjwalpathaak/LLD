package FoodDelivery.Managers;

import java.util.ArrayList;
import java.util.List;

import FoodDelivery.Factories.RestrauntFactory;
import FoodDelivery.Models.Restaurant;

public class RestrauntManager {
    private List<Restaurant> restaurants;

    public RestrauntManager() {
        this.restaurants = new ArrayList<>();

        RestrauntFactory rf = new RestrauntFactory();
        this.restaurants.add(rf.getRestraunt("FastFood"));
        this.restaurants.add(rf.getRestraunt("Indian"));
        this.restaurants.add(rf.getRestraunt("SouthIndian"));
    }

    public List<String> getAllRestrauntsNames() {
        List<String> names = new ArrayList<>(); 
        for (Restaurant restaurant : this.restaurants) {
            names.add(restaurant.getName());
        }
        return names;
    }

    public List<Restaurant> getAllRestraunts() {
        return restaurants;
    }
}