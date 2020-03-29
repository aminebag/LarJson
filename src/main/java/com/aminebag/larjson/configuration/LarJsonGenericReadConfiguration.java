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
//public interface LarJsonGenericReadConfiguration extends LarJsonReadConfiguration {
//
//    LarJsonGenericReadConfiguration DEFAULT = new Builder().build();
//
//    LarJsonGenericWriteConfiguration toWriteConfiguration();
//
//    class Builder extends LarJsonReadConfiguration.Builder<Builder> {
//
//        private static final LarJsonGenericWriteConfiguration DEFAULT_WRITE_CONFIGURATION =
//                new LarJsonGenericWriteConfiguration.Builder().build();
//
//        private class Configuration extends LarJsonReadConfiguration.Builder.Configuration
//                implements LarJsonGenericReadConfiguration {
//
//            LarJsonGenericWriteConfiguration writeConfiguration = new LarJsonGenericWriteConfiguration() {
//                @Override
//                public boolean isLenient() {
//                    return Configuration.this.isLenient();
//                }
//
//                @Override
//                public boolean isHtmlSafe() {
//                    return DEFAULT_WRITE_CONFIGURATION.isHtmlSafe();
//                }
//
//                @Override
//                public boolean getSerializeNulls() {
//                    return DEFAULT_WRITE_CONFIGURATION.getSerializeNulls();
//                }
//
//                @Override
//                public String getIndent() {
//                    return DEFAULT_WRITE_CONFIGURATION.getIndent();
//                }
//            };
//
//            @Override
//            public LarJsonGenericWriteConfiguration toWriteConfiguration() {
//                return writeConfiguration;
//            }
//        }
//
//        public LarJsonGenericReadConfiguration build() {
//            flagBuilt();
//            return new Configuration();
//        }
//    }
//}
