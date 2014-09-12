AppleTopMusic
=============

The application needs to contain 2 tabs.

1st Tab
-------

Read JSON data from the iTunes top chart service [link](https://itunes.apple.com/us/rss/topaudiobooks/limit=100/json) and display it to the user in ListView. Each entry of ListView should contain name of the song and the cover image. 

On click on each entry menu with options should be displayed:

 * Download image.
 * Add to favorites. Data should be saved to DB. One song can't be added to favorites more than one time.


2nd Tab
-------

Display to user saved to favorites songs from the DB.

On click on each entry menu with Delete option should be displayed.

Technical requirements:
-----------------------

 * You can use any frameworks and libraries.
 * Use Android official build tool Gradle.
 * minSdkVersion = 15 (use all advantages of 15 API level)
 * Follow OOP best practices
 * Following Android design and development best practices, good usability is a big plus.

ScreenShot
----------
![ScreenShot](https://raw.github.com/nahidshahin/AppleTopMusic/master/doc/screenshot-3.png)

