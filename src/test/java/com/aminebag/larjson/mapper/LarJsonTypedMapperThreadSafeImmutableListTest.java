package com.aminebag.larjson.mapper;

import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.configuration.LarJsonTypedReadConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Amine Bagdouri
 */
public class LarJsonTypedMapperThreadSafeImmutableListTest extends LarJsonTypedMapperImmutableListTest {

    @Test
    void testThreadSafeListGet(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListGet());
    }

    @Test
    void testThreadSafeListSize(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListSize());
    }

    @Test
    void testThreadSafeListIsEmpty(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListIsNotEmpty());
    }

    @Test
    void testThreadSafeEmptyListIsEmpty(@TempDir Path tempDir) throws IOException, LarJsonException {
        testEmptyList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListIsEmpty());
    }

    @Test
    void testThreadSafeListContains(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListContains());
    }

    @Test
    void testThreadSafeListIteratorNext(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListIteratorNext());
    }

    @Test
    void testThreadSafeListIteratorForEachReamaining(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListIteratorForEachReamaining());
    }

    @Test
    void testThreadSafeListToArray(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListToArray());
    }

    @Test
    void testThreadSafeListToArrayWithSmallArrayArg(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListToArrayWithSmallArrayArg());
    }

    @Test
    void testThreadSafeListToArrayWithBigArrayArg(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListToArrayWithBigArrayArg());
    }

    @Test
    void testThreadSafeListContainsAll(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListContainsAll());
    }

    @Test
    void testThreadSafeListEquals(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListEquals());
    }

    @Test
    void testThreadSafeListHashCode(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListHashCode());
    }

    @Test
    void testThreadSafeListIndexOf(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListIndexOf());
    }

    @Test
    void testThreadSafeListLastIndexOf(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListLastIndexOf());
    }

    @Test
    void testThreadSafeListListIteratorNext(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListListIteratorNext());
    }

    @Test
    void testThreadSafeListForEachRemaining(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListForEachRemaining());
    }

    @Test
    void testThreadSafeListListIteratorPrevious(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListListIteratorPrevious());
    }

    @Test
    void testThreadSafeListListIteratorNextIndex(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListListIteratorNextIndex());
    }

    @Test
    void testThreadSafeListListIteratorPreviousIndex(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListListIteratorPreviousIndex());
    }

    @Test
    void testThreadSafeListListIteratorWithIndexNext(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListListIteratorWithIndexNext());
    }

    @Test
    void testThreadSafeListListIteratorWithIndexPrevious(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListListIteratorWithIndexPrevious());
    }

    @Test
    void testThreadSafeListListIteratorWithIndexNextIndex(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListListIteratorWithIndexNextIndex());
    }

    @Test
    void testThreadSafeListListIteratorWithIndexPreviousIndex(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListListIteratorWithIndexPreviousIndex());
    }

    @Test
    void testThreadSafeListListIteratorWithIndexForEachRemaining(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListListIteratorWithIndexForEachRemaining());
    }

    @Test
    void testThreadSafeListSubList(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListSubList());
    }

    @Test
    void testThreadSafeListSpliterator(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListSpliterator());
    }

    @Test
    void testThreadSafeListStream(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListStream());
    }

    @Test
    void testThreadSafeListToString(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListToString());
    }

    @Test
    void testThreadSafeListForEach(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListForEach());
    }

    @Override
    protected LarJsonTypedReadConfiguration configuration() {
        return new LarJsonTypedReadConfiguration.Builder().setThreadSafe(true).build();
    }
}
