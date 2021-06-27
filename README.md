# FreedyMinigameMaker
**description**_:_ Make arcade-games with script system  
**Author**_:_  JongWonLee  
**Contributors**_:_ Monday_blues_, Dixizer, andrewwindsor, HongMint  
**Minecraft Versions**_:_ Spigot(Paper) 1.12 ~ 1.17  
**Contact Developer**_:_ Discord(updated): 종원#6938   
**Download**_:_ [_download latest_](https://github.com/FreedyPlugins/FreedyMinigameMaker2/raw/master/jar/FreedyMinigameMaker2.jar) */* 
[_download release_](https://github.com/FreedyPlugins/FreedyMinigameMaker2/raw/master/jar/FreedyMinigameMaker2.jar)

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

Simple one line complete lambda is...
```
on join -> print 'Hello World'
```

### Trigger event list

```
Join: player join server
Left: player left server
PreGameJoin: before join game
GameJoin: after join game
GamePreLeft: before left game
GameLeft: after left game
GamePreStop: before game end
GameStop: after game end
Interact: player interact stuffs
Move: everytime player move
```

---

## Known issue
Max game file size is about 1 billion columns.  
