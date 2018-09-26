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
            is CatViewEvent.Meow -> // show Meow! dialog
            is CatViewEvent.Sleep -> // finish Activity
        }
        true
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
