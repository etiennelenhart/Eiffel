## Migrating from 3.x.x to 4.0.0

### StateViewModel state property
The `state` property in `StateViewModel` is now protected to prevent observers from mutating its value. Just use the new `observeState` function instead. Note, that the provided view state is also no longer nullable.

Old and no longer supported:
```kotlin
viewModel.state.observe(this, Observer { it!! })
```

New way:
```kotlin
viewModel.observeState(this) { it }
```
