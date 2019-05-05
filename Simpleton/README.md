# Simpleton

## Description

Simpleton is a lightweight plug-in that handles singleton beans.

Each class, annotated with appropriate annotation, gets generated at startup  
and inserted into other beans' fields of the same type.  
There is only one instance of each bean alive at all times, so these beans have to be stateless.

The name expresses that these singleton beans are simple to use. 


## Example

**Bean 1**  
class SimpleBean1

**Bean 2**  
class SimpleBean2
{
  SimpleBean1 instance
}

On startup a single instance of beans 1 and 2 is created and  
the instance of SimpleBean1 is inserted into SimpleBean2's field instance.