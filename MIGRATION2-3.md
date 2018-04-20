## Migrating from 2.0.0 to 3.0.0

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
