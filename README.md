<h1 align="center">
    ExpBottle (Reworked)
</h1>
<p align="center">
Withdraw your XP into bottles
</p>

# Description

This is the forked version of [ExpBottle](https://www.spigotmc.org/resources/98763/) aiming to fix bugs,
improve performance, and add more features.

## What I have done

- %xp% in bottle's lore is no longer a requirement.
- Bottle's display name can be changed.
- Dispenser can throw bottle now.
- A bug where players will get unlimited XP if they use it on off-hand with 'throwable' off

# Features:

> I kept all features. In the future, maybe import new features.

- Withdraw all or a specific amount of experience into a bottle.
- Taxing (if enabled)
- Give a player your Experience Points.
- Supports dispensers.
- Custom aliases:
    * Main command
    * Sub-commands
- Changing bottle's display name

## Commands

> Usage: /expbottle <sub-command>

Available sub-commands:

- `give <player> <amount>`: Give specified player your XP.
- `all`: Extract all XP.
- `<amount>`: Extract a specific amount of XP.
- `reload`: Reload plugin.

## Permissions

| Permission          | Description                                          | 
|---------------------|------------------------------------------------------|
| expbottle.user      | 'give', 'all', and to withdraw specific amount of XP |
| expbottle.admin     | allows 'reload'                                      |
| expbottle.bypasstax | pays zero tax when withdraw                          |

## Placeholders (messages.yml)

| Placeholder           | Description                                                      | 
|-----------------------|------------------------------------------------------------------|
| %playername%          | Player's username                                                |
| %playerdisplayname%   | Player's custom nickname                                         |
| %receivername%        | Receiver's username                                              |
| %receiverdisplayname% | Receiver's custom nickname                                       |
|                       |                                                                  |
| %playerxp%            | Player's current XP                                              |
| %minxp%               | Lowest amount of XP can be withdrawn (configured in config.yml)  |
| %max%                 | Highest amount of XP can be withdrawn (configured in config.yml) |
|                       |                                                                  |
| %xp%                  | The amount of XP will be withdrawn from player                   |
| %tax%                 | Tax percentage                                                   |
| %aftertax%            | The amount of XP player will receive (if tax is enabled)         |

**In help and usage messages. `%cmd%` will be replaced by the command that the player used to execute**

# Issues / Support / Suggestion

> If you have any inquiry about the original work, please contact [plugin's owner](https://github.com/SpillereNO/ExpBottle)

Please visit [Issues](https://github.com/knighthat/ExpBottle/issues) page and open a ticket.

# License

This project is licensed under MIT. You can use, modify, distribute, even use it commercially without any restriction.
