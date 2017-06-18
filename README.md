# Wikipedia Image Search App

An Android application designed for searching wikipedia images.

It consists of :

1. Fragment for string input and grid of search images
2. Guiding user for proper input, network unavailability/errors, search results unavailability, etc.
3. Dynamic resizing of search results accordingly to available real estate of fragment.
4. Restoring state when device is rotated or fragment resized due to multi-window mode (Android N and above).
5. Restarting query if device is rotated during search progress
6. Usage of MVP pattern and Provider pattern for simplifying Junits.
7. Use of Volley for requesting data from wikipedia
8. Use of Picasso for downloading and caching images
9. Instrumented test cases with Espresso and Mockito
10. Local unit test cases for POJOs
