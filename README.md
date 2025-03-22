# Compose blurhash
Provides multiplatform library to use [blurhash](https://blurha.sh/) in your compose app.<br>
Using SKSL/AGSL for the best possible performance compared to regular bitmap alternatives.

## What platforms are currently supported
- [x] WasmJS/Browser
- [x] Desktop/JVM 
- [x] iOS/Apple
- [ ] Android (Coming Soon)


## Usage
```kt
@Composable
fun MyComposable() {
    BlurHashImage(
        "URGtW@NM}FV?w7={wOi{xYi%s,sm-CNZnPxt", // Your blurhash string 
        modifier = Modifier.fillMaxSize() // Modifiers you would like to apply
    )
}
```

## Project Structure

This project contains several modules each for a different purpose.

1. `blurhash` - Contains the public code for implementing in your project
2. `blurhash-skio` - Contains the code specific to skia related implementations (WasmJS/Desktop/iOS)
3. `blurhash-shader` - Contains the shader related code and processing 
4. `sample` - Contains a super simple sample project to demo the functionality

## Accreditation 

- [peerless2012](https://github.com/peerless2012) For personally helping me get this project adapted over to compose and for his [blurhash implementation for android](https://github.com/peerless2012/blurhash-android)
- [blurha](https://blurha.sh/) For providing a reference on implementation, as well as creating a very simplistic and elegant solution to placeholder images
- [shady](https://github.com/drinkthestars/shady) For providing a reference on how to implement shader based code into compose specifically
- [blurhash_shader](https://github.com/xioxin/blurhash_shader) For providing the initial shader code to base sksl shader code off of
- [Romain Guy](https://www.romainguy.dev/posts/2024/optimization-step-by-step/) For providing a fast and performant color calculation for blurhash strings