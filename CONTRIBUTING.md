# Contributing

## Code Quality Standards
The standards below are a direct extension to the [Java](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html) and [Kotlin](https://kotlinlang.org/docs/coding-conventions.html) Coding Conventions, and should also be followed during development. 

- Files should be written in Kotlin, excluding mixins and some configs.
- Event Subscriber functions should be placed before other methods. (excluding HudElement's `drawPreview()`)
- Use and build upon the Vice Events System. When possible, use Fabric API events.
- Use `Debugger` or `DevUtils` for error logging and debugging.
- When possible, avoid using Kotlin's `!!` feature, in favour of `?:`
- Avoid using Wildcard Imports (especially within Vice.kt)
    - This can be automatically disabled in IntelliJ by entering Settings > Editor > Code Style > Kotlin > Indents and selecting 'Use single name import' for each.

### Tips
- Use [regex101](https://regex101.com/) or [RegExr](https://regexr.com/) to test Regex.

## External Tools
### DevAuth
DevAuth enables the ability to log in to Minecraft Accounts using the IntelliJ client. This prevents the need to create jar files, and instead launch directly from your IDE.

Follow [this guide](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html) from SkyHanni to learn how to set it up.
