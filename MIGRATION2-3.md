## Migrating from 2.0.0 to 3.0.0

### ViewEvent
Handling of a `ViewEvent` has been greatly simplified. You now can just call `handle()` on an event and pass it a lambda expression that will only be called if the event has not been handled already. Internally the event is marked as "handled" before the expression is invoked.

Old and no longer supported method:
```kotlin
viewModel.state.observe(this, Observer {
    ...
    if (!it.event.handled) {
        when (it.event) {
            is CatViewEvent.Meow -> {
               it.event.handled = true
               // show Meow! dialog
            }
            is CatViewEvent.Sleep -> {
               it.event.handled = true
               // finish Activity
            }
        }
    }
})
```
New and simplified way:
```kotlin
viewModel.state.observe(this, Observer {
    ...
    when (it.event) {
       is CatViewEvent.Meow -> it.event.handle { // show Meow! dialog }
       is CatViewEvent.Sleep -> it.event.handle { // finish Activity }
    }
})
```

### Result
`Result` has been split into `Result` and `LiveResult`. `Result` now no longer provides a `Pending` variant, since commands with continous status updates are rare and the combined API polluted simple commands.

Commands with intermediate status updates are still supported with the new `LiveResult` class which also allows different types for pending and success data.

`Result.Error` no longer has a `data` property. This is more in line with the semantics of an "error" and you don't have to set a default value as a fallback anymore. This was especially cumbersome when returning results with Data Classes where you had to provide some kind of "empty" class in errors.

### Status
`Result` and `Resource` no longer provide a `status` property, since it was redundant to the actual type. Use the extension functions like `fold()` instead, if you previously relied on `status` for processing results. The `Status` enum has therefore also been removed. 

### Resource
The "failed" variant of `Resource` has gone through some naming changes to stay consistent with the new `LiveResult` type:

2.0.0 | 3.0.0
------|------
`Resource.Error` | `Resource.Failure`
`errorValue()` | `failureValue()`
`isError {}` | `isFailure {}`
