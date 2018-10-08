## Migrating from 3.x.x to 4.0.0

### StateViewModel state property
The `state` property in `StateViewModel` is now protected to prevent observers from mutating its value. Just use the new `observeState` function instead. Note, that the provided view state is also no longer nullable.

Old and no longer supported:
```kotlin
viewModel.state.observe(this, Observer { it!! }) // no longer supported
```

New way:
```kotlin
viewModel.observeState(this) { it }
```

### ViewEvent
Handling of a `ViewEvent` got even simpler. Instead of having to call `handle()` on every possible branch of the when expression, just use the new `peek()` function. It expects a lambda expression which receives the current event and expects a boolean. If it returns `true` the event is marked as handled. `ViewEvent.None` was removed and a `ViewState` should now use a nullable and specifically typed `event` property. This allows for exhaustive processing in when expressions so you'll never forget to handle an event you might have cared about.

So change your view states like this:
```kotlin
data class CatViewState(..., val event: ViewEvent = ViewEvent.None) : ViewState // no longer supported
data class CatViewState(..., val event: CatViewEvent? = null) : ViewState
```

Old and no longer supported way to handle events:
```kotlin
viewModel.state.observe(this, Observer {
    // no longer supported
    when (val event = it.event) {
       is CatViewEvent.Meow -> event.handle { /* show Meow! dialog */ }
       is CatViewEvent.Sleep -> event.handle { /* finish Activity */ }
    }
})
```
New way with support for exhaustive when expressions:
```kotlin
viewModel.observeState(this) { state ->
    state.event?.peek {
        when (it) {
            is CatViewEvent.Meow -> {
                // show Meow! dialog
                true
            }
            is CatViewEvent.Sleep -> {
                // finish Activity
                true
            }
        }
    }
}
```
To handle only some of the possible events just use `else -> false` in the when expression:
```kotlin
viewModel.observeState(this) { state ->
    state.event?.peek {
        when (it) {
            is CatViewEvent.Meow -> {
                // show Meow! dialog
                true
            }
            else -> false
        }
    }
}
```

### Result related functionality
Result-like error handling is more or less [discouraged in Kotlin](https://github.com/Kotlin/KEEP/blob/master/proposals/stdlib/result.md#error-handling-style-and-exceptions) and the narrow scope of Eiffel's result classes decreases interoperability with library code, that does not and probably should not depend on Eiffel. Additionally Eiffel should be an easy-to-use architecture library for everyone and not impose a specific style like functional programming, which the included result implementation did.

To ease transition all result-related classes are now deprecated and will be removed in the next major version. There are a couple of good fitting alternatives:

#### Nullable types
Kotlin's strong nullability support makes using nullable result types quite feasible. So instead of returning e.g. `Result<Int>`, just use `Int?`. It is even possible to use `?.let {}` as replacement for `then()` and `?:` to supplement `fold()`.
A simple migration of `Result` commands to nullable types could work like this:
```kotlin
getMilk().then { fillBowl(it) }.fold({ /* succeeded */ }, { /* failed */ }) // deprecated

getMilk()?.let { fillBowl(it) }?.let { /* succeeded */ } ?: /* failed */
```

#### Domain-specific data types
While nullable types support easy declaration and processing in Kotlin, they don't allow for more specific error handling. There is no indication of *why* the command failed. This is where domain-specific data types come into play. They are implemented as normal Sealed Classes and can then be processed using when expressions:
```kotlin
sealed class PersistResult {
    object Persisted : PersistResult()
    object FileNotFound : PersistResult()
}
```
```kotlin
typealias PersistMeowCount = (count: Int) -> PersistResult
```
```kotlin
when (persistMeowCount(/* count */)) {
    PersistResult.Persisted -> // Notify success
    PersistResult.FileNotFound -> // Process the error
}
```
The drawback here of course is the lack of easy chaining.

#### Functional programming types
If you need both easy chaining of commands and specific errors, functional programming types like `Try` and `Either` are a good fit. [Λrrow](https://arrow-kt.io/) is becoming the de-facto standard for functional programming with Kotlin. It provides `Try`, `Either` and a whole lot more, which more than compensate for Eiffel's result classes. It also increases interoperability, since in contrast to Eiffel, Λrrow may also be used to develop library code.

If your project heavily depends on Eiffel's implementation and the above alternatives don't cut it, feel free to contact me on Twitter [@etiennelenhart](https://twitter.com/etiennelenhart) or create an issue and if there are good reasons, it may be added back.

### Resource
Following the deprecation of the result functionality `Resource` has also been changed. It is now possible to provide your own type instead of having to extend `ErrorType`, so `Resource` takes two type parameters:
```kotlin
class CatMilkLiveData : LiveData<Resource<MilkStatus>>() { ... } // no longer supported
class CatMilkLiveData : LiveData<Resource<MilkStatus, MilkError>>() { ... }
```
`fold()` has also been removed to stay in line with not imposing a functional programming style. To accomomdate this, `isSuccess`, `isPending` and `isFailure` are now prefixed with `on` instead of `is` and pass through the `Resource`. This allows for a more dynamic handling of status types:
```kotlin
resource?
    .onSuccess {
        val name = if (it == MilkStatus.FULL) "Happy Whiskers" else "Hungry Whiskers"
        updateState { it.copy(name = name) }
    }
    .onError { _, _ -> updateState { it.copy(name = "") } }
```
