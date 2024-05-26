package org.ergea.foodapp.config;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Config {
    public String convertDateToString(Date date) {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String strDate = dateFormat.format(date);
        return strDate;
    }
}
