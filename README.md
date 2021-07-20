# FreedyMinigameMaker
**description**_:_ Create arcade games with a script system.
**Author**_:_  JongWonLee  
**Contributors**_:_ Monday_blues_, Dixizer, andrewwindsor, HongMint  
**Minecraft Versions**_:_ Spigot(Paper) 1.12 ~ 1.17  
**Contact Developer**_:_ Discord(updated): 종원#6938   
**Note**_:_ A library under the Apache 2.0 license, [jafama's FastMath](https://github.com/jeffhain/jafama), is used for good computational performance. [FastUUID](https://github.com/jchambers/fast-uuid), which is licensed for MIT, is also used for fast performance.  
**Website**_:_ [_Hompage_](https://wiki.freedy.online) 

# T o D o 
custom aliases of process

## Introduce
You can create MiniGame systems such as Spleef, Tnt Run, and PvP.  
There is a document for direct generation. This plugin is free to share.

---

## Event bundles

When plugin loaded, game file analyze process stuff.  
When certain minecraft event triggered, process bundle executed named event

###  Bundle prefix
There is identifier to read bundle section.  
It is `on` and can be changed in settings.yml 

### Example:
```
on join
    print 'Hello World'
```

Bundle allows space characters. Must be in one column line.  
Capital letters are automatically converted to lowercase letters.  
If you want to mark a text section, use quotation marks.   
The space or tabs before the string is optional.  

Simple one line complete is...
```
on join { print 'Hello World' }
```

example...

```
on join {
  //This message is for notes that don't affect. After two slashes.
  
  print "Hello World" //OK!
  
  print 'Hello World' //OK!
  
  print Hello World //Not if Hello is an executable word such as print! Then OK!
  
  print ( "Hello World" ) //OK!
  
  print("Hello World") //BAD! Must have spaces between brackets!!
  
  print ( 5 + 5 ) //OK!
  
  print 5 + 5 //Math operations must be in brackets!
  
  print ( 5 + 5 + 5 ) //BAD!
  
  print ( ( 5 * 2 ) + 10 ) //OK!
  
  print ( "/"Hello World/"" ) //OK! result: "Hello World"
  
}

```

## Processes

### ONLINE, GAME, and PLAYER
aliases:   
    ONLINE: `online, all`  
    PLAYER: `player`  
    GAME: `game`  
It is a modifier for another process.    
For example, if you run `player print "Hello"`, you send a message to the player,  
and if you run `game print "Hello"`, you send a message to all players in the game,  
and if you run `all print "Hello"`, you send a message to all players on the server.    
`player` can be skipped like `print "Hello"`  

### ACTION_BAR

aliases: "sendactionbar", "actionbar"
```
actionbar "Hello World"
game actionbar "Hello World"
all actionbar "Hello World"
```

### Trigger event list

```
Join: When you join the server
#Can't be cancelled
No meta

Left: When I left the server,
#Can't be cancelled
No meta

Pre game join: Before you join the game.
No meta

Game join: When I was in the game,
#Can't be cancelled
No meta

Pre game left: Before you leave the game,
No meta

Game left: When I left the game,
#Can't be cancelled
No meta

Pre game stop: After the last player left
#Can't be cancelled
No meta

Game stop: When it's deactivated because there's no player in the game.
#Can't be cancelled
No meta

Interact: When the player interacts
  data: 
    - interactAction
    - interactHand
    - interactBlockFace
  block:
    - interactBlock
  item:
    - interactItem

Move: When the player moves
  location:
    - moveFrom
    - moveTo  

Chat: When the player chats
  data:
    - chat

Command: When the player executes a command
  data:
    - command

Inventory click: When the player clicks Inventory
  inventory:
    - inventoryClicked
  data:
    - inventoryHotBar
    - inventoryAction
    - inventoryClick
    - inventoryRawSlot
    - inventorySlotType
    - inventorySlot
  item:
    - inventoryCursor
    - inventoryCurrentItem

Inventory close: When the player closes the inventory
#Can't be cancelled
  inventory: 
    - inventoryClosed

Attack: When a player attacks an entity
  data:
    - damage
    - damageCause
    - damageFinal
    - entityUuid


Damage: When the player is damaged by an entity
  data:
    - damage
    - damageCause
    - damageFinal
    - attackerUuid

Player damage: When the player is damaged,
  data:
    - damage
    - damageCause
    - damageFinal
  

Drop item: When the player drops the item.
  item:
    - dropItem


```



---

## Known issue
