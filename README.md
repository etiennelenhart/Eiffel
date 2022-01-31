# Eiffel

[![Build Status](https://github.com/etiennelenhart/eiffel/workflows/Android%20CI/badge.svg?branch=master)](https://github.com/etiennelenhart/Eiffel/actions)
[![JitPack](https://jitpack.io/v/etiennelenhart/eiffel.svg)](https://jitpack.io/#etiennelenhart/eiffel)

![Logo](./logo_full.svg)

A Redux-inspired Android architecture library leveraging [Architecture Components](https://developer.android.com/topic/libraries/architecture) and Kotlin Coroutines.

## Quick example

```kotlin
data class HelloEiffelState(val greeting: String = "Hello Eiffel") : State

sealed class HelloEiffelAction : Action {
    object NowInFrench : HelloEiffelAction()
    data class Greet(val name: String) : HelloEiffelAction()
}

val helloEiffelUpdate = update<HelloEiffelState, HelloEiffelAction> { action ->
    when (action) {
        is HelloEiffelAction.NowInFrench -> copy(greeting = greeting.replace("Hello", "Salut"))
        is HelloEiffelAction.Greet -> copy(greeting = "Salut ${action.name}")
    }
}

class HelloEiffelViewModel(initialState: HelloEiffelState) :
    EiffelViewModel<HelloEiffelState, HelloEiffelAction>(initialState) {
    override val update = helloEiffelUpdate
}

class HelloEiffelFragment : Fragment() {
    private val viewModel: HelloEiffelViewModel by eiffelViewModel()
    ...
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // example with View Bindings
        binding = FragmentHelloEiffelBinding.inflate(inflater, container, false)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.greetingText.text = state.greeting
        }
        binding.frenchButton.setOnClickListener { viewModel.dispatch(HelloEiffelAction.NowInFrench) }

        return binding.root
    }
    ...
}
```

## Installation

build.gradle _(project)_

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```

build.gradle _(module)_

```gradle
dependencies {
    implementation 'com.github.etiennelenhart.eiffel:eiffel:5.0.0'
    implementation 'com.github.etiennelenhart.eiffel:eiffel-test:5.0.0'
}
```

## Features

Apart from providing Redux-like reactive ViewModels, Eiffel includes the following features to simplify common Android-related tasks and architecture plumbing:

- First class support for Kotlin Coroutines and Flow
- Powerful middleware functionality in the form of `Interceptions` with an easy-to-use DSL
- Extended state observing for subscribing to specific state properties only
- Convenient way to restore part or all of a state after process death
- `BindableState` class to adapt one or more states for use with Data Binding
- Simple option to pass `Intent` extras and `Fragment` arguments to a ViewModel's initial state
- Implementation of a `ViewEvent` for one-off events inside of states
- `Resource` wrapper to associate a status to `LiveData`
- Delegated properties to lazily access a ViewModel indside an `Activity` or `Fragment`
- A dedicated debug mode to trace all dispatched actions, interception calls and state updates
- Separate testing module with JUnit rules to test async behavior and helpers to test a chain of `Interceptions` in isolation

Info on all of these and more can be found in the [Wiki](https://github.com/etiennelenhart/Eiffel/wiki).

## Interceptions DSL

Eiffel includes an easy-to-use Domain-specific language for creating a chain of `Interceptions`. This allows you to define the logic of your ViewModel domain in a simple and declarative way. Iterating on the quick example above, this is how you can define a set of interceptions in a few lines of code:

```kotlin
val helloEiffelInterceptions = interceptions<HelloEiffelState, HelloEiffelAction> {
    add(CustomInterception()) // your custom interception
    pipe { _, action -> Analytics.log("HelloEiffel", action) } // log something to analytics
    on<HelloEiffelAction.Greet> { // following will only react to 'Greet' action
        adapter("Upper case name") { _, action ->
            HelloEiffelAction.Greet(action.name.toUpperCase())
        }
        filter { state, action -> // ignore duplicate button presses and empty names
            !state.greeting.contains(action.name) || action.name.isNotBlank()
        }
    }
}

class HelloEiffelViewModel(initialState: HelloEiffelState) :
    EiffelViewModel<HelloEiffelState, HelloEiffelAction>(initialState) {
    override val update = helloEiffelUpdate
    override val interceptions = helloEiffelInterceptions
}
```

## Migration

Migration guides for breaking changes:

- [2.0.0 → 3.x.x](./MIGRATION2-3.md)
- [3.x.x → 4.x.x](./MIGRATION3-4.md)
- [since 5.0 in the Wiki](https://github.com/etiennelenhart/Eiffel/wiki/Migration-4.x.x-%E2%86%92-5.x.x)
