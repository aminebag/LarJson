package com.aminebag.larjson.configuration;

import com.aminebag.larjson.configuration.propertyresolver.*;
import com.aminebag.larjson.mapper.LarJsonTypedMapper;
import com.aminebag.larjson.valueconverter.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * This interface represents a configuration for a {@link LarJsonTypedMapper} used to customize its behavior.
 * Implementations must be immutable and thread-safe.
 * It is highly recommended to use a {@link LarJsonTypedReadConfiguration.Builder} to build an instance of this
 * interface.
 */
public interface LarJsonTypedReadConfiguration extends LarJsonReadConfiguration {

    /**
     * Indicates whether unsupported methods are allowed in model interfaces.
     * @return {@code true} if unsupported methods are allowed, {@code false} otherwise
     * @see UnsupportedMethodCalledBehavior
     */
    boolean isUnsupportedMethodAllowed();

    /**
     * Indicates whether unknown JSON attributes are allowed. An unknown JSON attribute is an attribute that doesn't
     * have a corresponding getter method in its model interface.
     * @return {@code true} if unknown JSON attributes are allowed, {@code false} otherwise
     */
    boolean isUnknownJsonAttributeAllowed();

    /**
     * Indicates whether the model objects returned by the mapper should be validated using standard Java validation
     * constraints.
     * @return {@code true} if validation is enabled, {@code false} otherwise
     */
    boolean isValidationEnabled();

    /**
     * Indicates whether all properties in a JSON object are required. A required property must be present in the
     * JSON object but can be {@code null}.
     * @return {@code true} if all properties are required, {@code false} otherwise
     */
    boolean getAllPropertiesRequired();

    /**
     * @return a factory of {@link PropertyResolver} used to map JSON attributes to getter and setter methods
     */
    PropertyResolverFactory getPropertyResolverFactory();

    /**
     * Indicates how to behave when an unsupported method is called.
     * @see #isUnsupportedMethodAllowed()
     */
    UnsupportedMethodCalledBehavior getUnsupportedMethodCalledBehavior();

    /**
     * @return a factory of {@link StringValueConverter}s used to convert JSON strings to custom types.
     */
    StringValueConverterFactory getStringValueConverterFactory();

    /**
     * @return a factory of {@link EqualsDelegate}s used as a substitute to the default implementations of
     * {@link #equals(Object)} and {@link #hashCode()} methods of model objects
     */
    EqualsDelegateFactory getEqualsDelegateFactory();

    /**
     * @return a factory of {@link PropertyConfiguration}s used to customize model properties
     */
    PropertyConfigurationFactory getPropertyConfigurationFactory();

    /**
     * @return a factory of {@link AnnotationConfiguration}s used to customize the use of annotations
     */
    AnnotationConfigurationFactory getAnnotationConfigurationFactory();

    /**
     * @return a write configuration reflecting the same configuration properties as this read configuration
     */
    LarJsonTypedWriteConfiguration toWriteConfiguration();

    class Builder extends LarJsonReadConfiguration.Builder<Builder> {

        private static final LarJsonTypedWriteConfiguration DEFAULT_WRITE_CONFIGURATION =
                new LarJsonTypedWriteConfiguration.Builder().build();
        private boolean unsupportedMethodAllowed = false;
        private boolean unknownJsonAttributeAllowed = true;
        private boolean allPropertiesRequired = false;
        private boolean validationEnabled = false;
        private PropertyResolverFactory propertyResolverFactory = CamelCasePropertyResolver::new;
        private UnsupportedMethodCalledBehavior unsupportedMethodCalledBehavior =
                UnsupportedMethodCalledBehavior.THROW_EXCEPTION;
        private final DefaultStringValueConverterFactory stringValueConverterFactory =
                new DefaultStringValueConverterFactory();
        private final DefaultEqualsDelegateFactory equalsDelegateFactory = new DefaultEqualsDelegateFactory();
        private final DefaultPropertyConfigurationFactory propertyConfigurationFactory =
                new DefaultPropertyConfigurationFactory();
        private final DefaultAnnotationConfigurationFactory annotationConfigurationFactory =
                new DefaultAnnotationConfigurationFactory();

        /**
         * Set whether unsupported methods are allowed.
         * The default value is {@code false}
         * @return this builder
         * @see LarJsonTypedReadConfiguration#isUnsupportedMethodAllowed()
         */
        public Builder setUnsupportedMethodAllowed(boolean unsupportedMethodAllowed) {
            checkNotBuilt();
            this.unsupportedMethodAllowed = unsupportedMethodAllowed;
            return this;
        }

        /**
         * Set whether unknown JSON attributes are allowed.
         * The default value is {@code true}
         * @return this builder
         * @see LarJsonTypedReadConfiguration#isUnknownJsonAttributeAllowed()
         */
        public Builder setUnknownJsonAttributeAllowed(boolean unknownJsonAttributeAllowed) {
            checkNotBuilt();
            this.unknownJsonAttributeAllowed = unknownJsonAttributeAllowed;
            return this;
        }

        /**
         * Enable validation.
         * Validation is disabled by default.
         * @return this builder
         * @see LarJsonTypedReadConfiguration#isValidationEnabled()
         */
        public Builder enableValidation() {
            checkNotBuilt();
            this.validationEnabled = true;
            return this;
        }

        /**
         * Disable validation.
         * Validation is disabled by default.
         * @return this builder
         * @see LarJsonTypedReadConfiguration#isValidationEnabled()
         */
        public Builder disableValidation() {
            checkNotBuilt();
            this.validationEnabled = false;
            return this;
        }

        /**
         * Set a property resolver factory.
         * <p>
         *      The default property resolver is based on the camel case style of writing.
         * </p>
         * @return this builder
         * @see LarJsonTypedReadConfiguration#getPropertyResolverFactory()
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
         * @see LarJsonTypedReadConfiguration#getPropertyResolverFactory()
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
         * @see LarJsonTypedReadConfiguration#getPropertyResolverFactory()
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
         * @see LarJsonTypedReadConfiguration#getPropertyResolverFactory()
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
         * @see LarJsonTypedReadConfiguration#getPropertyResolverFactory()
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
         * @see LarJsonTypedReadConfiguration#getPropertyResolverFactory()
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
         * @see LarJsonTypedReadConfiguration#getPropertyResolverFactory()
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
         * @see LarJsonTypedReadConfiguration#getPropertyResolverFactory()
         */
        public Builder setUpperDotCase() {
            checkNotBuilt();
            this.propertyResolverFactory = UpperDotCasePropertyResolver::new;
            return this;
        }

        /**
         * Set the JSON attribute name for the property identified by the getter method
         * @return this builder
         * @see LarJsonTypedReadConfiguration#getPropertyConfigurationFactory()
         */
        public Builder setPropertyName(Method getterMethod, String name) {
            checkNotBuilt();
            this.propertyConfigurationFactory.setPropertyName(getterMethod, name);
            return this;
        }

        /**
         * Set whether the property identified by the getter method must be ignored
         * @return this builder
         * @see LarJsonTypedReadConfiguration#getPropertyConfigurationFactory()
         */
        public Builder setPropertyIgnored(Method getterMethod, boolean ignored) {
            checkNotBuilt();
            this.propertyConfigurationFactory.setPropertyIgnored(getterMethod, ignored);
            return this;
        }

        /**
         * Set whether the property identified by the getter method is required
         * @return this builder
         * @see LarJsonTypedReadConfiguration#getPropertyConfigurationFactory()
         */
        public Builder setPropertyRequired(Method getterMethod, boolean required) {
            checkNotBuilt();
            this.propertyConfigurationFactory.setPropertyRequired(getterMethod, required);
            return this;
        }

        /**
         * Set the string value converter for the property identified by the getter method
         * @return this builder
         * @see LarJsonTypedReadConfiguration#getPropertyConfigurationFactory()
         */
        public Builder setPropertyStringValueConverter(Method getterMethod, StringValueConverter<?>
                stringValueConverter) {
            checkNotBuilt();
            this.propertyConfigurationFactory.setPropertyStringValueConverter(getterMethod, stringValueConverter);
            return this;
        }

        /**
         * Enables the {@link JsonIgnoreProperties} annotation support.
         * By default, this annotation support is disabled.
         * @return this builder
         * @see LarJsonTypedReadConfiguration#getAnnotationConfigurationFactory() ()
         */
        public Builder enableJsonIgnorePropertiesAnnotation() {
            checkNotBuilt();
            this.annotationConfigurationFactory.setAnnotationConfiguration(JsonIgnoreProperties.class, true);
            return this;
        }

        /**
         * Disables the {@link JsonIgnoreProperties} annotation support
         * By default, this annotation support is disabled.
         * @return this builder
         * @see LarJsonTypedReadConfiguration#getAnnotationConfigurationFactory() ()
         */
        public Builder disableJsonIgnorePropertiesAnnotation() {
            checkNotBuilt();
            this.annotationConfigurationFactory.setAnnotationConfiguration(JsonIgnoreProperties.class, false);
            return this;
        }

        /**
         * Enables the {@link JsonFormat} annotation support.
         * By default, this annotation support is disabled.
         * @return this builder
         * @see LarJsonTypedReadConfiguration#getAnnotationConfigurationFactory() ()
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
         * @see LarJsonTypedReadConfiguration#getAnnotationConfigurationFactory() ()
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
         * @see LarJsonTypedReadConfiguration#getAnnotationConfigurationFactory() ()
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
         * @see LarJsonTypedReadConfiguration#getAnnotationConfigurationFactory() ()
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
         * @see LarJsonTypedReadConfiguration#getAnnotationConfigurationFactory() ()
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
         * @see LarJsonTypedReadConfiguration#getAnnotationConfigurationFactory() ()
         */
        public Builder disableJsonIgnoreAnnotation() {
            checkNotBuilt();
            this.annotationConfigurationFactory.setAnnotationConfiguration(JsonIgnore.class, false);
            return this;
        }

        /**
         * Set whether all model properties are required
         * The default value is {@code false}
         * @return this builder
         * @see LarJsonTypedReadConfiguration#getAllPropertiesRequired()
         */
        public Builder setAllPropertiesRequired(boolean allPropertiesRequired) {
            checkNotBuilt();
            this.allPropertiesRequired = allPropertiesRequired;
            return this;
        }

        /**
         * Set the date format for the {@link Date} string value converter
         * @return this builder
         * @see LarJsonTypedReadConfiguration#getStringValueConverterFactory()
         */
        public Builder setDateFormat(String dateFormat) {
            checkNotBuilt();
            DefaultStringValueConverterFactory defaultStringConverterFactory = stringValueConverterFactory;
            defaultStringConverterFactory.setStringValueConverter(Date.class,
                    new DateValueConverter(dateFormat, null, null));
            return this;
        }

        /**
         * Set the date format for the {@link LocalDateTime} string value converter
         * @return this builder
         * @see LarJsonTypedReadConfiguration#getStringValueConverterFactory()
         */
        public Builder setLocalDateTimeFormat(String dateFormat) {
            checkNotBuilt();
            DefaultStringValueConverterFactory defaultStringConverterFactory = stringValueConverterFactory;
            defaultStringConverterFactory.setStringValueConverter(LocalDateTime.class,
                    new LocalDateTimeValueConverter(dateFormat, null, null));
            return this;
        }

        /**
         * Set the date format for the {@link LocalDate} string value converter
         * @return this builder
         * @see LarJsonTypedReadConfiguration#getStringValueConverterFactory()
         */
        public Builder setLocalDateFormat(String dateFormat) {
            checkNotBuilt();
            DefaultStringValueConverterFactory defaultStringConverterFactory = stringValueConverterFactory;
            defaultStringConverterFactory.setStringValueConverter(LocalDate.class,
                    new LocalDateValueConverter(dateFormat, null, null));
            return this;
        }

        /**
         * Set the date format for the {@link LocalTime} string value converter
         * @return this builder
         * @see LarJsonTypedReadConfiguration#getStringValueConverterFactory()
         */
        public Builder setLocalTimeFormat(String dateFormat) {
            checkNotBuilt();
            DefaultStringValueConverterFactory defaultStringConverterFactory = stringValueConverterFactory;
            defaultStringConverterFactory.setStringValueConverter(LocalTime.class,
                    new LocalTimeValueConverter(dateFormat, null, null));
            return this;
        }

        /**
         * Set the date format for the {@link ZonedDateTime} string value converter
         * @return this builder
         * @see LarJsonTypedReadConfiguration#getStringValueConverterFactory()
         */
        public Builder setZonedDateTimeFormat(String dateFormat) {
            checkNotBuilt();
            DefaultStringValueConverterFactory defaultStringConverterFactory = stringValueConverterFactory;
            defaultStringConverterFactory.setStringValueConverter(ZonedDateTime.class,
                    new ZonedDateTimeValueConverter(dateFormat, null, null));
            return this;
        }

        /**
         * Set a {@link DateTimeFormatter} for the {@link LocalDateTime} string value converter
         * @return this builder
         * @see LarJsonTypedReadConfiguration#getStringValueConverterFactory()
         */
        public Builder setLocalDateTimeFormatter(DateTimeFormatter formatter) {
            checkNotBuilt();
            DefaultStringValueConverterFactory defaultStringConverterFactory = stringValueConverterFactory;
            defaultStringConverterFactory.setStringValueConverter(LocalDateTime.class,
                    new LocalDateTimeValueConverter(formatter));
            return this;
        }

        /**
         * Set a {@link DateTimeFormatter} for the {@link LocalDate} string value converter
         * @return this builder
         * @see LarJsonTypedReadConfiguration#getStringValueConverterFactory()
         */
        public Builder setLocalDateFormatter(DateTimeFormatter formatter) {
            checkNotBuilt();
            DefaultStringValueConverterFactory defaultStringConverterFactory = stringValueConverterFactory;
            defaultStringConverterFactory.setStringValueConverter(LocalDate.class,
                    new LocalDateValueConverter(formatter));
            return this;
        }

        /**
         * Set a {@link DateTimeFormatter} for the {@link LocalTime} string value converter
         * @return this builder
         * @see LarJsonTypedReadConfiguration#getStringValueConverterFactory()
         */
        public Builder setLocalTimeFormatter(DateTimeFormatter formatter) {
            checkNotBuilt();
            DefaultStringValueConverterFactory defaultStringConverterFactory = stringValueConverterFactory;
            defaultStringConverterFactory.setStringValueConverter(LocalTime.class,
                    new LocalTimeValueConverter(formatter));
            return this;
        }

        /**
         * Set a {@link DateTimeFormatter} for the {@link ZonedDateTime} string value converter
         * @return this builder
         * @see LarJsonTypedReadConfiguration#getStringValueConverterFactory()
         */
        public Builder setZonedDateTimeFormatter(DateTimeFormatter formatter) {
            checkNotBuilt();
            DefaultStringValueConverterFactory defaultStringConverterFactory = stringValueConverterFactory;
            defaultStringConverterFactory.setStringValueConverter(ZonedDateTime.class,
                    new ZonedDateTimeValueConverter(formatter));
            return this;
        }

        /**
         * Throw an {@link UnsupportedOperationException} when an unsupported method is called.
         * This is the default behavior.
         * @return this builder
         * @see LarJsonTypedReadConfiguration#getUnsupportedMethodCalledBehavior()
         */
        public Builder throwExceptionOnUnsupportedMethodCalled() {
            checkUnsupportedMethodAllowed();
            this.unsupportedMethodCalledBehavior = UnsupportedMethodCalledBehavior.THROW_EXCEPTION;
            return this;
        }

        /**
         * Return {@code null} when an unsupported method is called.
         * The default behavior is throwing an {@link UnsupportedOperationException}.
         * @return this builder
         * @see LarJsonTypedReadConfiguration#getUnsupportedMethodCalledBehavior()
         */
        public Builder returnNullOnUnsupportedMethodCalled() {
            checkUnsupportedMethodAllowed();
            this.unsupportedMethodCalledBehavior = UnsupportedMethodCalledBehavior.RETURN_NULL;
            return this;
        }

        /**
         * Set a custom behavior when an unsupported method is called.
         * The default behavior is throwing an {@link UnsupportedOperationException}.
         * @return this builder
         * @see LarJsonTypedReadConfiguration#getUnsupportedMethodCalledBehavior()
         */
        public Builder setUnsupportedMethodCalledBehavior(UnsupportedMethodCalledBehavior behavior) {
            checkUnsupportedMethodAllowed();
            this.unsupportedMethodCalledBehavior = behavior;
            return this;
        }

        /**
         * Set a custom string value converter for the provided class.
         * Converters for the following classes are provided out-of-the-box but can be overridden : {@link Date},
         * {@link LocalDateTime}, {@link LocalDate}, {@link LocalTime}, {@link ZonedDateTime}
         * @return this builder
         * @see LarJsonTypedReadConfiguration#getStringValueConverterFactory()
         */
        public <T> Builder setStringValueConverter(Class<T> clazz, StringValueConverter<T> stringValueConverter) {
            checkNotBuilt();
            DefaultStringValueConverterFactory defaultStringConverterFactory = stringValueConverterFactory;
            defaultStringConverterFactory.setStringValueConverter(clazz, stringValueConverter);
            return this;
        }

        /**
         * Set an equals delegate for the provided class
         * @return this builder
         * @see LarJsonTypedReadConfiguration#getEqualsDelegateFactory()
         */
        public <T> Builder setEqualsDelegate(Class<T> clazz, EqualsDelegate<T> equalsDelegate) {
            checkNotBuilt();
            DefaultEqualsDelegateFactory defaultEqualsDelegateFactory = equalsDelegateFactory;
            defaultEqualsDelegateFactory.setEqualsDelegate(clazz, equalsDelegate);
            return this;
        }

        private void checkUnsupportedMethodAllowed() {
            if(!unsupportedMethodAllowed) {
                throw new IllegalStateException("You need to set unsupportedMethodAllowed to true before changing " +
                        "the unsupportedMethodCalledBehavior");
            }
        }

        private class Configuration extends LarJsonReadConfiguration.Builder.Configuration
                implements LarJsonTypedReadConfiguration {

            LarJsonTypedWriteConfiguration writeConfiguration = new LarJsonTypedWriteConfiguration() {
                @Override
                public boolean isLenient() {
                    return Configuration.this.isLenient();
                }

                @Override
                public boolean isHtmlSafe() {
                    return DEFAULT_WRITE_CONFIGURATION.isHtmlSafe();
                }

                @Override
                public String getIndent() {
                    return DEFAULT_WRITE_CONFIGURATION.getIndent();
                }

                @Override
                public StringValueConverterFactory getStringValueConverterFactory() {
                    return Configuration.this.getStringValueConverterFactory();
                }

                @Override
                public PropertyResolverFactory getPropertyResolverFactory() {
                    return Configuration.this.getPropertyResolverFactory();
                }

                @Override
                public PropertyConfigurationFactory getPropertyConfigurationFactory() {
                    return Configuration.this.getPropertyConfigurationFactory();
                }

                @Override
                public AnnotationConfigurationFactory getAnnotationConfigurationFactory() {
                    return Configuration.this.getAnnotationConfigurationFactory();
                }
            };

            @Override
            public boolean isUnsupportedMethodAllowed() {
                return unsupportedMethodAllowed;
            }

            @Override
            public boolean isUnknownJsonAttributeAllowed() {
                return unknownJsonAttributeAllowed;
            }

            @Override
            public boolean isValidationEnabled() {
                return validationEnabled;
            }

            @Override
            public boolean getAllPropertiesRequired() {
                return allPropertiesRequired;
            }

            @Override
            public PropertyResolverFactory getPropertyResolverFactory() {
                return propertyResolverFactory;
            }

            @Override
            public UnsupportedMethodCalledBehavior getUnsupportedMethodCalledBehavior() {
                return unsupportedMethodCalledBehavior;
            }

            @Override
            public StringValueConverterFactory getStringValueConverterFactory() {
                return stringValueConverterFactory;
            }

            @Override
            public LarJsonTypedWriteConfiguration toWriteConfiguration() {
                return writeConfiguration;
            }

            @Override
            public PropertyConfigurationFactory getPropertyConfigurationFactory() {
                return propertyConfigurationFactory;
            }

            @Override
            public AnnotationConfigurationFactory getAnnotationConfigurationFactory() {
                return annotationConfigurationFactory;
            }

            @Override
            public EqualsDelegateFactory getEqualsDelegateFactory() {
                return equalsDelegateFactory;
            }
        }

        public LarJsonTypedReadConfiguration build() {
            flagBuilt();
            return new Configuration();
        }
    }
}
