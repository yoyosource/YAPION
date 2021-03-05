# Serializer
## notserializable.net
- ServerSocketSerializer
- SocketSerializer
## notserializable.os
- ProcessBuilderSerializer
- ProcessSerializer
## notserializable.thread
- RunnableSerializer
- ThreadGroupSerializer
- ThreadLocalSerializer
- ThreadSerializer
## object.atomic
- AtomicBooleanSerializer
- AtomicIntegerArraySerializer
- AtomicIntegerSerializer
- AtomicLongArraySerializer
- AtomicLongSerializer
- AtomicMarkableReferenceSerializer
- AtomicReferenceArraySerializer
- AtomicReferenceSerializer
- AtomicStampedReferenceSerializer
- DoubleAdderSerializer
- LongAdderSerializer
## object.awt
- BufferedImageSerializer
- ColorSerializer
## object.deque
- DequeSerializer
## object.list
- ListSerializer
## object.map
- MapSerializer
## object.net
- Inet4AddressSerializer
- Inet6AddressSerializer
- InetAddressSerializer
- URISerializer
- URLSerializer
## object.optional
- OptionalDoubleSerializer
- OptionalIntSerializer
- OptionalLongSerializer
- OptionalSerializer
## object.other
- EnumMapSerializer
- EnumSerializer
- EnumSetSerializer
- FileSerializer
- MathContextSerializer
- RandomSerializer
- UUIDSerializer
- YAPIONPacketSerializer
## object.queue
- ArrayBlockingQueueSerializer
- PriorityBlockingQueueSerializer
- QueueSerializer
## object.regex
- PatternSerializer
## object.security
- KeyPairSerializer
- PrivateKeySerializer
- PublicKeySerializer
## object.security.internal
- DHKeySpecSerializer (1)
- DSAKeySpecSerializer (1)
- ECKeySpecSerializer (1)
- RSAKeySpecSerializer (1)
- RSAMultiPrimePrivateCrtKeySpecSerializer (1)
- RSAPrivateCrtKeySpecSerializer (1)
## object.security.internal.ec
- ECFieldF2mSerializer (1)
- ECFieldFpSerializer (1)
- ECParameterSpecSerializer (1)
- ECPointSerializer (1)
- EllipticCurveSerializer (1)
## object.security.internal.rsa
- RSAOtherPrimeInfoSerializer (1)
## object.set
- SetSerializer
## object.stack
- StackSerializer
## object.table
- HashTableSerializer
## object.text
- SimpleDateFormatSerializer
## object.throwable
- ErrorSerializer
- ExceptionSerializer
- InvocationTargetExceptionSerializer
- RuntimeExceptionSerializer
- StackTraceElementSerializer
- ThrowableSerializer
## object.time
- DateSerializer
- DurationSerializer
- InstantSerializer
- LocalDateSerializer
- LocalDateTimeSerializer
- LocalTimeSerializer
- MonthDaySerializer
- OffsetDateTimeSerializer
- OffsetTimeSerializer
- PeriodSerializer
- YearMonthSerializer
- YearSerializer
- ZoneIdSerializer
- ZoneOffsetSerializer
- ZonedDateTimeSerializer
## object.vector
- VectorSerializer
## object.zip
- Adler32Serializer
- CRC32Serializer
## primitive
- BooleanSerializer
- CharacterSerializer
## primitive.number
- BigDecimalSerializer
- BigIntegerSerializer
- ByteSerializer
- DoubleSerializer
- FloatSerializer
- IntegerSerializer
- LongSerializer
- ShortSerializer
## primitive.string
- StringBufferSerializer
- StringBuilderSerializer
- StringSerializer
# Foot Notes
- (1) not advised to use directly, use either PrivateKey or PublicKey instead