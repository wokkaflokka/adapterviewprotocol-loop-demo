# AVP Loop Demo

This project demonstrates an infinite loop in [**Espresso**](https://google.github.io/android-testing-support-library/docs/espresso/),
the UI testing framework. Espresso interactions with `ListView` and similar views can enter
a loop condition that will never terminate if the target view is not visible on screen
and cannot be brought on screen (from the perspective of the Espresso framework).

Though the code contained in this project is demonstrative in nature, this behavior is in fact
reproducible under real application conditions.

A detailed report of my individual analysis can be found in the documentation of `HangingAdapterViewProtocol`.

