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
## object.collection
- ArrayBlockingQueueSerializer
- DequeSerializer
- ListSerializer
- PriorityBlockingQueueSerializer
- QueueSerializer
- SetSerializer
- StackSerializer
- VectorSerializer
## object.map
- HashTableSerializer
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
- ClassSerializer
- EnumMapSerializer
- EnumSerializer
- EnumSetSerializer
- FileSerializer
- MathContextSerializer
- RandomSerializer
- UUIDSerializer
## object.reflect
- ConstructorSerializer
- FieldSerializer
- MethodSerializer
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
## object.yapion.diff
- DiffChangeSerializer
- DiffDeleteSerializer
- DiffInsertSerializer
- YAPIONDiffSerializer
## object.yapion.packet
- YAPIONPacketSerializer
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
