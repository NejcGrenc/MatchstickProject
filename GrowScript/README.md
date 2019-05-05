# GrowScript

## Description

GrowScript is a lightweight plug-in that handles text generation.  

Text is split into smaller closed segments, which hold references to one another with mustache variables *{{ var_name }}*.  
When text processing is executed, all text segments are merged together into a complete text.  
Some text can also be resolved according to provided variables.

The name comes form expanding text segments, which *grow* as they are being processed.


## Example

**text 1**  
*name*: "Luke Skywalker" 

**text 2**  
*statement*: "{{name}} destroyed the death star."  

**Processed text**  
"Luke Skywalker destroyed the death star." 