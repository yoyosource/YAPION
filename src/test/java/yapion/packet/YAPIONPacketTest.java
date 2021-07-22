/*
 * Copyright 2019,2020,2021 yoyosource
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yapion.packet;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.io.YAPIONSocket;
import yapion.parser.YAPIONParser;
import yapion.utils.IdentifierUtils;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static yapion.packet.YAPIONPacketTestObjects.*;
import static yapion.packet.YAPIONPacketTestObjects.TestType.*;

public class YAPIONPacketTest {

    private static ServerSocket serverSocket;

    @Rule
    public Timeout timeout = new Timeout(5, TimeUnit.SECONDS);

    @Before
    public void setUp() throws Exception {
        serverSocket = new ServerSocket(22222);
    }

    @After
    public void tearDown() throws Exception {
        serverSocket.close();
    }

    private static synchronized YAPIONPacketStream[] connection() throws Exception {
        Socket socket = new Socket("127.0.0.1", 22222);
        Socket sSocket = serverSocket.accept();
        return new YAPIONPacketStream[]{new YAPIONPacketStream(new YAPIONSocket(sSocket)), new YAPIONPacketStream(new YAPIONSocket(socket))};
    }

    private YAPIONPacketReceiver receiver(TestType testType, AtomicReference<TestType> worked) {
        YAPIONPacketReceiver yapionPacketReceiver = new YAPIONPacketReceiver();
        if (testType != DropHandler) {
            yapionPacketReceiver.setHandler(YAPIONPacketReceiver.Handler.DROP, yapionPacket -> {
                worked.set(DropHandler);
            });
        }
        if (testType != ErrorHandler) {
            yapionPacketReceiver.setHandler(YAPIONPacketReceiver.Handler.ERROR, yapionPacket -> {
                worked.set(ErrorHandler);
            });
        }
        if (testType != ExceptionHandler) {
            yapionPacketReceiver.setHandler(YAPIONPacketReceiver.Handler.EXCEPTION, yapionPacket -> {
                worked.set(ExceptionHandler);
            });
        }
        if (testType != HandleFailedHandler) {
            yapionPacketReceiver.setHandler(YAPIONPacketReceiver.Handler.HANDLE_FAILED, yapionPacket -> {
                worked.set(HandleFailedHandler);
            });
        }
        if (testType != UnknownHandler) {
            yapionPacketReceiver.setHandler(YAPIONPacketReceiver.Handler.UNKNOWN_PACKET, yapionPacket -> {
                worked.set(UnknownHandler);
            });
        }
        if (testType != DeserializationExceptionHandler) {
            yapionPacketReceiver.setHandler(YAPIONPacketReceiver.Handler.DESERIALIZE_EXCEPTION, yapionPacket -> {
                worked.set(DeserializationExceptionHandler);
            });
        }
        yapionPacketReceiver.add(TestPacket.class, yapionPacket -> {
            worked.set(ValidHandler);
        });
        return yapionPacketReceiver;
    }

    private void awaitResult(AtomicReference<TestType> result) {
        while (result.get() == null) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Test
    public void connectionWorking() throws Exception {
        YAPIONPacketStream[] yapionSockets = connection();
        assertThat(yapionSockets.length, is(2));
    }

    @Test
    public void sendKnownPacket() throws Exception {
        YAPIONPacketStream[] yapionPacketStreams = connection();
        AtomicReference<TestType> result = new AtomicReference<>(null);
        yapionPacketStreams[1].setYAPIONPacketReceiver(receiver(ValidHandler, result), 1);
        yapionPacketStreams[0].write(new TestPacket());
        awaitResult(result);
        assertThat(result.get(), is(ValidHandler));
    }

    @Test
    public void sendUnknownPacket() throws Exception {
        YAPIONPacketStream[] yapionPacketStreams = connection();
        AtomicReference<TestType> result = new AtomicReference<>(null);
        yapionPacketStreams[1].setYAPIONPacketReceiver(receiver(ValidHandler, result), 1);
        yapionPacketStreams[0].write(new TestPacketTwo());
        awaitResult(result);
        assertThat(result.get(), is(UnknownHandler));
    }

    @Test
    public void sendPlainYAPIONObject() throws Exception {
        YAPIONPacketStream[] yapionPacketStreams = connection();
        AtomicReference<TestType> result = new AtomicReference<>(null);
        yapionPacketStreams[1].setYAPIONPacketReceiver(receiver(ValidHandler, result), 1);
        yapionPacketStreams[0].getYapionOutputStream().write(new YAPIONObject());
        awaitResult(result);
        assertThat(result.get(), is(HandleFailedHandler));
    }

    @Test
    public void sendPlainYAPIONObjectWithInvalidTypeObject() throws Exception {
        YAPIONPacketStream[] yapionPacketStreams = connection();
        AtomicReference<TestType> result = new AtomicReference<>(null);
        yapionPacketStreams[1].setYAPIONPacketReceiver(receiver(ValidHandler, result), 1);
        yapionPacketStreams[0].getYapionOutputStream().write(new YAPIONObject().add(IdentifierUtils.TYPE_IDENTIFIER, new YAPIONObject()));
        awaitResult(result);
        assertThat(result.get(), is(HandleFailedHandler));
    }

    @Test
    public void sendPlainYAPIONObjectWithInvalidTypeValueType() throws Exception {
        YAPIONPacketStream[] yapionPacketStreams = connection();
        AtomicReference<TestType> result = new AtomicReference<>(null);
        yapionPacketStreams[1].setYAPIONPacketReceiver(receiver(ValidHandler, result), 1);
        yapionPacketStreams[0].getYapionOutputStream().write(new YAPIONObject().add(IdentifierUtils.TYPE_IDENTIFIER, new YAPIONValue<>(0)));
        awaitResult(result);
        assertThat(result.get(), is(HandleFailedHandler));
    }

    @Test
    public void sendPlainYAPIONObjectWithInvalidTypeValue() throws Exception {
        YAPIONPacketStream[] yapionPacketStreams = connection();
        AtomicReference<TestType> result = new AtomicReference<>(null);
        yapionPacketStreams[1].setYAPIONPacketReceiver(receiver(ValidHandler, result), 1);
        yapionPacketStreams[0].getYapionOutputStream().write(new YAPIONObject().add(IdentifierUtils.TYPE_IDENTIFIER, new YAPIONValue<>("hugo")));
        awaitResult(result);
        assertThat(result.get(), is(DeserializationExceptionHandler));
    }

    @Test
    public void sendPlainYAPIONObjectNotPacket() throws Exception {
        YAPIONObject yapionObject = YAPIONParser.parse("{@type(yapion.serializing.YAPIONTestObjects$TestInnerArray)intList{@type(java.util.ArrayList)values[[[0L],[1L],[2L]],[[1L],[2L],[3L]],[[2L],[3L],[4L]]]}}");

        YAPIONPacketStream[] yapionPacketStreams = connection();
        AtomicReference<TestType> result = new AtomicReference<>(null);
        yapionPacketStreams[1].setYAPIONPacketReceiver(receiver(ValidHandler, result), 1);
        yapionPacketStreams[0].getYapionOutputStream().write(yapionObject);
        awaitResult(result);
        assertThat(result.get(), is(HandleFailedHandler));
    }

    @Test
    public void sendPlainYAPIONObjectNotPacketExceptionInHandleFailedHandlerAndErrorHandler() throws Exception {
        YAPIONObject yapionObject = YAPIONParser.parse("{@type(yapion.serializing.YAPIONTestObjects$TestInnerArray)intList{@type(java.util.ArrayList)values[[[0L],[1L],[2L]],[[1L],[2L],[3L]],[[2L],[3L],[4L]]]}}");

        YAPIONPacketStream[] yapionPacketStreams = connection();
        AtomicReference<TestType> result = new AtomicReference<>(null);
        yapionPacketStreams[1].setYAPIONPacketReceiver(receiver(ValidHandler, result).setHandler(YAPIONPacketReceiver.Handler.HANDLE_FAILED, yapionPacket -> {
            throw new SecurityException();
        }).setHandler(YAPIONPacketReceiver.Handler.ERROR, yapionPacket -> {
            throw new SecurityException(yapionPacket.getException().getMessage(), yapionPacket.getException());
        }), 1);
        yapionPacketStreams[0].getYapionOutputStream().write(yapionObject);
        awaitResult(result);
        assertThat(result.get(), is(DropHandler));
    }

    @Test
    public void sendPlainYAPIONObjectNotPacketExceptionInHandleFailedHandler() throws Exception {
        YAPIONObject yapionObject = YAPIONParser.parse("{@type(yapion.serializing.YAPIONTestObjects$TestInnerArray)intList{@type(java.util.ArrayList)values[[[0L],[1L],[2L]],[[1L],[2L],[3L]],[[2L],[3L],[4L]]]}}");

        YAPIONPacketStream[] yapionPacketStreams = connection();
        AtomicReference<TestType> result = new AtomicReference<>(null);
        yapionPacketStreams[1].setYAPIONPacketReceiver(receiver(ValidHandler, result).setHandler(YAPIONPacketReceiver.Handler.HANDLE_FAILED, yapionPacket -> {
            throw new SecurityException();
        }), 1);
        yapionPacketStreams[0].getYapionOutputStream().write(yapionObject);
        awaitResult(result);
        assertThat(result.get(), is(ErrorHandler));
    }

    @Test
    public void sendKnownPacketWithException() throws Exception {
        YAPIONPacketStream[] yapionPacketStreams = connection();
        AtomicReference<TestType> result = new AtomicReference<>(null);
        yapionPacketStreams[1].setYAPIONPacketReceiver(receiver(ValidHandler, result).add(TestPacket.class, yapionPacket -> {
            throw new SecurityException();
        }), 1);
        yapionPacketStreams[0].write(new TestPacket());
        awaitResult(result);
        assertThat(result.get(), is(ExceptionHandler));
    }

    @Test
    public void sendPingPongUnknown() throws Exception {
        YAPIONPacketStream[] yapionPacketStreams = connection();
        yapionPacketStreams[1].setYAPIONPacketReceiver(receiver(ValidHandler, new AtomicReference<>(null)).add(TestPacket.class, yapionPacket -> {
            yapionPacketStreams[1].write(new TestPacketTwo());
        }), 1);
        AtomicReference<TestType> result = new AtomicReference<>(null);
        yapionPacketStreams[0].setYAPIONPacketReceiver(receiver(ValidHandler, result), 1);
        yapionPacketStreams[0].write(new TestPacket());
        awaitResult(result);
        assertThat(result.get(), is(UnknownHandler));
    }

    @Test
    public void sendPingPong() throws Exception {
        YAPIONPacketStream[] yapionPacketStreams = connection();
        yapionPacketStreams[1].setYAPIONPacketReceiver(receiver(ValidHandler, new AtomicReference<>(null)).add(TestPacket.class, yapionPacket -> {
            yapionPacketStreams[1].write(new TestPacket());
        }), 1);
        AtomicReference<TestType> result = new AtomicReference<>(null);
        yapionPacketStreams[0].setYAPIONPacketReceiver(receiver(ValidHandler, result), 1);
        yapionPacketStreams[0].write(new TestPacket());
        awaitResult(result);
        assertThat(result.get(), is(ValidHandler));
    }

    @Test
    public void sendPongPingUnknown() throws Exception {
        YAPIONPacketStream[] yapionPacketStreams = connection();
        yapionPacketStreams[0].setYAPIONPacketReceiver(receiver(ValidHandler, new AtomicReference<>(null)).add(TestPacket.class, yapionPacket -> {
            yapionPacketStreams[0].write(new TestPacketTwo());
        }), 1);
        AtomicReference<TestType> result = new AtomicReference<>(null);
        yapionPacketStreams[1].setYAPIONPacketReceiver(receiver(ValidHandler, result), 1);
        yapionPacketStreams[1].write(new TestPacket());
        awaitResult(result);
        assertThat(result.get(), is(UnknownHandler));
    }

    @Test
    public void sendPongPing() throws Exception {
        YAPIONPacketStream[] yapionPacketStreams = connection();
        yapionPacketStreams[0].setYAPIONPacketReceiver(receiver(ValidHandler, new AtomicReference<>(null)).add(TestPacket.class, yapionPacket -> {
            yapionPacketStreams[0].write(new TestPacket());
        }), 1);
        AtomicReference<TestType> result = new AtomicReference<>(null);
        yapionPacketStreams[1].setYAPIONPacketReceiver(receiver(ValidHandler, result), 1);
        yapionPacketStreams[1].write(new TestPacket());
        awaitResult(result);
        assertThat(result.get(), is(ValidHandler));
    }

}
