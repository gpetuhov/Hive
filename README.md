# Hive: find what you need, offer anything to anyone
Create offers with your current location updated in real-time for people nearby.

https://play.google.com/store/apps/details?id=com.gpetuhov.android.hive

## Description
![Hive screenshots](/img/screens.jpg?raw=true "Hive screenshots")

Hive is the perfect way for people nearby to cooperate and help each other instantly. If you have something to share with others right away, Hive allows you to create offers with your current location tracking, so that your offers are displayed on map exactly where you are in real-time. You can offer anything from food and drinks to taxi rides. Everyone looking for immediate goods and services, can easily search for offers provided by Hive users that are already close to them, instead of shopping and waiting for delivery.

Have you ever had something with you, that you don't really need right now? A book? A chocolate? A snack? A bottle of water? Flowers? Umbrella? Old hat? Instead of throwing it to trash, offer it to people nearby. How? Join Hive! Whether it is things or services, offer something, that you don't need instantly, to others. It is like sharing a taxi ride, but for anything. Don't be shy! You definitely have something to offer to the world!

Ever found yourself in the street craving for a sip of water and no shop around? And there are all those people with bottles in their hands or attached to their backpacks without even noticing it? How to know, if they are willing to share? Join Hive and see if there is someone nearby offering exactly what you are looking for.

There are more than 7 billion people on our planet. Start sharing things and help each other. Let's make the world a more friendly place together!

OFFER ANYTHING TO ANYONE\
Easily create offers for people nearby: just tap Add offer in your Profile and fill in offer title and description and upload offer photos. You can offer anything. Your offers are limited only by your imagination.

FIND OFFERS CLOSE TO YOU\
Find what you need exactly where you need it. See offers displayed on map at their location updated in real-time or arranged in a list. Filter and sort your search results by different criteria.

SEND MESSAGES\
Contact other users by sending messages directly in the app. Browse old messages history in chat archive.

DETAILED CONTACTS\
Publish detailed information about your contacts in profile, so that other users can reach you outside the app as soon as possible.

FAVORITES\
Save your favorite users and offers for future reference.

REVIEWS AND COMMENTS\
Leave reviews on offers provided by other users. Comment on reviews left on your own offers. Build user trust by providing best offers with high average rating.

BADGES FOR ACHIEVEMENTS\
Win badges for different achievements. All your badges are visible to other Hive users. Can you collect them all?

UP AND RUNNING QUICKLY\
Just register with your email, Google or Facebook account and you are ready to go. You can fill all your profile info later.

NO ADS\
Only offers provided by people like you.

Please note that if you have active (visible) offers, Hive constantly tracks your location, which can decrease battery life.

Install the app:\
https://play.google.com/store/apps/details?id=com.gpetuhov.android.hive

Problems using the app? Have a feature request? Willing to share, how exactly you use Hive? Your information will help us improve Hive and make it better!

Feel free to post your user experience, reviews and bug reports on Hive's Facebook page:\
https://www.facebook.com/YouHive

Or send your feedback to email:\
hive.app.feedback@gmail.com

Hive is open source, so you can always explore, what's under the hood, and create issues on Hive's GitHub page:\
https://github.com/gpetuhov/Hive

Want to get early access to the latest features? Join the beta-testing program:\
https://play.google.com/apps/testing/com.gpetuhov.android.hive

## Platform
Android

## Requirements
* Android Studio 3.6 Canary 3
* Kotlin 1.3.40
* Android Gradle Plugin 3.6.0-alpha03
* Gradle wrapper 5.4.1
* AAPT 2

## Architecture
* Clean architecture with Domain, Presentation and UI layers
* MVP and MVVM for the Presentation layer:
    * MVP (Moxy library) for screen rotations
    * MVVM (Google ViewModel) for updating views on data changes
* Firebase Firestore for storing and sharing data
* Firebase Real-time Database for user presence system
* Firebase Cloud Storage for storing media
* Firebase Cloud Functions for backend data handling
* Data Binding (in some views)
* Jetpack Navigation
* AndroidX
* AirBnb Epoxy for complex screens (with carousels)
* Dependency injection with Dagger 2

## Backend
Hive uses Firebase Cloud Functions for backend data handling. Source code can be found in a separate repository:\
https://github.com/gpetuhov/HiveCloud

## FAQ
What is Hive anyway?\
Hive is a mobile app that allows you easily create offers with your current location tracking, so that your offers will be displayed on map exactly where you are in real-time. In Hive your offers will go wherever you go. People nearby can see your offers and get in touch with you inside the app or by contacts you set up in your profile.

Is it another social network?\
No, in Hive there are no posts, no news feed, no friends, no likes. Hive's main purpose is to let users provide their offers to people nearby.

Is it a shopping app?\
No, Hive has been designed for people like you to help each other by sharing what they don't need instantly.

Is Hive a dating app?\
No, while using Hive indeed involves meeting new people, but Hive is focused mainly on offers instead of people, who provide those offers.

How can Hive improve my life?\
If you have something to share, you can use Hive to share it easily with people nearby. If you urgently need something, you can use Hive to search if there is someone close to you, that can satisfy your needs.

What can I offer?\
Anything! Water, snacks, hot coffee in your thermos, disposable rain coat, lighter, phone charger, city map. Your offers are limited only by your imagination. The only thing, you should keep in mind, is that you have to be ready to share what you offer immediately, so you should currently have it with you.

Can I offer only things?\
No, you can offer both things and services. You are great at car repair and ready to help wherever you go? Great, offer it! You are an awesome navigator and can show tourists their way to all city landmarks with your eyes closed? Cool, offer it! You always carry a power bank and willing to share that extra energy with others? Super, offer it!

Can I offer something for free?\
Yes, offers in Hive are free by default.

Can I sell something in Hive?\
Yes, offers can be both free or paid. To create a paid offer you should uncheck Free checkbox and set price.

How do I receive payments for my paid offers?\
Payments are out of Hive's scope. You should discuss payment method directly with your customers.

Can I offer something, that I don't currently have?\
No, Hive has been designed to help you offer something, that you currently have with you. If your offer is no longer available, just uncheck offer's Active checkbox, and the offer will stop showing in search results.

Can I use Hive without offering anything?\
Yes, even if you are not providing any offers, you can still use Hive to search for interesting offers around you.

Is Hive suitable for business?\
Yes, though Hive has been designed for ordinary people to use it everyday, but businesses can also benefit from adapting it to their needs.

How can I use Hive for my business?\
Hive can help you inform nearby people about offers, that your business provides. You may also send your employees in the streets carrying your offers with Hive running on their devices instead of waiting for orders and arranging delivery.

Why do I have to register?\
Registration is required to create your user account. Don't worry, Hive gets you up and running quickly. Just register with your email, Google or Facebook account and you are ready to go. You can fill all your profile info later.

Why Hive requires permissions?\
Hive requires permissions to track your current location and to access media for uploading user and offer photos. Without these permissions Hive won't work.

My search query is empty but I see some markers on map. What are these?\
By default map displays all Hive users with active offers.

Why do I see search results only in the center of the map?\
For better performance maximum search radius is limited to 2 km from the center of the map. This is needed to prevent too much search results when the map is zoomed out. Zooming map out allows you to quickly move the map between cities and countries.

Can I see all Hive users in the world?\
No, you can see only those users and offers that are located no more than 2 km from the center of the currently selected map area.

Markers on map are too close to each other, I can't tap the one I want.\
Try to zoom the map in. If it doesn't help (for example, users are in the same building), show search results in a list by tapping list icon in the upper right corner of the map.

How does search result list correspond to what I see on map?\
Search result list contains only those results, that are displayed on map. If you want to search in another area, you should close search result list and move map to a desired point and zoom level.

I want to see only offers on map, but I see both users and offers.\
You should filter your search results by tapping filter icon in the upper right corner of the map and selecting "Only offers".

Filter and sort icons changed color to red. Why is that?\
This indicates that you have changed filter and sort states, so that they are now different from default.

Why sort button is not available on map?\
Markers on map are tied to their locations and cannot be sorted. You can sort only items in search result list.

Why there are no search results on map?\
There are no users and offers matching your search criteria. Try to clear search filter and make your search query less specified. Or try to clear your search by tapping the cross icon in the upper right corner of the map. If this does not help, then there are no Hive users at all in the selected area. Feel free to invite your friends to join Hive!

How do I move map to my current location?\
Press target icon in the lower right corner of the map. Your location will be marked with a blue point in the center of the map.

How do I see all offers provided by a user?\
Open user details, and scroll to offers section. Notice that you can see only those offers that are currently active.

How is the distance to an offer (user) calculated?\
It is the length of a straight line, that connects a point with your current location with a point where selected offer (user) is.

Allright, I like the offer, how do I order it?\
You should get in touch with the user, who provides this offer, by sending him or her a message inside Hive or by contacts, specified in user's details.

How do I navigate to offer's (user's) current location?\
Scroll down to Location section of user or offer details and tap on map to open Location screen. Then tap on the marker to activate additional controls in the lower right corner of the map and tap on navigate icon to navigate to this destination in Google Maps application.

I have something to share. How do I create an offer?\
Tap "Add offer" in the Offers section of your profile, then fill in offer title and description, upload offer photos and set offer price.

What is active offer?\
Active offers are visible to other users. If you no longer provide an offer, but are planning to provide it in the future, you can simply make it inactive, instead of deleting.

Why can't I make all of my offers active? Hive says "You have reached maximum number of active offers".\
Currently Hive allows users to have only 3 active offers. You can create more offers, but they will be inactive.

I am adding a new offer photo and Hive says "You have reached maximum number of visible photos". Why?\
Currently only first 5 of each offer photos are visible to others users. You can upload more photos, but they will be visible only to you. Same applies also to user photos.

How to delete an offer?\
Tap on the offer in your profile and then tap on trash icon in the upper right corner of the screen. Notice that deleting an offer will also delete all of its reviews and your average rating will change!

I accidentally deleted an offer, what can I do?\
Sorry, this operation cannot be undone.

Why are my offers not visible to other users?\
Please, check if they are active. Only active offers are visible to other users.

Why can't I see my own offers on map?\
Hive allows you to search only for offers provided by other users.

Why can't I set a zero price on my offers?\
Hive does not allow you to set price less than 0.01 USD. If your offer is free you should check Free checkbox instead.

How do I change price currency?\
Currently Hive supports prices in USD only.

What if my offer title is the same as username?\
Hive searches for usernames first. For example, your username is "Burger" (or "BurgerMan") and you have an offer with the title "Burger". Then the results of search query "Burger" will include your user instead of the offer. Consider changing your username to something different.

I have 3 offers with the same title, which one will be available in search results?\
Hive searches for offers from the first (top of Offers section of your profile) to the last (bottom of Offers section) and shows only one offer from each user in search query results. For example, all your offers have the title "Burger". Then the results of search query "Burger" will include your first offer only. Consider creating offers for different things and giving them different titles.

What are the green and red dots close to my offers in the profile?\
Green dot indicates that the offer is active, red dot indicates that the offer is not active.

How do I make myself invisible without deleting the account?\
Just uncheck Active checkbox in all your offers.

Currently I have nothing to offer, should I delete all of my offers?\
No, you can just uncheck Active checkbox in those offers, that are currently not available. You should delete only those offers, that you never plan to provide again in the future. Notice that all offer reviews will also be deleted, which will affect your average user rating.

I unchecked Active offer checkbox and my average user rating changed. Why?\
Your average user rating is calculated based on active offers only.

How often my location on map gets updated?\
Hive updates your current location every minute.

Why can't I dismiss a notification about location sharing?\
This notification is required for Hive to track and share your location in background.

Hive drains my battery. Why?\
As soon as you have active offers, Hive will constantly run in background to track and share your current location, which will affect battery life.

I have no active offers. Will Hive update my location?\
No, Hive does not update user's location if there are no active offers.

Do I have to fill in all profile fields?\
No, but having a detailed profile is fun.

How to delete user or offer photo?\
To delete user photo long-click on it in the profile. To delete offer photo, tap on offer in the profile and then long-click on the photo.

What is the difference between email near user name and email in Contacts section?\
Email near user name in profile (registration email) is not visible to other users. Email in Contacts section is visible to other users. To quickly set registration email as visible email tap "Use registration email" under visible email in Contacts section.

Why should I fill my contacts while users can simply send me messages inside the app?\
Sometimes for other users it is more convenient to make a phone call to get in touch with you, than writing a message and waiting for reply.

What is user activity in the Status section?\
Hive will try to identify, what you are currently doing, and share this information with other users. Options are: still, walking, running, on bicycle, in vehicle.

If I delete my account, will my data be deleted?\
Yes, but chat messages are deleted only if second user in the chat is also deleted.

I accidentally deleted my account, how to restore my data?\
There is no way to restore data for deleted accounts.

Why should I upload user photos if I already uploaded offer photos?\
Offer photos are for offers. With user photos you can tell more about yourself or your business.

I have active offers, but my device has no network connection. Will other users see me?\
No, Hive shows only users that are connected to network and actively share their current location.

Will my data persist if I uninstall Hive?\
Yes, all data is saved on the server and will be available to you when you reinstall Hive and sign in with your user credentials.

What are awards needed for?\
You can receive awards for different achievements. Some awards are received for your own actions, some - for actions other users perform on you or your offers. Your awards are visible to other users and may tell more about your user experience and how you interact with other Hive users. Also awards are used in search filter.

How do I save offers and users for future reference?\
You should add them to Favorite by tapping star icon in the upper right corner of offer or user details.

I pressed yellow star on an offer (or a user) in Favorite tab, and this offer disappeared. How to restore it?\
You have to find this offer (or user) on map or in search result list again and add it to Favorite.

My offers have been reviewed 3 times. Why other users don't see my average user rating?\
You need at least 10 reviews for your average user rating to become visible.

What is the difference between offer rating and favorite star count?\
Offer rating is calculated based on reviews other users leave on this offer. Favorite star count shows how many times other users added this offer to their Favorite.

How to leave a review on an offer?\
Scroll down to Reviews section of the offer details. Tap on "All reviews" or "No reviews yet. Be the first one to review this offer" label and tap on red button in the lower right corner of the screen.

How to leave a review on a user?\
You cannot leave reviews on users, only on their offers.

Why can't I leave a second review on an offer?\
You can leave only one review on each offer.

Why can't I leave a comment on a review?\
Only offer provider (user, who provides the offer) can comment on this offer reviews.

Why can't I leave a second comment?\
If you are the provider of this offer, you can leave only one comment on each review. If you are not, you cannot comment on this offer's reviews at all.

Why can't I comment on a comment?\
Hive does not support this feature.

I opened chat and noticed that my old messages disappeared. Why?\
For better performance chat screen shows only the last 50 messages. To browse your old message history tap Archive in the upper right corner of the chat screen.

How to send a message to a user?\
Tap red button in the lower right corner of user or offer details screen to open chat screen.

What does "online" and "last seen" in user details and in chat mean?\
"Online" means that the user has Hive currently showing on screen. "Last seen" is the latest moment, when Hive was showing on screen.

How do I know that I have new messages?\
When Hive is in the background, you will receive new message notification. When Hive is on screen, icon on Messages tab will change to solid.

I am having Hive on screen. Why don't I receive new message notifications?\
New message notifications are shown only when Hive is in background.

## Credits
These free icons and animations are used in Hive.

#### Icons

For TextMaster, OfferProvider, GoodProvider, ReviewedProvider and Hivecore awards - icons made by [Vectors Market](https://www.flaticon.com/authors/vectors-market) from [www.flaticon.com](https://www.flaticon.com/)

For Altruist, SuperProvider, StoryTeller, MegaCritic, OfferFinder, Oflumbus, RockStar and Favoritizer awards - icons made by [Freepik](https://www.freepik.com/) from [www.flaticon.com](https://www.flaticon.com/)

For Newbie award - icon made by [Pixel perfect](https://www.flaticon.com/authors/pixel-perfect) from [www.flaticon.com](https://www.flaticon.com/)

For ReviewPoster award - icon made by [Roundicons](https://www.flaticon.com/authors/roundicons) from [www.flaticon.com](https://www.flaticon.com/)

For FavoriteProvider and AdorableProvider awards - icons made by [Smashicons](https://www.flaticon.com/authors/smashicons) from [www.flaticon.com](https://www.flaticon.com/)

#### Animations

For TextMaster, Altruist, GoodProvider and OfferFinder awards - animations made by [Hyebin Park](https://lottiefiles.com/smoothy.co) from [LottieFiles](https://lottiefiles.com/)

For OfferProvider award - animation made by [Jan Semler](https://lottiefiles.com/user/141) from [LottieFiles](https://lottiefiles.com/)

For SuperProvider award - animation made by [Lucas Nemo](https://lottiefiles.com/LucasNemo) from [LottieFiles](https://lottiefiles.com/)

For Newbie award - animation made by [Design](https://lottiefiles.com/designSquadRunner) from [LottieFiles](https://lottiefiles.com/)

For HiveCore award - animation made by [Mikhail Golub](https://lottiefiles.com/mihail) from [LottieFiles](https://lottiefiles.com/)

For ReviewPoster award - animation made by [Addy Martinez](https://lottiefiles.com/addymartinez) from [LottieFiles](https://lottiefiles.com/)

For StoryTeller award - animation made by [Victor Winnhed](https://lottiefiles.com/victorw) from [LottieFiles](https://lottiefiles.com/)

For MegaCritic award - animation made by [Anthony Tonev](https://lottiefiles.com/Spinne) from [LottieFiles](https://lottiefiles.com/)

For Oflumbus award - animation made by [Alex Bradt](https://lottiefiles.com/Alexbradt.com) from [LottieFiles](https://lottiefiles.com/)

For FavoriteProvider award - animation made by [Rachmat AG](https://lottiefiles.com/user/57803) from [LottieFiles](https://lottiefiles.com/)

For AdorableProvider award - animation made by [Jonas Alvarson](https://lottiefiles.com/jalvarson) from [LottieFiles](https://lottiefiles.com/)

For RockStar award - animation made by [sandeep pyla](https://lottiefiles.com/user/24960) from [LottieFiles](https://lottiefiles.com/)

For Favoritizer award - animation made by [Erick Daniel Juarez Gil](https://lottiefiles.com/ErickDanielJuarezGil) from [LottieFiles](https://lottiefiles.com/)

For Congratulation - animation made by [Jojo Lafrite](https://lottiefiles.com/jojolafrite) from [LottieFiles](https://lottiefiles.com/)

For Still activity - animation made by [Hoai Le](https://lottiefiles.com/koycatdang) from [LottieFiles](https://lottiefiles.com/)

For Walking activity - animation made by [Anastasiia Vlasenko](https://lottiefiles.com/libertyink) from [LottieFiles](https://lottiefiles.com/)

For Running activity - animation made by [Artem Kazak](https://lottiefiles.com/kazzzak) from [LottieFiles](https://lottiefiles.com/)

For Bicycle activity - animation made by [Yue XI](https://lottiefiles.com/user/1724) from [LottieFiles](https://lottiefiles.com/)

For Vehicle activity - animation made by [Dave Counts](https://lottiefiles.com/davecounts) from [LottieFiles](https://lottiefiles.com/)

For Permissions page and ReviewedProvider award - animations made by [LottieFiles](https://lottiefiles.com/lottiefiles) from [LottieFiles](https://lottiefiles.com/)