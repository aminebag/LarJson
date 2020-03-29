package com.aminebag.larjson.mapper;

import com.aminebag.larjson.api.*;
import com.aminebag.larjson.blueprint.LarJsonBlueprintWriter;
import com.aminebag.larjson.channel.FileRandomReadAccessChannel;
import com.aminebag.larjson.channel.RandomReadAccessChannel;
import com.aminebag.larjson.chardecoder.CharacterDecoder;
import com.aminebag.larjson.configuration.*;
import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.mapper.element.LarJsonRootListImpl;
import com.aminebag.larjson.mapper.element.LarJsonRootObject;
import com.aminebag.larjson.mapper.exception.LarJsonConstraintViolationException;
import com.aminebag.larjson.mapper.exception.LarJsonMappingDefinitionException;
import com.aminebag.larjson.mapper.propertymapper.*;
import com.aminebag.larjson.parser.LarJsonParseException;
import com.aminebag.larjson.parser.LarJsonToken;
import com.aminebag.larjson.parser.LarJsonTokenParser;
import com.aminebag.larjson.parser.LarJsonValueParser;
import com.aminebag.larjson.resource.SafeResourceCloser;
import com.aminebag.larjson.stream.ChannelByteStream;
import com.aminebag.larjson.stream.ChannelCharacterStreamPool;
import com.aminebag.larjson.utils.LarJsonMethods;
import com.aminebag.larjson.valueconverter.StringValueConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.core.ResolvableType;

import javax.validation.*;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * @author Amine Bagdouri
 *
 * <p>LarJsonTypedMapper provides functionality for reading JSON content into objects of a provided root model type.</p>
 * <p>In order to minimize memory usage and the number of allocated objects, the model objects provided by this
 * mapper do not store the entire JSON content of the JSON resource (e.g. JSON file) in memory. Instead, these model
 * objects reference the JSON resource, throughout their entire lifetimes, and only store minimal information
 * (blueprints) needed to quickly access JSON data. For this reason, the root model object implements the
 * {@link Closeable} interface and it must alawys be closed, using {@link Closeable#close()}, {@link #close(Object)} or
 * {@link #close(List)} methods, when it's no longer needed, to free up its associated resources.</p>
 * <p>The root model type must be an interface with getter methods representing the different JSON attributes.
 * The return types of these getters can be:
 *  <ul>
 *      <li>simple types (primitives, primitive wrappers, {@link String}, {@link CharSequence}, enums,
 *      {@link BigInteger}, {@link BigDecimal} or {@link Number}),</li>
 *      <li>types that can be instantiated using a converter based on a String representation, some configurable
 *      converters are provided out-of-the-box (for: {@link Date}, {@link LocalDateTime}, {@link LocalDate},
 *      {@link LocalTime} and {@link ZonedDateTime}), but custom converters can be defined,</li>
 *      <li>model interfaces containing getter methods,</li>
 *      <li>collection types ({@link Iterable}, {@link Collection}, {@link List} or {@link LarJsonList}) of any of
 *      the types mentioned in this list.</li>
 *  </ul>
 * </p>
 * <p>The model interfaces may contain setter methods if the configuration allows mutability.</p>
 * <p>The model interfaces may extend all or any of the following interfaces to extend their functionalities :
 * {@link Closeable}, {@link LarJsonCloneable}, {@link LarJsonWriteable}, {@link LarJsonTypedWriteable},
 * {@link LarJsonPath}, {@link LarJsonElement}, {@link LarJsonTypedElement} and {@link LarJsonRootTypedElement}.
 * These functionalities can also be exploited, without the need to extend model interfaces, using
 * {@link LarJsonPerspectives} static methods or {@link #close(Object)} {@link #close(List)} methods. Close methods
 * must only be called on root model objects</p>
 * <p>Depending on configuration, model interface unsupported methods can either be rejected or ignored during mapper
 * construction. The behavior of an ignored method can be configured as well.</p>
 * <p>When a model getter method is called multiple times, it might not always return the same instance. However, by
 * default, the returned instances are equal and return the same hash code.</p>
 * <p>A configurable cache is associated to each root model object in order to minimize JSON resource access for
 * frequently used data.</p>
 * <p>Depending on JSON resource size and on configuration, the blueprint of the resource might be stored in a
 * configurable temporary file, that is removed as soon as the root model object is closed, in order to limit memory
 * usage of the blueprint of huge JSON resources.</p>
 * <p>The thread-safety of model objects returned by this mapper is configurable. By default, these model objects
 * are not thread-safe. Thread-safe mutable model objects have a significant memory and CPU overhead.</p>
 * <p>The default implementation of {@link #equals(Object)} and {@link #hashCode()} methods of returned model
 * objects is based solely on the values returned by their getters and on the type of the model (i.e. two model
 * objects are considered equal if, and only if, they both implement the same model type and return equal values for
 * each of the supported getter methods). A custom implementation of {@link #equals(Object)} and {@link #hashCode()}
 * methods can be provided via the configuration.</p>
 * <p>Some Jackson annotations are supported, to some degree, by the mapper. They are disabled by default.
 * The currently supported ones are:
 *  <ul>
 *      <li>{@link JsonFormat}: can be set on getters with return types: enum (shape NUMBER or STRING),
 *      {@link Date} (shape NUMBER or STRING), {@link LocalDateTime} (shape NUMBER or STRING),
 *      {@link LocalDate} (shape NUMBER or STRING), {@link LocalTime} (shape STRING),
 *      {@link ZonedDateTime} (shape STRING) or a collection of any type from this list.</li>
 *      <li>{@link JsonIgnore}: can be set on any getter corresponding to a JSON attribute that must be ignored.</li>
 *      <li>{@link JsonProperty}: can be set on any getter to customize JSON attribute name or to indicate that a
 *      property is required (i.e. it must be present as a JSON attribute in the JSON object but can be {@code null}).
 *      </li>
 *      <li>{@link JsonIgnoreProperties}: can be set on any model interface to indicate if unknown attributes of
 *      that interface should be ignored and/or indicate properties that should be ignored (when reading or writing)
 *      </li>
 *  </ul>
 * </p>
 * <p>Annotation based validation is disabled by default. It can be enabled via mapper configuration
 * (a validation implementation dependency must be provided in runtime).</p>
 *
 * @implSpec
 * This class is immutable and thread-safe.
 */
public class LarJsonTypedMapper<T> {

    private static final LarJsonTypedReadConfiguration DEFAULT_CONFIG =
            new LarJsonTypedReadConfiguration.Builder().build();

    private final Class<T> rootInterface;
    private final LarJsonTypedReadConfiguration configuration;
    private final ObjectLarJsonPropertyMapper<T> rootObjectMapper;
    private final LarJsonValueParser valueParser;
    private final PropertyResolver propertyResolver;

    /**
     * Constructs a mapper for the provided root interface using the default configuration
     * @param rootInterface the type of the object that will be returned as a result of JSON deserialization, must be
     *                     an interface
     * @throws LarJsonMappingDefinitionException if the mapping with the provided root interface is not possible
     */
    public LarJsonTypedMapper(Class<T> rootInterface) {
        this(rootInterface, DEFAULT_CONFIG);
    }

    /**
     * Constructs a mapper for the provided root interface using a custom configuration
     * @param rootInterface the type of the object that will be returned as a result of JSON deserialization, must be
     *                     an interface
     * @throws LarJsonMappingDefinitionException if the mapping with the provided root interface is not possible
     */
    public LarJsonTypedMapper(Class<T> rootInterface, LarJsonTypedReadConfiguration configuration) {
        this.configuration = configuration;
        if (!rootInterface.isInterface()) {
            throw new LarJsonMappingDefinitionException(rootInterface + " is not an interface");
        }
        this.valueParser = configuration.getValueParserFactory().get(configuration);
        this.propertyResolver = configuration.getPropertyResolverFactory().get(rootInterface);
        this.rootInterface = rootInterface;
        this.rootObjectMapper = getObjectLarJsonPropertyMapper(null, null, null,
                0, 0, false, rootInterface);
    }

    private LarJsonPropertyMapper<?> getPropertyMapper(
            String name, Method getterMethod, Method setterMethod, int getterIndex, int setterIndex, boolean required,
            Class<?> returnType, ResolvableType resolvableType, int[] genericIndexes) {

        if (LarJsonMapperUtils.isCollectionType(returnType)) {
            LarJsonPropertyMapper<?> subMapper = getCollectionSubPropertyMapper(
                    getterMethod, resolvableType, genericIndexes);
            if (subMapper == null) return null;
            return new ListLarJsonPropertyMapper(name, getterMethod, setterMethod, getterIndex, setterIndex,
                    required, subMapper);
        }

        LarJsonPropertyMapper<?> mapper = getCustomizedPropertyMapper(name, getterMethod, setterMethod, getterIndex,
                setterIndex, required, returnType);
        if (mapper != null) return mapper;

        return getStandardPropertyMapper(name, getterMethod, setterMethod, getterIndex, setterIndex, required,
                returnType);
    }

    private LarJsonPropertyMapper<?> getCollectionSubPropertyMapper(
            Method getterMethod, ResolvableType resolvableType, int[] genericIndexes) {
        resolvableType = resolvableType != null ? resolvableType : ResolvableType.forMethodReturnType(getterMethod);
        genericIndexes = genericIndexes != null ?
                Arrays.copyOf(genericIndexes, genericIndexes.length + 1) : new int[1];
        genericIndexes[genericIndexes.length - 1] = 0;
        Class<?> parameterType = resolvableType.getGeneric(genericIndexes).resolve();
        if (parameterType == null) {
            throw new LarJsonMappingDefinitionException("Parameter type of " + getterMethod + " method return type " +
                    "could not be resolved");
        }
        LarJsonPropertyMapper<?> subMapper = getPropertyMapper(null, getterMethod, null,
                0, 0, false, parameterType, resolvableType, genericIndexes);
        if(subMapper == null) {
            return null;
        }
        return subMapper;
    }

    private LarJsonPropertyMapper<?> getStandardPropertyMapper(
            String name, Method getterMethod, Method setterMethod, int getterIndex, int setterIndex, boolean required,
            Class<?> returnType) {
        if (returnType.equals(String.class) || returnType.equals(CharSequence.class)) {
            return new StringPropertyMapper(name, getterMethod, setterMethod, getterIndex, setterIndex,
                    required, valueParser);
        } else if (returnType.equals(boolean.class)) {
            return new PrimitiveBooleanPropertyMapper(name, getterMethod, setterMethod, getterIndex, setterIndex,
                    required);
        } else if (returnType.equals(int.class)) {
            return new PrimitiveIntegerPropertyMapper(name, getterMethod, setterMethod, getterIndex, setterIndex,
                    required, valueParser);
        } else if (returnType.equals(long.class)) {
            return new PrimitiveLongPropertyMapper(name, getterMethod, setterMethod, getterIndex, setterIndex,
                    required, valueParser);
        } else if (returnType.equals(float.class)) {
            return new PrimitiveFloatPropertyMapper(name, getterMethod, setterMethod, getterIndex, setterIndex,
                    required, valueParser);
        } else if (returnType.equals(double.class)) {
            return new PrimitiveDoublePropertyMapper(name, getterMethod, setterMethod, getterIndex, setterIndex,
                    required, valueParser);
        } else if (returnType.equals(char.class)) {
            return new PrimitiveCharPropertyMapper(name, getterMethod, setterMethod, getterIndex, setterIndex,
                    required, valueParser);
        } else if (returnType.equals(byte.class)) {
            return new PrimitiveBytePropertyMapper(name, getterMethod, setterMethod, getterIndex, setterIndex,
                    required, valueParser);
        } else if (returnType.equals(short.class)) {
            return new PrimitiveShortPropertyMapper(name, getterMethod, setterMethod, getterIndex, setterIndex,
                    required, valueParser);
        } else if (returnType.equals(Boolean.class)) {
            return new BooleanPropertyMapper(name, getterMethod, setterMethod, getterIndex, setterIndex,
                    required);
        } else if (returnType.equals(Integer.class)) {
            return new IntegerPropertyMapper(name, getterMethod, setterMethod, getterIndex, setterIndex,
                    required, valueParser);
        } else if (returnType.equals(Long.class)) {
            return new LongPropertyMapper(name, getterMethod, setterMethod, getterIndex, setterIndex,
                    required, valueParser);
        } else if (returnType.equals(Float.class)) {
            return new FloatPropertyMapper(name, getterMethod, setterMethod, getterIndex, setterIndex,
                    required, valueParser);
        } else if (returnType.equals(Double.class)) {
            return new DoublePropertyMapper(name, getterMethod, setterMethod, getterIndex, setterIndex,
                    required, valueParser);
        } else if (returnType.equals(Character.class)) {
            return new CharPropertyMapper(name, getterMethod, setterMethod, getterIndex, setterIndex,
                    required, valueParser);
        } else if (returnType.equals(Byte.class)) {
            return new BytePropertyMapper(name, getterMethod, setterMethod, getterIndex, setterIndex,
                    required, valueParser);
        } else if (returnType.equals(Short.class)) {
            return new ShortPropertyMapper(name, getterMethod, setterMethod, getterIndex, setterIndex,
                    required, valueParser);
        } else if (returnType.equals(BigInteger.class)) {
            return new BigIntegerPropertyMapper(name, getterMethod, setterMethod, getterIndex, setterIndex,
                    required, valueParser);
        } else if (returnType.equals(BigDecimal.class)) {
            return new BigDecimalPropertyMapper(name, getterMethod, setterMethod, getterIndex, setterIndex,
                    required, valueParser);
        } else if (returnType.equals(Number.class)) {
            return new NumberPropertyMapper(name, getterMethod, setterMethod, getterIndex, setterIndex,
                    required, valueParser);
        } else if (returnType.isEnum()) {
            return new StringEnumPropertyMapper(name, getterMethod, setterMethod, getterIndex, setterIndex,
                    required, returnType);
        } else {
            return getObjectLarJsonPropertyMapper(name, getterMethod, setterMethod, getterIndex, setterIndex,
                    required, returnType);
        }
    }

    private LarJsonPropertyMapper<?> getCustomizedPropertyMapper(
            String name, Method getterMethod, Method setterMethod, int getterIndex, int setterIndex, boolean required,
            Class<?> returnType) {

        StringValueConverter<?> converter = LarJsonMapperUtils.getPropertyStringValueConverter(getterMethod,
                configuration.getPropertyConfigurationFactory());
        if(converter == null) {
            JsonFormat jsonFormat;
            if ((jsonFormat = LarJsonMapperUtils.getMethodAnnotation(
                    getterMethod, JsonFormat.class, configuration.getAnnotationConfigurationFactory())) != null) {
                if (returnType.isEnum()) {
                    LarJsonPropertyMapper<?> mapper = getEnumJsonFormatAnnotationPropertyMapper(name, getterMethod,
                            setterMethod, getterIndex,
                            setterIndex, required, returnType, jsonFormat);
                    if (mapper != null) return mapper;
                } else {
                    try {
                        converter = LarJsonMapperUtils.getJsonFormatValueConverter(returnType, jsonFormat);
                    } catch (IllegalArgumentException e) {
                        throw new LarJsonMappingDefinitionException("Illegal argument for the JsonFormat annotation " +
                                jsonFormat + " on " + getterMethod, e);
                    }
                }
            }
            if(converter == null) {
                converter = configuration.getStringValueConverterFactory().get(returnType);
            }
        }
        if(converter != null) {
            return new ConvertedStringPropertyMapper(name, getterMethod, setterMethod, getterIndex,
                    setterIndex, required, valueParser, converter, returnType);
        }
        return null;
    }

    private LarJsonPropertyMapper<?> getEnumJsonFormatAnnotationPropertyMapper(
            String name, Method getterMethod, Method setterMethod, int getterIndex, int setterIndex, boolean required,
            Class<?> returnType, JsonFormat jsonFormat) {
        switch (jsonFormat.shape()) {
            case STRING: return new StringEnumPropertyMapper(name, getterMethod, setterMethod, getterIndex,
                    setterIndex, required, returnType);
            case NUMBER: return new OrdinalEnumPropertyMapper(name, getterMethod, setterMethod, getterIndex,
                    setterIndex, required, returnType);
            default: return null;
        }
    }

    private ObjectLarJsonPropertyMapper getObjectLarJsonPropertyMapper(
            String name, Method getterMethod, Method setterMethod, int getterIndex, int setterIndex, boolean required,
            Class<?> clazz) {
        if (!clazz.isInterface()) {
            if (configuration.isUnsupportedMethodAllowed()) {
                return null;
            } else {
                throw new LarJsonMappingDefinitionException("The type " + clazz.getName() + " is not an interface");
            }
        }

        Map<Method, LarJsonPropertyMapper<?>> mapperByGetter = new HashMap<>();
        Map<Method, LarJsonPropertyMapper<?>> mapperBySetter =
                configuration.isMutable() ? new HashMap<>() : Collections.emptyMap();
        generatePropertyMappers(clazz, mapperByGetter, mapperBySetter);

        return new ObjectLarJsonPropertyMapper(name, getterMethod, setterMethod, getterIndex, setterIndex, required,
                clazz, mapperByGetter, mapperBySetter);
    }

    private void generatePropertyMappers(Class<?> clazz,
                                         Map<Method, LarJsonPropertyMapper<?>> mapperByGetter,
                                         Map<Method, LarJsonPropertyMapper<?>> mapperBySetter) {
        AnnotationConfigurationFactory annotationConfigurationFactory =
                configuration.getAnnotationConfigurationFactory();
        PropertyConfigurationFactory propertyConfigurationFactory =
                configuration.getPropertyConfigurationFactory();
        int getterIndex = 0;
        int setterIndex = 0;
        List<Method> getterCandidates = new ArrayList<>();
        Set<Method> setterCandidates = new HashSet<>();
        for(Method method : clazz.getMethods()) {
            if(LarJsonMethods.isSupportedMethod(method)) {
                continue;
            } else if (method.getReturnType().equals(void.class) && method.getParameterCount() == 1) {
                setterCandidates.add(method);
            } else if (!method.getReturnType().equals(void.class) && method.getParameterCount() == 0) {
                getterCandidates.add(method);
            } else {
                checkUnsupportedMethodIsAllowed(method);
            }
        }
        Collection<Method> immutableSetterCandidates = Collections.unmodifiableCollection(setterCandidates);
        for (Method getterMethod : getterCandidates) {
            if (propertyResolver.isGetter(getterMethod)) {
                Method setterMethod = propertyResolver.findSetter(getterMethod, immutableSetterCandidates);
                if (setterMethod != null) {
                    setterCandidates.remove(setterMethod);
                    if(!configuration.isMutable()) {
                        setterMethod = null;
                    }
                }

                if(LarJsonMapperUtils.isPropertyIgnored(getterMethod, propertyConfigurationFactory,
                        propertyResolver, annotationConfigurationFactory)) {
                    continue;
                }

                boolean required = LarJsonMapperUtils.isPropertyRequired(getterMethod, configuration.getAllPropertiesRequired(),
                        propertyConfigurationFactory, annotationConfigurationFactory);

                String name = LarJsonMapperUtils.getPropertyName(getterMethod, propertyResolver, propertyConfigurationFactory,
                        annotationConfigurationFactory);

                LarJsonPropertyMapper mapper = getPropertyMapper(name, getterMethod, setterMethod, getterIndex,
                        setterMethod == null ? -1 : setterIndex, required, getterMethod.getReturnType(),
                        null, null);
                if (mapper != null) {
                    mapperByGetter.put(getterMethod, mapper);
                    getterIndex++;
                    if (setterMethod != null) {
                        mapperBySetter.put(setterMethod, mapper);
                        setterIndex++;
                    }
                } else {
                    checkUnsupportedMethodIsAllowed(getterMethod);
                }
            } else {
                checkUnsupportedMethodIsAllowed(getterMethod);
            }
        }
        if(!setterCandidates.isEmpty()) {
            checkUnsupportedMethodIsAllowed(setterCandidates.iterator().next());
        }
    }

    private void checkUnsupportedMethodIsAllowed(Method method) {
        if (!configuration.isUnsupportedMethodAllowed()) {
            throw new LarJsonMappingDefinitionException("The signature of the method " + method +
                    " doesn't match any of the supported methods. " +
                    "Consider changing the method signature " +
                    "or changing the unsupported method allowed configuration.");
        }
    }

    /**
     * Deserialize JSON content from given file into an object of this mapper's root interface type.
     * The returned object implements {@link Closeable} interface and must be closed, using {@link Closeable#close()},
     * {@link #close(Object)} or {@link #close(List)} methods, when it's no longer needed, to free up its associated
     * resources.
     * @param jsonFile the file containing the JSON object to deserialize
     * @return a model object mapped to the JSON object
     * @throws IOException if a file access error is encountered
     * @throws LarJsonException if a JSON related error is encountered
     */
    public T readObject(File jsonFile) throws IOException, LarJsonException {
        return readObject((conf) -> new FileRandomReadAccessChannel(jsonFile));
    }

    /**
     * Deserialize JSON content from given channel factory into an object of this mapper's root interface type.
     * The returned object implements {@link Closeable} interface and must be closed, using {@link Closeable#close()},
     * {@link #close(Object)} or {@link #close(List)} methods, when it's no longer needed, to free up its associated
     * resources.
     * @param channelFactory the channel factory referencing the JSON resource to deserialize
     * @return a model object mapped to the JSON object
     * @throws IOException if a resource access error is encountered
     * @throws LarJsonException if a JSON related error is encountered
     */
    public T readObject(RandomReadAccessChannelFactory channelFactory) throws IOException, LarJsonException {
        CharacterDecoder characterDecoder = configuration.getCharacterDecoder();

        boolean successful = false;
        SafeResourceCloser onErrorCleaners = new SafeResourceCloser();
        T t;

        try {
            try(RandomReadAccessChannel channel = channelFactory.get(configuration)) {

                LarJsonTokenParser tokenParser = configuration.getTokenParserFactory()
                        .get(new ChannelByteStream(channel), characterDecoder, configuration);

                ChannelCharacterStreamPool characterStreamPool =
                        new ChannelCharacterStreamPool(channelFactory, characterDecoder, configuration);
                onErrorCleaners.add(characterStreamPool);

                try (LarJsonBlueprintWriter blueprintWriter = new LarJsonBlueprintWriter(
                        configuration.getMaxMemoryBlueprintSize(), configuration.getTemporaryFileFactory())) {
                    onErrorCleaners.add(blueprintWriter.getOnErrorCleaner());
                    LarJsonRootObject root = rootObjectMapper.buildRootObject(configuration, tokenParser,
                            blueprintWriter, characterStreamPool, propertyResolver, rootInterface);
                    checkEndOfDocument(tokenParser);
                    t = (T) Proxy.newProxyInstance(rootInterface.getClassLoader(),
                            LarJsonMapperUtils.getProxiedInterfaces(rootInterface, LarJsonRootTypedElement.class),
                            root);
                }
                if (configuration.isValidationEnabled()) {
                    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                    Validator validator = factory.getValidator();
                    Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
                    checkConstraintViolations(constraintViolations);
                }
            }
            successful = true;
            return t;
        } finally {
            if (!successful) {
                onErrorCleaners.close();
            }
        }
    }

    /**
     * Deserialize JSON content from given file into a list of this mapper's root interface type.
     * The returned list implements {@link Closeable} interface and must be closed, using {@link Closeable#close()},
     * {@link #close(Object)} or {@link #close(List)} methods, when it's no longer needed, to free up its associated
     * resources.
     * @param jsonFile the file containing the JSON object to deserialize
     * @return a list of model objects mapped to the JSON array
     * @throws IOException if a file access error is encountered
     * @throws LarJsonException if a JSON related error is encountered
     */
    public LarJsonRootList<T> readArray(File jsonFile) throws IOException, LarJsonException {

        return readArray((conf) -> new FileRandomReadAccessChannel(jsonFile));
    }

    /**
     * Deserialize JSON content from given channel factory into a list of this mapper's root interface type.
     * The returned list implements {@link Closeable} interface and must be closed, using {@link Closeable#close()},
     * {@link #close(Object)} or {@link #close(List)} methods, when it's no longer needed, to free up its associated
     * resources.
     * @param channelFactory the channel factory referencing the JSON resource to deserialize
     * @return a model object mapped to the JSON array
     * @throws IOException if a resource access error is encountered
     * @throws LarJsonException if a JSON related error is encountered
     */
    public LarJsonRootList<T> readArray(RandomReadAccessChannelFactory channelFactory)
            throws IOException, LarJsonException {
        CharacterDecoder characterDecoder = configuration.getCharacterDecoder();

        boolean successful = false;
        SafeResourceCloser onErrorCleaners = new SafeResourceCloser();
        LarJsonRootListImpl<T> root;

        try {
            try (RandomReadAccessChannel channel = channelFactory.get(configuration)) {

                LarJsonTokenParser tokenParser = configuration.getTokenParserFactory()
                        .get(new ChannelByteStream(channel), characterDecoder, configuration);

                ChannelCharacterStreamPool characterStreamPool =
                        new ChannelCharacterStreamPool(channelFactory, characterDecoder, configuration);
                onErrorCleaners.add(characterStreamPool);


                try (LarJsonBlueprintWriter blueprintWriter = new LarJsonBlueprintWriter(
                        configuration.getMaxMemoryBlueprintSize(), configuration.getTemporaryFileFactory())) {
                    onErrorCleaners.add(blueprintWriter.getOnErrorCleaner());
                    root = rootObjectMapper.buildRootList(configuration, tokenParser, blueprintWriter,
                            characterStreamPool, propertyResolver, rootInterface);
                    checkEndOfDocument(tokenParser);
                }
                if (configuration.isValidationEnabled()) {
                    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                    Validator validator = factory.getValidator();
                    Set<ConstraintViolation<T>> constraintViolations = new HashSet<>();
                    for (T t : root) {
                        constraintViolations.addAll(validator.validate(t));
                    }
                    checkConstraintViolations(constraintViolations);
                }
            }
            successful = true;
            return root;
        } finally {
            if (!successful) {
                onErrorCleaners.close();
            }
        }
    }

    private void checkConstraintViolations(Set<ConstraintViolation<T>> constraintViolations)
            throws LarJsonConstraintViolationException {
        try {
            if(constraintViolations.size() > 0) {
                throw new LarJsonConstraintViolationException(constraintViolations.toString());
            }
        } catch (ValidationException e) {
            throw new LarJsonConstraintViolationException("Validation failed", e);
        }
    }

    private void checkEndOfDocument(LarJsonTokenParser tokenParser) throws LarJsonParseException, IOException {
        LarJsonToken token = tokenParser.peek();
        if (token != LarJsonToken.END_DOCUMENT) {
            throw new LarJsonParseException(
                    String.format("Expected end of document, but found %s at byte position %d",
                            token.name(), tokenParser.getCurrentPosition()));
        }
    }

    /**
     * Provided that the {@code object} argument is an instance of {@link Closeable}, closes this stream and releases
     * any system resources associated with it. If the stream is already closed then invoking this method has no effect
     * @throws IOException if an I/O error occurs
     * @throws IllegalArgumentException if {@code object} is not an instance of {@link Closeable}
     * @see Closeable#close()
     */
    public void close(T object) throws IOException {
        if (object instanceof Closeable) {
            ((Closeable) object).close();
        } else {
            throw new IllegalArgumentException("Argument is not Closeable");
        }
    }

    /**
     * Provided that the {@code list} argument is an instance of {@link LarJsonRootList}, closes this stream and
     * releases any system resources associated with it.
     * If the stream is already closed then invoking this method has no effect
     * @throws IOException if an I/O error occurs
     * @throws IllegalArgumentException if {@code list} is not an instance of {@link LarJsonRootList}
     * @see Closeable#close()
     */
    public void close(List<T> list) throws IOException {
        if (list instanceof LarJsonRootList) {
            ((LarJsonRootList) list).close();
        } else {
            throw new IllegalArgumentException("Argument is not LarJsonRootList");
        }
    }

    @Override
    public String toString() {
        return "{" +
                "rootInterface=" + rootInterface +
                ", rootObjectMapper=" + rootObjectMapper +
                '}';
    }

    /**
     * @return the configuration associated to this mapper
     */
    public LarJsonTypedReadConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * @return the root interface associated to this mapper
     */
    public Class<T> getRootInterface() {
        return rootInterface;
    }
}