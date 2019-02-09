#ZIO Concurrency
In ZIO Concurrency, you will learn the basics of ZIO's fiber-based concurrency model, which scales well past the limiations of JVM threads. 
In the process, you'll learn how to build concurrent applications that take advantage of arbitrary numbers of cores, 
safely update data that's shared between many fibers, and write code without using any locks, semaphores, or other legacy concurrency structures. 
By the end of the day, you’ll be able to rapidly and easily build high-performance concurrent applications 
that don't suffer from any of the concurrency and synchronization problems that plague imperative and object-oriented approaches.

###Concurrency & Parallelism
In object-oriented programming, concurrency and parallelism are extremely painful, because of the presence of shared mutable state, 
which leads to deadlocks, thread leaks, and other massive pain points. Functional programming provides a more principled, 
sane model of concurrency and parallelism, which makes developing asynchronous, safe, scalable programs trivial. 
You will learn how to write efficient (asynchronous) and performant code using the simple building blocks in ZIO.

###Shared Concurrent State. 
In object-oriented programming, mutable data is notorously difficult to update safely in a concurrent setting. In functional programming, 
purely functional models of mutable data are equipped with powerful features like atomic update, which allows programmers to reduce or eliminate error-prone structures like semaphores, mutexes, and locks.
You will learn how to use ZIO primitives to easily and safely share data between many different fibers.

###Classic Concurrent Structures. 
In object-oriented programming, powerful concurrent structures like promises, 
queues and maps allows programmers to more easily solve challenging problems. In functional programming, we have these same concurrent structures, 
only they don't waste resources by blocking, they expose a uniform access model whether the updates are synchronous or asynchronous, 
and they integrate beautifully with other functional features. You will learn how to use ZIO's concurrent data structures to make short work of challenging problems in concurrency and synchronization.

###Streaming
In object-oriented programming, streaming libraries have taken some principles from functional programming and applied them to solve problems 
in service request and data processing. In functional programming, the birthplace of streaming, we push streaming further 
by making the entire workflow pure, which gives us advantages in improved laziness, type safety, and concurrency. 
You'll learn how to use ZIO's concurrent streaming abstractions to process large or infinite amounts of data in finite memory, 
using a declarative API that expresses intent rather than low-level mechanics.

