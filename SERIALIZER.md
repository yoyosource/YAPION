# Serializer
## notserializable.net
- ServerSocketSerializer (1)
- SocketSerializer (1)
## notserializable.os
- ProcessBuilderSerializer (1)
- ProcessSerializer (1)
## notserializable.thread
- RunnableSerializer (1)
- ThreadGroupSerializer (1)
- ThreadLocalSerializer (1)
- ThreadSerializer (1)
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
- DHKeySpecSerializer (2)
- DSAKeySpecSerializer (2)
- ECKeySpecSerializer (2)
- RSAKeySpecSerializer (2)
- RSAMultiPrimePrivateCrtKeySpecSerializer (2)
- RSAPrivateCrtKeySpecSerializer (2)
## object.security.internal.ec
- ECFieldF2mSerializer (2)
- ECFieldFpSerializer (2)
- ECParameterSpecSerializer (2)
- ECPointSerializer (2)
- EllipticCurveSerializer (2)
## object.security.internal.rsa
- RSAOtherPrimeInfoSerializer (2)
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
- (1) not serializable because of some internals
- (2) not advised to use directly, use either PrivateKey or PublicKey instead
