// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.wrapped.os;

import yapion.annotations.object.YAPIONData;
import yapion.annotations.object.YAPIONPostDeserialization;
import yapion.annotations.serialize.YAPIONOptimize;
import yapion.exceptions.YAPIONException;
import yapion.serializing.wrapped.WrappedImplementation;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@YAPIONData
@WrappedImplementation(since = "0.20.1")
public class WrappedProcessBuilder {

    @SuppressWarnings({"java:S2065"})
    private transient ProcessBuilder processBuilder;

    @YAPIONOptimize
    private List<String> command;

    public WrappedProcessBuilder(List<String> command) {
        processBuilder = new ProcessBuilder(command);
        this.command = command;
    }

    public WrappedProcessBuilder(String... command) {
        processBuilder = new ProcessBuilder(command);
        this.command = Arrays.asList(command);
    }

    @YAPIONPostDeserialization
    private void create() {
        if (processBuilder != null) {
            throw new YAPIONException("Cannot create ProcessBuilder if it already exists");
        }
        processBuilder = new ProcessBuilder(command);
    }

    public ProcessBuilder getProcessBuilder() {
        return processBuilder;
    }

    public ProcessBuilder command(List<String> command) {
        ProcessBuilder processBuilder = this.processBuilder.command(command);
        this.command = command;
        return processBuilder;
    }

    public ProcessBuilder command(String... command) {
        ProcessBuilder processBuilder = this.processBuilder.command(command);
        this.command = Arrays.asList(command);
        return processBuilder;
    }

    public List<String> command() {
        return processBuilder.command();
    }

    public Map<String, String> environment() {
        return processBuilder.environment();
    }

    public File directory() {
        return processBuilder.directory();
    }

    public ProcessBuilder directory(File directory) {
        return processBuilder.directory(directory);
    }

    public ProcessBuilder redirectInput(ProcessBuilder.Redirect source) {
        return processBuilder.redirectInput(source);
    }

    public ProcessBuilder redirectOutput(ProcessBuilder.Redirect destination) {
        return processBuilder.redirectOutput(destination);
    }

    public ProcessBuilder redirectError(ProcessBuilder.Redirect destination) {
        return processBuilder.redirectError(destination);
    }

    public ProcessBuilder redirectInput(File file) {
        return processBuilder.redirectInput(file);
    }

    public ProcessBuilder redirectOutput(File file) {
        return processBuilder.redirectOutput(file);
    }

    public ProcessBuilder redirectError(File file) {
        return processBuilder.redirectError(file);
    }

    public ProcessBuilder.Redirect redirectInput() {
        return processBuilder.redirectInput();
    }

    public ProcessBuilder.Redirect redirectOutput() {
        return processBuilder.redirectOutput();
    }

    public ProcessBuilder.Redirect redirectError() {
        return processBuilder.redirectError();
    }

    public ProcessBuilder inheritIO() {
        return processBuilder.inheritIO();
    }

    public boolean redirectErrorStream() {
        return processBuilder.redirectErrorStream();
    }

    public ProcessBuilder redirectErrorStream(boolean redirectErrorStream) {
        return processBuilder.redirectErrorStream(redirectErrorStream);
    }

    public Process start() throws IOException {
        return processBuilder.start();
    }

}