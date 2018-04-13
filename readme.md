# WIP

### Pipelines
Create a specific pipeline to represent an action associated with a data structure. 
Pipelines can be as generic or specific as you wish.
Examples of pipelines could be:

```kotlin
val likeRecipePipeline = RxBroadcaster<Recipe>()
val sendCommentPipeline = RxBroadcaster<Comment>()
```

A pipeline might be too generic if it's responsibilities 
are not clear:

```kotlin
val recipePipeline = RxBroadcaster<Recipe>()
```

A pipeline might be too specific if you need a lot of them to model 
the interaction:
```kotlin
val likeFirstRecipePipeline = RxBroadcaster<Recipe>()
val likeSecondRecipePipeline = RxBroadcaster<Recipe>()
val likeThirdRecipePipeline = RxBroadcaster<Recipe>()
```


## Use cases
### Observe in List Views
For lists views, you should stream in an unfiltered pipeline and then 
update the specific item from the list (and manually update your view after if required)
```kotlin
likeRecipePipeline.stream().subscribe { likedItem ->
    itemList.find{ it.id == likedItem.id}.like()
    // update recyclerview
}
```

### Observe in Detail Views
For single items (detail views) you can stream with a filter, 
that way, you'll only subscribe to events targeted for that specific item
```kotlin
recipeLikePipeline.filter(item.id).stream().subscribe {
    item.like()
    // update view
}

// alternativerly you can do
// recipeLikePipeline.stream(filter=item.id)
```

### Emitting
When emitting events, the best thing is to be as specific as possible 
by always adding a filter, that way both lists or detail views will be 
able to stream and uopdate itelfs
```kotlin
recipeLikePipeline.filter(item.id).emit(item)

// alternativerly you can do
// recipeLikePipeline.emit(item, filter="item.id")
```

### Getting events emitted before subscribing
The Android lifecycle is unpredictable, and sometimes the system might 
kill some activities in the background.  
That means that those activities will no longer be listening for events, 
so when you go back and the activity is recreated from the stack it might 
not be updated.  
In those cases, you might want to setup the `getLast` parameter to `true` when 
creating the listener
```kotlin
recipeLikePipeline.filter(item.id).stream(getLast = true).subscribe {
    // do something
}

// alternativerly you can do
// recipeLikePipeline.stream(filter=item.id, getLast = true)
```
this way you'll always get the latest event emitted in that pipeline 
with that filter
So for example take that you do the following from a list:
- Like recipe A
- Like recipe B
- Like recipe C
- Like recipe D

Then subscribe to that pipeline filtering for B and even though 
events for recipes C and D have been emitted, 
right after subscribing the observable will receive 
the latest event for recipe B so you can update your view.

How much you can go back in history size will depend in the bufferSize, 
that you can setup like this:
```kotlin
val likeRecipePipeline = RxBroadcaster<Recipe>(bufferSize=20) // default is 300
```
#### What about getting all events emmited for all items in a lists?

If you don't add a filter when observing a pipeline you'll still only
get one event (the last one emitted, no matter the filter). 

So if you want to get all the events emmited for all the items in your list
you can just get the whole buffered events and iterate through them.

Keep in mind that *only the last event for a specific filter will be available*, 
so if you liked and then unliked a recipe, only the last of those items (and thus the most updated)
will be there:
```kotlin
recipeLikePipeline.all().subscribe { recipeList ->
    recipeList.forEach { likedRecipe ->
        //do something
    }
}
```

#### Inmutability

We strongly advise you to only emmit immutable models (as Kotlin data classes), 
that way you'll prevent accidentally modifying one item from the history 
causing unexpected behavior, and you'll be sure that the latest emitted 
event within a filter is the most updated.
