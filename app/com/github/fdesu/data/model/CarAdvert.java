package com.github.fdesu.data.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CAR_ADVERT")
public class CarAdvert {

    public CarAdvert() {
    }

    public CarAdvert(Long id, String title, Fuel fuel,
                     Integer price, Boolean isNew, Integer mileage,
                     Date registrationDate) {
        this.id = id;
        this.title = title;
        this.fuel = fuel;
        this.price = price;
        this.isNew = isNew;
        this.mileage = mileage;
        this.registrationDate = registrationDate;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private Fuel fuel;

    private Integer price;

    private Boolean isNew;

    private Integer mileage;

    private Date registrationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Fuel getFuel() {
        return fuel;
    }

    public void setFuel(Fuel fuel) {
        this.fuel = fuel;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Boolean isNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

}
