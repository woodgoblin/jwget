package com.myzone.jwget.io;

import com.myzone.jwget.utils.Factory;
import org.jetbrains.annotations.NotNull;

import java.io.RandomAccessFile;

public class FileOutputConnectorFactory implements Factory<FileOutputConnector> {

    protected final RandomAccessFile file;

    public FileOutputConnectorFactory(@NotNull RandomAccessFile file) {
        this.file = file;
    }

    @NotNull
    @Override
    public FileOutputConnector create() {
        return new FileOutputConnector(file);
    }

}
