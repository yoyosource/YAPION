StringToCharsOutput
StringToBytesOutput
StringToIntsOutput

MutatingOutput<T, T> ----> is BaseOutput
Output<T> -> MutatingOutput<T, T>
StreamOutput(OutputStream) -> Output<Byte>
StringToBytesOutput(StreamOutput) -> MutatingOutut<String, Byte>
StringOutput -> Output<String>
StringToBytesOutput(FileOutput(File) -> StreamOutput(...))
PrettifyOutput(Output<?>) -> Output<?>
ArrayOutput(Output<?>) -> List<?>