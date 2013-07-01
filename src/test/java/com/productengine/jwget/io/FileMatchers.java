package com.productengine.jwget.io;

import org.apache.commons.io.IOUtils;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.io.*;

public class FileMatchers {

    public static <T extends File> Matcher<? super T> contentEqualsTo(T expected) {
        return new ContentEqualsToMatcher<>(expected);
    }

    protected static class ContentEqualsToMatcher<T extends File> extends TypeSafeDiagnosingMatcher<T> {

        protected final T expected;

        public ContentEqualsToMatcher(T expected) {
            this.expected = expected;
        }

        @Override
        protected boolean matchesSafely(T actual, Description description) {
            try (
                    FileReader actualStream = new FileReader(actual);
                    FileReader expectedStream = new FileReader(expected);
            ) {
                boolean equals = IOUtils.contentEquals(actualStream, expectedStream);

                if (!equals) {
                    description
                            .appendText("content of ")
                            .appendValue(actual)
                            .appendText(" is not equal to content of ")
                            .appendValue(expected);
                }

                return equals;
            } catch (FileNotFoundException e) {
                description.appendText("file has not found");

                return false;
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("content should be equal to content of ")
                    .appendValue(expected);
        }

    }

}
