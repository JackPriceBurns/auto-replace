# Auto Replace

Minecraft Spigot plugin for replacing broken tools and depleted stacks with new ones from your inventory (if you have 
them).

## Replacement Rules

If a tool breaks it will be replaced by the same tool if there is one in your inventory. If there are
multiple spare tools, it will pick the tool with the lowest durability. If there are no tools the same, it will replace
it with a better tool. For example if you had stone, and it broke, it would be replaced with Iron, then Diamond etc.

If you deplete a stack of blocks, it will be replaced by the same stack of blocks if you have that in your inventory. If
there are multiple stacks, it will pick the one with the lowest amount of blocks first.

Things not currently considered:
- Bows
- Thorwables (Snowballs, Ender Pearls, etc)
- Enchantments

Bugs:
- If you have a placeable in your off-hand (like torches) and your main hand is empty, it will move try and move a stack to the empty main hand because it thinks you just depleted your stack.
