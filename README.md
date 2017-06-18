# Wikipedia Image Search App

An Android application designed for searching wikipedia images.

It consists of :
1. Fragment for string input and grid of search images
2. Guiding user for proper input, network unavailablity/errors, search results unavailability, etc.
3. Dynamic resizing of search results accordingly to available real estate of fragment.
4. Restoring state when device is rotated or fragment resized due to multi-window mode (Android N and above).
5. Usage of MVP pattern and Provider pattern for simplifying Junits.
6. Use of Volley for requesting data from wikipedia
7. Use of Picasso for downloading and caching images
8. Test cases for instrumented and local unit test cases
