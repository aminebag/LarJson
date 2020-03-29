package com.aminebag.larjson.configuration;

import com.aminebag.larjson.channel.RandomReadAccessChannel;

import java.io.IOException;

/**
 * @author Amine Bagdouri
 *
 * A thread-safe factory of {@link RandomReadAccessChannel} instances that are customized using a
 * {@link LarJsonTypedReadConfiguration}
 */
public interface RandomReadAccessChannelFactory {

    /**
     * Return a new instance of {@link RandomReadAccessChannel} customized using the provided
     * {@link LarJsonTypedReadConfiguration}
     * @param readConfiguration used to customize the returned channel
     * @return a new instance of {@link RandomReadAccessChannel}
     * @throws IOException if a resource access error occurs
     */
    RandomReadAccessChannel get(LarJsonTypedReadConfiguration readConfiguration) throws IOException;
}
