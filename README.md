# FreedyMinigameMaker
**description**_:_ Make arcade-games with script system  
**Author**_:_  JongWonLee  
**Contributors**_:_ Monday_blues_, Dixizer, andrewwindsor, HongMint  
**Minecraft Versions**_:_ Spigot(Paper) 1.12 ~ 1.17  
**Contact Developer**_:_ Discord(updated): 종원짱#6938  

## Introduce
You can create MiniGame systems such as Spleef, Tnt Run, and PvP.  
There is a document for direct generation. This plugin is free to share.

---

## Event bundles

When plugin loaded, game file analyze process stuff.  
When certain minecraft event triggered, process bundle executed named event

###  Bundle prefix
There is identifier to read bundle section.  
It is `'on '` and can be changed in settings.yml 

### Example:
```
on Join
    print 'Hello World'
```

Bundle allows space characters. Must be in one column line.  
Capital letters are automatically converted to lowercase letters.  
If you want to mark a text section, use quotation marks.   
The space or tabs before the string is optional.

### Default triggering event list

```
PreJoin: before join server
Join: after join game
PreQuit: before quit game
Quit: after quit game
PreEnd: before game end
End: after game end
Interact: player interact stuffs
```

---

## Known issue
Max game file size is about 1 billion columns.  


---
# Note

settings.yml bundlePrefix 에 on 은 Yaml의 true로 인식해서 따옴표를 붙여야 합니다.