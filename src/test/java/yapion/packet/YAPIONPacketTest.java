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
import org.junit.Test;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.io.YAPIONSocket;
import yapion.parser.YAPIONParser;
import yapion.utils.IdentifierUtils;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static yapion.packet.YAPIONPacketTestObjects.*;
import static yapion.packet.YAPIONPacketTestObjects.TestType.*;

public class YAPIONPacketTest {

    private static ServerSocket serverSocket;

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
            yapionPacketReceiver.setDropHandler(yapionPacket -> {
                worked.set(DropHandler);
            });
        }
        if (testType != ErrorHandler) {
            yapionPacketReceiver.setErrorHandler(yapionPacket -> {
                worked.set(ErrorHandler);
            });
        }
        if (testType != ExceptionHandler) {
            yapionPacketReceiver.setExceptionHandler(yapionPacket -> {
                worked.set(ExceptionHandler);
            });
        }
        if (testType != HandleFailedHandler) {
            yapionPacketReceiver.setHandleFailedHandler(yapionPacket -> {
                worked.set(HandleFailedHandler);
            });
        }
        if (testType != UnknownHandler) {
            yapionPacketReceiver.setUnknownHandler(yapionPacket -> {
                worked.set(UnknownHandler);
            });
        }
        if (testType != DeserializationExceptionHandler) {
            yapionPacketReceiver.setDeserializationExceptionHandler(yapionPacket -> {
                worked.set(DeserializationExceptionHandler);
            });
        }
        yapionPacketReceiver.add(TestPacket.class, yapionPacket -> {
            worked.set(ValidHandler);
        });
        return yapionPacketReceiver;
    }

    private void sleep() {
        sleep(50);
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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
        sleep();
        assertThat(result.get(), is(ValidHandler));
    }

    @Test
    public void sendUnknownPacket() throws Exception {
        YAPIONPacketStream[] yapionPacketStreams = connection();
        AtomicReference<TestType> result = new AtomicReference<>(null);
        yapionPacketStreams[1].setYAPIONPacketReceiver(receiver(ValidHandler, result), 1);
        yapionPacketStreams[0].write(new TestPacketTwo());
        sleep();
        assertThat(result.get(), is(UnknownHandler));
    }

    @Test
    public void sendPlainYAPIONObject() throws Exception {
        YAPIONPacketStream[] yapionPacketStreams = connection();
        AtomicReference<TestType> result = new AtomicReference<>(null);
        yapionPacketStreams[1].setYAPIONPacketReceiver(receiver(ValidHandler, result), 1);
        yapionPacketStreams[0].getYapionOutputStream().write(new YAPIONObject());
        sleep();
        assertThat(result.get(), is(HandleFailedHandler));
    }

    @Test
    public void sendPlainYAPIONObjectWithInvalidTypeObject() throws Exception {
        YAPIONPacketStream[] yapionPacketStreams = connection();
        AtomicReference<TestType> result = new AtomicReference<>(null);
        yapionPacketStreams[1].setYAPIONPacketReceiver(receiver(ValidHandler, result), 1);
        yapionPacketStreams[0].getYapionOutputStream().write(new YAPIONObject().add(IdentifierUtils.TYPE_IDENTIFIER, new YAPIONObject()));
        sleep();
        assertThat(result.get(), is(HandleFailedHandler));
    }

    @Test
    public void sendPlainYAPIONObjectWithInvalidTypeValueType() throws Exception {
        YAPIONPacketStream[] yapionPacketStreams = connection();
        AtomicReference<TestType> result = new AtomicReference<>(null);
        yapionPacketStreams[1].setYAPIONPacketReceiver(receiver(ValidHandler, result), 1);
        yapionPacketStreams[0].getYapionOutputStream().write(new YAPIONObject().add(IdentifierUtils.TYPE_IDENTIFIER, new YAPIONValue<>(0)));
        sleep();
        assertThat(result.get(), is(HandleFailedHandler));
    }

    @Test
    public void sendPlainYAPIONObjectWithInvalidTypeValue() throws Exception {
        YAPIONPacketStream[] yapionPacketStreams = connection();
        AtomicReference<TestType> result = new AtomicReference<>(null);
        yapionPacketStreams[1].setYAPIONPacketReceiver(receiver(ValidHandler, result), 1);
        yapionPacketStreams[0].getYapionOutputStream().write(new YAPIONObject().add(IdentifierUtils.TYPE_IDENTIFIER, new YAPIONValue<>("hugo")));
        sleep(200);
        assertThat(result.get(), is(DeserializationExceptionHandler));
    }

    @Test
    public void sendPlainYAPIONObjectNotPacket() throws Exception {
        YAPIONObject yapionObject = YAPIONParser.parse("{@type(yapion.serializing.YAPIONTestObjects$TestInnerArray)intList{@type(java.util.ArrayList)values[[[0L],[1L],[2L]],[[1L],[2L],[3L]],[[2L],[3L],[4L]]]}}");

        YAPIONPacketStream[] yapionPacketStreams = connection();
        AtomicReference<TestType> result = new AtomicReference<>(null);
        yapionPacketStreams[1].setYAPIONPacketReceiver(receiver(ValidHandler, result), 1);
        yapionPacketStreams[0].getYapionOutputStream().write(yapionObject);
        sleep();
        assertThat(result.get(), is(HandleFailedHandler));
    }

    @Test
    public void sendPlainYAPIONObjectNotPacketExceptionInHandleFailedHandlerAndErrorHandler() throws Exception {
        YAPIONObject yapionObject = YAPIONParser.parse("{@type(yapion.serializing.YAPIONTestObjects$TestInnerArray)intList{@type(java.util.ArrayList)values[[[0L],[1L],[2L]],[[1L],[2L],[3L]],[[2L],[3L],[4L]]]}}");

        YAPIONPacketStream[] yapionPacketStreams = connection();
        AtomicReference<TestType> result = new AtomicReference<>(null);
        yapionPacketStreams[1].setYAPIONPacketReceiver(receiver(ValidHandler, result).setHandleFailedHandler(yapionPacket -> {
            throw new SecurityException();
        }).setErrorHandler(yapionPacket -> {
            throw new SecurityException(yapionPacket.getException().getMessage(), yapionPacket.getException());
        }), 1);
        yapionPacketStreams[0].getYapionOutputStream().write(yapionObject);
        sleep(400);
        assertThat(result.get(), is(DropHandler));
    }

    @Test
    public void sendPlainYAPIONObjectNotPacketExceptionInHandleFailedHandler() throws Exception {
        YAPIONObject yapionObject = YAPIONParser.parse("{@type(yapion.serializing.YAPIONTestObjects$TestInnerArray)intList{@type(java.util.ArrayList)values[[[0L],[1L],[2L]],[[1L],[2L],[3L]],[[2L],[3L],[4L]]]}}");

        YAPIONPacketStream[] yapionPacketStreams = connection();
        AtomicReference<TestType> result = new AtomicReference<>(null);
        yapionPacketStreams[1].setYAPIONPacketReceiver(receiver(ValidHandler, result).setHandleFailedHandler(yapionPacket -> {
            throw new SecurityException();
        }), 1);
        yapionPacketStreams[0].getYapionOutputStream().write(yapionObject);
        sleep();
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
        sleep();
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
        sleep();
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
        sleep();
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
        sleep();
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
        sleep();
        assertThat(result.get(), is(ValidHandler));
    }

}
