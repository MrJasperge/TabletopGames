## Checkers

### Compute available actions
- for every piece owned by player:
  - calculateCaptures(emptypath)
  - if actions is empty:
    - calculateMoves()
  - return actions

### calculateCaptures(path):
- if no available captures:
  - if path is not empty:
    - add Capture(path) to actions
  - return;
- for every possible capture:
  - make copy of gamestate
  - execute capture in copy
  - make copy of path
  - add capture to pathCopy
  - calculateCaptures(pathCopy)

### calculateMoves()
- for every possible move:
  - add Move() to actions

