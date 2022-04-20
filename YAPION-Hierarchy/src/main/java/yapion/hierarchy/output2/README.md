# Output API Version 3

- UnflavouredOutput
  - Flavour API
  - Output API
- Flavour
- Output

- UnflavouredOutput
  - UnflavouredMultiplexingOutput(UnflavouredOutput...)
  - FlavouredOutput(Flavour, Output)

- Flavour (the same as current)

- Output (the same as current)

## Pro/Cons
- Pro:
  - You can output a hierarchy directly into multiple output flavours, and even multiple files or streams
- Cons:
  - Indentation needs to be adjusted
  - Code needs to be re-written
  - Irritating API, multiple layers for simple output (Idea just rework the current output style)