//package com.aminebag.larjson.jsonmapper.configuration;
//
//import com.aminebag.larjson.jsonmapper.valueconverter.StringValueConverter;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.ZonedDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Date;
//
///**
// * @author Amine Bagdouri
// */
//public interface LarJsonGenericWriteConfiguration extends LarJsonWriteConfiguration {
//
//    class Builder extends LarJsonWriteConfiguration.Builder<Builder> {
//
//        private class Configuration extends LarJsonWriteConfiguration.Builder.Configuration
//                implements LarJsonGenericWriteConfiguration {
//        }
//
//        public LarJsonGenericWriteConfiguration build() {
//            flagBuilt();
//            return new Configuration();
//        }
//    }
//}
