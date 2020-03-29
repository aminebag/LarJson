package com.aminebag.larjson.configuration;

import com.aminebag.larjson.configuration.propertyresolver.*;
import com.aminebag.larjson.mapper.LarJsonTypedMapper;
import com.aminebag.larjson.valueconverter.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author Amine Bagdouri
 *
 * This interface represents a configuration for a {@link LarJsonTypedMapper} used to customize how a model object is
 * written.
 * Implementations must be immutable and thread-safe.
 * It is highly recommended to use a {@link LarJsonTypedWriteConfiguration.Builder} to build an instance of this
 * interface.
 */
public interface LarJsonTypedWriteConfiguration extends LarJsonWriteConfiguration {

    /**
     * @return a factory of {@link StringValueConverter}s used to convert JSON strings to custom types.
     */
    StringValueConverterFactory getStringValueConverterFactory();

    /**
     * @return a factory of {@link PropertyResolver} used to map JSON attributes to getter and setter methods
     */
    PropertyResolverFactory getPropertyResolverFactory();

    /**
     * @return a factory of {@link PropertyConfiguration}s used to customize model properties
     */
    PropertyConfigurationFactory getPropertyConfigurationFactory();

    /**
     * @return a factory of {@link AnnotationConfiguration}s used to customize the use of annotations
     */
    AnnotationConfigurationFactory getAnnotationConfigurationFactory();

    class Builder extends LarJsonWriteConfiguration.Builder<Builder> {

        private PropertyResolverFactory propertyResolverFactory = CamelCasePropertyResolver::new;
        private DefaultStringValueConverterFactory stringValueConverterFactory =
                new DefaultStringValueConverterFactory();
        private final DefaultPropertyConfigurationFactory propertyConfigurationFactory =
                new DefaultPropertyConfigurationFactory();
        private final DefaultAnnotationConfigurationFactory annotationConfigurationFactory =
                new DefaultAnnotationConfigurationFactory();

        /**
         * Set a property resolver factory.
         * <p>
         *      The default property resolver is based on the camel case style of writing.
         * </p>
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getPropertyResolverFactory()
         * @see #setCamelCase()
         */
        public Builder setPropertyResolverFactory(PropertyResolverFactory propertyResolverFactory) {
            checkNotBuilt();
            this.propertyResolverFactory = propertyResolverFactory;
            return this;
        }

        /**
         * Set a camel case property resolver factory.
         * <p>
         *      With this property resolver factory, a JSON attribute named {@code 'xyzAbc'} can be mapped to a getter
         *      with the pattern {@code '<type> getXyzAbc()'}. If the return type of the getter is a {@code boolean} or
         *      a {@link Boolean} the getter's pattern can also be {@code '<type> isXyzAbc()'}.
         *      The setter's pattern must be {@code 'void setXyzAbc(<type> value)'}.
         * </p>
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getPropertyResolverFactory()
         */
        public Builder setCamelCase() {
            checkNotBuilt();
            this.propertyResolverFactory = CamelCasePropertyResolver::new;
            return this;
        }

        /**
         * Set a Pascal case property resolver factory.
         * <p>
         *      With this property resolver factory, a JSON attribute named {@code 'XyzAbc'} can be mapped to a getter
         *      with the pattern {@code '<type> getXyzAbc()'}. If the return type of the getter is a {@code boolean} or
         *      a {@link Boolean} the getter's pattern can also be {@code '<type> isXyzAbc()'}.
         *      The setter's pattern must be {@code 'void setXyzAbc(<type> value)'}.
         * </p>
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getPropertyResolverFactory()
         */
        public Builder setPascalCase() {
            checkNotBuilt();
            this.propertyResolverFactory = PascalCasePropertyResolver::new;
            return this;
        }

        /**
         * Set a kebab case property resolver factory.
         * <p>
         *      With this property resolver factory, a JSON attribute named {@code 'xyz-abc'} can be mapped to a getter
         *      with the pattern {@code '<type> getXyzAbc()'}. If the return type of the getter is a {@code boolean} or
         *      a {@link Boolean} the getter's pattern can also be {@code '<type> isXyzAbc()'}.
         *      The setter's pattern must be {@code 'void setXyzAbc(<type> value)'}.
         * </p>
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getPropertyResolverFactory()
         */
        public Builder setKebabCase() {
            checkNotBuilt();
            this.propertyResolverFactory = KebabCasePropertyResolver::new;
            return this;
        }

        /**
         * Set a snake lower case property resolver factory.
         * <p>
         *      With this property resolver factory, a JSON attribute named {@code 'xyz_abc'} can be mapped to a getter
         *      with the pattern {@code '<type> getXyzAbc()'}. If the return type of the getter is a {@code boolean} or
         *      a {@link Boolean} the getter's pattern can also be {@code '<type> isXyzAbc()'}.
         *      The setter's pattern must be {@code 'void setXyzAbc(<type> value)'}.
         * </p>
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getPropertyResolverFactory()
         */
        public Builder setLowerSnakeCase() {
            checkNotBuilt();
            this.propertyResolverFactory = LowerSnakeCasePropertyResolver::new;
            return this;
        }

        /**
         * Set a snake upper case property resolver factory.
         * <p>
         *      With this property resolver factory, a JSON attribute named {@code 'XYZ_ABC'} can be mapped to a getter
         *      with the pattern {@code '<type> getXyzAbc()'}. If the return type of the getter is a {@code boolean} or
         *      a {@link Boolean} the getter's pattern can also be {@code '<type> isXyzAbc()'}.
         *      The setter's pattern must be {@code 'void setXyzAbc(<type> value)'}.
         * </p>
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getPropertyResolverFactory()
         */
        public Builder setUpperSnakeCase() {
            checkNotBuilt();
            this.propertyResolverFactory = UpperSnakeCasePropertyResolver::new;
            return this;
        }

        /**
         * Set a dot lower case property resolver factory.
         * <p>
         *      With this property resolver factory, a JSON attribute named {@code 'xyz.abc'} can be mapped to a getter
         *      with the pattern {@code '<type> getXyzAbc()'}. If the return type of the getter is a {@code boolean} or
         *      a {@link Boolean} the getter's pattern can also be {@code '<type> isXyzAbc()'}.
         *      The setter's pattern must be {@code 'void setXyzAbc(<type> value)'}.
         * </p>
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getPropertyResolverFactory()
         */
        public Builder setLowerDotCase() {
            checkNotBuilt();
            this.propertyResolverFactory = LowerDotCasePropertyResolver::new;
            return this;
        }

        /**
         * Set a dot upper case property resolver factory.
         * <p>
         *      With this property resolver factory, a JSON attribute named {@code 'XYZ.ABC'} can be mapped to a getter
         *      with the pattern {@code '<type> getXyzAbc()'}. If the return type of the getter is a {@code boolean} or
         *      a {@link Boolean} the getter's pattern can also be {@code '<type> isXyzAbc()'}.
         *      The setter's pattern must be {@code 'void setXyzAbc(<type> value)'}.
         * </p>
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getPropertyResolverFactory()
         */
        public Builder setUpperDotCase() {
            checkNotBuilt();
            this.propertyResolverFactory = UpperDotCasePropertyResolver::new;
            return this;
        }

        /**
         * Set the JSON attribute name for the property identified by the getter method
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getPropertyConfigurationFactory()
         */
        public Builder setPropertyName(Method getterMethod, String name) {
            checkNotBuilt();
            this.propertyConfigurationFactory.setPropertyName(getterMethod, name);
            return this;
        }

        /**
         * Set whether the property identified by the getter method must be ignored
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getPropertyConfigurationFactory()
         */
        public Builder setPropertyIgnored(Method getterMethod, boolean ignored) {
            checkNotBuilt();
            this.propertyConfigurationFactory.setPropertyIgnored(getterMethod, ignored);
            return this;
        }

        /**
         * Set the string value converter for the property identified by the getter method
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getPropertyConfigurationFactory()
         */
        public Builder setPropertyStringValueConverter(Method getterMethod, StringValueConverter<?>
                stringValueConverter) {
            checkNotBuilt();
            this.propertyConfigurationFactory.setPropertyStringValueConverter(getterMethod, stringValueConverter);
            return this;
        }

        /**
         * Enables the {@link JsonFormat} annotation support.
         * By default, this annotation support is disabled.
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getAnnotationConfigurationFactory() ()
         */
        public Builder enableJsonFormatAnnotation() {
            checkNotBuilt();
            this.annotationConfigurationFactory.setAnnotationConfiguration(JsonFormat.class, true);
            return this;
        }

        /**
         * Disables the {@link JsonFormat} annotation support
         * By default, this annotation support is disabled.
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getAnnotationConfigurationFactory() ()
         */
        public Builder disableJsonFormatAnnotation() {
            checkNotBuilt();
            this.annotationConfigurationFactory.setAnnotationConfiguration(JsonFormat.class, false);
            return this;
        }

        /**
         * Enables the {@link JsonProperty} annotation support.
         * By default, this annotation support is disabled.
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getAnnotationConfigurationFactory() ()
         */
        public Builder enableJsonPropertyAnnotation() {
            checkNotBuilt();
            this.annotationConfigurationFactory.setAnnotationConfiguration(JsonProperty.class, true);
            return this;
        }

        /**
         * Disabled the {@link JsonProperty} annotation support.
         * By default, this annotation support is disabled.
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getAnnotationConfigurationFactory() ()
         */
        public Builder disableJsonPropertyAnnotation() {
            checkNotBuilt();
            this.annotationConfigurationFactory.setAnnotationConfiguration(JsonProperty.class, false);
            return this;
        }

        /**
         * Enables the {@link JsonIgnore} annotation support.
         * By default, this annotation support is disabled.
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getAnnotationConfigurationFactory() ()
         */
        public Builder enableJsonIgnoreAnnotation() {
            checkNotBuilt();
            this.annotationConfigurationFactory.setAnnotationConfiguration(JsonIgnore.class, true);
            return this;
        }

        /**
         * Disables the {@link JsonIgnore} annotation support.
         * By default, this annotation support is disabled.
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getAnnotationConfigurationFactory() ()
         */
        public Builder disableJsonIgnoreAnnotation() {
            checkNotBuilt();
            this.annotationConfigurationFactory.setAnnotationConfiguration(JsonIgnore.class, false);
            return this;
        }

        /**
         * Set the date format for the {@link Date} string value converter
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getStringValueConverterFactory()
         */
        public Builder setDateFormat(String dateFormat) {
            checkNotBuilt();
            stringValueConverterFactory.setStringValueConverter(Date.class,
                    new DateValueConverter(dateFormat, null, null));
            return this;
        }

        /**
         * Set the date format for the {@link LocalDateTime} string value converter
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getStringValueConverterFactory()
         */
        public Builder setLocalDateTimeFormat(String dateFormat) {
            checkNotBuilt();
            stringValueConverterFactory.setStringValueConverter(LocalDateTime.class,
                    new LocalDateTimeValueConverter(dateFormat, null, null));
            return this;
        }

        /**
         * Set the date format for the {@link LocalDate} string value converter
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getStringValueConverterFactory()
         */
        public Builder setLocalDateFormat(String dateFormat) {
            checkNotBuilt();
            stringValueConverterFactory.setStringValueConverter(LocalDate.class,
                    new LocalDateValueConverter(dateFormat, null, null));
            return this;
        }

        /**
         * Set the date format for the {@link LocalTime} string value converter
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getStringValueConverterFactory()
         */
        public Builder setLocalTimeFormat(String dateFormat) {
            checkNotBuilt();
            stringValueConverterFactory.setStringValueConverter(LocalTime.class,
                    new LocalTimeValueConverter(dateFormat, null, null));
            return this;
        }

        /**
         * Set the date format for the {@link ZonedDateTime} string value converter
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getStringValueConverterFactory()
         */
        public Builder setZonedDateTimeFormat(String dateFormat) {
            checkNotBuilt();
            stringValueConverterFactory.setStringValueConverter(ZonedDateTime.class,
                    new ZonedDateTimeValueConverter(dateFormat, null, null));
            return this;
        }

        /**
         * Set a {@link DateTimeFormatter} for the {@link LocalDateTime} string value converter
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getStringValueConverterFactory()
         */
        public Builder setLocalDateTimeFormatter(DateTimeFormatter formatter) {
            checkNotBuilt();
            stringValueConverterFactory.setStringValueConverter(LocalDateTime.class,
                    new LocalDateTimeValueConverter(formatter));
            return this;
        }

        /**
         * Set a {@link DateTimeFormatter} for the {@link LocalDate} string value converter
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getStringValueConverterFactory()
         */
        public Builder setLocalDateFormatter(DateTimeFormatter formatter) {
            checkNotBuilt();
            stringValueConverterFactory.setStringValueConverter(LocalDate.class,
                    new LocalDateValueConverter(formatter));
            return this;
        }

        /**
         * Set a {@link DateTimeFormatter} for the {@link LocalTime} string value converter
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getStringValueConverterFactory()
         */
        public Builder setLocalTimeFormatter(DateTimeFormatter formatter) {
            checkNotBuilt();
            stringValueConverterFactory.setStringValueConverter(LocalTime.class,
                    new LocalTimeValueConverter(formatter));
            return this;
        }

        /**
         * Set a {@link DateTimeFormatter} for the {@link ZonedDateTime} string value converter
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getStringValueConverterFactory()
         */
        public Builder setZonedDateTimeFormatter(DateTimeFormatter formatter) {
            checkNotBuilt();
            stringValueConverterFactory.setStringValueConverter(ZonedDateTime.class,
                    new ZonedDateTimeValueConverter(formatter));
            return this;
        }

        /**
         * Set a custom string value converter for the provided class.
         * Converters for the following classes are provided out-of-the-box but can be overridden : {@link Date},
         * {@link LocalDateTime}, {@link LocalDate}, {@link LocalTime}, {@link ZonedDateTime}
         * @return this builder
         * @see LarJsonTypedWriteConfiguration#getStringValueConverterFactory()
         */
        public <T> Builder setStringValueConverter(Class<T> clazz, StringValueConverter<T> stringValueConverter) {
            checkNotBuilt();
            stringValueConverterFactory.setStringValueConverter(clazz, stringValueConverter);
            return this;
        }

        private class Configuration extends LarJsonWriteConfiguration.Builder.Configuration
                implements LarJsonTypedWriteConfiguration {

            @Override
            public final StringValueConverterFactory getStringValueConverterFactory() {
                return stringValueConverterFactory;
            }

            @Override
            public PropertyResolverFactory getPropertyResolverFactory() {
                return propertyResolverFactory;
            }

            @Override
            public PropertyConfigurationFactory getPropertyConfigurationFactory() {
                return propertyConfigurationFactory;
            }

            @Override
            public AnnotationConfigurationFactory getAnnotationConfigurationFactory() {
                return annotationConfigurationFactory;
            }
        }

        public LarJsonTypedWriteConfiguration build() {
            flagBuilt();
            return new Configuration();
        }
    }
}
