# WIP

The main purpose of this library is to create a simple way of **communicating with stacked activities/fragments** (not in the foreground) when the stack is too big or complex to use *onActivityResult* without it becomming very messy.

For example, you can use this library when you have a big stack of activities that contain a representation of an item, and you want to update all those representations when you press a **like** button, so when you go back in the stack all the views are updated.

### Pipelines
Pipelines represent a specific action or behavior associated with a data structure.
Share a pipeline for all the views that need to be aware of that action.  
Try to be specific enough with your pipelines, without creating too many of them.
Examples of pipelines could be:

```kotlin
val likeRecipePipeline = RxBroadcaster<Recipe>()
val sendCommentPipeline = RxBroadcaster<Comment>()
```

A pipeline is too generic if it's responsibilities 
are not clear (it doesn't represent a single action):

```kotlin
val recipePipeline = RxBroadcaster<Recipe>()
```

A pipeline is too specific if you need a lot of them to model 
the interaction:
```kotlin
val likeFirstRecipePipeline = RxBroadcaster<Recipe>()
val likeSecondRecipePipeline = RxBroadcaster<Recipe>()
val likeThirdRecipePipeline = RxBroadcaster<Recipe>()
```

You should define your pipelines globally, as they must be shared amongst all the components that you want to communicate with.
RxBroadcaster uses RxJava2 internally, so just like with observables, events emitted in a pipeline will only be received when subscribing to that same pipeline.
Once you subscribe to a pipeline stream, you should handle the unsubscription by yourself to avoid memory leaks.

## Channels
Pipelines represent an action related with a specific data type (like recipes, send chat messages, etc), but insinde of a specific pipeline you can create channels to get or emmit actions for specific items (like recipe #123,  send chat message to Peter, etc).
A channel is identified by a string, and can be accessed like this
```
val likeRecipePipeline = RxBroadcaster<Recipe>()
val channel = likeRecipePipeline.channel("123")
```
Every further actions performed in a channeled pipeline will be performed inside that specific channel.

## Streams
### List Views
For lists views, you should subscribe to a stream without specifying a channel, so you get all the events emmited in that pipeline, then you can filter and update a specific item from the list manually.
```kotlin
likeRecipePipeline.stream().subscribe { likedItem ->
    itemList.find{ it.id == likedItem.id}.like()
    // update views and data source
}
```

### Detail Views
For single items (detail views) you can get the stream for a specific channel, that way, you'll only subscribe to events targeted for that specific item.
```kotlin
recipeLikePipeline.channel(item.id).stream().subscribe { likedItem ->
    item.liked = likedItem.liked
    // update view
}
```

### Emitting
In a pipeline you can only emit object of a specific type, those objects represent events. 
You can create specific objects to represent behaviors but it's not necessary, as the pipelines already give a representation of that.

Emitting a **recipe** in channel `12345` of the pipeline `recipeLikePipeline` represents the event: The recipe with id `12345` has been liked.

For simple events like those, you can also choose to send the minimum required ammount of data you need to update your models/views, for example if you only need to know if a specific recipe has been liked or unliked, you could send a custom object `class RecipeLikeEvent(id: String, liked: Boolean)` then your pipeline will be `val likeRecipePipeline = RxBroadcaster<RecipeLikeEvent>()`

When emitting events, unless you have a very good reason not to, always emit in a channel, that way both lists and detail views will be able to get the stream and update themselves (because when subscribing to a stream without channel you also get the events emmited in channels).
```kotlin
recipeLikePipeline.channel(item.id).emit(item)
```

#### Inmutability

We strongly advise you to only emmit immutable models (as Kotlin data classes), 
that way you'll prevent accidentally modifying one item from the history 
causing unexpected behavior, and you'll be sure that the latest emitted 
event within a filter is the most updated.
