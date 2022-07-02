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

package yapion;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import yapion.annotations.object.YAPIONData;
import yapion.annotations.object.YAPIONPreDeserialization;
import yapion.packet.YAPIONPacket;
import yapion.packet.YAPIONPacketExecutor;
import yapion.packet.YAPIONPacketReceiver;
import yapion.packet.YAPIONPacketStream;
import yapion.serializing.data.DeserializationContext;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class WeirdPointerBehaviour {

    @AllArgsConstructor
    @ToString
    private static class TestPacket extends YAPIONPacket {
        private TestInnerClass testInnerClass;
        private TestInner2Class testInner2Class;

        @YAPIONPreDeserialization
        public void test(DeserializationContext deserializationContext) {
            System.out.println(": " + deserializationContext.getYapionObject().getObject("testInner2Class").getPointer("s").get());
        }
    }

    @AllArgsConstructor
    @ToString
    @YAPIONData
    private static class TestInnerClass {
        private String s;
    }

    @YAPIONData
    private static class TestInner2Class {
        @Setter
        private TestPacket s;

        @Override
        public String toString() {
            return "TestInner2Class{" +
                    "s=" + (s != null) + ", " +
                    "hashCode=" + hashCode() +
                    '}';
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        ServerSocket serverSocket = new ServerSocket(4000);
        YAPIONPacketStream stream = new YAPIONPacketStream(new Socket("localhost", 4000));
        YAPIONPacketStream sStream = new YAPIONPacketStream(serverSocket.accept());

        YAPIONPacketExecutor packetExecutor = new YAPIONPacketExecutor();
        packetExecutor.register(stream);
        packetExecutor.register(sStream);

        TestInnerClass testInnerClass = new TestInnerClass("test");
        TestInner2Class testInner2Class = new TestInner2Class();
        TestPacket testPacket = new TestPacket(testInnerClass, testInner2Class);
        testInner2Class.setS(testPacket);
        System.out.println("Original: " + testPacket);

        AtomicBoolean finished = new AtomicBoolean();
        sStream.setYAPIONPacketReceiver(new YAPIONPacketReceiver() {
            @PacketHandler
            public void testPacket(TestPacket packet) {
                System.out.println(packet);
                finished.set(true);
            }
        });

        stream.write(testPacket);

        while (!finished.get()) {
            Thread.sleep(100);
        }

        System.out.println("Original: " + testPacket);
    }
}
