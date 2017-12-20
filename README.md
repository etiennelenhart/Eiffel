# Eiffel
[![Build Status](https://www.bitrise.io/app/d982833489004cbc/status.svg?token=66rf2t84v8SFdippsAWM8g&branch=master)](https://www.bitrise.io/app/d982833489004cbc)
[![](https://jitpack.io/v/etiennelenhart/eiffel.svg)](https://jitpack.io/#etiennelenhart/eiffel)

Light-weight Kotlin Android architecture library for handling immutable view states with [Architecture Components](https://developer.android.com/topic/libraries/architecture/index.html).

## Installation
build.gradle *(Project)*
```gradle
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
```

build.gradle *(Module)*
```gradle
dependencies {
    ...
    implementation "android.arch.lifecycle:extensions:$architecture_version"
    implementation 'com.github.etiennelenhart:eiffel:1.0.0'
}
```
