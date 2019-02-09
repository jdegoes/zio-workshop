# ZIO essential

In ZIO Essentials, you will learn the basic building blocks in ZIO and how to combine them together to write fast programs 
that use the Scala compiler to safely handle errors, manage service-oriented dependencies, execute real-world effects, 
and safely acquire and release resources. More than just learning ZIO building blocks, however, 
you will learn how to think about pure functional programs as the composition of pure immutable values that model 
interaction with the real world. By the end of the day, you'll know to build real-world, effectful applications 
that are easy to reason about and easy to test using the ZIO library.

### Effects

 In object-oriented programming, types say nothing about the behavior of a function, and detailed exploration of code 
 is necessary to understand behavior. Functional programming solves these problems by turning effects into ordinary values, 
 which provides a uniform model for both asynchronous and synchronous computation, allows types to describe behavior, 
 and provides powerful ways of reasoning about and composing programs. You will learn how to model any interaction 
 with effectful systems using the ZIO library, trivially reusing existing code, even if it's not written using ZIO.

### Resource-Safety

 In object-oriented programming, the only primitive for resource safety is try / finally, which doesn’t work for asynchronous and concurrent programs, 
 and can’t help avoiding thread-leaks. Functional programming allows us to increase the usefulness of try / finally across asynchronous and concurrent code, and across threads as well as file and socket handles. 
 You will learn how to write ZIO programs that cannot leak resources.

### Testing & Mocking

 In object-oriented programming, testing effectful code is difficult, often requiring strange mocking libraries that 
 perform bytecode rewriting. Functional programming makes testing even the most effectful code trivial, without requiring 
 any bytecode rewriting and still being completely typesafe. You will learn how to thoroughly test effectful ZIO code 
 with blazing fast unit tests that require no magic and are completely type safe.
 
 # Legal
 
 Copyright&copy; 2019 John A. De Goes. All rights reserved.
