package com.aminebag.larjson.mapper;

import com.aminebag.larjson.api.LarJsonList;
import com.aminebag.larjson.api.LarJsonPath;
import com.aminebag.larjson.api.LarJsonRootTypedElement;
import com.aminebag.larjson.api.LarJsonTypedElement;
import com.aminebag.larjson.configuration.LarJsonTypedWriteConfiguration;
import com.aminebag.larjson.exception.LarJsonConversionException;
import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.valueconverter.StringValueConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.stream.JsonWriter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.NUMBER;
import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

/**
 * @author Amine Bagdouri
 */
public class LarJsonTypedMapperTestModels {

    public interface ModelWithPrimitiveBoolean extends Closeable {
        boolean isWhatever();
    }

    public interface ModelWithPrimitiveBooleanGetGetter extends Closeable {
        boolean getWhatever();
    }

    public interface ModelWithBooleanWrapper extends Closeable {
        Boolean isWhatever();
    }

    public interface ModelWithBooleanList extends Closeable {
        List<Boolean> getWhatever();
    }

    public interface ModelWithPrimitiveInt extends Closeable {
        int getWhatever();
    }

    public interface ModelWithIntegerWrapper extends Closeable {
        Integer getWhatever();
    }

    public interface ModelWithIntegerList extends Closeable {
        List<Integer> getWhatever();
    }

    public interface ModelWithPrimitiveLong extends Closeable {
        long getWhatever();
    }

    public interface ModelWithLongWrapper extends Closeable {
        Long getWhatever();
    }

    public interface ModelWithLongList extends Closeable {
        List<Long> getWhatever();
    }

    public interface ModelWithPrimitiveByte extends Closeable {
        byte getWhatever();
    }

    public interface ModelWithByteWrapper extends Closeable {
        Byte getWhatever();
    }

    public interface ModelWithByteList extends Closeable {
        List<Byte> getWhatever();
    }

    public interface ModelWithPrimitiveShort extends Closeable {
        short getWhatever();
    }

    public interface ModelWithShortWrapper extends Closeable {
        Short getWhatever();
    }

    public interface ModelWithShortList extends Closeable {
        List<Short> getWhatever();
    }

    public interface ModelWithPrimitiveChar extends Closeable {
        char getWhatever();
    }

    public interface ModelWithCharacterWrapper extends Closeable {
        Character getWhatever();
    }

    public interface ModelWithCharacterList extends Closeable {
        List<Character> getWhatever();
    }

    public interface ModelWithPrimitiveFloat extends Closeable {
        float getWhatever();
    }

    public interface ModelWithFloatList extends Closeable {
        List<Float> getWhatever();
    }

    public interface ModelWithFloatWrapper extends Closeable {
        Float getWhatever();
    }

    public interface ModelWithPrimitiveDouble extends Closeable {
        double getWhatever();
    }

    public interface ModelWithDoubleWrapper extends Closeable {
        Double getWhatever();
    }

    public interface ModelWithDoubleList extends Closeable {
        List<Double> getWhatever();
    }

    public interface ModelWithBigInteger extends Closeable {
        BigInteger getWhatever();
    }

    public interface ModelWithBigIntegerList extends Closeable {
        List<BigInteger> getWhatever();
    }

    public interface ModelWithBigDecimal extends Closeable {
        BigDecimal getWhatever();
    }

    public interface ModelWithBigDecimalList extends Closeable {
        List<BigDecimal> getWhatever();
    }

    public interface ModelWithNumber extends Closeable {
        Number getWhatever();
    }

    public interface ModelWithNumberList extends Closeable {
        List<Number> getWhatever();
    }

    public interface ModelWithString extends LarJsonRootTypedElement {
        String getWhatever();
    }

    public interface ModelWithValidation extends LarJsonRootTypedElement {
        @NotNull
        @NotBlank
        String getWhatever();
    }

    public interface ModelWithMultipleProperties extends LarJsonRootTypedElement {
        String getMyFirstLife();
        String getMySecondLife();
        String getMYThirdLife();
        boolean isMyFourthLife();
        void setMyFirstLife(String value);
        void setMySecondLife(String value);
        void setMyFourthLife(boolean value);
        String hello();
    }

    public static class ModelWithStringImpl implements ModelWithString {

        @Override
        public String getWhatever() {
            return null;
        }

        @Override
        public void getLarJsonPath(StringBuilder sb) {

        }

        @Override
        public LarJsonPath getParentLarJsonPath() {
            return null;
        }

        @Override
        public void write(JsonWriter jsonWriter, LarJsonTypedWriteConfiguration writeConfiguration) throws IOException, LarJsonException {

        }

        @Override
        public void write(JsonWriter jsonWriter) throws IOException, LarJsonException {

        }

        @Override
        public void write(Writer writer) throws IOException, LarJsonException {

        }

        @Override
        public void close() throws IOException {

        }

        @Override
        public Object clone() {
            return null;
        }
    }

    public interface UncloseableModelWithString {
        String getWhatever();
    }

    public interface ModelWithStringMutable extends LarJsonTypedElement, Closeable {
        String getWhatever();
        void setWhatever(String v);
    }

    public interface ModelWithObjectMutable extends LarJsonRootTypedElement {
        ModelWithStringMutable getSomething();
        void setSomething(ModelWithStringMutable v);
    }

    public interface ModelWithInvalidGetter extends Closeable {
        String getwhatever();
    }

    public interface ModelWithUnsupportedMethod extends Closeable {
        void hello();
    }

    public interface ModelWithStringSetterOnly extends Closeable {
        void setWhatever(String v);
    }

    public interface ModelWithStringSetterTypeDifferentThanGetterType extends Closeable {
        String getWhatever();
        void setWhatever(int v);
    }

    public interface ModelWithStringManySettersForOneGetter extends Closeable {
        String getWhatever();
        void setWhatever(int v);
        void setWhatever(String v);
        void setWhatever(float v);
    }

    public interface ModelWithStringList extends Closeable {
        List<String> getWhatever();
    }

    public interface ModelWithStringLarJsonList extends Closeable {
        LarJsonList<String> getWhatever();
    }

    public interface ModelWithStringCollection extends Closeable {
        Collection<String> getWhatever();
    }

    public interface ModelWithStringIterable extends Closeable {
        Iterable<String> getWhatever();
    }

    public interface ModelWithCharSequence extends Closeable {
        CharSequence getWhatever();
    }

    public interface ModelWithCharSequenceList extends Closeable {
        List<CharSequence> getWhatever();
    }

    public enum TestEnum {
        HELLO, SALUT, HOLA
    }

    public interface ModelWithEnum extends Closeable {
        TestEnum getWhatever();
    }

    public interface ModelWithEnumList extends Closeable {
        List<TestEnum> getWhatever();
    }

    static class TestEnumConverter implements StringValueConverter<TestEnum> {
        @Override
        public TestEnum fromString(String s) throws LarJsonConversionException {
            if(s == null) {
                return TestEnum.SALUT;
            } else if (s.equals("EN")) {
                return TestEnum.HELLO;
            } else if (s.trim().isEmpty()) {
                return TestEnum.HOLA;
            } else {
                throw new LarJsonConversionException();
            }
        }

        @Override
        public String toString(TestEnum value) {
            if(value == TestEnum.SALUT) {
                return null;
            } else if (value == TestEnum.HELLO) {
                return "EN";
            } else {
                return "";
            }
        }
    }

    public interface ModelWithDate extends Closeable {
        Date getWhatever();
    }

    public interface ModelWithDateList extends Closeable {
        List<Date> getWhatever();
    }

    public interface ModelWithLocalDateTime extends Closeable {
        LocalDateTime getWhatever();
    }

    public interface ModelWithLocalDateTimeList extends Closeable {
        List<LocalDateTime> getWhatever();
    }

    public interface ModelWithLocalDate extends Closeable {
        LocalDate getWhatever();
    }

    public interface ModelWithLocalDateList extends Closeable {
        List<LocalDate> getWhatever();
    }

    public interface ModelWithLocalTime extends Closeable {
        LocalTime getWhatever();
    }

    public interface ModelWithLocalTimeList extends Closeable {
        List<LocalTime> getWhatever();
    }

    public interface ModelWithZonedDateTime extends Closeable {
        ZonedDateTime getWhatever();
    }

    public interface ModelWithZonedDateTimeList extends Closeable {
        List<ZonedDateTime> getWhatever();
    }

    public interface ModelWithNestedLists extends Closeable {
        List<Collection<Iterable<LarJsonList<List<String>>>>> getWhatever();
    }

    public interface ModelWithObjectList extends Closeable {
        LarJsonList<ModelWithString> getSomething();
    }

    public interface ModelWithTwoObjectLists extends Closeable {
        LarJsonList<ModelWithString> getSomething();
        LarJsonList<ModelWithString> getAnother();
    }

    public interface ModelWithObject extends LarJsonRootTypedElement {
        ModelWithString getSomething();
    }

    public interface ModelWithObjectCloneable extends LarJsonRootTypedElement {
        ModelWithString getSomething();
        ModelWithObjectCloneable clone();
    }

    public static class ModelWithStringMutableImpl implements ModelWithStringMutable {
        @Override
        public String getWhatever() {
            return null;
        }

        @Override
        public void setWhatever(String v) {

        }

        @Override
        public void getLarJsonPath(StringBuilder sb) {

        }

        @Override
        public LarJsonPath getParentLarJsonPath() {
            return null;
        }

        @Override
        public void write(JsonWriter jsonWriter, LarJsonTypedWriteConfiguration writeConfiguration) throws IOException, LarJsonException {
            jsonWriter.beginObject();
            jsonWriter.name("whatever");
            jsonWriter.nullValue();
            jsonWriter.endObject();
        }

        @Override
        public void write(JsonWriter jsonWriter) throws IOException, LarJsonException {
            write(jsonWriter, new LarJsonTypedWriteConfiguration.Builder().build());
        }

        @Override
        public void write(Writer writer) throws IOException, LarJsonException {

        }

        @Override
        public void close() throws IOException {

        }

        @Override
        public ModelWithStringMutableImpl clone() {
            return new ModelWithStringMutableImpl();
        }

        @Override
        public boolean equals(Object obj) {
            return obj !=null && obj instanceof ModelWithStringMutable && ((ModelWithStringMutable)obj).getWhatever() == null;
        }

        @Override
        public int hashCode() {
            return 987987104;
        }


    }

    public static class ModelWithObjectMutableImpl implements ModelWithObjectMutable {

        @Override
        public ModelWithStringMutable getSomething() {
            return null;
        }

        @Override
        public void setSomething(ModelWithStringMutable v) {

        }

        @Override
        public void getLarJsonPath(StringBuilder sb) {

        }

        @Override
        public LarJsonPath getParentLarJsonPath() {
            return null;
        }

        @Override
        public void write(JsonWriter jsonWriter, LarJsonTypedWriteConfiguration writeConfiguration) throws IOException, LarJsonException {

        }

        @Override
        public void write(JsonWriter jsonWriter) throws IOException, LarJsonException {

        }

        @Override
        public void write(Writer writer) throws IOException, LarJsonException {

        }

        @Override
        public void close() throws IOException {

        }

        @Override
        public Object clone() {
            return null;
        }
    }

    public interface RichModel extends ModelWithString, Closeable, LarJsonTypedElement {
        int getAge();
        Boolean getHappy();
        boolean isMad();
        String getFirstName();
        String getLastName();
        String getMiddleName();
        ModelWithString getAddress();
        LarJsonList<TestEnum> getWords();
        Date getBirthDate();
        Collection<ModelWithDoubleWrapper> getScores();
    }

    public interface ModelWithUntypedList extends Closeable {
        List getWhatever();
    }

    public static class ClassTypeModel {

    }

    public interface ModelWithClassTypeProperty extends Closeable {
        ClassTypeModel getWhatever();
    }

    public interface ModelWithListOfClassTypeProperty extends Closeable {
        List<ClassTypeModel> getWhatever();
    }

    public interface ModelWithStringAndInt extends ModelWithString {
        int getSomething();
    }

    public interface ModelWithJsonFormatAnnotatedDateDefaultShape extends Closeable {
        @JsonFormat(pattern = "yyyy.MMM.dd G 'at' HH:mm:ss z")
        Date getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalDateTimeDefaultShape extends Closeable {
        @JsonFormat(pattern = "yyyy.MMM.dd G 'at' HH:mm:ss")
        LocalDateTime getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalDateDefaultShape extends Closeable {
        @JsonFormat(pattern = "yyyy.MMM.dd G")
        LocalDate getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalTimeDefaultShape extends Closeable {
        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedZonedDateTimeDefaultShape extends Closeable {
        @JsonFormat(pattern = "yyyy.MMM.dd G 'at' HH:mm:ss Z")
        ZonedDateTime getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedEnumDefaultShape extends Closeable {
        @JsonFormat
        TestEnum getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedString extends Closeable {
        @JsonFormat(shape = STRING, pattern = "whatever")
        String getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedDateShapeString extends Closeable {
        @JsonFormat(shape = STRING)
        Date getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalDateTimeShapeString extends Closeable {
        @JsonFormat(shape = STRING)
        LocalDateTime getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalDateShapeString extends Closeable {
        @JsonFormat(shape = STRING)
        LocalDate getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalTimeShapeString extends Closeable {
        @JsonFormat(shape = STRING)
        LocalTime getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedZonedDateTimeShapeString extends Closeable {
        @JsonFormat(shape = STRING)
        ZonedDateTime getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedDateWithPattern extends Closeable {
        @JsonFormat(shape = STRING, pattern = "yyyy.MMM.dd G 'at' HH:mm:ss z")
        Date getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalDateTimeWithPattern extends Closeable {
        @JsonFormat(shape = STRING, pattern = "yyyy.MMM.dd G 'at' HH:mm:ss")
        LocalDateTime getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalDateWithPattern extends Closeable {
        @JsonFormat(shape = STRING, pattern = "yyyy.MMM.dd G")
        LocalDate getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalTimeWithPattern extends Closeable {
        @JsonFormat(shape = STRING, pattern = "HH_mm_ss_SSS")
        LocalTime getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedZonedDateTimeWithPattern extends Closeable {
        @JsonFormat(shape = STRING, pattern = "yyyy.MMM.dd G 'at' HH:mm:ss Z")
        ZonedDateTime getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedDateList extends Closeable {
        @JsonFormat(shape = STRING, pattern = "yyyy.MMM.dd G 'at' HH:mm:ss z")
        List<Date> getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalDateTimeList extends Closeable {
        @JsonFormat(shape = STRING, pattern = "yyyy.MMM.dd G 'at' HH:mm:ss")
        List<LocalDateTime> getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalDateList extends Closeable {
        @JsonFormat(shape = STRING, pattern = "yyyy.MMM.dd G")
        List<LocalDate> getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalTimeList extends Closeable {
        @JsonFormat(shape = STRING, pattern = "HH_mm_ss_SSS")
        List<LocalTime> getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedZonedDateTimeList extends Closeable {
        @JsonFormat(shape = STRING, pattern = "yyyy.MMM.dd G 'at' HH:mm:ss Z")
        List<ZonedDateTime> getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedDateWithInvalidPattern extends Closeable {
        @JsonFormat(shape = STRING, pattern = "whatever")
        Date getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalDateTimeWithInvalidPattern extends Closeable {
        @JsonFormat(shape = STRING, pattern = "whatever")
        LocalDateTime getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalDateWithInvalidPattern extends Closeable {
        @JsonFormat(shape = STRING, pattern = "whatever")
        LocalDate getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalTimeWithInvalidPattern extends Closeable {
        @JsonFormat(shape = STRING, pattern = "whatever")
        LocalTime getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedZonedDateTimeWithInvalidPattern extends Closeable {
        @JsonFormat(shape = STRING, pattern = "whatever")
        ZonedDateTime getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedDateWithLocale extends Closeable {
        @JsonFormat(shape = STRING, pattern = "yyyy.MMM.dd G 'at' HH:mm:ss z", locale = "ja-JP")
        Date getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalDateTimeWithLocale extends Closeable {
        @JsonFormat(shape = STRING, pattern = "yyyy.MMM.dd G 'at' HH:mm:ss", locale = "ja-JP")
        LocalDateTime getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalDateWithLocale extends Closeable {
        @JsonFormat(shape = STRING, pattern = "yyyy.MMM.dd G", locale = "ja-JP")
        LocalDate getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalTimeWithLocale extends Closeable {
        @JsonFormat(shape = STRING, pattern = "HH_mm_ss_SSS", locale = "ja-JP")
        LocalTime getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedZonedDateTimeWithLocale extends Closeable {
        @JsonFormat(shape = STRING, pattern = "yyyy.MMM.dd G 'at' HH:mm:ss Z", locale = "ja-JP")
        ZonedDateTime getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedDateWithTimeZone extends Closeable {
        @JsonFormat(shape = STRING, pattern = "yyyy.MMM.dd G 'at' HH:mm:ss z", timezone = "Brazil/East")
        Date getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalDateTimeWithTimeZone extends Closeable {
        @JsonFormat(shape = STRING, pattern = "yyyy.MMM.dd G 'at' HH:mm:ss", timezone = "Brazil/East")
        LocalDateTime getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalDateWithTimeZone extends Closeable {
        @JsonFormat(shape = STRING, pattern = "yyyy.MMM.dd G", timezone = "Brazil/East")
        LocalDate getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalTimeWithTimeZone extends Closeable {
        @JsonFormat(shape = STRING, pattern = "HH_mm_ss_SSS", timezone = "Brazil/East")
        LocalTime getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedZonedDateTimeWithTimeZone extends Closeable {
        @JsonFormat(shape = STRING, pattern = "yyyy.MMM.dd G 'at' HH:mm:ss Z", timezone = "Brazil/East")
        ZonedDateTime getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedDateWithInvalidTimeZone extends Closeable {
        @JsonFormat(shape = STRING, pattern = "yyyy.MMM.dd G 'at' HH:mm:ss z", timezone = "whatever")
        Date getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalDateTimeWithInvalidTimeZone extends Closeable {
        @JsonFormat(shape = STRING, pattern = "yyyy.MMM.dd G 'at' HH:mm:ss", timezone = "whatever")
        LocalDateTime getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalDateWithInvalidTimeZone extends Closeable {
        @JsonFormat(shape = STRING, pattern = "yyyy.MMM.dd G", timezone = "whatever")
        LocalDate getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalTimeWithInvalidTimeZone extends Closeable {
        @JsonFormat(shape = STRING, pattern = "HH_mm_ss_SSS", timezone = "whatever")
        LocalTime getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedZonedDateTimeWithInvalidTimeZone extends Closeable {
        @JsonFormat(shape = STRING, pattern = "yyyy.MMM.dd G 'at' HH:mm:ss Z", timezone = "whatever")
        ZonedDateTime getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedDateShapeNumber extends Closeable {
        @JsonFormat(shape = NUMBER)
        Date getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalDateTimeShapeNumber extends Closeable {
        @JsonFormat(shape = NUMBER)
        LocalDateTime getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalDateShapeNumber extends Closeable {
        @JsonFormat(shape = NUMBER)
        LocalDate getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedLocalTimeShapeNumber extends Closeable {
        @JsonFormat(shape = NUMBER)
        LocalTime getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedZonedDateTimeShapeNumber extends Closeable {
        @JsonFormat(shape = NUMBER)
        ZonedDateTime getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedEnumShapeString extends Closeable {
        @JsonFormat(shape = STRING)
        TestEnum getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedEnumShapeNumber extends Closeable {
        @JsonFormat(shape = NUMBER)
        TestEnum getWhatever();
    }

    public interface ModelWithJsonFormatAnnotatedEnumList extends Closeable {
        @JsonFormat(shape = NUMBER)
        List<TestEnum> getWhatever();
    }

}
