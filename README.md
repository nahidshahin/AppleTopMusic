AppleTopMusic
=============

The application needs to contain 2 tabs.
1. Tab
Read JSON data from the iTunes top chart service https://itunes.apple.com/us/rss/topsongs/limit=300/json and display it to the user in ListView. Each entry of ListView should contain name of the song and the cover image. On click on each entry menu with options should be displayed:
1. Download image.
2. Add to favorites. Data should be saved to DB. One song can't be added to favorites more than one time.


2. Tab

Display to user saved to favorites songs from the DB.
 On click on each entry menu with Delete option should be displayed.

Technical requirements:
1. You can use any frameworks and libraries.
2. Use Android official build tool Gradle.
3. minSdkVersion = 15 (use all advantages of 15 API level)
4. Follow OOP best practices
5. Following Android design and development best practices, good usability is a big plus.
