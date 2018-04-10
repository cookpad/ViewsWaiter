# WIP

### Pipelines
Create a specific pipeline to represent an action, 
pipelines can be as generic or specific as you wish.
Examples of pipelines could be:

- likeRecipePipeline
- sendCommentPipeline

A pipeline might be too generic if it's responsibilities 
are not clear:

- updateRecipePipeline

A pipeline might be too specific if you need a lot of them to model 
the interaction:

- likeFirstRecipePipeline
- likeSecondRecipePipeline
- likeThirdRecipePipeline


### Use cases
For lists views, you should listen to an unfiltered pipeline and then 
update the specific item from the list (and manually update your view after if required)
```
recipeLikePipeline.listen().subscribe { likedItem ->
    itemList.find{ it.id == likedItem.id}.like()
    // update recyclerview
}
```

For single items (detail views) you can listen with a specific filter, 
that way, you'll only listen for events targeted for that specific item
```
recipeLikePipeline.filter(item.id).listen().subscribe {
    item.like()
    // update view
}
```

When emmiting events, the best is to be as specific as possible 
by always adding a filter, that way both lists or detail views will be 
able to listen and uopdate itelfs
```
recipeLikePipeline.filter(item.id).emit(item)
```

The Android lifecycle is unpredictable, and sometimes the system might 
kill some activities in the background. That means that those activities will
no longer be listening for events, so when you go back and the activity is 
recreated from the stack it might not be updated. 
In those cases, you might want to setup the `getLast` parameter to `true` when 
creating the listener
```
recipeLikePipeline.filter(item.id).listen(getLast = true).subscribe {
    // do something
}
```
this way you'll always get the latest event emitted in that pipeline 
with that filter (depending on the bufferSize).
So for example take that you do the following from a list:
- Like recipe A
- Like recipe B
- Like recipe C
- Like recipe D

Then you go back in the stack to the detail of recipe C, 
that was killed by the system, and listen for events filtered with the recipe ID.

Even though event for recipes C and D have been emitted since the last 
event for recipe B, right after subscribing the observable will receive 
the latest event for recipe B so you can update your view.

What about lists?
If you do
```
recipeLikePipeline.listen(getLast = true).subscribe {
    // do something
}
```
you'll just get the last event emitted in that pipeline, 
so if other events from the list were liked you won't know.
To fix that, you can always get the whole buffered events and iterate through them.
Only the last event for a specific filter will be present, 
so if you liked and unliked a recipe 3 times, only the last of those events (the most updated)
will be there:
```
recipeLikePipeline.getAll().forEach { likedRecipe ->
    //do something
}
```
