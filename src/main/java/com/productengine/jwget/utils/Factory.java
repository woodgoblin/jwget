package com.productengine.jwget.utils;

import org.jetbrains.annotations.NotNull;

public interface Factory<T> {

    @NotNull T create() throws Exception;

}
