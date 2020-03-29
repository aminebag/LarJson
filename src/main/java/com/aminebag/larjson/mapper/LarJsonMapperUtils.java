package com.aminebag.larjson.mapper;

import com.aminebag.larjson.api.LarJsonList;
import com.aminebag.larjson.api.LarJsonTypedElement;
import com.aminebag.larjson.api.LarJsonTypedWriteable;
import com.aminebag.larjson.api.LarJsonWriteable;
import com.aminebag.larjson.configuration.*;
import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.exception.LarJsonValueReadException;
import com.aminebag.larjson.utils.LarJsonProxy;
import com.aminebag.larjson.valueconverter.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.*;
import java.util.*;

/**
 * @author Amine Bagdouri
 *
 * Utility methods used for mapping between the Java objects and the JSON resource
 */
public class LarJsonMapperUtils {
    private static final List<Class<?>> DEFAULT_INTERFACES =
            Arrays.asList(LarJsonTypedElement.class, LarJsonProxy.class);

    /**
     * @throws IllegalArgumentException
     */
    public static StringValueConverter getJsonFormatValueConverter(Class<?> returnType, JsonFormat jsonFormat) {
        switch (jsonFormat.shape()) {
            case STRING: {
                String pattern = jsonFormat.pattern();
                if(pattern == null || pattern.trim().isEmpty()) {
                    return null;
                }

                ZoneId zoneId = null;
                if (jsonFormat.timezone() != null && !jsonFormat.timezone().trim().isEmpty() &&
                        !jsonFormat.timezone().equals(JsonFormat.DEFAULT_TIMEZONE)) {
                    try {
                        zoneId = ZoneId.of(jsonFormat.timezone());
                    }catch (DateTimeException e) {
                        throw new IllegalArgumentException("timezone : " + jsonFormat.timezone() ,e);
                    }
                }

                Locale locale = null;
                if (jsonFormat.locale() != null && !jsonFormat.locale().trim().isEmpty() &&
                        !jsonFormat.locale().equals(JsonFormat.DEFAULT_LOCALE)) {
                    locale = Locale.forLanguageTag(jsonFormat.locale());
                }

                if (returnType.equals(Date.class)) {
                    return new DateValueConverter(pattern, locale, zoneId);
                } else if (returnType.equals(LocalDateTime.class)) {
                    return new LocalDateTimeValueConverter(pattern, locale, zoneId);
                } else if (returnType.equals(LocalDate.class)) {
                    return new LocalDateValueConverter(pattern, locale, zoneId);
                } else if (returnType.equals(LocalTime.class)) {
                    return new LocalTimeValueConverter(pattern, locale, zoneId);
                } else if (returnType.equals(ZonedDateTime.class)) {
                    return new ZonedDateTimeValueConverter(pattern, locale, zoneId);
                }
                return null;
            }
            case NUMBER: {
                if (returnType.equals(Date.class)) {
                    return new EpochDateValueConverter();
                } else if (returnType.equals(LocalDateTime.class)) {
                    return new EpochLocalDateTimeValueConverter();
                } else if (returnType.equals(LocalDate.class)) {
                    return new EpochLocalDateValueConverter();
                }
                return null;
            }
            default: {
                return null;
            }
        }
    }

    public static Object nullValue(Method method){
        Class<?> returnType = method.getReturnType();
        if(returnType == boolean.class) return false;
        else if(returnType == int.class) return 0;
        else if(returnType == double.class) return 0d;
        else if(returnType == float.class) return 0f;
        else if(returnType == long.class) return 0L;
        else if(returnType == char.class) return (char)0;
        else if(returnType == short.class) return (short)0;
        else if(returnType == byte.class) return (byte)0;
        else return null;
    }

    public static JsonWriter getJsonWriter(Writer writer, LarJsonWriteConfiguration larJsonWriteConfiguration) {
        JsonWriter jsonWriter = new JsonWriter(writer);
        jsonWriter.setIndent(larJsonWriteConfiguration.getIndent());
        jsonWriter.setHtmlSafe(larJsonWriteConfiguration.isHtmlSafe());
        jsonWriter.setLenient(larJsonWriteConfiguration.isLenient());
        return jsonWriter;
    }

    public static String toString(LarJsonWriteable writeable) {
        StringWriter writer = new StringWriter();
        try {
            writeable.write(writer);
        } catch (IOException | LarJsonException e) {
            throw new LarJsonValueReadException(e);
        }
        return writer.toString();
    }

    public static void writeValue(JsonWriter jsonWriter, Object o, LarJsonTypedWriteConfiguration configuration)
            throws IOException, LarJsonException {
        if(o == null) {
            jsonWriter.nullValue();
        } else if(o instanceof LarJsonTypedWriteable) {
            LarJsonTypedWriteable writeable = (LarJsonTypedWriteable) o;
            writeable.write(jsonWriter, configuration);
        } else if(o instanceof LarJsonWriteable) {
            LarJsonWriteable writeable = (LarJsonWriteable) o;
            writeable.write(jsonWriter);
        } else if(o instanceof Number) {
            jsonWriter.value((Number)o);
        } else if(o instanceof Boolean) {
            jsonWriter.value((Boolean)o);
        } else {
            jsonWriter.value(o.toString());
        }
    }

    public static Class<?>[] getProxiedInterfaces(Class<?>... customInterfaces) {
        Class[] classes = new Class[customInterfaces.length + DEFAULT_INTERFACES.size()];
        int i=0;
        for(Class<?> interfacee : customInterfaces) {
            classes[i++] = interfacee;
        }
        for(Class<?> interfacee : DEFAULT_INTERFACES) {
            classes[i++] = interfacee;
        }
        return classes;
    }

    public static <T extends Annotation> T getClassAnnotation(
            Class<?> targetClass, Class<T> annotation, AnnotationConfigurationFactory annotationConfigurationFactory) {
        AnnotationConfiguration annotationConfiguration = annotationConfigurationFactory
                .get(annotation);
        if(annotationConfiguration == null || !annotationConfiguration.isEnabled()) {
            return null;
        }
        return targetClass.getAnnotation(annotation);
    }

    public static <T extends Annotation> T getMethodAnnotation(
            Method getterMethod, Class<T> annotation, AnnotationConfigurationFactory annotationConfigurationFactory) {
        AnnotationConfiguration annotationConfiguration = annotationConfigurationFactory
                .get(annotation);
        if(annotationConfiguration == null || !annotationConfiguration.isEnabled()) {
            return null;
        }
        return getterMethod.getAnnotation(annotation);
    }

    public static String getPropertyName(
            Method getterMethod, PropertyResolver propertyResolver,
            PropertyConfigurationFactory propertyConfigurationFactory,
            AnnotationConfigurationFactory annotationConfigurationFactory) {
        PropertyConfiguration propertyConfiguration = propertyConfigurationFactory
                .get(getterMethod);
        if(propertyConfiguration != null) {
            String name = propertyConfiguration.getName();
            if(name != null) {
                return name;
            }
        }
        JsonProperty jsonProperty = getMethodAnnotation(getterMethod, JsonProperty.class, annotationConfigurationFactory);
        if(jsonProperty != null) {
            if(jsonProperty.value() != null && !jsonProperty.value().trim().isEmpty()) {
                return jsonProperty.value();
            }
        }
        return propertyResolver.getAttributeName(getterMethod);
    }

    public static boolean isPropertyRequired(
            Method getterMethod, boolean allPropertiesRequired,
            PropertyConfigurationFactory propertyConfigurationFactory,
            AnnotationConfigurationFactory annotationConfigurationFactory) {
        PropertyConfiguration propertyConfiguration = propertyConfigurationFactory
                .get(getterMethod);
        if(propertyConfiguration != null) {
            Boolean required = propertyConfiguration.isRequired();
            if(required != null) {
                return required;
            }
        }
        JsonProperty jsonProperty = getMethodAnnotation(getterMethod, JsonProperty.class, annotationConfigurationFactory);
        if(jsonProperty != null) {
            return jsonProperty.required();
        }
        return allPropertiesRequired;
    }

    public static boolean isPropertyIgnored(
            Method getterMethod, PropertyConfigurationFactory propertyConfigurationFactory,
            PropertyResolver propertyResolver, AnnotationConfigurationFactory annotationConfigurationFactory) {
        PropertyConfiguration propertyConfiguration = propertyConfigurationFactory.get(getterMethod);
        if(propertyConfiguration != null) {
            Boolean ignored = propertyConfiguration.isIgnored();
            if(ignored != null) {
                return ignored;
            }
        }
        JsonIgnore jsonIgnore = getMethodAnnotation(getterMethod, JsonIgnore.class, annotationConfigurationFactory);
        if(jsonIgnore != null) {
            return jsonIgnore.value();
        }
        JsonIgnoreProperties jsonIgnoreProperties = LarJsonMapperUtils.getClassAnnotation(getterMethod.getDeclaringClass(),
                JsonIgnoreProperties.class, annotationConfigurationFactory);
        if(jsonIgnoreProperties != null) {
            String[] ignoredProperties = jsonIgnoreProperties.value();
            if(ignoredProperties != null && ignoredProperties.length > 0) {
                String propertyName = getPropertyName(getterMethod, propertyResolver, propertyConfigurationFactory,
                        annotationConfigurationFactory);
                for(String ignoredProperty : ignoredProperties) {
                    if(propertyName.equals(ignoredProperty)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isUnknownAttributeAllowed(Class<?> clazz, LarJsonTypedReadConfiguration configuration) {
        JsonIgnoreProperties jsonIgnoreProperties = LarJsonMapperUtils.getClassAnnotation(
                clazz, JsonIgnoreProperties.class, configuration.getAnnotationConfigurationFactory());
        if(jsonIgnoreProperties != null) {
            return jsonIgnoreProperties.ignoreUnknown();
        }
        return configuration.isUnknownJsonAttributeAllowed();
    }

    public static StringValueConverter getPropertyStringValueConverter(Method getterMethod, PropertyConfigurationFactory propertyConfigurationFactory) {
        PropertyConfiguration propertyConfiguration = propertyConfigurationFactory.get(getterMethod);
        return propertyConfiguration == null ? null :
                propertyConfiguration.getStringValueConverter();
    }

    public static boolean isCollectionType(Class<?> returnType) {
        return returnType.equals(LarJsonList.class) || returnType.equals(List.class)
                || returnType.equals(Collection.class) || returnType.equals(Iterable.class);
    }
}
