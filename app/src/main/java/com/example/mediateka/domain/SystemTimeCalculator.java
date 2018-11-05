package com.example.mediateka.domain;


import com.example.mediateka.models.model.DateAndTimeInfo;

public interface SystemTimeCalculator {
    long futureTimeInMillisFromDateAndTimeInfo(DateAndTimeInfo dateAndTimeInfo);
    long currentTimeInMillis();
}
